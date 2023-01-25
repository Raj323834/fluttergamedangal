import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import '../network_post_request.dart';

class ArtoonHelperService {
  static Future<Map<String, Object>> getBalanceCheck(
    String lobbyId,
    String game,
    String entryFee,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    String state = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.state,
        ) ??
        StringConstants.emptyString;
    try {
      Map requestData = {
        "game_name": game, //"Callbreak" or "Pool", //Game Name
        "lobby_id": lobbyId, //Id of lobby,
        "entry_fee": entryFee, //Entry Fee
        "state": state,
      };

      result = await NetworkPostRequest.postRequestWithAccess(
        requestData,
        UrlConstants.artoonBalanceURl,
        userId,
      );

      CommonMethods.printLog(
        "Artoon",
        result.toString(),
      );

      return result;
    } catch (e) {
      result['error'] = 'Error occured while logging in: ' + e.toString();
      return result;
    }
  }
}
