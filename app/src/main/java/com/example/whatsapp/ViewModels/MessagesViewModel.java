package com.example.whatsapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.Repository.MessageRepository;

import java.util.List;

public class MessagesViewModel extends ViewModel {
    private MutableLiveData<List<Message>> messages;
    private MessageRepository repository;
    private LiveData<List<Message>> contactListData;

    public MessagesViewModel () {
        repository = new MessageRepository(MyApp.context);
        contactListData = repository.getAll();
    }

    public void setChatId(String chatId){
        repository.setChatId(chatId);
    }

    public MutableLiveData<List<Message>> getMessages(){
        if(messages == null){
            messages = new MutableLiveData<List<Message>>();
        }
        return messages;
    }

    public LiveData<List<Message>> getMessageList() {
        return contactListData;
    }

    public void add(String msg) { repository.add(msg); }

    /*public void delete(Contact contact) { repository.delete(contact); }*/

    public void reload() { repository.reload(); }

}