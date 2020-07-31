package com.example.buet_edu_project_one_vmasum.Answer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.buet_edu_project_one_vmasum.Activities.ProblemActivity;
import com.example.buet_edu_project_one_vmasum.R;

import org.json.JSONArray;

import static com.example.buet_edu_project_one_vmasum.Utils.Constant.RIGHT_ANS;
import static com.example.buet_edu_project_one_vmasum.Utils.Constant.WRONG_ANS;


public class AnswerDialog {

    private final Context context;
    private final String explanation;
    private final String[] imageLinks;

    public AnswerDialog(Context context, String explanation, JSONArray ansImages) {
        this.context = context;
        this.explanation = explanation;

        if (ansImages != null) {
            imageLinks = new String[ansImages.length()];
            for (int i=0; i<ansImages.length(); i++)
                imageLinks[i] = ansImages.optString(i);
        }
        else imageLinks = null;
    }

    public void showDialog(boolean correctAns) {
        String title = correctAns ? RIGHT_ANS : WRONG_ANS;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.answer_dialog, null, false);

        TextView text = view.findViewById(R.id.explanation_text);
        text.setText(explanation);

        LinearLayout imageHolder = view.findViewById(R.id.answer_images);
        if(imageLinks!=null)
            for (String link : imageLinks) {
                ImageView image = new ImageView(context);
                Glide.with(context)
                        .load(link)
                        .fitCenter()
                        .into(image);
                image.setOnClickListener(((ProblemActivity) context)::showSingleImage);
                imageHolder.addView(image, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }

        builder.setTitle(title)
                .setView(view)
                .setCancelable(true);

        // runOnUiThread
        new Handler(Looper.getMainLooper()).post(builder::show);
    }
}
