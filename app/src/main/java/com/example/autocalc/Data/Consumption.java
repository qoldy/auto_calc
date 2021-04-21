package com.example.autocalc.Data;

import java.time.LocalDate;

public class Consumption {
    public int id;
    public String category;
    public double price;
    public LocalDate date;
    public Consumption(String cat, double pric, LocalDate dat){
        category=cat;
        price=pric;
        date=dat;
    }
}
