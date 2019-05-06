package com.shoparsoft.cevizapp;


import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class note {
    private String title,desc,type,date;

    public note() {

    }

    public note(String title, String desc, String type, String date) {
        this.title = title;
        this.desc = desc;
        this.type = type;
        this.date = date;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("title",title);
        result.put("description",desc);
        result.put("type",type);
        result.put("date",date);
        return result;
    }
}
