package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.Fragment.ProfileFragment;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MyAccountActivity extends AppCompatActivity {
    private ImageView img_back, img_profile;
    private TextView tv_change_img_logo, tv_change_name, tv_change_email, tv_change_pass, tv_name, tv_email, tv_tb_xac_thuc, tv_xac_thuc_email;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Progress_Dialog dialog;
    private String imageUrl, url_anh_cu;
    public static final int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        anhxa();
        click_event();
        set_profile();
        if (user.isEmailVerified()){
            tv_tb_xac_thuc.setText("Tài khoản đã xác thực.");
            tv_tb_xac_thuc.setTextColor(Color.parseColor("#026A06"));
        } else {
            tv_tb_xac_thuc.setText("Tài khoản chưa được xác thực.");
            tv_tb_xac_thuc.setTextColor(Color.parseColor("#F66767"));
            tv_xac_thuc_email.setVisibility(View.VISIBLE);
        }

        tv_xac_thuc_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_email_xac_thuc();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }
    private void anhxa(){
        img_back = findViewById(R.id.img_back);
        img_profile = findViewById(R.id.img_profile);
        tv_change_img_logo = findViewById(R.id.tv_change_img_logo);
        tv_change_name = findViewById(R.id.tv_change_name);
        tv_change_email = findViewById(R.id.tv_change_email);
        tv_change_pass = findViewById(R.id.tv_change_pass);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_tb_xac_thuc = findViewById(R.id.tv_tb_xac_thuc);
        tv_xac_thuc_email = findViewById(R.id.tv_xac_thuc_email);
        dialog = new Progress_Dialog(this);
    }

    private void set_profile(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                imageUrl = snapshot.child("imageUrl").getValue(String.class);
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_profile);
                tv_name.setText(user.getName());
                tv_email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void click_event(){

        tv_change_img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });
        tv_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewName();
            }
        });
        tv_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewEmail();
            }
        });
        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewPass();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            saveImageToFirebase(imageUri);
        }
    }
    private void saveImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
        dialog.ShowDilag("Đang thay đổi ảnh đại diện...");
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog.HideDialog();
                        // Lưu URL của ảnh vào Firebase Realtime Database
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                        databaseRef.child("imageUrl").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi upload ảnh thất bại
            }
        });
    }

    private void openDialogNewName(){
        final Dialog dialog_name = new Dialog(this);
        dialog_name.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_name.setContentView(R.layout.dialog_change_name);
        dialog_name.setCancelable(false);
        Window window = dialog_name.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        EditText edt_new = dialog_name.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_name.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_name.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_name.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.ShowDilag("Đang cập nhật lại họ và tên.");
                String newName = edt_new.getText().toString();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                reference1.child("name").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.HideDialog();
                        dialog_name.dismiss();
                    }
                });
            }
        });

        dialog_name.show();
    }
    private void openDialogNewEmail(){
        final Dialog dialog_email = new Dialog(this);
        dialog_email.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_email.setContentView(R.layout.dialog_change_email);
        dialog_email.setCancelable(false);
        Window window = dialog_email.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        TextView tv_tb = dialog_email.findViewById(R.id.tv_tb);
        EditText edt_pass_old = dialog_email.findViewById(R.id.edt_pass_old);
        EditText edt_new = dialog_email.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_email.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_email.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_email.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_new = edt_new.getText().toString();
                String pass_old = edt_pass_old.getText().toString();
                String email_old = user.getEmail();

                dialog.ShowDilag("Đang cập nhật lại Email mới.");
                if (TextUtils.isEmpty(pass_old)) {
                    Toast.makeText(MyAccountActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();

                } else if (pass_old.length() < 6) {
                    Toast.makeText(MyAccountActivity.this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();

                } else if (TextUtils.isEmpty(email_new)) {
                    Toast.makeText(MyAccountActivity.this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();

                } else if (!email_new.contains("@gmail.com")) {
                    Toast.makeText(MyAccountActivity.this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();

                } else if (email_new.equalsIgnoreCase(email_old)){
                    dialog.HideDialog();
                    tv_tb.setText("Tài khoản Email mới trùng với tài khoản Email cũ..");
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass_old);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                user.updateEmail(email_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                            databaseRef.child("email").setValue(email_new);
                                            thong_bao1();
                                            dialog_email.dismiss();
                                            dialog.HideDialog();
                                        } else {
                                            dialog.HideDialog();
                                            tv_tb.setText("Thay đổi Email mới thất bại. Vui lòng kiểm tra lại thông tin muốn thay đổi.");
                                        }
                                    }
                                });

                            } else {
                                dialog.HideDialog();
                                tv_tb.setText("Quá trình xác thực mật khẩu cũ với tài khoản đang đăng nhập thất bại. Vui lòng nhập đúng mật khẩu hiện tại để được phép cập nhật Email mới.");
                            }
                        }
                    });

                }

            }
        });
        dialog_email.show();
    }
    private void openDialogNewPass(){
        final Dialog dialog_pass = new Dialog(this);
        dialog_pass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pass.setContentView(R.layout.dialog_change_pass);
        dialog_pass.setCancelable(false);
        Window window = dialog_pass.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        TextView tv_tb = dialog_pass.findViewById(R.id.tv_tb);
        EditText edt_pass_old = dialog_pass.findViewById(R.id.edt_pass_old);
        EditText edt_new = dialog_pass.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_pass.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_pass.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pass.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass_new = edt_new.getText().toString();
                String pass_old = edt_pass_old.getText().toString();

                dialog.ShowDilag("Đang cập nhật lại Email mới.");
                if (TextUtils.isEmpty(pass_old) || TextUtils.isEmpty(pass_new)) {
                    Toast.makeText(MyAccountActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();

                } else if (pass_old.length() < 6 || pass_new.length() < 6) {
                    Toast.makeText(MyAccountActivity.this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
                    dialog.HideDialog();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass_old);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                user.updatePassword(pass_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog_pass.dismiss();
                                            dialog.HideDialog();
                                        } else {
                                            tv_tb.setText("Thay đổi mật khẩu mới thất bại. Vui lòng kiểm tra lại thông tin muốn thay đổi.");
                                            dialog.HideDialog();
                                        }
                                    }
                                });

                            } else {
                                tv_tb.setText("Quá trình xác thực mật khẩu cũ với tài khoản đang đăng nhập thất bại. Vui lòng nhập đúng mật khẩu hiện tại để được phép cập nhật mật khẩu mới.");
                                dialog.HideDialog();
                            }

                        }
                    });

                }
            }
        });

        dialog_pass.show();
    }
    private void send_email_xac_thuc(){
        tv_xac_thuc_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.ShowDilag("Email xác thực đang được gửi đi.");
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            thong_bao();
                            dialog.HideDialog();
                        } else {
                            dialog.HideDialog();
                            // Thông báo cho người dùng rằng không thể gửi email xác thực
                            Toast.makeText(view.getContext(), "Không thể gửi email xác thực.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void thong_bao(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_thong_bao);
        dialog1.setCancelable(false);
        Window window = dialog1.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        AppCompatButton btn_click = dialog1.findViewById(R.id.btn_click);
        TextView tv_thong_bao_dialog = dialog1.findViewById(R.id.tv_thong_bao_dialog);

        user.getEmail();
        tv_thong_bao_dialog.setText("Đã gửi link xác thực tài khoản đến "+ user.getEmail() +". Lưu ý link xác thực có thể gửi đến thư rác trong Email của bạn. Khi bạn ấn vào mục đồng ý hệ thống sẽ tự đăng xuất tài khoản của bạn và trước khi đăng nhập trở lại bạn lên xác thực tài khoản đề được phép dùng đầy đủ chức năng của ứng dụng.");
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    private void thong_bao1(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_thong_bao);
        dialog1.setCancelable(false);
        Window window = dialog1.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        AppCompatButton btn_click = dialog1.findViewById(R.id.btn_click);
        TextView tv_thong_bao_dialog = dialog1.findViewById(R.id.tv_thong_bao_dialog);

        user.getEmail();
        tv_thong_bao_dialog.setText("Chú ý khi ấn vào nút đồng ý hệ thống sẽ đăng xuất tài khoản bạn. Bạn lên đăng nhập lại bằng Email mới và xác thực tài khoản.");
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }


}