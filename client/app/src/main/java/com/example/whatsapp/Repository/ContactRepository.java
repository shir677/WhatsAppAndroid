package com.example.whatsapp.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.Api.ContactAPI;
import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.ContactDao;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.serverJson.addChat;

import java.util.LinkedList;
import java.util.List;

public class ContactRepository {
    private ContactDao dao;
    private ContactListData contactListData;
    private ContactAPI api;

    public ContactRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.contactDao();
        contactListData = new ContactListData();
        api = new ContactAPI(contactListData);
    }

    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new LinkedList<Contact>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                contactListData.postValue(dao.index());
            }).start();
        }
    }

    public LiveData<List<Contact>> getAll() {
        return contactListData;
    }


    public void reload() {
        api.get();
    }

    public void add(String username){
        addChat chat = new addChat(username);
        api.addChat(chat);
    }
}
