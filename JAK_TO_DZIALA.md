# AutoGłośnik — opis działania i utrzymania projektu

## Przepływ działania

1. `MainActivity` prowadzi użytkownika przez uprawnienie Telefon i świadome
   włączenie `AutoSpeakerService` jako usługi dostępności.
2. Usługa reaguje na stan rozmowy oraz odczyt czujnika zbliżeniowego.
3. Podczas aktywnej rozmowy wyszukuje wyłącznie kontrolkę głośnika w
   obsługiwanych pakietach Telefonu Google/AOSP i Samsung.
4. Odsunięcie urządzenia aktywuje kontrolkę głośnika, a zbliżenie przywraca
   słuchawkę. Aktywne zewnętrzne urządzenie audio wstrzymuje automatyzację.
5. Stan działania i diagnostyka są prezentowane lokalnie w aplikacji. Dane
   zdarzeń nie opuszczają telefonu.

## Architektura aktualizatora

Głównym repozytorium projektu jest publiczne
`proszczok2002/AutoGlosnik`. Zawiera kod źródłowy, dokumentację, Issues, Pull
Requesty i kopię najnowszego oficjalnego APK.

Wersja 1.03 pobiera manifest `update.json` i APK z głównego repozytorium
`proszczok2002/AutoGlosnik`. Wersja 1.02 ma jeszcze zapisany adres
`proszczok2002/AutoGlosnik-Releases`. Dlatego podpisane wydanie 1.03 trafia do
obu repozytoriów: stary kanał dostarcza jednorazową migrację urządzeniom z 1.02,
a po instalacji 1.03 kolejne aktualizacje są obsługiwane przez główne repo.

APK przechodzi dwie weryfikacje: po pobraniu oraz bezpośrednio przed utworzeniem
sesji instalacji. Sprawdzane są SHA-256, pakiet `pl.bumbox.autospeaker`, rosnący
`versionCode` i zgodność certyfikatu podpisu. Wersja 1.02 i nowsze przekazują bajty APK
bezpośrednio do systemowej sesji `PackageInstaller`; ogólny `ACTION_VIEW` nie
jest używany, więc zewnętrzny instalator APK nie przejmuje tego etapu. Android
nadal zawsze wymaga decyzji użytkownika.

## Budowanie

Wymagane są JDK 17 i Android SDK 36. W tym katalogu SDK wskazuje
`local.properties`, a Gradle Wrapper utrzymuje wersję narzędzi projektu.

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat lintDebug
.\gradlew.bat testDebugUnitTest
```

Wyniki:

- debug: `app/build/outputs/apk/debug/app-debug.apk`;
- release: `app/build/outputs/apk/release/app-release.apk` — tylko po lokalnym
  skonfigurowaniu oficjalnego podpisu przez właściciela projektu.

Podpis wydawniczy jest ładowany wyłącznie z ignorowanego katalogu `.signing`.
Nie wolno dodawać klucza, pliku właściwości ani haseł do Gita. Utrata klucza
uniemożliwi przygotowanie aktualizacji dla istniejących instalacji.

## Numeracja i publikacja

- każda opublikowana wersja otrzymuje nowy, rosnący `versionCode`;
- `versionName` jest nazwą widoczną dla użytkownika;
- opublikowanego APK nie należy podmieniać pod istniejącym tagiem;
- najpierw publikuje się podpisany zasób APK, a dopiero po zweryfikowaniu jego
  publicznej sumy aktualizuje `update.json`;
- po publikacji trzeba ponownie pobrać publiczny APK i porównać SHA-256, pakiet,
  wersję oraz certyfikat z artefaktem lokalnym.

Wydania 1.0 (`105`), 1.01 (`106`), 1.02 (`107`), 1.03 (`108`) i 1.04 (`109`) należą do jednej linii
podpisu. Stare 1.3.1 było podpisane testowo i wymaga jednorazowej deinstalacji.

## Pliki stanu

- `README.md` — opis projektu i budowanie;
- `INSTRUKCJA.md` — pełna instrukcja użytkownika;
- `PRIVACY.md` — polityka prywatności i uprawnienia;
- `CHANGELOG.md` — historia wydań;
- `STAN_PRAC.md` — aktualny stan techniczny oraz wyniki weryfikacji;
- `CONTRIBUTING.md` — budowanie, standard zmian i proces Pull Requestów;
- `SECURITY.md` — prywatne zgłaszanie podatności i weryfikacja APK;
- `hasiok/` — odzyskiwalne lokalne archiwum; katalog jest ignorowany przez Git.
