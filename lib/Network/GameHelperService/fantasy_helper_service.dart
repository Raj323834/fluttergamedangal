import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import '../network_post_request.dart';

class FantasyHelperService {
  static Future<Map<String, Object>> fantasyLaunch(
    String userId,
  ) async {
    String userName = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );
    String avatarUrl = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.avatarURL,
    );
    Map<String, dynamic> requestData = {
      "username": userName,
      "avatarUrl": avatarUrl
    };
    Map<String, dynamic> result =
        await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.fantasyLaunchUrl,
      userId,
    );
    CommonMethods.printLog(
      "Fantasy Launch ",
      "Fantasy Launch :--> " + result.toString(),
    );
    return result;
  }
}
