package vn.edu.tdmu.democloudcomputing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends Activity {

    TextView titleName, titleUsername, profileName, profileEmail, profileUsername, profilePassword;
    Button edtProfile, btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các TextView
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        btnExit = findViewById(R.id.exitButton);

        // Lấy username từ Intent
        String userUsername = getIntent().getStringExtra("username");

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo dialog cảnh báo
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");

                // Nút xác nhận
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý đăng xuất
                        logout();
                    }
                });

                // Nút hủy
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng dialog nếu không muốn đăng xuất
                        dialog.dismiss();
                    }
                });

                // Hiển thị dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        if (userUsername != null) {
            // Truy vấn Firebase để lấy thông tin người dùng
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@Nullable DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Lấy dữ liệu từ snapshot
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String email = userSnapshot.child("email").getValue(String.class);
                            String username = userSnapshot.child("username").getValue(String.class);
                            String password = userSnapshot.child("password").getValue(String.class);

                            // Hiển thị dữ liệu lên TextView
                            titleName.setText(name);
                            titleUsername.setText(username);
                            profileName.setText(name);
                            profileEmail.setText(email);
                            profileUsername.setText(username);
                            profilePassword.setText(password);
                        }
                    } else {
                        // Xử lý nếu username không tồn tại
                        Toast.makeText(ProfileActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@Nullable DatabaseError error) {
                    // Xử lý lỗi
                    Toast.makeText(ProfileActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No username passed!", Toast.LENGTH_SHORT).show();
        }
    }
    private void logout() {
        // Nếu bạn sử dụng Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Chuyển hướng về LoginActivity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Kết thúc Activity hiện tại
        finish();
    }

}

