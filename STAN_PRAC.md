# Stan prac — AutoGłośnik

Data aktualizacji: 2026-11-13

## Aktualna wersja

- Przygotowana wersja aplikacji: `1.06` (`versionCode 111`).
- Pakiet: `pl.bumbox.autospeaker`.
- Artefakt lokalny: `AutoGlosnik-v1.06.apk`.
- Rozmiar APK: 2185410 bajtów.
- SHA-256 APK:
  `1F8E106F7E40311AB3ACAFDB78BFAD865F50CF05EFB17260D40A62E1779375C6`.
- SHA-256 certyfikatu podpisu:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- APK 1.05 przeniesiono do `hasiok/release-artifacts/`; wcześniejsze wydania
  pozostają na GitHubie. W katalogu głównym są nowy APK oraz dostarczony do
  porównania `AutoGlosnik-v1.2.0-SZYBKI-TALKBACK.apk`.

## Cel wersji 1.06

Przywrócić dla domyślnego Telefonu Google reguły z referencyjnego APK 1.2.0,
niezależnie od producenta telefonu, i zachować aktualną ścieżkę Samsunga dla
Telefonu Samsung. Wykrywanie opiera się na pakiecie domyślnego dialera; jeśli
Android go nie zwraca, wykorzystywany jest zaobserwowany interfejs rozmowy.
Zmiana profilu unieważnia oczekujące próby poprzedniego dialera. Dla Google
starszy wybór przycisku ma pierwszeństwo, a rozszerzone etykiety stanowią
zapasową metodę. Zewnętrzne trasy audio nadal mają pierwszeństwo.

Referencyjny APK: SHA-256
`6DF582606DE8A6F63449B2A4926492CE092DF5EFE8460AE59266F912D2AB07BC`,
`versionCode 102`, `versionName 1.2.0`, pakiet `pl.bumbox.autospeaker`.
Jego certyfikat SHA-256
`C033826456D8A38F662306C5D3253EF0A7B8B4D19582DD50408D0861CD4CC41E`
jest inny od certyfikatu wydań GitHub; instalacja na nim wymaga wcześniejszego
odinstalowania tej wersji testowej.

## Weryfikacja lokalna 1.06

- `testDebugUnitTest`: 4 testy profilu, 0 błędów;
- `lintRelease` i `assembleRelease`: powodzenie, lint: brak problemów;
- APK Signature Scheme v2: poprawny, certyfikat zgodny z wersją 1.05;
- pakiet: `pl.bumbox.autospeaker`, `versionCode 111`, `versionName 1.06`;
- w tej sesji nie było podłączonego urządzenia ADB. Nie potwierdzono jeszcze
  faktycznej rozmowy na Pixelu, innym telefonie Google ani Samsungu z TalkBackiem.

## Cel wersji 1.05

Zakładka Wsparcie nie pokazuje nad przyciskami identyfikatorów ani adresów
Revolut i PayPal. Nazwy usług i przyciski pozostają widoczne, a ich wybranie
otwiera dotychczasowe strony. Adresy są nadal zapisane w kodzie jako cele
przycisków, więc nie jest to ukrywanie danych w samym pakiecie APK.

## Weryfikacja lokalna 1.05

- `lintRelease` i `assembleRelease`: powodzenie, lint: brak problemów;
- `testDebugUnitTest`: zadanie `NO-SOURCE` — brak testów jednostkowych;
- podpis APK Signature Scheme v2: poprawny, certyfikat zgodny z wersją 1.04;
- pakiet: `pl.bumbox.autospeaker`, `versionCode 110`, `versionName 1.05`;
- kontrola kodu widoku Wsparcie: tylko nazwy Revolut i PayPal są tekstem nad
  przyciskami, a przyciski nadal wywołują odpowiednie linki;
- nie było podłączonego urządzenia ADB, więc nie wykonano testu dotykowego
  ani testu TalkBacka na telefonie.

## Publikacja 1.05

- Kod i tag `v1.05` opublikowano w głównym repozytorium.
- [Wydanie 1.05](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.05)
  zawiera podpisany APK i pakiet źródeł.
- Oba zasoby pobrano ponownie przez publiczny HTTPS; rozmiary i SHA-256 są
  identyczne z lokalnymi plikami.
- Główny `update.json` wskazuje APK 1.05; stary kanał zgodności pozostawia
  migracyjną wersję 1.03 dla instalacji 1.02.

## Cel wersji 1.04

Dolne zakładki mają teraz większy obszar dotyku, czytelny tekst, obramowanie
i osobne, kontrastowe tło wybranej pozycji. Dla czytnika ekranu mają nazwy
oraz opis stanu wyboru w czterech językach. W kolejności dostępności są przed
treścią ekranu; nagłówki ekranów i obszar treści są odpowiednio oznaczone.

## Weryfikacja lokalna 1.04

- `lintRelease` i `assembleRelease`: powodzenie, lint: brak problemów;
- `testDebugUnitTest`: powodzenie zadania, stan `NO-SOURCE` — projekt nie ma
  jeszcze testów jednostkowych;
- podpis APK Signature Scheme v2: poprawny, certyfikat zgodny z linią 1.0–1.03;
- pakiet: `pl.bumbox.autospeaker`, `versionCode 109`, `versionName 1.04`;
- w chwili kompilacji nie było podłączonego urządzenia ADB.

Kontrole statyczne nie zastępują testu wizualnego na telefonie ani testu
faktycznych wypowiedzi i nawigacji TalkBacka. Działanie rozmów telefonicznych
nie było ponownie testowane na urządzeniu w tej wersji.

## Publikacja 1.04

- Kod i tag `v1.04` opublikowano w głównym repozytorium.
- [Wydanie 1.04](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.04)
  zawiera podpisany APK oraz pakiet źródeł.
- Oba zasoby pobrano ponownie przez publiczny HTTPS; rozmiary i SHA-256 są
  identyczne z plikami lokalnymi.
- Główny `update.json` wskazuje nowe APK. Stary kanał zgodności nadal dostarcza
  wydanie migracyjne 1.03 dla instalacji 1.02.

## Cel wersji 1.03

Wersja 1.03 jest wydaniem migracyjnym pozwalającym zakończyć podział projektu na
dwa aktywne repozytoria:

- manifest przyszłych aktualizacji:
  `https://raw.githubusercontent.com/proszczok2002/AutoGlosnik/main/update.json`;
- dozwolone APK:
  `/proszczok2002/AutoGlosnik/releases/download/`;
- polityka prywatności:
  `https://github.com/proszczok2002/AutoGlosnik/blob/main/PRIVACY.md`.

Kontrole HTTPS, SHA-256, pakietu, rosnącego `versionCode` i certyfikatu oraz
instalacja przez systemowy `PackageInstaller` nie zostały osłabione. Android
nadal wymaga świadomego potwierdzenia pobrania i instalacji.

## Zrealizowana publikacja 1.03

1. Podpisane APK 1.03 opublikowano w głównym
   [`proszczok2002/AutoGlosnik`](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.03).
2. To samo binarnie identyczne APK opublikowano również w
   [`proszczok2002/AutoGlosnik-Releases`](https://github.com/proszczok2002/AutoGlosnik-Releases/releases/tag/v1.03).
3. Główny `update.json` wskazuje zasób w głównym repozytorium.
4. Stary `update.json` wskazuje zasób 1.03 w starym repozytorium, ponieważ
   wersja 1.02 ufa wyłącznie temu adresowi.
5. Po instalacji 1.03 urządzenie pobiera następne aktualizacje już z głównego
   repozytorium.

Kod wersji 1.03 i główny manifest zapisano w commicie `9950d7b` i tagu
`v1.03`. Stary manifest migracyjny zapisano w commicie `95ff1dc`.

`AutoGlosnik-Releases` nie może zostać usunięte natychmiast po publikacji.
Urządzenia pozostające na 1.02 potrzebują go do znalezienia wersji migracyjnej.
Po fizycznym teście 1.02 → 1.03 i okresie przejściowym repozytorium można
oznaczyć jako archiwalne. Usunięcie pozostawiłoby niemigrowane instalacje bez
aktualizacji.

## Weryfikacja lokalna 1.03

- `clean`: powodzenie;
- `lintDebug`: powodzenie;
- `testDebugUnitTest`: powodzenie, ale projekt nie ma jeszcze testów
  jednostkowych, więc zadanie ma stan `NO-SOURCE`;
- `assembleDebug`: powodzenie;
- `lintRelease`: powodzenie;
- `assembleRelease`: powodzenie;
- weryfikacja APK Signature Scheme v2: powodzenie;
- pakiet: `pl.bumbox.autospeaker`;
- `versionCode 108`, `versionName 1.03`;
- `minSdk 31`, `targetSdk 36`, `compileSdk 36`;
- certyfikat jest identyczny z wersjami 1.0–1.02;
- publiczne APK pobrane z obu repozytoriów mają identyczny rozmiar i SHA-256;
- publiczne APK ma pakiet `pl.bumbox.autospeaker`, `versionCode 108`,
  `versionName 1.03` oraz właściwy certyfikat;
- dokładne publiczne adresy `raw.githubusercontent.com/.../main/update.json`
  używane przez aplikacje zwracają wersję 1.03/108 i właściwe zasoby w
  odpowiednich repozytoriach;
- publiczny pakiet źródeł został pobrany ponownie i ma zgodny hash;
- publiczny pakiet źródeł nie zawiera `.signing`, `local.properties`, APK ani
  plików kluczy;
- Android CI dla commita wydania zakończył się powodzeniem:
  <https://github.com/proszczok2002/AutoGlosnik/actions/runs/41132243042>;
- podczas weryfikacji nie było podłączonego urządzenia ADB.

Kontrole kodu i pliku APK nie zastępują fizycznego testu rozmowy, TalkBacka ani
potwierdzenia instalacji Androida. Test migracji 1.02 → 1.03 na telefonie
pozostaje wymagany przed wyłączeniem starego kanału.

## Repozytorium główne

<https://github.com/proszczok2002/AutoGlosnik>

Repozytorium jest publiczne na licencji Apache 2.0 i zawiera kod, dokumentację,
Issues, Pull Requesty, zasady bezpieczeństwa, Dependabot i Android CI. Klucz
wydawniczy oraz hasła pozostają wyłącznie lokalnie w ignorowanym katalogu
`.signing`.

## Otwarte automatyczne Pull Requesty

- [PR #1](https://github.com/proszczok2002/AutoGlosnik/pull/1) proponuje Gradle
  Wrapper 9.8.2 i ma poprawny workflow.
- [PR #2](https://github.com/proszczok2002/AutoGlosnik/pull/2) proponuje Android
  Gradle Plugin 9.4.1 i samodzielnie nie przechodzi, ponieważ wymaga Gradle co
  najmniej 9.6.0.

Żaden z tych PR-ów nie jest częścią wydania 1.03. Narzędzia pozostają w
zweryfikowanej konfiguracji Gradle 9.4.1 i Android Gradle Plugin 9.2.0.

## Urządzenia bazowo przetestowane dla linii 1.02

- POCO F8 Ultra;
- Samsung Galaxy S25;
- Samsung Galaxy S26 Ultra;
- Samsung Galaxy S23;
- Samsung Galaxy S21;
- Google Pixel 10;
- Google Pixel 11 Pro.

Lista pochodzi z wcześniejszych testów 1.02 i nie jest twierdzeniem, że 1.03
została ponownie sprawdzona na wszystkich tych telefonach.
