import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/enum.dart';
import '../models/leaderboards_dm.dart';
import 'leadeboard_tile_item.dart';

class LeaderBoardExpansionTile extends StatelessWidget {
  const LeaderBoardExpansionTile({
    Key key,
    @required this.leaderboard,
    @required this.index,
    @required this.userId,
  }) : super(key: key);
  final Leaderboard leaderboard;
  final int index;
  final String userId;
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 82.w,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(
          10,
        ),
        boxShadow: [
          BoxShadow(
            color: ColorConstants.kSecondaryColor.withOpacity(
              0.25,
            ),
            spreadRadius: 0.2,
            blurRadius: 7,
            offset: Offset(
              -4,
              4,
            ),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(
          10,
        ),
        child: ExpansionTile(
          title: Text(
            leaderboard.name.toString(),
            style: TextStyle(
              color: ColorConstants.kSecondaryColor,
              fontSize: 17,
            ),
          ),
          backgroundColor: ColorConstants.black,
          collapsedBackgroundColor: ColorConstants.black,
          collapsedIconColor: ColorConstants.kSecondaryColor,
          iconColor: ColorConstants.kSecondaryColor,
          childrenPadding: EdgeInsets.symmetric(
            horizontal: 26,
            vertical: 16,
          ),
          children: [
            LeaderBoardTileItem(
              index: index,
              leaderBoardItemType: LeaderBoardItemType.score,
              leaderboard: leaderboard,
              userId: userId,
            ),
            SizedBox(
              height: 1.0.h,
            ),
            LeaderBoardTileItem(
              index: index,
              leaderBoardItemType: LeaderBoardItemType.about,
              leaderboard: leaderboard,
              userId: userId,
            ),
            SizedBox(
              height: 1.0.h,
            ),
            LeaderBoardTileItem(
              index: index,
              leaderBoardItemType: LeaderBoardItemType.tnc,
              leaderboard: leaderboard,
              userId: userId,
            ),
          ],
        ),
      ),
    );
  }
}
