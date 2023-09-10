package com.example.whatsapp.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsapp.Entities.Contact;
import com.example.whatsapp.Entities.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message ")
    List<Message> index();
    @Query("SELECT * FROM message WHERE chatid = :chatid")
    List<Message> getAllMessages(int chatid);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Message>messages);
    @Query("SELECT * FROM message WHERE id = :id")
    Message find(int id);

    @Insert
    void insert(Message... messages);
    @Delete
    void delete(Message... messages);
    @Update
    void update(Message... messages);
}
