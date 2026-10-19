package pl.bumbox.autospeaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class UpdateManager {
    private static final String MANIFEST_URL = "https://raw.githubusercontent.com/proszczok2002/AutoGlosnik-Releases/main/update.json";
    private static final String TRUSTED_APK_PREFIX = "/proszczok2002/AutoGlosnik-Releases/releases/download/";
    private static final String MIME_APK = "application/vnd.android.package-archive";
    private static final String PREF_DOWNLOAD_ID = "update_download_id";
    private static final String PREF_SHA256 = "update_sha256";
    private static final String PREF_VERSION_CODE = "update_version_code";
    private static final String PREF_VERSION_NAME = "update_version_name";
    private static final String PREF_FILE_NAME = "update_file_name";
    private static final String PREF_READY = "update_ready";

    private final Activity activity;
    private final DownloadManager downloads;
    private final SharedPreferences preferences;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private TextView statusView;
    private CharSequence lastStatus;
    private boolean closed;
    private boolean receiverRegistered;
    private boolean installDialogShown;
    private boolean installationLaunched;

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            if (id == preferences.getLong(PREF_DOWNLOAD_ID, -2L)) verifyDownloadedUpdate(id);
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    UpdateManager(Activity activity) {
        this.activity = activity;
        downloads = activity.getSystemService(DownloadManager.class);
        preferences = activity.getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE);
        lastStatus = activity.getString(R.string.update_ready_to_check);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        if (Build.VERSION.SDK_INT >= 33) activity.registerReceiver(downloadReceiver, filter, Context.RECEIVER_EXPORTED);
        else activity.registerReceiver(downloadReceiver, filter);
        receiverRegistered = true;
    }

    void bindStatus(TextView view) {
        statusView = view;
        view.setText(lastStatus);
    }

    String currentVersionName() {
        try {
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            return "—";
        }
    }

    void checkForUpdates(boolean userInitiated) {
        if (closed) return;
        updateStatus(activity.getString(R.string.update_checking));
        executor.execute(() -> {
            try {
                UpdateInfo info = fetchUpdateInfo();
                long currentCode = currentVersionCode();
                main.post(() -> {
                    if (closed) return;
                    if (info.versionCode > currentCode) {
                        updateStatus(activity.getString(R.string.update_available_status, info.versionName));
                        showUpdateDialog(info);
                    } else {
                        updateStatus(activity.getString(R.string.update_up_to_date));
                        if (userInitiated) Toast.makeText(activity, R.string.update_up_to_date, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception exception) {
                main.post(() -> {
                    if (closed) return;
                    updateStatus(activity.getString(R.string.update_check_failed));
                    if (userInitiated) Toast.makeText(activity, R.string.update_check_failed, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    void onResume() {
        if (closed) return;
        long id = preferences.getLong(PREF_DOWNLOAD_ID, -1L);
        if (preferences.getBoolean(PREF_READY, false)) {
            if (activity.getPackageManager().canRequestPackageInstalls()) showInstallReadyDialog();
            else updateStatus(activity.getString(R.string.update_waiting_permission));
        } else if (id >= 0L && isDownloadSuccessful(id)) {
            verifyDownloadedUpdate(id);
        }
    }

    void close() {
        closed = true;
        statusView = null;
        executor.shutdownNow();
        if (receiverRegistered) {
            try { activity.unregisterReceiver(downloadReceiver); }
            catch (IllegalArgumentException ignored) { }
            receiverRegistered = false;
        }
    }

    private UpdateInfo fetchUpdateInfo() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(MANIFEST_URL).openConnection();
        connection.setConnectTimeout(12_000);
        connection.setReadTimeout(15_000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "AutoGlosnik-Android-Updater");
        try {
            int response = connection.getResponseCode();
            if (response < 200 || response >= 300) throw new IllegalStateException("HTTP " + response);
            StringBuilder json = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                    if (json.length() > 262_144) throw new IllegalStateException("Manifest too large");
                }
            }
            JSONObject root = new JSONObject(json.toString());
            long code = root.getLong("versionCode");
            String name = root.getString("versionName");
            String url = root.getString("apkUrl");
            String sha = root.getString("sha256").toLowerCase(Locale.ROOT);
            JSONObject notes = root.optJSONObject("notes");
            String lang = Locale.getDefault().getLanguage();
            String note = notes == null ? "" : notes.optString(lang, notes.optString("en", ""));
            validateManifest(code, name, url, sha);
            return new UpdateInfo(code, name, url, sha, note);
        } finally { connection.disconnect(); }
    }

    private void validateManifest(long code, String name, String apkUrl, String sha) {
        Uri uri = Uri.parse(apkUrl);
        boolean trusted = "https".equalsIgnoreCase(uri.getScheme())
                && "github.com".equalsIgnoreCase(uri.getHost())
                && uri.getPath() != null && uri.getPath().startsWith(TRUSTED_APK_PREFIX);
        if (code <= 0L || name.isBlank() || !trusted || !sha.matches("[0-9a-f]{64}"))
            throw new IllegalArgumentException("Invalid update manifest");
    }

    private void showUpdateDialog(UpdateInfo info) {
        if (activity.isFinishing() || activity.isDestroyed()) return;
        String notes = info.notes.isBlank() ? activity.getString(R.string.update_no_notes) : info.notes;
        new AlertDialog.Builder(activity)
                .setTitle(R.string.update_dialog_title)
                .setMessage(activity.getString(R.string.update_dialog_message, info.versionName, notes))
                .setNegativeButton(R.string.not_now, null)
                .setPositiveButton(R.string.download_update, (dialog, which) -> startDownload(info))
                .show();
    }

    private void startDownload(UpdateInfo info) {
        if (downloads == null) { updateStatus(activity.getString(R.string.update_download_failed)); return; }
        String safe = info.versionName.replaceAll("[^0-9A-Za-z._-]", "_");
        String fileName = "AutoGlosnik-v" + safe + ".apk";
        File directory = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (directory == null) { updateStatus(activity.getString(R.string.update_download_failed)); return; }
        File destination = new File(directory, fileName);
        if (destination.exists() && !destination.delete()) { updateStatus(activity.getString(R.string.update_download_failed)); return; }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.apkUrl))
                .setTitle(activity.getString(R.string.update_download_title, info.versionName))
                .setDescription(activity.getString(R.string.update_download_description))
                .setMimeType(MIME_APK).setAllowedOverMetered(true).setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, fileName);
        try {
            long previous = preferences.getLong(PREF_DOWNLOAD_ID, -1L);
            if (previous >= 0L) downloads.remove(previous);
            long id = downloads.enqueue(request);
            preferences.edit().putLong(PREF_DOWNLOAD_ID, id).putString(PREF_SHA256, info.sha256)
                    .putLong(PREF_VERSION_CODE, info.versionCode).putString(PREF_VERSION_NAME, info.versionName)
                    .putString(PREF_FILE_NAME, fileName).putBoolean(PREF_READY, false).apply();
            installDialogShown = false;
            installationLaunched = false;
            updateStatus(activity.getString(R.string.update_downloading, info.versionName));
            Toast.makeText(activity, R.string.update_download_started, Toast.LENGTH_LONG).show();
        } catch (Exception exception) { updateStatus(activity.getString(R.string.update_download_failed)); }
    }

    private boolean isDownloadSuccessful(long id) {
        if (downloads == null) return false;
        try (Cursor cursor = downloads.query(new DownloadManager.Query().setFilterById(id))) {
            if (cursor == null || !cursor.moveToFirst()) return false;
            int index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            return index >= 0 && cursor.getInt(index) == DownloadManager.STATUS_SUCCESSFUL;
        }
    }

    private void verifyDownloadedUpdate(long id) {
        if (downloads == null || closed) return;
        updateStatus(activity.getString(R.string.update_verifying));
        executor.execute(() -> {
            try {
                if (!isDownloadSuccessful(id)) throw new IllegalStateException("Download failed");
                Uri uri = downloads.getUriForDownloadedFile(id);
                if (uri == null) throw new IllegalStateException("Missing URI");
                String expected = preferences.getString(PREF_SHA256, "");
                if (!sha256(uri).equalsIgnoreCase(expected)) throw new VerificationException(R.string.update_integrity_failed);
                String fileName = preferences.getString(PREF_FILE_NAME, "");
                File directory = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                verifyPackage(directory == null ? null : new File(directory, fileName),
                        preferences.getLong(PREF_VERSION_CODE, -1L));
                preferences.edit().putBoolean(PREF_READY, true).apply();
                main.post(() -> { if (!closed) { updateStatus(activity.getString(R.string.update_verified)); requestInstallPermissionOrShowDialog(); } });
            } catch (VerificationException exception) {
                main.post(() -> updateStatus(activity.getString(exception.messageId)));
            } catch (Exception exception) {
                main.post(() -> updateStatus(activity.getString(R.string.update_download_failed)));
            }
        });
    }

    private String sha256(Uri uri) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream input = activity.getContentResolver().openInputStream(uri)) {
            if (input == null) throw new IllegalStateException("Cannot open update");
            byte[] buffer = new byte[32_768];
            int read;
            while ((read = input.read(buffer)) != -1) digest.update(buffer, 0, read);
        }
        StringBuilder result = new StringBuilder(64);
        for (byte value : digest.digest()) result.append(String.format(Locale.ROOT, "%02x", value));
        return result.toString();
    }

    private void verifyPackage(File apk, long expectedCode) throws Exception {
        if (apk == null || !apk.isFile()) throw new VerificationException(R.string.update_package_invalid);
        PackageManager pm = activity.getPackageManager();
        PackageInfo archive = pm.getPackageArchiveInfo(apk.getAbsolutePath(), PackageManager.GET_SIGNING_CERTIFICATES);
        PackageInfo installed = pm.getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
        if (archive == null || !activity.getPackageName().equals(archive.packageName)
                || archive.getLongVersionCode() != expectedCode
                || archive.getLongVersionCode() <= installed.getLongVersionCode())
            throw new VerificationException(R.string.update_package_invalid);
        Signature[] incoming = archive.signingInfo == null ? new Signature[0] : archive.signingInfo.getApkContentsSigners();
        Signature[] trusted = installed.signingInfo == null ? new Signature[0] : installed.signingInfo.getApkContentsSigners();
        if (!sameSigner(incoming, trusted)) throw new VerificationException(R.string.update_signature_failed);
    }

    private static boolean sameSigner(Signature[] first, Signature[] second) {
        if (first.length == 0 || second.length == 0) return false;
        for (Signature candidate : first) for (Signature trusted : second)
            if (Arrays.equals(candidate.toByteArray(), trusted.toByteArray())) return true;
        return false;
    }

    private void requestInstallPermissionOrShowDialog() {
        if (activity.getPackageManager().canRequestPackageInstalls()) { showInstallReadyDialog(); return; }
        if (activity.isFinishing() || activity.isDestroyed()) return;
        new AlertDialog.Builder(activity).setTitle(R.string.unknown_sources_title)
                .setMessage(R.string.unknown_sources_message).setNegativeButton(R.string.not_now, null)
                .setPositiveButton(R.string.open_settings, (dialog, which) -> activity.startActivity(
                        new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:" + activity.getPackageName())))).show();
    }

    private void showInstallReadyDialog() {
        if (installDialogShown || installationLaunched || activity.isFinishing() || activity.isDestroyed()) return;
        installDialogShown = true;
        String version = preferences.getString(PREF_VERSION_NAME, "");
        new AlertDialog.Builder(activity).setTitle(R.string.update_ready_title)
                .setMessage(activity.getString(R.string.update_ready_message, version))
                .setNegativeButton(R.string.not_now, (dialog, which) -> installDialogShown = false)
                .setPositiveButton(R.string.install_update, (dialog, which) -> { installDialogShown = false; launchInstaller(); })
                .setOnCancelListener(dialog -> installDialogShown = false).show();
    }

    private void launchInstaller() {
        long id = preferences.getLong(PREF_DOWNLOAD_ID, -1L);
        Uri uri = downloads == null ? null : downloads.getUriForDownloadedFile(id);
        if (uri == null) { updateStatus(activity.getString(R.string.update_package_invalid)); return; }
        try {
            Intent install = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, MIME_APK)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installationLaunched = true;
            activity.startActivity(install);
        } catch (Exception exception) {
            installationLaunched = false;
            updateStatus(activity.getString(R.string.update_install_failed));
        }
    }

    private long currentVersionCode() throws PackageManager.NameNotFoundException {
        return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).getLongVersionCode();
    }

    private void updateStatus(CharSequence value) {
        lastStatus = value;
        if (statusView != null) statusView.setText(value);
    }

    private static final class UpdateInfo {
        final long versionCode; final String versionName; final String apkUrl; final String sha256; final String notes;
        UpdateInfo(long code, String name, String url, String sha, String notes) {
            versionCode = code; versionName = name; apkUrl = url; sha256 = sha; this.notes = notes;
        }
    }

    private static final class VerificationException extends Exception {
        final int messageId;
        VerificationException(int messageId) { this.messageId = messageId; }
    }
}
