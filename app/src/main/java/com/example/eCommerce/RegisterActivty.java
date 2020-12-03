package com.example.eCommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivty extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnReg;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activty);

        mAuth= FirebaseAuth.getInstance();

        mDialog=new ProgressDialog(this);

        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);

        btnReg=findViewById(R.id.btn_reg);

    }



    public void registerUser(View view) {
        String mEmail=email.getText().toString().trim();
        String mPass=pass.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPass)){
            email.setError("All Fields are  Required ..");
            return;
        }

        mDialog.setMessage("Processing..");
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    Toast.makeText(getApplicationContext(),"Successfully Registered !!",Toast.LENGTH_SHORT).show();

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}