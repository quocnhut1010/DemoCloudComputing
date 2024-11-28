package bottom_fargment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.edu.tdmu.democloudcomputing.R;

public class HomeFragment extends Fragment {
    private String id_user;
    private String userName;

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

        // Lấy TextView để hiển thị tên người dùng
        TextView textView = view.findViewById(R.id.userName);
        textView.setText(userName); // Hiển thị tên người dùng

        return view;
    }
}