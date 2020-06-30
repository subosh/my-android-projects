package com.example.subosh.restauranttrack.ownercontent;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.subosh.restauranttrack.R;

public class CustomOwnerOrderRequestViewManager extends LinearLayout {
    public CustomOwnerOrderRequestViewManager(Context context) {
        super(context);
        this.setBackgroundColor(Color.WHITE);
        float density=context.getResources().getDisplayMetrics().density;
        final  ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(Math.round((float)100*density), Math.round((float)50*density));
        ImageView acceptanceconfirmationTewxtview=new ImageView(context);
        acceptanceconfirmationTewxtview.setImageResource(R.drawable.order_accepted_image);
        acceptanceconfirmationTewxtview.setLayoutParams(params);
        acceptanceconfirmationTewxtview.requestLayout();
        addView(acceptanceconfirmationTewxtview);
    }
}
