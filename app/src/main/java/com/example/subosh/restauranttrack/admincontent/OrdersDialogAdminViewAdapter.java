package com.example.subosh.restauranttrack.admincontent;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class OrdersDialogAdminViewAdapter extends RecyclerView.Adapter<OrdersDialogAdminViewAdapter.MyHolder> {
    Context context;
    final ArrayList<CustomerOrdersPojo> customerInformationArrayList;
    String  price;
    String setpricestring;
    DatabaseReference databaseReference;

    String marketname;
    ArrayList<String> productpricelist=new ArrayList<>();
    ArrayList<String> productnamelist=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    static float grandsum;
    public OrdersDialogAdminViewAdapter(Context context, ArrayList<CustomerOrdersPojo> customerOrderDetails,float grandtotal) {
        this.context=context;
        this.customerInformationArrayList=customerOrderDetails;
        grandsum=grandtotal;
    }


    @NonNull @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.order_dialog_adapter_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        holder.orderproductcount.setText(String.valueOf(position+1));
        final float productQuantity=customerInformationArrayList.get(position).getProductsquantity();
        holder.orderedproductquantity.setText(customerInformationArrayList.get(position).getProductsquantity()+" "+customerInformationArrayList.get(position).getMeasure());
        holder.orderedproductname.setText(customerInformationArrayList.get(position).getProductname());
        if(customerInformationArrayList.get(position).getMeasure().equals("Gram")) {
            holder.orderedproductamount.setText("" + (customerInformationArrayList.get(position).getProductamount()/1000) * customerInformationArrayList.get(position).getProductsquantity());
        }
        else if (customerInformationArrayList.get(position).getMeasure().equals("KG"))
        {
            holder.orderedproductamount.setText(""+customerInformationArrayList.get(position).getProductamount()*customerInformationArrayList.get(position).getProductsquantity());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<customerInformationArrayList.size(); i++){
                    Toast.makeText(context, ""+customerInformationArrayList.get(i), Toast.LENGTH_SHORT).show();
                }
              Toast.makeText(context,""+productQuantity,Toast.LENGTH_LONG).show();
            }

        });
    }
    public static float getGrandTotal(){
        return grandsum;
    }
    @Override

    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView orderproductcount;
        TextView orderedproductname;
        TextView orderedproductquantity;
        TextView orderedproductamount;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderproductcount=itemView.findViewById(R.id.ordered_product_count);
            orderedproductname=itemView.findViewById(R.id.ordered_product_name);
            orderedproductquantity=itemView.findViewById(R.id.ordered_product_quantity);
            orderedproductamount=itemView.findViewById(R.id.ordered_product_amount);


        }
    }
}

