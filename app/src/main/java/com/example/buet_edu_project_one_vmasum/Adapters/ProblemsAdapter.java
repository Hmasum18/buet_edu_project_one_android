package com.example.buet_edu_project_one_vmasum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buet_edu_project_one_vmasum.Activities.ProblemActivity;
import com.example.buet_edu_project_one_vmasum.DataBase.Problem;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.R;

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.ProblemViewHolder> {

    private Context context;

    public ProblemsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.problems_adapter_view,parent,false);
        return new ProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemViewHolder holder, final int position) {
        String[] categoryDescription = {"Algebra","Geometry","Number Theory","Combination","Circuit","Graph Theory","Other"};
        String[] ans_typeDescription = {"Figure Board","Text","MCQ"};

        RunTimeDB db = RunTimeDB.getInstance();
        Problem problem = db.getProblems().get(position);
        String title = problem.getTitle();
        String author = "Author: " + problem.getAuthor();
        String category = "Category : " +categoryDescription[problem.getCategory()] ;
        String ansType = "Ans Type: " + ans_typeDescription[problem.getAns_type() ];
        String difficulty ="Difficulty: " +problem.getDifficulty();
        String series = "Series: " +problem.getSeries();

        holder.probTitle.setText(title);
        holder.probAuthor.setText(author);
        holder.probCategory.setText(category);
        holder.probAnsType.setText(ansType);
        holder.probDifficulty.setText(difficulty);
        holder.probSeries.setText(series);

        holder.probCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProblemActivity.class);
                intent.putExtra("problemId",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RunTimeDB.getInstance().getProblems().size();
    }

    public class ProblemViewHolder extends RecyclerView.ViewHolder {
         TextView probAuthor,probCategory,probTitle,probAnsType,probDifficulty,probSeries;
         CardView probCardView ;
        public ProblemViewHolder(@NonNull View itemView) {
            super(itemView);
            probAuthor = itemView.findViewById(R.id.probAdptrAuthorId);
            probCategory = itemView.findViewById(R.id.probAdptrCategoryId);
            probTitle = itemView.findViewById(R.id.probAdptrTitleId);
            probAnsType = itemView.findViewById(R.id.probAdptrAndTypeId);
            probDifficulty = itemView.findViewById(R.id.probAdptrDifficultyId);
            probSeries = itemView.findViewById(R.id.probAdptrSeriesId);
            probCardView = itemView.findViewById(R.id.probAdptrCardViewId);

        }
    }
}
