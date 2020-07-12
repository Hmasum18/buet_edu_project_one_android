package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.R;
import com.example.buet_edu_project_one_vmasum.Utils.JSONBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity:";
    private Animation logo,appName;
    private  ImageView logoImage;
    private TextView appNameText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logo = AnimationUtils.loadAnimation(this,R.anim.splash_logo);
        logo.setDuration(1000);
        appName = AnimationUtils.loadAnimation(this,R.anim.splash_app_name);
        appName.setDuration(1000);

        logoImage = findViewById(R.id.splashLogoId);
        appNameText = findViewById(R.id.splashAppNameId);
        progressBar = findViewById(R.id.splashProgressBar);
        logo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ///after compelting animation step 2 is to get data form firestore
                getDataFromFireStore();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoImage.startAnimation(logo);
        appNameText.startAnimation(appName);

    }

    public void getDataFromFireStore()
    {
        progressBar.setVisibility(View.VISIBLE);
        CollectionReference colRef = FirebaseFirestore.getInstance().collection("problem");
        colRef.whereEqualTo("isReviewed",true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        /*OutputStreamWriter writer = null;
                        try {
                            writer = new OutputStreamWriter(getApplicationContext().openFileOutput("json.txt", Context.MODE_PRIVATE));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*/
                        RunTimeDB.getInstance().getProblemJsons().clear();
                        for(DocumentSnapshot snapshot : list)
                        {
                            if(snapshot.exists())
                            {
                                    try {
                                    JSONObject jsonObject = JSONBuilder.mapToJSON(snapshot.getData());
                                    RunTimeDB.getInstance().addNewProblem(jsonObject);
                                    Log.w(TAG,jsonObject.optString("title"));
                                    // RunTimeDB.getInstance().addNewProblem(jsonObject);
                                    //writer.write(jsonObject.toString()+"\n");
                                   // Log.w(TAG,jsonObject.getString("title")+"\n");
                                   // Log.w(TAG,jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /*try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}