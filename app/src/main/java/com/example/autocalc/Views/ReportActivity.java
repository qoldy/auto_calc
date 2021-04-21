package com.example.autocalc.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autocalc.Data.Consumption;
import com.example.autocalc.R;
import com.example.autocalc.Tools.GraphEditor;
import com.example.autocalc.Tools.SQLiteHelper;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    Spinner categories;//выпадающие списки для категорий и временного отрезка
    Spinner time;
    GraphView graphView;//график
    GraphEditor editor;//помощник для работы с графиком
    ArrayList<Consumption> list;//массив записей
    SQLiteHelper helper;//помощник для работы с бд
    final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());//формат дат
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        categories=findViewById(R.id.spinner_category);
        String[] cat=getResources().getStringArray(R.array.categories_report);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//прослушиватель изменения значения спиннера
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupGraph();
                Log.v("selected", "category changed");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time=findViewById(R.id.spinner_time);
        String[] tim=getResources().getStringArray(R.array.time);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tim);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter1);
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        graphView=findViewById(R.id.graph);
        helper = new SQLiteHelper(this, "database.db", null, 1);
        setupGraph();
    }

    void setupGraph(){//настройка графикаа
        list=helper.read(categories.getSelectedItem().toString(),time.getSelectedItem().toString());
        editor = new GraphEditor(list);
        graphView.removeAllSeries();//очищаем график
        graphView.addSeries(editor.getSeries());//при помощи едитора получаем серии

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX)
                    return dateFormat.format(new Date((long)value));
                return super.formatLabel(value, isValueX);
            }
        });
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setScrollable(false);
        graphView.getViewport().scrollToEnd();
        graphView.getViewport().setMinY(0);
        if(!list.isEmpty())
            graphView.getViewport().setMinX(Date.from(list.get(0).date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        TextView sum=findViewById(R.id.textView);
        sum.setText("Итого за период: "+editor.sum);
    }
}
