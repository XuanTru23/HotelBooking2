package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_email, edt_pass;
    private TextView tv_forgot_password, tv_register;
    private AppCompatButton btn_login;
    private Progress_Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        set_text();
        click_event();

    }

    private void anhxa(){
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        dialog = new Progress_Dialog(this);
    }
    private void set_text(){
        // setText cho textview_register
        String fullText1 = "Không có tài khoản? Đăng ký ở đây.";
        Spannable spannable1 = new SpannableString(fullText1);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start1 = fullText1.indexOf("Đăng ký");
        int end1 = start1 + "Đăng ký".length();
        // Đặt màu xanh cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan1 = new ForegroundColorSpan(Color.parseColor("#1D3AF2"));
        spannable1.setSpan(redColorSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan1 = new UnderlineSpan();
        spannable1.setSpan(underlineSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào TextView
        tv_register.setText(spannable1);
    }

    private void click_event(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_in();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finishAffinity();
            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finishAffinity();
            }
        });
    }

    private void log_in(){
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang đăng nhập...");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    dialog.HideDialog();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại! Kiểm tra thông tin tài khoản đăng nhập.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}