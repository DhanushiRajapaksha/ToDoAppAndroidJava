package com.example.todoapptwo.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapptwo.Model.todo_model;

import java.util.ArrayList;
import java.util.List;

public class database extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String Database_name="TODO_Database";
    private static final String Table_name="TODO_Table";
    private static final String Col_1="ID";
    private static final String Col_2="Task";
    private static final String Col_3="Status";
    public database(@Nullable Context context ) {
        super(context,Database_name, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Table_name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_name);
        onCreate(db);
    }

    public void insertTask(todo_model model){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_2,model.getTask());
        values.put(Col_3,0);
        db.insert(Table_name, null, values);
    }

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_2, task);
        db.update(Table_name, values, Col_1 + "=?", new String[]{String.valueOf(id)});
    }


    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_3, status);
        db.update(Table_name, values, "ID=?" , new String[]{String.valueOf(id)});
    }

    public  void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(Table_name, "ID=?", new String[]{String.valueOf(id)});
    }


    public List<todo_model> getAllTask(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<todo_model> modelList = new ArrayList<>();


        db.beginTransaction();
        try{
            cursor = db.query(Table_name, null,null, null, null,null,null);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        todo_model task = new todo_model();
                        task.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                        task.setTask(cursor.getString(cursor.getColumnIndex("TASK")));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));

                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}
