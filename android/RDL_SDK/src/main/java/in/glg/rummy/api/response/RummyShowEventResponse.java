package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class RummyShowEventResponse extends RummyBaseResponse {
    @Attribute(name = "data", required = false)
    private String data;
    @Attribute(name = "meldtimer", required = false)
    private String meldTimer;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "timeout", required = false)
    private String timeOut;

    public String getMeldTimer() {
        return this.timeOut;
    }

    public void setMeldTimer(String meldTimer) {
        this.timeOut = meldTimer;
    }

    public String getMeldtimer() {
        return this.meldTimer;
    }

    public void setMeldtimer(String meldtimer) {
        this.meldTimer = meldtimer;
    }

    public String getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public int getErrorMessage() {
        return 0;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
