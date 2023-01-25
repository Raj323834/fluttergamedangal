package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.builder.xml.RummyXmlInterface;

@Root(name = "authreq", strict = false)
public class RummyAuthReq implements RummyXmlInterface {
    @Attribute(name = "msg_uuid")
    public String msg_uuid;
    @Attribute(name = "timestamp")
    public String timestamp;
}
