# Moj IT servis

Demonstracijska aplikacija za rezervacijo servisnega termina za računalnik, telefon ali tiskalnik, v Android Studiu.

## Funkcionalnosti

- **MaterialToolbar** s temno modrim ozadjem in belim naslovom na obeh zaslonih
- **MainActivity** z izbiro naprave
  - Tri klikljive kartice s slikami **ImageView** (Računalnik, Telefon, Tiskalnik), kratkim opisom in nastavljenim `contentDescription`
  - Ob kliku se z eksplicitnim namenom zažene **RezervacijaActivity**, izbrana naprava pa se preda z `putExtra()`
- **RezervacijaActivity** s pomičnim obrazcem (**ScrollView**)
  - Naslov »Rezervacija • naprava«, prebran z `getStringExtra()`, in puščica za vrnitev na začetni zaslon
  - **EditText** za ime in priimek (`textPersonName|textCapWords`), e-pošto (`textEmailAddress`), telefon (`phone`) in opis težave (`textCapSentences|textAutoCorrect|textMultiLine`), vsa z namigi (`hint`)
  - **Spinner** »Vrsta posega« z možnostmi Diagnostika, Popravilo, Nadgradnja in Drugo; vrednosti so v `string-array`, izbirnik pa napolni `ArrayAdapter`, izbiro zajame `OnItemSelectedListener`
  - Izbira datuma z **DatePickerDialog** in ure s **TimePickerDialog**, oba v razredu **DialogFragment**; izbrani vrednosti se izpišeta v obrazcu
  - **RadioGroup** s tremi **RadioButton** (E-pošta, Telefonski klic, SMS); izbira se shrani in prikaže v Toast sporočilu
  - **Button** »POTRDI REZERVACIJO« preveri obvezna polja (`setError`), izbrani datum in uro ter način kontakta; datum ne sme biti v preteklosti, pri manjkajočih podatkih se prikaže Toast
  - **AlertDialog** s povzetkom rezervacije (naprava, vrsta posega, ime, datum, ura, kontakt) in gumboma POTRDI ter PREKLIČI; POTRDI prikaže Toast o uspešni demonstracijski oddaji, PREKLIČI pa uporabnika pusti v obrazcu
- Vnos in izbire se ohranijo pri premikanju obrazca in ob obratu zaslona
- Lastna **ikona aplikacije** (prilagodljiva vektorska ikona: bel zaslon računalnika s ključem na temno modrem ozadju)
- Vsa besedila so v datoteki `strings.xml`
- Aplikacija ne potrebuje strežnika in ne pošilja pravih zahtevkov
