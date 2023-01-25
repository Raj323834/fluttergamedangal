import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../Network/generate_access_token.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../leaderboard/models/leaderboards_dm.dart';
import 'models/leaderboard_ranks_dm.dart';
import 'repos/fetch_leaderboards_ranks_repo.dart';

class LeaderboardRankScreen extends StatefulWidget {
  final String leaderboardId, ruleType, type;
  final double promoPrizePool,
      lockedPrizePool,
      depositPrizePool,
      withdrawalPrizePool;
  final List<PrizeDetails> prizeDetails;
  final String userId;

  const LeaderboardRankScreen({
    Key key,
    @required this.leaderboardId,
    @required this.ruleType,
    @required this.type,
    @required this.promoPrizePool,
    @required this.lockedPrizePool,
    @required this.depositPrizePool,
    @required this.withdrawalPrizePool,
    @required this.prizeDetails,
    @required this.userId,
  }) : super(key: key);

  @override
  State<LeaderboardRankScreen> createState() => _LeaderboardRankScreenState();
}

class _LeaderboardRankScreenState extends State<LeaderboardRankScreen> {
  bool isLeaderboard = true;
  String userName = StringConstants.emptyString;
  String avatarImage = UrlConstants.imageUrl + 'assets/images/batman.png';
  String userRank = "-";
  String userPoints = "-";
  List<Ranks> ranks;
  bool ranksAtTop = true;

  final ScrollController scrollController = ScrollController();

  @override
  void initState() {
    asynchronousMethodCalls(
      userId: widget.userId,
      leaderBoardId: widget.leaderboardId,
    );
    scrollController.addListener(
      () {
        setState(
          () {
            ranksAtTop = scrollController.position.pixels == 0.0 ||
                scrollController.position.pixels < 0.0;
          },
        );
      },
    );

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xFFeaeaea),
      body: Container(
        height: Singleton().deviceSize.height,
        decoration: BoxDecoration(
          image: DecorationImage(
            image: AssetImage(
              'assets/images/background.webp',
            ),
            fit: BoxFit.cover,
          ),
        ),
        child: Stack(
          children: [
            SingleChildScrollView(
              child: Column(
                children: [
                  Container(
                    width: 100.0.w,
                    height: isLeaderboard ? 25.0.h : 15.0.h,
                    decoration: BoxDecoration(
                      color: ColorConstants.white,
                      image: DecorationImage(
                        fit: BoxFit.cover,
                        image: AssetImage(
                          'assets/images/leaderboard_icons/leaderboard_rank_upper.webp',
                        ),
                      ),
                    ),
                    child: Padding(
                      padding: EdgeInsets.only(
                        top: 1.0.h,
                        left: 2.0.w,
                        right: 2.0.w,
                      ),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              GestureDetector(
                                onTap: () {
                                  Navigator.of(context).pop();
                                },
                                child: Icon(
                                  Icons.arrow_back_ios,
                                  color: ColorConstants.white,
                                ),
                              ),
                              GestureDetector(
                                onTap: () {
                                  setState(
                                    () {
                                      isLeaderboard = !isLeaderboard;
                                    },
                                  );
                                },
                                child: Container(
                                  height: 5.0.h,
                                  width: 85.0.w,
                                  decoration: BoxDecoration(
                                    color: ColorConstants.white,
                                    borderRadius: BorderRadius.circular(
                                      10.0.w,
                                    ),
                                  ),
                                  child: Row(
                                    children: [
                                      Container(
                                        height: 5.0.h,
                                        width: 42.5.w,
                                        decoration: BoxDecoration(
                                          color: isLeaderboard
                                              ? ColorConstants.blue1
                                              : null,
                                          borderRadius: BorderRadius.circular(
                                            10.0.w,
                                          ),
                                        ),
                                        child: Center(
                                          child: Text(
                                            'LEADERBOARD',
                                            style: TextStyle(
                                              fontSize: 12.0.sp,
                                              fontWeight: FontWeight.bold,
                                              color: isLeaderboard
                                                  ? ColorConstants.white
                                                  : ColorConstants.blue1,
                                            ),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        height: 5.0.h,
                                        width: 42.5.w,
                                        decoration: BoxDecoration(
                                          color: isLeaderboard
                                              ? null
                                              : ColorConstants.blue1,
                                          borderRadius: BorderRadius.circular(
                                            10.0.w,
                                          ),
                                        ),
                                        child: Center(
                                          child: Text(
                                            'PRIZE BREAKUP',
                                            style: TextStyle(
                                              fontSize: 12.0.sp,
                                              fontWeight: FontWeight.bold,
                                              color: isLeaderboard
                                                  ? ColorConstants.blue1
                                                  : ColorConstants.white,
                                            ),
                                          ),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              ),
                            ],
                          ),
                          isLeaderboard
                              ? Column(
                                  children: [
                                    Padding(
                                      padding: EdgeInsets.only(
                                        top: 0.5.h,
                                      ),
                                      child: Text(
                                        'DangalChamp',
                                        style: TextStyle(
                                          color: ColorConstants.orange,
                                          fontWeight: FontWeight.bold,
                                          fontSize: 18.5.sp,
                                        ),
                                      ),
                                    ),
                                    SizedBox(
                                      height: 0.1.h,
                                    ),
                                    Text(
                                      "@" + userName,
                                      style: TextStyle(
                                        fontWeight: FontWeight.bold,
                                        color: ColorConstants.white,
                                        fontSize: 10.5.sp,
                                      ),
                                    ),
                                    SizedBox(
                                      height: 6.0.h,
                                    ),
                                    Row(
                                      children: [
                                        Expanded(
                                          flex: 2,
                                          child: Center(
                                            child: Text(
                                              'RANK',
                                              style: TextStyle(
                                                  color: ColorConstants.white,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 12),
                                            ),
                                          ),
                                        ),
                                        Spacer(),
                                        Expanded(
                                          flex: 2,
                                          child: Center(
                                            child: Text(
                                              (widget.ruleType !=
                                                      "Dangal Points")
                                                  ? "DANGAL POINTS"
                                                  : 'POINTS',
                                              style: TextStyle(
                                                  color: ColorConstants.white,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 12),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                    Row(
                                      children: [
                                        Expanded(
                                          flex: 2,
                                          child: Center(
                                            child: Text(
                                              userRank,
                                              style: TextStyle(
                                                color: ColorConstants.white,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          ),
                                        ),
                                        Spacer(),
                                        Expanded(
                                          flex: 2,
                                          child: Center(
                                            child: Text(
                                              userPoints,
                                              style: TextStyle(
                                                color: ColorConstants.white,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ],
                                )
                              : Flexible(
                                  fit: FlexFit.loose,
                                  child: Column(
                                    mainAxisSize: MainAxisSize.min,
                                    children: [
                                      Spacer(),
                                      Text(
                                        widget.type,
                                        style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          color: ColorConstants.white,
                                          fontSize: 14.0.sp,
                                        ),
                                      ),
                                      SizedBox(
                                        height: 1.0.h,
                                      ),
                                      Container(
                                        width: 35.0.w,
                                        height: 0.3.h,
                                        decoration: BoxDecoration(
                                          color: ColorConstants.white,
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                        ],
                      ),
                    ),
                  ),
                  isLeaderboard ? leaderboard() : prizeBreakup()
                ],
              ),
            ),
            Visibility(
              visible: isLeaderboard,
              child: AnimatedPositioned(
                top: ranksAtTop ? 18.4.h : 13.4.h,
                left: ranksAtTop ? 50.0.w - 6.5.h : 50.0.w - 5.5.h,
                duration: Duration(
                  milliseconds: 200,
                ),
                child: AnimatedContainer(
                  duration: Duration(
                    milliseconds: 200,
                  ),
                  width: ranksAtTop ? 13.0.h : 11.0.h,
                  height: ranksAtTop ? 13.0.h : 11.0.h,
                  decoration: BoxDecoration(
                    color: ColorConstants.grey700,
                  ),
                  child: AnimatedContainer(
                    duration: Duration(
                      milliseconds: 200,
                    ),
                    height: ranksAtTop ? 10.0.h : 8.0.h,
                    width: ranksAtTop ? 10.0.h : 8.0.h,
                    child: Image(
                      image: NetworkImage(
                        avatarImage,
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  //METHODS
  void asynchronousMethodCalls({
    @required String userId,
    @required String leaderBoardId,
  }) async {
    avatarImage = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.avatarURL,
    );
    userName = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );
    setState(
      () {},
    );
    await getLeaderboardRanks(
      userId: userId,
      leaderBoardId: leaderBoardId,
    );
  }

  Future<void> getLeaderboardRanks({
    @required String userId,
    @required String leaderBoardId,
  }) async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
      FocusScope.of(context).unfocus();
    } else {
      var repoObj = FetchLeaderBoardsRanksRepo();
      LeaderBoardRankDM leaderBoardRankDM = await repoObj.fetchLeaderBoards(
        userId: userId,
        leaderBoardId: leaderBoardId,
      );
      if (leaderBoardRankDM != null) {
        switch (leaderBoardRankDM.result) {
          case ResponseStatus.success:
            initialisingLeaderboardRanks(
              leaderBoardRankDM.ranks,
            );
            initialisingUserInformation(
              leaderBoardRankDM.currentUser,
            );
            break;

          case ResponseStatus.dbError:
            initialisingLeaderboardRanks(
              [],
            );
            break;

          case ResponseStatus.notFound:
          case ResponseStatus.leaderboardNotFound:
            initialisingLeaderboardRanks(
              [],
            );
            initialisingUserInformation(
              leaderBoardRankDM.currentUser,
            );
            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await getLeaderboardRanks(
                userId: userId,
                leaderBoardId: leaderBoardId,
              );
            }
            break;
          default:
        }
      } else {
        initialisingLeaderboardRanks(
          [],
        );
      }
    }
  }

  void initialisingLeaderboardRanks(
    List<Ranks> ranksList,
  ) {
    setState(
      () {
        ranks = ranksList;
      },
    );
  }

  void initialisingUserInformation(
    CurrentUser userInfo,
  ) {
    if (userInfo.result == ResponseStatus.success) {
      setState(
        () {
          userRank = userInfo.playerDetails.rank.toString();
          userPoints = userInfo.playerDetails.score.toString();
        },
      );
    } else if (userInfo.result == ResponseStatus.userNotQualified) {
      CommonMethods.showSnackBar(
        context,
        'You are not qualified for this Leaderboard',
      );
    }
  }

  Widget leaderboard() {
    return ranks == null
        ? Padding(
            padding: EdgeInsets.only(
              top: 8.0.h,
            ),
            child: CircularProgressIndicator(),
          )
        : ranks.length == 0
            ? Padding(
                padding: EdgeInsets.only(
                  top: 8.0.h,
                ),
                child: Text(
                  "No Users have been considered yet",
                  style: TextStyle(
                    fontSize: 14.0.sp,
                    color: ColorConstants.grey900,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              )
            : Container(
                height: 55.0.h,
                child: Column(
                  children: [
                    Expanded(
                      child: ListView.builder(
                        itemCount: ranks.length,
                        physics: BouncingScrollPhysics(),
                        controller: scrollController,
                        itemBuilder: (context, index) {
                          return Column(
                            children: [
                              index == 0
                                  ? Column(
                                      children: [
                                        SizedBox(
                                          height: 8.0.h,
                                        ),
                                        Row(
                                          children: [
                                            Padding(
                                              padding: EdgeInsets.only(
                                                left: 11.0.w,
                                              ),
                                              child: Text(
                                                'Rank',
                                                style: TextStyle(
                                                  color: ColorConstants.blue1,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 8.0.sp,
                                                ),
                                              ),
                                            ),
                                            Padding(
                                              padding: EdgeInsets.only(
                                                left: 25.0.w,
                                              ),
                                              child: Text(
                                                'Username',
                                                style: TextStyle(
                                                  color: ColorConstants.blue1,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 8.0.sp,
                                                ),
                                              ),
                                            ),
                                            Spacer(),
                                            Padding(
                                              padding: EdgeInsets.only(
                                                right: 14.0.w,
                                              ),
                                              child: Text(
                                                (widget.ruleType ==
                                                        "Dangal Points")
                                                    ? "Dangal Points"
                                                    : 'POINTS_UI',
                                                style: TextStyle(
                                                  overflow:
                                                      TextOverflow.ellipsis,
                                                  color: ColorConstants.blue1,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 8.0.sp,
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                        SizedBox(
                                          height: 0.5.h,
                                        )
                                      ],
                                    )
                                  : SizedBox.shrink(),
                              Container(
                                width: 80.0.w,
                                height: 8.0.h,
                                decoration: BoxDecoration(
                                  color: ColorConstants.white,
                                ),
                                child: Row(
                                  children: [
                                    Container(
                                      height: 8.0.h,
                                      width: 8.0.w,
                                      decoration: BoxDecoration(
                                        color: ColorConstants.blue1,
                                      ),
                                      child: Center(
                                        child: Text(
                                          ranks[index].rank.toString(),
                                          style: TextStyle(
                                            fontWeight: FontWeight.bold,
                                            color: ColorConstants.white,
                                            fontSize: 14.0.sp,
                                          ),
                                        ),
                                      ),
                                    ),
                                    Container(
                                        height: 8.0.h,
                                        width: 14.0.w,
                                        decoration: BoxDecoration(
                                          image: DecorationImage(
                                            image: NetworkImage(
                                              ranks[index].userAvatar ??
                                                  avatarImage,
                                            ),
                                            fit: BoxFit.fill,
                                          ),
                                        )),
                                    Padding(
                                      padding: EdgeInsets.only(
                                        left: 10.0.w,
                                      ),
                                      child: Text(
                                        ranks[index].userName.toString(),
                                        style: TextStyle(
                                          color: ColorConstants.blue1,
                                          fontWeight: FontWeight.bold,
                                          fontSize: 9.0.sp,
                                        ),
                                      ),
                                    ),
                                    Spacer(),
                                    Padding(
                                      padding: EdgeInsets.only(
                                        right: 5.0.w,
                                      ),
                                      child: Text(
                                        ranks[index].score,
                                        style: TextStyle(
                                          color: ColorConstants.blue1,
                                          fontWeight: FontWeight.bold,
                                          fontSize: 9.0.sp,
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                              SizedBox(
                                height: 2.0.h,
                              ),
                            ],
                          );
                        },
                      ),
                    ),
                  ],
                ),
              );
  }

  Widget prizeBreakup() {
    return Column(
      children: [
        SizedBox(
          height: 2.0.h,
        ),
        Padding(
          padding: EdgeInsets.only(
            left: 10.0.w,
            right: 10.0.w,
          ),
          child: Row(
            children: [
              Expanded(
                flex: 5,
                child: Center(
                  child: Text(
                    'RANK',
                    style: TextStyle(
                      color: ColorConstants.blue1,
                      fontSize: 12.0.sp,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 5,
                child: Center(
                  child: Text(
                    'PRIZE',
                    style: TextStyle(
                      color: ColorConstants.blue1,
                      fontSize: 12.0.sp,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
        Container(
          width: 80.0.w,
          height: 50.0.h,
          decoration: BoxDecoration(
            color: ColorConstants.white.withOpacity(
              0.8,
            ),
            borderRadius: BorderRadius.circular(
              2.0.w,
            ),
            border: Border.all(
              color: ColorConstants.blue1,
            ),
          ),
          child: ListView.builder(
            itemCount: widget.prizeDetails.length,
            itemBuilder: (context, index) {
              return prizeBreakupRanks(
                index,
              );
            },
          ),
        ),
      ],
    );
  }

  Widget prizeBreakupRanks(
    int index,
  ) {
    double finalPrize = 0.0;
    finalPrize +=
        (widget.promoPrizePool * widget.prizeDetails[index].promoPercent) / 100;
    finalPrize +=
        (widget.lockedPrizePool * widget.prizeDetails[index].lockedPercent) /
            100;
    finalPrize +=
        (widget.depositPrizePool * widget.prizeDetails[index].depositPercent) /
            100;
    finalPrize += (widget.withdrawalPrizePool *
            widget.prizeDetails[index].withdrawalPercent) /
        100;
    return Container(
      width: 80.0.w,
      height: 2.5.h,
      decoration: BoxDecoration(
        border: Border(
          bottom: BorderSide(
            color: ColorConstants.grey,
          ),
        ),
      ),
      child: Row(
        children: [
          Expanded(
            flex: 5,
            child: Center(
              child: Text(
                widget.prizeDetails[index].rank.toString(),
                style: TextStyle(
                  color: ColorConstants.blue1,
                  fontSize: 10.0.sp,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
          Expanded(
            flex: 5,
            child: Center(
              child: Text(
                finalPrize.toInt().toString(),
                style: TextStyle(
                  color: ColorConstants.blue1,
                  fontSize: 10.0.sp,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
