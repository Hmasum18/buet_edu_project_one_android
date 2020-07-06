package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

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
                        for(DocumentSnapshot snapshot : list)
                        {
                            if(snapshot.exists())
                            {
                                RunTimeDB.getInstance().addNewProblem(snapshot.getData());
                                //  Log.w(TAG," title "+ problem.getTitle());
                                 /*   try {
                                        JSONObject jsonObject = JSONBuilder.mapToJSON(snapshot.getData());
                                        RunTimeDB.getInstance().addNewProblem(jsonObject,snapshot.getData());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}