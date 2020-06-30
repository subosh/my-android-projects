package com.example.subosh.restauranttrack.ownercontent;

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
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.example.subosh.restauranttrack.admincontent.AdminViewOrderRequestSummaryAdapter;
import com.example.subosh.restauranttrack.admincontent.OrdersDialogAdminViewAdapter;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;

public class OwnerOrderHistoryAdapter extends RecyclerView.Adapter<OwnerOrderHistoryAdapter.MyHolder> {
    Context context;
    ArrayList<OrdersSummaryPojo> ownerOrderHistoryArrayList;
    OrdersDialogAdminViewAdapter ordersDialogAdminViewAdapter;
    RecyclerView recyclerView;
    TextView grandtotaltextview;
    Button orderDeliveredButton;
    FirebaseAuth firebaseAuth;
    static ArrayList<CustomerOrdersPojo> customerOrders;
    static  Float orderamountfinal;
    FirebaseUser firebaseUser;
    static float grandtotal, grandsum;

    public OwnerOrderHistoryAdapter(Context context, ArrayList<OrdersSummaryPojo> customerOrderDetails) {
        this.context = context;
        this.ownerOrderHistoryArrayList = customerOrderDetails;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.delivered_orders_history_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        holder.customernameTextview.setText(ownerOrderHistoryArrayList.get(position).getCustomername());
        final String customerName=ownerOrderHistoryArrayList.get(position).getCustomername();
        final  String customerDeliveryAddress=ownerOrderHistoryArrayList.get(position).getCustomerDeliveryAddressDetails().get(0).getCustomeraddress();
        final String customerPhone=ownerOrderHistoryArrayList.get(position).getCustomerDeliveryAddressDetails().get(0).getCustomerphone();
        final ArrayList<CustomerOrdersPojo> productname=ownerOrderHistoryArrayList.get(position).getCustomerOrdersPojos();
        final String admincaretaker=ownerOrderHistoryArrayList.get(position).getAdmincaretaker();
        final String ownerName=ownerOrderHistoryArrayList.get(position).getOwnerInformationArrayList().get(0).getOwnername();
        final String deliveredDate=ownerOrderHistoryArrayList.get(position).getOrderedDate();
        final String deliveredTime=ownerOrderHistoryArrayList.get(position).getOrderedTime();
        final String orderId=ownerOrderHistoryArrayList.get(position).getOrderId();

        final float orderAmount=ownerOrderHistoryArrayList.get(position).getOrderAmount();
        holder.customerAddress.setText(customerDeliveryAddress);
        holder.customerPhone.setText(customerPhone);
        holder.deliveredDate.setText(deliveredDate);
        holder.deliveredTime.setText(deliveredTime);
        holder.orderId.setText(orderId);
        holder.deliveredOrderDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerOrders=getCustomerOrders(productname);
                orderamountfinal = getOrderAmount(orderAmount);
initializePermanentOrdersConfirmDialog(orderamountfinal,customerName);
            }
        });
        }
    private void initializePermanentOrdersConfirmDialog(final Float orderAmount, final String customername){
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
        return ownerOrderHistoryArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView customernameTextview,customerAddress,customerPhone,deliveredDate,deliveredTime,orderId;
        Button deliveredOrderDetailsButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            customernameTextview = itemView.findViewById(R.id.customerName);
            deliveredDate = itemView.findViewById(R.id.ordered_date);
            deliveredTime = itemView.findViewById(R.id.ordered_time);
            orderId = itemView.findViewById(R.id.order_id);
            customerAddress = itemView.findViewById(R.id.address);
            customerPhone = itemView.findViewById(R.id.phonenumber);
            deliveredOrderDetailsButton=itemView.findViewById(R.id.delivered_order_details_button);



        }
    }
}
