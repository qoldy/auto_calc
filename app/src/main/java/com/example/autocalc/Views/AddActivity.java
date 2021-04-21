package com.example.autocalc.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autocalc.Data.Consumption;
import com.example.autocalc.R;
import com.example.autocalc.Tools.SQLiteHelper;

import java.time.LocalDate;

public class AddActivity extends AppCompatActivity {
    Spinner categories;//выпадающий список
    Button add;//кнопка
    EditText price;//поле ввода
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        categories=findViewById(R.id.spinner_category);//инициализация спиннера
        String[] cat=getResources().getStringArray(R.array.categories);//из ресурсов получаем категории
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cat);//создаем адаптер
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);//устанавливаем спинеру адаптер

        price=findViewById(R.id.text_price);

        add=findViewById(R.id.button_add);
        final SQLiteHelper helper = new SQLiteHelper(this, "database.db", null, 1);//помощник для работы с бд
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//нажатие на кнопку
                double pric=Double.parseDouble(price.getText().toString());//получаем стоимость из поля ввода
                String cat = categories.getSelectedItem().toString();//поучаем выбранную категорию
                Consumption cons=new Consumption(cat,pric,LocalDate.now());//создаем запись
                helper.insert(cons);//вставляем ее в бд
                Intent intent = new Intent(v.getContext(), MainActivity.class);//возвращаемся на главную активность
                startActivityForResult(intent, 0);
            }
        });
    }
}
