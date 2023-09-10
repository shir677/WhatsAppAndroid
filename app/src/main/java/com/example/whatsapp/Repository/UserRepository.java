package com.example.whatsapp.Repository;
import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.Api.UserApi;
import com.example.whatsapp.AppDB;
import com.example.whatsapp.Daos.UserDao;
import com.example.whatsapp.Entities.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserApi userApi;
    private UserDao userDao;
    private UserListData userListData;
    private ExecutorService executor;

    public UserListData getUserListData() {
        return userListData;
    }


    public UserRepository(Context context) {

        AppDB database = AppDB.getInstance(context);

        executor = Executors.newSingleThreadExecutor();

        userDao = database.userDao();
        userListData = new UserListData();

        //userApi = new UserApi(userListData);
    }

    public void setUserListData(User user) {
        this.userListData.setValue(user);
    }

    class UserListData extends MutableLiveData<User> {
        public UserListData() {
            super();
            setValue(new User());
        }
    }


    /*public boolean insert(User user) {
        userApi.addUser(user);
        if(userListData.getValue().getUsername()!= null){
            InsertUserRunnable insertUserRunnable = new InsertUserRunnable(userDao, user);
            executor.execute(insertUserRunnable);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean insert() {
        userApi.addUser();
        if(userListData.getValue().getUsername()!= null){
            InsertUserRunnable insertUserRunnable = new InsertUserRunnable(userDao,userListData.getValue() );
            executor.execute(insertUserRunnable);
            return true;
        }
        else{
            return false;
        }
    }*/

    private static class InsertUserRunnable implements Runnable {
        private UserDao userDao;
        private User user;

        public InsertUserRunnable(UserDao userDao, User user) {
            this.userDao = userDao;
            this.user = user;
        }

        @Override
        public void run() {
            userDao.insert(user);
        }
    }

}
