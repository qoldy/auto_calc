package com.example.autocalc.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.autocalc.Data.Consumption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.LocalDate.now;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//создание таблицы в бд
        db.execSQL("create table consumptions ("+
                " id integer primary key,"+
                " category text,"+
                " price double,"+
                " date text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insert(Consumption consumption){//вставка
        SQLiteDatabase db = this.getWritableDatabase();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = consumption.date.format(formatter);

        ContentValues values = new ContentValues();//закидываем все по соответствующим столбцам
        values.put("category", consumption.category);
        values.put("price", consumption.price);
        values.put("date", date);
        Log.v("added",consumption.category+" "+consumption.price+consumption.date);
        long newRowId = db.insert("consumptions", null, values);//и вставляем
    }
    public ArrayList<Consumption> read(String category, String time){//чтение из БД
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Consumption> list = new ArrayList<>();
        String query="";
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date=now();
        //определяем, какие параметры отчета
        if(time.equals("За последний год"))
            date=date.minusYears(1);
        else if(time.equals("За последние полгода"))
            date=date.minusMonths(6);
        else if(time.equals("За последний месяц"))
            date=date.minusMonths(1);
        else if(time.equals("За последнюю неделю"))
            date=date.minusDays(7);
        String dateString=date.toString();
        //делаем выборку по соответствующим параметрам
        if(category.equals("Все затраты")&&time.equals("За все время"))
            query = "select * from consumptions;";
        else if(category.equals("Все затраты")&&!time.equals("За все время"))
            query = "select * from consumptions where date>'"+dateString+"';";
        else if(!category.equals("Все затраты")&&time.equals("За все время"))
            query = "select * from consumptions where category='"+category+"';";
        else
            query="select * from consumptions where category='"+category+"' and date>'"+dateString+"';";
        Cursor result = db.rawQuery(query, null);
        if (result.moveToFirst()) {//читаем ответ на запрос
            do {
                Consumption consumption = new Consumption(result.getString(result.getColumnIndex("category")),
                        result.getDouble(result.getColumnIndex("price")),
                        LocalDate.parse(result.getString(result.getColumnIndex("date")), formatter));
                consumption.id=result.getInt(result.getColumnIndex("id"));

                list.add(consumption);
            }
            while (result.moveToNext());
        }
        return list;
    }
    public double getMaxPrice(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select max(price) from consumptions;";
        Cursor result = db.rawQuery(query, null);
        if(result.moveToFirst()){
            return result.getDouble(result.getColumnIndex("max(price)"));
        }
        return 0;
    }
}
