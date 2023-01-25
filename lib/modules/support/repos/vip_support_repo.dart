import 'dart:convert';

import 'package:flutter/cupertino.dart';

import '../../../network_new/http_service.dart';
import '../models/vip_support_dm.dart';

class VipSupportRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<VipSupportDM> fetchUserStats({
    @required String userId,
  }) async {
    VipSupportDM vipSupportDm;
    try {
      var response = await httpServices.getUserStats(
        userId: userId,
      );

      vipSupportDm = VipSupportDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
    }
    return vipSupportDm;
  }
}
