package in.glg.rummy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RummyDepositsAPIResponse
{

    @SerializedName("data")
    @Expose
    private RummyDepositsResposeData data;

    public RummyDepositsResposeData getData() {
        return data;
    }

    public void setData(RummyDepositsResposeData data) {
        this.data = data;
    }
}
