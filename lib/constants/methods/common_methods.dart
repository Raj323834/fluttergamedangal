import 'dart:developer';

import 'package:device_apps/device_apps.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:package_info/package_info.dart';

import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/custom_cupertino_picker.dart';
import '../color_constants.dart';
import '../locale_constants.dart';

class CommonMethods {
  static printLog(
    String tag,
    String msg,
  ) {
    bool showLogs = FlavorConfig.instance.variables["showlogs"];
    if (showLogs) {
      RegExp('.{1,800}')
          .allMatches(
            tag + "****:" + msg,
          )
          .forEach((match) => print(match.group(0)));
    }
  }

  static devLog(String message) {
    bool showLogs = FlavorConfig.instance.variables["showlogs"];
    if (showLogs) {
      log(message);
    }
  }

  static changeLanguage({
    @required BuildContext context,
    @required String language,
  }) {
    if (language == 'English') {
      context.setLocale(
        LocaleConstants.english,
      );
    } else if (language == 'Bengali') {
      context.setLocale(
        LocaleConstants.bengali,
      );
    } else if (language == 'Gujrati') {
      context.setLocale(
        LocaleConstants.gujarati,
      );
    } else if (language == 'Hindi') {
      context.setLocale(
        LocaleConstants.hindi,
      );
    } else if (language == 'Marathi') {
      context.setLocale(
        LocaleConstants.marathi,
      );
    } else if (language == 'Tamil') {
      context.setLocale(
        LocaleConstants.tamil,
      );
    } else {
      printLog(
        "Language Change",
        "This language does not exists",
      );
    }
  }

  static showSnackBar(
    BuildContext context,
    String msg,
  ) {
    EasyLoading.instance..backgroundColor = ColorConstants.black;
    EasyLoading.showToast(
      msg,
    );
  }

  static getInviteText({
    @required String downloadURL,
    @required String referralCode,
  }) {
    return "Hey, Play with me on Dangal Games. You can play many exciting games 🤩! Install the app from here: $downloadURL$referralCode, use my referral code $referralCode to get Rs. 100 bonus. Start playing and winning on India ka Playground";
  }

  static showCustomDialog({
    @required BuildContext context,
    @required String title,
    @required String error,
    bool barrierDismissible = false,
    Widget child,
  }) {
    return showDialog(
      context: context,
      barrierDismissible: barrierDismissible,
      builder: (_) =>
          child ??
          BaseErrorDialog(
            title: title,
            error: error,
          ),
    );
  }

  static Future showCustomPickerBottomSheet({
    @required BuildContext context,
    @required String title,
    @required String initialItem,
    @required List<String> list,
    @required VoidCallback onTap,
    @required ValueChanged onSelectedItemChanged,
    bool enableDrag = false,
    bool isDismissible = false,
  }) async {
    return showModalBottomSheet(
      context: context,
      enableDrag: enableDrag,
      isDismissible: isDismissible,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(
            11,
          ),
          topRight: Radius.circular(
            11,
          ),
        ),
      ),
      builder: (_) {
        return CustomCupertinoPicker(
          onTap: onTap,
          onSelectedItemChanged: onSelectedItemChanged,
          title: title,
          initialItem: initialItem,
          list: list,
        );
      },
    );
  }

  static Future<String> getHtmlFromAsset({@required String path}) async {
    return await rootBundle.loadString(path);
  }

  static Future<String> packageName() async {
    PackageInfo pI = await PackageInfo.fromPlatform();
    return pI.packageName;
  }

  static Future<bool> checkIfPackageIsInstalled(String packageName) async {
    bool isInstalled = await DeviceApps.isAppInstalled(packageName);
    return isInstalled;
  }
}
