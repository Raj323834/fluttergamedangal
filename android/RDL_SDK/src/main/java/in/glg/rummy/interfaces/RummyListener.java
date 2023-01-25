package in.glg.rummy.interfaces;

public interface RummyListener {

    void lowBalance(double lowAmount);
    void onSdkCrash();
    void onResourceNotFound();
}
