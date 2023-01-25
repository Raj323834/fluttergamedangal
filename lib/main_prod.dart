import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_downloader/flutter_downloader.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_flavor/flutter_flavor.dart';

import 'constants/asset_paths.dart';
import 'constants/environment.dart';
import 'constants/locale_constants.dart';
import 'constants/string_constants.dart';
import 'my_app.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await EasyLocalization.ensureInitialized();
  await FlutterDownloader.initialize(
    debug: false,
  );
  SystemChrome.setPreferredOrientations(
    [
      DeviceOrientation.portraitUp,
    ],
  );
  configLoading();
  FirebaseMessaging.onBackgroundMessage(
    _firebaseMessagingBackgroundHandler,
  );
  FlavorConfig(
    name: StringConstants.envProd,
    color: Colors.red,
    location: BannerLocation.topEnd,
    variables: Environment.prodEnvVariables,
  );

  runApp(
    EasyLocalization(
      child: MyApp(),
      supportedLocales: LocaleConstants.localeList,
      path: AssetPaths.translations,
      fallbackLocale: LocaleConstants.english,
    ),
  );
}

void configLoading() {
  EasyLoading.instance
    ..displayDuration = const Duration(
      milliseconds: 2000,
    )
    ..indicatorType = EasyLoadingIndicatorType.fadingCircle
    ..loadingStyle = EasyLoadingStyle.custom
    ..indicatorSize = 45.0
    ..radius = 10.0
    ..fontSize = 12
    ..progressColor = Colors.white
    ..backgroundColor = Colors.black.withOpacity(
      0.7,
    )
    ..indicatorColor = Colors.white
    ..textColor = Colors.white
    ..maskColor = Colors.blue.withOpacity(
      0.5,
    )
    ..userInteractions = true
    ..toastPosition = EasyLoadingToastPosition.bottom
    ..boxShadow = []
    ..dismissOnTap = false;
}

Future<void> _firebaseMessagingBackgroundHandler(
  RemoteMessage message,
) async {
  await Firebase.initializeApp();
  CleverTapPlugin.createNotification(
    message.data.toString(),
  );
}
