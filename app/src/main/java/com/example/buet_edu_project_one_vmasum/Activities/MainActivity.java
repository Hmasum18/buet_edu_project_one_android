package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;

import com.example.buet_edu_project_one_vmasum.Adapters.ProblemsAdapter;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity:";


    public ProblemsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///set toolbar instead of actionbar
        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        ///set the nav drawer
        DrawerLayout drawerLayout = findViewById(R.id.mainActivityDrawerLayout);
        ActionBarDrawerToggle toggle =new  ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        adapter = new ProblemsAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.problemsRecyclerViewId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }


}