package com.example.meditation.ui.meditation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeditationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MeditationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is meditation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}