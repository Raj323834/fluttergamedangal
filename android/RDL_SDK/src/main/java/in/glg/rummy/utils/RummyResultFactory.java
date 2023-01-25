package in.glg.rummy.utils;

public class RummyResultFactory {

   public static <R> RummyApiResult<R> success(R result){
       return new RummyApiResult<>(result);
   }
   public static <R> RummyApiResult<R> error(String message){
       return new RummyApiResult<>(message);
   }
   public static <R> RummyApiResult<R> setLoading(boolean isLoading){
       return new RummyApiResult<>(isLoading);
   }
   
}
