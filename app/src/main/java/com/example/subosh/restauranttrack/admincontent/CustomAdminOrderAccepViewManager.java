package com.example.subosh.restauranttrack.admincontent;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.subosh.restauranttrack.R;

public class CustomAdminOrderAccepViewManager extends LinearLayout{

private static CustomAdminOrderAccepViewManager customAdminOrderAccepViewManager;


    public CustomAdminOrderAccepViewManager(Context context, boolean check) {
        super(context);
        this.setBackgroundColor(Color.WHITE);
       // ImageView acceptanceconfirmationTewxtview = null;
        float density=context.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(Math.round((float)150*density), Math.round((float)50*density));
        ImageView  acceptanceconfirmationTewxtview=new ImageView(context);

        acceptanceconfirmationTewxtview.setImageResource(R.drawable.ic_order_confirmed);
        acceptanceconfirmationTewxtview.setLayoutParams(params);
        acceptanceconfirmationTewxtview.requestLayout();
       // removeView(acceptanceconfirmationTewxtview);
        if (check) {
            acceptanceconfirmationTewxtview.setVisibility(View.VISIBLE);
        }
        if (!check){
            acceptanceconfirmationTewxtview.setVisibility(View.GONE);
        }
        addView(acceptanceconfirmationTewxtview);
    }

}
