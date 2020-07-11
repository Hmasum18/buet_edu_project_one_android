package com.example.buet_edu_project_one_vmasum.DataBase;

import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


///if int field doesn't exist return -1;
///double field returns 0.0
///boolean field returns false
///arrayList size will be zero
//strings will return ""(empty string)
///ohter objects return null
///you can use these to chk if a filed exist or not
public class Problem {
    public static final String TAG = "Problem:";

    /// Problem properties are in alphabatical order as in FireStore
    private ArrayList<String> ans_images = new ArrayList<>();
    private int ans_type = -1;
    private String answer  = "";
    private String author = "" ;
    private int category = -1;
    private ArrayList<String> des_images = new ArrayList<>();
    private String description  = "";
    private int difficulty = -1;
    private String explanation = "";
    private ArrayList<String> options = new ArrayList<>();
    private Schema prob_schema ;
    private String restrictions = "";
    private String series = "";
    private ArrayList<Schema> sol_schema = new ArrayList<>();
    private String statement = "";
    private long timestamp = -1;
    private String title = "";

    public Problem(Map<String,Object>probMap)
    {
        Object obj = probMap.get("ans_images");
        if(obj != null)
            ans_images = (ArrayList<String>) obj;  //defualt size 0
       /* if(ans_images!=null)
        for(String s: ans_images)
            Log.w(TAG,s);*/

        obj = probMap.get("ans_type") ;
        if(obj!=null)
            ans_type = Integer.parseInt(String.valueOf(obj)); //defualt -1

        obj = (probMap.get("answer")) ;
        if(obj!=null)
            answer = String.valueOf(obj); //defualt ""

        obj = probMap.get("author");
        if(obj!=null)
            author = String.valueOf( obj ); //defualt ""

        obj = probMap.get("category") ;
        if(obj!=null)
            category = Integer.parseInt( String.valueOf(obj) );  ///defualt -1

        obj = probMap.get("des_images");
        if(obj!=null)
             des_images = (ArrayList<String>)obj ; //defualt size 0

        obj = probMap.get("description");
        if(obj!=null)
             description = String.valueOf(probMap.get("description")); ///default ""

        obj = (probMap.get("difficulty")) ;
        if(obj!=null)
            difficulty = Integer.parseInt( String.valueOf(obj) ); ///default  -1

        obj = probMap.get("explanation");
        if(obj!=null)
            explanation = String.valueOf(obj); ///defualt -1

        obj =probMap.get("options");
        if(obj!=null)
            options = (ArrayList<String>) probMap.get("options"); ///defualt size 0

        Map<String,Object> map = (Map<String, Object>) probMap.get("prob_schema");
        if(map!=null)
            prob_schema = new Schema(map); ///defualt null

        obj = probMap.get("restrictions");
        if(obj!=null)
            restrictions = String.valueOf(probMap.get("restrictions")); //defualt ""

        obj = probMap.get("series");
        if(obj!=null)
             series = String.valueOf(obj); ///defualt ""

        obj = probMap.get("sol_schema");
        if(obj!=null)
        {
            ArrayList<Map<String,Object> >mapList = (ArrayList<Map<String, Object>>)obj ;
            for(Map<String,Object> tempMap : mapList)
            {
                sol_schema.add(new Schema(tempMap)); //default size of sol_shema array is zero
            }
        }
        obj = probMap.get("statement");
        if(obj!=null)
            statement = String.valueOf(obj); //default ""

        obj = (probMap.get("timestamp")) ;
        if(obj!=null)
            timestamp = Long.parseLong(String.valueOf(obj));//default -1

        obj = probMap.get("title");
        if(obj!=null)
             title = String.valueOf(obj); //defualt ""
       // Log.w(TAG,title);
    }

    public ArrayList<String> getAns_images() { return ans_images; }
    public int getAns_type() { return ans_type; }
    public String getAnswer() { return answer; }
    public String getAuthor() { return author; }
    public int getCategory() { return category; }
    public ArrayList<String> getDes_images() { return des_images; }
    public String getDescription() { return description; }
    public int getDifficulty() { return difficulty; }
    public String getExplanation() { return explanation; }
    public ArrayList<String> getOptions() { return options; }
    public Schema getProb_schema() { return prob_schema; }
    public String getRestrictions() { return restrictions; }
    public String getSeries() { return series; }
    public ArrayList<Schema> getSol_schema() { return sol_schema; }
    public String getStatement() { return statement; }
    public long getTimestamp() { return timestamp; }
    public String getTitle() { return title; }
}
