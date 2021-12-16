package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> myFoodList;
    private int lastPosition = -1;

    public MyAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent, false);

        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {

        Glide.with(mContext)
                .load(myFoodList.get(position).getItemImage())
                .into(holder.imageView);

       // holder.imageView.setImageResource(myFoodList.get(position).getItemImage());
        holder.mTitle.setText(myFoodList.get(position).getItemName());
        holder.mDescription.setText(myFoodList.get(position).getItemDescription());
        holder.mPrice.setText(myFoodList.get(position).getItemPrice());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Image",myFoodList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",myFoodList.get(holder.getAdapterPosition()).getItemDescription());
                intent.putExtra("RecipeName",myFoodList.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("price",myFoodList.get(holder.getAdapterPosition()).getItemPrice());

                intent.putExtra("keyValue",myFoodList.get(holder.getAdapterPosition()).getKey());
                mContext.startActivity(intent);
            }
        });

        setAnimation(holder.itemView, position);

    }

    public  void setAnimation(View view, int position){

        if(position> lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1500);
            view.startAnimation(animation);
            lastPosition = position;

        }

    }

    @Override
    public int getItemCount() {

        return myFoodList.size();
    }

    public void filter(String text) {

        ArrayList<FoodData> filterList = new ArrayList<>();
        for(FoodData item: myFoodList){
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        myFoodList = filterList;
        notifyDataSetChanged();
    }

    public void filteredList(String filterList) {
        alertView("You really want this?");
        //notifyDataSetChanged();
    }
    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle( "Hello" )
                .setMessage(message)
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription, mPrice;
    CardView mCardView;

    public FoodViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mPrice = itemView.findViewById(R.id.tvPrice);
        mCardView = itemView.findViewById(R.id.myCardView);
    }
}