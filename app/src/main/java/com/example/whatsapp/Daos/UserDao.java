package com.example.whatsapp.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsapp.Entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    User get(String username);

    @Query("SELECT * FROM user WHERE displayName = :displayName")
    User find(String displayName);

    @Insert
    void insert(User... users);
    @Delete
    void delete(User... users);
    @Update
    void update(User... users);

}

