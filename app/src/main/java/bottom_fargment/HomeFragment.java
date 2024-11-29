package bottom_fargment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import android.widget.*;
import com.google.firebase.database.*;
import java.util.ArrayList;

import vn.edu.tdmu.democloudcomputing.Note;
import vn.edu.tdmu.democloudcomputing.NoteAdapter;
import vn.edu.tdmu.democloudcomputing.R;

public class HomeFragment extends Fragment {

    private String id_user;
    private String userName;
    private Spinner spnNote;
    private ListView lstNote;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public HomeFragment() {
        // Constructor rỗng
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User  Prefs", getActivity().MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user", null);
        userName = sharedPreferences.getString("name", "Người dùng không xác định");

        // Hiển thị tên người dùng
        TextView textView = view.findViewById(R.id.userName);
        textView.setText(userName);

        // Khởi tạo Spinner và ListView
        spnNote = view.findViewById(R.id.spnNote);
        lstNote = view.findViewById(R.id.lstNotes);

        // Thiết lập Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("notes");

        // Cấu hình Spinner tình trạng
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Complete", "Incomplete", "In Progress"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNote.setAdapter(adapter);

        // Xử lý khi chọn item trong Spinner
        spnNote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                Query query;

                if (selectedStatus.equals("All")) {
                    // Hiển thị tất cả ghi chú của id_user
                    query = reference.orderByChild("id_user").equalTo(id_user);
                } else {
                    // Hiển thị ghi chú với tình trạng cụ thể
                    query = reference.orderByChild("id_user_tinh_trang").equalTo(id_user + "_" + selectedStatus);
                }

                // Gọi hàm hiển thị danh sách
                showNotes(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không chọn item nào
            }
        });

        return view;
    }

    private void showNotes(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> notesList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.exists()) {
                        // Lấy dữ liệu từ Firebase với kiểu dữ liệu chính xác
                        String idNotes = String.valueOf(data.child("id_notes").getValue(Long.class)); // Chuyển đổi Long thành String
                        String idUser  = data.child("id_user").getValue(String.class);
                        String noiDung = data.child("noi_dung").getValue(String.class);
                        String tieuDe = data.child("tieu_de").getValue(String.class);
                        String time = data.child("time").getValue(String.class);
                        String tinhTrang = data.child("tinh_trang").getValue(String.class);

                        // Tạo đối tượng Note và thêm vào danh sách
                        Note note = new Note(idNotes, idUser , noiDung, tieuDe, time, tinhTrang);
                        notesList.add(note);
                    }
                }

                // Kiểm tra nếu danh sách rỗng
                if (notesList.isEmpty()) {
                    notesList.add(new Note("", "", "Không có ghi chú nào phù hợp.", "", "", ""));
                }

                // Cập nhật ListView với adapter tùy chỉnh
                NoteAdapter adapter = new NoteAdapter(getContext(), notesList);
                lstNote.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
