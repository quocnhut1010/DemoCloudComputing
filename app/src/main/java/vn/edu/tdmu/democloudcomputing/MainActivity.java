package vn.edu.tdmu.democloudcomputing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import bottom_fargment.HomeFragment;
import bottom_fargment.SettingFragment;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    String id_user;
    String userName; // Thêm biến để lưu tên người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nhận id_user và userName từ Intent
        id_user = getIntent().getStringExtra("id_user");
        userName = getIntent().getStringExtra("name"); // Nhận tên người dùng từ Intent


        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_user", id_user); // Truyền id_user vào HomeFragment
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, homeFragment) // R.id.fragment_container là container của Fragment
                .commit();
        // Chỉ mở HomeFragment khi lần đầu tiên mở MainActivity
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Thay vì mở Activity, load Fragment
        }

        // Lưu thông tin vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("User  Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_user", id_user);
        editor.putString("name", userName); // Lưu tên người dùng
        editor.apply();
        // Gán view cho BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.setting) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("id_user", id_user);  // Đảm bảo rằng userId là giá trị chính xác
                startActivity(intent);
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Sự kiện cho FloatingActionButton
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Log.d("MainActivity", "FloatingActionButton clicked");
            showBottomDialog();
        });
    }

    // Phương thức để load Fragment vào FrameLayout
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    // Phương thức hiển thị BottomSheetDialog
    private void showBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_transaction);

        Button btnAdd = bottomSheetDialog.findViewById(R.id.btnAdd);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                Toast.makeText(getApplicationContext(), "Thêm Ghi Chu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Add_work.class);
                intent.putExtra("id_user", id_user);  // Đảm bảo rằng userId là giá trị chính xác
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });
        } else {
            Log.e("MainActivity", "Button btnAdd not found in BottomSheetDialog layout");
        }

        bottomSheetDialog.show();
    }
}