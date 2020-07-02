package com.example.buet_edu_project_one_vmasum.DataBase;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


///it is a singleton class
public class RunTimeDB {

    private static RunTimeDB runTimeDB = null ;

    private ArrayList<Problem> problems = new ArrayList<>();


    public static RunTimeDB getInstance()
    {
        if(runTimeDB == null) runTimeDB = new RunTimeDB();
        return runTimeDB;
    }

    public void addNewProblem(Map<String,Object>probMap)
    {
        problems.add(new Problem(probMap));
    }

    public void addNewProblem(JSONObject object,Map<String,Object>probMap)
    {
        problems.add(new Problem(object,probMap));
    }

    public void addNewProblem(Problem problem)
    {
        problems.add(problem);
    }

    public ArrayList<Problem> getProblems() { return problems; }
}

