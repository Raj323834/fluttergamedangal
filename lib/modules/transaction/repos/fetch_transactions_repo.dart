import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/transactions_dm.dart';

class FetchTransactionsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<TransactionsDM> fetchTransactions({
    @required String userId,
    @required dynamic fromTime,
    @required int endTime,
    @required String walletName,
  }) async {
    TransactionsDM transactionsDM;
    try {
      var response = await httpServices.fetchTransactions(
        userId: userId,
        fromTime: fromTime,
        endTime: endTime,
        walletName: walletName,
      );

      transactionsDM = TransactionsDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return transactionsDM;
  }
}
