package com.example.final_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecipeActivity extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText txt_name, txt_description, txt_price;
    String imageUrl;
    String key = "";
   String oldImageUrl = "";
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String recipename, recipeDescription, recipePrice;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        uri = data.getData();
                        recipeImage.setImageURI(uri);
                    }
                    else Toast.makeText(UpdateRecipeActivity.this,"You have not picked any photo.",Toast.LENGTH_SHORT).show();
                }
            });

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        recipeImage = (ImageView) findViewById(R.id.iv_foodImage);
        txt_description = (EditText) findViewById(R.id.txt_description);
        txt_name = (EditText) findViewById(R.id.txt_recipe_name);
        txt_price = (EditText) findViewById(R.id.txt_price);

        Bundle bundle = getIntent().getExtras();
                if (bundle != null){
                    Glide.with(UpdateRecipeActivity.this)
                            .load(bundle.getString("oldImageUrl"))
                            .into(recipeImage);
                    txt_name.setText(bundle.getString("recipeNameKey"));
                    txt_description.setText(bundle.getString("descriptionKey"));
                    txt_price.setText(bundle.getString("priceKey"));

                    key = bundle.getString("key");
                    oldImageUrl = bundle.getString("oldImageUrl");
                }

                databaseReference = FirebaseDatabase.getInstance("https://finalproject-2e04a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Recipe").child(key);

    }

    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        //startActivityForResult(photoPicker,1);
        someActivityResultLauncher.launch(photoPicker);
    }


    public void btnUpdateRecipe(View view) {
         recipename = txt_name.getText().toString().trim();
         recipeDescription = txt_description.getText().toString().trim();
         recipePrice = txt_price.getText().toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadRecipe();
                // Toast.makeText(Upload_Recipe.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  void uploadRecipe(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Updating...");
        progressDialog.show();

        FoodData foodData = new FoodData(
               recipename,
                recipeDescription,
                recipePrice,
                imageUrl
        );
        databaseReference.setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateRecipeActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateRecipeActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}