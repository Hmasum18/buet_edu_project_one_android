package com.example.buet_edu_project_one_vmasum.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.buet_edu_project_one_vmasum.DataBase.Problem;
import com.example.buet_edu_project_one_vmasum.DataBase.RunTimeDB;
import com.example.buet_edu_project_one_vmasum.R;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

public class AnsImagePagerAdapter extends PagerAdapter {


    public static final String TAG = "AnsImagePagerAdapter:";
    Context context;
    int idx;
    private Problem problem;

    public AnsImagePagerAdapter(Context context, int idx)
    {
        this.context = context;
        this.idx = idx;
//        problem = RunTimeDB.getInstance().getProblems().get(idx);
    }

    @Override
    public int getCount() {
        return problem.getAns_images().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ArrayList<String> ans_image = problem.getAns_images();


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewpager_image_view,container,false);
        ZoomageView zoomageView = view.findViewById(R.id.des_image_view_ZV);

        String url = null;
        if(ans_image.size()!=0)
        {
            url = ans_image.get(position);
            Log.w(TAG,problem.getTitle()+":"+url);
            Log.w(TAG,"Showing image");
            // Picasso.with(context).load(url).fit().centerCrop().into(zoomageView);
            Glide.with(context).load(url).fitCenter().into(zoomageView);
        }
        else zoomageView.setVisibility(View.GONE);
        CardView cardView = view.findViewById(R.id.des_image_view_cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"haha",Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view,0);
        return view;
    }
}
