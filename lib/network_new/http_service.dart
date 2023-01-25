import 'package:flutter/material.dart';
import 'package:package_info/package_info.dart';

import 'api_helper.dart';
import 'constants/parameter_constants.dart';

class HttpServices {
  static final HttpServices _httpServices = HttpServices._internal();
  final ApiBaseHelper _apiBaseHelper = ApiBaseHelper();

  factory HttpServices() {
    return _httpServices;
  }

  HttpServices._internal();

  //Get User Stats API
  Future<dynamic> getUserStats({
    @required String userId,
  }) async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

    final queryParameters = {
      ParameterConstants.platform: 'APK',
      ParameterConstants.clientVersion: clientVersion,
    };
    //Request
    var response = await _apiBaseHelper.get(
      queryParameters: queryParameters,
      url:
          '/v1/user/support/$userId', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch ReferralCode API
  Future<dynamic> fetchRefferal({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url: "/v1/referralCode/", //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Promo Codes API
  Future<dynamic> fetchPromoCodes({
    @required String userId,
  }) async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

    final queryParameters = {
      ParameterConstants.platform: 'APK',
      ParameterConstants.clientVersion: clientVersion,
    };
    //Request
    var response = await _apiBaseHelper.get(
      queryParameters: queryParameters,
      url: '/v1/bonuses/available/', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Banners  API
  Future<dynamic> fetchBanners({
    @required String userId,
    bool isDeals = false,
  }) async {
    final queryParameters = {
      ParameterConstants.type: (isDeals) ? 'DEALS' : 'HOME',
    };
    //Request
    var response = await _apiBaseHelper.get(
      queryParameters: queryParameters,
      url: '/v1/banners/', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Leaderboards
  Future<dynamic> fetchLeaderBoards({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url:
          '/v1/leaderboards/active', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Leaderboard Ranks
  Future<dynamic> fetchLeaderBoardRanks({
    @required String userId,
    @required String leaderBoardId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/leaderboards/ranks/' +
          leaderBoardId, //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Transactions
  Future<dynamic> fetchTransactions({
    @required String userId,
    @required dynamic fromTime,
    @required int endTime,
    @required String walletName,
  }) async {
    endTime = endTime + 86399000;
    final queryParameters = {
      "fromTime": fromTime.toString(),
      "toTime": endTime.toString(),
      "walletName": walletName
    };
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/transactions/', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
      queryParameters: queryParameters,
    );

    return response.body;
  }

  //Fetch Deposits
  Future<dynamic> fetchDeposits({
    @required dynamic fromTime,
    @required int endTime,
    @required String status,
    @required String userId,
  }) async {
    endTime = endTime + 86399000;
    final queryParameters = {
      "fromTime": fromTime.toString(),
      "toTime": endTime.toString(),
      "status": status
    };
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/deposits/details', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
      queryParameters: queryParameters,
    );

    return response.body;
  }

  //Fetch Withdrawals
  Future<dynamic> fetchWithdrawals({
    @required dynamic fromTime,
    @required int endTime,
    @required String status,
    @required String userId,
  }) async {
    final queryParameters = {
      "fromTime": fromTime.toString(),
      "toTime": endTime.toString(),
      "status": status,
    };
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/withdrawal/details', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
      queryParameters: queryParameters,
    );

    return response.body;
  }

  //Fetch KYC
  Future<dynamic> fetchKycDocsAndConfig({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/kyc/v2', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch Last Successfull Withdrawal
  Future<dynamic> fetchLastSuccessfullWithdrawal({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/withdrawal/load', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch User Info
  Future<dynamic> fetchUserInfo({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url: '/v1/info/users', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch User Profile Info
  Future<dynamic> fetchUserProfileInfo({
    @required String userId,
  }) async {
    //Request
    var response = await _apiBaseHelper.get(
      url:
          '/v1/users/profile/info/', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }

  //Fetch User Login Streak
  Future<dynamic> fetchUserLoginStreak({
    @required String userId,
  }) async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String clientVersion = packageInfo.version;

    final queryParameters = {
      ParameterConstants.platform: 'APK',
      ParameterConstants.clientVersion: clientVersion,
    };
    //Request
    var response = await _apiBaseHelper.get(
      queryParameters: queryParameters,
      url:
      '/v1/user/streak/$userId', //TO BE CHANGED (Constant File To Be Added)
      userId: userId,
    );

    return response.body;
  }
}
