package com.example.subosh.restauranttrack.customerscontent;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
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
public class CustomerMonthlyListAdapter extends RecyclerView.Adapter<CustomerMonthlyListAdapter.MyHolder> {
    Context context;
    ArrayList<CustomerOrdersPojo> customerInformationArrayList;
    ArrayList<CustomerInformation> customerInformationArrayList1=new ArrayList<>();
    String customername;
    ArrayList<CustomerOrdersPojo> checkedMonthlyList=new ArrayList<>();
    static Float grandTotal,grandSum;
    Button updateDetailsMylistButton;
    EditText updateQuantityEditText;
    Spinner measureTypeSpinner;
    ArrayList<CustomerMonthlyListPojo> customerMonthlyListPojo;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference monthlyListDatabaseReference;
    Float itemAmount=(float)0,updateAmount;
    CustomerInformation customerInformation;
    final boolean[] checker = {false};
    float getItemmAmount;
    String marketname;
    StringBuilder stringBuilder=new StringBuilder();
    public CustomerMonthlyListAdapter(Context c, ArrayList<CustomerOrdersPojo> p, ArrayList<CustomerMonthlyListPojo> customerMonthlyListPojo, String customername,String marketname, float grandTotal) {
        context = c;
        customerInformationArrayList = p;
        this.customerMonthlyListPojo=customerMonthlyListPojo;
        this.customername = customername;
        this.marketname=marketname;
        this.grandTotal=grandTotal;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.customer_monthly_list_card, viewGroup, false));
    }

//    @Override
//    public void onViewRecycled(@NonNull MyHolder holder) {
//        holder.monthlylistCheckList.setChecked(false);
//        super.onViewRecycled(holder);
//    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
final CustomerOrdersPojo customerOrdersPojo=customerInformationArrayList.get(position);

myHolder.productname.setText(customerInformationArrayList.get(position).getProductname());
final String orderNodeString=customerMonthlyListPojo.get(position).getOrderNodeString();
final boolean productchecker=customerMonthlyListPojo.get(position).getProductAvailabilityBoolean();
final String productImagePath=customerInformationArrayList.get(position).getProductImagePath();
final Float productamount=customerInformationArrayList.get(position).getProductamount();
final String measureType=customerInformationArrayList.get(position).getMeasure();
final Float quantity=customerInformationArrayList.get(position).getProductsquantity();
        Picasso.get().load(productImagePath).fit().placeholder(R.drawable.ic_default_img)
                .into(myHolder.productimage);
        myHolder.monthlylistCheckList.setOnCheckedChangeListener(null);
       myHolder.monthlylistCheckList.setChecked(customerOrdersPojo.isSelected());

        updateOrderAmount(measureType,position);
        refresh(myHolder,quantity,measureType);
myHolder.clearMylistImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
initializeMylistItemDeletionConfirmationDialog(orderNodeString);

    }
});
if (productchecker){
    myHolder.productAvailabilityStatusTextView.setText("Available");
    myHolder.productAvailabilityStatusTextView.setTextColor(ContextCompat.getColor(context,R.color.tender_button_green_click));
}
        if (!productchecker){
            myHolder.productAvailabilityStatusTextView.setText("Not Available");
            myHolder.productAvailabilityStatusTextView.setTextColor(ContextCompat.getColor(context,R.color.session_session_red));

        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+orderNodeString+" "+quantity+String.valueOf(productchecker)+position,Toast.LENGTH_LONG).show();
            }
        });
        myHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
initializeOrdersConfirmDialog(myHolder,orderNodeString,productamount);
            }
        });

myHolder.monthlylistCheckList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        customerOrdersPojo.setSelected(isChecked);
    }
});
        myHolder.setCustomerItemClickListener(new CustomerItemClickListener() {
            @Override
            public void OnItemClick(final View view, int pos) {
                CheckBox monthlylistcheckbox=(CheckBox) view;
                if(customerOrdersPojo.isSelected())
                {
                    checkedMonthlyList.add(customerInformationArrayList.get(pos));
                    if (customerInformationArrayList.get(pos).getMeasure().equals("KG")) {
                        grandTotal =grandTotal + (customerInformationArrayList.get(pos).getProductsquantity() * customerInformationArrayList.get(pos).getProductamount());
                    }
                  else if (customerInformationArrayList.get(pos).getMeasure().equals("Gram"))
                    {
                        grandTotal=grandTotal+((customerInformationArrayList.get(pos).getProductamount()/1000)*(customerInformationArrayList.get(pos).getProductsquantity()));
                    }
                    }
               else if(!customerOrdersPojo.isSelected()){
                    checkedMonthlyList.remove(customerInformationArrayList.get(pos));
                    if (customerInformationArrayList.get(pos).getMeasure().equals("KG")) {
                        grandTotal =grandTotal - (customerInformationArrayList.get(pos).getProductsquantity() * customerInformationArrayList.get(pos).getProductamount());
                    }
               else    if (customerInformationArrayList.get(pos).getMeasure().equals("Gram"))
                    {
                        grandTotal=grandTotal-((customerInformationArrayList.get(pos).getProductamount()/1000)*(customerInformationArrayList.get(pos).getProductsquantity()));
                    }
                    }
                CustomerMonthlyListFragment.setGrandTotalTextview.setText(""+getGrandTotal());
            }
        });


        }
        public void deleteMylistItemFromFirebase(final String orderNodeString) {
            final boolean[] checker1 = {false};
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            monthlyListDatabaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
            DatabaseReference mainUpadateReference = monthlyListDatabaseReference.child(firebaseUser.getUid()).child(customername).child("MYLIST").child(marketname).child(orderNodeString);
            mainUpadateReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final DatabaseReference mainUpadateReference = monthlyListDatabaseReference.child(firebaseUser.getUid()).
                            child(customername).child("MYLIST").child(marketname).child(orderNodeString);

                    if (!checker1[0]&&dataSnapshot.getKey().equals(orderNodeString)) {
                    mainUpadateReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Toast.makeText(context,"You Successfully removed this item From Your List",Toast.LENGTH_SHORT).show();
                                checker1[0]=true;
                            }
                        }
                    });

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
        public void setDetails(MyHolder myHolder, Float quantity, String measureType){
            myHolder.quantityTextView.setText(""+quantity);
            myHolder.measureTextView.setText(""+measureType);
            myHolder.amountMyListTextView.setText(""+getItemAmount());
        }
        public void refresh(final MyHolder myHolder, final Float quantity, final String measureType){
         setDetails(myHolder,quantity,measureType);
        }
        public Float getItemAmount()
        {
            return itemAmount;
        }
        public  void updateOrderAmount(final String measureType, int position){
            if (measureType.equals("KG")) {
                itemAmount = (customerInformationArrayList.get(position).getProductsquantity() * customerInformationArrayList.get(position).getProductamount());
            }
            else if (measureType.equals("Gram"))
            {
                itemAmount=((customerInformationArrayList.get(position).getProductamount()/1000)*(customerInformationArrayList.get(position).getProductsquantity()));
            }

        }
    public void initializeOrdersConfirmDialog(final MyHolder myHolder, final String orderNodeString, final Float productamount){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.custom_layout,null);
        updateDetailsMylistButton=view.findViewById(R.id.updatedetails_mylist);
        updateQuantityEditText=view.findViewById( R.id.mylist_edit_quantityEditView);
        measureTypeSpinner=view.findViewById(R.id.quantity_spinner_mylist);

       builder.setTitle("Edit Your Grocery List Item");
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

        updateDetailsMylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setOrderStatusToFirebase(marketnamerootnode,ordernodestring,customername);

                getUpdationDetailsFromUser(alertDialog,myHolder,orderNodeString,productamount);


            }
        });


    }
    private void initializeMylistItemDeletionConfirmationDialog(final String orderNodeString){
        final String Yes="YES";
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(context,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Delete Item Confirmation")
                .setTitle("Do You Want Delete this Item From Your List?")
                .setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                 deleteMylistItemFromFirebase(orderNodeString);

                    }
                });
        builder.setNegativeButton(No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getUpdationDetailsFromUser(AlertDialog alertDialog, MyHolder myHolder, String orderNodeString, Float productamount){
        String updatedQuantity=updateQuantityEditText.getText().toString();
        String updatedMeasureType=measureTypeSpinner.getSelectedItem().toString();
        if(TextUtils.isEmpty(updatedQuantity))
        {
            Toast.makeText(context,"Please Enter Quantity ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(updatedMeasureType.equals("Select Quantity Type"))
        {
            Toast.makeText(context,"Please Select Quantity Type ",Toast.LENGTH_SHORT).show();
            return;
        }
        float updateQuantityFloat=Float.parseFloat(updatedQuantity);
        alertDialog.dismiss();

        updateCustomerMyListDetailsOfFirebase(myHolder,orderNodeString,updateQuantityFloat,updatedMeasureType,productamount);
    }
    public void getCustomerDetailFromFirebase(){
        DatabaseReference databaseReference;
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    customerInformation=ds.getValue(CustomerInformation.class);
                    if (!checker[0] &&customerInformation.getCustomername().equals(customername)) {
                        customerInformationArrayList1.add(customerInformation);
                        checker[0] =true;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public ArrayList<CustomerInformation> getCustomerDetailsList(){
        return customerInformationArrayList1;
    }
    public void updateCustomerMyListDetailsOfFirebase(final MyHolder myHolder, final String orderNodeString, final float updatedQuantity, final String updatedMeasureType, final Float productamount){
        final boolean[] checker1 = {false};
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        monthlyListDatabaseReference = firebaseDatabase.getReference().child("CUSTOMERS");
        DatabaseReference mainUpadateReference=monthlyListDatabaseReference.child(firebaseUser.getUid()).child(customername).child("MYLIST").child(marketname).child(orderNodeString);
        mainUpadateReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final DatabaseReference mainUpadateReference=monthlyListDatabaseReference.child(firebaseUser.getUid()).child(customername).child("MYLIST").child(marketname).child(orderNodeString);
               // mainUpadateReference.child("DELIVERYADDRESS").setValue(getCustomerDetailsList());
                if(!checker1[0]) {

                    mainUpadateReference.child("PRODUCTNAME").child("0").child("measure").setValue(updatedMeasureType);
                    mainUpadateReference.child("PRODUCTNAME").child("0").child("productsquantity").setValue(updatedQuantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                  getItemmAmount=updateProductAmount(updatedMeasureType,updatedQuantity,myHolder.getLayoutPosition(),productamount);
                                mainUpadateReference.child("ORDEREDAMOUNT").setValue(getItemmAmount);


                                checker1[0]=true;
                                refresh(myHolder,updatedQuantity,updatedMeasureType);
                                Toast.makeText(context, "Successfully Edited Your List Item", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }


//                    customerMonthlyListAdapter.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"error in showing profiles"+" "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
public Float updateProductAmount(String updatedMeasureType, float updatedQuantity, int position, Float productamount){
    if (updatedMeasureType.equals("KG")) {
        updateAmount = (updatedQuantity* productamount);
    }
    else if (updatedMeasureType.equals("Gram"))
    {
        updateAmount=((productamount/1000)*(updatedQuantity));
    }
    return updateAmount;

}
    public static Float getGrandTotal() {
        return grandTotal;
    }

    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView clearMylistImageView,productimage;
        TextView productname,quantityTextView,measureTextView,amountMyListTextView,productAvailabilityStatusTextView;
        CheckBox monthlylistCheckList;
        Button editButton;
        CustomerItemClickListener customerItemClickListener;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productname = (TextView) itemView.findViewById(R.id.customer_monthly_list_productname);
            monthlylistCheckList= itemView.findViewById(R.id.monthly_checklist);
            quantityTextView=itemView.findViewById(R.id.quantity_textView);
            measureTextView=itemView.findViewById(R.id.measure_textView);
            amountMyListTextView=itemView.findViewById(R.id.item_amount_mylist);
            productimage=itemView.findViewById(R.id.productimageimageview);
            editButton=itemView.findViewById(R.id.edit_mylist_button);
            clearMylistImageView=itemView.findViewById(R.id.clear_mylist_imageview);
            productAvailabilityStatusTextView=itemView.findViewById(R.id.productAvailabilty_status);
            monthlylistCheckList.setOnClickListener(this);

           // monthlylistCheckList.setClickable(false);
        }
        public void setCustomerItemClickListener(CustomerItemClickListener  itemClickListener){
            this.customerItemClickListener=itemClickListener;
        }

        @Override
        public void onClick(final View v) {

                this.customerItemClickListener.OnItemClick(v,getLayoutPosition());


        }
    }
}

