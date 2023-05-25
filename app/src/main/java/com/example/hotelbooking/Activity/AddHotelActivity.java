package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AddHotelActivity extends AppCompatActivity {

    private ImageView img_back, img_add_image;
    private EditText edt_name, edt_address, edt_gmail, edt_phone, edt_price, edt_description, edt_priceKM;
    private TextView tv_add_image;
    private AppCompatButton add_house;
    private Progress_Dialog dialog;
    public static final int REQUEST_CODE_ADD_GALLERY = 1;
    private Uri imageUri;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        anhxa();
        click_event();
    }

    private void anhxa(){
        img_back = findViewById(R.id.img_back);
        img_add_image = findViewById(R.id.img_add_image);
        edt_name = findViewById(R.id.edt_name);
        edt_address = findViewById(R.id.edt_address);
        edt_gmail = findViewById(R.id.edt_gmail);
        edt_phone = findViewById(R.id.edt_phone);
        edt_price = findViewById(R.id.edt_price);
        edt_description = findViewById(R.id.edt_description);
        tv_add_image = findViewById(R.id.tv_add_image);
        add_house = findViewById(R.id.add_house);
        edt_priceKM = findViewById(R.id.edt_priceKM);
        dialog = new Progress_Dialog(this);

        CustomTextWatcher customTextWatcher = new CustomTextWatcher(edt_price);
        edt_price.addTextChangedListener(customTextWatcher);

        CustomTextWatcher customTextWatcher1 = new CustomTextWatcher(edt_priceKM);
        edt_priceKM.addTextChangedListener(customTextWatcher1);
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddHotelActivity.this, HotelManagerActivity.class));
            }
        });
        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });
        add_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_house();
            }
        });
    }
    private void uploadImg(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_ADD_GALLERY);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_GALLERY && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(img_add_image);
            tv_add_image.setVisibility(View.GONE);

        }
    }
    private void save_house(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Lay id nguoi dung hien tai
        String idUser = user.getUid();

        String name = edt_name.getText().toString();
        String address = edt_address.getText().toString();
        String price = edt_price.getText().toString();
        String gmail = edt_gmail.getText().toString();
        String phone = edt_phone.getText().toString();
        String description = edt_description.getText().toString();
        String priceKM = edt_priceKM.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Tên không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()){
            Toast.makeText(getApplicationContext(), "Địa chỉ không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else if (price.isEmpty()){
            Toast.makeText(getApplicationContext(), "Giá không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else if (gmail.isEmpty()){
            Toast.makeText(getApplicationContext(), "Gmail không được để trống", Toast.LENGTH_SHORT).show();
        } else if (!gmail.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()){
            Toast.makeText(getApplicationContext(), "Mô tả không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null){
            Toast.makeText(getApplicationContext(), "Hình ảnh không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang đăng bài viết...");
            final StorageReference reference = FirebaseStorage.getInstance().getReference().child("images/")
                    .child(System.currentTimeMillis()+"");
            if (priceKM.isEmpty()){
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Tao node House Booking
                                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("HouseBooking");
                                // Tạo khóa tự động và lấy giá trị của tung house con
                                String idH = Ref.push().getKey();
                                Hotel hotel = new Hotel();
                                hotel.setId_hotel(idH);
                                hotel.setId_user(idUser);
                                hotel.setName_hotel(name);
                                hotel.setAddress_hotel(address);
                                hotel.setGmail_hotel(gmail);
                                hotel.setPhone_hotel(phone);
                                hotel.setPrice_hotel(price);
                                hotel.setDescription_hotel(description);
                                hotel.setImage_hotel(uri.toString());
                                Ref.child(idH).setValue(hotel)// Tao house con va idH trung voi id house con
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.HideDialog();
                                                startActivity(new Intent(AddHotelActivity.this, HotelManagerActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.HideDialog();
                                                Toast.makeText(AddHotelActivity.this, "Add House Fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

                    }
                });

            } else {
                double goc = Double.parseDouble(price);
                double km = Double.parseDouble(priceKM);

                if (km > goc){
                    Toast.makeText(this, "Giá khuyễn mãi không được lớn hơn giá gốc.", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();
                    return;
                }
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Tao node House Booking
                                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("HouseBooking");
                                // Tạo khóa tự động và lấy giá trị của tung house con
                                String idH = Ref.push().getKey();
                                Hotel hotel = new Hotel();
                                hotel.setId_hotel(idH);
                                hotel.setId_user(idUser);
                                hotel.setPriceKM_hotel(priceKM);
                                hotel.setName_hotel(name);
                                hotel.setAddress_hotel(address);
                                hotel.setGmail_hotel(gmail);
                                hotel.setPhone_hotel(phone);
                                hotel.setPrice_hotel(price);
                                hotel.setDescription_hotel(description);
                                hotel.setImage_hotel(uri.toString());
                                Ref.child(idH).setValue(hotel)// Tao house con va idH trung voi id house con
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.HideDialog();
                                                startActivity(new Intent(AddHotelActivity.this, HotelManagerActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.HideDialog();
                                                Toast.makeText(AddHotelActivity.this, "Add House Fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

                    }
                });

            }

        }



    }
}