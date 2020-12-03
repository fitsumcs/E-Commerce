package com.example.eCommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleAdapter  extends RecyclerView.Adapter<RecycleAdapter.MyIdeaHandler> {

    Context context;
    ArrayList<ProductModel> productList;
    private  OnItemClickedListener m_onItemClickedListener;

    public RecycleAdapter(Context con, ArrayList<ProductModel> productList)
    {

        this.context = con;
        this.productList = productList;
        //this.m_onItemClickedListener = onItemClickedListener;

    }

    @NonNull
    @Override
    public MyIdeaHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        MyIdeaHandler myIdeaHandler = new MyIdeaHandler(view,m_onItemClickedListener);
        return myIdeaHandler;

    }

    @Override
    public void onBindViewHolder(@NonNull MyIdeaHandler holder, int position) {

        ProductModel productModel = productList.get(position);
        String catagory = productModel .getType();
        String title = productModel .getNote();
        String date = productModel .getDate();

        //set the view
        holder.productTitle.setText(title);
        holder.productCatagory.setText(catagory);
        holder.datePublished.setText(date);

        //image url
        Glide.with(context)
                .load("https://www.1datagroup.com/wp-content/uploads/2020/10/ecommerce-solutions.jpg")
                .into(holder.imgUrl);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyIdeaHandler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productCatagory, productTitle, datePublished ;
        ImageView imgUrl;
        OnItemClickedListener onItemClickedListener;

        public MyIdeaHandler(@NonNull View myview , OnItemClickedListener onItemClickedListener) {
            super(myview);
           productCatagory   =   myview.findViewById(R.id.textView_Catagory);
           productTitle   =   myview.findViewById(R.id.textView_Title);
           datePublished =   myview.findViewById(R.id.textView_Date);
           imgUrl = myview.findViewById(R.id.imageView_Pic);

           this.onItemClickedListener = onItemClickedListener;

           myview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onItemClickedListener.onItemClicked(getAdapterPosition());

        }
    }


}

