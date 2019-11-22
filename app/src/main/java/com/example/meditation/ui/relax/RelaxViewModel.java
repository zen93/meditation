package com.example.meditation.ui.relax;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RelaxViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RelaxViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Quiet Time - David Fesliyan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}