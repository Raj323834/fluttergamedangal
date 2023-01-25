import 'dart:convert';

import 'package:flutter/material.dart';

import '../common_models/user_info_dm.dart';
import '../network_new/http_service.dart';

class FetchUserInfoRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<UserInfoDM> fetchUserInfo({
    @required String userId,
  }) async {
    UserInfoDM userInfoDM;

    try {
      var response = await httpServices.fetchUserInfo(
        userId: userId,
      );

      userInfoDM = UserInfoDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return userInfoDM;
  }
}
