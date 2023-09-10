package com.example.whatsapp.ViewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp.Entities.User;

public class UserViewModel extends ViewModel {

    private LiveData<User> userList;

    public UserViewModel(Application application) {
        //userList = userRepository.getAllUsers();
    }

    // Expose the userList LiveData to the UI
    public LiveData<User> getUserList() {
        return userList;
    }
}
