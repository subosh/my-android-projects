package com.example.subosh.restauranttrack.admincontent;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.example.subosh.restauranttrack.ownercontent.OrderSummaryListFragment;
import com.example.subosh.restauranttrack.ownercontent.OwnerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminViewOrderSummaryAdapter extends RecyclerView.Adapter<AdminViewOrderSummaryAdapter.MyHolder> {
    Context context;
    static ArrayList<AdminViewOrdersSummaryPojo> customerInformationArrayList;
    static ArrayList<CustomerOrdersPojo> customerOrders;
    static  Float orderamountfinal;
    String  price;
    RecyclerView recyclerView;
    Button deliveryconfirmationButton,orderDeliveredBuuton;
    TextView grandtotaltextview;
    // ArrayList<CustomerOrdersPojo> productname;
    String setpricestring;
    DatabaseReference orderstatusdatabaserefernce;
    String marketname;
    FirebaseDatabase firebaseDatabase;
    OrderSummaryListFragment orderSummaryListFragment;
    ArrayList<OwnerInformation> ownerInformationArrayList;


String marketnamerootnode;
    ArrayList<CustomerOrdersPojo> productdetaillist;
    ArrayList<String> customernamelist=new ArrayList<>();
    ArrayList<String> checkingorderstatusbooleanlist=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
    FirebaseUser firebaseUser;
//    String orderstatus;
    String currentadminname;
    ArrayList<CustomerInformation> customerDetailsList=new ArrayList<>();

    public AdminViewOrderSummaryAdapter(Context c, ArrayList<AdminViewOrdersSummaryPojo> p, String adminname) {
        context=c;
        customerInformationArrayList=p;
        currentadminname=adminname;
        this.customerDetailsList=customerDetailsList;
    }
    @NonNull @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.adminviewordersummarycard,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int  position) {

        final String customername = customerInformationArrayList.get(position).getCustomername();
//        final String customerAddress=customerDetailsList.get(position).getCustomeraddress();
        final String ordernodestring = customerInformationArrayList.get(position).getOrdernodes();
        final String customerAddress=customerInformationArrayList.get(position).getCustomerDeliveryDetailsList().get(0).getCustomeraddress();
         String orderstatus = customerInformationArrayList.get(position).getOrderstatus();
        final ArrayList<CustomerOrdersPojo> productname = customerInformationArrayList.get(position).getCustomerOrdersPojos();
        final Float orderamount = customerInformationArrayList.get(position).getOrderamount();
        final String customerPhone=customerInformationArrayList.get(position).getCustomerDeliveryDetailsList().get(0).getCustomerphone();
        final String ownerdeliveryRequeststatus = customerInformationArrayList.get(position).getDeliveryRequestStatus();
        final String orderedDate=customerInformationArrayList.get(position).getOrderedDate();
        final String orderedTime=customerInformationArrayList.get(position).getOrderedTime();
        final String orderId=customerInformationArrayList.get(position).getOrderId();
        holder.ordersummarycustomername.setText(customerAddress);
        holder.customerName.setText(customername);
        holder.customerPhone.setText(customerPhone);
       holder.orderedDate.setText(orderedDate);
        holder.orderedTime.setText(orderedTime);
        holder.orderId.setText(orderId);
        marketnamerootnode = AdminViewOwnerOrderFragment.getMarketnamenode();
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderamount);
        customerOrders = getCustomerOrders(productname);
        orderamountfinal = getOrderAmount(orderamount);
        holder.linearLayout.setVisibility(View.GONE);
        holder.orderAcceptText.setVisibility(View.GONE);
        if (ownerdeliveryRequeststatus.equals("YES") && orderstatus.equals("ORDERED")) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.orderaccepbutton.setVisibility(View.VISIBLE);
            holder.showorderdetails.setVisibility(View.GONE);
            }
         if ((ownerdeliveryRequeststatus.equals("YES") && orderstatus.equals("ORDERACCEPTEDBYADMIN")) || (ownerdeliveryRequeststatus.equals("NO") && orderstatus.equals("ORDERACCEPTEDBYADMIN"))) {
            holder.orderaccepbutton.setVisibility(View.GONE);
            holder.showorderdetails.setVisibility(View.VISIBLE);
            boolean check=true;
          CustomAdminOrderAccepViewManager   customAdminOrderAccepViewManager = new CustomAdminOrderAccepViewManager(context.getApplicationContext(),check);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.linearLayout.addView(customAdminOrderAccepViewManager);
            holder.orderAcceptText.setVisibility(View.VISIBLE);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ordernodestring, Toast.LENGTH_SHORT).show();
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
                initializePermanentOrdersConfirmDialog(holder, marketnamerootnode, ordernodestring, customername,ownerdeliveryRequeststatus);
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

    private void setOrderStatusToFirebase(final String marketnamerootnode, final String ordernodestring, final String customernamefinal){
        firebaseDatabase=FirebaseDatabase.getInstance();
        orderstatusdatabaserefernce=firebaseDatabase.getReference("OWNERS");
        final ProgressDialog progressDialog=new ProgressDialog(this.context);
        progressDialog.setMessage("Please Wait We are making Your Confirmation of Order Acceptance...");
        orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String customername = dataSnapshot1.getKey();
                    DatabaseReference databaseReference=orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERORDERS").child(customername).child(ordernodestring);
                    DatabaseReference orderstatuschild = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring).child("ORDERSTATUS");
                    DatabaseReference ordercaretakeradminname = orderstatusdatabaserefernce.child(marketnamerootnode).child("CUSTOMERSORDERS").child(customername).child(ordernodestring).child("ORDERCARETAKER");

                    if (customername.equals(customernamefinal))
                    {
                    orderstatuschild.setValue("ORDERACCEPTEDBYADMIN");
                    ordercaretakeradminname.setValue(currentadminname);
                    progressDialog.dismiss();
                    Toast.makeText(context,"SuccessFully You We confirmed Your Acceptance Now You Can Deliver Product",Toast.LENGTH_LONG).show();

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initializeOrdersConfirmDialog(final MyHolder holder, final String marketnamerootnode, final String ordernodestring, final String customername){
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
                setOrderStatusToFirebase(marketnamerootnode,ordernodestring,customername);
             alertDialog.dismiss();
            }
        });


    }
    private void initializePermanentOrdersConfirmDialog(final MyHolder holder, final String marketnamerootnode, final String ordernodestring, final String customername, String ownerdeliveryRequeststatus){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.orders_summary_list_fragment,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        orderDeliveredBuuton=view.findViewById(R.id.confirm_delivered_button);
        orderDeliveredBuuton.setVisibility(View.GONE);
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

                    }
                });

            }
        };
        new Thread(runnable).start();
        grandtotaltextview.setText(""+OrdersDialogAdminViewAdapter.getGrandTotal());


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
        TextView ordersummarycustomername,orderAcceptText,customerName,customerPhone,orderedDate,orderedTime,orderId;
        EditText setprice;
        LinearLayout linearLayout;
        Button orderaccepbutton,showorderdetails;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ordersummarycustomername=(TextView)itemView.findViewById(R.id.admin_view_ordersummary_customername);
            customerName=itemView.findViewById(R.id.customerName);
            orderedDate=itemView.findViewById(R.id.ordered_date);
            orderedTime=itemView.findViewById(R.id.ordered_time);
            orderId=itemView.findViewById(R.id.order_id);
            customerPhone=itemView.findViewById(R.id.customerPhone);
            orderAcceptText=itemView.findViewById(R.id.order_accepted_Text);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.order_acceptance_linear_layout);
            orderaccepbutton=(Button)itemView.findViewById(R.id.order_accept_button);
            showorderdetails=itemView.findViewById(R.id.show_order_details);
        }
    }
}

