package com.shoparsoft.cevizapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addNewnote extends AppCompatActivity {
    private TextView dateInput,timeInput;
    private EditText inputTitle,inputDesc;
    private RadioGroup radioGroupType;
    private int selectedType;
    private noteType noteT;
    private String[] selectedDate;

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newnote);
        dateInput = findViewById(R.id.inputDate);
        timeInput = findViewById(R.id.inputTime);
        inputTitle = (EditText) findViewById(R.id.addnoteInputTitle);
        inputDesc = (EditText) findViewById(R.id.addnoteInputDesc);
        radioGroupType = findViewById(R.id.radioGroupnotetype);
        selectedDate = new String[]{"0","0"};

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("notes").child(fAuth.getCurrentUser().getUid());
        noteT =  new noteType();
        String[] noteNames = noteT.getAllType();
        for(int i=0;i <noteNames.length;i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(noteNames[i]);
            rb.setId(i+100);
            radioGroupType.addView(rb);
        }
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedType = checkedId-100;
            }
        });
    }
    public void showDatepicker(View v){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateInput.setText(dayOfMonth +"/"+(month+1)+"/"+year);
                selectedDate[0] = (dayOfMonth +"/"+(month+1)+"/"+year);

            }
        },year,month,day);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE,"Seç",dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE,"İptal",dpd);
        dpd.show();

    }
    public void showTimepicker(View v){
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String min="";
                if(minute<10)
                    min = "0"+minute;
                else
                    min = String.valueOf(minute);
                selectedDate[1] = hourOfDay+":"+min;
                timeInput.setText(selectedDate[1]);
            }
        },hour,minute,true);
        timePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE,"Seç",timePickerDialog);
        timePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE,"İptal",timePickerDialog);
        timePickerDialog.show();
    }

    public void save(View v){
        String title = inputTitle.getText().toString().trim();
        String desc = inputDesc.getText().toString().trim();
        String type = String.valueOf(selectedType);
        String date = selectedDate[0];
        String time = selectedDate[1];
        if(!title.matches("") && !desc.matches("") && !type.matches("") && date!="0" && time!="0" ){
            saveNote(title,desc,type,date,time);
        }else{
            Toast.makeText(getApplicationContext(),"Tüm Alanları doldurduğunuzdan emin olun",Toast.LENGTH_LONG).show();
        }

    }
    public void saveNote(String title,String desc,String type,String date,String time){
        final DatabaseReference newNoteRef = fDatabase.push();

        note newNoteObj = new note(title,desc,type,date+" "+time);
        final Map<String,Object> newNote = newNoteObj.toMap();

        final ProgressDialog dialog = new ProgressDialog(addNewnote.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Yükleniyor");
        dialog.setMessage("Not Kaydediliyor, Lütfen bekleyiniz");
        dialog.show();

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                newNoteRef.setValue(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Not Başarıyla eklendi",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Not eklemede hata ile karşılaşıldı",Toast.LENGTH_LONG).show();
                        }
                        dialog.hide();
                    }
                });
            }
        });
        mainThread.start();
    }
}
