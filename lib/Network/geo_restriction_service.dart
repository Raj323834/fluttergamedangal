import 'package:connectivity/connectivity.dart';
import 'package:flutter/material.dart';

import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/responses_keys.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import 'network_get_request.dart';
import 'generate_access_token.dart';

class GeorestrictionService {
  static gameplayAllowed(
    BuildContext context,
    String game,
    String userId,
  ) async {
    CommonMethods.printLog(
      "TESTINF",
      "gameplayAllowed------------ > Geo restriction",
    );
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      return false;
    }

    String state = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.state,
    );
    final requestData = {
      "game": game,
      "activity_type": "GAME_PLAY_CASH",
      "profile_state": state
    };
    result = await NetworkGetRequest.georesGetRequestWithQueryParameter(
      "GeoRestriction Gameplay Allowed",
      requestData,
      UrlConstants.gameplayAllowedUrl,
      userId,
    );
    CommonMethods.printLog(
      "gamePlayAllowed",
      'Response: ' + result.toString(),
    );
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      return false;
    } else if (result.containsKey('error')) {
      return true;
    } else {
      Map<String, Object> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        return true;
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          return await gameplayAllowed(
            context,
            game,
            userId,
          );
        }
      } else if (responseMap.containsKey('result') &&
          responseMap['result'] == ResponsesKeys.SUCCESS) {
        return true;
      } else if (responseMap.containsKey('result') &&
          responseMap['result'] == ResponsesKeys.RESTRICTED_ACTIVITY) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
        return false;
      } else if (responseMap['result'] ==
          ResponsesKeys.ACTIVITY_BLOCKED_FOR_USER) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
        return false;
      } else if (responseMap['result'] == ResponsesKeys.GAME_BLOCKED_FOR_USER) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
        return false;
      } else {
        return true;
      }
    }
  }

  static activityAllowed(
    BuildContext context,
    String activity,
    String userId,
  ) async {
    CommonMethods.printLog(
      "TESTINF",
      "activityAllowed------------ > Geo restriction",
    );
    Map<String, Object> result = Map();
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      return false;
    }

    String state = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.state,
    );
    final requestData = {
      "activity_type": activity,
      "profile_state": state,
    };
    result = await NetworkGetRequest.georesGetRequestWithoutAccessToken(
      "GeoRestriction Activity Allowed",
      requestData,
      UrlConstants.activityAllowedUrl,
      userId,
    );
    CommonMethods.printLog(
      "activityAllowed",
      'Response: ' + result.toString(),
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      return false;
    } else if (result.containsKey('error')) {
      return true;
    } else {
      Map<String, Object> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        return true;
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          return await activityAllowed(
            context,
            activity,
            userId,
          );
        }
      } else if (responseMap.containsKey('result') &&
          responseMap['result'] == ResponsesKeys.SUCCESS) {
        return true;
      } else if (responseMap.containsKey('result') &&
          responseMap['result'] == ResponsesKeys.RESTRICTED_ACTIVITY) {
        if (activity == "WITHDRAWAL") {
          CommonMethods.showSnackBar(
            context,
            StringConstants.restrictedActivityMessage,
          );
        } else if (activity == "ADD_CASH") {
          CommonMethods.showSnackBar(
            context,
            StringConstants.restrictedActivityMessage,
          );
        }
        return false;
      } else if (responseMap['result'] ==
          ResponsesKeys.ACTIVITY_BLOCKED_FOR_USER) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
        return false;
      } else if (responseMap['result'] == ResponsesKeys.GAME_BLOCKED_FOR_USER) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
        return false;
      } else {
        return true;
      }
    }
  }
}
