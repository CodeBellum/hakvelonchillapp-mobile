package com.example.chillapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomItem {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("sound_name")
    @Expose
    public String theme;
    @SerializedName("phrase")
    @Expose
    public String primaryText;
    @SerializedName("secondary_phrase")
    @Expose
    public String secondaryText;
    @SerializedName("font_size")
    @Expose
    public int firstTextSize;
    @SerializedName("secondary_font_size")
    @Expose
    public int secondTextSize;
    @SerializedName("min_show_time")
    @Expose
    public long minShowTime;
    @SerializedName("max_show_time")
    @Expose
    public long maxShowTime;



    public CustomItem(int id, String p, String s, int fs, int ss, String theme, long min, long max) {
        this.id = id;
        this.primaryText = p;
        this.secondaryText = s;
        this.firstTextSize = fs;
        this.secondTextSize = ss;
        this.theme = theme;
        this.minShowTime = min;
        this.maxShowTime = max;
    }
}