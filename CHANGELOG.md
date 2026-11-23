# Historia zmian

## 1.09 — przywrócenie obsługi Galaxy S23 z 1.07

- Usunięto profil Galaxy S23 z 1.08 po zgłoszeniu, że telefon przestał
  automatycznie przełączać między głośnikiem a słuchawką.
- Galaxy S23 z Telefonem Samsung ponownie korzysta ze wspólnej ścieżki
  Samsunga z 1.07. Wcześniejsze zgłoszenie utraty głosu po powrocie do
  słuchawki pozostaje otwarte.
- Zwiększono `versionCode` do 114.

## 1.08 — profil rozmów dla Galaxy S23

- Na Galaxy S23 z Telefonem Samsung AutoGłośnik przełącza trasę przez rozpoznany
  przycisk „Głośnik”, a Telefon Samsung dobiera słuchawkę i mikrofon.
- Zmiana dotyczy zgłoszenia utraty głosu po powrocie z głośnika do słuchawki.
  Test rozmowy na Galaxy S23 pozostaje do wykonania.
- Profil S23 jest oddzielony od profili Telefonu Google, AOSP i pozostałych
  modeli z Telefonem Samsung.
- Zwiększono `versionCode` do 113.

## 1.07 — bezpieczne marginesy i dostępniejsze zakładki

- Odsunięto nagłówek i dolne zakładki od systemowych pasków Androida oraz wycięć ekranu.
- Dolne zakładki mają ikony z podpisami i wyróżnioną aktywną pozycję, na wzór
  androidowej nawigacji w YouTube Downloaderze. Przy dużej czcionce używają
  szerszych przycisków z czytelnym tekstem.
- Zachowano nazwy, stany wyboru i kolejność dla TalkBacka; przywracana jest
  otwarta zakładka po zmianie konfiguracji.
- Doprecyzowano opis usługi dostępności, politykę prywatności i informację
  o wykrytej aplikacji Telefon.
- Zwiększono `versionCode` z 111 do 112.

## 1.06 — osobne ścieżki dla Telefonu Google i Samsunga

- Wykrywanie sposobu obsługi rozmowy opiera się na domyślnej aplikacji Telefon,
  więc ścieżka Google działa także na urządzeniach innych producentów z Telefonem Google.
- Dla Telefonu Google przywrócono kolejność wyboru interfejsu i wyszukiwania
  przycisku głośnika ze sprawdzonego APK 1.2.0; rozszerzone etykiety pozostają
  zapasową metodą dla innych języków.
- Dla Telefonu Samsung zachowano aktualną ścieżkę z jego interfejsem rozmowy,
  gestem i opóźnieniem dla TalkBacka.
- Zablokowano wykonywanie oczekujących prób przełączenia po zmianie wykrytego
  profilu aplikacji Telefon.
- Zwiększono `versionCode` z 110 do 111.

## 1.05 — dyskretniejsza zakładka Wsparcie

- Usunięto z widoku Wsparcie adresy i identyfikatory kont nad przyciskami Revolut i PayPal.
- Przyciski nadal otwierają właściwe strony usług po ich wybraniu.
- Zwiększono `versionCode` z 109 do 110.

## 1.04 — czytelniejsze i łatwiej dostępne zakładki

- Zwiększono dolne przyciski nawigacji i ich tekst oraz dodano wyraźne tło i obramowanie.
- Wybrana zakładka ma osobny, kontrastowy wygląd; jej stan jest podawany czytnikowi ekranu w czterech językach.
- Ustawiono zakładki przed treścią w kolejności dostępności, oznaczono nagłówki ekranów i nazwano obszary treści.
- Zwiększono `versionCode` z 108 do 109.

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
