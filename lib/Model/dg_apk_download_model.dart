import 'dart:async';
import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/services.dart';

import '../constants/methods/common_methods.dart';

final appUpdateSC = StreamController.broadcast();

class DgApkDownloadModel {
  static const String TAG = "DgApkDownloadModel  ";

  static bool isWebViewOpened = false;

  static checkUpdate() {
    if (DgApkDownloadModel.isWebViewOpened) {
      appUpdateSC.add(true);
      DgApkDownloadModel.isWebViewOpened = false;
      CommonMethods.printLog(
        TAG,
        "checkUpdate inside",
      );
    }
  }

  static Future<bool> downloadDgApk({
    String downloadUrl,
    String apkName,
  }) async {
    Dio dio = Dio();
    const platformDir = MethodChannel(
      'com.flutter.downloadDir',
    );
    String downloadDirectory = await platformDir.invokeMethod(
      "downloadDir",
    );

    try {
      CommonMethods.printLog(
        TAG,
        "downloadUrl $downloadUrl",
      );
      CommonMethods.printLog(
        TAG,
        "path $downloadDirectory/$apkName",
      );
      await dio.download(
        downloadUrl,
        "$downloadDirectory/$apkName",
        onReceiveProgress: (
          rec,
          total,
        ) {},
      );
    } catch (e) {
      log(e);
      return false;
    }

    return true;
  }
}
