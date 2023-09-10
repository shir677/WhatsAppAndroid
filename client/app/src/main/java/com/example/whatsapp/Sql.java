package com.example.whatsapp;

import android.content.Context;
import android.content.Intent;

import com.example.whatsapp.Activities.ContactsActivity;
import com.example.whatsapp.Activities.RegActivity;
import com.example.whatsapp.Daos.UserDao;
import com.example.whatsapp.Entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Sql{
    private AppDB db;
    private ExecutorService executor;

    private Context context;

    public Sql(Context context) {
        this.context = context;
        executor = Executors.newSingleThreadExecutor();
        this.db = AppDB.getInstance(context);
    }


    public boolean haveDB(){
        try {
            UserDao u = db.userDao();
            exists exists = new exists(u,context);
            executor.execute(exists);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean deleteAll(){
        try {
            DeleteAll deleteAll = new DeleteAll(db);
            executor.execute(deleteAll);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class DeleteAll implements Runnable {
        private AppDB db;

        public DeleteAll(AppDB db) {
            this.db = db;
        }

        @Override
        public void run() {
            db.clearAllTables();
        }
    }

    private static class exists implements Runnable {
        private UserDao dao;
        private Context context;

        public exists(UserDao dao, Context context) {
            this.dao = dao;
            this.context = context;
        }

        @Override
        public void run() {
            List<User> uu = dao.index();
            Intent intent;
            if (uu.size()!=0) {
                // User is registered, navigate to ContactsActivity
                intent = new Intent(context, ContactsActivity.class);
                //intent.putExtra("pic",uu.get(0).getProfilePic());
                intent.putExtra("username",uu.get(0).getUsername());
            } else {
                // User is not registered, navigate to RegisterActivity
                intent = new Intent(context, RegActivity.class);
            }
            context.startActivity(intent);
        }
    }
}
