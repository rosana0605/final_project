package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {


    TextView foodDescription;
    ImageView foodImage;
    String key = "";
    String imageUrl = "";
     TextView RecipeName;
    TextView RecipePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        RecipeName = (TextView) findViewById(R.id.txt_Recipe_name);
        RecipePrice = (TextView) findViewById(R.id.txtPrice);

        foodDescription = (TextView) findViewById(R.id.txtDescription);
        //foodDescription = (TextView) findViewById(R.id.txtDescription);
        foodImage = (ImageView) findViewById(R.id.ivImage2);
        

        Bundle mBundle = getIntent().getExtras();

        if(mBundle!=null){

            foodDescription.setText(mBundle.getString("Description"));
            RecipeName.setText(mBundle.getString("RecipeName"));
            RecipePrice.setText(mBundle.getString("price"));
            //foodImage.setImageResource(mBundle.getInt("Image"));
            key = mBundle.getString("keyValue");
            imageUrl = mBundle.getString("Image");


            Glide.with(this).load(mBundle.getString("Image"))
            .into(foodImage);
        }
    }

    public void btnDeleteRecipe(View view) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://finalproject-2e04a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Recipe");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(imageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(key).removeValue();
                Toast.makeText(DetailActivity.this,"Recipe Deleted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class) );
                finish();
            }
        });
    }

    public void btnUpdateRecipe(View view) {
        startActivity(new Intent(getApplicationContext(),UpdateRecipeActivity.class)
        .putExtra("recipeNameKey",RecipeName.getText().toString())
                .putExtra("descriptionKey",foodDescription.getText().toString())
                .putExtra("priceKey",RecipePrice.getText().toString())
                .putExtra("oldImageUrl",imageUrl)
                .putExtra("key",key));
    }
}