package com.example.whatsapp.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsapp.Entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact ")
    List<Contact> index();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Contact>contacts);
    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(String id);

    @Query("SELECT * FROM contact WHERE userName = :userName")
    List<Contact> getAll(String userName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact... contacts);
    @Delete
    void delete(Contact... contacts);
    @Update
    void update(Contact... contacts);
}
