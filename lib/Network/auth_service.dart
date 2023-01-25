import 'package:advertising_identifier/advertising_identifier.dart';
import 'package:connectivity/connectivity.dart';
import 'package:device_info/device_info.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:get_version/get_version.dart';
import 'package:package_info/package_info.dart';

import '../Model/logging_model.dart';
import '../Model/navigation_service.dart';
import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import '../utils/singleton.dart';
import 'network_post_request.dart';
import 'web_socket_helper_service.dart';

class AuthService {
  static const String TAG = "Auth Service  ";

  static Future<Map<String, Object>> signInWithSocialLogin(
      String socialLogin,
      String socialLoginMethod,
      var _user,
      String width,
      String height,
      String referralCode,
      String language,
      bool languageSelected,
      String userId) async {
    Map<String, Object> result = Map();
    CommonMethods.printLog(
      TAG,
      "       --------- SIGN IN WITH SOCIAL LOGIN --------     ",
    );

    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

    var connectivityResult = await Connectivity().checkConnectivity();

    final DeviceInfoPlugin deviceInfoPlugin = DeviceInfoPlugin();
    AndroidDeviceInfo build = await deviceInfoPlugin.androidInfo;

    String deviceId = StringConstants.emptyString;
    String cpuType = StringConstants.emptyString;

    String advertisingId = StringConstants.emptyString;

    try {
      final AdvertisingIdInfo info =
          await AdvertisingIdManager.getAdvertisingIdInfo();
      advertisingId = info.id;
    } catch (e) {
      advertisingId = StringConstants.emptyString;
    }

    String osVer = StringConstants.emptyString;
    try {
      osVer = await GetVersion.platformVersion;
      if (osVer.contains("Android ")) {
        osVer = osVer.split("Android ")[1];
      } else if (osVer.contains("Android")) {
        osVer = osVer.split("Android")[1];
      }
    } catch (e) {
      osVer = StringConstants.emptyString;
    }

    Map requestData = {
      "email": _user.email,
      "social_token": socialLogin,
      "network": "GOOGLE",
      "platform": "APK",
      "ad_source": StringConstants.emptyString,
      "ad_key": StringConstants.emptyString,
      "clientVersion": clientVersion,
      "code": referralCode,
      "additionalInformation": {
        "country_code": "IN",
        "language": languageSelected ? language : null,
        "package_name": FlavorConfig.instance.variables["packageName"],
        "connection_type":
            connectivityResult == ConnectivityResult.mobile ? "mobile" : "wifi",
        "isV8": FlavorInfo.isV8,
        "device_info": {
          "id": deviceId,
          "os": "android",
          "os_version": osVer,
          "brand": build.brand,
          "model": build.model,
          "android_id": build.androidId,
          "advertising_id": advertisingId,
          "cpu_type": cpuType,
          "width": width,
          "height": height,
          "build": build.id
        }
      }
    };

    switch (socialLoginMethod) {
      case 'GOOGLE':
        {
          result = await NetworkPostRequest.postRequestWithoutAccess(
            requestData,
            UrlConstants.loginWithSocialUrl,
            userId,
          );

          break;
        }
    }

    return result;
  }

  static Future<Map<String, Object>> signInWithMobileOTP(
    String mobileNumber,
    String refercode,
    width,
    height,
    String requestId,
    String otp,
    String language,
    bool languageSelected,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    CommonMethods.printLog(
      TAG,
      "       ---------SIGN IN WITH MOBILE OTP--------     ",
    );

    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

    var connectivityResult = await Connectivity().checkConnectivity();

    final DeviceInfoPlugin deviceInfoPlugin = DeviceInfoPlugin();
    AndroidDeviceInfo build = await deviceInfoPlugin.androidInfo;

    String deviceId = StringConstants.emptyString;
    String cpuType = StringConstants.emptyString;

    String advertisingId = StringConstants.emptyString;
    try {
      final AdvertisingIdInfo info =
          await AdvertisingIdManager.getAdvertisingIdInfo();
      advertisingId = info.id;
    } catch (e) {
      advertisingId = StringConstants.emptyString;
    }

    String osVer = StringConstants.emptyString;
    try {
      osVer = await GetVersion.platformVersion;
      if (osVer.contains("Android")) {
        osVer = osVer.split("Android ")[1];
      } else if (osVer.contains("Android")) {
        osVer = osVer.split("Android")[1];
      }
    } catch (e) {
      osVer = StringConstants.emptyString;
    }

    Map requestData = {
      "mobile_number": mobileNumber,
      "platform": "APK",
      "ad_source": StringConstants.emptyString,
      "ad_key": StringConstants.emptyString,
      "client_version": clientVersion,
      "code": refercode,
      "request_id": requestId,
      "otp": otp,
      "additionalInformation": {
        "country_code": "IN",
        "language": languageSelected ? language : null,
        "package_name": FlavorConfig.instance.variables["packageName"],
        "connection_type":
            connectivityResult == ConnectivityResult.mobile ? "mobile" : "wifi",
        "isV8": FlavorInfo.isV8,
        "device_info": {
          "id": deviceId,
          "os": "android",
          "os_version": osVer,
          "brand": build.brand,
          "model": build.model,
          "android_id": build.androidId,
          "advertising_id": advertisingId,
          "cpu_type": cpuType,
          "width": width,
          "height": height,
          "build": build.id
        }
      }
    };

    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.loginWithMobileUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> verifyLogin(
    String address,
    bool isMobile,
    String userId,
  ) async {
    Map<String, Object> result = Map();
    CommonMethods.printLog(
      TAG,
      "       ---------verifyLogin------     ",
    );

    Map requestData = {
      "address": address,
      "is_mobile": isMobile,
    };

    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.verifyLoginUrl,
      userId,
    );

    return result;
  }

  static Future<Map<String, Object>> requestOTPMobile(
    String mobileNumber,
    String template,
    String hashString,
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "       --------- REQUEST OTP MOBILE --------     ",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      "source": "Client",
      "channel": "SMS",
      "to_address": mobileNumber,
      "template": template,
      "hash_string": hashString
    };
    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.requestOtpUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, Object>> requestResendOTPMobile(
      String mobileNumber,
      String requestId,
      String template,
      String hashString,
      String userId) async {
    CommonMethods.printLog(
      TAG,
      "       ---------RESENDOTP MOBILE --------     ",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      "source": "Client",
      "channel": "SMS",
      "to_address": mobileNumber,
      "template": template,
      "request_id": requestId,
      "hash_string": hashString
    };
    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.resendOtpUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, Object>> requestResendOTPEmail(
    String email,
    String requestId,
    String username,
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "       --------- RESEND OTP EMAIL --------     ",
    );
    Map<String, Object> result = Map();

    Map requestData = {
      "source": "Client",
      "username": username,
      "channel": "EMAIL",
      "to_address": email,
      "template": "VERIFY_EMAIL",
      "request_id": requestId,
    };
    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.resendOtpUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, Object>> requestUSerPRofileOTPEmail(
    String newEmail,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    Map requestData = {
      "contact_type": "EMAIL",
      "address": newEmail,
    };
    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.userProfileRequestOtpUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, Object>> requestUSerPRofileOTPMobile(
    String mobileNumber,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    Map requestData = {
      "contact_type": "MOBILE",
      "address": mobileNumber,
    };
    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.userProfileRequestOtpUrl,
      userId,
    );
    return result;
  }

  static Future<Map<String, Object>> verifyOTP(
    String requestId,
    String otp,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    Map requestData = {
      "request_id": requestId,
      "otp": otp,
    };

    result = await NetworkPostRequest.postRequestWithAccess(
      requestData,
      UrlConstants.verifyOtpUrl,
      userId,
    );
    return result;
  }

  static void loggingOutUser() async {
    Singleton().listOfBanners = [];
    Singleton().listOfDealsBanners = [];
    String userId = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userID,
    );
    await SharedPrefService.clear();
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.languageSelected,
      true,
    );
    NavigationService.instance.navigateToReplacement(
      '/LoginScreens',
    );
    try {
      await sockets.reset();
    } catch (e) {
      LoggingModel.logging(
        "Socket exception Logout",
        "Socket exception : $e",
        DateTime.now().toString(),
        userId,
      );
    }

    await LoggingModel.logging(
      "LOGOUT",
      "Logout from sm-logout",
      DateTime.now().toString(),
      userId,
    );

    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.languageSelected,
      true,
    );
  }
}
