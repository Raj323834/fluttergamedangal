package in.glg.rummy.packagedev.android.api.base.builders.xml;

public interface RummyXmlBuilder {
    <T> T getEntityForJson(String str, Class<T> cls);

    <T> String getJsonForEntity(RummyXmlInterface<T> xmlInterface);
}
