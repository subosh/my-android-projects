package com.example.subosh.restauranttrack.ownercontent;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.OrdersDialogAdminViewAdapter;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class OwnerProductsAdapter extends RecyclerView.Adapter<OwnerProductsAdapter.MyHolder> {
    Context context;
    ArrayList<ProductlistSinglenton> customerInformationArrayList;
String  price;
Float setpricestring;
DatabaseReference databaseReference,databaseReference1;
String marketname;
ArrayList<OwnerProductsPojo> productpricelist=new ArrayList<OwnerProductsPojo>();
ArrayList<String> productnamelist=new ArrayList<>();
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
ProductlistSinglenton productlistSinglenton;
    public OwnerProductsAdapter(Context c, ArrayList<ProductlistSinglenton> p, String marketname) {
        context=c;
        customerInformationArrayList=p;
        this.marketname=marketname;

    }




    @NonNull @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.ownerproductscard,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.ownerproductname.setText(customerInformationArrayList.get(position).getProductname());
        final String productImagePath=customerInformationArrayList.get(position).getProductImageDownloadPath();
        final Float productprice=customerInformationArrayList.get(position).getProductprice();
        final String productname=customerInformationArrayList.get(position).getProductname();
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(productImagePath)
                                .fit()
                                .placeholder(R.drawable.ic_default_img)
                                .into(holder.imageView);
                    }
                });

            }
        };
        new Thread(runnable).start();
holder.productprice.setText(""+productprice);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+ productname, Toast.LENGTH_SHORT).show();
            }
        });
        holder.setpricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] marketperoductschecker = {false};
                String price=holder.setprice.getText().toString();
                if (TextUtils.isEmpty(price)){
                    Toast.makeText(context,"Please Enter Price for product",Toast.LENGTH_LONG).show();
                    return;
                }
                setpricestring = Float.parseFloat(price);
                productlistSinglenton=new ProductlistSinglenton(productname,setpricestring,productImagePath);
                Toast.makeText(context, "" + setpricestring, Toast.LENGTH_LONG).show();
                updateItemPriceToCustomerMyListToFirebase(marketname,productname,productlistSinglenton);
                databaseReference = FirebaseDatabase.getInstance().getReference("OWNERS");
                databaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ProductlistSinglenton productlistSinglenton1=ds.getValue(ProductlistSinglenton.class);
                            if(productname.equals(productlistSinglenton1.getProductname())&&!marketperoductschecker[0])
                            {
                                databaseReference.child(firebaseUser.getUid()).child("MARKETPRODUCTS").child(marketname).child(ds.getKey()).setValue(productlistSinglenton);
                                marketperoductschecker[0] =true;
                            }
                         }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                       // Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

    }
    public void updateItemPriceToCustomerMyListToFirebase(final String marketname, final String productnameCheck, final ProductlistSinglenton productlistSinglenton){
boolean check=false;
        databaseReference1 = FirebaseDatabase.getInstance().getReference("CUSTOMERS");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CustomerInformation customerInformation=dataSnapshot1.getValue(CustomerInformation.class);
                    String nodes=dataSnapshot1.getKey();
                    final DatabaseReference updateItemAmountReference=databaseReference1
                            .child(nodes).child(customerInformation.getCustomername())
                            .child("MYLIST").child(marketname);

                    updateItemAmountReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren())
                                    {
                                        String mylistnode=dataSnapshot2.getKey();
                                           // GenericTypeIndicator<ArrayList<CustomerOrdersPojo>> t1 = new GenericTypeIndicator<ArrayList<CustomerOrdersPojo>>() {};
                                        String productname=dataSnapshot2.child("PRODUCTNAME").child("0").child("productname").getValue(String.class);
                                            //if(dataSnapshot2.child("PRODUCTNAME").getValue()!=null){
                                                if (productnameCheck.equals(productname))
                                                {
                                                    updateItemAmountReference.child(mylistnode).child("PRODUCTNAME").child("0").child("productamount").setValue(productlistSinglenton.getProductprice());
                                                    //Toast.makeText(context,"Succesfully updated mylist itemamount of customers To firebase",Toast.LENGTH_LONG).show();
                                                }
                                            //}
                                            }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
       // ImageView profilepic;
        TextView ownerproductname,productprice;
        EditText setprice;
        Button setpricebutton;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
setprice=(EditText)itemView.findViewById(R.id.set_price);
setpricebutton=(Button)itemView.findViewById(R.id.set_price_button);
            ownerproductname=(TextView)itemView.findViewById(R.id.ownerproductname);
            imageView=itemView.findViewById(R.id.owner_product_image);
            productprice=itemView.findViewById(R.id.productprice_owner);
            //setpricestring=setprice.getText().toString();
        }
    }
}

