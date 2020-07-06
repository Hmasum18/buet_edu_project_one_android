package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.buet_edu_project_one_vmasum.Adapters.AnsImagePagerAdapter;
import com.example.buet_edu_project_one_vmasum.Adapters.DesImagePagerAdapter;
import com.example.buet_edu_project_one_vmasum.DataBase.Problem;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.DataBase.Schema;
import com.example.buet_edu_project_one_vmasum.R;
import com.example.buet_edu_project_one_vmasum.Utils.EnterSharedElementCallback;
import com.example.buet_edu_project_one_vmasum.Utils.TextSizeTransition;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProblemActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "ProblemActivity:";
    private Problem problem;
    private TextView probTitle,probAuthor,probCategory,probDifficulty,probCreateDate,probSeries,probDescription,probStatement;
    private  TextView probRestriction,probSchema,probOptions,probAns_type,probAnswer,probSolSchema,probExplanation;
    private ViewPager des_images,ans_images;
    private Button probSchemaButton,probSolutionSchemaButton;

    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int left = bundle.getInt("titlePosition");
        int problemIdx = bundle.getInt("problemId");
        problem = RunTimeDB.getInstance().getProblems().get(problemIdx);

       // getWindow().setEnterTransition(makeEnterTransition());

        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.probActvTitleId);
        changeBounds.addTarget(problem.getTitle());
        set.addTransition(changeBounds);

        Transition textSize = new TextSizeTransition();
        textSize.addTarget(R.id.probActvTitleId);
        textSize.addTarget(problem.getTitle());
        set.addTransition(textSize);

        /*Transition textPadding = new TestPaddingTransition();
        textSize.addTarget(R.id.probActvTitleId);
        textSize.addTarget(problem.getTitle());
        set.addTransition(textPadding);*/

        set.setDuration(500);
        getWindow().setSharedElementEnterTransition(set);
        setEnterSharedElementCallback(new EnterSharedElementCallback(this,left));


        probTitle = findViewById(R.id.probActvTitleId);
        probAuthor = findViewById(R.id.probActvAuthorId);
        probCategory = findViewById(R.id.probActvCategoryId);
        probDifficulty = findViewById(R.id.probActvDifficultyId);
        probCreateDate = findViewById(R.id.probActvCreationDateId);
        probSeries = findViewById(R.id.probActvSeriesId);
        probDescription = findViewById(R.id.probActvDescriptionId);

        des_images = findViewById(R.id.des_image_viewpager);
        DesImagePagerAdapter adapter = new DesImagePagerAdapter(this,problemIdx);
        des_images.setAdapter(adapter);

        probStatement = findViewById(R.id.probActvStatementId);
        probRestriction = findViewById(R.id.probActvRestrictionsId);
        probSchemaButton = findViewById(R.id.probActvprobSchemaButtonId);
        probSchema = findViewById(R.id.probActvProblemScemaId);
        probOptions = findViewById(R.id.probActvOptionsId);
        probAns_type = findViewById(R.id.probActvAnsTypeId);
        probAnswer = findViewById(R.id.probActvAnsId);
        probSolutionSchemaButton = findViewById(R.id.probActvSolSchemaButtonId);
        probSolSchema = findViewById(R.id.probActvSolSchemaId);

        ans_images = findViewById(R.id.ans_image_viewpager);
        AnsImagePagerAdapter ansImageAdapter = new AnsImagePagerAdapter(this,problemIdx);
        ans_images.setAdapter(ansImageAdapter);

        probExplanation = findViewById(R.id.probActvExplantionId);

        init();

    }

    public void init()
    {
        String[] categoryDescription = {"Algebra","Geometry","Number Theory","Combination","Circuit","Graph Theory","Other"};
        String[] ans_typeDescription = {"Figure Board","Text","MCQ"};

        String title = problem.getTitle();
        String author = "Author: " + problem.getAuthor();
        String category = "Category : " +categoryDescription[problem.getCategory()] ;
        String ansType = "Ans Type: " + ans_typeDescription[problem.getAns_type() ];
        String difficulty ="Difficulty: " +problem.getDifficulty();
        String date = "Creation date: " + DateFormat.getDateTimeInstance().format(new Date(problem.getTimestamp()));
        String series = "Series: " +problem.getSeries();
        String description = "Description:\n"+problem.getDescription();
        String statement = "Statement: \n" + problem.getStatement();
        String restrictions = "Restriction: \n" + problem.getRestrictions();
        String problemSchema = "Problem Schema: \n\n" + getSchemaString(problem.getProb_schema());
        String option = "Options:\n ";
        String ans_type = "Ans type : " + problem.getAns_type();
        String answer = "Answer : " + problem.getAnswer();
        String sol_schemas = "Sol schemas: \n";
        String explantion = "Explanation: \n" +problem.getExplanation() ;


        probTitle.setText(title);
        Log.w(TAG,"Gravity title"+probTitle.getGravity()+"");
        probAuthor.setText(author);
        probCategory.setText(category);
        probDifficulty.setText(difficulty);
        probCreateDate.setText(date);
        probSeries.setText(series);
        probDescription.setText(description);
        if(problem.getDes_images().size() == 0)
            des_images.setVisibility(View.GONE);
        probStatement.setText(statement);
        probRestriction.setText(restrictions);
        probSchemaButton.setOnClickListener(this);
        probSchema.setText(problemSchema);
        if(problem.getOptions().size()>0)
        {
            int i = 0 ;
            for(String s : problem.getOptions() ){
                i++;
                option+= i+".  "+s+"\n";
            }
            probOptions.setText(option);
        }else probOptions.setVisibility(View.GONE);
        probAns_type.setText(ans_type);
        probAnswer.setText(answer);
        probSolutionSchemaButton.setOnClickListener(this);
        ArrayList<Schema> list = problem.getSol_schema();
        int i = 0;
        for(Schema schema : list)
        {
            i++;
            sol_schemas += "sol "+ i+": \n"+ getSchemaString(schema) +"\n\n";
        }
        probSolSchema.setText(sol_schemas);
        if(problem.getAns_images().size() == 0)
            ans_images.setVisibility(View.GONE);
        probExplanation.setText(explantion);

    }

    private String getSchemaString(Schema schema) {


        String bgColor = schema.getBgColor();
        bgColor = bgColor.replace("0x","#");
        Log.w(TAG,"bgColor:"+bgColor);
        probSchema.setBackgroundColor(Color.parseColor(bgColor));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("DefaultCoin: ").append("\n");
        stringBuilder.append("  innerColor: "+ schema.getDefaultCoin().getInnerColor()).append("\n");
        stringBuilder.append("  outterColor: " +schema.getDefaultCoin().getOuterColor()).append("\n");
        stringBuilder.append("  isMust: "+schema.getDefaultCoin().isMust()).append("\n");
        stringBuilder.append("  skin:"+schema.getDefaultCoin().getSkin()).append("\n");
        stringBuilder.append("  useSkin: "+schema.getDefaultCoin().isUseSkin() ).append("\n");

        stringBuilder.append("DefaultMatchStick: " ).append("\n");
        stringBuilder.append("  fillColor :"+ schema.getDefaultMatchStick().getFillColor() ).append("\n");
        stringBuilder.append("  isMust:"+schema.getDefaultMatchStick().isMust()).append("\n");
        stringBuilder.append("  useSkin: "+schema.getDefaultMatchStick().isUseSkin() ).append("\n");

        stringBuilder.append("Elements: ").append("\n");
        int idx = 0;
        for(Schema.Elements element : schema.getElements())
        {
            stringBuilder.append("  id:"+idx).append("\n");
            if(element.getType().equals("matchStick"))
            {
                stringBuilder.append("  Type: matchStick" ).append("\n");
                stringBuilder.append("      fillColor :"+ element.getFillColor() ).append("\n");
                stringBuilder.append("      isMust:"+element.isMust()).append("\n");
                stringBuilder.append("      useSkin: "+element.isUseSkin() ).append("\n");
                stringBuilder.append("      indHeadX:"+ element.getIndHeadX() ).append("\n");
                stringBuilder.append("      indHeadY:"+ element.getIndHeadY() ).append("\n");
                stringBuilder.append("      indTailX:"+ element.getIndTailX()).append("\n");
                stringBuilder.append("      indTailY:"+ element.getIndTailY()).append("\n");
                stringBuilder.append("      cantMove:"+ element.isCantMove()).append("\n");
            }else if(element.getType().equals("coin"))
            {
                stringBuilder.append("  Type: coin").append("\n");
                stringBuilder.append("      innerColor: "+ element.getInnerColor()).append("\n");
                stringBuilder.append("      outterColor: " +element.getOuterColor()).append("\n");
                stringBuilder.append("      isMust: "+element.isMust()).append("\n");
                stringBuilder.append("      skin:"+element.getSkin()).append("\n");
                stringBuilder.append("      useSkin: "+element.isUseSkin() ).append("\n");
                stringBuilder.append("      indX:"+ element.getIndX()).append("\n");
                stringBuilder.append("      indY:"+ element.getIndY()).append("\n");
                stringBuilder.append("      splash_logo:" +element.getText()).append("\n");
            }
            idx++;
        }

        stringBuilder.append("indicatorColor: "+ schema.getIndicatorColor() ).append("\n");
        stringBuilder.append("isIndicator: "+ schema.isIndicator()).append("\n");
        stringBuilder.append("lineColor: "+ schema.getLineColor()).append("\n");
        stringBuilder.append("line opacity: "+schema.getLineOpacity()).append("\n\n");

        return stringBuilder.toString();
        //stringBuilder.append(""+ ).append("\n");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.probActvprobSchemaButtonId:
                if(probSchemaButton.getText().equals("Show Problem Schema"))
                {
                    probSchema.setVisibility(View.VISIBLE);
                    probSchemaButton.setText("Hide Problem Schema");
                }else
                {
                    probSchema.setVisibility(View.GONE);

                    probSchemaButton.setText("Show Problem Schema");
                }

                break;
            case R.id.probActvSolSchemaButtonId:
                if(probSolutionSchemaButton.getText().equals("Show Sol Schema"))
                {
                    probSolSchema.setVisibility(View.VISIBLE);
                    probSolutionSchemaButton.setText("Hide Sol Schema");
                }else
                {
                    probSolSchema.setVisibility(View.GONE);

                    probSolutionSchemaButton.setText("Show Sol Schema");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       // finishAfterTransition();
        super.onBackPressed();
    }
}