package com.example.easymusicapp.database;

public class DBSingleton {
    static DBSingleton instance;

    public static DBSingleton getInstance(){
        if(instance == null){
            instance = new DBSingleton();
        }

        return instance;
    }

    public static DBHelper dbHelper;
}
