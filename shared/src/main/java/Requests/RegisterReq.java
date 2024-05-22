package Requests;

public class RegisterReq {
    private String email;
    private String username;
    private String password;


    public RegisterReq(){}

    public RegisterReq(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
