package com.example.chillapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionResp {

    @SerializedName("version")
    @Expose
    public String version;

    public VersionResp(String version) {
        this.version = version;
    }
}
