import 'package:device_info/device_info.dart';
import 'package:get_version/get_version.dart';
import 'package:package_info/package_info.dart';

import '../Network/network_post_request.dart';
import '../constants/methods/common_methods.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/url_constants.dart';

class LoggingModel {
  static const String TAG = "Logging out";

  static Future<void> logging(
    String action,
    String description,
    String timestamp,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

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

    final DeviceInfoPlugin deviceInfoPlugin = DeviceInfoPlugin();
    AndroidDeviceInfo build = await deviceInfoPlugin.androidInfo;

    Map requestData = {
      "timestamp": timestamp,
      "userId": userId,
      "action": action,
      "description": description,
      "apkVersion": clientVersion,
      "deviceBrand": build.brand,
      "deviceModel": build.model,
      "osVersion": osVer
    };

    result = await NetworkPostRequest.postRequestWithoutAccess(
      requestData,
      UrlConstants.loggingOutUrl,
      userId,
    );
    CommonMethods.printLog(
      TAG,
      result.toString(),
    );
  }
}
