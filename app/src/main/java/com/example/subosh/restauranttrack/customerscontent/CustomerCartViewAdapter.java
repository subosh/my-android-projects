package com.example.subosh.restauranttrack.customerscontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class CustomerCartViewAdapter extends RecyclerView.Adapter<CustomerCartViewAdapter.MyHolder> {
    Context context;
    ArrayList<CustomerOrdersPojo> customerInformationArrayList;
    String  price;
    String setpricestring;
    DatabaseReference databaseReference;
    //String measuretype;
    String marketname;
    static float grandtotal,grandsum;
    float sum;
    ArrayList<String> productpricelist=new ArrayList<>();
    ArrayList<String> productnamelist=new ArrayList<>();
    Float quantity;
    public CustomerCartViewAdapter(Context c, ArrayList<CustomerOrdersPojo> p, float grandtotal) {
        context=c;
        customerInformationArrayList=p;
        this.grandtotal=grandtotal;
        }




    @NonNull @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.customer_cart_view_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.customercartproductname.setText(customerInformationArrayList.get(position).getProductname());
        quantity=customerInformationArrayList.get(position).getProductsquantity();
        final String productImagepath=customerInformationArrayList.get(position).getProductImagePath();
        Picasso.get().load(productImagepath)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imageView);
        //holder.customerproductquantity.setText(customerInformationArrayList.get(position).getProductsquantity()+" "+customerInformationArrayList.get(position).getMeasure());
        if (customerInformationArrayList.get(position).getMeasure().equals("KG")) {
            holder.customerproductamount.setText(""+customerInformationArrayList.get(position).getProductamount());
        }
        if (customerInformationArrayList.get(position).getMeasure().equals("Gram")){
        holder.customerproductamount.setText(""+((customerInformationArrayList.get(position).getProductamount()/1000)*customerInformationArrayList.get(position).getProductsquantity()));
        }
        for (int i=0;i<customerInformationArrayList.size();i++)
        {

            if (customerInformationArrayList.get(i).getMeasure().equals("KG")) {
                grandtotal = grandtotal + (customerInformationArrayList.get(i).getProductsquantity() * customerInformationArrayList.get(i).getProductamount());
            }
           else if (customerInformationArrayList.get(i).getMeasure().equals("Gram"))
            {
                grandtotal=grandtotal+((customerInformationArrayList.get(i).getProductamount()/1000)*(customerInformationArrayList.get(i).getProductsquantity()));
            }
        }
        grandsum=grandtotal;
        grandtotal=0;

        final String measuretype=customerInformationArrayList.get(position).getMeasure();
        holder.customercartproductmeasure.setText(measuretype);
        holder.customerproductquantity.setText(""+customerInformationArrayList.get(position).getProductsquantity());
        final String productname=customerInformationArrayList.get(position).getProductname();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, ""+ productname+" "+measuretype+" "+grandsum, Toast.LENGTH_SHORT).show();
            }
        });

    }

public static Float getGrandTotal(){
        return  grandsum;
}

    @Override

    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        // ImageView profilepic;
        TextView customercartproductname;
        TextView customerproductquantity;
        TextView customerproductamount;
        EditText setprice;
        TextView customercartproductmeasure;
        Button setpricebutton;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            customercartproductname=(TextView)itemView.findViewById(R.id.customer_cart_productname);
            customerproductquantity=(TextView)itemView.findViewById(R.id.customer_cart_product_quantity);
            customerproductamount=itemView.findViewById(R.id.customer_cart_product_amount);
            customercartproductmeasure=itemView.findViewById(R.id.customer_cart_product_measure);
            imageView=itemView.findViewById(R.id.productimage_cart);

        }
    }
}

