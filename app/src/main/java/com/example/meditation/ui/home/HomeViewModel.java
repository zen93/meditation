package com.example.meditation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> wText;
    private MutableLiveData<String> qText;

    public HomeViewModel() {
        wText = new MutableLiveData<>();
        wText.setValue("Welcome!");

        qText = new MutableLiveData<>();
        qText.setValue("\"Calm mind brings inner strength and self-confidence, so that's very important for good health.\"");
    }

    public LiveData<String> getWelcomeText() {
        return wText;
    }

    public LiveData<String> getQuoteText() { return qText; }
}