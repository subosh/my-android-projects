package com.example.subosh.restauranttrack.customerscontent;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerMyListAdapter extends RecyclerView.Adapter<CustomerMyListAdapter.MyHolder> {
    Context context;
    ArrayList<OwnerInformation> customerInformationArrayList;
    String customername;
    public CustomerMyListAdapter(Context c, ArrayList<OwnerInformation> p, String customername) {
        context = c;
        customerInformationArrayList = p;
        this.customername=customername;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.shop_details_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.marketname.setText(customerInformationArrayList.get(i).getOwnername());
        final String marketname=customerInformationArrayList.get(i).getOwnername();
        String path=customerInformationArrayList.get(i).getDownloadpath();
        final String shopAddress=customerInformationArrayList.get(i).getOwnerAddress();
        final String shopPhone=customerInformationArrayList.get(i).getOwnerphone();
        final String emailview=customerInformationArrayList.get(i).getEmail();
        myHolder.shopAddress.setText(shopAddress);
        myHolder.shopPhone.setText(shopPhone);

        Picasso.get().load(path)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(myHolder.profilepic);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+ marketname, Toast.LENGTH_SHORT).show();
                if(CustomerViewOwnerProductfragment.getCustomerproductslist()!=null)
                {
                    CustomerViewOwnerProductfragment.getCustomerproductslist().clear();
                }
                Intent intent=new Intent(context,CustomerCreateListActivity.class);
                intent.putExtra("MARKETNAME",marketname);
                intent.putExtra("customername",customername);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView profilepic;
        TextView marketname,shopAddress,shopPhone;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profilepic=(ImageView) itemView.findViewById(R.id.profilepic);
            marketname=(TextView)itemView.findViewById(R.id.shopName);
            shopAddress=(TextView)itemView.findViewById(R.id.address);
            shopPhone=(TextView)itemView.findViewById(R.id.phonenumber);
        }
    }
}

