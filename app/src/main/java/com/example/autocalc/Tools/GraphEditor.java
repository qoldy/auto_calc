package com.example.autocalc.Tools;

import android.util.Log;

import com.example.autocalc.Data.Consumption;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class GraphEditor {
    ArrayList<Consumption> list;
    public double sum;
    public GraphEditor(ArrayList<Consumption> list){
        this.list=list;
        Log.v("create editor", Double.toString(list.size()));
    }
    public GraphEditor(){list=new ArrayList<>();}
    public void setData(ArrayList<Consumption> list){
        this.list=list;
    }
    public LineGraphSeries<DataPoint> getSeries(){//получение серий
        sum=0.;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        list=normalizeList(list);
        if(!list.isEmpty()){//если массив записей не пустой
            for (int i = 0; i < list.size(); i++) {
                series.appendData(//добавляем данные в серии
                        new DataPoint(Date.from(list.get(i).date.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        list.get(i).price), true, list.size());
                sum+=list.get(i).price;
            }
            //немного настраиваем внешний вид серий
            series.setThickness(2);
            series.setDataPointsRadius(5);
            series.setDrawDataPoints(true);
        }
        return series;//возвращаем их
    }
    ArrayList<Consumption> normalizeList(ArrayList<Consumption> list){//здесь происходит сложение затрат за один день
        ArrayList<Consumption> result = new ArrayList<>();
        if(!list.isEmpty())
            result.add(list.get(0));
        if(list.size()!=1)
            for(int i=1; i<list.size();i++){
                if(list.get(i).date.equals(list.get((i-1)).date))
                    result.get(result.size()-1).price+=list.get(i).price;
                else result.add(list.get(i));
            }
        return result;
    }
}
