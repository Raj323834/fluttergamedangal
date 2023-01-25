package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.response.RummyBaseResponse;

@Root(name = "request",strict = false)
public class RummyLogoutRequest extends RummyBaseResponse {
    @Attribute(name = "command", required = false)
    public String command;

    public int getErrorMessage() {
        return 0;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
