package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.buet_edu_project_one_vmasum.Answer.AnswerDialog;
import com.example.buet_edu_project_one_vmasum.Answer.AnswerLayout;
import com.example.buet_edu_project_one_vmasum.Answer.BoardMatcher;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.Graph.GraphView;
import com.example.buet_edu_project_one_vmasum.R;
import com.example.buet_edu_project_one_vmasum.Utils.Constant;
import com.example.buet_edu_project_one_vmasum.Utils.EnterSharedElementCallback;
import com.example.buet_edu_project_one_vmasum.Utils.TextSizeTransition;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.buet_edu_project_one_vmasum.Utils.Constant.ANSWER_BOARD;
import static com.example.buet_edu_project_one_vmasum.Utils.Constant.ANSWER_MCQ;
import static com.example.buet_edu_project_one_vmasum.Utils.Constant.ANSWER_TEXT;

public class ProblemActivity extends AppCompatActivity {

    public static final String TAG = "ProblemActivity:";
    private FloatingActionButton addToBoard;
    private boolean fabIsOpen = false;
    private ImageView addCoinToBoard;
    private ImageView addStickToBoard;

    private GraphView graph;

    private ConstraintLayout graphHolder;
    private ScrollView scrollQuestion;
    private HorizontalScrollView scrollDescriptionImages;
    private LinearLayout questionImageHolder;

    private AnswerLayout answerLayout;
    private ChipGroup questionTags;

    private Animation fabOpen;
    private Animation fabClose;
    private Animation addPaneOpen;
    private Animation addPaneClose;

    private int answerType;
    private String answer; // Store Ans Here in case of MCQ or Text

    private TextView statementText;
    private TextView restrictionText;

    private JSONObject problem;

    private TextView title ;

    public static Transition makeEnterTransition() {
        Transition fade = new Explode();
        fade.setDuration(600);
        fade.setInterpolator(new AccelerateDecelerateInterpolator());
        return fade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        ///masum
        ///start
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int left = bundle.getInt("titlePosition");
        int problemIdx = bundle.getInt("problemId");
        problem = RunTimeDB.getInstance().getProblemJsons().get(problemIdx);
        
        getWindow().setEnterTransition(makeEnterTransition());

        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.probActvTitleId);
        changeBounds.addTarget(problem.optString("title"));
        set.addTransition(changeBounds);

        Transition textSize = new TextSizeTransition();
        textSize.addTarget(R.id.probActvTitleId);
        textSize.addTarget(problem.optString("title"));
        set.addTransition(textSize);

        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        getWindow().setSharedElementEnterTransition(set);
        setEnterSharedElementCallback(new EnterSharedElementCallback(this,left));
        //end

        init(); // Assign id from Resources

        addToBoard.hide(); // Hide FAB by Default, No adding

        showProblem();

    }

    public void showProblem()
    {

        try {
           // problem = new JSONObject(brilliant);

            // Title and other Text (MUST)
            String probTitle = problem.getString("title");
            title.setText(probTitle);

            // MUST
            statementText.setText(problem.getString("statement"));

            // OPTIONAL
            String restriction = problem.optString("restrictions");
            if(restriction.equals(""))
                restrictionText.setVisibility(View.GONE);
            else {
                restrictionText.setVisibility(View.VISIBLE);
                restrictionText.setText(restriction);
            }

            // Tags
            addTag(problem.optString("series"));
            addTag("Difficulty: "+problem.optInt("difficulty")+"/10");
            addTag("by "+problem.optString("author"));
            if (problem.has("category")) addTag(Constant.CATEGORIES[problem.getInt("category")]);

            // Problem Board
            JSONObject probSchema = problem.getJSONObject("prob_schema");
            graph.setBoardContent(probSchema);

            // Description Image
            /// TEST
//            JSONArray mockImages = new JSONArray();
//            mockImages.put("https://bueteduproject1.s3.ap-south-1.amazonaws.com/problem_images/1593656976430_d_0.jpg");
//            mockImages.put("https://bueteduproject1.s3.ap-south-1.amazonaws.com/problem_images/1593656976430_a_1.jpg");
//            problem.put("des_images", mockImages);

            if (problem.has("des_images")) {
                Log.d(TAG, "showProblem: Adding Images");
                scrollDescriptionImages.setVisibility(View.VISIBLE);
                JSONArray description_images = problem.getJSONArray("des_images");

                for (int i=0; i<description_images.length(); i++)
                    addDescriptionImage(description_images.getString(i));
            }

            // Setting Default Stick and Default Coin for add Pane
            JSONObject defaultStick = probSchema.getJSONObject("defaultMatchStick");
            Drawable stick = graph.setDefaultStick(defaultStick.optBoolean("useSkin"), defaultStick.getString("fillColor"));
            addStickToBoard.setImageDrawable(stick);

            JSONObject defaultCoin = probSchema.getJSONObject("defaultCoin");
            Bitmap coin = graph.setDefaultCoin(
                    defaultCoin.has("useSkin") && defaultCoin.getBoolean("useSkin"), // If not defined, false (by default)
                    defaultCoin.has("skin") ? defaultCoin.getInt("skin") : -1 , // If not defined, -1 (by default)
                    defaultCoin.getString("innerColor"),
                    defaultCoin.getString("outerColor")
            );

            if (coin != null)
                addCoinToBoard.setImageBitmap(coin);

            // Solution Type Setting
            answerType = problem.optInt("ans_type");

            if (answerType == ANSWER_TEXT) {
                // Set AnswerLayout to have a EditText
                answerLayout.setAsText();

                // Retrieve Answer from JSON
                answer = problem.getString("answer");

            } else if (answerType == ANSWER_MCQ) {
                JSONArray options = problem.getJSONArray("options");

                // Set AnswerLayout to have multiple radio buttons
                answerLayout.setAsMCQ(options);

                // Set Answer String from answerIndex
                answer = options.getString(problem.getInt("answer"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        //masum
        title = findViewById(R.id.probActvTitleId);

        addToBoard = findViewById(R.id.add_button);
        addCoinToBoard = findViewById(R.id.default_coin);
        addStickToBoard = findViewById(R.id.default_stick);

        graph = findViewById(R.id.graph);
        graphHolder = findViewById(R.id.graph_holder);
        scrollQuestion = findViewById(R.id.scroll_ques);
        answerLayout = findViewById(R.id.answer_container);
        questionTags = findViewById(R.id.tags);
        scrollDescriptionImages = findViewById(R.id.question_image_scroll);
        questionImageHolder = findViewById(R.id.question_image_holder);

        statementText = findViewById(R.id.question_text);
        restrictionText = findViewById(R.id.answer_constrain);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.add_fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.add_fab_close);
        addPaneOpen = AnimationUtils.loadAnimation(this, R.anim.add_fab_drawer_open);
        addPaneClose = AnimationUtils.loadAnimation(this, R.anim.add_fab_drawer_close);

        fabOpen.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                addCoinToBoard.startAnimation(addPaneOpen);
                addStickToBoard.startAnimation(addPaneOpen);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        fabClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                addCoinToBoard.startAnimation(addPaneClose);
                addStickToBoard.startAnimation(addPaneClose);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        addPaneOpen.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                addCoinToBoard.setVisibility(View.VISIBLE);
                addStickToBoard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        addPaneClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                addCoinToBoard.setVisibility(View.INVISIBLE);
                addStickToBoard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        addCoinToBoard.setOnTouchListener((v, event) -> {
            // Sending (x, y) from Screen LeftTop (not relative to View)
            graph.addCoin(event.getRawX(), event.getRawY());

            // Close The FAB
            toggleAddFAB(null);

            // Touch not Handled, Should be Handled by GraphView
            return false;
        });

        addStickToBoard.setOnTouchListener((v, event) -> {
            // Sending (x, y) from Screen LeftTop (not relative to View)
            graph.addStick(event.getRawX(), event.getRawY());

            // Close the FAB
            toggleAddFAB(null);

            // Touch not Handled, Should be Handled by GraphView
            return false;
        });

        scrollDescriptionImages.setVisibility(View.GONE);
    }

    public void toggleAddFAB(View view) {
        if (fabIsOpen) {
            // If FAB is Open, Close now
            addToBoard.startAnimation(fabClose);
            if (view != addToBoard) {
                addCoinToBoard.setVisibility(View.INVISIBLE);
                addStickToBoard.setVisibility(View.INVISIBLE);
            }
        } else {
            // FAB is Closed, Open now
            addToBoard.startAnimation(fabOpen);
        }

        // FAB open status is reversed
        fabIsOpen = !fabIsOpen;
    }

    private void addTag(String tagText) {
        Chip tag = new Chip(this);
        tag.setText(tagText);

        // Add Chips to Chip Holder
        questionTags.addView(tag);
    }

    private void addDescriptionImage(String imageLink) {
        ImageView image = new ImageView(this);
        Glide.with(this)
                .load(imageLink)
                .fitCenter()
                .into(image);
        // image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setOnClickListener(this::showSingleImage);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        questionImageHolder.addView(image, params);
    }

    public void showSingleImage(View v) {
        ImageView view = new ImageView(this);
        view.setImageDrawable(((ImageView)v).getDrawable());
        new AlertDialog.Builder(this)
                .setTitle("Image")
                .setView(view)
                .show();
    }

    public void showDetails(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            // Set Series As Title, Description as Message in AlertDialog
            builder.setTitle(problem.getString("series"))
                    .setMessage(problem.getString("description"))
                    .setCancelable(true)
                    .show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkAnswer(View v) {
        AnswerDialog dialog = new AnswerDialog(this, problem.optString("explanation"), problem.optJSONArray("ans_images"));
        switch (answerType) {
            // Text and MCQ Checking Process is Same
            case ANSWER_TEXT:
            case ANSWER_MCQ: {
                dialog.showDialog(answer.equals(answerLayout.getAsText()));
                break;
            }
            case ANSWER_BOARD: {
                // Match Board in New AsyncTask Thread
                new BoardMatcher(dialog, graph, problem.optJSONArray("sol_schema")).execute();
                break;
            }
        }
    }



    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }
}