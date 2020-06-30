package com.example.subosh.restauranttrack.admincontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminDeliveredShopsListAdapter extends RecyclerView.Adapter<AdminDeliveredShopsListAdapter.MyHolder> {
    Context context;
    static ArrayList<OwnerInformation> customerInformationArrayList;
    String adminname;
    static  OwnerInformation ownerInformation;
    static String MarketNametoAdminFragment;
    AdminViewOwnerOrderFragment adminViewOwnerOrderFragment;
    DatabaseReference marketproductsdatabaseReference;
    AdminDeliveredOrderHistoryFragment adminDeliveredOrderHistoryFragment;

    // CustomerInformation customerInformation=new CustomerInformation();
    public AdminDeliveredShopsListAdapter(Context c, ArrayList<OwnerInformation> p) {
        context = c;
        customerInformationArrayList = p;
        this.adminname = adminname;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.shop_details_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        final String shopName = customerInformationArrayList.get(i).getOwnername();
        String path = customerInformationArrayList.get(i).getDownloadpath();
        final String shopPhoneNumber = customerInformationArrayList.get(i).getOwnerphone();
        myHolder.marketname.setText(shopName);
        myHolder.address.setText(customerInformationArrayList.get(i).getOwnerAddress());
        myHolder.phoneNumber.setText(shopPhoneNumber);
        Picasso.get().load(path)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(myHolder.profilepic);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OwnerInformation informationArrayList=customerInformationArrayList.get(i);
                initializeOrderHistoryFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("ownerInformation",informationArrayList);
                adminDeliveredOrderHistoryFragment.setArguments(bundle);

            }
        });

    }
    public void initializeOrderHistoryFragment(){
        adminDeliveredOrderHistoryFragment=new AdminDeliveredOrderHistoryFragment();
        FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_delivered_order_frame,adminDeliveredOrderHistoryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
public static OwnerInformation getOwnerInformationList(){
        return ownerInformation;
}
    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    public String getMarketName(String marketname) {
        return marketname;
    }

    public static String getMarketNametoAdminFragment() {
        return MarketNametoAdminFragment;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        ImageView profilepic;
        TextView marketname,address,phoneNumber;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            marketname = (TextView) itemView.findViewById(R.id.shopName);
            address = (TextView) itemView.findViewById(R.id.address);
            phoneNumber = (TextView) itemView.findViewById(R.id.phonenumber);


        }
    }
}
