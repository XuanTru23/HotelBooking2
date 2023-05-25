package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Models.Hotel;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.UUID;

public class DetailHotelActivity extends AppCompatActivity {

    private ImageView img_back, img_add_image;
    private EditText edt_name, edt_address, edt_gmail, edt_phone, edt_price, edt_description,edt_priceKM;
    private AppCompatButton btn_sua, btn_xoa;
    private Progress_Dialog dialog;
    public static final int REQUEST_CODE_ADD_GALLERY = 1;
    private Uri imageUri;
    private String Url_anh, idHotel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);
        idHotel = getIntent().getStringExtra("idHotel");
        anhxa();
        get_hotel();
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
        btn_sua = findViewById(R.id.btn_sua);
        btn_xoa = findViewById(R.id.btn_xoa);
        edt_priceKM = findViewById(R.id.edt_priceKM);
        dialog = new Progress_Dialog(this);

        CustomTextWatcher customTextWatcher = new CustomTextWatcher(edt_price);
        edt_price.addTextChangedListener(customTextWatcher);

        CustomTextWatcher customTextWatcher1 = new CustomTextWatcher(edt_priceKM);
        edt_priceKM.addTextChangedListener(customTextWatcher1);
    }
    private void click_event(){
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sua_hotel();
            }
        });

        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoa_hotel();
            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                finish();
            }
        });
    }
    private void get_hotel(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HouseBooking");
        Query query = databaseReference.orderByChild("id_hotel").equalTo(idHotel);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);

                    edt_name.setText(hotel.getName_hotel());
                    edt_address.setText(hotel.getAddress_hotel());
                    edt_gmail.setText(hotel.getGmail_hotel());
                    edt_phone.setText(hotel.getPhone_hotel());
                    edt_priceKM.setText(hotel.getPriceKM_hotel());
                    edt_price.setText(hotel.getPrice_hotel());
                    edt_description.setText(hotel.getDescription_hotel());
                    Url_anh = hotel.getImage_hotel();
                    Glide.with(DetailHotelActivity.this).load(Url_anh).into(img_add_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void xoa_hotel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailHotelActivity.this);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có xóa bài viết này không!");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("HouseBooking").child(idHotel);
                dialog.ShowDilag("Đang xóa...");
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        xoa_anh_cu();
                        dialog.HideDialog();
                        startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.HideDialog();;
                    }
                });
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }
    private void sua_hotel(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HouseBooking").child(idHotel);

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
        } else if (phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()){
            Toast.makeText(getApplicationContext(), "Mô tả không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang cập nhật...");
            if (priceKM.isEmpty()){
                if (imageUri == null){
                    reference.child("name_hotel").setValue(name);
                    reference.child("address_hotel").setValue(address);
                    reference.child("gmail_hotel").setValue(gmail);
                    reference.child("phone_hotel").setValue(phone);
                    reference.child("priceKM_hotel").removeValue();
                    reference.child("price_hotel").setValue(price);
                    reference.child("description_hotel").setValue(description).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.HideDialog();
                            startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                            finish();
                        }
                    });
                } else {
                    final StorageReference reference1 = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
                    reference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.child("name_hotel").setValue(name);
                                    reference.child("address_hotel").setValue(address);
                                    reference.child("gmail_hotel").setValue(gmail);
                                    reference.child("phone_hotel").setValue(phone);
                                    reference.child("price_hotel").setValue(price);
                                    reference.child("priceKM_hotel").setValue(priceKM);
                                    reference.child("description_hotel").setValue(description);
                                    reference.child("image_hotel").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            xoa_anh_cu();
                                            dialog.HideDialog();
                                            startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            } else {
                double goc = Double.parseDouble(price);
                double km = Double.parseDouble(priceKM);
                if (km > goc){
                    Toast.makeText(this, "Giá khuyễn mãi không được lớn hơn giá gốc.", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();
                    return;
                }
                if (imageUri == null){
                    reference.child("name_hotel").setValue(name);
                    reference.child("address_hotel").setValue(address);
                    reference.child("gmail_hotel").setValue(gmail);
                    reference.child("phone_hotel").setValue(phone);
                    reference.child("priceKM_hotel").setValue(priceKM);
                    reference.child("price_hotel").setValue(price);
                    reference.child("description_hotel").setValue(description).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.HideDialog();
                            startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                            finish();
                        }
                    });
                } else {
                    final StorageReference reference1 = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
                    reference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.child("name_hotel").setValue(name);
                                    reference.child("address_hotel").setValue(address);
                                    reference.child("gmail_hotel").setValue(gmail);
                                    reference.child("phone_hotel").setValue(phone);
                                    reference.child("price_hotel").setValue(price);
                                    reference.child("priceKM_hotel").setValue(priceKM);
                                    reference.child("description_hotel").setValue(description);
                                    reference.child("image_hotel").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            xoa_anh_cu();
                                            dialog.HideDialog();
                                            startActivity(new Intent(DetailHotelActivity.this, HotelManagerActivity.class));
                                            finish();
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
    private void xoa_anh_cu(){
        // xoa anh cua bai viet
        // Tìm vị trí của chuỗi "%2F" trong URL
        int firstSlashIndex = Url_anh.indexOf("%2F");
        // Tìm vị trí của chuỗi "?alt" trong URL
        int altIndex = Url_anh.indexOf("?alt");
        // Cắt chuỗi từ vị trí sau "%2F" đến trước "?alt"
        String substring = Url_anh.substring(firstSlashIndex + 3, altIndex);
        StorageReference reference1 = FirebaseStorage.getInstance().getReference("images/").child(substring);
        reference1.delete();
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
        }
    }

}