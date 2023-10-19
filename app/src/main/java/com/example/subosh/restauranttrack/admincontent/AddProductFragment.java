package com.example.subosh.restauranttrack.admincontent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.subosh.restauranttrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends DialogFragment implements View.OnClickListener {
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference1;
    private StorageReference firebaseStorage;
    private ArrayList<String> productlist;
    ProductlistSinglenton productlistSinglenton;
    //String productlistarray[];
    static String[] productlistarray;
    public static String productName;
    public static Float productPrice;
    public static boolean[] checkeditems;
    EditText product, price;
    Uri resultUri;
    Toolbar toolbar;
    static ArrayList<String> dummylist = new ArrayList<>();
    Button save;
    static int len;
    ArrayList<Integer> itemarraylist = new ArrayList<>();
    ArrayList<ProductlistSinglenton> dummyproductlist = new ArrayList<>();
    CircleImageView circleImageViewproductImage;
//private  Async task;
ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.addproductfragment, container, false);
        save = (Button) view.findViewById(R.id.save_product);
        product = view.findViewById(R.id.productname);
        price = (EditText) view.findViewById(R.id.price);
        circleImageViewproductImage=view.findViewById(R.id.circleImageView_product_image);
        progressDialog = new ProgressDialog(getContext());
        save.setOnClickListener(this);
        circleImageViewproductImage.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("WHOLE_PRODUCTS");
        firebaseStorage = FirebaseStorage.getInstance().getReference("product_images");
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }
    public void putproductlist(String productName, Float productprice) {

        progressDialog.setMessage("Please wait for Creating Product");
        progressDialog.show();
        productlistSinglenton = new ProductlistSinglenton(productName, productprice,"na");
        databaseReference1 = databaseReference.push();
        databaseReference1.setValue(productlistSinglenton).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    final StorageReference str=firebaseStorage.child(resultUri.getLastPathSegment()+".jpg");
                    str.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference1.child("productImageDownloadPath").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            AddProductFragment.super.getDialog().dismiss();
                                            Toast.makeText(getActivity(), "hello admin your desired products are added to database", Toast.LENGTH_SHORT).show();
                                        }
                                    }) ;
                                }
                            }) ;
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void initializeproductcatalogview() {

        productName = product.getText().toString();
        String productPrice=price.getText().toString();
        if (TextUtils.isEmpty(productName)){
Toast.makeText(getContext(),"Please Enter ProductName",Toast.LENGTH_LONG).show();
return;
        }
        if (TextUtils.isEmpty(productPrice))
        {
            Toast.makeText(getContext(),"Please Enter Price",Toast.LENGTH_LONG).show();
            return;
        }
        Float productprice=Float.parseFloat(productPrice);
setCircleImageViewproductImage(productprice);

    }
public void setCircleImageViewproductImage(Float productprice){
    if (resultUri == null) {
       Toast.makeText(getContext(),"Please Select Image to upload Image To product",Toast.LENGTH_LONG).show();
       return;

        }

        putproductlist(productName,productprice);
//    this.getDialog().dismiss();

}
    @Override
    public void onClick(View v) {
        if (v == save) {
            initializeproductcatalogview();

            //startTask();
        }
        if (v==circleImageViewproductImage){
            selectImage();
        }
    }
    public void selectImage() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,12);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            resultUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(),this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                // resultUri1=data.getData();
                circleImageViewproductImage.setImageURI(resultUri);
Toast.makeText(getContext(),"You Uploaded image Sucessfully",Toast.LENGTH_LONG).show();
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }


    }
//    class Async extends AsyncTask<Void, Void, Void> {
//        @Override
//        public Void doInBackground(Void... voids) {
//            return putproductlist(productName, productprice);
//        }
//
//        @Override
//        protected void onPostExecute(Void s) {
//
//            super.onPostExecute(s);
//            }
//    }
//    public void startTask() {
//        task = new Async();
//        task.execute();
//
//
//    }

}