# Współpraca przy AutoGłośniku

Dziękuję za chęć ulepszania projektu. Mile widziane są zgłoszenia błędów,
propozycje, poprawki dokumentacji, tłumaczenia i zmiany w kodzie.

## Zanim zaczniesz

- sprawdź, czy podobny temat nie istnieje już w
  [Issues](https://github.com/proszczok2002/AutoGlosnik/issues);
- dla większej zmiany najpierw otwórz Issue i krótko opisz cel;
- zgłoszenia podatności przekazuj prywatnie zgodnie z [SECURITY.md](SECURITY.md);
- jedna gałąź i jeden Pull Request powinny rozwiązywać jeden spójny problem.

## Środowisko

Potrzebujesz JDK 17 i Android SDK 36. Projekt używa Gradle Wrappera, więc nie
musisz instalować osobnego Gradle.

```bash
git clone https://github.com/TWOJ_LOGIN/AutoGlosnik.git
cd AutoGlosnik
git remote add upstream https://github.com/proszczok2002/AutoGlosnik.git
git switch -c nazwa-zmiany
```

Na Windows uruchom:

```powershell
.\gradlew.bat lintDebug testDebugUnitTest assembleDebug
```

Na Linuxie lub macOS:

```bash
./gradlew lintDebug testDebugUnitTest assembleDebug
```

## Zasady zmian

- zachowaj minimalny zakres potrzebny do rozwiązania problemu;
- nie dodawaj zależności bez uzasadnienia;
- teksty widoczne dla użytkownika uzupełnij po polsku, angielsku, niemiecku i
  rosyjsku;
- elementy interfejsu muszą mieć czytelne etykiety i zachować logiczną kolejność
  fokusu dla TalkBacka;
- nie zapisuj ani nie przesyłaj treści ekranu, numerów telefonu lub rozmów;
- nie osłabiaj kontroli HTTPS, sumy SHA-256, pakietu, numeru wersji ani podpisu
  aktualizacji;
- aktualizacja i instalacja zawsze wymagają świadomego działania użytkownika;
- przy zmianie zachowania uzupełnij README, instrukcję albo opis techniczny.

## Czego nie commitować

- `.signing/`, kluczy, certyfikatów prywatnych i haseł;
- `local.properties` oraz lokalnych ścieżek SDK;
- katalogów `.gradle/`, `.idea/`, `build/` i `.tools/`;
- plików APK, zrzutów ekranu zawierających dane prywatne ani tokenów.

Oficjalny klucz podpisu pozostaje wyłącznie u właściciela projektu. Do pracy i
testów używaj wariantu debug.

## Pull Request

W opisie podaj:

1. co i dlaczego zmieniasz;
2. jak zmiana została sprawdzona;
3. jakie urządzenie lub emulator i wersja Androida były użyte;
4. czy zmiana dotyczy dostępności, prywatności, uprawnień lub aktualizatora;
5. zrzuty ekranu tylko wtedy, gdy pomagają ocenić zmianę interfejsu.

Pull Request musi przejść automatyczny build. Przegląd kodu może wskazać
poprawki przed scaleniem. Samo wysłanie zmiany nie gwarantuje jej przyjęcia.

Przesyłając wkład, zgadzasz się na udostępnienie go na warunkach
[Apache License 2.0](LICENSE).
