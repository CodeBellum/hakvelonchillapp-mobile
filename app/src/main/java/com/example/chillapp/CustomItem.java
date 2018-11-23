package com.example.chillapp;

public class CustomItem {
    public int id;
    public String primaryText;
    public String secondaryText;
    public int firstTextSize;
    public int secondTextSize;

    public CustomItem(int id, String p, String s, int fs, int ss) {
        this.id = id;
        this.primaryText = p;
        this.secondaryText = s;
        this.firstTextSize = fs;
        this.secondTextSize = ss;
    }
}