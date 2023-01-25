import '../../constants/methods/common_methods.dart';
import '../../network_new/constants/url_constants.dart';
import '../network_get_request.dart';

class PokerHelperService {
  static Future<Map<String, Object>> pokerLaunch(
    String username,
    String firstname,
    String deviceType,
    String avatarUrl,
    String userId,
  ) async {
    final queryParameters = {
      "user_name": username,
      "first_name": firstname,
      "device_type": deviceType,
      "avatar_url": avatarUrl
    };

    Map<String, dynamic> result =
        await NetworkGetRequest.getRequestWithQueryParameter(
      "pokerLaunch ",
      queryParameters,
      UrlConstants.pokerLaunchUrl,
      userId,
    );
    CommonMethods.printLog(
      "pokerLaunch ",
      "pokerLaunch :--> " + result.toString(),
    );
    return result;
  }
}
