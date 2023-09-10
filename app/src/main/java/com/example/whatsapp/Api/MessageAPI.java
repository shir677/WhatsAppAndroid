package com.example.whatsapp.Api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.MessageDao;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.SharedPreferencesManager;
import com.example.whatsapp.UsefulClass.BaseUrl;
import com.example.whatsapp.serverJson.getMsg;
import com.example.whatsapp.serverJson.sendMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageAPI {
    private MutableLiveData<List<Message>> MessageListData;
    private MessageDao messageDao;
    private String chatId;
    private String token;
    private ExecutorService executor;
    private String url;
    Retrofit retrofit;
    WebServiceApi webServiceAPI;


    public MessageAPI(MutableLiveData<List<Message>> MessageListData) {
        this.MessageListData = MessageListData;
        this.token = SharedPreferencesManager.getInstance(MyApp.context).getToken();
        AppDB database = AppDB.getInstance(MyApp.context);
        executor = Executors.newSingleThreadExecutor();
        messageDao = database.messageDao();
        BaseUrl b = BaseUrl.getInstance();
        url = b.getBaseUrl();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceApi.class);
    }


    private LinkedList<Message> convertMessage(List<getMsg> s, String chatId) {
        LinkedList<Message> messages = new LinkedList<>();
        for (getMsg getMsg : s) {
            Message message = convertMessage(getMsg, chatId);
            messages.add(message);
        }
        return messages;
    }

    private Message convertMessage(getMsg getMsg, String chatId) {
        Message message = new Message();
        message.setChatId(chatId);
        message.setContent(getMsg.getContent());
        message.setCreated(getMsg.getCreated());
        message.setSender(getMsg.getSenderName());

        return message;
    }


    public void get(String chatId) {
        this.chatId = chatId;
        Call<List<getMsg>> call = webServiceAPI.getChatMessages(chatId, token);
        call.enqueue(new Callback<List<getMsg>>() {
            @Override
            public void onResponse(Call<List<getMsg>> call, Response<List<getMsg>> response) {
                if (response.isSuccessful()) {
                    Log.i("res", response.body().toString());
                    List<getMsg> s = new LinkedList<>(response.body());
                    LinkedList<Message> newList = convertMessage(s, chatId);
                    MessageListData.setValue(newList);
                    getMessageRunnable getMessageRunnable = new getMessageRunnable(messageDao, newList);
                    executor.execute(getMessageRunnable);
                } else {
                    Log.i("ff", "ff");
                    // Handle unsuccessful API response
                }
            }

            @Override
            public void onFailure(Call<List<getMsg>> call, Throwable t) {
                Log.i("ff", "ff");
            }
        });
    }

    public void add(sendMsg message, String chatId) {
        this.chatId = chatId;
        Call<getMsg> call = webServiceAPI.addMessage(chatId, token, message);
        call.enqueue(new Callback<getMsg>() {
            @Override
            public void onResponse(Call<getMsg> call, Response<getMsg> response) {
                if (response.isSuccessful()) {
                    Log.i("res", response.body().toString());
                    getMsg s = response.body();
                    Message newMsg = convertMessage(s, chatId);

                    List<Message> add = MessageListData.getValue();
                    add.add(newMsg);
                    MessageListData.setValue(add);


                    BaseUrl baseUrl = BaseUrl.getInstance();
                    Socket socket = baseUrl.getSocket();


                    JSONObject payload = new JSONObject();

                    try {
                        payload.put("roomId", chatId);
                        payload.put("message", newMsg);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    socket.emit("message", payload);


                    AddMessageRunnable addMessageRunnable = new AddMessageRunnable(messageDao, newMsg);
                    executor.execute(addMessageRunnable);
                } else {
                    Log.i("ff", "ff");
                    // Handle unsuccessful API response
                }
            }

            @Override
            public void onFailure(Call<getMsg> call, Throwable t) {
                // Handle API call failure
            }
        });
    }

    private static class AddMessageRunnable implements Runnable {
        private MessageDao messageDao;
        private Message message;

        public AddMessageRunnable(MessageDao messageDao, Message message) {
            this.messageDao = messageDao;
            this.message = message;
        }

        @Override
        public void run() {
            messageDao.insert(message);
        }
    }

    private static class getMessageRunnable implements Runnable {
        private MessageDao dao;
        private List<Message> messages;

        public getMessageRunnable(MessageDao messageDao, List<Message> message) {
            this.dao = messageDao;
            this.messages = message;
        }

        @Override
        public void run() {
            dao.insertList(messages);
        }
    }
}

