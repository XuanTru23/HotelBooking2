package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.Models.Booking;
import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailBookingHotelActivity extends AppCompatActivity {

    private EditText edt_name, edt_phone;
    private TextView tv_check_in, tv_check_out, tv_people, tv_rooms;
    private ImageView img_back;
    private AppCompatButton btn_update_booking, btn_delete_booking;
    private Progress_Dialog dialog;
    private int  value_ps = 0, value_eb = 0, value_ddr = 0, value_sdr = 0; // Giá trị ban đầu
    private int value_adults = 0, value_Teens = 0, value_Children = 0, value_Infants = 0; // Giá trị ban đầu
    private String roomTypes = "", peopleTypes = "";
    private String idbooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking_hotel);
        anhxa();
        idbooking = getIntent().getStringExtra("idbooking");
        click_event();
        get_hotel();

    }
    private void anhxa(){
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        tv_check_in = findViewById(R.id.tv_check_in);
        tv_check_out = findViewById(R.id.tv_check_out);
        img_back = findViewById(R.id.img_back);
        tv_people = findViewById(R.id.tv_people);
        tv_rooms = findViewById(R.id.tv_rooms);
        btn_update_booking = findViewById(R.id.btn_update_booking);
        btn_delete_booking = findViewById(R.id.btn_delete_booking);
        dialog = new Progress_Dialog(this);
    }

    private void get_hotel(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Booking");
        Query query = databaseReference.orderByChild("id_booking").equalTo(idbooking);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                    Booking booking = dataSnapshot.getValue(Booking.class);

                    edt_name.setText(booking.getName());
                    edt_phone.setText(booking.getPhone());
                    tv_check_in.setText(booking.getCheck_in());
                    tv_check_out.setText(booking.getCheck_out());
                    tv_people.setText(booking.getPeople());
                    tv_rooms.setText(booking.getRooms());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void click_event(){
        btn_delete_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoa();
            }
        });
        btn_update_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sua();
            }
        });

        tv_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_tv_check_in();
            }
        });

        tv_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_tv_check_out();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        tv_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peopleTypes = "";
                show_people();
            }
        });

        tv_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomTypes = "";
                show_room();
            }
        });

    }
    public void showDatePicker_tv_check_in() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Xử lý ngày được chọn
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tv_check_in.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
    public void showDatePicker_tv_check_out() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Xử lý ngày được chọn
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tv_check_out.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
    private void show_people(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_people);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = dialog.findViewById(R.id.tv_done);

        TextView tv_sl_adults = dialog.findViewById(R.id.tv_sl_adults);
        ImageView img_giam_adults = dialog.findViewById(R.id.img_giam_adults);
        ImageView img_tang_adults = dialog.findViewById(R.id.img_tang_adults);

        TextView tv_sl_Teens = dialog.findViewById(R.id.tv_sl_Teens);
        ImageView img_tang_Teens = dialog.findViewById(R.id.img_tang_Teens);
        ImageView img_giam_Teens = dialog.findViewById(R.id.img_giam_Teens);

        TextView tv_sl_Children = dialog.findViewById(R.id.tv_sl_Children);
        ImageView img_giam_Children = dialog.findViewById(R.id.img_giam_Children);
        ImageView img_tang_Children = dialog.findViewById(R.id.img_tang_Children);

        TextView tv_sl_Infants = dialog.findViewById(R.id.tv_sl_Infants);
        ImageView img_tang_Infants = dialog.findViewById(R.id.img_tang_Infants);
        ImageView img_giam_Infants = dialog.findViewById(R.id.img_giam_Infants);

        img_tang_adults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_adults++; // Tăng giá trị
                tv_sl_adults.setText(String.valueOf(value_adults)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_adults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_adults > 0) {
                    value_adults--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_adults.setText(String.valueOf(value_adults)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_Teens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_Teens++; // Tăng giá trị
                tv_sl_Teens.setText(String.valueOf(value_Teens)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_Teens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_Teens > 0) {
                    value_Teens--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_Teens.setText(String.valueOf(value_Teens)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_Children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_Children++; // Tăng giá trị
                tv_sl_Children.setText(String.valueOf(value_Children)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_Children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_Children > 0) {
                    value_Children--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_Children.setText(String.valueOf(value_Children)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_Infants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_Infants++; // Tăng giá trị
                tv_sl_Infants.setText(String.valueOf(value_Infants)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_Infants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_Infants > 0) {
                    value_Infants--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_Infants.setText(String.valueOf(value_Infants)); // Cập nhật giá trị trong TextView
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value_adults > 0){
                    peopleTypes += "" + value_adults + " Adults\n";
                }

                if (value_Teens > 0){
                    peopleTypes += "" + value_Teens + " Teens\n";
                }

                if (value_Children > 0){
                    peopleTypes += "" + value_Children + " Children\n";
                }

                if (value_Infants > 0){
                    peopleTypes += "" + value_Infants + " Infants\n";
                }
                // Xóa dấu phẩy và khoảng trắng cuối chuỗi nếu có
                peopleTypes = peopleTypes.replaceAll(", $", "");
                tv_people.setText(peopleTypes);

                value_adults = 0;
                value_Teens = 0;
                value_Children = 0;
                value_Infants = 0;
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void show_room(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rooms);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = dialog.findViewById(R.id.tv_done);

        TextView tv_sl_DDR = dialog.findViewById(R.id.tv_sl_DDR);
        ImageView img_giam_DDR = dialog.findViewById(R.id.img_giam_DDR);
        ImageView img_tang_DDR = dialog.findViewById(R.id.img_tang_DDR);

        TextView tv_sl_SDR = dialog.findViewById(R.id.tv_sl_SDR);
        ImageView img_giam_SDR = dialog.findViewById(R.id.img_giam_SDR);
        ImageView img_tang_SDR =  dialog.findViewById(R.id.img_tang_SDR);

        TextView tv_sl_EB =  dialog.findViewById(R.id.tv_sl_EB);
        ImageView img_giam_EB = dialog.findViewById(R.id.img_giam_EB);
        ImageView img_tang_EB = dialog.findViewById(R.id.img_tang_EB);

        TextView tv_sl_PS = dialog.findViewById(R.id.tv_sl_PS);
        ImageView img_tang_PS = dialog.findViewById(R.id.img_tang_PS);
        ImageView img_giam_PS = dialog.findViewById(R.id.img_giam_PS);



        img_tang_SDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_sdr++; // Tăng giá trị
                tv_sl_SDR.setText(String.valueOf(value_sdr)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_SDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_sdr > 0) {
                    value_sdr--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_SDR.setText(String.valueOf(value_sdr)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_DDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_ddr++; // Tăng giá trị
                tv_sl_DDR.setText(String.valueOf(value_ddr)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_DDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_ddr > 0) {
                    value_ddr--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_DDR.setText(String.valueOf(value_ddr)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_EB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_eb++; // Tăng giá trị
                tv_sl_EB.setText(String.valueOf(value_eb)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_EB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_eb > 0) {
                    value_eb--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_EB.setText(String.valueOf(value_eb)); // Cập nhật giá trị trong TextView
                }
            }
        });

        img_tang_PS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_ps++; // Tăng giá trị
                tv_sl_PS.setText(String.valueOf(value_ps)); // Cập nhật giá trị trong TextView
            }
        });

        img_giam_PS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_ps > 0) {
                    value_ps--; // Giảm giá trị nếu giá trị lớn hơn 0
                    tv_sl_PS.setText(String.valueOf(value_ps)); // Cập nhật giá trị trong TextView
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value_sdr > 0){
                    roomTypes += "" + value_sdr + " Single Deluxe Room\n";
                }

                if (value_ddr > 0){
                    roomTypes += "" + value_ddr + " Double Deluxe Room\n";
                }

                if (value_eb > 0){
                    roomTypes += "" + value_eb + " Extra Bed\n";
                }

                if (value_ps > 0){
                    roomTypes += "" + value_ps + " Premium Suite\n";
                }
                // Xóa dấu phẩy và khoảng trắng cuối chuỗi nếu có
                roomTypes = roomTypes.replaceAll(", $", "");
                tv_rooms.setText(roomTypes);
                value_sdr = 0;
                value_ddr = 0;
                value_eb = 0;
                value_ps = 0;
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void xoa(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailBookingHotelActivity.this);
        builder.setTitle("Hủy phòng!");
        builder.setMessage("Bạn có muốn hủy phòng này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Booking").child(idbooking);
                dialog.ShowDilag("Đang hủy phòng...");
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.HideDialog();
                        startActivity(new Intent(DetailBookingHotelActivity.this, BookingManagerActivity.class));
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
    private void sua(){
        String out = tv_check_out.getText().toString();
        String in = tv_check_in.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String people = tv_people.getText().toString();
        String room = tv_rooms.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Bo trong name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Bo trong phone", Toast.LENGTH_SHORT).show();
            return;
        }

        Date checkInDate;
        if (in.isEmpty() || out.isEmpty()) {
            Toast.makeText(this, "Bo trong check out, in", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Lấy ngày đi và ngày đến từ người dùng
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date checkOutDate = null; // Ngày đi
            try {
                checkOutDate = dateFormat.parse(out);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            checkInDate = null;
            try {
                checkInDate = dateFormat.parse(in);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Chuyển đổi ngày thành đối tượng Calendar
            Calendar calendarCheckOut = Calendar.getInstance();
            calendarCheckOut.setTime(checkOutDate);

            Calendar calendarCheckIn = Calendar.getInstance();
            calendarCheckIn.setTime(checkInDate);
            if (calendarCheckIn.after(calendarCheckOut)) {
                // Ngày đến lớn hơn ngày đi
                Toast.makeText(this, "Ngày đến phải lớn hơn ngày đi.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (people.isEmpty()) {
            Toast.makeText(this, "Bo trong people", Toast.LENGTH_SHORT).show();
            return;
        }
        if (room.isEmpty()) {
            Toast.makeText(this, "Bo trong room", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.ShowDilag("Đang đặt lại phòng.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Booking").child(idbooking);
        reference.child("name").setValue(name);
        reference.child("phone").setValue(phone);
        reference.child("check_in").setValue(in);
        reference.child("check_out").setValue(out);
        reference.child("people").setValue(people);
        reference.child("rooms").setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.HideDialog();
                startActivity(new Intent(DetailBookingHotelActivity.this, BookingManagerActivity.class));
                finishAffinity();
            }
        });

    }
}