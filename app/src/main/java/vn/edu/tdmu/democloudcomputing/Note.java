package vn.edu.tdmu.democloudcomputing;

public class Note {
    private String idNotes; // ID của ghi chú
    private String idUser ;  // ID của người dùng
    private String noiDung; // Nội dung ghi chú
    private String tieuDe;  // Tiêu đề ghi chú
    private String time;     // Thời gian
    private String tinhTrang; // Tình trạng

    public Note(String idNotes, String idUser , String noiDung, String tieuDe, String time, String tinhTrang) {
        this.idNotes = idNotes;
        this.idUser  = idUser ;
        this.noiDung = noiDung;
        this.tieuDe = tieuDe;
        this.time = time;
        this.tinhTrang = tinhTrang;
    }

    public String getIdNotes() {
        return idNotes;
    }

    public String getIdUser () {
        return idUser ;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getTime() {
        return time;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }
}
