package vn.edu.tdmu.democloudcomputing;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Daily_work extends AppCompatActivity {

    private TextView tvMonthYear;
    private GridLayout gridCalendar;
    private ImageButton btnPrevMonth, btnNextMonth;
    private Button btnToday;

    private Calendar calendar;
    private List<Integer> selectedDays;  // Danh sách lưu các ngày được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work);

        // Ánh xạ các view
        tvMonthYear = findViewById(R.id.tvMonthYear);
        gridCalendar = findViewById(R.id.gridCalendar);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnToday = findViewById(R.id.btnToday);

        // Khởi tạo Calendar và danh sách các ngày được chọn
        calendar = Calendar.getInstance();
        selectedDays = new ArrayList<>();

        // Hiển thị tháng và năm hiện tại
        updateCalendar();

        // Xử lý sự kiện nút
        btnPrevMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(1));
        btnToday.setOnClickListener(v -> goToToday());
    }

    private void updateCalendar() {
        // Xóa tất cả các ô cũ trong GridLayout
        gridCalendar.removeAllViews();

        // Cập nhật tiêu đề tháng và năm
        int month = calendar.get(Calendar.MONTH); // Tháng (0-11)
        int year = calendar.get(Calendar.YEAR);   // Năm
        tvMonthYear.setText(String.format("%d tháng %d", year, month + 1));

        // Thêm hàng tiêu đề các ngày trong tuần
        addWeekDaysHeader();

        // Tính ngày đầu tháng và số ngày trong tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Chuyển về 0-6 (Chủ Nhật - Thứ Bảy)
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Thêm các ô trống trước ngày 1
        for (int i = 0; i < firstDayOfWeek; i++) {
            addDayCell("", false, 0);
        }

        // Thêm các ngày trong tháng
        for (int day = 1; day <= daysInMonth; day++) {
            addDayCell(String.valueOf(day), true, day);
        }
    }

    private void addWeekDaysHeader() {
        String[] weekDays = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};

        for (int i = 0; i < weekDays.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(weekDays[i]);
            textView.setTextSize(14); // Giảm kích thước chữ
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setPadding(4, 4, 4, 4);

            // Cài đặt màu sắc
            if (i == 0) {
                textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                textView.setTextColor(getResources().getColor(android.R.color.black));
            }

            // Cài đặt kích thước và margin
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels / 8.5); // Nhỏ hơn để vừa màn hình
            params.height = params.width / 2; // Chiều cao nhỏ hơn chiều rộng
            params.setMargins(4, 4, 4, 4); // Thêm margin
            textView.setLayoutParams(params);

            gridCalendar.addView(textView);
        }
    }

    private void addDayCell(String text, boolean isDay, int dayOfMonth) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setPadding(4, 4, 4, 4);

        // Lấy ngày hiện tại
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int currentMonth = today.get(Calendar.MONTH);
        int currentYear = today.get(Calendar.YEAR);

        // Kiểm tra ngày trong tháng
        if (isDay) {
            // Nếu là ngày trong tháng
            if (dayOfMonth == currentDay && calendar.get(Calendar.MONTH) == currentMonth && calendar.get(Calendar.YEAR) == currentYear) {
                textView.setBackground(getResources().getDrawable(R.drawable.square_border));; // Đánh dấu ngày hiện tại bằng màu đỏ
            }

            // Kiểm tra ngày quá khứ và tương lai
            if (calendar.get(Calendar.YEAR) < currentYear ||
                    (calendar.get(Calendar.YEAR) == currentYear && calendar.get(Calendar.MONTH) < currentMonth) ||
                    (calendar.get(Calendar.YEAR) == currentYear && calendar.get(Calendar.MONTH) == currentMonth && dayOfMonth < currentDay)) {
                // Ngày quá khứ: màu xám
                textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                // Ngày tương lai: màu đen
                textView.setTextColor(getResources().getColor(android.R.color.black));
            }

            // Nếu ngày đã được chọn, thay đổi màu nền
            if (selectedDays.contains(dayOfMonth)) {
                textView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Màu nền khi chọn
            }
        } else {
            // Ngày trống: màu xám nhạt
            textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        // Cài đặt kích thước và margin
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels / 8.5);
        params.height = params.width; // Hình vuông
        params.setMargins(4, 4, 4, 4); // Thêm margin
        textView.setLayoutParams(params);

        // Thêm sự kiện khi nhấn vào ngày
        textView.setOnClickListener(v -> selectDay(dayOfMonth));

        gridCalendar.addView(textView);
    }

    private void changeMonth(int amount) {
        calendar.add(Calendar.MONTH, amount);
        updateCalendar();
    }

    private void goToToday() {
        calendar = Calendar.getInstance();
        updateCalendar();
    }

    private void selectDay(int day) {
        if (selectedDays.contains(day)) {
            selectedDays.remove(Integer.valueOf(day));  // Nếu ngày đã được chọn, hủy chọn
        } else {
            selectedDays.add(day);  // Nếu ngày chưa được chọn, thêm vào danh sách
        }

        // Cập nhật giao diện sau khi chọn hoặc hủy chọn ngày
        updateCalendar();
    }
}
