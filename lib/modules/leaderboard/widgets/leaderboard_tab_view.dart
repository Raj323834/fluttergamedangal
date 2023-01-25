import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../models/leaderboards_dm.dart';
import 'leaderboard_expansion_tile.dart';
import 'no_leaderboard_available.dart';

class LeaderBoardTabView extends StatelessWidget {
  const LeaderBoardTabView({
    Key key,
    @required this.leaderboards,
    @required this.userId,
  }) : super(key: key);
  final List<Leaderboard> leaderboards;
  final String userId;
  @override
  Widget build(BuildContext context) {
    return (leaderboards.isEmpty || leaderboards == [])
        ? NoLeaderBoardAvailable()
        : ListView.builder(
            itemCount: leaderboards.length,
            physics: BouncingScrollPhysics(),
            itemBuilder: (
              context,
              index,
            ) {
              return Column(
                children: [
                  SizedBox(
                    height: 2.0.h,
                  ),
                  LeaderBoardExpansionTile(
                    leaderboard: leaderboards[index],
                    index: index,
                    userId: userId,
                  ),
                ],
              );
            },
          );
  }
}
