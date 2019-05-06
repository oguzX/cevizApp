package com.shoparsoft.cevizapp;

import android.widget.RadioGroup;

import java.util.ArrayList;

public class noteType{
    private class note{
        private int typeId;
        private String typeName;

        public note(int typeId, String typeName) {
            this.typeId = typeId;
            this.typeName = typeName;
        }
    }
    private ArrayList<note> NoteTypes;
    private String[] types;

    public noteType() {
        types = new String[]{"Normal","Doğum Günü","Evlilik Yıldönümü"};
        NoteTypes = new ArrayList<>();
        for (int i=0; i<types.length;i++) {
            NoteTypes.add(new note(i,types[i]));
        }
    }
    public String getNameById(int typeid){
        return  NoteTypes.get(typeid).typeName;
    }
    public String[] getAllType(){
        return types;
    }
}