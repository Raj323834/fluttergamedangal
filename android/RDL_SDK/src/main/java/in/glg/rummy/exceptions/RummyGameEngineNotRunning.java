package in.glg.rummy.exceptions;

public class RummyGameEngineNotRunning extends Exception {
    public RummyGameEngineNotRunning(String message) {
        super(message);
    }

    public RummyGameEngineNotRunning(Throwable cause) {
        super(cause);
    }
}
