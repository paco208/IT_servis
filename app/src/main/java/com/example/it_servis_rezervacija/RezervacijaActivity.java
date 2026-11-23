package com.example.it_servis_rezervacija;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

/**
 * Obrazec za rezervacijo servisnega termina. Izbrano napravo prejme iz
 * namena (getStringExtra), uporabnik pa vnese kontaktne podatke, vrsto
 * posega, datum, uro in način kontakta. Ob potrditvi se podatki preverijo
 * in prikažejo v povzetku (AlertDialog).
 */
public class RezervacijaActivity extends AppCompatActivity {

    private static final String KLJUC_LETO = "leto";
    private static final String KLJUC_MESEC = "mesec";
    private static final String KLJUC_DAN = "dan";
    private static final String KLJUC_URA = "ura";
    private static final String KLJUC_MINUTA = "minuta";

    private EditText vnosIme;
    private EditText vnosEposta;
    private EditText vnosTelefon;
    private EditText vnosOpis;
    private TextView prikazDatum;
    private TextView prikazUra;
    private RadioGroup skupinaKontakt;

    private String naprava;
    private String izbraniPoseg;
    private String nacinKontakta;

    // Vrednost -1 pomeni, da datum oziroma ura še nista izbrana.
    private int leto = -1;
    private int mesec;
    private int dan;
    private int ura = -1;
    private int minuta;

    // Med obnavljanjem stanja (npr. po obratu zaslona) ne prikazujemo Toasta.
    private boolean obnavljanje = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rezervacija);
        InsetsUtil.setupToolbarAndInsets(this);

        // Izbrano napravo preberemo iz namena in jo izpišemo v naslovu.
        naprava = getIntent().getStringExtra(MainActivity.EXTRA_NAPRAVA);
        if (naprava == null) {
            naprava = getString(R.string.naprava_racunalnik);
        }
        setTitle(getString(R.string.naslov_rezervacija, naprava));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        vnosIme = findViewById(R.id.vnos_ime);
        vnosEposta = findViewById(R.id.vnos_eposta);
        vnosTelefon = findViewById(R.id.vnos_telefon);
        vnosOpis = findViewById(R.id.vnos_opis);
        prikazDatum = findViewById(R.id.prikaz_datum);
        prikazUra = findViewById(R.id.prikaz_ura);
        skupinaKontakt = findViewById(R.id.skupina_kontakt);

        nastaviIzbirnikPosega();
        nastaviNacinKontakta();

        findViewById(R.id.izbira_datum).setOnClickListener(v ->
                new DatePickerFragment().show(getSupportFragmentManager(), "datePicker"));
        findViewById(R.id.izbira_ura).setOnClickListener(v ->
                new TimePickerFragment().show(getSupportFragmentManager(), "timePicker"));
        findViewById(R.id.gumb_potrdi).setOnClickListener(v -> potrdiRezervacijo());

        // Po obratu zaslona obnovimo izbrani datum in uro.
        if (savedInstanceState != null) {
            leto = savedInstanceState.getInt(KLJUC_LETO, -1);
            mesec = savedInstanceState.getInt(KLJUC_MESEC);
            dan = savedInstanceState.getInt(KLJUC_DAN);
            ura = savedInstanceState.getInt(KLJUC_URA, -1);
            minuta = savedInstanceState.getInt(KLJUC_MINUTA);
            posodobiPrikazDatuma();
            posodobiPrikazUre();
        }
    }

    /** Spinner napolnimo z vrednostmi iz string-array in zajamemo izbiro. */
    private void nastaviIzbirnikPosega() {
        Spinner spinner = findViewById(R.id.izbirnik_poseg);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.vrste_posega, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        izbraniPoseg = spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                izbraniPoseg = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /** RadioGroup dovoli le eno izbiro; ob spremembi jo shranimo in prikažemo. */
    private void nastaviNacinKontakta() {
        skupinaKontakt.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                return;
            }
            RadioButton izbran = findViewById(checkedId);
            nacinKontakta = izbran.getText().toString();
            if (!obnavljanje) {
                Toast.makeText(this, getString(R.string.toast_kontakt, nacinKontakta),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Pokliče DatePickerFragment, ko uporabnik izbere datum. */
    public void obdelajDatum(int leto, int mesec, int dan) {
        this.leto = leto;
        this.mesec = mesec;
        this.dan = dan;
        posodobiPrikazDatuma();
    }

    /** Pokliče TimePickerFragment, ko uporabnik izbere uro. */
    public void obdelajUro(int ura, int minuta) {
        this.ura = ura;
        this.minuta = minuta;
        posodobiPrikazUre();
    }

    private void posodobiPrikazDatuma() {
        if (leto == -1) {
            return;
        }
        prikazDatum.setText(oblikujDatum());
        prikazDatum.setTextColor(ContextCompat.getColor(this, R.color.besedilo));
    }

    private void posodobiPrikazUre() {
        if (ura == -1) {
            return;
        }
        prikazUra.setText(oblikujUro());
        prikazUra.setTextColor(ContextCompat.getColor(this, R.color.besedilo));
    }

    private String oblikujDatum() {
        // Meseci v razredu Calendar se štejejo od 0, zato prištejemo 1.
        return getString(R.string.format_datum, dan, mesec + 1, leto);
    }

    private String oblikujUro() {
        return getString(R.string.format_ura, ura, minuta);
    }

    /** Preveri obvezne podatke in, če so ustrezni, prikaže povzetek. */
    private void potrdiRezervacijo() {
        String ime = vnosIme.getText().toString().trim();
        String eposta = vnosEposta.getText().toString().trim();
        String telefon = vnosTelefon.getText().toString().trim();
        String opis = vnosOpis.getText().toString().trim();

        EditText prvoNapacno = null;

        if (ime.isEmpty()) {
            vnosIme.setError(getString(R.string.napaka_ime));
            prvoNapacno = vnosIme;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(eposta).matches()) {
            vnosEposta.setError(getString(R.string.napaka_eposta));
            if (prvoNapacno == null) {
                prvoNapacno = vnosEposta;
            }
        }
        // Telefonska številka mora imeti pravo obliko in vsaj 6 števk.
        if (!Patterns.PHONE.matcher(telefon).matches()
                || telefon.replaceAll("[^0-9]", "").length() < 6) {
            vnosTelefon.setError(getString(R.string.napaka_telefon));
            if (prvoNapacno == null) {
                prvoNapacno = vnosTelefon;
            }
        }
        if (opis.isEmpty()) {
            vnosOpis.setError(getString(R.string.napaka_opis));
            if (prvoNapacno == null) {
                prvoNapacno = vnosOpis;
            }
        }

        if (prvoNapacno != null) {
            prvoNapacno.requestFocus();
            prikaziSporocilo(R.string.napaka_obrazec);
            return;
        }
        if (leto == -1) {
            prikaziSporocilo(R.string.napaka_datum);
            return;
        }
        if (ura == -1) {
            prikaziSporocilo(R.string.napaka_ura);
            return;
        }

        // Datum ne sme biti v preteklosti; primerjamo samo dan brez ure.
        Calendar danes = Calendar.getInstance();
        danes.set(Calendar.HOUR_OF_DAY, 0);
        danes.set(Calendar.MINUTE, 0);
        danes.set(Calendar.SECOND, 0);
        danes.set(Calendar.MILLISECOND, 0);

        Calendar izbraniDan = Calendar.getInstance();
        izbraniDan.clear();
        izbraniDan.set(leto, mesec, dan);

        if (izbraniDan.before(danes)) {
            prikaziSporocilo(R.string.napaka_datum_preteklost);
            return;
        }

        // Če je izbran današnji dan, ura ne sme biti že mimo.
        Calendar termin = Calendar.getInstance();
        termin.clear();
        termin.set(leto, mesec, dan, ura, minuta);
        if (termin.before(Calendar.getInstance())) {
            prikaziSporocilo(R.string.napaka_ura_preteklost);
            return;
        }

        if (skupinaKontakt.getCheckedRadioButtonId() == -1) {
            prikaziSporocilo(R.string.napaka_kontakt);
            return;
        }

        prikaziPovzetek(ime);
    }

    /** Povzetek rezervacije z gumboma POTRDI in PREKLIČI. */
    private void prikaziPovzetek(String ime) {
        String povzetek = getString(R.string.dialog_povzetek,
                naprava, izbraniPoseg, ime, oblikujDatum(), oblikujUro(), nacinKontakta);

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_naslov)
                .setMessage(povzetek)
                .setPositiveButton(R.string.dialog_potrdi, (dialog, which) ->
                        Toast.makeText(this, R.string.toast_uspeh, Toast.LENGTH_LONG).show())
                // Ob preklicu se okno zapre, uporabnik pa ostane v obrazcu.
                .setNegativeButton(R.string.dialog_preklici, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void prikaziSporocilo(int sporociloId) {
        Toast.makeText(this, sporociloId, Toast.LENGTH_SHORT).show();
    }

    /** Puščica v naslovni vrstici deluje enako kot gumb Nazaj. */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KLJUC_LETO, leto);
        outState.putInt(KLJUC_MESEC, mesec);
        outState.putInt(KLJUC_DAN, dan);
        outState.putInt(KLJUC_URA, ura);
        outState.putInt(KLJUC_MINUTA, minuta);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        obnavljanje = true;
        super.onRestoreInstanceState(savedInstanceState);
        obnavljanje = false;
    }
}
