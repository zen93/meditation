package com.example.meditation.ui.relax;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meditation.R;

public class RelaxViewModel extends ViewModel {

    private MutableLiveData<Integer> mText;

    public RelaxViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(R.string.song_title);
    }

    public LiveData<Integer> getText() {
        return mText;
    }
}