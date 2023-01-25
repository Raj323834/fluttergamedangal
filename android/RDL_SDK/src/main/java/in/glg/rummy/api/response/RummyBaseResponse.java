package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.builder.xml.RummyXmlInterface;
import in.glg.rummy.utils.RummyErrorCodes;

@Root(strict = false)
public abstract class RummyBaseResponse implements RummyXmlInterface {
    @Attribute(name = "code", required = false)
    protected String code;
    @Attribute(name = "msg_uuid", required = false)
    protected String msg_uuid;
    @Attribute(name = "timestamp", required = false)
    protected String timestamp;
    @Attribute(name = "type", required = false)
    protected String type;

    public abstract int getErrorMessage();

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsg_uuid() {
        return this.msg_uuid;
    }

    public void setMsg_uuid(String msg_uuid) {
        this.msg_uuid = msg_uuid;
    }

    public boolean isSuccessful() {
        return !this.code.matches("[0-9]+") || Integer.parseInt(this.code) == RummyErrorCodes.SUCCESS;
    }
}
