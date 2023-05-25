package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_name, edt_email, edt_pass;
    private AppCompatRadioButton radio_button;
    private TextView tv_login;
    private AppCompatButton btn_register;
    private Progress_Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhxa();
        set_text();
        click_event();

    }
    private void anhxa(){
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        radio_button = findViewById(R.id.radio_button);
        tv_login = findViewById(R.id.tv_login);
        btn_register = findViewById(R.id.btn_register);
        dialog = new Progress_Dialog(this);
    }

    private void set_text(){
        String fullText = "Tôi đồng ý với TXT và Chính sách quyền riêng tư.";
        Spannable spannable = new SpannableString(fullText);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start = fullText.indexOf("TXT và Chính sách quyền riêng tư.");
        int end = start + "TXT và Chính sách quyền riêng tư.".length();
        // Đặt màu đen cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.BLACK);
        spannable.setSpan(redColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannable.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đặt độ đậm cho phần văn bản được chọn
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào radioButton
        radio_button.setText(spannable);


        // setText cho textview_login
        String fullText1 = "Đã là thành viên? Đăng nhập tại đây..";
        Spannable spannable1 = new SpannableString(fullText1);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start1 = fullText1.indexOf("Đăng nhập");
        int end1 = start1 + "Đăng nhập".length();
        // Đặt màu xanh cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan1 = new ForegroundColorSpan(Color.parseColor("#1D3AF2"));
        spannable1.setSpan(redColorSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan1 = new UnderlineSpan();
        spannable1.setSpan(underlineSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào TextView
        tv_login.setText(spannable1);
    }

    private void click_event(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_button.isChecked()){
                    create_account();
                }else {
                    Toast.makeText(RegisterActivity.this, "Bạn cần đồng ý với TXT và Chính sách quyền riêng tư", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

    }

    private void create_account(){
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang tạo tài khoản...");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = auth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                    if (user != null){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", name);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());
                        hashMap.put("imageUrl", "default");
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.HideDialog();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại! Tài khoản Email đã đăng ký.", Toast.LENGTH_SHORT).show();
                }
            });
            
        }

    }


}