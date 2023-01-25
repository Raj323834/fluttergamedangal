package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by GridLogic on 1/12/17.
 */

@Root(name = "request",strict = false)
public class RummyTournamentsDataRequest extends RummyBaserequest
{
    @Attribute(name = "command")
    private String command;

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
