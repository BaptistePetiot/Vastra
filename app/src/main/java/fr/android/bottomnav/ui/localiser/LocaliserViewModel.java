package fr.android.bottomnav.ui.localiser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocaliserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LocaliserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment localiser");
    }

    public LiveData<String> getText() {
        return mText;
    }
}