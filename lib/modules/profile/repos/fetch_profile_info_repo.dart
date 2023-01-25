import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/profile_info_dm.dart';

class FetchProfileInfoRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<ProfileInfoDM> fetchProfileInfo({
    @required String userId,
  }) async {
    ProfileInfoDM profileInfoDM;

    try {
      var response = await httpServices.fetchUserProfileInfo(
        userId: userId,
      );

      profileInfoDM = ProfileInfoDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return profileInfoDM;
  }
}
