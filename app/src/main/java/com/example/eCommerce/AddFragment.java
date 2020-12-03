package com.example.eCommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddFragment extends Fragment {

    EditText ed_title, ed_amount, ed_imagUrl;
    Spinner sp_catagory;
    Button bt_addProduct;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String uId;

    private ProgressDialog mDialog;

    public AddFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog=new ProgressDialog(getContext());
        mAuth= FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("E_Commerece");

        mDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed_title = view.findViewById(R.id.edt_title);
        sp_catagory = view.findViewById(R.id.edt_type);
        ed_amount = view.findViewById(R.id.edt_ammount);
        ed_imagUrl = view.findViewById(R.id.edt_imgUrl);

        bt_addProduct = view.findViewById(R.id.btn_addProduct);

        if (mAuth.getCurrentUser()==null)
        {
            bt_addProduct.setEnabled(false);
        }

        bt_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mTitle  = ed_title.getText().toString().trim();
                String mCatagory = sp_catagory.getSelectedItem().toString().trim();
                String mAmount = ed_amount.getText().toString().trim();
                String mImage = ed_imagUrl.getText().toString().trim();

                //check empty
                if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mCatagory) || TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mImage)){
                    Toast.makeText(getContext(),"All Fields are  Required.. ",Toast.LENGTH_SHORT).show();
                    return;
                }


                mDialog.setMessage("Processing..");
                mDialog.show();


                float ammint = Float.parseFloat(mAmount);

                String date= new UtilitiesClass().getFormatedDate();
                String id= mDatabase.push().getKey();

                ProductModel data = new ProductModel(mCatagory, ammint, mTitle, date, uId, mImage);

                writeToDb( id,data,"Product Added Successfully !! ");


            }
        });

    }

    public void writeToDb(String regId, ProductModel data, String type)
    {
        mDatabase.child(regId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();
                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}