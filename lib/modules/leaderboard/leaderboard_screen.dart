import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../Network/generate_access_token.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import 'models/leaderboards_dm.dart';
import 'repos/fetch_leaderboards_repo.dart';
import 'widgets/leaderboard_tab_view.dart';
import 'widgets/no_leaderboard_available.dart';

class LeaderboardScreen extends StatefulWidget {
  final String userId;

  LeaderboardScreen({
    @required this.userId,
  });

  @override
  State<LeaderboardScreen> createState() => _LeaderboardScreenState();
}

class _LeaderboardScreenState extends State<LeaderboardScreen> {
  //LeaderBoard Lists
  List<Leaderboard> upcomingLeaderboards;
  List<Leaderboard> activeLeaderboards;
  List<Leaderboard> completedLeaderboards;

  @override
  void initState() {
    getLeaderboards();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ColorConstants.kBackgroundColor,
      body: Column(
        children: [
          //LeaderBoard Image
          Container(
            height: 23.h,
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage(
                  FlavorInfo.isPS
                      ? AssetPaths.leaderBoardPlayStore
                      : AssetPaths.leaderBoard,
                ),
                fit: BoxFit.fill,
              ),
            ),
          ),
          //Circular Loader
          (upcomingLeaderboards == null &&
                  activeLeaderboards == null &&
                  completedLeaderboards == null)
              ? Padding(
                  padding: EdgeInsets.only(
                    top: 2.0.h,
                  ),
                  child: CircularProgressIndicator(),
                )
              : upcomingLeaderboards.length +
                          activeLeaderboards.length +
                          completedLeaderboards.length ==
                      0
                  ?
                  //No List Available
                  Expanded(
                      child: NoLeaderBoardAvailable(),
                    )
                  :
                  //TabBar (UPCOMING,ACTIVE,COMPLETED)
                  Expanded(
                      child: Column(
                        children: [
                          DefaultTabController(
                            length: 3,
                            initialIndex: 1,
                            child: Expanded(
                              child: Column(
                                children: [
                                  Material(
                                    color: ColorConstants.greyBackgroundColor,
                                    child: TabBar(
                                      labelStyle: TextStyle(
                                        fontSize: 10.5.sp,
                                      ),
                                      labelColor:
                                          ColorConstants.kSecondaryColor,
                                      indicatorColor:
                                          ColorConstants.kSecondaryColor,
                                      unselectedLabelColor:
                                          ColorConstants.offWhiteTextColor,
                                      tabs: [
                                        Tab(
                                          text: StringConstants.upcoming,
                                        ),
                                        Tab(
                                          text: StringConstants.active,
                                        ),
                                        Tab(
                                          text: StringConstants.completed,
                                        ),
                                      ],
                                    ),
                                  ),
                                  //Tab Bar View
                                  Expanded(
                                    child: TabBarView(
                                      children: [
                                        LeaderBoardTabView(
                                          leaderboards: upcomingLeaderboards,
                                          userId: widget.userId,
                                        ),
                                        LeaderBoardTabView(
                                          leaderboards: activeLeaderboards,
                                          userId: widget.userId,
                                        ),
                                        LeaderBoardTabView(
                                          leaderboards: completedLeaderboards,
                                          userId: widget.userId,
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
        ],
      ),
    );
  }

  //METHODS
  Future<void> getLeaderboards() async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
      FocusScope.of(context).unfocus();
    } else {
      var repoObj = FetchLeaderboardsRepo();
      LeaderBoardsDM leaderBoardsDM = await repoObj.fetchLeaderBoards(
        userId: widget.userId,
      );
      if (leaderBoardsDM != null) {
        switch (leaderBoardsDM.result) {
          case ResponseStatus.success:
            initialisingLeaderboards(
              activeLeaderboard: leaderBoardsDM.leaderboards,
              upcomingLeaderboard: leaderBoardsDM.upcomingLeaderboards,
              completedLeaderboard: leaderBoardsDM.completedLeaderboards,
            );
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              StringConstants.unableToFetchActiveLeaderboards,
            );
            break;
          case ResponseStatus.notFound:
          case ResponseStatus.leaderboardNotFound:
            CommonMethods.showSnackBar(
              context,
              StringConstants.leaderBoardNotFound,
            );
            setState(
              () {
                upcomingLeaderboards = [];
                activeLeaderboards = [];
                completedLeaderboards = [];
              },
            );
            break;

          case ResponseStatus.tokenExpired:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              widget.userId,
            );
            if (accessTokenGenerated) {
              await getLeaderboards();
            }
            break;
          default:
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          StringConstants.unableToFetchActiveLeaderboards,
        );
      }
    }
  }

  void initialisingLeaderboards({
    @required List<dynamic> activeLeaderboard,
    @required List<dynamic> upcomingLeaderboard,
    @required List<dynamic> completedLeaderboard,
  }) {
    upcomingLeaderboards = upcomingLeaderboard;
    activeLeaderboards = activeLeaderboard;
    completedLeaderboards = completedLeaderboard;
    if (mounted) {
      setState(
        () {},
      );
    }
  }
}
