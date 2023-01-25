package in.glg.rummy.utils;

public class RummyApiResult<T> {
    
    boolean isSuccess;
    T result;
    String errorMessage;
    boolean isConsumed = false;
    boolean isLoading = false;
    
    public RummyApiResult(T result) {
        this.result = result;
        isSuccess = true;
        this.isLoading = false;
    }
    
    public RummyApiResult(boolean isLoading) {
        this.isLoading = isLoading;
        isSuccess = false;
    }
    
    
    public RummyApiResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isSuccess = false;
        this.isLoading = false;
    }
    
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public T getResult() {
        return result;
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
    }
    
    public void setConsumed() {
        isConsumed = true;
    }
    
    public boolean isConsumed() {
        return isConsumed;
    }
    
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    
    public boolean isLoading() {
        return isLoading;
    }
}


