package in.glg.rummy.models;

import org.simpleframework.xml.Root;

import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;

@Root(name = "data",strict = false)
public class RummyEmialUsRequest extends RummyAbstractDataRequest {
    private String email;
    private String message;
    private String name;
    private String playerid;
    private String subject;

    public String getPlayerid() {
        return this.playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
