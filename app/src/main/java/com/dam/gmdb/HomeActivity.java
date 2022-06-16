package com.dam.gmdb;

import static com.dam.gmdb.commons.NodesNames.KEY_TITRE;
import static com.dam.gmdb.commons.NodesNames.TABLE_FILM;
import static com.dam.gmdb.commons.NodesNames.UPLOAD_PREFS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {

    /** Les vars globales**/
    private RecyclerView revFilms;
    private AdapterFilms adapterFilms;
    private Context context;
    private FirebaseFirestore db;

    /* Inits */
    public void initUI(){
        revFilms = findViewById(R.id.rv_films);
        revFilms.setHasFixedSize(true);//optimisation dans la memoire pour voir combien d items et pour reserver la taille dans la mémoire
        revFilms.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        db = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser ==null){
            startActivity(new Intent(HomeActivity.this, SignInActivity.class));
        }else {
            adapterFilms.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterFilms.startListening();
    }

    private void getDataFromFirestore(){
        Query query = db.collection(TABLE_FILM).orderBy(KEY_TITRE);

        FirestoreRecyclerOptions<ModelFilms> films =
                new FirestoreRecyclerOptions.Builder<ModelFilms>()
                        .setQuery(query, ModelFilms.class)
                        .build();

        adapterFilms = new AdapterFilms(films);

        revFilms.setAdapter(adapterFilms);
    }

    private void addSampleData(){
        SharedPreferences sharedPreferences = getSharedPreferences(R.class.getPackage().getName()
                +".prefs", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(UPLOAD_PREFS, false)){
            AddSampleDatasToFireBase.addDatasToFireBase(getApplicationContext());
        }
    }
    /* Récuperer les datas */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        addSampleData();
        getDataFromFirestore();
    }
}