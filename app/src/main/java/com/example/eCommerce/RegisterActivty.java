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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivty extends AppCompatActivity {


    private EditText  ed_fName, ed_lName, ed_email, ed_phone,ed_pass,ed_pass2;
    private Button btnReg;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersDatabase;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activty);

        firebaseAuth= FirebaseAuth.getInstance();
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDialog=new ProgressDialog(this);

        ed_fName = findViewById(R.id.first_reg);
        ed_lName = findViewById(R.id.last_reg);
        ed_email = findViewById(R.id.email_reg);
        ed_phone = findViewById(R.id.phone_reg);
        ed_pass = findViewById(R.id.password_reg);
        ed_pass2 = findViewById(R.id.password_reg2);



        btnReg=findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fName = ed_fName.getText().toString().trim();
                String lName =  ed_lName.getText().toString().trim();
                String uemail =  ed_email.getText().toString().trim();
                String uphone = ed_phone.getText().toString().trim();
                String upass = ed_pass.getText().toString().trim();
                String upass2 = ed_pass2.getText().toString().trim();

                if(TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(uemail) || TextUtils.isEmpty(uphone) || TextUtils.isEmpty(upass) || TextUtils.isEmpty(upass2))
                {
                     Toast.makeText(RegisterActivty.this,"Please Fill All Fields!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(upass.length() < 6)
                {
                    Toast.makeText(RegisterActivty.this,"Password Length Must be Greater than 6!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!upass.equalsIgnoreCase(upass2))
                {
                    Toast.makeText(RegisterActivty.this,"Password Does not Match!!",Toast.LENGTH_SHORT).show();
                    return;
                }


                registerUser(fName,lName,uemail, uphone,upass);
            }
        });

    }



    private void registerUser(String userFirstName, String userLastName, String uemail,String userPhoneNumber,String upassword) {

        mDialog.setMessage("Please Wait...");
        mDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                mDialog.dismiss();

                if(task.isSuccessful())
                {

                    //get user
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    User user  = new User(userFirstName, userLastName,uemail, userPhoneNumber);

                    //add data
                    usersDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivty.this,"Successfully Registered You can Login!!",Toast.LENGTH_SHORT).show();
                                ed_fName.setText("");
                                ed_lName.setText("");
                                ed_email.setText("");
                                ed_phone.setText("");
                                ed_pass.setText("");
                                ed_pass2.setText("");
                                Intent intent = new Intent(RegisterActivty.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(RegisterActivty.this,"Some thing went Wrong Try again",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
                else{
                    String errMesg = task.getException().getMessage();
                    Toast.makeText(RegisterActivty.this,errMesg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}