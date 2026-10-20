# Stan prac — AutoGłośnik

Data aktualizacji: 2026-10-20

## Aktualna wersja

- Wersja aplikacji: `1.02` (`versionCode 107`).
- Podpis: nowy klucz wydawniczy RSA 4096.
- SHA-256 certyfikatu podpisu:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- Najnowszy artefakt dla użytkownika: `AutoGlosnik-v1.02.apk`.
- Kopia archiwalna: `hasiok/release-artifacts/AutoGlosnik-v1.02.apk`.
- Źródła mają trafić do prywatnego repozytorium `proszczok2002/AutoGlosnik`.
- APK i manifest aktualizacji mają trafić do publicznego repozytorium
  `proszczok2002/AutoGlosnik-Releases` bez kodu źródłowego.

## Wykonane w wersji 1.0

- Dodano dolną nawigację `Strona główna` / `Wsparcie`.
- Dodano dobrowolne wsparcie przez Revolut i PayPal.
- Dodano język polski, angielski, niemiecki i rosyjski.
- Rozszerzono rozpoznawanie etykiet głośnika o język niemiecki i rosyjski.
- Dodano updater GitHub z automatycznym sprawdzaniem i ręcznym przyciskiem.
- Dodano kontrolę HTTPS, SHA-256, nazwy pakietu, rosnącego `versionCode`
  i podpisu APK przed otwarciem instalatora Androida.
- Aktualizacje wymagają potwierdzenia pobrania i osobnego potwierdzenia instalacji.
- Dodano politykę prywatności oraz jawny opis usługi dostępności i uprawnień.
- Ruch nieszyfrowany HTTP jest zablokowany konfiguracją sieciową Androida.

## Wykonane w wersji 1.02

- Zastąpiono ogólny `ACTION_VIEW` bezpośrednią sesją systemowego
  `PackageInstaller`, dzięki czemu Uptodown nie przejmuje instalacji.
- Dodano obsługę potwierdzenia i wyniku systemowej sesji instalacji.
- APK jest ponownie weryfikowany bezpośrednio przed przekazaniem do systemu.
- Dodano `INSTRUKCJA.md` i `JAK_TO_DZIALA.md`.

## Zgodność i migracja

Dawna wersja 1.3.1 była podpisana kluczem debug. Nowa wersja 1.0 używa osobnego
klucza wydawniczego, dlatego na urządzeniu z wersją 1.3.1 trzeba ją jednorazowo
odinstalować przed instalacją 1.0. Kolejne wydania zachowują wspólny podpis i
mogą być instalowane jako aktualizacje.

## Weryfikacja

- `assembleRelease`: zakończone powodzeniem.
- `lintRelease`: zakończone powodzeniem, bez błędów.
- APK 1.0 ma `versionCode 105` i jest podpisany nowym certyfikatem wydawniczym.
- SHA-256 APK 1.0:
  `6E2A67DE9127667E98D1E3C48E1B63BF08F8F1291910A9C6B0F7BDF9DE9D5D8E`.
- `assembleRelease` i `lintRelease` dla wersji 1.01: zakończone powodzeniem.
- APK 1.01 ma `versionCode 106` i ten sam certyfikat co 1.0.
- SHA-256 APK 1.01:
  `022D93EEA103EB9A93FEE297B056A6E460C2BAE6A28E847AF367BB1F2CACD134`.
- `assembleRelease` i `lintRelease` dla wersji 1.02: zakończone powodzeniem.
- APK 1.02 ma `versionCode 107` i ten sam certyfikat co 1.0/1.01.
- SHA-256 APK 1.02:
  `FE84A901AED68C34A7330B2DF9C5645F8D44547BAD4D66EF0946B4268344D5F4`.
- Publiczny zasób wydania został pobrany ponownie z GitHuba: SHA-256, pakiet
  `pl.bumbox.autospeaker`, `versionCode 107`, `versionName 1.02` i certyfikat
  są zgodne z lokalnym artefaktem.
- W chwili weryfikacji nie było podłączonego urządzenia ADB, dlatego końcowy
  test kliknięcia potwierdzenia instalacji musi zostać wykonany na telefonie.

## Publikacja

- Wersja 1.0 została opublikowana w publicznym repozytorium wydań.
- Wersja 1.01 została opublikowana jako aktualizacja testowa.
- Wersja 1.02 została opublikowana jako najnowsze stabilne wydanie:
  <https://github.com/proszczok2002/AutoGlosnik-Releases/releases/tag/v1.02>.
- Publiczny `update.json` wskazuje wersję 1.02 i jej zweryfikowany zasób APK.
- Publiczne repozytorium ma nowy opis, tematy, stronę główną README z logo,
  odnośnikami pobierania, instrukcją, sumami kontrolnymi i ujawnieniem usługi
  dostępności.
- GitHub rozpoznaje szablon zgłoszenia błędu oraz linki wsparcia Revolut/PayPal;
  prywatne zgłaszanie podatności jest włączone.
- Przetestować pełny przepływ instalacji 1.01 → 1.02 na urządzeniu.
