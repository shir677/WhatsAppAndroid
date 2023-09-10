package com.example.whatsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.whatsapp.Daos.ContactDao;
import com.example.whatsapp.Daos.MessageDao;
import com.example.whatsapp.Daos.UserDao;
import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;
import com.example.whatsapp.Entities.User;
@Database(entities = {User.class, Contact.class, Message.class}, version = 2, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    private static final String DATABASE_NAME = "my_app_database";
    private static AppDB instance;

    public abstract UserDao userDao();
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
