package com.osmacademy.mkpss.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.Model.Category;
import com.osmacademy.mkpss.Model.Question;
import com.osmacademy.mkpss.Model.Testler;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {

    private static final  String DB_NAME ="database.db";
    private static final int DB_VER=1;


    private static DBHelper instance;



    public static synchronized DBHelper getInstance(Context context){
        if (instance ==null)
            instance = new DBHelper(context);
        return  instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }


    //GET ALL CATEGORIES FROM DB

    public List<Category> getAllCategories(){

        SQLiteDatabase db = instance.getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM Category;",null);
        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Category category = new Category(cursor.getInt(cursor.getColumnIndex("ID")),cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Image")));

                categories.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return categories;
    }

    //GET ALL TESTS FROM DB

    public List<Testler> getAllTestler(int category){

        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursorTestler = db.rawQuery(String.format("SELECT * FROM Testler WHERE CategoryID = %d;",category),null);
        List<Testler> testlerList = new ArrayList<>();
        if (cursorTestler.moveToFirst()){
            while (!cursorTestler.isAfterLast()){
                Testler testler = new Testler(cursorTestler.getInt(cursorTestler.getColumnIndex("ID")),cursorTestler.getString(
                        cursorTestler.getColumnIndex("Name")
                ),cursorTestler.getInt(cursorTestler.getColumnIndex("CategoryID")));
                testlerList.add(testler);
                cursorTestler.moveToNext();
            }
        }

        cursorTestler.close();
        db.close();

        return testlerList;
    }

    //GET ALL QUESTION FROM DB

    public List<Question> getAllQuestion(int testler){
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor= db.rawQuery(String.format("SELECT * FROM Question WHERE CategoryID = %d ORDER BY RANDOM();",testler),null);
        List<Question> questions = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Question question = new Question(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("AnswerC")),
                        cursor.getString(cursor.getColumnIndex("AnswerD")),
                        cursor.getString(cursor.getColumnIndex("AnswerE")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                        cursor.getInt(cursor.getColumnIndex("CategoryID")));
                questions.add(question);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;
    }



}
