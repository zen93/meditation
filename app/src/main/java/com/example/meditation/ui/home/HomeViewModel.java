package com.example.meditation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meditation.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> wText;
    private MutableLiveData<Integer> qText;

    public HomeViewModel() {
        wText = new MutableLiveData<>();
        wText.setValue(R.string.welcome);

        qText = new MutableLiveData<>();
        qText.setValue(R.string.quote);
    }

    public LiveData<Integer> getWelcomeText() {
        return wText;
    }

    public LiveData<Integer> getQuoteText() { return qText; }
}