import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/withdrawals_dm.dart';

class FetchWithdrawalsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<WithdrawalsDM> fetchWithdrawals({
    @required dynamic fromTime,
    @required int endTime,
    @required String status,
    @required String userId,
  }) async {
    WithdrawalsDM withdrawalsDM;
    try {
      var response = await httpServices.fetchWithdrawals(
        fromTime: fromTime,
        endTime: endTime,
        status: status,
        userId: userId,
      );

      withdrawalsDM = WithdrawalsDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return withdrawalsDM;
  }
}
