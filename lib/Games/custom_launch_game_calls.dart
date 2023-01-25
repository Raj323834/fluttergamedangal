import 'dart:async';
import 'dart:convert' as convert;

import 'package:android_intent/android_intent.dart';
import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:device_apps/device_apps.dart';

import '../constants/methods/common_methods.dart';
import 'all_games_helper_service.dart';

class CustomLaunchGameCalls {
  final Function gameNotInstalledCallback;

  CustomLaunchGameCalls(
    this.gameNotInstalledCallback,
  );
  Future<void> openGame(
    var lobbyJson,
    int index,
    var lobbyInfo,
    String gameName,
  ) async {
    CommonMethods.printLog(
      "",
      "----------Opening ----------" + gameName,
    );
    CommonMethods.printLog(
      "Game open",
      lobbyJson.toString(),
    );
    bool isInstalled = await DeviceApps.isAppInstalled(
      AllGamesHelperService.getGamePackageName(
        gameName,
      ),
    );
    if (isInstalled) {
      if (lobbyInfo[index]['type'] == "CASH") {
        var eventData = {
          "game_name": AllGamesHelperService.getGameNameForLaunch(
            gameName,
          ),
          "game_amount": lobbyJson["entryFee"],
          "game_type": lobbyJson["name"],
        };
        CommonMethods.printLog(
          "CleverTap Cash game Played",
          eventData.toString(),
        );
        CleverTapPlugin.recordEvent(
          "Cash game Played",
          eventData,
        );
      } else {
        CleverTapPlugin.recordEvent(
          "Free game Played",
          {
            "game_name": AllGamesHelperService.getGameNameForLaunch(
              gameName,
            )
          },
        );
      }
      var body = convert.jsonEncode(
        lobbyJson,
      );
      try {
        final AndroidIntent intent = AndroidIntent(
          action: "android.intent.action.MAIN",
          package: AllGamesHelperService.getGamePackageName(
            gameName,
          ),
          componentName: "com.unity3d.player.UnityPlayerActivity",
          arguments: {
            "data": body,
          },
        );
        intent.launch();
      } catch (e) {
        CommonMethods.printLog(
          "",
          "Unable to launch ",
        );
      }
    } else {
      gameNotInstalledCallback(
        gameName,
      );
    }
  }
}
