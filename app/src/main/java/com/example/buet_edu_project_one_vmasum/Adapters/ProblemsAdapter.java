package com.example.buet_edu_project_one_vmasum.Adapters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buet_edu_project_one_vmasum.Activities.ProblemActivity;
import com.example.buet_edu_project_one_vmasum.DataBase.Problem;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.R;
import com.example.buet_edu_project_one_vmasum.Utils.Constant;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.ProblemViewHolder> {

    private Activity activity;
    private  int  lastScaleAnimatedPosition = -1;

    public ProblemsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.problems_adapter_view,parent,false);
        return new ProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProblemViewHolder holder, final int position) {
        String[] ans_typeDescription = {"Figure Board","Text","MCQ"};

        RunTimeDB db = RunTimeDB.getInstance();
        /*Problem problem = db.getProblems().get(position);
        String title = problem.getTitle();
        String author = "-by " + problem.getAuthor()+" on " + DateFormat.getDateTimeInstance().format(new Date(problem.getTimestamp()));
        String category = "Category : " +Constant.CATEGORIES[problem.getCategory()] ;
        //Log.w("name :", title);
        String ansType = "Ans Type: " ;
        if(problem.getAns_type()!=-1)
            ansType += ans_typeDescription[problem.getAns_type() ];
        String difficulty ="Difficulty: " +problem.getDifficulty();
        String series = "Series: " +problem.getSeries();*/
        JSONObject problem = db.getProblemJsons().get(position);

        String title = problem.optString("title");   //default ""
        String author = "-by " + problem.optString("author")+" on " + DateFormat.getDateTimeInstance().format(new Date(problem.optLong("timestamp")));
        String category = "Category : " +Constant.CATEGORIES[problem.optInt("category")] ;
        //Log.w("name :", title);
        String ansType = "Ans Type: " + ans_typeDescription[problem.optInt("ans_type") ];
        String difficulty ="Difficulty: " +problem.optInt("difficulty");
        String series = "Series: " +problem.optString("series");


        holder.probTitle.setText(title);
        holder.cat_icon.setImageResource(Constant.CAT_ICONS[problem.optInt("category")]) ;
        holder.probAuthor.setText(author);
        holder.probCategory.setText(category);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f,50f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float  progress = (float)(animation.getAnimatedValue());
               String text = (int)progress +"%";
                holder.succssRateBar.setProgress((int) progress);
                holder.probSuccessRate.setText(text);
            }
        });

        holder.probSeries.setText(series);

        holder.probCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProblemActivity.class);
                intent.putExtra("problemId",position);
                intent.putExtra("titlePosition",holder.probTitle.getLeft());

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(holder.probTitle,"problemTitleTransition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                       pairs);
                activity.startActivity(intent,options.toBundle());
            }
        });

       /* if(position>lastScaleAnimatedPosition)
        {
            //set cardview aninatin
            ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f,0f,1f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(800);
            holder.probCardView.startAnimation(scaleAnimation);
            lastScaleAnimatedPosition = position;
        }*/

    }

    @Override
    public int getItemCount() {
        return RunTimeDB.getInstance().getProblemJsons().size();
    }

    public class ProblemViewHolder extends RecyclerView.ViewHolder {
         TextView probAuthor,probCategory,probTitle,probSuccessRate,probSeries;
         ImageView cat_icon ;
         ProgressBar succssRateBar;
         CardView probCardView ;
        public ProblemViewHolder(@NonNull View itemView) {
            super(itemView);
            probTitle = itemView.findViewById(R.id.probAdptrTitleId);
            cat_icon = itemView.findViewById(R.id.probAdptrCatIconId);
            probCategory = itemView.findViewById(R.id.probAdptrCategoryId);
            succssRateBar = itemView.findViewById(R.id.probAdptrSuccssRateBar);
            probSuccessRate = itemView.findViewById(R.id.probAdptrSuccssRate);
          //  probAnsType = itemView.findViewById(R.id.probAdptrAndTypeId);
          //  probDifficulty = itemView.findViewById(R.id.probAdptrDifficultyId);
            probSeries = itemView.findViewById(R.id.probAdptrSeriesId);
            probAuthor = itemView.findViewById(R.id.probAdptrAuthorId);
            probCardView = itemView.findViewById(R.id.probAdptrCardViewId);

        }
    }
}
