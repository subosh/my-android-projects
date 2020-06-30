package com.example.subosh.restauranttrack.admincontent;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerViewOwnerProduct;
import com.example.subosh.restauranttrack.customerscontent.CustomerViewOwnerProductfragment;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.example.subosh.restauranttrack.ownercontent.OwnerProductsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdminViewOwnerOrderAdapter extends RecyclerView.Adapter<AdminViewOwnerOrderAdapter.MyHolder> {
    Context context;
    ArrayList<OwnerInformation> customerInformationArrayList;
    String adminname;
    static String MarketNametoAdminFragment;
    AdminViewOwnerOrderFragment adminViewOwnerOrderFragment;
    DatabaseReference marketproductsdatabaseReference;
    // CustomerInformation customerInformation=new CustomerInformation();
    public AdminViewOwnerOrderAdapter(Context c, ArrayList<OwnerInformation> p, String adminname) {
        context = c;
        customerInformationArrayList = p;
        this.adminname=adminname;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.shop_details_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.marketname.setText(customerInformationArrayList.get(i).getOwnername());
        final String orderDeliveryRequestStatus=customerInformationArrayList.get(i).getOwnerDeliveryRequestStatus();
        final String marketname=customerInformationArrayList.get(i).getOwnername();
        final String shopAddress=customerInformationArrayList.get(i).getOwnerAddress();
        final String shopPhone=customerInformationArrayList.get(i).getOwnerphone();
        String path=customerInformationArrayList.get(i).getDownloadpath();
        final String emailview=customerInformationArrayList.get(i).getEmail();
        myHolder.shopAddress.setText(shopAddress);
        myHolder.shopPhone.setText(shopPhone);
        checkOrderStatus(myHolder,orderDeliveryRequestStatus);
        Picasso.get().load(path)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(myHolder.profilepic);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketNametoAdminFragment=getMarketName(marketname);
                Toast.makeText(context,orderDeliveryRequestStatus,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,AdminViewOwnerOrderActivity.class);
                intent.putExtra("adminname",adminname);
                context.startActivity(intent);



            }
        });
    }
    public void checkOrderStatus(final MyHolder myHolder, final String orderDeliveryRequestStatus){
        if (orderDeliveryRequestStatus.equals("NO")){
            updateUiforDeliveryRequest(myHolder);
        }
        }

    public void updateUiforDeliveryRequest(final MyHolder myHolder){
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                      // myHolder.deliveryRequestImageView.setVisibility(View.VISIBLE);
                         CustomOwnerOrderAdminRequestViewManager customOwnerOrderAdminRequestViewManager=new CustomOwnerOrderAdminRequestViewManager(context.getApplicationContext());
                        myHolder.deliveryRequestLinearLayout.addView(customOwnerOrderAdminRequestViewManager);
                    }
                });

            }
        };
        new Thread(runnable).start();
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


    class MyHolder extends RecyclerView.ViewHolder{
        ImageView profilepic;
        LinearLayout deliveryRequestLinearLayout;
        TextView marketname,shopAddress,shopPhone;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profilepic=(ImageView) itemView.findViewById(R.id.profilepic);
            marketname=(TextView)itemView.findViewById(R.id.shopName);
            shopAddress=(TextView)itemView.findViewById(R.id.address);
            shopPhone=(TextView)itemView.findViewById(R.id.phonenumber);
            deliveryRequestLinearLayout=itemView.findViewById(R.id.delivery_request_linear_layout);


        }
    }
}

