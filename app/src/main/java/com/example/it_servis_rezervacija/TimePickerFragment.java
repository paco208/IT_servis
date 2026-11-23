package com.example.it_servis_rezervacija;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Pogovorno okno za izbiro ure (po zgledu iz gradiva Android Developer
 * Fundamentals). Izbrano uro vrne dejavnosti RezervacijaActivity.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Kot privzeto uro uporabimo trenutni čas, prikaz je 24-urni.
        final Calendar c = Calendar.getInstance();
        int ura = c.get(Calendar.HOUR_OF_DAY);
        int minuta = c.get(Calendar.MINUTE);

        return new TimePickerDialog(requireActivity(), this, ura, minuta, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int ura, int minuta) {
        RezervacijaActivity activity = (RezervacijaActivity) requireActivity();
        activity.obdelajUro(ura, minuta);
    }
}
