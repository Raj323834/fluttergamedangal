import 'package:clevertap_plugin/clevertap_plugin.dart';

import '../common_models/user_info_dm.dart';
import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../utils/shared_pref_service.dart';
import 'all_games_model.dart';

void storeUserInfo(
  UserInfoDM userInfoDM,
) async {
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.userName,
    userInfoDM.userName,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.firstName,
    userInfoDM.firstName,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.middleName,
    userInfoDM.middleName,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.lastName,
    userInfoDM.lastName,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.avatarId,
    int.parse(
      userInfoDM.avatarId,
    ),
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.avatarURL,
    userInfoDM.avatarUrl,
  );
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.idVerified,
    userInfoDM.idVerified,
  );
  await SharedPrefService.addBoolToSharedPref(
    SharedPrefKeys.addVerified,
    userInfoDM.addressVerified,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.usernameUpdateCount,
    userInfoDM.usernameUpdateCount,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.state,
    userInfoDM.state,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.lrAvatarURL,
    userInfoDM.lrAvatarUrl,
  );
  CleverTapPlugin.profileSet(
    {
      "First Name": userInfoDM.firstName,
      "Username": userInfoDM.userName,
      "KYC Status": userInfoDM.kycVerified,
    },
  );
}

void storeWalletInfo(
    String cashAmount,
    String withdrawAmount,
    String chipsAmount,
    String bonusAmount,
    String gameChipsAmount,
    String pokerChipsAmount) async {
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

void storingMandatoryInfo(
  UserInfoDM userInfoDM,
  bool languageSelected,
  String language,
) async {
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.apkVersion,
    userInfoDM.apkVersion.toString(),
  );
  await SharedPrefService.addStringToSharedPref(
    'apkUrl32Bit',
    userInfoDM.apkUrl32Bit,
  );
  await SharedPrefService.addStringToSharedPref(
    'apkUrl64Bit',
    userInfoDM.apkUrl64Bit,
  );
  await SharedPrefService.addStringToSharedPref(
    'whitelistedApkWithPoker',
    userInfoDM.whitelistedApkWithPoker,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.pokerAvatarUrl,
    userInfoDM.pokerAvatarUrl,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.callBreakPercent,
    userInfoDM.callbreakBonusPercentage,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.ballPoolPercent,
    userInfoDM.poolBonusPercentage,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.quizPercent,
    userInfoDM.quizBonusPercentage,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.fantasyPercent,
    userInfoDM.fantasyBonusPercentage,
  );
  await SharedPrefService.addIntToSharedPref(
    SharedPrefKeys.workLooperPercent,
    userInfoDM.worklooperBonusPercentage,
  );
  await SharedPrefService.addStringToSharedPref(
    'apkName',
    userInfoDM.apkName,
  );

  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.psApkVersion,
    userInfoDM.playStoreApkVersion.toString(),
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.psApkUrl,
    userInfoDM.playStoreApkUrl,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.psApkName,
    userInfoDM.playStoreApkName,
  );
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.psUpdateDetails,
    userInfoDM.playStoreUpdateDetails,
  );

  try {
    await AllGamesModel.storeGamesOrderInSharedPref(
      userInfoDM.gameCategories,
    );
  } catch (e) {
    CommonMethods.printLog(
      "HERE",
      e.toString(),
    );
  }
  await SharedPrefService.addStringToSharedPref(
    SharedPrefKeys.updateDetails,
    userInfoDM.updateDetails,
  );
  if (!languageSelected) {
    try {
      await SharedPrefService.addStringToSharedPref(
        SharedPrefKeys.preferredLanguage,
        userInfoDM.preferredLanguage,
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
