import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/leaderboard_ranks_dm.dart';

class FetchLeaderBoardsRanksRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<LeaderBoardRankDM> fetchLeaderBoards({
    @required String userId,
    @required String leaderBoardId,
  }) async {
    LeaderBoardRankDM leaderBoardRankDM;
    try {
      var response = await httpServices.fetchLeaderBoardRanks(
        userId: userId,
        leaderBoardId: leaderBoardId,
      );

      leaderBoardRankDM = LeaderBoardRankDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return leaderBoardRankDM;
  }
}
