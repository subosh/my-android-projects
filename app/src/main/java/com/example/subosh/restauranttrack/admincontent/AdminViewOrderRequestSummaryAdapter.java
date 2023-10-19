package com.example.subosh.restauranttrack.admincontent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.example.subosh.restauranttrack.ownercontent.OrderSummaryListFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AdminViewOrderRequestSummaryAdapter extends RecyclerView.Adapter<AdminViewOrderRequestSummaryAdapter.MyHolder> {
Context context;
static ArrayList<AdminViewOrdersSummaryPojo> customerInformationArrayList;
static ArrayList<CustomerOrdersPojo> customerOrders;
static ArrayList<Map<String,Double>> coordinatesList;
static  Float orderamountfinal;
        String  price;
        RecyclerView recyclerView;
        Button deliveryconfirmationButton,orderDeliveredConfirmButton;
        TextView grandtotaltextview,shopNameText,customerNameText;
        ImageView orderlistImage,locationShareImage;
        // ArrayList<CustomerOrdersPojo> productname;
        String setpricestring;
        DatabaseReference orderstatusdatabaserefernce,orderDeliveredAdminDatabaseReference,pushNodeDatabaseReference;
        String marketname;
        FirebaseDatabase firebaseDatabase;
        ShareLocationFragment shareLocationFragment;
        OrderSummaryListFragment orderSummaryListFragment;
        ArrayList<OwnerInformation> ownerInformationArrayList;
        String productaddingtime,productaddingdate,productId;
        String productIdGeneration;
        String code;
        String productIdArray[];
        String marketnamerootnode;
        FirebaseAuth.AuthStateListener authStateListener;
        ArrayList<CustomerOrdersPojo> productdetaillist;
        ArrayList<String> customernamelist=new ArrayList<>();
        ArrayList<String> checkingorderstatusbooleanlist=new ArrayList<>();
        OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;

//    String orderstatus;
        String currentadminname;
        ArrayList<CustomerInformation> customerDetailsList=new ArrayList<>();


public AdminViewOrderRequestSummaryAdapter(Context c, ArrayList<AdminViewOrdersSummaryPojo> p, String adminname, String marketname) {
        context=c;
        customerInformationArrayList=p;
        currentadminname=adminname;
        customerDetailsList=customerDetailsList;
        this.marketname=marketname;

        }
@NonNull
@Override
public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.adminrequestviewcard,viewGroup,false));
        }

@Override
public void onBindViewHolder(@NonNull final MyHolder holder, final int  position) {
        holder.ordersummarycustomername.setText(customerInformationArrayList.get(position).getCustomerDeliveryDetailsList().get(0).getCustomeraddress());
        final ArrayList<CustomerInformation> customerDeliveryDetails=customerInformationArrayList.get(position).getCustomerDeliveryDetailsList();
final String customername = customerInformationArrayList.get(position).getCustomername();
final  ArrayList<Map<String,Double>> customerCoordinatesDetails=customerInformationArrayList.get(position).getCoordinateDataList();
//        final String customerAddress=customerDetailsList.get(position).getCustomeraddress();
final String ordernodestring = customerInformationArrayList.get(position).getOrdernodes();
        String orderstatus = customerInformationArrayList.get(position).getOrderstatus();
        final String customerPhoneNumber=customerInformationArrayList.get(position).getCustomerDeliveryDetailsList().get(0).getCustomerphone();
final ArrayList<CustomerOrdersPojo> productname = customerInformationArrayList.get(position).getCustomerOrdersPojos();
final Float orderamount = customerInformationArrayList.get(position).getOrderamount();
        String ownerdeliveryRequeststatus = customerInformationArrayList.get(position).getDeliveryRequestStatus();
        final String orderedDate=customerInformationArrayList.get(position).getOrderedDate();
        final String orderedTime=customerInformationArrayList.get(position).getOrderedTime();
        final String orderId=customerInformationArrayList.get(position).getOrderId();
        holder.customerPhone.setText(customerPhoneNumber);
        holder.orderedDate.setText(orderedDate);
        holder.orderedTime.setText(orderedTime);
        holder.orderId.setText(orderId);
        marketnamerootnode =AdminOwnerOrderRequestFragment.getMarketnamenode();
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderamount);
        coordinatesList=getCustomerCoordinateDataList(customerCoordinatesDetails);
        holder.linearLayout.setVisibility(View.GONE);
        holder.orderAcceptedText.setVisibility(View.GONE);
        if (orderstatus.equals("ORDERED")) {
        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.orderaccepbutton.setVisibility(View.VISIBLE);
        holder.showorderdetails.setVisibility(View.GONE);
        }
        if (orderstatus.equals("ORDERACCEPTEDBYADMIN") ||orderstatus.equals("ORDERACCEPTEDBYADMIN")) {
        holder.orderaccepbutton.setVisibility(View.GONE);
        holder.showorderdetails.setVisibility(View.VISIBLE);
        boolean check=true;
        CustomAdminOrderAccepViewManager   customAdminOrderAccepViewManager = new CustomAdminOrderAccepViewManager(context.getApplicationContext(),check);
        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.linearLayout.addView(customAdminOrderAccepViewManager);
        holder.orderAcceptedText.setVisibility(View.VISIBLE);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Toast.makeText(context, ordernodestring,Toast.LENGTH_SHORT).show();
        }
        });
        holder.orderaccepbutton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderamount);
        initializeOrdersConfirmDialog(holder, marketnamerootnode, ordernodestring, customername);
        }
        });
        holder.showorderdetails.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderamount);
        initializeOrderInfoDialog(holder,ordernodestring,customername,marketnamerootnode,marketname,customername,customerOrders,orderamountfinal,customerCoordinatesDetails,customerDeliveryDetails,orderedDate,orderedTime,orderId);
        }
        });

        }
@Override
public int getItemCount() {
        return customerInformationArrayList.size();
        }
public String getMarketnamerootnode() {
        return marketnamerootnode;
        }
        public String getProductId() {
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat savecurrentdate=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                productaddingdate= savecurrentdate.format(calendar.getTime());
                SimpleDateFormat savecurrenttime=new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
                SimpleDateFormat savecurrenttimeforproductId=new SimpleDateFormat("HH:mm:ss ", Locale.getDefault());
                productaddingtime= savecurrenttime.format(calendar.getTime());
                productIdGeneration=savecurrenttimeforproductId.format(calendar.getTime());
                Random random = new Random();
                int n = 1000 + random.nextInt(9000);
                code= String.valueOf(n);
                productIdArray=productIdGeneration.split(":");
                productId=code+productIdArray[0]+productIdArray[1]+productIdArray[2];
                return productId;
        }

        public String getProductaddingdate() {
                return productaddingdate;
        }

        public String getProductaddingtime() {
                return productaddingtime;
        }

private void setOrderStatusToFirebase(final String marketnamerootnode, final String ordernodestring, final String customernamefinal, final String currentOrderStatus, final AlertDialog alertDialog, final String toastMessage, final String deliveredDatefinal, final String deliveredTimefinal, final String progressMessage){
        firebaseDatabase=FirebaseDatabase.getInstance();
        orderstatusdatabaserefernce=firebaseDatabase.getReference("OWNERS");
        final boolean[] checker = {false};
        final ProgressDialog progressDialog=new ProgressDialog(this.context);
        orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
        String customername = dataSnapshot1.getKey();
        DatabaseReference databaseReference=orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERORDERS").child(customername).child(ordernodestring);
        DatabaseReference orderstatuschild = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring).child("ORDERSTATUS");
        DatabaseReference ordercaretakeradminname = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring).child("ORDERCARETAKER");
                DatabaseReference deliveredDate = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring).child("DELIVEREDDATE");
                DatabaseReference deliveredTime = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring);


        if (customername.equals(customernamefinal)&&!checker[0])
        {
        orderstatuschild.setValue(currentOrderStatus);

                //deliveredDate.setValue(deliveredDate);
                deliveredTime.child("DELIVEREDDATE").setValue(deliveredDatefinal);
                deliveredTime.child("DELIVEREDTIME").setValue(deliveredTimefinal);
                alertDialog.dismiss();

progressDialog.setMessage(progressMessage);
progressDialog.show();
        ordercaretakeradminname.setValue(currentadminname).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                                checker[0]=true;
                      progressDialog.dismiss();
                                Toast.makeText(context,toastMessage,Toast.LENGTH_LONG).show();

                        }
                }
        });
        }

        }
        }
@Override
public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
        }
        public static ArrayList<Map<String,Double>> getCustomerCoordinateDataList(ArrayList<Map<String, Double>> customerCoordinatesDetails){
        return customerCoordinatesDetails;
        }
        public static ArrayList<Map<String,Double>> getCustomerCoordinateList(){
                return coordinatesList;
        }

private void initializeOrdersConfirmDialog(final MyHolder holder, final String marketnamerootnode, final String ordernodestring, final String customername){
        final String currentOrderStatus="ORDERACCEPTEDBYADMIN";
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.admin_orders_summary_list,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        deliveryconfirmationButton=view.findViewById(R.id.delivery_confirmation_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        ordersDialogAdminViewAdapter=new OrdersDialogAdminViewAdapter(context,customerOrders(), orderamountfinal);
        recyclerView.setAdapter(ordersDialogAdminViewAdapter);
        ordersDialogAdminViewAdapter.notifyDataSetChanged();
        builder.setView(view);
final AlertDialog alertDialog=builder.create();
        alertDialog.show();
final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
@Override
public void run() {
        handler.post(new Runnable() {
@Override
public void run() {
        grandtotaltextview.setText(""+OrdersDialogAdminViewAdapter.getGrandTotal());

        }
        });

        }
        };
        new Thread(runnable).start();
        deliveryconfirmationButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        String ToastMessage="SuccessFully You Accepted This Order for"+" "+marketname;
        String progressMessage="Please Wait We are Confirming Your Acceptance Order";
        setOrderStatusToFirebase(marketnamerootnode,ordernodestring,customername,currentOrderStatus, alertDialog,ToastMessage,"not delivered","not delivered",progressMessage);
               }
        });


        }
        private void initializeOrderInfoDialog(final MyHolder holder, final String ordernodestring, String s, final String marketnamerootnode, final String marketname, final String customername, ArrayList<CustomerOrdersPojo> customerOrders, final Float orderamountfinal, final ArrayList<Map<String, Double>> customerCoordinatesDetails, final ArrayList<CustomerInformation> customerDeliveryDetails, final String orderedDate, final String orderedTime, final String orderId){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view=inflater.inflate(R.layout.adminrequestpopupcard,null);
                shopNameText=view.findViewById(R.id.set_shopname);
                customerNameText=view.findViewById(R.id.set_customername);
                orderlistImage=view.findViewById(R.id.order_list_image);
                locationShareImage=view.findViewById(R.id.location_share_image);
                builder.setView(view);
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();
                shopNameText.setText(marketname);
                customerNameText.setText(customername);
                orderlistImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                initializePermanentOrdersConfirmDialog(orderamountfinal,holder,marketnamerootnode, ordernodestring,marketname,customername,alertDialog,customerDeliveryDetails,orderedDate,orderedTime,orderId);
                        }
                });
                locationShareImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initializeShareLocationFragment(customerCoordinatesDetails);
                            alertDialog.dismiss();
                        }
                });


        }
        public void initializeShareLocationFragment(ArrayList<Map<String, Double>> customerCoordinatesDetails)
        {
        shareLocationFragment=new ShareLocationFragment();
        FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_owner_order_request_fragment_frame,shareLocationFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        }
private void initializePermanentOrdersConfirmDialog(final Float orderAmount, final MyHolder holder, final String marketnamerootnode, final String ordernodestring, final String marketname, final String customername, final AlertDialog alertDialog1, final ArrayList<CustomerInformation> customerDeliveryDetails, final String orderedDate, final String orderedTime, final String orderId){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.orders_summary_list_fragment,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        orderDeliveredConfirmButton=view.findViewById(R.id.confirm_delivered_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        ordersDialogAdminViewAdapter=new OrdersDialogAdminViewAdapter(context,customerOrders(),orderAmount);
        recyclerView.setAdapter(ordersDialogAdminViewAdapter);
        builder.setView(view);
final AlertDialog alertDialog=builder.create();
        alertDialog.show();
final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
@Override
public void run() {
        handler.post(new Runnable() {
@Override
public void run() {
        grandtotaltextview.setText(""+OrdersDialogAdminViewAdapter.getGrandTotal());
        }
        });

        }
        };
        new Thread(runnable).start();
        orderDeliveredConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        initializeOrderDeliveredConfirmationDialog(orderAmount,holder, marketnamerootnode, ordernodestring,marketname, customername,alertDialog,customerOrders(),customerDeliveryDetails,orderedDate,orderedTime,orderId,firebaseUser);
                        alertDialog1.dismiss();
                }
        });


        }

        public void initializeOrderDeliveredConfirmationDialog(final Float orderAmount, final MyHolder holder, final String marketnamerootnode, final String ordernodestring, final String marketname, final String customername, final AlertDialog alertDialog, final ArrayList<CustomerOrdersPojo> customerOrdersPojos, final ArrayList<CustomerInformation> customerDeliveryDetails, final String orderedDate, final String orderedTime, final String orderId, final FirebaseUser firebaseUser){
                final String Yes="YES";
                final String No="NO";
                AlertDialog.Builder builder= new AlertDialog.Builder(context,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setMessage("Order Delivered Confirmation")
                        .setTitle("Did You delivered this Order of a customer?")
                        .setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                        String currentOrderStatus="DELIVERED";
                                        String ToastMessage="Successfully You Delivered This order of Shop so this order Details moving to Delivered Orders History List of"+" "+marketname;
                                        String progressMessage="Please Wait We are confirming Your Delivered Order of Customer...";
                                        getProductId();
                                        setOrderStatusToFirebase(marketnamerootnode, ordernodestring, customername,currentOrderStatus,alertDialog, ToastMessage,getProductaddingdate(),getProductaddingtime(), progressMessage);
                                        storeOrdersDeliveredByAdminToFireBase(marketname,customername,customerOrdersPojos,orderAmount,customerDeliveryDetails,orderedDate,orderedTime,orderId,getProductaddingdate(),getProductaddingtime(),currentOrderStatus);

                                }
                        });
                builder.setNegativeButton(No, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int position) {
                                        //dialog.dismiss();
                                }
                        }
                );
                AlertDialog dialog = builder.create();
                dialog.show();
        }



        public void storeOrdersDeliveredByAdminToFireBase(String marketname, String customername, ArrayList<CustomerOrdersPojo> customerOrdersPojos, final Float orderAmount, final ArrayList<CustomerInformation> customerDeliveryDetails, final String orderedDate, final String orderedTime, final String productId, final String deliveredDate, final String deliveredTime, String currentOrderStatus){
                firebaseDatabase=FirebaseDatabase.getInstance();
                orderDeliveredAdminDatabaseReference=firebaseDatabase.getReference("ADMINS");
                 final DatabaseReference pushNodeDatabaseReference= orderDeliveredAdminDatabaseReference.child(firebaseUser.getUid()).child("ADMINDELIVEREDORDERS").child(marketname)
                        .child(customername).push();
                pushNodeDatabaseReference.child("DELIVERYADDRESS").setValue(customerDeliveryDetails);
                pushNodeDatabaseReference.child("ORDEREDAMOUNT").setValue(orderAmount);
                pushNodeDatabaseReference.child("DELIVEREDDATE").setValue(deliveredDate);
                pushNodeDatabaseReference.child("DELIVEREDTIME").setValue(deliveredTime);
                pushNodeDatabaseReference.child("ORDEREDDATE").setValue(orderedDate);
                pushNodeDatabaseReference.child("ORDEREDTIME").setValue(orderedTime);
                pushNodeDatabaseReference.child("PRODUCTID").setValue(productId);
                pushNodeDatabaseReference.child("ORDERSTATUS").setValue(currentOrderStatus);
              pushNodeDatabaseReference.child("PRODUCTNAME").setValue(customerOrdersPojos);


        }

private static Float getOrderAmount(Float orderamount){
        return orderamount;
        }
private static ArrayList<CustomerOrdersPojo> getCustomerOrders(final ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList){
        return customerOrdersPojoArrayList;
        }
private static ArrayList<CustomerOrdersPojo> customerOrders(){
        return  customerOrders;
        }
public static Float orderamount(){
        return orderamountfinal;
        }

class MyHolder extends RecyclerView.ViewHolder{
    TextView ordersummarycustomername,orderAcceptedText,customerPhone,orderedDate,orderedTime,orderId;
    EditText setprice;
    LinearLayout linearLayout;
    Button orderaccepbutton,showorderdetails;
    public MyHolder(@NonNull View itemView) {
        super(itemView);
        ordersummarycustomername=(TextView)itemView.findViewById(R.id.admin_request_customerName);
        orderAcceptedText=itemView.findViewById(R.id.order_accepted_Text);
        orderedDate=itemView.findViewById(R.id.ordered_date);
        orderedTime=itemView.findViewById(R.id.ordered_time);
        orderId=itemView.findViewById(R.id.order_id);
        customerPhone=itemView.findViewById(R.id.phonenumber);
        linearLayout=(LinearLayout)itemView.findViewById(R.id.order_acceptance_linear_layout);
        orderaccepbutton=(Button)itemView.findViewById(R.id.admin_request_order_accept_button);
        showorderdetails=itemView.findViewById(R.id.admin_request_show_order_details);
    }
}
}