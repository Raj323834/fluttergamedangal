package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by GridLogic on 14/12/17.
 */
@Root(strict = false)
public class RummyGetTableDetailsRequest extends RummyBaserequest
{
    @Attribute(name = "command")
    private String command;

    @Attribute(name = "table_id")
    private String table_id;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }
}
