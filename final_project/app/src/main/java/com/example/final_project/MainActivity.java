package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    List<FoodData>myFoodList;
    FoodData mFoodData;
    MyAdapter myAdapter;
    EditText txt_Search;
    List<FoodData> otherList;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);

        myRecyclerView.setLayoutManager(gridLayoutManager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items...");
        myFoodList = new ArrayList<>();
       /* mFoodData = new FoodData("Bucatini all'Amatriciana", "Bucatini is one of our top favorite pastas! \n" +
                "It's a long thin noodle just like spaghetti, but with hole through the middle. \n" +
                "It's extra thickness gives it a better bite and holds up to a rich tomato sauce. \n" +
                "Bucatini all'Amatriciana is made with pancetta and adds extra saltiness and flavor \n" +
                "to the otherwise classic tomato sauce.", "3 Ron", R.drawable.pasta);
        myFoodList.add(mFoodData);
        mFoodData = new FoodData("London Broil with Herb Butter", "We swear by the simple marinade of olive oil, \n" +
                "lemon juice, garlic, and Worcestershire.", "2 Ron", R.drawable.meat);
        myFoodList.add(mFoodData);
        mFoodData = new FoodData("Summer Asian Slaw", "Make this salad for your next picnic, and it’ll be a guaranteed hit. \n" +
                "A tahini miso dressing gives it a creamy umami coating, while peaches add juicy pops of \n" +
                "sweetness. I finish it with toasted pepitas for crunch.", "3 Ron", "");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData("Baked ricotta cake", "Ricotta is fantastic in desserts, and this cake is no exception.", "4 Ron", R.drawable.sweet);

        myFoodList.add(mFoodData); food data introdus manual inainte de baza de date*/

        final MyAdapter myAdapter =new MyAdapter(MainActivity.this,myFoodList);
        myRecyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance("https://finalproject-2e04a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Recipe");
        txt_Search = (EditText) findViewById(R.id.txt_searchtext);

        progressDialog.show();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherList = myFoodList;
                myFoodList.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());
                    myFoodList.add(foodData);

                }
                //otherList = myFoodList;
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               myAdapter.filter(s.toString());

            }
        });

    }





    public void btnUploadActivity(View view) {
        startActivity(new Intent(this, Upload_Recipe.class));
    }
}