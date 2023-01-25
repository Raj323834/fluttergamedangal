import 'dart:convert';

import 'package:dangal_games_demo/modules/rewards/models/login_streak_model.dart';
import 'package:flutter/cupertino.dart';

import '../../../network_new/http_service.dart';
import '../models/login_streak_model.dart';

class LoginStreakRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<LoginStreakModel> fetchLoginStreak({
    @required String userId,
  }) async {
    LoginStreakModel loginStreakModel;
    try {
      var response = await httpServices.fetchUserLoginStreak(
        userId: userId,
      );

      loginStreakModel = LoginStreakModel.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
    }
    return loginStreakModel;
  }
}