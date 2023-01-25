package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class RummyRebuyResponse extends RummyBaseResponse
{
    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "table_ammount", required = false)
    private String table_ammount;

    @Attribute(name = "table_id", required = false)
    private String table_id;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTable_ammount() {
        return table_ammount;
    }

    public void setTable_ammount(String table_ammount) {
        this.table_ammount = table_ammount;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
