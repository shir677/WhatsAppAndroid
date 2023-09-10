package com.example.whatsapp.ViewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.Repository.ContactRepository;

import java.util.List;


public class ContactsViewModel extends ViewModel {
    private ContactRepository repository;
    private LiveData<List<Contact>> contactListData;

    public ContactsViewModel () {
        repository = new ContactRepository(MyApp.context);
        contactListData = repository.getAll();
    }

    public LiveData<List<Contact>> getContactList() {
        return contactListData;
    }

    public void add(String contact) { repository.add(contact); }

    /*public void delete(Contact contact) { repository.delete(contact); }*/

    public void reload() { repository.reload(); }


}
