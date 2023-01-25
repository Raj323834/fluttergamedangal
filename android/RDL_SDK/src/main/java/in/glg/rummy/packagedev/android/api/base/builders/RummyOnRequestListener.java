package in.glg.rummy.packagedev.android.api.base.builders;

import in.glg.rummy.packagedev.android.api.base.result.RummyDataResult;

public interface RummyOnRequestListener {
    <T> void onRequestFail(RummyDataResult<T> dataResult);

    <T> void onRequestFail(String str);

    <T> void onRequestResult(RummyDataResult<T> dataResult);

    <T> void onRequestStart();
}
