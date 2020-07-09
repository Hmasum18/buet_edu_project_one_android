package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.buet_edu_project_one_vmasum.Adapters.ProblemsAdapter;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.R;
import com.example.buet_edu_project_one_vmasum.Utils.JSONBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String TAG = "MainActivity:";

    private DrawerLayout drawerLayout;
    public ProblemsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///set toolbar instead of actionbar
        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        ///set the nav drawer
        drawerLayout = findViewById(R.id.mainActivityDrawerLayout);
        ActionBarDrawerToggle toggle =new  ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.mainActivity_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapter = new ProblemsAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.problemsRecyclerViewId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
       if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            Log.w(TAG,"ToolbarCLosed");
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.w(TAG,"nav drawer clicked");
        switch (item.getItemId())
        {
            case R.id.nav_register:
                Log.w(TAG," register clicked");
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class) );
                break;
            default:
                break;

        }
        return true;
    }
}