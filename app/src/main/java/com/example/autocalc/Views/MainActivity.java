package com.example.autocalc.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.autocalc.Data.Consumption;
import com.example.autocalc.R;
import com.example.autocalc.Tools.SQLiteHelper;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {//Основная активность, с которой начинается работа приложения
    Button add, report;//кнопки добавления и просмотра отчета

    @Override
    protected void onCreate(Bundle savedInstanceState) {//начальная точка любой активности
        super.onCreate(savedInstanceState);//вызывается аналогичная функция из родительского класса
        setContentView(R.layout.activity_main);//выставляется соответствующий лэйаут
        add=findViewById(R.id.button_add);//находим кнопку добавления
        report=findViewById(R.id.button_report);//и кнопку отчета
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//выставляем прослушиватель на кнопку
                Intent intent = new Intent(v.getContext(), AddActivity.class);//при нажатии на кнопку создается поток
                startActivityForResult(intent, 0);//и осуществляется переход по этому потоку
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//аналогично
                Intent intent = new Intent(v.getContext(), ReportActivity.class);
                startActivityForResult(intent, 0);
            }
        });
//        test();
    }

    void test(){//эту функцию я делал для заполнения данными. чтобы вручную не вбивать, можно один раз с ней скопилить и потом убрать
        SQLiteHelper helper= new SQLiteHelper(this, "database.db", null, 1);
        LocalDate date=LocalDate.now().minusMonths(1);
        Consumption consumption=new Consumption("Бензин", 1000, date);
        helper.insert(consumption);
        consumption=new Consumption("Штрафы", 5000, date);
        helper.insert(consumption);

        date=date.plusDays(2);
        consumption=new Consumption("Бензин", 1500, date);
        helper.insert(consumption);

        date=date.plusDays(2);
        consumption=new Consumption("Бензин", 2000, date);
        helper.insert(consumption);
        consumption=new Consumption("Тех. обслуживание", 10000, date);
        helper.insert(consumption);

        date=date.plusDays(4);
        consumption=new Consumption("Бензин", 1500, date);
        helper.insert(consumption);
        consumption=new Consumption("Штрафы", 3000, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 1000, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 1200, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 1000, date);
        helper.insert(consumption);
        consumption=new Consumption("Тех. обслуживание", 3000, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 2000, date);
        helper.insert(consumption);

        date=date.plusDays(2);
        consumption=new Consumption("Бензин", 1000, date);
        helper.insert(consumption);

        date=date.plusDays(1);
        consumption=new Consumption("Штрафы", 3000, date);
        helper.insert(consumption);

        date=date.plusDays(1);
        consumption=new Consumption("Бензин", 1000, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 2000, date);
        helper.insert(consumption);
        consumption=new Consumption("Тех. обслуживание", 1000, date);
        helper.insert(consumption);

        date=date.plusDays(3);
        consumption=new Consumption("Бензин", 1500, date);
        helper.insert(consumption);
    }
}