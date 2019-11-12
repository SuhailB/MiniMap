package edu.uark.csce.minimap.ui.notifications;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.uark.csce.minimap.ui.LoginResultCallbacks;
import edu.uark.csce.minimap.ui.User;

public class  NotificationsViewModel extends ViewModel {

    private User user;
    private LoginResultCallbacks loginResultCallbacks;


    private MutableLiveData<String> mText;

    public NotificationsViewModel(LoginResultCallbacks loginResultCallbacks) {
        this.loginResultCallbacks = loginResultCallbacks;
        this.user = new User();
    }

    //Write method to get Email from EditText and set to user
    public TextWatcher getEmailTextWatcher()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setEmail(s.toString());

            }
        };
    }

    //Write method to get Password from EditText and set to user
    public TextWatcher getPasswordTextWatcher()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setPassword(s.toString());

            }
        };
    }

    //Write method to process Login
    public void onLoginClicked(View view){
        int errorCode = user.isValidData();
        if(errorCode == 0)
            loginResultCallbacks.onError("error: enter email address");
        else if(errorCode == 1)
            loginResultCallbacks.onError("error: invalid email address");
        else if(errorCode == 2)
            loginResultCallbacks.onError("error: password length must be equal or greater than 6 characters");
        else
            loginResultCallbacks.onSuccess("Login Success!");

    }


    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}