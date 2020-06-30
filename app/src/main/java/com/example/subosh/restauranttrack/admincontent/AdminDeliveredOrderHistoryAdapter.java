package com.example.subosh.restauranttrack.admincontent;

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
import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.example.subosh.restauranttrack.ownercontent.OrdersSummaryPojo;

import java.util.ArrayList;
import java.util.Map;

public class AdminDeliveredOrderHistoryAdapter extends RecyclerView.Adapter<AdminDeliveredOrderHistoryAdapter.MyHolder>{
    Context context;
    ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayList;
    static ArrayList<CustomerOrdersPojo> customerOrders;
    static  Float orderamountfinal;
    RecyclerView recyclerView;
    Button orderDeliveredConfirmButton;
    TextView grandtotaltextview;
    OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
    public AdminDeliveredOrderHistoryAdapter(Context context, ArrayList<OrdersSummaryPojo> ordersSummaryPojoArrayListcustomername) {
        this.context=context;
        this.ordersSummaryPojoArrayList=ordersSummaryPojoArrayListcustomername;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.delivered_orders_history_card, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
holder.customernameTextview.setText(ordersSummaryPojoArrayList.get(position).getCustomername());
final String customeraddress=ordersSummaryPojoArrayList.get(position).getCustomerDeliveryAddressDetails().get(0).getCustomeraddress();
        final String customerPhoneNumber=ordersSummaryPojoArrayList.get(position).getCustomerDeliveryAddressDetails().get(0).getCustomerphone();
        final  ArrayList<CustomerOrdersPojo> productname = ordersSummaryPojoArrayList.get(position).getCustomerOrdersPojos();
        final Float orderamount = ordersSummaryPojoArrayList.get(position).getOrderAmount();
        final String deliveredDate=ordersSummaryPojoArrayList.get(position).getOrderedDate();
        final String deliveredTime=ordersSummaryPojoArrayList.get(position).getOrderedTime();
        final String orderId=ordersSummaryPojoArrayList.get(position).getOrderId();
        holder.customeraddress.setText(customeraddress);
        holder.customerPhonenumber.setText(customerPhoneNumber);
        holder.deliveredDate.setText(deliveredDate);
        holder.deliveredTime.setText(deliveredTime);
        holder.orderID.setText(orderId);
        holder.deliveredOrderDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrders = getCustomerOrders(productname);
                orderamountfinal = getOrderAmount(orderamount);
                initializePermanentOrdersConfirmDialog(orderamountfinal);
}
        });

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
    private static Float getOrderAmount(Float orderamount){
        return orderamount;
    }
    private void initializePermanentOrdersConfirmDialog(final Float orderAmount){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.orders_summary_list_fragment,null);
        recyclerView=view.findViewById(R.id.order_display_recycle);
        grandtotaltextview=view.findViewById(R.id.grand_total_ownerside);
        orderDeliveredConfirmButton=view.findViewById(R.id.confirm_delivered_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        ordersDialogAdminViewAdapter=new OrdersDialogAdminViewAdapter(context,customerOrders(),orderAmount);
        orderDeliveredConfirmButton.setVisibility(View.GONE);
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
    @Override
    public int getItemCount() {
        return ordersSummaryPojoArrayList.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView customernameTextview,customeraddress,customerPhonenumber,deliveredDate,deliveredTime,orderID;
        Button deliveredOrderDetailsButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            customernameTextview = itemView.findViewById(R.id.customerName);
            deliveredDate=itemView.findViewById(R.id.ordered_date);
            deliveredTime=itemView.findViewById(R.id.ordered_time);
            orderID=itemView.findViewById(R.id.order_id);

            customeraddress = itemView.findViewById(R.id.address);
            customerPhonenumber = itemView.findViewById(R.id.phonenumber);

            deliveredOrderDetailsButton=itemView.findViewById(R.id.delivered_order_details_button);



        }


    }
}
