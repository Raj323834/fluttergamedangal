package in.glg.rummy.packagedev.android.api.base.result;

import java.util.List;

public class RummyDataResult<T> {
    public List<T> entities;
    public T entity;
    public int statusCode;
    public boolean successful;
}
