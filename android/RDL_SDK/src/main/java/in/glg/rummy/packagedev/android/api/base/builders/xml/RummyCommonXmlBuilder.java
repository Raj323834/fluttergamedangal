package in.glg.rummy.packagedev.android.api.base.builders.xml;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

public class RummyCommonXmlBuilder {
    public static final String TAG = RummyCommonXmlBuilder.class.getSimpleName();

    public <T> T getEntityForXml(String xmlDoc, Class<? extends T> entity) {
        try {
            return (T) new Persister().read((Class) entity, xmlDoc);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> String getXmlForEntity(RummyXmlInterface<T> entity) {
        Serializer xmlSerializer = new Persister();
        Writer stringWriter = new StringWriter();
        try {
            xmlSerializer.write((Object) entity, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }
}
