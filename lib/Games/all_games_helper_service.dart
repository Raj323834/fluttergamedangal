import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:path_provider/path_provider.dart';

import '../Network/generate_access_token.dart';
import '../Network/network_get_request.dart';
import '../Network/network_post_request.dart';
import '../Network/wallet_service.dart';
import '../common_widgets/custom_add_cash_dialog.dart';
import '../constants/game_names.dart';
import '../constants/game_package_names.dart';
import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/responses_keys.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';

class AllGamesHelperService {
  static checkGameBalance(
    BuildContext context,
    var entryFee,
    Map param,
    int index,
    String game,
    String userId,
  ) async {
    String state = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.state,
        ) ??
        StringConstants.emptyString;
    Map requestBody = {
      "entry_fee": entryFee,
      "state": state,
    };
    Map<String, Object> result =
        await NetworkPostRequest.postRequestWithAccessGameName(
      requestBody,
      getGameBalanceUrl(
        game,
      ),
      game,
      userId,
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "Something went wrong. Try again",
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          return await checkGameBalance(
            context,
            entryFee,
            param,
            index,
            game,
            userId,
          );
        }
      } else {
        switch (data['result']) {
          case ResponsesKeys.SUCCESS:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            return ResponsesKeys.SUCCESS;
            break;

          case ResponsesKeys.INSUFFICIENT_BALANCE:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return CustomAddCashDialog(
                  userId: userId,
                );
              },
            );
            break;

          case ResponsesKeys.DB_ERROR:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              "There was some issue. Try after some time.",
            );
            break;

          case ResponsesKeys.WALLET_SERVICE_NOT_REACHABLE:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              "Unable to fetch Wallet Info",
            );
            break;

          case ResponsesKeys.RESTRICTED_ACTIVITY:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              StringConstants.restrictedActivityMessage,
            );
            break;

          case ResponsesKeys.ACTIVITY_BLOCKED_FOR_USER:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              StringConstants.restrictedActivityMessage,
            );
            break;

          case ResponsesKeys.GAME_BLOCKED_FOR_USER:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              StringConstants.restrictedActivityMessage,
            );
            break;

          default:
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            CommonMethods.showSnackBar(
              context,
              "Problem in launching app",
            );
        }
        return ResponsesKeys.FAILURE;
      }
    }
  }

  static Future<String> findLocalPath() async {
    return (await getExternalStorageDirectory()).path.toString();
  }

  static allGamesAppInstall(
    String game,
  ) async {
    const platform = MethodChannel(
      'com.flutter.install',
    );
    await platform.invokeMethod(
      "install",
      {
        "fileName": getGameInstallApk(
          game,
        ),
        "packageName": FlavorConfig.instance.variables["packageName"]
      },
    );
  }

  static getGameLobbyApi(
    context,
    bool installed,
    String gameName,
    String userId,
  ) async {
    Map<String, Object> result =
        await NetworkGetRequest.getRequestWithoutQueryParameter(
      gameName + ' lobby',
      getGameLobbyUrl(
        gameName,
      ),
      userId,
    );
    if (result.containsKey(ResponsesKeys.NO_INTERNET)) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
    } else if (result.containsKey('error')) {
      CommonMethods.showSnackBar(
        context,
        'Server Error!',
      );
      Navigator.pop(context);
    } else {
      Map<String, dynamic> responseMap = result['data'];
      if (responseMap.containsKey(ResponsesKeys.ERROR)) {
        CommonMethods.showSnackBar(
          context,
          "Server Error!",
        );
        Navigator.pop(context);
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          return await getGameLobbyApi(
            context,
            installed,
            gameName,
            userId,
          );
        }
      } else if (responseMap.containsKey('result') &&
          responseMap['result'] == ResponsesKeys.SUCCESS) {
        return responseMap;
      } else {
        CommonMethods.showSnackBar(
          context,
          "Server Error!",
        );
        Navigator.pop(context);
      }
      return responseMap;
    }
  }

  static Future<void> reloadCoins(
    BuildContext context,
    String userId,
  ) async {
    Map<String, Object> result = await WalletService.reloadCoins(
      context,
      userId,
    );
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.showSnackBar(
        context,
        "Technical Issue. Unable to reload coins",
      );
    } else {
      Map<String, dynamic> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await reloadCoins(
            context,
            userId,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.showSnackBar(
          context,
          "Coins have been added successfully",
        );
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DOES_NOT_EXIST) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      }
    }
  }

  static String getGameNameForLaunch(
    String gameName,
  ) {
    switch (gameName) {
      case GameNames.archery:
        return "archery";
        break;

      case GameNames.candyCrush:
        return "Candy Crush";
        break;

      case GameNames.carrom:
        return "carrom";
        break;

      case GameNames.bubbleShooter:
        return "Bubble Shooter";
        break;

      case GameNames.fruitSplit:
        return "Fruit Split";
        break;

      case GameNames.knifeCut:
        return "Knife Cut";
        break;

      case GameNames.streetRacer:
        return "Street Racer";
        break;

      case GameNames.runnerNumberOne:
        return "runner_number_one";
        break;

      case GameNames.ludo:
        return "Ludo";
        break;
    }
    return StringConstants.emptyString;
  }

  static String getGamePackageName(
    String gameName,
  ) {
    switch (gameName) {
      case GameNames.archery:
        return GamePackageNames.archery;
        break;

      case GameNames.candyCrush:
        return GamePackageNames.candyClash;
        break;

      case GameNames.carrom:
        return GamePackageNames.carrom;
        break;

      case GameNames.bubbleShooter:
        return GamePackageNames.bubbleShooter;
        break;

      case GameNames.fruitSplit:
        return GamePackageNames.fruitSplit;
        break;

      case GameNames.knifeCut:
        return GamePackageNames.knifeCut;
        break;

      case GameNames.streetRacer:
        return GamePackageNames.streetRacer;
        break;

      case GameNames.runnerNumberOne:
        return GamePackageNames.runnerNumberOne;
        break;

      case GameNames.ludo:
        return GamePackageNames.ludo;
        break;
    }
    return StringConstants.emptyString;
  }

  static String getGameLobbyUrl(
    String game,
  ) {
    switch (game) {
      case GameNames.archery:
        return UrlConstants.archeryLobbyUrl;
        break;

      case GameNames.candyCrush:
        return UrlConstants.candyCrushLobbyUrl;
        break;

      case GameNames.carrom:
        return UrlConstants.carromLobbyUrl;
        break;

      case GameNames.bubbleShooter:
        return UrlConstants.bubbleShooterLobbyUrl;
        break;

      case GameNames.fruitSplit:
        return UrlConstants.fruitSplitLobbyUrl;
        break;

      case GameNames.knifeCut:
        return UrlConstants.knifeCutLobbyUrl;
        break;

      case GameNames.streetRacer:
        return UrlConstants.streetRacerLobbyUrl;
        break;

      case GameNames.runnerNumberOne:
        return UrlConstants.sumoRunnerLobbyUrl;
        break;

      case GameNames.ludo:
        return UrlConstants.ludoLobbyUrl;
        break;
    }
    return StringConstants.emptyString;
  }

  static String getGameBalanceUrl(
    String game,
  ) {
    switch (game) {
      case GameNames.archery:
        return UrlConstants.archeryBalanceUrl;
        break;

      case GameNames.carrom:
        return UrlConstants.carromBalanceUrl;
        break;

      case GameNames.candyCrush:
        return UrlConstants.candyCrushBalanceUrl;
        break;

      case GameNames.bubbleShooter:
        return UrlConstants.bubbleShooterBalanceUrl;
        break;

      case GameNames.fruitSplit:
        return UrlConstants.fruitSplitBalanceUrl;
        break;

      case GameNames.knifeCut:
        return UrlConstants.knifeCutBalanceUrl;
        break;

      case GameNames.streetRacer:
        return UrlConstants.streetRacerBalanceUrl;
        break;

      case GameNames.runnerNumberOne:
        return UrlConstants.sumoRunnerBalanceUrl;
        break;

      case GameNames.ludo:
        return UrlConstants.ludoBalanceUrl;
        break;
    }
    return StringConstants.emptyString;
  }

  static String getGameInstallApk(
    String game,
  ) {
    switch (game) {
      case GameNames.archery:
        return UrlConstants.archeryInstall;
        break;

      case GameNames.carrom:
        return UrlConstants.carromInstall;
        break;

      case GameNames.candyCrush:
        return UrlConstants.candyCrushInstall;
        break;

      case GameNames.bubbleShooter:
        return UrlConstants.bubbleShooterInstall;
        break;

      case GameNames.fruitSplit:
        return UrlConstants.fruitSplitInstall;
        break;

      case GameNames.knifeCut:
        return UrlConstants.knifeCutInstall;
        break;

      case GameNames.streetRacer:
        return UrlConstants.streetRacerInstall;
        break;

      case GameNames.runnerNumberOne:
        return UrlConstants.sumoRunnerInstall;
        break;

      case GameNames.ludo:
        return UrlConstants.ludoInstall;
        break;
    }
    return StringConstants.emptyString;
  }

  static String getGameNameToDisplay(
    String gameName,
  ) {
    switch (gameName) {
      case GameNames.archery:
        return "Archery";
        break;

      case GameNames.candyCrush:
        return "Candy Crush";
        break;

      case GameNames.carrom:
        return "Carrom";
        break;

      case GameNames.bubbleShooter:
        return "Bubble Shooter";
        break;

      case GameNames.fruitSplit:
        return "Fruit Split";
        break;

      case GameNames.knifeCut:
        return "Knife Cut";
        break;

      case GameNames.streetRacer:
        return "Street Racer";
        break;

      case GameNames.runnerNumberOne:
        return "Sumo Runner";
        break;

      case GameNames.ludo:
        return "Ludo";
        break;
    }
    return StringConstants.emptyString;
  }
}
