package com.example.ibrep.projectf2.SQ;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Oclemmy on 5/5/2016 for ProgrammingWizards Channel and http://www.Camposha.com.
 */
public class DBAdapter {

    Context c;
    static SQLiteDatabase db;
    static DBHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper=new DBHelper(c);
    }

    //OPEN CON
    public static void openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {

        }
    }
    //CLOSE DB
    public void closeDB()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {

        }
    }

    //SAVE
    public boolean add(String name, String assunto)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.NAME,assunto + ";" + "\n" + name);

            long result=db.insert(Constants.TB_NAME,Constants.ROW_ID,cv);

            if(result>0)
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    //SELECT
    public Cursor retrieve()
    {
        String[] columns={Constants.ROW_ID,Constants.NAME};


        Cursor c=db.query(Constants.TB_NAME,columns,null,null,null,null,null);
        return c;
    }

    //UPDATE/edit
    public boolean update(String newName, int id)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.NAME,newName);


            int result=db.update(Constants.TB_NAME,cv, Constants.ROW_ID + " =?", new String[]{String.valueOf(id)});
            if(result>0)
            {
                return true;
            }
        }catch (SQLException e)
        {
             e.printStackTrace();
        }

        return false;

    }

    //DELETE/REMOVE
    public boolean delete(int id)
    {
        try
        {
            int result=db.delete(Constants.TB_NAME,Constants.ROW_ID+" =?",new String[]{String.valueOf(id)});
            if(result>0)
            {
                return true;
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }



}












