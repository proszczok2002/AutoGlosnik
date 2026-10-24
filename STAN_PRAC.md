# Stan prac — AutoGłośnik

Data aktualizacji: 2026-10-24

## Aktualna wersja

- Wersja aplikacji: `1.02` (`versionCode 107`).
- Pakiet: `pl.bumbox.autospeaker`.
- Najnowszy artefakt: `AutoGlosnik-v1.02.apk`.
- SHA-256 APK:
  `FE84A901AED68C34A7330B2DF9C5645F8D44547BAD4D66EF0946B4268344D5F4`.
- SHA-256 certyfikatu podpisu:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- Wydania 1.0 (`105`), 1.01 (`106`) i 1.02 (`107`) korzystają z tej
  samej linii podpisu wydawniczego.

## Repozytorium i licencja

- Główne publiczne repozytorium:
  <https://github.com/proszczok2002/AutoGlosnik>.
- Repozytorium zawiera kod źródłowy, dokumentację, Issues, Pull Requesty oraz
  kopię najnowszego oficjalnego APK w GitHub Releases.
- Projekt jest udostępniony na licencji Apache License 2.0.
- Historia Gita została sprawdzona przed publikacją: nie zawiera klucza
  podpisu, pliku właściwości podpisu, haseł, tokenów ani plików APK.
- Klucz wydawniczy pozostaje lokalnie w ignorowanym katalogu `.signing` i nie
  jest dostępny dla GitHub Actions.
- Każdy push i Pull Request przechodzi workflow: `lintDebug`,
  `testDebugUnitTest` i `assembleDebug`.

## Dystrybucja i aktualizator

- Oficjalne wydanie 1.02 jest dostępne w głównym repozytorium:
  <https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.02>.
- Wersja 1.02 nadal pobiera `update.json` i wskazany APK z
  <https://github.com/proszczok2002/AutoGlosnik-Releases>.
- Repozytorium `AutoGlosnik-Releases` pozostaje publicznym kanałem zgodności
  dla istniejących instalacji i nie może zostać usunięte.
- Publiczny manifest wskazuje wersję 1.02 i zweryfikowany zasób APK.
- Opublikowanego APK nie wolno podmieniać pod istniejącym tagiem.

## Funkcje gotowe w 1.02

- automatyczne przełączanie rozmowy na podstawie czujnika zbliżeniowego;
- obsługa Telefonu Google/AOSP i Telefonu Samsung;
- pierwszeństwo Bluetooth, słuchawek przewodowych/USB i aparatów słuchowych;
- interfejs PL, EN, DE i RU;
- dolne zakładki **Strona główna** i **Wsparcie**;
- dobrowolne wsparcie przez Revolut i PayPal;
- kontrola HTTPS, SHA-256, pakietu, rosnącego `versionCode` i podpisu APK;
- bezpośrednia instalacja przez systemową sesję `PackageInstaller`, bez
  przekazywania pliku do Uptodown ani innego zewnętrznego instalatora;
- dokumentacja użytkownika, prywatności, architektury, bezpieczeństwa i
  współpracy.

## Weryfikacja

- lokalne `lintDebug`: zakończone powodzeniem 2026-10-24;
- lokalne `testDebugUnitTest`: zakończone powodzeniem; projekt nie zawiera
  obecnie testów jednostkowych, więc zadanie miało stan `NO-SOURCE`;
- lokalne `assembleDebug`: zakończone powodzeniem 2026-10-24;
- wcześniejsze `lintRelease` i `assembleRelease` dla 1.02: zakończone
  powodzeniem;
- publiczny APK 1.02 został wcześniej pobrany ponownie i potwierdzono zgodność
  SHA-256, pakietu, `versionCode 107`, `versionName 1.02` i certyfikatu;
- nie było podłączonego urządzenia ADB podczas ostatniej automatycznej
  weryfikacji, dlatego kliknięcie końcowego potwierdzenia instalacji pozostaje
  testem wykonywanym na fizycznym telefonie.

Kontrole kodu i build nie są tym samym co ręczny test TalkBacka lub rozmowy na
urządzeniu. Takie testy należy opisywać osobno w Issue albo Pull Requeście.

## Urządzenia zgłoszone jako przetestowane

Wersja 1.02 została przetestowana na:

- POCO F8 Ultra;
- Samsung Galaxy S25;
- Samsung Galaxy S26 Ultra;
- Samsung Galaxy S23;
- Samsung Galaxy S21;
- Google Pixel 10;
- Google Pixel 11 Pro.

Lista nie jest ograniczeniem zgodności. Aplikacja może działać także na innych
telefonach z Androidem 12 lub nowszym, czujnikiem zbliżeniowym i obsługiwaną
aplikacją Telefon.

## Następne wydanie

Zmiany funkcjonalne wymagają nowego, rosnącego `versionCode`, osobnego tagu,
podpisanego APK oraz aktualizacji manifestu. Przed wskazaniem nowej wersji w
`update.json` trzeba pobrać publiczny zasób i ponownie sprawdzić hash, pakiet,
wersję i certyfikat. Migrację adresu aktualizatora do głównego repozytorium
należy wykonać dopiero w nowej wersji, zachowując działanie już zainstalowanej
1.02.
