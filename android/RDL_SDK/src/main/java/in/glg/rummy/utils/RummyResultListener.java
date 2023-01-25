package in.glg.rummy.utils;

import android.widget.Toast;

import org.json.JSONObject;

import in.glg.rummy.RummyApplication;

public abstract class RummyResultListener {

    public abstract void onSuccess(JSONObject jsonObject);
    
    public void onFailed(String error){
        //Toast.makeText(RummyApplication.getInstance().getContext(),error,Toast.LENGTH_SHORT).show();
    }
}
