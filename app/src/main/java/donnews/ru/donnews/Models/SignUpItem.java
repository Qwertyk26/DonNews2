package donnews.ru.donnews.Models;

/**
 * Created by antonnikitin on 12.04.17.
 */

public class SignUpItem {
    private String message;
    private String dn_token;
    private String name;
    private String soc_type;
    private boolean success;
    private boolean auth;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDn_token() {
        return dn_token;
    }

    public void setDn_token(String dn_token) {
        this.dn_token = dn_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoc_type() {
        return soc_type;
    }

    public void setSoc_type(String soc_type) {
        this.soc_type = soc_type;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
