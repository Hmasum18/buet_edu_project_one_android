package com.example.buet_edu_project_one_vmasum.DataBase;

import org.json.JSONObject;

import java.util.ArrayList;


///it is a singleton class
public class RunTimeDB {

    private static RunTimeDB runTimeDB = null ;
    private ArrayList<JSONObject> problemJsons = new ArrayList<>();


    public static RunTimeDB getInstance()
    {
        if(runTimeDB == null) runTimeDB = new RunTimeDB();
        return runTimeDB;
    }

    public void addNewProblem(JSONObject object)
    {
        problemJsons.add(object);
    }
    public ArrayList<JSONObject> getProblemJsons() {
        return problemJsons;
    }
}

