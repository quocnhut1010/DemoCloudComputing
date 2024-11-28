package vn.edu.tdmu.democloudcomputing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class Add_work extends AppCompatActivity {

    private Button btnSetTime, btnAdd, btnClear;
    private TextView tvSelectedTime;
    private EditText edtTieuDe, edtNoidung;
    private Spinner spnTinhtrang;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String selectedTime = "";
    private long lastId = 0; // Lưu trữ id_notes cuối cùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);

        // Lấy id_user từ Intent
        String id_user = getIntent().getStringExtra("id_user");

        btnSetTime = findViewById(R.id.btn_set_time);
        tvSelectedTime = findViewById(R.id.tv_time_label);
        edtTieuDe = findViewById(R.id.et_subtitle);
        edtNoidung = findViewById(R.id.et_task_content);
        spnTinhtrang = findViewById(R.id.spinner_note_type);
        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);

        // Cấu hình Spinner tình trạng
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Complete", "Incomplete", "In Progress"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTinhtrang.setAdapter(adapter);

        // Firebase reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("notes");

        // Lấy id_notes cuối cùng và gán giá trị vào lastId
        reference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    lastId = Long.parseLong(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Add_work.this, "Failed to load last ID", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Set Time (Ngày, giờ, phút)
        btnSetTime.setOnClickListener(v -> showDateTimePicker());

        // Nút Add
        btnAdd.setOnClickListener(view -> addNote(id_user));

        //nut clear
        btnClear.setOnClickListener(view -> clearFields());

    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, selectedHour, selectedMinute) -> {
                selectedTime = String.format("%04d-%02d-%02d %02d:%02d", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
                tvSelectedTime.setText("Thời gian đã chọn: " + selectedTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addNote(String id_user) {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String noiDung = edtNoidung.getText().toString().trim();
        String tinhTrang = spnTinhtrang.getSelectedItem().toString();

        if (tieuDe.isEmpty() || noiDung.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tăng id_notes
        long newId = lastId + 1;

        // Tạo dữ liệu note
        HashMap<String, Object> note = new HashMap<>();
        note.put("id_notes", newId);
        note.put("id_user", id_user);
        note.put("tieu_de", tieuDe);
        note.put("noi_dung", noiDung);
        note.put("time", selectedTime);
        note.put("tinh_trang", tinhTrang);
        note.put("id_user_tinh_trang", id_user + "_" + tinhTrang); // Thêm giá trị id_user_tinh_trang

        // Lưu vào Firebase
        reference.child(String.valueOf(newId)).setValue(note)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thêm ghi chú thành công", Toast.LENGTH_SHORT).show();
                    lastId = newId; // Cập nhật id cuối cùng
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Thêm ghi chú thất bại", Toast.LENGTH_SHORT).show());
    }
    private void clearFields() {
        edtTieuDe.setText(""); // Xóa nội dung tiêu đề
        edtNoidung.setText(""); // Xóa nội dung mô tả
        spnTinhtrang.setSelection(0); // Đặt lại Spinner về mục đầu tiên
        tvSelectedTime.setText("Thời gian đã chọn:"); // Đặt lại TextView về trạng thái mặc định
        selectedTime = ""; // Xóa thời gian được chọn
    }

}
