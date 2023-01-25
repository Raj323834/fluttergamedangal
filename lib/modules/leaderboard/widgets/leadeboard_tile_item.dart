import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/asset_paths.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/enum.dart';
import '../../../constants/methods/common_methods.dart';
import '../../../constants/string_constants.dart';
import '../../leaderboard_score/leaderboard_rank_screen.dart';
import '../models/leaderboards_dm.dart';

class LeaderBoardTileItem extends StatefulWidget {
  const LeaderBoardTileItem({
    Key key,
    @required this.index,
    @required this.leaderBoardItemType,
    @required this.leaderboard,
    @required this.userId,
  }) : super(key: key);
  final int index;
  final LeaderBoardItemType leaderBoardItemType;
  final Leaderboard leaderboard;
  final String userId;

  @override
  State<LeaderBoardTileItem> createState() => _LeaderBoardTileItemState();
}

class _LeaderBoardTileItemState extends State<LeaderBoardTileItem> {
  bool visibleAbout = false;
  bool visibleTnC = false;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        if (widget.leaderBoardItemType == LeaderBoardItemType.score) {
          CommonMethods.printLog(
            'leaderboardId',
            widget.leaderboard.leaderboardId,
          );
          Navigator.of(context).push(
            CupertinoPageRoute(
              builder: (context) => LeaderboardRankScreen(
                leaderboardId: widget.leaderboard.leaderboardId,
                promoPrizePool: widget.leaderboard.promoPrizePool,
                lockedPrizePool: widget.leaderboard.lockedPrizePool,
                depositPrizePool: widget.leaderboard.depositPrizePool,
                withdrawalPrizePool: widget.leaderboard.withdrawalPrizePool,
                prizeDetails: widget.leaderboard.prizeDetails,
                ruleType: widget.leaderboard.ruleType,
                userId: widget.userId,
                type: widget.leaderboard.type,
              ),
            ),
          );
        } else if (widget.leaderBoardItemType == LeaderBoardItemType.about) {
          setState(
            () {
              visibleAbout = !visibleAbout;
            },
          );
        } else if (widget.leaderBoardItemType == LeaderBoardItemType.tnc) {
          setState(
            () {
              visibleTnC = !visibleTnC;
            },
          );
        } else {}
      },
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(
            10,
          ),
          border: Border.all(
            color: ColorConstants.greyBorderColor,
          ),
        ),
        child: Column(
          children: [
            Container(
              height: 5.0.h,
              child: Row(
                children: [
                  Image(
                    image: AssetImage(
                      (widget.leaderBoardItemType == LeaderBoardItemType.score)
                          ? AssetPaths.score
                          : (widget.leaderBoardItemType ==
                                  LeaderBoardItemType.about)
                              ? AssetPaths.about
                              : AssetPaths.tnc,
                    ),
                  ),
                  Text(
                    (widget.leaderBoardItemType == LeaderBoardItemType.score)
                        ? StringConstants.score
                        : (widget.leaderBoardItemType ==
                                LeaderBoardItemType.about)
                            ? StringConstants.about
                            : StringConstants.tnc,
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                      color: ColorConstants.kSecondaryColor,
                    ),
                  ),
                  Spacer(),
                  (visibleAbout || visibleTnC)
                      ? Icon(
                          Icons.keyboard_arrow_up_sharp,
                          color: ColorConstants.kSecondaryColor,
                        )
                      : (widget.leaderBoardItemType ==
                              LeaderBoardItemType.score)
                          ? Icon(
                              Icons.arrow_right_alt,
                              color: ColorConstants.kSecondaryColor,
                            )
                          : Icon(
                              Icons.keyboard_arrow_down_sharp,
                              color: ColorConstants.kSecondaryColor,
                            ),
                  SizedBox(
                    width: 2.0.w,
                  )
                ],
              ),
            ),
            Visibility(
              visible: (widget.leaderBoardItemType == LeaderBoardItemType.about)
                  ? visibleAbout
                  : visibleTnC,
              child: Padding(
                padding: EdgeInsets.only(
                  left: 2.0.w,
                  right: 2.0.w,
                  bottom: 0.5.h,
                ),
                child: Text(
                  (widget.leaderBoardItemType == LeaderBoardItemType.about)
                      ? widget.leaderboard.aboutLeaderboard
                      : widget.leaderboard.termsAndConditions ??
                          StringConstants.emptyString,
                  textAlign: TextAlign.justify,
                  style: TextStyle(
                    color: ColorConstants.white,
                    fontSize: 8.0.sp,
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
