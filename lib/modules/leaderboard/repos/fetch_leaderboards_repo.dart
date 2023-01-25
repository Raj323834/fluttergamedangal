import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/leaderboards_dm.dart';

class FetchLeaderboardsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<LeaderBoardsDM> fetchLeaderBoards({
    @required String userId,
  }) async {
    LeaderBoardsDM leaderBoardsDM;
    try {
      var response = await httpServices.fetchLeaderBoards(
        userId: userId,
      );

      leaderBoardsDM = LeaderBoardsDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return leaderBoardsDM;
  }
}
