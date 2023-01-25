package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.builder.xml.RummyXmlInterface;

@Root(strict = false)
public class RummyBaserequest implements RummyXmlInterface {
    @Attribute(name = "DEVICE_ID")
    private String deviceId = "ANDROID";
    @Attribute(name = "system")
    protected String system = "TajRummy";
    @Attribute(name = "msg_uuid")
    private String uuid;

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
