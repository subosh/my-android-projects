package com.example.subosh.restauranttrack.customerscontent;





import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerViewCreateListProductsAdapter extends RecyclerView.Adapter<CustomerViewCreateListProductsAdapter.MyHolder> {
    Context context;
    ArrayList<ProductlistSinglenton> customerInformationArrayList;
    String  price;
    String setpricestring;
    String measure;
    DatabaseReference databaseReference;
    String marketname;
    ArrayList<Float> productpricelist=new ArrayList<Float>();
    ArrayList<CustomerOrdersPojo> productnamelist=new ArrayList<CustomerOrdersPojo>();
    //ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList=new ArrayList<>();
    ArrayList<String> test=new ArrayList<>();
    String customername;
    Float quantity;
    Float grandtotal=(float)0,grandsum;
    float orderGrandTotal;
    DatabaseReference monthlyListDatabaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
 static    ArrayList<String> productnameArraylist;

    public CustomerViewCreateListProductsAdapter(Context c, ArrayList<ProductlistSinglenton> p, String marketname, String customername, ArrayList<String> productnamelist) {
        context=c;
        customerInformationArrayList=p;
        this.marketname=marketname;
        this.customername=customername;
        productnameArraylist=productnamelist;

    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.create_list_card_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,final int position) {
        holder.ownerproductname.setText(customerInformationArrayList.get(position).getProductname());
        final String productname=customerInformationArrayList.get(position).getProductname();
        final Float productprice=customerInformationArrayList.get(position).getProductprice();
        final String productImagePath=customerInformationArrayList.get(position).getProductImageDownloadPath();
        final Float productamount=customerInformationArrayList.get(position).getProductprice();
        Picasso.get().load(productImagePath)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, ""+ productname, Toast.LENGTH_SHORT).show();
            }
        });
        holder.productprice.setText(""+productprice);

        holder.addtocartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String measurestring= holder.quantityMeasurespinner.getSelectedItem().toString();
                String quantitystring=holder.quantityedittext.getText().toString();
                if (measurestring.equals("Select Quantity Type"))
                {
                    Toast.makeText(context,"Please select Quantity Type for Your Product first before adding product",Toast.LENGTH_LONG).show();
                    return;
                }
                if (quantitystring.equals(""))
                {
                    Toast.makeText(context,"Please Enter Quantity for Your product before Adding product",Toast.LENGTH_LONG).show();
                    return;
                }
                quantity=Float.parseFloat(quantitystring);
                CustomerOrdersPojo customerOrdersPojo=new CustomerOrdersPojo(productname,quantity,measurestring,productamount,productImagePath);
                productnamelist.add(customerOrdersPojo);
                orderGrandTotal=orderAmount();
                if(!getproductlistfromcustomer().isEmpty()){
                    //storeCustomerMonthlyGroceryListToFirebase();
                    storeCustomerOrderstoCustomerView();
                    productnamelist.clear();
                }

                Toast.makeText(context,productname+" "+"is succesfully added to your  Grocery List for "+" "+quantity,Toast.LENGTH_SHORT).show();
            }
        });
        holder.quantityMeasurespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equals("KG")){
                    Toast.makeText(context,parent.getSelectedItem().toString()+" "+"selected for"+productname,Toast.LENGTH_SHORT).show();
                    }
                if (parent.getSelectedItem().toString().equals("Gram")){
                    Toast.makeText(context,parent.getSelectedItem().toString()+" "+"selected for"+productname,Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public static ArrayList<String> getProductnameArraylist(){
        return productnameArraylist;
    }
    public ArrayList<CustomerOrdersPojo> getproductlistfromcustomer(){
        return productnamelist;
    }
    public void storeCustomerOrderstoCustomerView() {
        DatabaseReference customerordersDatabaseReferencecustomerview;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        customerordersDatabaseReferencecustomerview = FirebaseDatabase.getInstance().getReference("CUSTOMERS");

        final DatabaseReference databaseReference=customerordersDatabaseReferencecustomerview.child(firebaseUser.getUid()).child(customername).child("MYLIST")
                .child(marketname).push();

        databaseReference.child("PRODUCTNAME").setValue(getproductlistfromcustomer()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //customerordersDatabaseReferencecustomerview.child(user.getUid()).child(customername).child("ORDERSLIST").child(marketname).child("PRODUCTNAME").child("ORDEREDGRANDTOTAL").setValue(customerCartViewAdapter.getGrandTotal());
                    databaseReference.child("ORDEREDAMOUNT").setValue(orderGrandTotal);
                    databaseReference.child("DELIVERYADDRESS").setValue(CustomerCreateListFragment.getCustomerDetailsList());
                    databaseReference.child("ORDERSTATUS").setValue("ADDEDTOLIST");
                    Toast.makeText(context, "Successfully You Added To your Grocery list", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void storeCustomerMonthlyGroceryListToFirebase(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        monthlyListDatabaseReference = firebaseDatabase.getReference().child("OWNERS");
        monthlyListDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    OwnerInformation ownerinformation = ds.getValue(OwnerInformation.class);
                    if (!getproductlistfromcustomer().isEmpty()&&ownerinformation.getOwnername().equals(marketname)) {
                        DatabaseReference customersorders=monthlyListDatabaseReference.child(ds.getKey()).child("CUSTOMERSORDERS").child(customername).push();
                        customersorders.child("DELIVERYADDRESS").setValue(CustomerCreateListFragment.getCustomerDetailsList());
                        customersorders.child("PRODUCTNAME").setValue(getproductlistfromcustomer());
                        customersorders.child("ORDERAMOUNT").setValue(orderGrandTotal);
                        customersorders.child("ORDERSTATUS").setValue("ORDERED");
                        getproductlistfromcustomer().clear();
//                        customerCartViewAdapter.notifyDataSetChanged();
//
//                        CustomerCartViewAdapter.grandsum=(float)0.0;
//                        grandtotaltextview.setText("0.0");
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context,"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public float orderAmount(){
        for (int i=0;i<productnamelist.size();i++)
        {
            if (productnamelist.get(i).getMeasure().equals("KG")) {
                grandtotal = grandtotal + (productnamelist.get(i).getProductsquantity() * productnamelist.get(i).getProductamount());
            }
            else if (productnamelist.get(i).getMeasure().equals("Gram"))
            {
                grandtotal=grandtotal+((productnamelist.get(i).getProductamount()/1000)*(productnamelist.get(i).getProductsquantity()));
            }
        }
        grandsum=grandtotal;
        grandtotal=(float)0;
        return grandsum;
    }

    public ArrayList<Float> getProductpricelist() {
        return productpricelist;
    }
    public String updatequantitymeasure(String s)
    {
        String measurement=s;
        return measurement;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView ownerproductname,productprice;
        EditText quantityedittext;
        Spinner quantityMeasurespinner;
        ImageView addtocartImageView;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            addtocartImageView=itemView.findViewById(R.id.add_to_cart);
            ownerproductname=(TextView)itemView.findViewById(R.id.customer_view_productname);
            quantityedittext=(EditText)itemView.findViewById(R.id.quantity_edittext);
            quantityMeasurespinner=(Spinner)itemView.findViewById(R.id.quantity_spinner);
            imageView=itemView.findViewById(R.id.productImageMyList);
            productprice=itemView.findViewById(R.id.productprice);

        }
    }
}

