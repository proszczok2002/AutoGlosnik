# Polityka prywatności — AutoGłośnik

Ostatnia aktualizacja: 2026-11-16

## W skrócie

AutoGłośnik nie tworzy kont, nie wyświetla reklam, nie używa analityki i nie
zbiera danych osobowych. Nie nagrywa rozmów, nie zapisuje numerów telefonów ani
zawartości ekranu.

## Usługa dostępności

Usługa dostępności służy wyłącznie do rozpoznania i aktywowania przycisku
głośnika w obsługiwanej aplikacji Telefon podczas trwającej rozmowy. W tym celu
przegląda drzewo interfejsu rozmowy, w którym mogą znajdować się także inne
etykiety, np. numer rozmówcy. Zdarzenia i teksty interfejsu nie są zapisywane
ani wysyłane. Usługa nie zmienia ustawień
systemowych, nie wykonuje działań poza obsługiwanym ekranem rozmowy i może być
w każdej chwili wyłączona przez użytkownika.

## Dane i połączenia sieciowe

Aplikacja przechowuje lokalnie jedynie ustawienie włączenia automatycznego
przełączania, ostatni komunikat stanu oraz techniczny stan rozpoczętej
aktualizacji. Dane te pozostają na urządzeniu i są usuwane razem z aplikacją.

Połączenie z internetem następuje tylko w następujących sytuacjach:

1. pobranie publicznego manifestu `update.json` z `raw.githubusercontent.com`;
2. pobranie wybranego przez użytkownika APK z wydania GitHub;
3. otwarcie przez użytkownika strony Revolut, PayPal lub polityki prywatności.

GitHub, Revolut i PayPal mogą przetwarzać dane techniczne zgodnie z własnymi
politykami prywatności. AutoGłośnik nie otrzymuje od nich danych o wpłatach ani
profilu użytkownika.

## Bezpieczeństwo aktualizacji

Aktualizacje nie są instalowane automatycznie. Użytkownik potwierdza pobranie,
a następnie instalację w systemowym instalatorze Androida. Przed instalacją
aplikacja sprawdza adres HTTPS, SHA-256, identyfikator pakietu, rosnący numer
wersji i zgodność podpisu cyfrowego z zainstalowaną aplikacją.

## Uprawnienia

- Telefon — wyłącznie wykrycie aktywnej rozmowy;
- ustawienia dźwięku — sterowanie trasą rozmowy;
- dostępność — aktywowanie przycisku głośnika podczas rozmowy;
- internet — aktualizacje z GitHub i otwieranie wybranych łączy;
- instalowanie nieznanych aplikacji — tylko po jednorazowej zgodzie Androida
  i świadomej decyzji użytkownika o instalacji pobranego wydania.

## Kontakt i zmiany

Pytania oraz zgłoszenia można przekazywać przez publiczne repozytorium projektu:
<https://github.com/proszczok2002/AutoGlosnik>.

Istotne zmiany tej polityki będą publikowane w repozytorium projektu oraz w
informacjach o nowej wersji.

---

# Privacy Policy — AutoSpeaker

Last updated: 2026-11-16

AutoSpeaker does not create accounts, show ads, use analytics, record calls,
or retain phone numbers or screen content. Its accessibility service is used
to identify and activate the speaker control in a supported Phone app during
an active call. It scans the call interface tree, which can include other labels
such as the caller's number. Interface events and content are not stored or sent.

The app stores only its enabled setting, the latest local status message and
the technical state of an update in progress. Network access is limited to the
public GitHub update manifest and release APK selected by the user, plus links
to Revolut, PayPal and this policy that the user explicitly opens.

Updates are never installed silently. The app validates HTTPS origin, SHA-256,
package identity, increasing version code and the APK signing certificate.
Android presents the final installation confirmation.

Questions can be submitted at
<https://github.com/proszczok2002/AutoGlosnik>.
