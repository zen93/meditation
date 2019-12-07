package com.example.meditation.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.meditation.R;
import com.yariksoffice.lingver.Lingver;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String languageCode = sharedPref.getString("languageCode", "en");
        Lingver.getInstance().setLocale(getContext(), languageCode);
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView wTextView = root.findViewById(R.id.text_welcome);
        final TextView qTextView = root.findViewById(R.id.text_quote);
        homeViewModel.getWelcomeText().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                wTextView.setText(getContext().getString(s));
            }
        });

        homeViewModel.getQuoteText().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                qTextView.setText(getContext().getString(s));
            }
        });

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.languages_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        switch (languageCode) {
            case "en":
                spinner.setSelection(0, false);
                break;
            case "hi":
                spinner.setSelection(1, false);
                break;
            case "zh":
                spinner.setSelection(2, false);
                break;
            case "te":
                spinner.setSelection(3, false);
                break;
        }
        spinner.setOnItemSelectedListener(this);
        return root;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.setSelection(pos);
        String language = (String) parent.getItemAtPosition(pos);
        String languageCode = "en";
        switch (language) {
            case "English":
                languageCode = "en";
                break;
            case "हिन्दी":
                languageCode = "hi";
                break;
            case "中文":
                languageCode = "zh";
                break;
            case "తెలుగు":
                languageCode = "te";
                break;
        }
        Lingver.getInstance().setLocale(getContext(), languageCode);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("languageCode", languageCode);
        editor.apply();
        getActivity().recreate();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}