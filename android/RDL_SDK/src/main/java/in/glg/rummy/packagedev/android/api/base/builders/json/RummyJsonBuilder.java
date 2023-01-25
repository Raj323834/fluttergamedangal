package in.glg.rummy.packagedev.android.api.base.builders.json;

public interface RummyJsonBuilder {
    <T> T getEntityForJson(String str, Class<T> cls);

    <T> String getJsonForEntity(RummyJsonInterface<T> jsonInterface);
}
