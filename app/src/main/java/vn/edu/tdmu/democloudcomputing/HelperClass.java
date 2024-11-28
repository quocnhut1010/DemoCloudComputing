package vn.edu.tdmu.democloudcomputing;

public class HelperClass {
    String id_user, name, email, username, password;

    // Constructor đầy đủ
    public HelperClass(String id_user, String name, String email, String username, String password) {
        this.id_user = id_user;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Constructor rỗng (cần thiết cho Firebase)
    public HelperClass() {
    }

    // Getter và Setter
    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
