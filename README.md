# AutoGłośnik

<p align="center">
  <img src="assets/autoglosnik-icon.svg" width="128" height="128" alt="Logo AutoGłośnika">
</p>

<p align="center">
  <a href="https://github.com/proszczok2002/AutoGlosnik/releases/latest"><img alt="Najnowsze wydanie" src="https://img.shields.io/github/v/release/proszczok2002/AutoGlosnik?display_name=release&label=wersja"></a>
  <a href="https://github.com/proszczok2002/AutoGlosnik/actions/workflows/android.yml"><img alt="Android CI" src="https://github.com/proszczok2002/AutoGlosnik/actions/workflows/android.yml/badge.svg"></a>
  <img alt="Android 12+" src="https://img.shields.io/badge/Android-12%2B-3DDC84?logo=android&logoColor=white">
  <a href="LICENSE"><img alt="Licencja Apache 2.0" src="https://img.shields.io/badge/licencja-Apache%202.0-blue"></a>
</p>

<p align="center">
  <strong>Automatyczne przełączanie rozmowy między słuchawką i głośnikiem.</strong>
</p>

<p align="center">
  <a href="https://github.com/proszczok2002/AutoGlosnik/releases/latest"><strong>Pobierz najnowsze APK</strong></a>
  ·
  <a href="INSTRUKCJA.md">Instrukcja</a>
  ·
  <a href="https://github.com/proszczok2002/AutoGlosnik/issues">Zgłoś problem lub pomysł</a>
</p>

AutoGłośnik przełącza dźwięk zwykłej rozmowy telefonicznej na podstawie czujnika
zbliżeniowego: przy uchu pozostawia słuchawkę, a po odsunięciu telefonu włącza
głośnik. Obsługuje Telefon Google/AOSP i Telefon Samsung. Nie zastępuje dialera,
nie nagrywa rozmów i nie odczytuje ich treści.

Kod źródłowy, dokumentacja i oficjalne APK są publiczne w tym repozytorium.
Możesz zgłosić błąd, zaproponować zmianę, utworzyć fork i wysłać Pull Request.

## Najważniejsze funkcje

- automatyczne przełączanie: przy uchu — słuchawka, po odsunięciu — głośnik;
- pierwszeństwo Bluetooth, słuchawek przewodowych/USB i aparatów słuchowych;
- interfejs po polsku, angielsku, niemiecku i rosyjsku;
- wyraźne dolne zakładki **Strona główna** i **Wsparcie** z dużymi przyciskami;
- nazwy, stan wyboru i kolejność zakładek udostępnione TalkBackowi i innym
  czytnikom ekranu;
- aktualizacje z GitHuba, zatwierdzane przez użytkownika;
- weryfikacja HTTPS, SHA-256, pakietu, rosnącego `versionCode` i podpisu APK;
- instalacja przez systemowy `PackageInstaller`, bez przekazywania pliku do
  zewnętrznego instalatora APK;
- brak reklam, kont i analityki.

## Zgodność

Wymagany jest Android 12 lub nowszy, czujnik zbliżeniowy oraz Telefon
Google/AOSP albo Telefon Samsung.

Wersja 1.02 została przetestowana na:

- POCO F8 Ultra;
- Samsung Galaxy S25;
- Samsung Galaxy S26 Ultra;
- Samsung Galaxy S23;
- Samsung Galaxy S21;
- Google Pixel 10;
- Google Pixel 11 Pro.

Lista nie jest zamknięta — aplikacja może działać także na innych zgodnych
telefonach. Rzeczywiste działanie zależy m.in. od wersji aplikacji Telefon,
nakładki producenta i sposobu udostępnienia przycisku głośnika.

## Instalacja

1. Otwórz [najnowsze wydanie](https://github.com/proszczok2002/AutoGlosnik/releases/latest).
2. Pobierz plik `AutoGlosnik-v*.apk`.
3. Porównaj SHA-256 z wartością podaną w opisie wydania.
4. Otwórz APK i potwierdź instalację w Androidzie.
5. W aplikacji przyznaj uprawnienie Telefon i świadomie włącz usługę
   dostępności AutoGłośnika.

Pełna instrukcja instalacji, konfiguracji, testowania i aktualizacji znajduje
się w [INSTRUKCJA.md](INSTRUKCJA.md).

## Budowanie ze źródeł

### Wymagania

- JDK 17;
- Android SDK 36;
- system Windows, Linux lub macOS;
- połączenie z internetem przy pierwszym pobraniu zależności Gradle.

Sklonuj repozytorium:

```bash
git clone https://github.com/proszczok2002/AutoGlosnik.git
cd AutoGlosnik
```

Na Windows:

```powershell
.\gradlew.bat lintDebug testDebugUnitTest assembleDebug
```

Na Linuxie lub macOS:

```bash
./gradlew lintDebug testDebugUnitTest assembleDebug
```

Gotowy wariant deweloperski znajduje się w
`app/build/outputs/apk/debug/app-debug.apk`.

Klucz wydawniczy i hasła nie są częścią repozytorium ani CI. Oficjalne wydania
są podpisywane poza GitHub Actions, a Pull Requesty budują bezpieczny wariant
debug. Nie dodawaj kluczy, plików `local.properties`, katalogów `build` ani
plików APK do commita.

## Współpraca i Pull Requesty

Szczegółowy proces opisuje [CONTRIBUTING.md](CONTRIBUTING.md). W skrócie:

1. utwórz fork i osobną gałąź;
2. wykonaj małą, opisaną zmianę;
3. uzupełnij wszystkie cztery wersje językowe tekstów widocznych w aplikacji;
4. uruchom lint, testy i build debug;
5. opisz sposób weryfikacji i wyślij Pull Request do gałęzi `main`.

Automatyczny workflow sprawdza każdy Pull Request. Błędy i pomysły zgłaszaj w
[Issues](https://github.com/proszczok2002/AutoGlosnik/issues), a podatności
zgodnie z [SECURITY.md](SECURITY.md).

## Dostępność i prywatność

Ograniczona usługa dostępności działa podczas rozmowy i wyszukuje istniejący
przycisk głośnika wyłącznie w obsługiwanych aplikacjach Telefon. Treść ekranu
nie jest zapisywana ani wysyłana. Użytkownik sam włącza usługę i może ją w
każdej chwili wyłączyć.

Uprawnienie `INTERNET` służy do sprawdzania i pobierania aktualizacji z GitHuba
oraz otwierania wybranego przez użytkownika linku do polityki prywatności lub
wsparcia. Szczegóły: [PRIVACY.md](PRIVACY.md).

## Aktualizator i migracja repozytorium

To repozytorium jest głównym miejscem kodu, wydań APK, zgłoszeń i Pull
Requestów. Wersja 1.03 i nowsze pobierają manifest oraz APK bezpośrednio z
`proszczok2002/AutoGlosnik`.

Wersja 1.02 nadal odczytuje manifest z publicznego repozytorium
[`AutoGlosnik-Releases`](https://github.com/proszczok2002/AutoGlosnik-Releases).
Wydanie 1.03 jest więc publikowane w obu miejscach. Stare repozytorium pozostaje
tymczasowym mostem dla urządzeń z wersją 1.02 i nie może zostać usunięte przed
zakończeniem migracji.

Opis architektury i procedury wydawniczej: [JAK_TO_DZIALA.md](JAK_TO_DZIALA.md).

## Dokumentacja

- [Instrukcja użytkownika](INSTRUKCJA.md)
- [Opis techniczny i utrzymanie](JAK_TO_DZIALA.md)
- [Historia zmian](CHANGELOG.md)
- [Stan projektu i wyniki weryfikacji](STAN_PRAC.md)
- [Polityka prywatności](PRIVACY.md)
- [Zasady współpracy](CONTRIBUTING.md)
- [Zasady bezpieczeństwa](SECURITY.md)

## Wsparcie

- Revolut: <https://revolut.me/r_rosiejka>
- PayPal: <https://paypal.me/proszczok>

Wsparcie jest całkowicie dobrowolne, nie odblokowuje funkcji i nie wpływa na
możliwość korzystania z kodu na warunkach licencji.

## Licencja

Copyright 2026 proszczok2002.

Projekt jest udostępniony na licencji [Apache License 2.0](LICENSE). Możesz go
używać, modyfikować i rozpowszechniać zgodnie z jej warunkami. Zachowaj
informacje o prawach autorskich, licencji oraz plik `NOTICE`.

---

**English summary:** AutoSpeaker is an open-source Android 12+ app that switches
an active phone call between the earpiece and speaker using the proximity
sensor. It supports Google/AOSP Phone and Samsung Phone, includes PL/EN/DE/RU
locales, and accepts issues and Pull Requests. See the installation and build
sections above or open an issue if you need help.
