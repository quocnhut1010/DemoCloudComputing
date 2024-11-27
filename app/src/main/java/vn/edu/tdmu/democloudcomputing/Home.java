package vn.edu.tdmu.democloudcomputing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends Activity {
    private String id_user; // Biến để lưu id_user
    private TextView ten; // TextView để hiển thị tên người dùng
    Button btnthem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home); // Gọi layout cho activity_home

        // Lấy id_user từ Intent
        id_user = getIntent().getStringExtra("id_user");

        // Ánh xạ TextView
        ten = findViewById(R.id.userName);

        // Kiểm tra xem id_user có tồn tại không
        if (id_user != null) {
            // Truy vấn Firebase để lấy thông tin người dùng theo id_user
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(id_user);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        ten.setText(name); // Hiển thị tên người dùng lên TextView
                    } else {
                        Toast.makeText(Home.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Home.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No user ID passed!", Toast.LENGTH_SHORT).show();
        }
    }
}
