package com.example.eCommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserFragment extends Fragment {

    Button bt_logOut;

    private FirebaseAuth mAuth;

    private EditText email;
    private EditText pass;
    private Button btnLogin;
    private TextView signUp;

    private ProgressDialog mDialog;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mAuth.getCurrentUser()!=null)
        {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_user, container, false);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loged_out, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser()!=null)
        {
            bt_logOut = view.findViewById(R.id.bt_logout);

            bt_logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout();
                    new UtilitiesClass().openFragment(getContext(), new UserFragment());  ;

                }
            });
        }
        else {


            mDialog=new ProgressDialog(getContext());

            email= view.findViewById(R.id.email_login);
            pass= view.findViewById(R.id.password_login);

            btnLogin= view.findViewById(R.id.btn_login);
            signUp= view.findViewById(R.id.signup_txt);

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),RegisterActivty.class));
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser();

                }
            });

        }


    }

    //login
    public void loginUser() {

        String mEmail=email.getText().toString().trim();
        String mPass=pass.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPass)){
            email.setError("All Fields are Required ..");
            return;
        }
        mDialog.setMessage("Processing..");
        mDialog.show();
        mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    new UtilitiesClass().openFragment(getContext(), new UserFragment());

                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //logout
    public  void logout()
    {
        FirebaseAuth.getInstance().signOut();
    }
}