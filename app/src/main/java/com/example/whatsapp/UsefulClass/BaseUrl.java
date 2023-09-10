package com.example.whatsapp.UsefulClass;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class BaseUrl {

    private Socket socket;
    private static BaseUrl instance;

    private String baseUrl="http://192.168.1.107:12345";
    //private String baseUrl="http://10.0.2.2:12345/api/";
    private String fire;

    public BaseUrl()  {
        try {
            socket = IO.socket( baseUrl );
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    // Handle the token
                    Log.e("Token", token);
                    fire = token;
                    //save the token
                });
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(){
        try {
            socket = IO.socket( baseUrl );
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



    public static synchronized BaseUrl getInstance(){
        if(instance==null){
            instance = new BaseUrl();
        }
        return instance;
    }


    public String getBaseUrl() {
        return baseUrl+"/api/";
    }

    public String getFire() {
        return fire;
    }

    public  void setBaseUrl(String url){
        baseUrl="http://"+url;
        int i = 1;
        setSocket();
    }
}
