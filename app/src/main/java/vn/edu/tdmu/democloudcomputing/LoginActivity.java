package vn.edu.tdmu.democloudcomputing;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        // Lấy tham chiếu đến Firebase Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // Truy vấn kiểm tra người dùng theo trường "username"
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lặp qua tất cả các kết quả trả về (chỉ có 1 người dùng trong trường hợp này)
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String id_user = userSnapshot.getKey(); // Lấy id_user từ Firebase (khóa chính)
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        // Kiểm tra mật khẩu
                        if (passwordFromDB == null || passwordFromDB.isEmpty()) {
                            loginPassword.setError("Password not found in database");
                            loginPassword.requestFocus();
                            return;
                        }

                        // Nếu mật khẩu đúng
                        if (passwordFromDB.equals(userPassword)) {
                            // Lấy thông tin khác của người dùng nếu cần
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);
                            String usernameFromDB = userSnapshot.child("username").getValue(String.class);

                            // Chuyển sang MainActivity kèm theo thông tin id_user và name
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("id_user", id_user); // Truyền id_user qua Intent
                            intent.putExtra("name", nameFromDB); // Truyền tên người dùng qua Intent
                            startActivity(intent);
                            finish(); // Đóng LoginActivity
                        } else {
                            loginPassword.setError("Invalid Credentials");
                            loginPassword.requestFocus();
                        }
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}