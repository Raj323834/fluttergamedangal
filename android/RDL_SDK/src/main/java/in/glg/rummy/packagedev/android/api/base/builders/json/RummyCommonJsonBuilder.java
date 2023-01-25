package in.glg.rummy.packagedev.android.api.base.builders.json;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RummyCommonJsonBuilder {
    public static final String TAG = RummyCommonJsonBuilder.class.getSimpleName();

    public <T> T getEntityForJson(String json, Class<T> entity) {
        try {
            return new Gson().fromJson(json, entity);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> String getJsonForEntity(RummyJsonInterface<T> entity) {
        try {
            return new Gson().toJson(entity);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> Type getEntityForJson(String json, Type type) {
        try {
            return (Type) new Gson().fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> RummyGenericGsonListResult<T> getListOfTypeForJson(String json, Type type) {
        try {
            return (RummyGenericGsonListResult) new Gson().fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> RummyGenericGsonResult<T> getTypeForJson(String json, Type type) {
        try {
            return (RummyGenericGsonResult) new Gson().fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }

    public <T> List<T> getListForJson(String json, Type type) {
        try {
            return (ArrayList) new Gson().fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, String.format("%s  ", new Object[]{getClass().getName()}), e);
            return null;
        }
    }
}
