package com.example.subosh.restauranttrack.admincontent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminViewOwnerOrderRequestAdapter extends RecyclerView.Adapter<AdminViewOwnerOrderRequestAdapter.MyHolder> {
    Context context;
    ArrayList<OwnerInformation> customerInformationArrayList;
    String adminname;
    static String MarketNametoAdminFragment;
    AdminViewOwnerOrderFragment adminViewOwnerOrderFragment;
    DatabaseReference marketproductsdatabaseReference;
    public AdminViewOwnerOrderRequestAdapter(Context c, ArrayList<OwnerInformation> p, String adminname) {
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
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.marketname.setText(customerInformationArrayList.get(i).getOwnername());
        final String orderDeliveryRequestStatus = customerInformationArrayList.get(i).getOwnerDeliveryRequestStatus();
        final String marketname = customerInformationArrayList.get(i).getOwnername();
        String path = customerInformationArrayList.get(i).getDownloadpath();
        final String emailview = customerInformationArrayList.get(i).getEmail();
        final String customerAddress=customerInformationArrayList.get(i).getOwnerAddress();
        final String customerPhone=customerInformationArrayList.get(i).getOwnerphone();
        //checkOrderStatus(myHolder,orderDeliveryRequestStatus);
        myHolder.shopAddress.setText(customerAddress);
        myHolder.shopPhone.setText(customerPhone);
        Picasso.get().load(path)
                .placeholder(R.drawable.ic_default_img)
                .into(myHolder.profilepic);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketNametoAdminFragment = getMarketName(marketname);
                Toast.makeText(context, orderDeliveryRequestStatus, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AdminOwnerOrderRequestActivity.class);
                intent.putExtra("adminname", adminname);
                context.startActivity(intent);


            }
        });
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
        ImageView profilepic, deliveryRequestImageView;
        TextView marketname,shopAddress,shopPhone;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            marketname = (TextView) itemView.findViewById(R.id.shopName);
            shopAddress = (TextView) itemView.findViewById(R.id.address);
            shopPhone = (TextView) itemView.findViewById(R.id.phonenumber);

        }
    }
}
