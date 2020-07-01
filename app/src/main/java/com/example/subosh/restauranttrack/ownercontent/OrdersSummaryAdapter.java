package com.example.subosh.restauranttrack.ownercontent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
//import com.example.subosh.restauranttrack.admincontent.CustomOwnerOrderRequestViewManager;
import com.example.subosh.restauranttrack.admincontent.CustomAdminOrderAccepViewManager;
import com.example.subosh.restauranttrack.admincontent.OrdersDialogAdminViewAdapter;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
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
import java.util.Random;


public class OrdersSummaryAdapter extends RecyclerView.Adapter<OrdersSummaryAdapter.MyHolder> {
    Context context;
    static  ArrayList<OrdersSummaryPojo> customerInformationArrayList;
    static ArrayList<CustomerOrdersPojo> customerOrders;
    String  price;
    OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
    RecyclerView recyclerView;
    TextView grandtotaltextview;
    Button orderDeliveredButton;
   // ArrayList<CustomerOrdersPojo> productname;
    String setpricestring;
    DatabaseReference databaseReference;
    String marketname;
OrderSummaryListFragment orderSummaryListFragment;
FirebaseDatabase firebaseDatabase;
Button deliveryConfirmationButton;
DatabaseReference orderstatusdatabaserefernce;
    static  Float orderamountfinal;
    ArrayList<CustomerOrdersPojo> productdetaillist;
    ArrayList<String> customernamelist=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    OrdersDialogAdapter ordersDialogAdapter;
    FirebaseUser firebaseUser;
    String productaddingtime,productaddingdate,productId;
    String productIdGeneration;
    String code;
    String productIdArray[];
    public OrdersSummaryAdapter(Context c, ArrayList<OrdersSummaryPojo> p) {
        context=c;
        customerInformationArrayList=p;
        }
        @NonNull @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.ordersummarycard,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int  position) {
        final String customerAddress=customerInformationArrayList.get(position).getCustomerDeliveryAddressDetails().get(0).getCustomeraddress();
        final String customerNamefinal=customerInformationArrayList.get(position).getCustomername();
        final String orderNodeString=customerInformationArrayList.get(position).getOrdernodestring();
        final Float orderAmount=customerInformationArrayList.get(position).getOrderAmount();
        final String orderStatus=customerInformationArrayList.get(position).getOrderStatus();
        final String orderedDate=customerInformationArrayList.get(position).getOrderedDate();
        final String orderedTime=customerInformationArrayList.get(position).getOrderedTime();
        final String orderId=customerInformationArrayList.get(position).getOrderId();
        final ArrayList<CustomerOrdersPojo> productname=customerInformationArrayList.get(position).getCustomerOrdersPojos();
       final String admincaretaker=customerInformationArrayList.get(position).getAdmincaretaker();
       final String ownerName=customerInformationArrayList.get(position).getOwnerInformationArrayList().get(0).getOwnername();
        final String ownerdeliveryRequeststatus=customerInformationArrayList.get(position).getOwnerdeliveryRequestStatus();
        holder.ordersummarycustomername.setText(customerNamefinal);
        holder.orderAddreess.setText(customerAddress);
        holder.orderedDate.setText(orderedDate);
        holder.orderedTime.setText(orderedTime);
        holder.orderId.setText(orderId);
        holder.ownerorderAcceptLinearLayout.setVisibility(View.GONE);
        holder.owneracceptindicator.setVisibility(View.GONE);
        holder.orderaccepttext.setVisibility(View.GONE);
        if (ownerdeliveryRequeststatus.equals("YES") && orderStatus.equals("ORDERED")) {
            holder.ownerorderAcceptLinearLayout.setVisibility(View.VISIBLE);
            holder.ownerOrderAcceptButton.setVisibility(View.GONE);

        }
        if ((ownerdeliveryRequeststatus.equals("YES") && orderStatus.equals("ORDERACCEPTEDBYADMIN")) || (ownerdeliveryRequeststatus.equals("NO") && orderStatus.equals("ORDERACCEPTEDBYADMIN"))) {
            holder.ownerOrderAcceptButton.setVisibility(View.GONE);
            boolean check=true;
            CustomAdminOrderAccepViewManager   customAdminOrderAccepViewManager = new CustomAdminOrderAccepViewManager(context.getApplicationContext(),check);
            holder.ownerorderAcceptLinearLayout.setVisibility(View.VISIBLE);
            holder.owneracceptindicator.setVisibility(View.VISIBLE);
            holder.orderaccepttext.setVisibility(View.VISIBLE);
            holder.ownerorderAcceptLinearLayout.addView(customAdminOrderAccepViewManager);
        }
    if (ownerdeliveryRequeststatus.equals("NO")&&orderStatus.equals("ORDERED"))
    {
    holder.ownerorderAcceptLinearLayout.setVisibility(View.VISIBLE);
    holder.ownerOrderAcceptButton.setVisibility(View.VISIBLE);
    }

    firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        customerOrders=getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderAmount);

        holder.owneracceptindicator.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderAmount);
        initializePermanentOrdersConfirmDialog(holder,orderamountfinal,ownerdeliveryRequeststatus,ownerName,customerNamefinal,orderStatus,orderNodeString);

    }
});
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 //initializeOrdersConfirmDialog();
                Toast.makeText(context,orderNodeString, Toast.LENGTH_LONG).show();

            }
        });
        holder.ownerOrderAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrders = getCustomerOrders(productname);
                orderamountfinal = getOrderAmount(orderAmount);
                initializeOrdersConfirmDialog(holder,ownerName,customerNamefinal,orderStatus,orderNodeString,orderamountfinal);

            }
        });
        holder.ordercaretaker.setText(admincaretaker);

    }
    private static Float getOrderAmount(Float orderamount){
        return orderamount;
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
    public void updateOrderedStatusUi(final MyHolder holder, String ownerDeliveryRequestStatus){
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        };
        new Thread(runnable).start();
        if (ownerDeliveryRequestStatus.equals("YES")){
            holder.ownerOrderAcceptButton.setVisibility(View.VISIBLE);
            }
       else if (ownerDeliveryRequestStatus.equals("NO")){
            holder.ownerOrderAcceptButton.setVisibility(View.GONE);
            }
    }
    public void initializeOrdersConfirmDialog(final MyHolder holder,final String ownerName,final String customerNamefinal,final String orderStatus, final String orderNodeString,final float orderamountfinal){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.admin_orders_summary_list,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        deliveryConfirmationButton=view.findViewById(R.id.delivery_confirmation_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        ordersDialogAdminViewAdapter=new OrdersDialogAdminViewAdapter(context,customerOrders(), orderamountfinal);
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
        deliveryConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderStatus="ORDERACCEPTEDBYADMIN";
                String toastMessage="SuccessfUlly You Accepted Order From Customer";

                setOrderStatusToFirebase(ownerName,customerNamefinal,orderStatus,orderNodeString, alertDialog,toastMessage,"not delivered","notdelivered");
               // alertDialog.dismiss();
            }
        });


    }
    private void initializePermanentOrdersConfirmDialog(final MyHolder holder, Float orderAmount, String ownerdeliveryRequeststatus, final String ownerName, final String customerNamefinal, final String orderStatus, final String orderNodeString){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.orders_summary_list_fragment,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        orderDeliveredButton=view.findViewById(R.id.confirm_delivered_button);
        if (ownerdeliveryRequeststatus.equals("YES"))
        {
            orderDeliveredButton.setVisibility(View.GONE);
        }
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
orderDeliveredButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //setOrderStatusToFirebase(ownerName,customerNamefinal,orderStatus,orderNodeString);
initializeOrderDeliveredConfirmationDialog(holder,ownerName,customerNamefinal,orderNodeString,alertDialog);
    }
});

    }
    public void initializeOrderDeliveredConfirmationDialog(MyHolder holder, final String ownerName, final String customerNamefinal, final String orderNodeString, final AlertDialog alertDialog){
        final String Yes="YES";
        final String No="NO";
        AlertDialog.Builder builder= new AlertDialog.Builder(context,R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        builder.setMessage("Order Delivered Confirmation")
                .setTitle("Did You delivered this Order to a customer?")
                .setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        String currentOrderStatus="DELIVERED";
                        String toastMessage="SuccessFully You Delivered Order";
                        getProductId();
                        setOrderStatusToFirebase(ownerName, customerNamefinal,currentOrderStatus,orderNodeString,alertDialog, toastMessage,getProductaddingdate(),getProductaddingtime());


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
    @Override
    public int getItemCount() {
        return customerInformationArrayList.size();
    }


    public void setOrderStatusToFirebase(final String ownerName, final String customerNamefinal, final String orderStatus, final String orderNodeString, final AlertDialog dialog, final String toastMessage,final String deliveredDatefinal,final String deliveredTimefinal){
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        orderstatusdatabaserefernce=firebaseDatabase.getReference("OWNERS");
        final boolean[] checker = {false};
        final ProgressDialog progressDialog=new ProgressDialog(this.context);
        orderstatusdatabaserefernce.child(firebaseUser.getUid()).child("CUSTOMERSORDERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String customername = dataSnapshot1.getKey();

                        DatabaseReference orderstatuschild = orderstatusdatabaserefernce.child(firebaseUser.getUid()).child("CUSTOMERSORDERS").child(customername).child(orderNodeString).child("ORDERSTATUS");
                        DatabaseReference ordercaretakeradminname = orderstatusdatabaserefernce.child(firebaseUser.getUid()).child("CUSTOMERSORDERS").child(customername).child(orderNodeString).child("ORDERCARETAKER");
                        DatabaseReference deliveredDate=orderstatusdatabaserefernce.child(firebaseUser.getUid()).child("CUSTOMERSORDERS").child(customername).child(orderNodeString).child("DELIVEREDDATE");
                        DatabaseReference deliveredTime=orderstatusdatabaserefernce.child(firebaseUser.getUid()).child("CUSTOMERSORDERS").child(customername).child(orderNodeString).child("DELIVEREDTIME");
                        if (customername.equals(customerNamefinal)&&!checker[0])
                        {
                            orderstatuschild.setValue(orderStatus);
                            deliveredDate.setValue(deliveredDatefinal);
                            deliveredTime.setValue(deliveredTimefinal);
                            dialog.dismiss();
                            progressDialog.setMessage("Please Wait We are Confirm Your Order Acceptance From Your Customer");
                            progressDialog.show();
                            ordercaretakeradminname.setValue(ownerName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        progressDialog.dismiss();
                                        Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show();
                                        checker[0] =true;
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
public  static ArrayList<CustomerOrdersPojo> getCustomerOrders(final ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList){
        return customerOrdersPojoArrayList;
}
public static ArrayList<CustomerOrdersPojo> customerOrders(){
        return  customerOrders;
}

    class MyHolder extends RecyclerView.ViewHolder{
        TextView ordersummarycustomername,ordercaretaker,orderaccepttext,orderAddreess,orderedDate,orderedTime,orderId;
        Button ownerOrderAcceptButton;
        LinearLayout ownerorderAcceptLinearLayout,ownerorderAcceptLinearLayout1;
        ImageView owneracceptindicator;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ordersummarycustomername=(TextView)itemView.findViewById(R.id.ordersummary_customername);
            orderAddreess=(TextView)itemView.findViewById(R.id.order_address);
            orderedDate=itemView.findViewById(R.id.ordered_date);
            orderedTime=itemView.findViewById(R.id.ordered_Time);
            orderId=itemView.findViewById(R.id.order_Id);
            ordercaretaker=itemView.findViewById(R.id.order_care_takerName);
            orderaccepttext=itemView.findViewById(R.id.order_accepted_text);
            ownerOrderAcceptButton=itemView.findViewById(R.id.order_accept_button_owner);
            ownerorderAcceptLinearLayout=itemView.findViewById(R.id.order_acceptance_linear_layout_owner);
            ownerorderAcceptLinearLayout1=itemView.findViewById(R.id.order_acceptance_linear_layout1_owner);
            owneracceptindicator=itemView.findViewById(R.id.order_accept_indicator_owner);

            }
    }
}

