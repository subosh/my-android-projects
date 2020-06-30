package com.example.subosh.restauranttrack.customerscontent;

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
import android.widget.TextView;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.OrdersDialogAdminViewAdapter;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CustomerOrderHistoryAdapter extends RecyclerView.Adapter<CustomerOrderHistoryAdapter.MyHolder> {
    Context context;
    ArrayList<OrdersSummaryPojo> customerOrderHistoryArrayList;
    OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
    RecyclerView recyclerView;
    TextView grandtotaltextview;
    Button orderDeliveredButton;
    FirebaseAuth firebaseAuth;
    static ArrayList<CustomerOrdersPojo> customerOrders;
    static  Float orderamountfinal;
    FirebaseUser firebaseUser;
    static float grandtotal, grandsum;

    public CustomerOrderHistoryAdapter(Context context, ArrayList<OrdersSummaryPojo> customerOrderHistoryArrayList) {
        this.context = context;
        this.customerOrderHistoryArrayList = customerOrderHistoryArrayList;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.deliverd_order_history_customercard, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final String shopName=customerOrderHistoryArrayList.get(position).getCustomername();
        final ArrayList<CustomerOrdersPojo> productname=customerOrderHistoryArrayList.get(position).getCustomerOrdersPojos();
        final String ownerAddress=customerOrderHistoryArrayList.get(position).getOwnerInformationArrayList().get(position).getOwnerAddress();
        final String shopPhoneNumber=customerOrderHistoryArrayList.get(position).getOwnerInformationArrayList().get(position).getOwnerphone();
        final String admincaretaker=customerOrderHistoryArrayList.get(position).getAdmincaretaker();
        final float orderAmount=customerOrderHistoryArrayList.get(position).getOrderAmount();
        final String orderedDate=customerOrderHistoryArrayList.get(position).getOrderedDate();
        final String orderTime=customerOrderHistoryArrayList.get(position).getOrderedTime();
        final String orderId=customerOrderHistoryArrayList.get(position).getOrderId();
        holder.customernameTextview.setText(shopName);
        holder.customeraddress.setText(ownerAddress);
        holder.customerPhoneNumber.setText(shopPhoneNumber);
        holder.orderedDate.setText(orderedDate);
        holder.orderedTime.setText(orderTime);
        holder.orderId.setText(orderId);
        holder.deliveredOrderDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrders=getCustomerOrders(productname);

                orderamountfinal = getOrderAmount(orderAmount);
                initializePermanentOrdersConfirmDialog(orderamountfinal);
            }
        });
    }
    private void initializePermanentOrdersConfirmDialog(final Float orderAmount){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.orders_summary_list_fragment,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        orderDeliveredButton=view.findViewById(R.id.confirm_delivered_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        orderDeliveredButton.setVisibility(View.GONE);
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
    }
    public  static ArrayList<CustomerOrdersPojo> getCustomerOrders(final ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList){
        return customerOrdersPojoArrayList;
    }
    public static ArrayList<CustomerOrdersPojo> customerOrders(){
        return  customerOrders;
    }
    private static Float getOrderAmount(Float orderamount){
        return orderamount;
    }

    @Override

    public int getItemCount() {
        return customerOrderHistoryArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView customernameTextview,customeraddress,customerPhoneNumber,orderedDate,orderedTime,orderId;
        Button deliveredOrderDetailsButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            customernameTextview = itemView.findViewById(R.id.customerName);
            orderedDate=itemView.findViewById(R.id.ordered_date);
            orderedTime=itemView.findViewById(R.id.ordered_Time);
            orderId=itemView.findViewById(R.id.order_Id);
            customeraddress=itemView.findViewById(R.id.address);
            customerPhoneNumber=itemView.findViewById(R.id.phonenumber);
            deliveredOrderDetailsButton=itemView.findViewById(R.id.delivered_order_details_button);



        }
    }
}

