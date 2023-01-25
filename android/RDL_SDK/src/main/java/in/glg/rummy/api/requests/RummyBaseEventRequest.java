package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.builder.xml.RummyXmlInterface;

@Root(strict = false)
public class RummyBaseEventRequest implements RummyXmlInterface {
    @Attribute(name = "msg_uuid")
    private String uuid;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
