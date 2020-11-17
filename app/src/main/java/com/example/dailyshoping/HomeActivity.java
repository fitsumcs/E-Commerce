package com.example.dailyshoping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {



    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;

    private TextView totalsumResult;
    ProgressDialog progressDialog;

    ArrayList<ShopingModle> shope_List = new ArrayList<>();;

    //Global variable..

    private String type;
    private int amount;
    private String note;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(HomeActivity.this);

        totalsumResult=findViewById(R.id.total_ammount);


        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        //mDatabase.keepSynced(true);


        recyclerView=findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        //read all Shopping List
        readAllData();

    }

    public void addShoppingItem(View view) {
        addShoppingDialog();
    }



    private void readAllData() {



        shope_List.clear();;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    ShopingModle shopingListModel = snapshot.getValue( ShopingModle.class);

                    shope_List.add(shopingListModel);
                    Collections.reverse(shope_List);

                }

                recycleAdapter =  new RecycleAdapter(HomeActivity.this,shope_List);

                recyclerView.setAdapter(recycleAdapter);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void addShoppingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_shoping_item);
        dialog.show();

        final EditText type=dialog .findViewById(R.id.edt_type);
        final EditText amount=dialog .findViewById(R.id.edt_ammount);
        final EditText note=dialog .findViewById(R.id.edt_note);
        Button btnSave=dialog .findViewById(R.id.btn_save);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType=type.getText().toString().trim();
                String mAmount=amount.getText().toString().trim();
                String mNote=note.getText().toString().trim();



                if (TextUtils.isEmpty(mType) || TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mNote)){
                    Toast.makeText(getApplicationContext(),"All Fields are  Required.. ",Toast.LENGTH_SHORT).show();
                    return;
                }

                int ammint = Integer.parseInt(mAmount);

                String id= mDatabase.push().getKey();

                String date= DateFormat.getDateInstance().format(new Date());

                ShopingModle data = new ShopingModle(mType,ammint,mNote,date,id);

                mDatabase.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Data Added Successfully !! ",Toast.LENGTH_SHORT).show();
                            readAllData();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                dialog.dismiss();
            }
        });


    }
}