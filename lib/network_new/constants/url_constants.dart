import 'package:flutter_flavor/flutter_flavor.dart';

class UrlConstants {
  static const String imageUrl = 'https://d2twq4sdujio2d.cloudfront.net/';

  // LOGIN URLS
  static final String loginWithMobileUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/login/mobile/new";
  static final String verifyLoginUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/login/verify";
  static final String loginWithSocialUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/login/email";
  static final String requestOtpUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/otp/send";

  static final String resendOtpUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/otp/re-send";
  static final String verifyOtpUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/otp/verify";
  static final String getRefreshTokenUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/refresh-token";
  static final String loggingOutUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/logs/client";

//REFER AND EARN
  static final String inviteFriendsEmailUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/invites/email";
  static final String inviteFriendsTextUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/invites/mobile";
  static final String invitesStatusUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/invites/";
  static final String invitesCountUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/invites/count/";
  static final String getReferralCodeUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/referralCode/";

//KYC SERVICE
  static final String documentConfigUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/kyc/documentConfig";
  static final String documentUserUrl = "/v1/kyc/v2";
  static final String uploadDocumentUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/kyc/upload";

//PAYMENT AND PROMOCODE
  static final String promocodeUrl = "/v1/bonuses/available/";
  static final String initiateTransactionPaytmUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/deposits/initiate";
  static final String verifyTransactionPaytmUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/deposits/verify";

//WITHDRAWAL
  static final String lastFullfilledWithdrawalUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/withdrawal/load";
  static final String validateBankAccountUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/withdrawal/account/validate";
  static final String withdrawTransactionUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/withdrawal/initiate";

//WALLET INFO
  static final String walletInfoUrl =
      FlavorConfig.instance.variables["base_url"] +
          '/v1/info/wallets'; // free+cash
  static final String infoUrl =
      FlavorConfig.instance.variables["base_url"] + '/v1/info/users';
  static final String reloadCoinUrl =
      FlavorConfig.instance.variables["base_url"] + '/v1/wallets/free/reload';

//UserProfileScreen
  static final String verifyEmailupsUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/email/verify";
  static final String verifyMobileupsUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/mobile/verify";
  static final String verifyKycipsUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/email/verify";
  static final String updateUserProfileInfoUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/users/profile/info";
  static final String updateUsernameUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/username";
  static final String getUserProfileUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/users/profile/info/";
  static final String updateAvatarIdUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/users/profile/avatar";
  static final String userProfileRequestOtpUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/contact/verify";
//Transaction Url
  static final String getTransactionsUrl = "/v1/transactions/";
//Withdrawal Url
  static final String getWithdrawalListUrl = "/v1/withdrawal/details";
  static final String cancelWithdrawalUrl = "/v1/withdrawal/cancel";
//Deposit Url
  static final String getDepositUrl = "/v1/deposits/details";

//Callbreak
  static final String callBreakLobbyUrl =
      FlavorConfig.instance.variables["callbreakUrl"];
  static final String callBreakDownload =
      FlavorConfig.instance.variables["callbreakDownloadUrl"];
  static final String callBreakInstall =
      FlavorConfig.instance.variables["callbreakApk"];

//Street Racer
  static final String streetRacerLobbyUrl =
      FlavorConfig.instance.variables["streetRacerUrl"];
  static final String streetRacerInstall =
      FlavorConfig.instance.variables["streetRacerApk"];
  static final String streetRacerBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Fruit Split
  static final String fruitSplitLobbyUrl =
      FlavorConfig.instance.variables["fruitSplitUrl"];
  static final String fruitSplitInstall =
      FlavorConfig.instance.variables["fruitSplitApk"];
  static final String fruitSplitBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Sumo Runner
  static final String sumoRunnerLobbyUrl =
      FlavorConfig.instance.variables["sumoRunnerUrl"];
  static final String sumoRunnerInstall =
      FlavorConfig.instance.variables["sumoRunnerApk"];
  static final String sumoRunnerBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Bubble Shooter
  static final String bubbleShooterLobbyUrl =
      FlavorConfig.instance.variables["bubbleShooterUrl"];
  static final String bubbleShooterInstall =
      FlavorConfig.instance.variables["bubbleShooterApk"];
  static final String bubbleShooterBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Knife cut
  static final String knifeCutLobbyUrl =
      FlavorConfig.instance.variables["knifeCutUrl"];
  static final String knifeCutInstall =
      FlavorConfig.instance.variables["knifeCutApk"];
  static final String knifeCutBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Archery
  static final String archeryLobbyUrl =
      FlavorConfig.instance.variables["archeryUrl"];
  static final String archeryInstall =
      FlavorConfig.instance.variables["archeryApk"];
  static final String archeryBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//Carrom
  static final String carromLobbyUrl =
      FlavorConfig.instance.variables["carromUrl"];
  static final String carromInstall =
      FlavorConfig.instance.variables["carromApk"];
  static final String carromBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//CandyCrush
  static final String candyCrushLobbyUrl =
      FlavorConfig.instance.variables["candyCrushUrl"];
  static final String candyCrushInstall =
      FlavorConfig.instance.variables["candyCrushApk"];
  static final String candyCrushBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//8BallPool
  static final String poolLobbyUrl =
      FlavorConfig.instance.variables["ball8Url"];
  static final String poolDownload =
      FlavorConfig.instance.variables["ballPoolDownloadUrl"];
  static final String poolInstall =
      FlavorConfig.instance.variables["ballPoolApk"];

//Bubble Shooter
  static final String ludoLobbyUrl = FlavorConfig.instance.variables["ludoUrl"];
  static final String ludoInstall = FlavorConfig.instance.variables["ludoApk"];
  static final String ludoBalanceUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/workloopers/games/play";

//POKER URL
  static final String pokerLaunchUrl = "/v1/poker/launch/new";

// FANTASY DANGAL
  static final String fantasyLaunchUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/baazi/launch";

//GAMEPLAY URL ARTOON URL
  static final String artoonBalanceURl =
      FlavorConfig.instance.variables["base_url"] + "/v1/artoon/game/play";

//Qunami URL
  static final String qunamiLobby =
      FlavorConfig.instance.variables["base_url"] + "/v1/qunami/game/lobby";
  static final String qunamiLandingUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/qunami/game/landing";
  static final String qunamiGamePlay =
      FlavorConfig.instance.variables["base_url"] + "/v1/qunami/game/play";

//Georestriction
  static final String gameplayAllowedUrl = "/v1/georestrict/game";
  static final String activityAllowedUrl = "/v1/georestrict";

//Settings
  static final String updateSubscriptionUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/subscription-setting";

//Cashfree
  static final String initiateCashfreeTransactionUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/cashfree/transaction/initiate";
  static final String verifyCashfreeTransactionUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/cashfree/transaction/status";

//RazorPay
  static final String initiateRazorpayOrderUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/orders/";
  static final String verifyRazorpayTransactionUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/orders/verify";

//App Launch
  static final String appLaunchUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/launch";
  static final String appUpdateUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/update";

//Leaderboard
  static final String leaderboardActiveUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/leaderboards/active";
  static final String leaderboardRankUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/leaderboards/ranks/";

// Preferred Language
  static final String preferredLanguageUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/users/profile/preferred-language";

// METHOD CHANNEL
  static const String rummyMethodChannel = "com.flutter.rummyDangal";

//Poker Chips
  static final String getPokerTdsUrl =
      FlavorConfig.instance.variables["base_url"] + "/v1/wallets/poker/tds/";
  static final String pokerToDgWalletUrl =
      FlavorConfig.instance.variables["base_url"] +
          "/v1/wallets/poker/transfer/";
}
