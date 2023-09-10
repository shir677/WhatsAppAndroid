package com.example.whatsapp.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.Api.ContactAPI;
import com.example.whatsapp.Api.MessageAPI;
import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.MessageDao;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.serverJson.sendMsg;

import java.util.LinkedList;
import java.util.List;

public class MessageRepository {

    private String chatId;
    private MessageDao dao;
    private MessageListData messageListData;
    private MessageAPI api;

    public MessageRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.messageDao();
        messageListData = new MessageListData();
        api = new MessageAPI(messageListData);
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    class MessageListData extends MutableLiveData<List<Message>> {
        public MessageListData() {
            super();
            setValue(new LinkedList<Message>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                messageListData.postValue(dao.index());
            }).start();
        }
    }

    public LiveData<List<Message>> getAll() {
        return messageListData;
    }


    public void reload() {
        api.get(chatId);
    }

    public void add(String msg) {
        sendMsg sendMsg = new sendMsg(msg);
        api.add(sendMsg, chatId);
    }
}
