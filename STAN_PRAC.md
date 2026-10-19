# Stan prac — AutoGłośnik

Data aktualizacji: 2026-10-19

## Aktualna wersja

- Wersja aplikacji: `1.01` (`versionCode 106`).
- Podpis: nowy klucz wydawniczy RSA 4096.
- SHA-256 certyfikatu podpisu:
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.
- Artefakt lokalny: `hasiok/release-artifacts/AutoGlosnik-v1.01.apk`.
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

## Publikacja

- Wersja 1.0 została opublikowana w publicznym repozytorium wydań.
- Wersja 1.01 jest zbudowana i gotowa do publikacji jako aktualizacja testowa.
- Po publikacji `update.json` ma wskazywać wersję 1.01.
- Przetestować pełny przepływ instalacji 1.0 → 1.01 na urządzeniu.
