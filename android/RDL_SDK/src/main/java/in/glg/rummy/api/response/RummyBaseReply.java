package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

/**
 * Created by GridLogic on 9/1/18.
 */

@Root(name = "reply",strict = false)
public class RummyBaseReply extends RummyBaserequest
{
    @Attribute(name = "code")
    private String code;

    @Attribute(name = "type")
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
