package vn.edu.tdmu.democloudcomputing;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class Add_work extends AppCompatActivity {

    private Button btnSetTime;
    private TextView tvSelectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);

        btnSetTime = findViewById(R.id.btn_set_time);
        tvSelectedTime = findViewById(R.id.tv_time_label);

        // Hiển thị TimePickerDialog khi bấm nút
        btnSetTime.setOnClickListener(v -> showTimePicker());
    }

    private void showTimePicker() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (TimePicker view, int hourOfDay, int minute1) -> {
                    // Cập nhật TextView với thời gian được chọn
                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    tvSelectedTime.setText("Thời gian đã chọn: " + selectedTime);
                },
                hour,
                minute,
                true // Sử dụng định dạng 24 giờ (true: 24 giờ, false: 12 giờ AM/PM)
        );

        // Hiển thị TimePickerDialog
        timePickerDialog.show();
    }
}
