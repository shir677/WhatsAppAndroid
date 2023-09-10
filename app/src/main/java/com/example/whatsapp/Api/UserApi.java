package com.example.whatsapp.Api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.AppDB;
import com.example.whatsapp.Activities.ContactsActivity;
import com.example.whatsapp.Daos.UserDao;
import com.example.whatsapp.Entities.User;
import com.example.whatsapp.Activities.LoginActivity;
import com.example.whatsapp.MyApp;
import com.example.whatsapp.SharedPreferencesManager;
import com.example.whatsapp.Sql;
import com.example.whatsapp.UsefulClass.BaseUrl;
import com.example.whatsapp.serverJson.loginUser;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private MutableLiveData<User> userListData;
    private WebServiceApi webServiceAPI;
    private Retrofit retrofit;
    private ExecutorService executor;
    private UserDao userDao;
    private String url;
    private Context MyContext;

    public UserApi(MutableLiveData<User> userListData, Context context) {
        MyContext = context;
        AppDB database = AppDB.getInstance(context);
        this.userListData = userListData;
        executor = Executors.newSingleThreadExecutor();
        userDao = database.userDao();
        BaseUrl b = BaseUrl.getInstance();
        url = b.getBaseUrl();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceApi.class);
    }


    public void addUser() {
        Call<Void> call = webServiceAPI.addUser(userListData.getValue());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    //getToken();
                    //Intent intent = new Intent(MyContext, ContactsActivity.class);

                    Intent intent = new Intent(MyContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add the flag
                    MyContext.startActivity(intent);

                    //InsertUserRunnable insertUserRunnable = new InsertUserRunnable(userDao, userListData.getValue());
                    //executor.execute(insertUserRunnable);
                } else {
                    Toast.makeText(MyContext, "The username already exists, please choose a new username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyContext, "onFailure error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Get token
    public void getToken() {
        loginUser user = new loginUser(userListData.getValue().getUsername(), userListData.getValue().getPassword());
        Call<ResponseBody> getTokenCall = webServiceAPI.getToken(user);
        getTokenCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String addToken = null;
                    try {
                        addToken = "bearer " + response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    SharedPreferencesManager.getInstance(MyContext).saveToken(addToken);
                    String res = SharedPreferencesManager.getInstance(MyContext).getToken();
                    /*Intent intent = new Intent(MyContext, ContactsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add the flag
                    intent.putExtra("user",userListData.getValue().getUsername());
                    MyContext.startActivity(intent);*/
                    getaddUser();

                } else {
                    Toast.makeText(MyContext, "wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MyContext, "onFailure error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getaddUser() {
        BaseUrl b = BaseUrl.getInstance();
        String fire = b.getFire();
        String token = SharedPreferencesManager.getInstance(MyContext).getToken();
        Call<User> getUserCall = webServiceAPI.getUser(token,fire, userListData.getValue().getUsername());
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {


                    User u = response.body();
                    u.setPassword(userListData.getValue().getPassword());
                    userListData.setValue(u);

                    Intent intent = new Intent(MyContext, ContactsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add the flag
                    intent.putExtra("username",u.getUsername());
                    //intent.putExtra("pic",u.getProfilePic());
                    MyContext.startActivity(intent);


                    GetUserRunnable getUserRunnable = new GetUserRunnable(userDao, userListData.getValue());
                    executor.execute(getUserRunnable);
                } else {
                    Toast.makeText(MyContext, "cant get username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle API call failure
            }
        });

    }
    public void getUser() {
        BaseUrl b = BaseUrl.getInstance();
        String fire = b.getFire();
        String token = SharedPreferencesManager.getInstance(MyContext).getToken();
        Call<User> getUserCall = webServiceAPI.getUser(token,fire, userListData.getValue().getUsername());
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    User u = response.body();
                    u.setPassword(userListData.getValue().getPassword());
                    userListData.setValue(u);

                } else {
                    Toast.makeText(MyContext, "cant get username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("hh","whatttttttttt");
                // Handle API call failure
            }
        });

    }


    private static class InsertUserRunnable implements Runnable {
        private UserDao userDao;
        private User user;

        public InsertUserRunnable(UserDao userDao, User user) {
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            String username = user.getUsername();
            User existingUser = userDao.get(username);
            if (existingUser == null) {
                userDao.insert(user);
            } else {
                String log = existingUser.toString();
                Log.i("existingUser",log);
            }
        }
    }

    private static class GetUserRunnable implements Runnable {
        private UserDao userDao;
        private User user;

        public GetUserRunnable(UserDao userDao, User user) {
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            String username = user.getUsername();
            List<User> u = userDao.index();
            if (u.isEmpty()){
                userDao.insert(user);
            }else {
                Sql sql = new Sql(MyApp.context);
                sql.deleteAll();
                userDao.insert(user);
            }
        }
    }

}