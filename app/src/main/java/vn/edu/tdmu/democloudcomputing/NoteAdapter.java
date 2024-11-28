package vn.edu.tdmu.democloudcomputing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, R.layout.list_item_note, notes);
        this.context = context;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_note, parent, false);

        TextView txtContent = rowView.findViewById(R.id.txtNoiDung);
        TextView txtTime = rowView.findViewById(R.id.txtTime);
        TextView txtStatus = rowView.findViewById(R.id.txtTinhTrang);

        Note note = notes.get(position);
        txtContent.setText(note.getNoiDung()); // Hiển thị nội dung
        txtTime.setText(note.getTime()); // Hiển thị thời gian
        txtStatus.setText(note.getTinhTrang()); // Hiển thị tình trạng

        // Thi ết lập màu sắc cho tình trạng
        switch (note.getTinhTrang()) {
            case "Complete":
                txtStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
                break;
            case "Incomplete":
                txtStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                break;
            case "In Progress":
                txtStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
                break;
            default:
                txtStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                break;
        }

        return rowView;
    }
}