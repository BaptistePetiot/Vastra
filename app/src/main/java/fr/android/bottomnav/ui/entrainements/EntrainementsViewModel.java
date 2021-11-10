package fr.android.bottomnav.ui.entrainements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntrainementsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EntrainementsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment entrainements");
    }

    public LiveData<String> getText() {
        return mText;
    }
}