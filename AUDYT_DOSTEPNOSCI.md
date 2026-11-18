# Audyt dostępności interfejsu AutoGłośnika 1.07

Data: 2026-11-16. Zakres: ekran główny, Wsparcie, dolna nawigacja, komunikaty
stanu i tekst uprawnień. Wzorzec nawigacji: aplikacja Android z projektu
`F:\YouTube Downloader by Proszczok` (ikona nad podpisem, wyróżniona aktywna
pozycja, osobny układ przy powiększonej czcionce).

## Zaobserwowany problem

Na Pixelu 11 (1080 × 2424 px, gęstość 420 dpi, trzy przyciski systemowe,
TalkBack włączony) w wersji 1.06 dolne zakładki miały granice do `y=2392`,
czyli wchodziły pod systemowy pasek nawigacji. Nagłówek wchodził pod pasek
stanu. Potwierdzono to zrzutem ekranu i hierarchią UI z urządzenia.

## Poprawki w 1.07

- Główny widok uwzględnia bezpieczne marginesy pasków systemowych i wycięcia
  ekranu. Treść i zakładki pozostają nad paskiem nawigacji Androida.
- Pasek nawigacji ma ikony i podpisy. Wybrana zakładka ma osobną, kontrastową
  powierzchnię. Przy skali czcionki większej niż 1,4 układ przechodzi na
  poziome przyciski z większym podpisem i obramowaniem.
- Każda zakładka ma obszar dotyku o wysokości co najmniej 72 dp, nazwę,
  semantykę przycisku oraz lokalizowany opis stanu wybrania. Ikony i podpisy
  wewnątrz nie tworzą zdublowanych pozycji dla czytnika.
- Przyciski czynności i przełącznik mają wysokość co najmniej 56 dp. Nagłówki
  sekcji są oznaczone dla czytnika. Zmiana zawartości zakładki ustawia nazwę
  obszaru; komunikat aktualizatora jest obszarem ogłoszeń uprzejmych.
- Stan wykrytej aplikacji Telefon mówi wprost, czy jest obsługiwana. Opis
  usługi dostępności i polityka prywatności wyjaśniają odczyt interfejsu
  rozmowy bez zapisywania treści ekranu.
- Na ekranie Wsparcie nad przyciskami pozostają tylko nazwy Revolut i PayPal;
  identyfikatory kont i adresy są celami otwieranych linków, nie tekstem ekranu.

## Kontrole

- Kontrast tekstu względem tła, obliczony z kolorów interfejsu: podstawowy
  17,01:1, pomocniczy na pasku nawigacji 9,70:1, podstawowy na zaznaczeniu
  6,51:1, biały na głównym niebieskim przycisku 5,12:1.
- `lintDebug` i `lintRelease`: bez problemów; `testDebugUnitTest`: cztery testy
  profili aplikacji Telefon; kompilacja podpisanego APK: poprawna.
- Pixel 11 po systemowej instalacji 1.07: na ekranie głównym i Wsparcie zakładki
  mają granice `[32,2077][524,2266]` i `[556,2077][1048,2266]` w obrazie
  1080 × 2424 px. Są w całości nad trzema przyciskami systemowymi; nagłówki
  są pod paskiem stanu. Przełączanie obu zakładek działa, a hierarchia UI
  pokazuje ich nazwy, klasę przycisku i właściwy stan zaznaczenia.
- Przy skali czcionki 1,6 zakładki przyjmują układ poziomy. Podpis „Strona
  główna” zawija się do dwóch linii bez obcięcia; zakładki nadal kończą się
  nad systemową nawigacją. Przewinięcie ekranu Wsparcie pozwala dosięgnąć
  przycisku polityki prywatności i końcowej informacji. Po teście przywrócono
  skalę 1,0.
- Na ekranie Wsparcie hierarchia i zrzut pokazują wyłącznie nazwy Revolut i
  PayPal nad przyciskami. Numery kont i adresy nie występują w tekście widoku.

## Granice audytu

TalkBack był włączony podczas testu, ale zrzut ekranu i hierarchia UI nie
potwierdzają jego rzeczywistej wypowiedzi ani fizycznej obsługi gestami.
Przełączenie zakładek wykonano przez wstrzyknięte dotknięcia ADB. Nie wykonano
rozmowy SIM, więc działanie przełączania audio w 1.07 nie zostało ponownie
ocenione.
Test interakcyjny wykonano w polskiej wersji językowej; pozostałe tłumaczenia
sprawdzono przez kompilację i lint, bez osobnego testu na telefonie.
