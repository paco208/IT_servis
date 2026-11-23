package com.example.it_servis_rezervacija;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Pogovorno okno za izbiro datuma (po zgledu iz gradiva Android Developer
 * Fundamentals). Izbrani datum vrne dejavnosti RezervacijaActivity.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Kot privzeti datum uporabimo današnji dan.
        final Calendar c = Calendar.getInstance();
        int leto = c.get(Calendar.YEAR);
        int mesec = c.get(Calendar.MONTH);
        int dan = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), this, leto, mesec, dan);
        // Preteklih dni v koledarju ni mogoče izbrati.
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int leto, int mesec, int dan) {
        RezervacijaActivity activity = (RezervacijaActivity) requireActivity();
        activity.obdelajDatum(leto, mesec, dan);
    }
}
