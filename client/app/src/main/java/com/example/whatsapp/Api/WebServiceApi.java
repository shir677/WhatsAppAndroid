package com.example.whatsapp.Api;


import com.example.whatsapp.Entities.User;
import com.example.whatsapp.serverJson.addChat;
import com.example.whatsapp.serverJson.getContact;
import com.example.whatsapp.serverJson.getMsg;
import com.example.whatsapp.serverJson.loginUser;
import com.example.whatsapp.serverJson.sendMsg;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {

    @POST("Users")
    Call<Void> addUser(@Body User user);

    @GET("Users/{id}")
    Call<User> getUser(
            @Header("Authorization") String token,
            @Header("fire")String fire,
            @Path(value = "id", encoded = true) String username);

    @POST("Tokens")
    Call<ResponseBody> getToken(@Body loginUser password);

    @GET("Chats")
    Call<List<getContact>> getChats( @Header ("Authorization")String token);


    @POST("Chats")
    Call<getContact> addChat(
            @Header ("Authorization")String token,
            @Body addChat chat);

    @DELETE("Chats/{id}")
    Call<Void> deleteChat(
            @Header ("Authorization")String token,
            @Path(value = "id", encoded = true)String chatId);

    @GET("Chats/{id}/messages")
    Call<List<getMsg>> getChatMessages(
            @Path(value = "id", encoded = true)String id,
            @Header ("Authorization")String token);

    @POST("Chats/{id}/messages")
    Call<getMsg> addMessage(@Path(value = "id", encoded = true)String id,
                          @Header ("Authorization")String token,
                           @Body sendMsg msg);

}
