import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/last_withdrawal_dm.dart';

class FetchLastWithdrawalRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<LastWithdrawalDM> fetchLastSuccessfullWithdrawal({
    @required String userId,
  }) async {
    LastWithdrawalDM lastWithdrawalDM;
    try {
      var response = await httpServices.fetchLastSuccessfullWithdrawal(
        userId: userId,
      );

      lastWithdrawalDM = LastWithdrawalDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return lastWithdrawalDM;
  }
}
