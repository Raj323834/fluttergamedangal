package in.glg.rummy.api.requests;

import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;

public class RummySignUpRequest extends RummyAbstractDataRequest {
    public String channel;
    public String email;
    public String password;
    public String subChannel = "Android";
    public String userName;

    public String getSubChannel() {
        return this.subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
