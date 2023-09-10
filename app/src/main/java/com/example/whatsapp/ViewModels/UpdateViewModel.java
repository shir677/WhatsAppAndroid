package com.example.whatsapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateViewModel extends ViewModel {
    private MutableLiveData<Integer> update;
    public MutableLiveData<Integer> getUpdate(){
        if(update == null){
            update = new MutableLiveData<Integer>();
            update.setValue(0);
        }
        return update;
    }
}
