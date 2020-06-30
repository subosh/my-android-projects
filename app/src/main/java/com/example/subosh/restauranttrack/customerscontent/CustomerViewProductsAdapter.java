package com.example.subosh.restauranttrack.customerscontent;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.ProductlistSinglenton;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.CacheInterceptor;

public class CustomerViewProductsAdapter extends RecyclerView.Adapter<CustomerViewProductsAdapter.MyHolder> {
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
    public CustomerViewProductsAdapter(Context c, ArrayList<ProductlistSinglenton> p, String marketname, String customername) {
        context=c;
        customerInformationArrayList=p;
        this.marketname=marketname;
        this.customername=customername;

    }
    @NonNull @Override
    public CustomerViewProductsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new CustomerViewProductsAdapter.MyHolder(LayoutInflater.from(context).inflate(R.layout.customerview_ownerproduct_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,final int position) {
        holder.ownerproductname.setText(customerInformationArrayList.get(position).getProductname());
        final String productname=customerInformationArrayList.get(position).getProductname();
         final String productimagepath=customerInformationArrayList.get(position).getProductImageDownloadPath();
        final Float productamount=customerInformationArrayList.get(position).getProductprice();
        Picasso.get().load(productimagepath)
                .fit()
                .placeholder(R.drawable.ic_default_img)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, ""+ productname+productimagepath, Toast.LENGTH_SHORT).show();
            }
        });
        holder.productprice.setText(""+productamount);
        holder.addtocartbutton.setOnClickListener(new View.OnClickListener() {
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
                CustomerOrdersPojo customerOrdersPojo=new CustomerOrdersPojo(productname,quantity,measurestring,productamount,productimagepath);
                productnamelist.add(customerOrdersPojo);
                Toast.makeText(context,productname+" "+"is succesfully added to yout cart for "+" "+quantity,Toast.LENGTH_SHORT).show();
            }
        });
       holder.quantityMeasurespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(parent.getSelectedItem().toString().equals("KG")){
                   Toast.makeText(context,parent.getSelectedItem().toString()+" "+"selected for"+productname,Toast.LENGTH_SHORT).show();
//                   measure=updatequantitymeasure(parent.getSelectedItem().toString());
//                   test.add(measure);

;               }
               if (parent.getSelectedItem().toString().equals("Gram")){
                   Toast.makeText(context,parent.getSelectedItem().toString()+" "+"selected for"+productname,Toast.LENGTH_SHORT).show();
//                   measure=updatequantitymeasure(parent.getSelectedItem().toString());
//                   test.add(measure);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       }
    public ArrayList<CustomerOrdersPojo> getproductlistfromcustomer(){
        return productnamelist;
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
        Button addtocartbutton;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //setprice=(EditText)itemView.findViewById(R.id.set_price);
             addtocartbutton=(Button)itemView.findViewById(R.id.add_to_cart);
            ownerproductname=(TextView)itemView.findViewById(R.id.customer_view_productname);
            quantityedittext=(EditText)itemView.findViewById(R.id.quantity_edittext);
            quantityMeasurespinner=(Spinner)itemView.findViewById(R.id.quantity_spinner);
            imageView=itemView.findViewById(R.id.owner_product_image);
            productprice=itemView.findViewById(R.id.productprice_freshorder);
            //setpricestring=setprice.getText().toString();
        }
    }
}


