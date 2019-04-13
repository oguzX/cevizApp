package com.shoparsoft.cevizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    EditText useremail,
    password,
    passwordAgain;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();

        useremail = (EditText)findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputPassword);
        passwordAgain = (EditText)findViewById(R.id.inputPasswordAgain);
    }

    public void signupNow(View view){
        String userEmailText = useremail.getText().toString();
        String passwordText = password.getText().toString();
        String passwordAgainText = passwordAgain.getText().toString();
        if(!TextUtils.isEmpty(userEmailText) || !TextUtils.isEmpty(passwordText) || !TextUtils.isEmpty(passwordAgainText)){
            if(passwordText.compareTo(passwordAgainText) == 0 ){
                auth.createUserWithEmailAndPassword(useremail.getText().toString(),password.getText().toString()).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Yetkilendirme Hatası",Toast.LENGTH_SHORT).show();
                        }else{
                            gotoLogin(true);
                            finish();
                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"Şifreler eşleşmedi",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Lütfen Kullanıcı adı ve şifrenizi giriniz",Toast.LENGTH_LONG).show();
        }
    }

    public void login(View view){
        gotoLogin();
    }
    public void gotoLogin(){
        Intent signupIntent =  new Intent(getApplicationContext(),login.class);
        startActivity(signupIntent);
    }
    public void gotoLogin(Boolean signup){
        Intent signupIntent =  new Intent(getApplicationContext(),login.class);
        signupIntent.putExtra("signup",true);
        startActivity(signupIntent);
    }
}

