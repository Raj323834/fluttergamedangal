import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:flutter/material.dart';

import '../../Model/all_games_model.dart';
import '../../utils/shared_pref_service.dart';
import '../shared_pref_keys.dart';
import '../string_constants.dart';
import 'common_methods.dart';

class SharedPrefMethods {
  static void storeUserInfo({
    @required Map<String, dynamic> responseMap,
  }) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.userName,
      responseMap['userName'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.firstName,
      responseMap['firstName'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.middleName,
      responseMap['middleName'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.lastName,
      responseMap['lastName'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.avatarId,
      int.parse(
        responseMap['avatarId'],
      ),
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.avatarURL,
      responseMap['avatarUrl'],
    );
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.idVerified,
      responseMap['idVerified'],
    );
    await SharedPrefService.addBoolToSharedPref(
      SharedPrefKeys.addVerified,
      responseMap['addressVerified'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.usernameUpdateCount,
      responseMap['usernameUpdateCount'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.state,
      responseMap['state'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.lrAvatarURL,
      responseMap['lrAvatarUrl'],
    );
    CleverTapPlugin.profileSet(
      {
        "First Name": responseMap['firstName'],
        "Username": responseMap['userName'],
        "KYC Status": responseMap['kycVerified']
      },
    );
  }

  static void storeWalletInfo({
    @required String cashAmount,
    @required String withdrawAmount,
    @required String chipsAmount,
    @required String bonusAmount,
    @required String gameChipsAmount,
    @required String pokerChipsAmount,
  }) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.cashAmount,
      cashAmount,
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.withdrawAmount,
      withdrawAmount,
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.chipsAmount,
      chipsAmount,
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.bonusAmount,
      bonusAmount,
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.gameChipsAmount,
      gameChipsAmount,
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.pokerChipsAmount,
      pokerChipsAmount,
    );
    CleverTapPlugin.profileSet(
      {
        "Deposit Cash": cashAmount,
        "Game Chips": gameChipsAmount,
        "Bonus Cash": bonusAmount,
        "Withdraw Cash": withdrawAmount,
      },
    );
  }

  static void storeMandatoryInfo({
    @required Map<String, dynamic> responseMap,
    @required bool languageSelected,
    @required String language,
  }) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.apkVersion,
      responseMap['apkVersion'].toString(),
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.apkUrl32Bit,
      responseMap[SharedPrefKeys.apkUrl32Bit],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.apkUrl64Bit,
      responseMap[SharedPrefKeys.apkUrl64Bit],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.whitelistedApkWithPoker,
      responseMap[SharedPrefKeys.whitelistedApkWithPoker],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.pokerAvatarUrl,
      responseMap[SharedPrefKeys.pokerAvatarUrl],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.callBreakPercent,
      responseMap['callbreakBonusPercentage'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.ballPoolPercent,
      responseMap['poolBonusPercentage'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.quizPercent,
      responseMap['quizBonusPercentage'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.fantasyPercent,
      responseMap['fantasyBonusPercentage'],
    );
    await SharedPrefService.addIntToSharedPref(
      SharedPrefKeys.workLooperPercent,
      responseMap['worklooperBonusPercentage'],
    );
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.apkName,
      responseMap[SharedPrefKeys.apkName],
    );

    try {
      await AllGamesModel.storeGamesOrderInSharedPref(
        responseMap['gameCategories'],
      );
    } catch (e) {
      CommonMethods.printLog(
        "HERE",
        e.toString(),
      );
    }
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.updateDetails,
      responseMap['updateDetails'],
    );
    if (!languageSelected) {
      try {
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.preferredLanguage,
          responseMap['preferredLanguage'],
        );
      } catch (e) {
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.preferredLanguage,
          StringConstants.emptyString,
        );
      }
    } else {
      await SharedPrefService.addStringToSharedPref(
        SharedPrefKeys.preferredLanguage,
        language,
      );
    }
  }
}
