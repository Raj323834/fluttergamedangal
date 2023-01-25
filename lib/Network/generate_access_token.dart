import '../Model/logging_model.dart';
import '../Model/navigation_service.dart';
import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../network_new/constants/responses_keys.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import 'network_post_request.dart';
import 'web_socket_helper_service.dart';

class GenerateAccessToken {
  static const String TAG = "GenerateAccessToken Service  ";

  static Future<bool> regenerateAccessToken(
    String userId,
  ) async {
    Map<String, Object> result = Map();

    final refreshToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.refreshToken,
    );

    Map requestData = {
      "refresh_token": refreshToken,
    };

    result = await NetworkPostRequest.postRequestHttpClientWithAccessNoTimeout(
      requestData,
      UrlConstants.getRefreshTokenUrl,
      userId,
    );
    CommonMethods.printLog(
      TAG,
      'Response: ' + result.toString(),
    );
    if (result.containsKey('error')) {
      logout(
        "Error",
        result.toString(),
        userId,
      );
      return false;
    } else {
      Map<String, Object> responseMap = result['data'];
      if (responseMap['result'] == ResponsesKeys.INVALID_REFRESH_TOKEN) {
        logout(
          ResponsesKeys.INVALID_REFRESH_TOKEN,
          responseMap.toString(),
          userId,
        );
        return false;
      } else if (responseMap['result'] == ResponsesKeys.REFRESH_TOKEN_EXPIRED) {
        logout(
          ResponsesKeys.REFRESH_TOKEN_EXPIRED,
          responseMap.toString(),
          userId,
        );
        return false;
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        await SharedPrefService.storeInSharedPref(
          SharedPrefKeys.accessToken,
          responseMap["accessToken"],
        );
        await SharedPrefService.storeInSharedPref(
          SharedPrefKeys.refreshToken,
          responseMap['refreshToken'],
        );
        return true;
      } else if (responseMap['result'] ==
          ResponsesKeys.REFRESH_TOKEN_NOT_FOUND) {
        logout(
          ResponsesKeys.REFRESH_TOKEN_NOT_FOUND,
          responseMap.toString(),
          userId,
        );
        return false;
      } else {
        logout(
          "NOT_SUCCESS",
          responseMap.toString(),
          userId,
        );
        return false;
      }
    }
  }

  static Future<void> logout(
    reason,
    responseMap,
    userId,
  ) async {
    String timestamp = DateTime.now().toString();
    await LoggingModel.logging(
        reason,
        "Generate Access Token API: " + responseMap.toString(),
        timestamp,
        userId);
    await SharedPrefService.clear();
    await SharedPrefService.addBoolToSharedPref(
      'LANGUAGE_SELECTED',
      true,
    );
    await sockets.reset();
    await NavigationService.instance.navigateToReplacement(
      '/LoginScreens',
    );
  }
}
