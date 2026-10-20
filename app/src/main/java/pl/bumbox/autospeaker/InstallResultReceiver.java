package pl.bumbox.autospeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Build;
import android.widget.Toast;

public final class InstallResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!UpdateManager.ACTION_INSTALL_STATUS.equals(intent.getAction())) return;

        int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS,
                PackageInstaller.STATUS_FAILURE);
        if (status == PackageInstaller.STATUS_PENDING_USER_ACTION) {
            Intent confirmation;
            if (Build.VERSION.SDK_INT >= 33) {
                confirmation = intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent.class);
            } else {
                @SuppressWarnings("deprecation")
                Intent legacy = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                confirmation = legacy;
            }
            if (confirmation != null) {
                try {
                    confirmation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(confirmation);
                    return;
                } catch (Exception ignored) { }
            }
            Toast.makeText(context, R.string.update_install_failed, Toast.LENGTH_LONG).show();
            return;
        }

        if (status == PackageInstaller.STATUS_SUCCESS) {
            UpdateManager.clearStoredUpdateState(context);
            Toast.makeText(context, R.string.update_install_success, Toast.LENGTH_LONG).show();
        } else {
            context.getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE).edit()
                    .putBoolean(UpdateManager.PREF_READY, true)
                    .apply();
            Toast.makeText(context, R.string.update_install_failed, Toast.LENGTH_LONG).show();
        }
    }
}
