package com.example.dailyshoping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class RecycleAdapter  extends RecyclerView.Adapter<RecycleAdapter.MyIdeaHandler> {

    Context context;
    ArrayList<ShopingModle> shoppingList;

    public RecycleAdapter(Context con, ArrayList<ShopingModle> shoppingList)
    {

        this.context = con;
        this.shoppingList = shoppingList;

    }

    @NonNull
    @Override
    public MyIdeaHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        MyIdeaHandler myIdeaHandler = new MyIdeaHandler(view);
        return myIdeaHandler;

    }

    @Override
    public void onBindViewHolder(@NonNull MyIdeaHandler holder, int position) {

        ShopingModle shopeList = shoppingList.get(position);
        String title = shopeList.getType();
        String description = shopeList.getNote();
        String date = shopeList.getDate();
        String amount =   String.valueOf(shopeList.getAmount());




        //set the view
        holder.mType.setText(title);
        holder.mNote.setText(description);
        holder.mDate.setText(date);
        holder.mAmount.setText(amount);



    }


    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public class MyIdeaHandler extends RecyclerView.ViewHolder{

        TextView mType;
        TextView mNote;
        TextView mDate;
        TextView mAmount;

        public MyIdeaHandler(@NonNull View myview) {
            super(myview);
           mType   =   myview.findViewById(R.id.type);
           mNote   =   myview.findViewById(R.id.note);
           mDate   =   myview.findViewById(R.id.date);
           mAmount =   myview.findViewById(R.id.amount);

        }
    }


}

