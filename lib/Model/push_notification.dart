import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:overlay_support/overlay_support.dart';

import '../constants/methods/common_methods.dart';

class PushNotificationService {
  final FirebaseMessaging _fcm;

  PushNotificationService(
    this._fcm,
  );

  Future initialise() async {
    String token = await _fcm.getToken();
    CommonMethods.printLog(
      "FirebaseMessaging token: ",
      token,
    );

    await CleverTapPlugin.setPushToken(
      token,
    );

    await CleverTapPlugin.createNotificationChannel(
      'high_importance_channel',
      'high_importance_channel',
      'Creating channel',
      5,
      true,
    );

    // var result = await FlutterNotificationChannel.registerNotificationChannel(
    //   description: 'Creating channel',
    //   id: 'high_importance_channel',
    //   importance: NotificationImportance.IMPORTANCE_HIGH,
    //   name: 'high_importance_channel',
    //   visibility: 1,
    //   allowBubbles: true,
    //   enableVibration: true,
    //   enableSound: true,
    //   showBadge: true,
    // );

    FirebaseMessaging.onMessage.listen(
      (
        RemoteMessage message,
      ) {
        CommonMethods.printLog(
          "onMessage: ",
          message.data.toString(),
        );
        showSimpleNotification(
          Text(
            message.data['data']['nt'],
            style: TextStyle(
              fontWeight: FontWeight.bold,
            ),
          ),
          subtitle: Text(
            message.data['data']['nm'],
          ),
          leading: Image(
            image: AssetImage(
              'assets/images/ic_launcher.png',
            ),
          ),
          slideDismissDirection: DismissDirection.up,
          duration: Duration(
            seconds: 5,
          ),
          background: Color.fromARGB(
            255,
            33,
            47,
            103,
          ),
        );
      },
    );
  }

  static backgroundMessageHandler(
    Map<String, dynamic> message,
  ) {
    CommonMethods.printLog(
      "onBackgroundMessage: ",
      message.toString(),
    );

    CleverTapPlugin.createNotification(
      message.toString(),
    );
  }
}
