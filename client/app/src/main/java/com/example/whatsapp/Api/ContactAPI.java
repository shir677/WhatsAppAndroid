package com.example.whatsapp.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.ContactDao;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.SharedPreferencesManager;
import com.example.whatsapp.UsefulClass.BaseUrl;
import com.example.whatsapp.serverJson.addChat;
import com.example.whatsapp.serverJson.getContact;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    private MutableLiveData<List<Contact>> ContactListData;
    private WebServiceApi webServiceAPI;
    private Retrofit retrofit;
    private ContactDao contactDao;
    private Context MyContext;
    private ExecutorService executor;

    private String url;
    private String token;

    public ContactAPI(MutableLiveData<List<Contact>> contactListData) {
        ContactListData = contactListData;
        MyContext = MyApp.context;
        AppDB database = AppDB.getInstance(MyApp.context);
        executor = Executors.newSingleThreadExecutor();
        contactDao = database.contactDao();
        token = SharedPreferencesManager.getInstance(MyApp.context).getToken();
        BaseUrl b = BaseUrl.getInstance();
        url = b.getBaseUrl();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceApi.class);
    }


    private LinkedList<Contact> convertContact(List<getContact> s) {
        LinkedList<Contact> contacts = new LinkedList<>();
        for (getContact getContact : s) {
            Contact contact = convertContact(getContact);
            /*Contact contact = new Contact();
            contact.setId(getContact.getId());
            contact.setUsername(getContact.getUser().getUsername());
            contact.setDisplayName(getContact.getUser().getDisplayName());
            contact.setProfilePic(getContact.getUser().getProfilePic());
            if (getContact.getMessage()!=null){
                contact.setLastCreated(getContact.getMessage().getCreated());
                contact.setLastContent(getContact.getMessage().getContent());
            }*/
            contacts.add(contact);
        }
        return contacts;
    }
    private Contact convertContact(getContact getContact) {
            Contact contact = new Contact();
            contact.setId(getContact.getId());
            contact.setUsername(getContact.getUser().getUsername());
            contact.setDisplayName(getContact.getUser().getDisplayName());
            contact.setProfilePic(getContact.getUser().getProfilePic());
            if (getContact.getMessage()!=null){
                contact.setLastCreated(getContact.getMessage().getCreated());
                contact.setLastContent(getContact.getMessage().getContent());
            }
        return contact;
    }

    public void get() {
        Call<List<getContact>> call = webServiceAPI.getChats(token);
        call.enqueue(new Callback<List<getContact>>() {
            @Override
            public void onResponse(Call<List<getContact>> call, Response<List<getContact>> response) {
                if (response.isSuccessful()) {
                    Log.i("res",response.body().toString());
                    List<getContact> s = new LinkedList<>(response.body());
                    LinkedList<Contact> newList = convertContact(s);
                    ContactListData.setValue(newList);
                    getChatsRunnable getChatsRunnable = new getChatsRunnable(contactDao, newList);
                    executor.execute(getChatsRunnable);

                    //contactDao.insertList(newList);
                } else {
                    Toast.makeText(MyContext, "error get chats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<getContact>> call, Throwable t) {
                Toast.makeText(MyContext, "onFailure error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addChat(addChat newChat) {
        Call<getContact> call = webServiceAPI.addChat(token, newChat);
        call.enqueue(new Callback<getContact>() {
            @Override
            public void onResponse(Call<getContact> call, Response<getContact> response) {
                if (response.isSuccessful()) {
                    getContact getContact = response.body();
                    Contact contact = convertContact(getContact);
                    List<Contact> add =  ContactListData.getValue();
                    add.add(contact);
                    ContactListData.setValue(add);
                    addChatsRunnable addChatsRunnable = new addChatsRunnable(contactDao, contact);
                    executor.execute(addChatsRunnable);
                    //todo firebase

                } else {
                    // Handle unsuccessful API response
                    Toast.makeText(MyApp.context, "cant add user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getContact> call, Throwable t) {
                Toast.makeText(MyApp.context, "cant add user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteChat(String chatId) {
        Call<Void> call = webServiceAPI.deleteChat(token, chatId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Chat deleted successfully
                    //todo firebase
                } else {
                    // Handle unsuccessful API response
                    Toast.makeText(MyContext, "error: cant delete contact", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(MyContext, "onFailure error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class getChatsRunnable implements Runnable {
        private ContactDao dao;
        private List<Contact> ContactListData;

        public getChatsRunnable(ContactDao dao, List<Contact> ContactListData) {
            this.dao = dao;
            this.ContactListData = ContactListData;
        }

        @Override
        public void run() {
            dao.insertList(ContactListData);
        }
    }

    private static class addChatsRunnable implements Runnable {
        private ContactDao dao;
        private Contact contact;

        public addChatsRunnable(ContactDao dao, Contact contact) {
            this.dao = dao;
            this.contact = contact;
        }

        @Override
        public void run() {
            dao.insert(contact);
        }
    }
    private static class deleteChatsRunnable implements Runnable {
        private ContactDao dao;
        private Contact contact;

        public deleteChatsRunnable(ContactDao dao, Contact contact) {
            this.dao = dao;
            this.contact = contact;
        }

        @Override
        public void run() {
            dao.delete(contact);
        }
    }

}
