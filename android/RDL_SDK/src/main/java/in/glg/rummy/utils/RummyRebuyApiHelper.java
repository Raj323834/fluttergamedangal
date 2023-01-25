package in.glg.rummy.utils;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import in.glg.rummy.models.RummyTableDetails;

public class RummyRebuyApiHelper {
    final private static String apiURL = RummyUtils.getApiSeverAddress() + "api/v1/do-rebuy-or-rejoin/";
    
    private static String getChipType(RummyTableDetails rummyTableDetails) {
        String chipType = "";
        if (rummyTableDetails.getTableCost().equalsIgnoreCase("CASH_CASH")) {
            chipType = "cash";
        } else {
            chipType = "funchips";
        }
        return chipType;
    }
    
    private static String getGameSettingId(RummyTableDetails rummyTableDetails) {
        String settingId = "";
        String[] split = rummyTableDetails.getUnique_gamesettings_id().split("_");
        if (split.length > 0) {
            settingId = split[split.length - 1];
        } else {
            settingId = rummyTableDetails.getUnique_gamesettings_id();
        }
        return settingId;
    }
    
    public static StringRequest prRebuyIn(String amount,
                                          RummyTableDetails mTableDetails,
                                          RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        
        
        Map<String, String> params = getPrRebuyInParams(mTableDetails.getTableId(),
                mTableDetails.getGameId(),
                getChipType(mTableDetails),
                amount,
                mTableDetails.getTableType(),
                getGameSettingId(mTableDetails));
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    
    private static Map<String, String> getPrRebuyInParams(String tableId,
                                                          String gameId,
                                                          String chips,
                                                          String amount,
                                                          String gameType,
                                                          String gamessttingId) {
        
        Map<String, String> params = new HashMap<>();
        params.put("tableid", tableId);
        params.put("gameid", gameId);
        params.put("event_name", "rebuyin");
        params.put("chips", chips);
        params.put("amount", amount);
        params.put("game_type", gameType);
        params.put("gamesetting_id", gamessttingId);
        return params;
    }
    
    
    public static StringRequest poolRebuyIn(RummyTableDetails rummyTableDetails,
                                            RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        
        Map<String, String> params = getPoolRebuyInParams(rummyTableDetails.getTableId(),
                rummyTableDetails.getGameId(), getChipType(rummyTableDetails), getGameSettingId(rummyTableDetails));
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    
    private static Map<String, String> getPoolRebuyInParams(String tableId,
                                                            String gameId,
                                                            String chips,
                                                            String gameSettingId) {
        
        Map<String, String> params = new HashMap<>();
        params.put("tableid", tableId);
        params.put("gameid", gameId);
        params.put("event_name", "rejoin");
        params.put("chips", chips);
        params.put("gamesetting_id", gameSettingId);
        return params;
    }
    
    
}
