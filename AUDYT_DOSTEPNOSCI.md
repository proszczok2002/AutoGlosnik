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
- Kontrola na urządzeniu obejmuje granice kontrolek w hierarchii UI i zrzuty
  ekranów. Wynik testu wersji 1.07 zostanie dopisany po instalacji zatwierdzonej
  w systemie Android.

## Granice audytu

Zrzut ekranu i hierarchia UI nie potwierdzają rzeczywistej wypowiedzi
TalkBacka ani fizycznej obsługi gestami. Nie wykonano rozmowy SIM, więc
działanie przełączania audio w 1.07 nie zostało ponownie ocenione.
