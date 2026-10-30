# Historia zmian

## 1.03 — migracja aktualizatora do głównego repozytorium

- Zwiększono `versionCode` z 107 do 108.
- Manifest przyszłych aktualizacji jest pobierany z publicznego repozytorium
  `proszczok2002/AutoGlosnik`.
- Aktualizator ufa wyłącznie zasobom Releases głównego repozytorium.
- Link polityki prywatności prowadzi do głównego repozytorium.
- Zachowano kontrole HTTPS, SHA-256, pakietu, rosnącego numeru wersji i podpisu.
- Zachowano systemowy `PackageInstaller` oraz wymagane potwierdzenie użytkownika.
- Wersja 1.03 jest publikowana także w starym kanale zgodności, aby zainstalowana
  wersja 1.02 mogła bezpiecznie przejść do nowego kanału.

## 1.02 — systemowy instalator aktualizacji

- Usunięto ogólny mechanizm `ACTION_VIEW`, który mógł uruchamiać Uptodown App
  Installer zamiast instalatora systemowego.
- Zweryfikowany APK jest teraz przekazywany bezpośrednio do sesji Android
  `PackageInstaller`.
- Dodano ponowną kontrolę SHA-256, pakietu, wersji i podpisu bezpośrednio przed
  instalacją.
- Dodano pełną instrukcję użytkownika i opis działania projektu.

## 1.01 — test aktualizacji

- Zwiększono `versionCode` z 105 do 106.
- Wydanie służy do pełnego testu aktualizatora 1.0 → 1.01.
- Zachowano ten sam bezpieczny certyfikat podpisu co w wersji 1.0.

## 1.0 — pierwsze publiczne wydanie

- Obsługa Telefonu Google/AOSP i Telefonu Samsung.
- Automatyczne przełączanie słuchawka/głośnik czujnikiem zbliżeniowym.
- Współpraca z TalkBackiem oraz pierwszeństwo zewnętrznych urządzeń audio.
- Interfejs polski, angielski, niemiecki i rosyjski.
- Dolne zakładki `Strona główna` i `Wsparcie`.
- Dobrowolne wsparcie przez Revolut i PayPal.
- Updater GitHub z weryfikacją HTTPS, SHA-256, pakietu, wersji i podpisu.
- Polityka prywatności i jawne wyjaśnienie użycia usługi dostępności.
