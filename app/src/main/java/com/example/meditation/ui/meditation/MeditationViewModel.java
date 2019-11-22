package com.example.meditation.ui.meditation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeditationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MeditationViewModel() {
        mText = new MutableLiveData<>();
<<<<<<< HEAD:app/src/main/java/com/example/meditation/ui/notifications/NotificationsViewModel.java
        mText.setValue("Now this is notifications fragment, but might be meditation videopage.");
=======
        mText.setValue("This is meditation fragment");
>>>>>>> akshay:app/src/main/java/com/example/meditation/ui/meditation/MeditationViewModel.java
    }

    public LiveData<String> getText() {
        return mText;
    }
}