package com.example.eCommerce;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


public class AddFragment extends Fragment {

    EditText ed_title, ed_amount, ed_imagUrl;
    EditText ed_Longitude, ed_Latitued;
    ImageView im_GetLocation;
    Spinner sp_catagory;
    Button bt_addProduct;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String uId;

    LocationManager locationManager;

    private ProgressDialog mDialog;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {

            FirebaseUser mUser = mAuth.getCurrentUser();
            uId = mUser.getUid();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference().child("E_Commerece");

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

        //location related info
        ed_Latitued = view.findViewById(R.id.edit_Latitude);
        ed_Longitude = view.findViewById(R.id.edit_Longitude);
        im_GetLocation = view.findViewById(R.id.imag_getLocation);

        bt_addProduct = view.findViewById(R.id.btn_addProduct);

        //location

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (mAuth.getCurrentUser() == null) {
            bt_addProduct.setEnabled(false);
            sp_catagory.setEnabled(false);
            sp_catagory.setClickable(false);
            ed_title.setFocusable(false);
            ed_amount.setFocusable(false);
            ed_imagUrl.setFocusable(false);
            Toast.makeText(getContext(), "Need to Login .. ", Toast.LENGTH_SHORT).show();
        }

        im_GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SetLogLat();

            }
        });

        bt_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mTitle = ed_title.getText().toString().trim();
                String mCatagory = sp_catagory.getSelectedItem().toString().trim();
                String mAmount = ed_amount.getText().toString().trim();
                String mImage = ed_imagUrl.getText().toString().trim();

                String longitude = ed_Longitude.getText().toString().trim();
                String latitiude = ed_Latitued.getText().toString().trim();


                //check empty
                if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mCatagory) || TextUtils.isEmpty(mAmount) || TextUtils.isEmpty(mImage) || TextUtils.isEmpty(latitiude) || TextUtils.isEmpty(longitude)) {
                    Toast.makeText(getContext(), "All Fields are  Required.. ", Toast.LENGTH_SHORT).show();
                    return;
                }


                mDialog.setMessage("Processing..");
                mDialog.show();


                float ammint = Float.parseFloat(mAmount);


                String id = mDatabase.push().getKey();

                ProductModel data = new ProductModel(mCatagory, ammint, mTitle,uId, mImage, longitude, latitiude);

                writeToDb(id, data, "Product Added Successfully !! ");


            }
        });

    }


    public void writeToDb(String regId, ProductModel data, String type) {
        mDatabase.child(regId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
                    ed_title.setText("");
                    ed_amount.setText("");
                    ed_imagUrl.setText("");
                    ed_Longitude.setText("");
                    ed_Latitued.setText("");
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SetLogLat() {


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        Toast.makeText(getContext(), "Retriving Location ...",Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 0, new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {

                ed_Latitued.setText(String.valueOf(location.getLatitude()));
                ed_Longitude.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}