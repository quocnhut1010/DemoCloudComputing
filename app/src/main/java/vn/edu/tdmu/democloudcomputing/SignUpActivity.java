package vn.edu.tdmu.democloudcomputing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                // Kiểm tra các trường không được để trống
                if (name.isEmpty()) {
                    signupName.setError("Name cannot be empty");
                    signupName.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    signupEmail.requestFocus();
                    return;
                }
                if (username.isEmpty()) {
                    signupUsername.setError("Username cannot be empty");
                    signupUsername.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    signupPassword.requestFocus();
                    return;
                }

                // Kiểm tra định dạng email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupEmail.setError("Enter a valid email address");
                    signupEmail.requestFocus();
                    return;
                }

                // Kiểm tra độ dài mật khẩu (tối thiểu 6 ký tự)
                if (password.length() < 6) {
                    signupPassword.setError("Password must be at least 6 characters");
                    signupPassword.requestFocus();
                    return;
                }

                // Kiểm tra tên người dùng chỉ chứa chữ cái và số
                if (!username.matches("[a-zA-Z0-9]+")) {
                    signupUsername.setError("Username can only contain letters and numbers");
                    signupUsername.requestFocus();
                    return;
                }

                // Nếu tất cả kiểm tra đều hợp lệ, lưu thông tin vào Firebase
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Kiểm tra username trùng lặp trước khi lưu
                reference.child(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        signupUsername.setError("Username already exists");
                        signupUsername.requestFocus();
                    } else {
                        HelperClass helperClass = new HelperClass(name, email, username, password);
                        reference.child(username).setValue(helperClass).addOnCompleteListener(setTask -> {
                            if (setTask.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}