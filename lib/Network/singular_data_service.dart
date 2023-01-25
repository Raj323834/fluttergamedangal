import 'package:package_info/package_info.dart';
import 'package:singular_flutter_sdk/singular.dart';
import 'package:singular_flutter_sdk/singular_config.dart';
import 'package:singular_flutter_sdk/singular_link_params.dart';

import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import 'network_post_request.dart';

class SingularDataService {
  static Future singularStartup() async {
    String userId = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.userID,
        ) ??
        StringConstants.emptyString;
    SingularConfig config = SingularConfig(
      'dangal_games_af1b5efa',
      '3f6e40491a26c4578474cc5d8cf66cce',
    );
    // config.enableLogging=true;
    config.customUserId = userId;
    config.singularLinksHandler = (
      SingularLinkParams params,
    ) async {
      String deeplink = params.deeplink;

      CommonMethods.printLog(
        "DEFERRED DEEPLINK",
        deeplink,
      );

      if (deeplink.contains(
        "refer-code",
      )) {
        String code = deeplink.split('refer-code/')[1];
        CommonMethods.printLog(
          "DEEPLINK REFERRAL CODE",
          code,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.referralCode,
          code,
        );
      } else if (deeplink.contains(
        "sign-up",
      )) {
        String code = deeplink.split('sign-up/')[1];
        CommonMethods.printLog(
          "DEEPLINK SIGN UP CODE",
          code,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.signUpCode,
          code,
        );
      } else if (deeplink.contains(
        "fantasy-dangal",
      )) {
        String code = deeplink.split('fantasy-dangal/')[1];
        CommonMethods.printLog(
          "DEEPLINK FANTASY CODE",
          code,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.leagueCode,
          code,
        );
      } else {
        CommonMethods.printLog(
          "DEEPLINK ELSE",
          "HERE",
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.referralCode,
          StringConstants.emptyString,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.signUpCode,
          StringConstants.emptyString,
        );
      }
    };
    Singular.setGlobalProperty(
      "domain",
      FlavorInfo.isCoIn
          ? StringConstants.websiteCoIn
          : FlavorInfo.isPS
              ? StringConstants.websiteIn
              : StringConstants.websiteCom,
      true,
    );
    Singular.start(config);
  }

  static Future appLaunch() async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "App Launch Event",
    );
    String userId = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.userID,
        ) ??
        StringConstants.emptyString;
    if (userId == StringConstants.emptyString) {
      return;
    }

    PackageInfo packageInfo = await PackageInfo.fromPlatform();

    Map requestData = {
      "SharedPrefKeys.userID": userId,
      "app_version": packageInfo.version
    };

    await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.appLaunchUrl,
      userId,
    );
  }

  static Future appRegister(
    String width,
    String height,
    String from,
    String referralCode,
    String userId,
  ) async {
    DateTime date = DateTime.now();

    Map<dynamic, dynamic> args = {};

    if (referralCode == StringConstants.emptyString || referralCode == null) {
      args = {
        "userId": userId,
        "regDate": date.day,
        "regMonth": date.month,
        "regYear": date.year,
        "regVia": from,
        "language": "EN"
      };
    } else {
      args = {
        "userId": userId,
        "regDate": date.day,
        "regMonth": date.month,
        "regYear": date.year,
        "regVia": from,
        "referralCode": referralCode,
        "language": "EN"
      };
    }
    Singular.setCustomUserId(
      userId,
    );
    try {
      Singular.eventWithArgs(
        "App Register",
        args,
      );
    } catch (e) {
      CommonMethods.printLog(
        "SINGULAR EXCEPTION",
        e.toString(),
      );
    }
  }

  static Future appUpdate(
    String width,
    String height,
    String userId,
  ) async {
    bool panVerified = await SharedPrefService.getBoolValuesFromSharedPref(
      SharedPrefKeys.idVerified,
    );
    bool addVerified = await SharedPrefService.getBoolValuesFromSharedPref(
      SharedPrefKeys.addVerified,
    );

    Map<dynamic, dynamic> args = {
      "userId": userId,
      "addressVerified": addVerified.toString(),
      "panVerified": panVerified.toString(),
      "language": "EN",
    };
    Singular.eventWithArgs(
      "App Update",
      args,
    );
  }
}
