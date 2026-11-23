package com.example.it_servis_rezervacija;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Začetni zaslon z izbiro naprave. Ob kliku na sliko (ali kartico) se z
 * eksplicitnim namenom zažene RezervacijaActivity, ki ji z putExtra()
 * predamo izbrano vrsto naprave.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NAPRAVA = "com.example.it_servis_rezervacija.extra.NAPRAVA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        InsetsUtil.setupToolbarAndInsets(this);

        nastaviIzbiro(R.id.kartica_racunalnik, R.id.slika_racunalnik, R.string.naprava_racunalnik);
        nastaviIzbiro(R.id.kartica_telefon, R.id.slika_telefon, R.string.naprava_telefon);
        nastaviIzbiro(R.id.kartica_tiskalnik, R.id.slika_tiskalnik, R.string.naprava_tiskalnik);
    }

    /** Slika in celotna kartica odpreta obrazec za isto napravo. */
    private void nastaviIzbiro(int karticaId, int slikaId, int napravaId) {
        View.OnClickListener poslusalec = v -> odpriRezervacijo(getString(napravaId));
        findViewById(karticaId).setOnClickListener(poslusalec);
        findViewById(slikaId).setOnClickListener(poslusalec);
    }

    private void odpriRezervacijo(String naprava) {
        Intent intent = new Intent(this, RezervacijaActivity.class);
        intent.putExtra(EXTRA_NAPRAVA, naprava);
        startActivity(intent);
    }
}
