# AutoGłośnik

AutoGłośnik automatycznie przełącza dźwięk zwykłej rozmowy telefonicznej między
słuchawką a głośnikiem na podstawie czujnika zbliżeniowego. Obsługuje Telefon
Google/AOSP i Telefon Samsung. Nie zastępuje dialera i nie nagrywa rozmów.

## Funkcje

- automatyczne przełączanie: przy uchu — słuchawka, po odsunięciu — głośnik;
- zgodność z TalkBackiem i czytnikami ekranu;
- pierwszeństwo Bluetooth, słuchawek przewodowych/USB i aparatów słuchowych;
- interfejs po polsku, angielsku, niemiecku i rosyjsku;
- zakładki `Strona główna` i `Wsparcie`;
- dobrowolne wsparcie przez Revolut lub PayPal;
- aktualizacje z publicznego repozytorium wydań GitHub;
- kontrola HTTPS, SHA-256, nazwy pakietu, rosnącego `versionCode` i podpisu APK
  przed uruchomieniem instalatora Androida;
- bezpośrednie przekazanie zweryfikowanego APK do systemowego `PackageInstaller`,
  bez wybierania zewnętrznego instalatora APK.

Pełna instrukcja instalacji, konfiguracji, testu i aktualizacji znajduje się w
[INSTRUKCJA.md](INSTRUKCJA.md). Opis techniczny projektu jest w
[JAK_TO_DZIALA.md](JAK_TO_DZIALA.md).

## Ważne ujawnienie usługi dostępności

AutoGłośnik używa ograniczonej usługi dostępności podczas trwającej rozmowy,
aby znaleźć i aktywować istniejący przycisk głośnika w obsługiwanej aplikacji
Telefon. Usługa jest ograniczona w konfiguracji do pakietów Telefonu Google,
AOSP i Samsunga. Aplikacja nie zapisuje, nie przesyła i nie analizuje treści
ekranu. Użytkownik sam włącza i może w każdej chwili wyłączyć usługę w
ustawieniach Androida.

## Uprawnienia

- `READ_PHONE_STATE` — sprawdza, czy trwa rozmowa; nie odczytuje numerów ani
  historii połączeń;
- `MODIFY_AUDIO_SETTINGS` — wspomaga wybór trasy dźwięku rozmowy;
- usługa dostępności — aktywuje przycisk głośnika wyłącznie w obsługiwanym
  interfejsie rozmowy;
- `INTERNET` — łączy się wyłącznie z GitHub w celu sprawdzania i pobierania
  aktualizacji oraz po wybraniu zewnętrznego łącza wsparcia;
- `REQUEST_INSTALL_PACKAGES` — pozwala otworzyć instalator wyłącznie po
  świadomym wybraniu aktualizacji przez użytkownika. Nie ma cichej instalacji.

Szczegóły opisuje [polityka prywatności](PRIVACY.md).

## Budowanie

Projekt wymaga Android SDK 36 oraz JDK 17 lub nowszego. Lokalne SDK jest
skonfigurowane w `.tools/android-sdk`.

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat lintRelease
.\gradlew.bat assembleRelease
```

APK debug powstaje w `app/build/outputs/apk/debug/app-debug.apk`, a podpisane
APK wydawnicze w `app/build/outputs/apk/release/app-release.apk`.

## Podpis wydawniczy

Klucz i hasło są przechowywane lokalnie w ignorowanym katalogu `.signing` i
nie mogą trafić do GitHuba. Utrata pliku `autoglosnik-release.p12` uniemożliwi
publikowanie aktualizacji instalowanych na istniejącej wersji. Należy wykonać
bezpieczną kopię zapasową całego katalogu `.signing` poza repozytorium.

Wersja 1.0 rozpoczyna nową linię podpisu wydawniczego. Dawna wersja 1.3.1 była
podpisana kluczem debug, dlatego przed pierwszą instalacją 1.0 trzeba ją
jednorazowo odinstalować. Aktualizacje 1.0 → 1.01 → 1.02 zachowują już wspólny,
bezpieczny podpis.

## Dystrybucja i aktualizacje

- źródła: prywatne repozytorium `proszczok2002/AutoGlosnik`;
- APK, dokumentacja i `update.json`: publiczne repozytorium
  `proszczok2002/AutoGlosnik-Releases`;
- manifest aktualizacji nie zawiera sekretów ani tokenów;
- aplikacja nie uzyskuje dostępu do prywatnego repozytorium.

Wbudowany updater jest przeznaczony dla dystrybucji bezpośredniej przez GitHub.
Wersja przeznaczona do Google Play musiałaby korzystać z mechanizmu aktualizacji
Google Play i nie może używać tego mechanizmu samodzielnej instalacji.

Numeracja pierwszych wydań tej linii to `1.0` (`versionCode 105`), testowa
aktualizacja `1.01` (`versionCode 106`) oraz poprawka instalatora `1.02`
(`versionCode 107`). Android porównuje rosnący `versionCode`, natomiast
`versionName` jest nazwą widoczną dla użytkownika.

## Wsparcie

- Revolut: <https://revolut.me/r_rosiejka>
- PayPal: <https://paypal.me/proszczok>

Wsparcie jest całkowicie dobrowolne i nie odblokowuje żadnych funkcji.
