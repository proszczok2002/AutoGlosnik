# AutoGłośnik — pełna instrukcja użytkownika

## 1. Do czego służy program

AutoGłośnik automatycznie przełącza dźwięk zwykłej rozmowy telefonicznej:

- gdy telefon jest przy uchu — rozmowa pozostaje w słuchawce;
- po odsunięciu telefonu — włącza się głośnik;
- po ponownym zbliżeniu telefonu do ucha — dźwięk wraca do słuchawki.

Aplikacja współpracuje z Telefonem Google/AOSP i Telefonem Samsung. Nie jest
dialerem, nie wykonuje połączeń, nie nagrywa rozmów i nie odczytuje ich treści.
Bluetooth, słuchawki przewodowe/USB i aparaty słuchowe mają pierwszeństwo — gdy
są aktywne, AutoGłośnik nie wymusza głośnika telefonu.

## 2. Wymagania

- Android 12 lub nowszy;
- telefon z czujnikiem zbliżeniowym;
- Telefon Google/AOSP albo Telefon Samsung;
- połączenie z internetem tylko do sprawdzania i pobierania aktualizacji;
- świadomie włączona usługa dostępności AutoGłośnika.

Język interfejsu jest dobierany automatycznie do języka systemu. Dostępne są:
polski, angielski, niemiecki i rosyjski.

## 3. Pierwsza instalacja

1. Otwórz oficjalne wydania:
   <https://github.com/proszczok2002/AutoGlosnik/releases/latest>.
2. Pobierz najnowszy plik `AutoGlosnik-v*.apk`.
3. Otwórz pobrany plik i zatwierdź instalację w systemie Android.
4. Jeśli Android poprosi o zgodę „Instaluj nieznane aplikacje”, zezwól tylko
   przeglądarce lub menedżerowi plików, z którego otwierasz oficjalny APK.
5. Uruchom AutoGłośnik z listy aplikacji.

### Migracja ze starej wersji 1.3.1

Stara wersja 1.3.1 była podpisana innym, testowym kluczem. Jeżeli Android
wyświetla błąd konfliktu pakietu lub podpisu, odinstaluj 1.3.1 i dopiero wtedy
zainstaluj wydanie 1.0 lub nowsze. Jest to jednorazowa operacja. Wydania od 1.0
do 1.03 korzystają z tego samego certyfikatu i aktualizują się bez odinstalowania.

## 4. Konfiguracja po uruchomieniu

1. Na zakładce **Strona główna** włącz główny przełącznik AutoGłośnika.
2. Wybierz **Uprawnienie Telefon** i zezwól aplikacji na rozpoznanie, czy trwa
   rozmowa. Aplikacja nie odczytuje numeru ani historii połączeń.
3. Wybierz **Usługa AutoGłośnik**, znajdź AutoGłośnik na liście dostępności i
   włącz usługę.
4. Jeżeli Android pokazuje komunikat o ustawieniu z ograniczeniami, otwórz
   ekran informacji o aplikacji i użyj systemowej opcji **Zezwól na ustawienia
   z ograniczeniami**, a następnie ponownie włącz usługę dostępności. Nazwa i
   położenie tej opcji zależą od producenta telefonu.
5. Wróć do aplikacji. Przy pozycji Telefon i usługa powinien pojawić się stan
   gotowości.
6. Opcjonalnie ustaw dla AutoGłośnika brak ograniczeń baterii, jeżeli producent
   telefonu agresywnie zatrzymuje usługi działające w tle.

## 5. Test działania

1. Wykonaj zwykłe połączenie przez kartę SIM.
2. Podczas rozmowy odsuń telefon od ucha na około pół sekundy. Powinien włączyć
   się głośnik.
3. Zbliż telefon do ucha. Dźwięk powinien wrócić do słuchawki.
4. Powtórz test z podłączonym Bluetooth albo słuchawkami — aplikacja nie powinna
   przejmować trasy dźwięku z urządzenia zewnętrznego.

Na dole ekranu są dwie zakładki:

- **Strona główna** — konfiguracja, stan czujnika i diagnostyka działania;
- **Wsparcie** — dobrowolne wsparcie, wersja programu, aktualizacje i polityka
  prywatności.

## 6. Aktualizacje z GitHuba

Wersja 1.03 i nowsze sprawdzają publiczny manifest w głównym repozytorium
`proszczok2002/AutoGlosnik`. Kod źródłowy, najnowsze APK, zgłoszenia i Pull
Requesty znajdują się w tym samym miejscu.

Wersja 1.02 korzysta jeszcze z repozytorium zgodności
`proszczok2002/AutoGlosnik-Releases`. Wydanie 1.03 jest publikowane także tam,
aby istniejące instalacje mogły przejść do nowego kanału bez odinstalowania.

Przebieg aktualizacji:

1. AutoGłośnik informuje o nowej wersji; możesz też wybrać **Sprawdź
   aktualizacje** na zakładce Wsparcie.
2. Po wybraniu **Pobierz** Android pobiera APK z oficjalnego wydania GitHub.
3. Aplikacja sprawdza HTTPS, sumę SHA-256, nazwę pakietu, wyższy numer wersji
   oraz zgodność certyfikatu z już zainstalowaną aplikacją.
4. Przy pierwszej aktualizacji Android może poprosić o jednorazową zgodę
   **Instaluj nieznane aplikacje** dla AutoGłośnika.
5. Po wybraniu **Zainstaluj** zweryfikowany APK jest przekazywany bezpośrednio
   do systemowego `PackageInstaller`. AutoGłośnik nie wysyła go do Uptodown ani
   do innego zewnętrznego instalatora.
6. Android pokazuje własny ekran potwierdzenia. Aktualizacja nie jest i nie może
   być instalowana po cichu.

## 7. Play Protect i bezpieczna weryfikacja

AutoGłośnik jest rozpowszechniany bezpośrednio przez GitHub, a nie przez Google
Play. Play Protect może więc pokazać dodatkowe ostrzeżenie, szczególnie dla
nowego certyfikatu wydawniczego, aplikacji korzystającej z dostępności lub
uprawnienia do samodzielnej aktualizacji. Nie oznacza to automatycznie, że APK
jest zmodyfikowany, ale ostrzeżenia nie należy ignorować w ciemno.

Przed instalacją sprawdź, czy:

- plik pochodzi z oficjalnego wydania
  `github.com/proszczok2002/AutoGlosnik/releases` albo z kanału zgodności
  `github.com/proszczok2002/AutoGlosnik-Releases/releases`;
- suma SHA-256 jest taka sama jak w opisie wydania;
- SHA-256 certyfikatu podpisu wynosi
  `60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326`.

Nie wyłączaj Play Protect na stałe. Jeżeli system blokuje instalację albo nazwa
źródła, suma lub podpis się nie zgadzają, przerwij instalację i zgłoś problem w
głównym repozytorium projektu.

## 8. Najczęstsze problemy

### Głośnik nie przełącza się

- sprawdź główny przełącznik w aplikacji;
- sprawdź uprawnienie Telefon i usługę dostępności;
- upewnij się, że używasz obsługiwanego Telefonu Google/AOSP lub Samsung;
- odłącz na próbę Bluetooth, słuchawki USB/przewodowe i aparat słuchowy;
- sprawdź stan oraz ostatnie zdarzenie na Stronie głównej;
- wykonaj zwykłe połączenie SIM — komunikatory mogą mieć inny interfejs i nie
  są objęte obsługą.

### Aktualizacja pobrała się, ale instalacja nie startuje

- wróć do aplikacji i ponownie wybierz **Zainstaluj**;
- sprawdź, czy AutoGłośnik ma zgodę **Instaluj nieznane aplikacje**;
- upewnij się, że instalujesz nowszy `versionCode` i APK z tym samym podpisem;
- jeśli pojawia się konflikt ze starą wersją 1.3.1, wykonaj jednorazową migrację
  opisaną w punkcie 3;
- wersja 1.02 i nowsze używają bezpośrednio systemowego instalatora i nie powinny uruchamiać
  Uptodown App Installer.

### Android zatrzymuje usługę

Wyłącz ograniczenia baterii dla AutoGłośnika i sprawdź ustawienia autostartu
producenta telefonu. Niektóre nakładki systemowe samodzielnie usypiają usługi.

## 9. Wyłączenie lub usunięcie

Aby czasowo wyłączyć działanie, użyj głównego przełącznika albo wyłącz usługę
dostępności. Aby całkowicie usunąć program, wyłącz usługę dostępności, a potem
odinstaluj AutoGłośnik w ustawieniach aplikacji Androida.

## 10. Prywatność i wsparcie

Aplikacja nie ma reklam ani analityki, nie nagrywa rozmów i nie przesyła treści
ekranu. Szczegóły: [PRIVACY.md](PRIVACY.md).

- Revolut: <https://revolut.me/r_rosiejka>
- PayPal: <https://paypal.me/proszczok>

Wsparcie jest dobrowolne i nie odblokowuje żadnych funkcji.

Kod źródłowy, zgłoszenia błędów i propozycje zmian:
<https://github.com/proszczok2002/AutoGlosnik>.
