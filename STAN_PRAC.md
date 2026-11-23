# Stan prac — AutoGłośnik

Data aktualizacji: 2026-11-23

## Wersja 1.09 — przywrócenie ścieżki Samsunga z 1.07

- W 1.08 użytkownik zgłosił całkowity brak automatycznego przełączania na
  Galaxy S23. Osobny profil pomijał zmianę, gdy nie znalazł jednoznacznej
  kontrolki „Głośnik”. W 1.09 wycofano ten profil i przywrócono wszystkie
  źródła aplikacji z tagu `v1.07`. Zachowano obecne narzędzia budowania.
- Dokumentacja Androida potwierdza, że system sam dobiera mikrofon do
  wyjścia przekazanego do `setCommunicationDevice`. Podobny objaw utraty
  głosu podczas ręcznego przełączania głośnika zgłosił użytkownik S23+,
  również w trybie bezpiecznym. Zgłoszenie nie ustala przyczyny na testowanym
  S23. [Android AudioManager](https://developer.android.com/reference/android/media/AudioManager#setCommunicationDevice(android.media.AudioDeviceInfo)) · [Zgłoszenie S23+](https://www.reddit.com/r/GalaxyS23/comments/16dr6pg/s23_call_issue/)
- Dostępny przez ADB był Pixel 11. Na Galaxy S23 trzeba sprawdzić w zwykłej
  rozmowie, czy automatyczne przełączanie wróciło i czy rozmówca nadal słyszy
  głos po przejściu z głośnika do słuchawki.
- `versionCode 114`, pakiet `pl.bumbox.autospeaker`. Lokalnie przeszły cztery
  testy jednostkowe, lint debug/release oraz kompilacja obu wariantów APK.
  Certyfikat podpisu APK jest zgodny z 1.08.
- [Wydanie 1.09 i sumy kontrolne plików](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.09).
- [GitHub CI dla kodu wydania 1.09](https://github.com/proszczok2002/AutoGlosnik/actions/runs/45170268365).
- Publiczny APK i ZIP są identyczne z lokalnymi. APK ma 2188670 bajtów,
  SHA-256 `d1318a07d06a1a963d457f98de1c327f9261a46cb1bcfdaf0d9916364dbc1b58`.
  Po ich weryfikacji manifest aktualizacji na `main` wskazuje 1.09/114.

## Poprzednie wydanie — 1.08

- `versionCode 113`, pakiet `pl.bumbox.autospeaker`, plik
  `AutoGlosnik-v1.08.apk` (2190746 bajtów), SHA-256
  `3E5B4169CE23AE359AB20486F793339516C218E117B4D66A86D0702480E483F1`.
  Certyfikat podpisu zgodny z 1.07.
- Zgłoszenie dotyczy głosu wysyłanego do rozmówcy po powrocie z głośnika do
  słuchawki. `GalaxyS23CallProfile` korzysta wyłącznie z rozpoznanego przycisku
  „Głośnik” w Telefonie Samsung. Trasę słuchawki i mikrofonu wybiera Telefon
  Samsung. Profil jest wybierany dla `SM-S911…` lub `dm1q` z Telefonem Samsung.
- Reguły Telefonu Google, AOSP i pozostałych modeli z Telefonem Samsung
  pozostają takie jak w 1.07. Dźwięk zewnętrzny zachowuje pierwszeństwo.
- Lokalnie przeszło 7 testów jednostkowych, lint debug/release oraz kompilacja
  obu wariantów APK. Sprawdzono numer pakietu i podpis APK release.
- [Wydanie 1.08 i sumy kontrolne plików](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.08).
- [GitHub CI dla zmian w kodzie 1.08](https://github.com/proszczok2002/AutoGlosnik/actions/runs/45166833797).
- Po weryfikacji pobranego APK publiczny `update.json` kieruje na 1.08/113.
  Kanał migracyjny z wersji 1.02 pozostaje przy 1.03.
- Według późniejszego zgłoszenia 1.08 przestała automatycznie przełączać
  trasę na Galaxy S23. Profil wycofano w 1.09.

## Poprzednie wydanie — 1.07

- Poprawiono bezpieczne marginesy względem systemowych pasków Androida oraz
  przebudowano zakładki na wzór Androidowej nawigacji YouTube Downloadera.
- Uzupełniono [audyt dostępności](AUDYT_DOSTEPNOSCI.md), instrukcję i politykę
  prywatności.
- `versionCode 112`, `versionName 1.07`, podpisany APK:
  `AutoGlosnik-v1.07.apk`, 2190330 bajtów, SHA-256
  `9A89B00ABDE0625C5B5E8931126D6C3684CB66E4CF35A944F7BBBBBE94F9B845`.
- Certyfikat podpisu jest zgodny z 1.06:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- `testDebugUnitTest` (4 testy), `lintDebug`, `lintRelease`, `assembleRelease`:
  powodzenie; oba raporty lint: bez problemów.
- Na Pixelu 11 potwierdzono nakładanie się interfejsu 1.06 na pasek stanu i
  systemową nawigację. Po zatwierdzonej instalacji 1.07 oba ekrany i obie
  zakładki są widoczne ponad przyciskami Androida. Hierarchia UI pokazuje
  granice zakładek do `y=2266` na ekranie 2424 px i właściwy stan zaznaczenia.
  Przy skali czcionki 1,6 podpisy pozostają czytelne; przywrócono skalę 1,0.
  Szczegóły i ograniczenia testu są w audycie dostępności.
- [Wydanie 1.07](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.07)
  i tag opublikowano w głównym repozytorium. Publiczne APK i ZIP źródeł
  pobrano niezależnie: ich rozmiary i SHA-256 są identyczne z lokalnymi.
  ZIP: 122489 bajtów, SHA-256
  `501FF6ECCFF52ECC933376068AB0FFF5C0D90F7DC5F733915DEA4F7C25843CA3`.
- Po publikacji 1.07 publiczny `update.json` wskazywał 1.07/112 i właściwy hash APK;
  manifest migracyjny w
  `AutoGlosnik-Releases` pozostaje przy 1.03 dla wersji 1.02.
- [Android CI dla commita wydania](https://github.com/proszczok2002/AutoGlosnik/actions/runs/43869380190):
  lint, cztery testy i kompilacja debug zakończone powodzeniem.
- Lokalną kopię kontrolną APK 1.06 przeniesiono do `niepotrzebne/`. Szczegóły
  odtworzenia są w sekcji o lokalnych porządkach.

## Poprzednie wydanie — 1.06

- Wcześniejsze wydanie aplikacji: `1.06` (`versionCode 111`).
- Pakiet: `pl.bumbox.autospeaker`.
- Archiwalna kopia APK: `niepotrzebne/20261119T154306688Z-wydanie-1.07/.tools/verify-AutoGlosnik-v1.06.apk`.
- Rozmiar APK: 2185410 bajtów.
- SHA-256 APK:
  `1F8E106F7E40311AB3ACAFDB78BFAD865F50CF05EFB17260D40A62E1779375C6`.
- Publiczny pakiet kodu: `AutoGlosnik-v1.06-source.zip`.
- Rozmiar pakietu kodu: 114401 bajtów.
- SHA-256 pakietu kodu:
  `8B251E81E6131C3AD3E1A971D5FFB5BE3F759BE732E62CC58B99170D1B421379`.
- SHA-256 certyfikatu podpisu:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- APK 1.05 przeniesiono wcześniej do `hasiok/release-artifacts/`. Podczas
  porządków z 2026-11-19 folder `hasiok` nie występował w tym katalogu projektu.
  Wcześniejsze wydania pozostają dostępne na GitHubie.

## Cel wersji 1.06

Przywrócić dla domyślnego Telefonu Google reguły z referencyjnego APK 1.2.0,
niezależnie od producenta telefonu, i zachować aktualną ścieżkę Samsunga dla
Telefonu Samsung. Wykrywanie opiera się na pakiecie domyślnego dialera; jeśli
Android go nie zwraca, wykorzystywany jest zaobserwowany interfejs rozmowy.
Zmiana profilu unieważnia oczekujące próby poprzedniego dialera. Dla Google
starszy wybór przycisku ma pierwszeństwo, a rozszerzone etykiety stanowią
zapasową metodę. Zewnętrzne trasy audio nadal mają pierwszeństwo.

Referencyjny APK: `niepotrzebne/2026-11-19-porzadki/AutoGlosnik-v1.2.0-SZYBKI-TALKBACK.apk`.
SHA-256
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

## Publikacja 1.06

- Kod i tag `v1.06` opublikowano w głównym repozytorium.
- [Wydanie 1.06](https://github.com/proszczok2002/AutoGlosnik/releases/tag/v1.06)
  zawiera podpisany APK i pakiet źródeł.
- Oba pliki pobrano ponownie przez publiczny HTTPS; rozmiary i SHA-256 są
  identyczne z plikami lokalnymi.
- Główny `update.json` wskazuje APK 1.06; kanał zgodności pozostaje przy
  migracyjnej wersji 1.03 dla instalacji 1.02.

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

## Narzędzia budowania na `main`

- Zaktualizowano Gradle Wrapper do 9.8.2. [PR #1](https://github.com/proszczok2002/AutoGlosnik/pull/1)
  · [GitHub CI](https://github.com/proszczok2002/AutoGlosnik/actions/runs/44331055183).
- Po aktualizacji Gradle zaktualizowano Android Gradle Plugin do 9.5.1.
  [PR #3](https://github.com/proszczok2002/AutoGlosnik/pull/3)
  · [GitHub CI](https://github.com/proszczok2002/AutoGlosnik/actions/runs/44331510514).
- Na Windows oba warianty APK zbudowały się poprawnie, lint zakończył się
  bez problemów, a wszystkie cztery testy jednostkowe przeszły. `apksigner`
  potwierdził zgodność certyfikatu lokalnego APK release z certyfikatem 1.07.
  W tym czasie publicznym wydaniem była wersja 1.07 (`versionCode 112`).

## Lokalne porządki

- 2026-11-19 przeniesiono 1309 plików do `niepotrzebne/`, zachowując ścieżki
  względem projektu. Sprawdzono zgodność sum SHA-256 przed przeniesieniem i po nim.
- Główna partia `2026-11-19-porzadki/` zawiera Gradle 9.4.1, stare wyniki
  kompilacji i pamięć podręczną projektu, dawną lokalną kopię repozytorium
  migracyjnego oraz materiały referencyjne i z audytu. `PRZENIESIONE.csv`
  w tej partii wymienia pliki, ich rozmiary i SHA-256. Osobna partia zakończona
  `-wydanie-1.07` zawiera cztery kopie kontrolne APK i ZIP z wersji 1.06 i 1.07.
- Po wydaniu 1.08 poprzednie APK i ZIP 1.07 trafiły do partii `-wydanie-1.08`.
  Sprzątanie jest częścią [procedury wydania](JAK_TO_DZIALA.md#archiwizacja-po-wydaniu).
- Po przeniesieniu plików czysta kompilacja debug, lint i cztery testy
  jednostkowe przeszły bez korzystania ze starego Gradle ani lokalnych wyników
  kompilacji. Skrypt sprawdzono w trybie podglądu i przenoszenia, również przy
  błędnej sumie APK oraz z plikami nadchodzącej wersji.

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
