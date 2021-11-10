package fr.android.bottomnav.ui.accueil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccueilViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccueilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment accueil");
    }

    public LiveData<String> getText() {
        return mText;
    }
}