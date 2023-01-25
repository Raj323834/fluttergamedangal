package in.glg.rummy.api.builder.xml;

public interface RummyXmlBuilder {
    <T> T getEntityForJson(String str, Class<T> cls);

    <T> String getJsonForEntity(RummyXmlInterface<T> xmlInterface);
}
