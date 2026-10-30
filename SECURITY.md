# Zasady bezpieczeństwa

## Obsługiwane wersje

| Wersja | Aktualizacje bezpieczeństwa |
| --- | --- |
| 1.03 | Tak |
| 1.02 | Tylko migracja do 1.03 |
| 1.01 i starsze | Nie |

## Prywatne zgłoszenie podatności

Nie publikuj szczegółów podatności ani działającego exploita w zwykłym Issue.
Użyj prywatnego formularza:

<https://github.com/proszczok2002/AutoGlosnik/security/advisories/new>

Podaj wersję aplikacji, wersję Androida, model urządzenia, sposób odtworzenia,
wpływ problemu i — jeśli to bezpieczne — propozycję poprawki. Nie dołączaj
rzeczywistych numerów telefonu, treści rozmów, tokenów ani innych danych
osobowych.

## Weryfikacja oficjalnego APK

Oficjalne APK pobieraj wyłącznie z
[GitHub Releases](https://github.com/proszczok2002/AutoGlosnik/releases).
Każde wydanie podaje SHA-256 pliku. Odcisk SHA-256 certyfikatu oficjalnej linii
wydań 1.0–1.03 wynosi:

```text
60A685333B8B79BD70998D692FD44F73D236B043429505C7555DDF7BECBF3326
```

Klucz prywatny nie znajduje się w repozytorium ani w GitHub Actions.
