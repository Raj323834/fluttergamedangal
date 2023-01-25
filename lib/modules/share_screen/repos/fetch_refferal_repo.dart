import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/referral_code_dm.dart';

class FetchReferralRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future fetchRefferal({
    @required String userId,
  }) async {
    ReferralCodeDM referralCodeDM;

    try {
      var response = await httpServices.fetchRefferal(
        userId: userId,
      );

      referralCodeDM = ReferralCodeDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return referralCodeDM;
  }
}
