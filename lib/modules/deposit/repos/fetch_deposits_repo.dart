import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/deposits_dm.dart';

class FetchDepositsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<DepositsDM> fetchDeposits({
    @required dynamic fromTime,
    @required int endTime,
    @required String status,
    @required String userId,
  }) async {
    DepositsDM depositsDM;

    try {
      var response = await httpServices.fetchDeposits(
        fromTime: fromTime,
        endTime: endTime,
        status: status,
        userId: userId,
      );

      depositsDM = DepositsDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return depositsDM;
  }
}
