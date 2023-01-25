import 'package:firebase_analytics/firebase_analytics.dart';
import 'package:flutter/material.dart';

const String GAME_CLICK_EVENT_FIREBASE = "game_click";
const String REGISTRATION_EVENT_FIREBASE = "sign_up";
const String All_GAMES_ROUTE = "AllGames";
const String DEALS_ROUTE = "Deals";
const String ADD_CASH_ROUTE = "AddCash";
const String RAF_ROUTE = "Refer&Earn";
const String SUPPORT_ROUTE = "Support";
const String CONTACTS_ROUTE = "ContactsPage";
const String PROFILE_ROUTE = "UserProfile";
const String RESPONSIBLE_GAMING_ROUTE = "ResponsibleGaming";
const String TNC_ROUTE = "Terms&Conditions";
const String AVATAR_ROUTE = "Avatar";
const String WITHDRAWAL_HISTORY_ROUTE = "WithdrawalHistory";
const String WITHDRAWAL_DESC_ROUTE = "WithdrawalDescription";
const String DEPOSIT_HISTORY_ROUTE = "DepositHistory";
const String DEPOSIT_DESC_ROUTE = "DepositDescription";
const String TRANSACTION_HISTORY_ROUTE = "TransactionHistory";
const String TRANSACTION_DESC_ROUTE = "TransactionDescription";
const String KYC_ROUTE = "Kyc";
const String WALLET_ROUTE = "Wallet";
const String WITHDRAW_CASH_ROUTE = "WithdrawCash";
const String CALLBREAK_ROUTE = "Callbreak";
const String STREET_RACER_ROUTE = "StreetRacer";
const String FRUIT_SPLIT_ROUTE = "FruitSplit";
const String POOL_ROUTE = "8BallPool";
const String RUNNER_ROUTE = "SumoRunner";
const String BUBBLE_SHOOTER_ROUTE = "BubbleShooter";
const String KNIFE_CUT_ROUTE = "KnifeHit";
const String ARCHERY_ROUTE = "Archery";
const String CARROM_ROUTE = "Carrom";
const String CANDY_CLASH_ROUTE = "CandyClash";
const String LUDO_ROUTE = "Ludo";

class FirebaseAnalyticsModel {
  static FirebaseAnalytics analytics = FirebaseAnalytics.instance;

  static Future<void> analyticsSetUserId({
    @required String userId,
  }) async {
    await analytics.setUserId(
      id: userId,
    );
  }

  static Future<void> analyticsLogEvent({
    @required String eventName,
    Map<String, dynamic> parameters,
  }) async {
    await analytics.logEvent(
      name: eventName,
      parameters: parameters,
    );
  }

  static Future<void> analyticsScreenTracking({
    @required String screenName,
  }) async {
    await analytics.setCurrentScreen(
      screenName: screenName,
    );
  }
}
