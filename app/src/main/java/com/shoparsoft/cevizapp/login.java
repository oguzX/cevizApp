package com.shoparsoft.cevizapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        boolean issignUp = intent.getBooleanExtra("signup",false);
        if(issignUp){
            Toast.makeText(getApplicationContext(),"Kayıt Başarılı, Lütfen Giriş Yapınız",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void loginin(View view){
        String email = ((EditText)findViewById(R.id.inputUsername)).getText().toString();
        final String password = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre boş",Toast.LENGTH_LONG).show();
            return;
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(login.this,mainScreen.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"Hata!",Toast.LENGTH_LONG).show();
                        Log.e("Giriş Hatası",task.getException().getMessage());
                    }
                }
            });
        }
    }


    public void signup(View view){
        Intent signupIntent =  new Intent(getApplicationContext(),signup.class);
        startActivity(signupIntent);
        finish();
    }
}
