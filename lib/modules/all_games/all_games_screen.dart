import 'dart:async';
import 'dart:convert';
import 'dart:developer';
import 'dart:ui';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:sizer/sizer.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../Games/callbreak/call_break_screen.dart';
import '../../Games/poker/poker_web_view.dart';
import '../../Games/pool/eight_ball_pool_screen.dart';
import '../../Games/worklooper_games/worklooper_games_screen.dart';
import '../../Model/dg_apk_download_model.dart';
import '../../Model/firebase_analytics_model.dart';
import '../../Network/GameHelperService/fantasy_helper_service.dart';
import '../../Network/GameHelperService/poker_helper_service.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/geo_restriction_service.dart';
import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/complete_profile_dialog.dart';
import '../../common_widgets/update_username_dialog.dart';
import '../../constants/app_constants.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/enum.dart';
import '../../constants/game_names.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import '../wallet/wallet_screen.dart';
import 'models/game_tile_dm.dart';
import 'widgets/fantasy_dangal_banner.dart';
import 'widgets/game_tile.dart';

class AllGamesScreen extends StatefulWidget {
  final void Function(
    int newIndex,
    String code,
  ) _changeFromDealsIndex;
  final void Function(
    FlutterWebviewPlugin webviewInstance,
  ) _flutterWebviewInstance;
  final String _routeGames;
  final String userId;

  AllGamesScreen(
    this._changeFromDealsIndex,
    this._flutterWebviewInstance,
    this._routeGames,
    this.userId,
  );

  @override
  _AllGamesScreenState createState() => _AllGamesScreenState();
}

class _AllGamesScreenState extends State<AllGamesScreen> {
  DateTime currentBackPressTime;
  var flutterWebviewPlugin = FlutterWebviewPlugin();

  String leagueCode = StringConstants.emptyString;

  List cardGamesList = [];
  List casualGamesList = [];

  final _dialogtextcontroller = TextEditingController();
  int usernameUpdateCount = 0;
  String username = StringConstants.emptyString;

  @override
  void initState() {
    asynchronousMethodsCall(
      userId: widget.userId,
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: All_GAMES_ROUTE,
    );
    super.initState();
  }

  @override
  void dispose() {
    flutterWebviewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        //Double Tap To Exit
        DateTime now = DateTime.now();
        if (currentBackPressTime == null ||
            now.difference(currentBackPressTime) >
                Duration(
                  seconds: 2,
                )) {
          currentBackPressTime = now;
          CommonMethods.showSnackBar(
            context,
            StringConstants.pleaseClickBackAgainToExit,
          );
          return Future.value(
            false,
          );
        }
        return Future.value(
          true,
        );
      },
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        body: Column(
          children: <Widget>[
            Expanded(
              child: SingleChildScrollView(
                physics: BouncingScrollPhysics(),
                child: Column(
                  children: [
                    SizedBox(
                      height: 7.0.h,
                    ),
                    //Sports Games Title
                    Center(
                      child: Text(
                        'all_games_screen.sports_games'.tr(),
                        style: TextStyle(
                          fontSize: 14.3.sp,
                          color: ColorConstants.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 1.5.h,
                    ),
                    //Fantasy Dangal Game Banner
                    FantasyDangalBanner(
                        onTap: () async {
                          var connectivityResult =
                              await Connectivity().checkConnectivity();
                          if (connectivityResult == ConnectivityResult.none) {
                            CommonMethods.showSnackBar(
                              context,
                              StringConstants.noInternetConnection,
                            );
                          } else {
                            onTapGame(
                              gameType: GameType.sportsGame,
                              index: 0,
                              userId: widget.userId,
                            );
                          }
                        },
                        iconPath: getGameTileDm(
                          GameNames.fantasy,
                        ).gameIconPath,
                        gameName: getGameTileDm(
                          GameNames.fantasy,
                        ).gameName),
                    SizedBox(
                      height: 4.0.h,
                    ),
                    //Card Games Title
                    Center(
                      child: Text(
                        'all_games_screen.card_games'.tr(),
                        style: TextStyle(
                          fontSize: 14.3.sp,
                          color: ColorConstants.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 1.5.h,
                    ),
                    //Card Games Grid
                    Padding(
                      padding: EdgeInsets.symmetric(
                        horizontal: countOfGames(
                                  GameType.cardGame,
                                ) ==
                                3
                            ? 10.0.w
                            : 25.0.w,
                      ),
                      child: GridView.builder(
                        physics: ClampingScrollPhysics(),
                        itemCount: countOfGames(
                          GameType.cardGame,
                        ),
                        shrinkWrap: true,
                        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: countOfGames(
                                    GameType.cardGame,
                                  ) ==
                                  3
                              ? 3
                              : 2,
                          crossAxisSpacing: 5.0.w,
                          mainAxisSpacing: 1.5.h,
                        ),
                        itemBuilder: (
                          BuildContext context,
                          int index,
                        ) {
                          //Card Game Tile
                          return GameTile(
                            onTap: () async {
                              var connectivityResult =
                                  await Connectivity().checkConnectivity();
                              if (connectivityResult ==
                                  ConnectivityResult.none) {
                                CommonMethods.showSnackBar(
                                  context,
                                  StringConstants.noInternetConnection,
                                );
                              } else {
                                onTapGame(
                                  gameType: GameType.cardGame,
                                  index: index,
                                  userId: widget.userId,
                                );
                              }
                            },
                            iconPath: getGameTileDm(
                              cardGamesList[index],
                            ).gameIconPath,
                            gameName: getGameTileDm(
                              cardGamesList[index],
                            ).gameName,
                          );
                        },
                      ),
                    ),
                    SizedBox(
                      height: 4.0.h,
                    ),
                    //Casual Games Title
                    Center(
                      child: Text(
                        'all_games_screen.casual_games'.tr(),
                        style: TextStyle(
                          fontSize: 14.3.sp,
                          color: ColorConstants.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 1.5.h,
                    ),
                    //Casual Games Grid
                    Padding(
                      padding: EdgeInsets.symmetric(
                        horizontal: 10.0.w,
                      ),
                      child: GridView.builder(
                        physics: ClampingScrollPhysics(),
                        itemCount: countOfGames(
                          GameType.casualGame,
                        ),
                        shrinkWrap: true,
                        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 3,
                          crossAxisSpacing: 5.0.w,
                          mainAxisSpacing: 1.5.h,
                        ),
                        itemBuilder: (
                          BuildContext context,
                          int index,
                        ) {
                          //Casual Game Tile
                          return GameTile(
                            onTap: () async {
                              var connectivityResult =
                                  await Connectivity().checkConnectivity();
                              if (connectivityResult ==
                                  ConnectivityResult.none) {
                                CommonMethods.showSnackBar(
                                  context,
                                  StringConstants.noInternetConnection,
                                );
                              } else {
                                onTapGame(
                                  gameType: GameType.casualGame,
                                  index: index,
                                  userId: widget.userId,
                                );
                              }
                            },
                            iconPath: getGameTileDm(
                              casualGamesList[index],
                            ).gameIconPath,
                            gameName: getGameTileDm(
                              casualGamesList[index],
                            ).gameName,
                          );
                        },
                      ),
                    ),
                    SizedBox(
                      height: 4.0.h,
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  //Methods
  void asynchronousMethodsCall({
    @required String userId,
  }) async {
    await getGamesOrdering();
    await openFantasy(
      userId: userId,
    );
    await preferredLanguage();
    usernameUpdateCount = await SharedPrefService.getIntValuesFromSharedPref(
      SharedPrefKeys.usernameUpdateCount,
    );
    username = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );
    openGame(
      gameName: widget._routeGames,
      userId: userId,
    );
  }

  Future<void> getGamesOrdering() async {
    var cardList = await SharedPrefService.getListValuesFromSharedPref(
      SharedPrefKeys.cardGames,
    );
    cardGamesList = (cardList != null)
        ? cardList.toList()
        : [
            GameNames.rummy,
            GameNames.poker,
            GameNames.callBreak,
          ].toList();
    casualGamesList = await SharedPrefService.getListValuesFromSharedPref(
          SharedPrefKeys.casualGames,
        ) ??
        [
          GameNames.streetRacer,
          GameNames.runnerNumberOne,
          GameNames.pool,
          GameNames.fruitSplit,
          GameNames.candyCrush,
          GameNames.carrom,
          GameNames.archery,
          GameNames.bubbleShooter,
          GameNames.knifeCut,
        ];
    setState(
      () {
        if (FlavorInfo.isCoInWithoutPoker) {
          if (cardGamesList.contains(GameNames.poker)) {
            cardGamesList.remove(GameNames.poker);
          }
        } else {
          CommonMethods.printLog('POKER IS AVAILABLE', 'POKER IS AVAILABLE');
        }
      },
    );
  }

  Future<void> openFantasy({@required String userId}) async {
    leagueCode = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.leagueCode,
        ) ??
        StringConstants.emptyString;
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.leagueCode,
      StringConstants.emptyString,
    );
    if (leagueCode != StringConstants.emptyString) {
      await getFantasyUrl(
        userId,
      );
    }
  }

  Future<void> preferredLanguage() async {
    String language = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.preferredLanguage,
    );
    CommonMethods.printLog(
      "PREFERRED LANGUAGE:------> ",
      language,
    );
  }

  _askingPermission({
    @required BuildContext context,
    @required String game,
    @required String userId,
  }) async {
    final PermissionStatus permissionStatus = await _getPermission(
      context,
    );
    if (permissionStatus == PermissionStatus.granted) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        StringConstants.storagePermissionGranted,
      );
      if (game == GameNames.callBreak) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: CALLBREAK_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => CallBreakScreen(
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.streetRacer) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: STREET_RACER_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.streetRacer,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.fruitSplit) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: FRUIT_SPLIT_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.fruitSplit,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.pool) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: POOL_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => EightBallPoolScreen(
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.runnerNumberOne) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: RUNNER_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.runnerNumberOne,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.bubbleShooter) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: BUBBLE_SHOOTER_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.bubbleShooter,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.knifeCut) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: KNIFE_CUT_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.knifeCut,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.archery) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: ARCHERY_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.archery,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.carrom) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: CARROM_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.carrom,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.candyCrush) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: CANDY_CLASH_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.candyCrush,
              userId: userId,
            ),
          ),
        );
      } else if (game == GameNames.ludo) {
        FirebaseAnalyticsModel.analyticsScreenTracking(
          screenName: LUDO_ROUTE,
        );
        await Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => WorklooperGamesScreen(
              gameName: GameNames.ludo,
              userId: userId,
            ),
          ),
        );
      }
    } else {
      showDialog(
        context: context,
        builder: (BuildContext context) => CupertinoAlertDialog(
          title: Text(
            StringConstants.permissionError,
          ),
          content: Text(
            StringConstants.pleaseEnableStorageAccess,
          ),
          actions: <Widget>[
            CupertinoDialogAction(
              child: Text(
                StringConstants.ok,
              ),
              onPressed: () => Navigator.of(context).pop(),
            )
          ],
        ),
      );
    }
  }

  Future<void> getPokerUrl({
    @required BuildContext context,
    @required String userId,
  }) async {
    String firstName = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.firstName,
    );
    String username = await SharedPrefService.getStringValuesFromSharedPref(
        SharedPrefKeys.userName);
    String avatarUrl = await SharedPrefService.getStringValuesFromSharedPref(
        SharedPrefKeys.pokerAvatarUrl);
    Map<String, Object> result = await PokerHelperService.pokerLaunch(
      username,
      firstName,
      "MOBILE",
      avatarUrl,
      userId,
    );
    CommonMethods.printLog(
      "result POKER URL---- >",
      result.toString(),
    );
    if (result.containsKey('noInternet')) {
      Navigator.of(context).pop();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      DgApkDownloadModel.checkUpdate();
    } else if (result.containsKey('error')) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
      } else if (data.containsKey('result') &&
              (data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await getPokerUrl(
            context: context,
            userId: userId,
          );
        }
      } else if (data.containsKey('result') &&
          data['result'] == ResponsesKeys.SUCCESS) {
        try {
          final Set<JavascriptChannel> jsChannels = [
            JavascriptChannel(
              name: 'appControl',
              onMessageReceived: (
                JavascriptMessage message,
              ) {
                if (message.message == "exit-poker") {
                  flutterWebviewPlugin.reloadUrl(
                    'https://www.google.com/',
                  );
                  flutterWebviewPlugin.close();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).pop();
                  DgApkDownloadModel.checkUpdate();
                } else if (message.message == "add-cash") {
                  flutterWebviewPlugin.reloadUrl(
                    'https://www.google.com/',
                  );
                  flutterWebviewPlugin.close();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).pop();
                  DgApkDownloadModel.checkUpdate();
                  widget._changeFromDealsIndex(2, StringConstants.emptyString);
                } else if (message.message.contains(
                  "cashgame_clevertap",
                )) {
                  final jsonBody = json.decode(
                    message.message,
                  );
                  var eventData = {
                    "game_name": "Poker",
                    "game_amount": jsonBody['cashgame_clevertap']['buyin'],
                    "game_type": jsonBody['cashgame_clevertap']['tableName'],
                  };
                  CommonMethods.printLog(
                    "CleverTap Cash game Played",
                    eventData.toString(),
                  );
                  CleverTapPlugin.recordEvent(
                    "Cash game Played",
                    eventData,
                  );
                } else if (message.message.contains(
                  "tour_clevertap",
                )) {
                  final jsonBody = json.decode(
                    message.message,
                  );
                  var eventData = {
                    "game_name": "Poker",
                    "game_amount": jsonBody['tour_clevertap']['buyin'],
                    "game_type": jsonBody['tour_clevertap']['tableName'],
                  };
                  CommonMethods.printLog(
                    "CleverTap Cash game Played",
                    eventData.toString(),
                  );
                  CleverTapPlugin.recordEvent(
                    "Cash game Played",
                    eventData,
                  );
                  CleverTapPlugin.recordEvent(
                    "Tournament Joined",
                    {
                      "game_name": "Poker",
                      "game_type": "Cash",
                      "game_amount": jsonBody['tour_clevertap']['buyin']
                    },
                  );
                }
              },
            ),
          ].toSet();

          String url = data['gameUrl'];
          log(url);
          final statusbarHeight = MediaQueryData.fromWindow(window).padding.top;
          flutterWebviewPlugin.close();
          flutterWebviewPlugin.launch(
            url,
            hidden: true,
            withOverviewMode: true,
            javascriptChannels: jsChannels,
            rect: Rect.fromLTWH(
              0,
              statusbarHeight,
              MediaQuery.of(context).size.width,
              MediaQuery.of(context).size.height,
            ),
          );
          flutterWebviewPlugin.show();
          Navigator.of(
            context,
            rootNavigator: true,
          ).push(
            CupertinoPageRoute(
              builder: (context) => PokerWebview(
                flutterWebviewPlugin: flutterWebviewPlugin,
              ),
            ),
          );
        } catch (e) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "Inside Catch " + e.toString(),
          );
        }
      } else {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.somethingWentWrong,
            );
          },
        );
      }
    }
  }

  getFantasyUrl(
    String userId,
  ) async {
    Map<String, Object> result = await FantasyHelperService.fantasyLaunch(
      userId,
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "result---- >",
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      result.toString(),
    );
    if (result.containsKey('noInternet')) {
      Navigator.of(context).pop();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      DgApkDownloadModel.checkUpdate();
    } else if (result.containsKey('error')) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrongTryAgain,
        );
      } else if (data.containsKey('result') &&
              (data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await getFantasyUrl(
            userId,
          );
        }
      } else if (data.containsKey('result') &&
          data['result'] == ResponsesKeys.SUCCESS) {
        try {
          int percentage = await SharedPrefService.getIntValuesFromSharedPref(
            SharedPrefKeys.fantasyPercent,
          );

          String url = StringConstants.emptyString;
          if (leagueCode != StringConstants.emptyString) {
            url = data['url'] +
                "/?access_token=" +
                data['accessToken'] +
                "&partner=" +
                data['partnerId'] +
                "&percent=" +
                percentage.toString() +
                "&splash=1&splashPath=dangal&league_code=" +
                leagueCode;
          } else {
            url = data['url'] +
                "/?access_token=" +
                data['accessToken'] +
                "&partner=" +
                data['partnerId'] +
                "&percent=" +
                percentage.toString() +
                "&splash=1&splashPath=dangal";
          }
          CommonMethods.printLog(
            "Fantasy Url",
            url,
          );

          final Set<JavascriptChannel> jsChannels = [
            JavascriptChannel(
              name: 'flutterWebView',
              onMessageReceived: (
                JavascriptMessage message,
              ) async {
                CommonMethods.printLog(
                  StringConstants.emptyString,
                  'App Control: ${message.message}'.toString(),
                );
                if (message.message.contains("bbAddMoney")) {
                  widget._changeFromDealsIndex(
                    2,
                    StringConstants.emptyString,
                  );
                  widget._flutterWebviewInstance(
                    flutterWebviewPlugin,
                  );
                  flutterWebviewPlugin.hide();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).pop();
                  DgApkDownloadModel.checkUpdate();
                } else if (message.message == "bbExitApp") {
                  widget._changeFromDealsIndex(
                    0,
                    StringConstants.emptyString,
                  );
                  flutterWebviewPlugin.reloadUrl(
                    'https://www.google.com/',
                  );
                  flutterWebviewPlugin.close();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).pop();
                  DgApkDownloadModel.checkUpdate();
                } else if (message.message == "bbPartnerWalletCTA") {
                  widget._changeFromDealsIndex(
                    0,
                    StringConstants.emptyString,
                  );
                  flutterWebviewPlugin.reloadUrl(
                    'https://www.google.com/',
                  );
                  flutterWebviewPlugin.close();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).pop();
                  DgApkDownloadModel.checkUpdate();
                  Navigator.of(
                    context,
                    rootNavigator: true,
                  ).push(
                    CupertinoPageRoute(
                      builder: (BuildContext context) => WalletScreen(
                        userId: userId,
                      ),
                    ),
                  );
                } else if (message.message.contains(
                  "SelectMatch",
                )) {
                  String jsonBody = message.message.split("#")[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  CleverTapPlugin.recordEvent("Select match", body);
                } else if (message.message.contains(
                  "SelectLeague",
                )) {
                  String jsonBody = message.message.split("#")[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  CleverTapPlugin.recordEvent(
                    "Select league",
                    body,
                  );
                } else if (message.message.contains(
                  "JoinLeague",
                )) {
                  String jsonBody = message.message.split(
                    "#",
                  )[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  CleverTapPlugin.recordEvent(
                    "Join league",
                    body,
                  );
                  if (body['is_free_entry'] == 'No') {
                    var eventData = {
                      "game_name": "Fantasy",
                      "game_amount": body['entry_fee'],
                      "game_type": body['league_type'],
                    };
                    CommonMethods.printLog(
                      "CleverTap Cash game Played",
                      eventData.toString(),
                    );
                    CleverTapPlugin.recordEvent(
                      "Cash game Played",
                      eventData,
                    );
                  }
                } else if (message.message.contains("bbCreateTeamInitiated")) {
                  String jsonBody = message.message.split(
                    "#",
                  )[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  CleverTapPlugin.recordEvent(
                    "Team Creation Initiated",
                    body,
                  );
                } else if (message.message.contains(
                  "bbCreateTeamComplete",
                )) {
                  String jsonBody = message.message.split(
                    "#",
                  )[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  CleverTapPlugin.recordEvent(
                    "Team Creation Completed",
                    body,
                  );
                } else if (message.message.contains(
                  "bbLeagueInvite",
                )) {
                  String jsonBody = message.message.split(
                    "#",
                  )[1];
                  final body = json.decode(
                    jsonBody,
                  );
                  String inviteUrl =
                      "https://dangalgames.sng.link/Dzeuw/hc9d?_ddl=fantasy-dangal/" +
                          body['leagueCode'];
                  CommonMethods.printLog(
                    "Invite Url",
                    inviteUrl,
                  );

                  String text =
                      "Hey, Play Fantasy Dangal with me. Click on this link: $inviteUrl, to join my league.";
                  String url = "https://wa.me/?text=" + text;
                  var encoded = Uri.encodeFull(
                    url,
                  );
                  if (await canLaunchUrl(
                    Uri.parse(
                      encoded,
                    ),
                  )) {
                    await launchUrl(
                      Uri.parse(
                        encoded,
                      ),
                    );
                  } else {
                    CommonMethods.showSnackBar(
                      context,
                      "Unable to launch whatsapp!",
                    );
                  }
                }
              },
            ),
          ].toSet();

          final statusbarHeight = MediaQueryData.fromWindow(
            window,
          ).padding.top;
          flutterWebviewPlugin.close();
          flutterWebviewPlugin.launch(
            url,
            withJavascript: true,
            withLocalStorage: true,
            hidden: true,
            withOverviewMode: true,
            javascriptChannels: jsChannels,
            rect: Rect.fromLTWH(
              0,
              statusbarHeight,
              MediaQuery.of(context).size.width,
              MediaQuery.of(context).size.height - statusbarHeight,
            ),
          );
          Navigator.of(
            context,
            rootNavigator: true,
          ).push(
            CupertinoPageRoute(
              builder: (context) => PokerWebview(
                flutterWebviewPlugin: flutterWebviewPlugin,
              ),
            ),
          );
          flutterWebviewPlugin.show();
        } catch (e) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "Inside Catch " + e.toString(),
          );
        }
      } else {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (_) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.somethingWentWrong,
            );
          },
        );
      }
    }
  }

  Future<PermissionStatus> _getPermission(
    BuildContext context,
  ) async {
    final PermissionStatus permission = await Permission.storage.request();
    if (permission != PermissionStatus.granted &&
        permission != PermissionStatus.denied) {
      final Map<Permission, PermissionStatus> permissionStatus =
          await [Permission.storage].request();
      return permissionStatus[Permission.storage];
    } else {
      return permission;
    }
  }

  void onTapGame({
    @required GameType gameType,
    @required int index,
    @required String userId,
  }) async {
    String game = StringConstants.emptyString;
    if (gameType == GameType.sportsGame) {
      game = GameNames.fantasy;
    } else if (gameType == GameType.cardGame) {
      game = cardGamesList[index];
    } else if (gameType == GameType.casualGame) {
      game = casualGamesList[index];
    }
    openGame(
      gameName: game,
      userId: userId,
    );
  }

  void openGame({
    @required String gameName,
    @required String userId,
  }) async {
    //FACEBOOK
    const platform = MethodChannel(
      'com.flutter.fbevents',
    );
    platform.invokeMethod(
      "customEvents",
      {
        "eventName": gameName + " clicked",
      },
    );
    switch (gameName) {
      case GameNames.fantasy:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {"gameName": GameNames.fantasy},
          );
          CleverTapPlugin.recordEvent(
            StringConstants.gameLaunched,
            {
              StringConstants.gameName: GameNames.fantasy,
            },
          );
          CommonMethods.showSnackBar(
            context,
            StringConstants.pleaseWait,
          );

          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return WillPopScope(
                onWillPop: () {
                  return Future.delayed(Duration.zero).then(
                    (value) => false,
                  );
                },
                child: Center(
                  child: CircularProgressIndicator(
                    backgroundColor: ColorConstants.blue,
                  ),
                ),
              );
            },
          );
          bool geoRes = await GeorestrictionService.gameplayAllowed(
            context,
            GameNames.fantasy,
            userId,
          );
          Navigator.of(
            context,
            rootNavigator: true,
          ).pop();
          if (geoRes) {
            usernameUpdateCount =
                await SharedPrefService.getIntValuesFromSharedPref(
              SharedPrefKeys.usernameUpdateCount,
            );
            if (usernameUpdateCount != 0) {
              await getFantasyUrl(
                userId,
              );
            } else {
              showDialog(
                context: context,
                builder: (
                  BuildContext context,
                ) {
                  return UpdateUsernameDialog(
                    header: "USERNAME",
                    description: "Enter Username to Update",
                    textInputType: TextInputType.name,
                    oldValue: username,
                    textEditingController: _dialogtextcontroller,
                    inputformatters: [
                      FilteringTextInputFormatter.allow(
                        RegExpMethods.alphaNumeric,
                      ),
                    ],
                    length: 25,
                  );
                },
              ).whenComplete(
                () => _dialogtextcontroller.clear(),
              );
            }
          }
        }
        break;
      case GameNames.archery:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.archery,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.archery,
            userId: userId,
          );
        }
        break;
      case GameNames.bubbleShooter:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.bubbleShooter,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.bubbleShooter,
            userId: userId,
          );
        }
        break;
      case GameNames.knifeCut:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {"gameName": GameNames.knifeCut},
          );
          await _askingPermission(
            context: context,
            game: GameNames.knifeCut,
            userId: userId,
          );
        }
        break;
      case GameNames.fruitSplit:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.fruitSplit,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.fruitSplit,
            userId: userId,
          );
        }
        break;
      case GameNames.carrom:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.carrom,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.carrom,
            userId: userId,
          );
        }
        break;
      case GameNames.rummy:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.rummy,
            },
          );
          CleverTapPlugin.recordEvent(
            StringConstants.gameLaunched,
            {
              StringConstants.gameName: GameNames.rummy,
            },
          );
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return WillPopScope(
                onWillPop: () {
                  return Future.delayed(Duration.zero).then(
                    (value) => false,
                  );
                },
                child: Center(
                  child: CircularProgressIndicator(
                    backgroundColor: ColorConstants.blue,
                  ),
                ),
              );
            },
          );
          bool geoRes = await GeorestrictionService.gameplayAllowed(
            context,
            GameNames.rummy,
            userId,
          );
          Navigator.of(
            context,
            rootNavigator: true,
          ).pop();
          if (geoRes) {
            usernameUpdateCount =
                await SharedPrefService.getIntValuesFromSharedPref(
              SharedPrefKeys.usernameUpdateCount,
            );
            if (usernameUpdateCount != 0) {
              const platform = MethodChannel(
                UrlConstants.rummyMethodChannel,
              );
              String userName =
                  await SharedPrefService.getStringValuesFromSharedPref(
                SharedPrefKeys.userName,
              );
              String assetImage =
                  await SharedPrefService.getStringValuesFromSharedPref(
                SharedPrefKeys.lrAvatarURL,
              );
              await SharedPrefService.addBoolToSharedPref(
                SharedPrefKeys.rummyOpened,
                true,
              );
              var rummyResult = await platform.invokeMethod(
                "startRummyDangal",
                {
                  "user_id": userId,
                  "user_name": userName,
                  "asset_image": assetImage
                },
              );
              await SharedPrefService.addBoolToSharedPref(
                SharedPrefKeys.rummyOpened,
                false,
              );
              if (rummyResult == "Low Balance") {
                widget._changeFromDealsIndex(
                  FlavorInfo.isPS
                      ? AppConstants.addCashPS
                      : AppConstants.addCash,
                  StringConstants.emptyString,
                );
              }
            } else {
              showDialog(
                context: context,
                builder: (
                  BuildContext context,
                ) {
                  return UpdateUsernameDialog(
                    header: "USERNAME",
                    description: "Enter Username to Update",
                    textInputType: TextInputType.name,
                    oldValue: username,
                    textEditingController: _dialogtextcontroller,
                    inputformatters: [
                      FilteringTextInputFormatter.allow(
                        RegExpMethods.alphaNumeric,
                      ),
                    ],
                    length: 25,
                  );
                },
              ).whenComplete(
                () => _dialogtextcontroller.clear(),
              );
            }
          }
        }
        break;
      case GameNames.poker:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.poker,
            },
          );
          CleverTapPlugin.recordEvent(
            StringConstants.gameLaunched,
            {
              StringConstants.gameName: GameNames.poker,
            },
          );
          var username = await SharedPrefService.getStringValuesFromSharedPref(
            SharedPrefKeys.userName,
          );
          if (username == StringConstants.emptyString || username == null) {
            showDialog(
              context: context,
              builder: (
                BuildContext context,
              ) {
                return CompleteProfileDialog(
                  userId: userId,
                );
              },
            );
          } else {
            CommonMethods.showSnackBar(
              context,
              StringConstants.pleaseWait,
            );
            showDialog(
              context: context,
              barrierDismissible: false,
              builder: (BuildContext context) {
                return WillPopScope(
                  onWillPop: () {
                    return Future.delayed(Duration.zero).then(
                      (value) => false,
                    );
                  },
                  child: Center(
                    child: CircularProgressIndicator(
                      backgroundColor: ColorConstants.blue,
                    ),
                  ),
                );
              },
            );
            bool geoRes = await GeorestrictionService.gameplayAllowed(
              context,
              GameNames.poker,
              userId,
            );
            Navigator.of(
              context,
              rootNavigator: true,
            ).pop();
            if (geoRes) {
              usernameUpdateCount =
                  await SharedPrefService.getIntValuesFromSharedPref(
                SharedPrefKeys.usernameUpdateCount,
              );
              if (usernameUpdateCount != 0) {
                await getPokerUrl(
                  context: context,
                  userId: userId,
                );
              } else {
                showDialog(
                  context: context,
                  builder: (
                    BuildContext context,
                  ) {
                    return UpdateUsernameDialog(
                      header: "USERNAME",
                      description: "Enter Username to Update",
                      textInputType: TextInputType.name,
                      oldValue: username,
                      textEditingController: _dialogtextcontroller,
                      inputformatters: [
                        FilteringTextInputFormatter.allow(
                          RegExpMethods.alphaNumeric,
                        ),
                      ],
                      length: 25,
                    );
                  },
                ).whenComplete(
                  () => _dialogtextcontroller.clear(),
                );
              }
            }
          }
        }
        break;
      case GameNames.callBreak:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.callBreak,
            },
          );
          //CleverTap Events
          CleverTapPlugin.recordEvent(
            StringConstants.gameLaunched,
            {
              StringConstants.gameName: GameNames.callBreak,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.callBreak,
            userId: userId,
          );
        }
        break;
      case GameNames.streetRacer:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.streetRacer,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.streetRacer,
            userId: userId,
          );
        }
        break;
      case GameNames.runnerNumberOne:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.runnerNumberOne,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.runnerNumberOne,
            userId: userId,
          );
        }
        break;
      case GameNames.pool:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.pool,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.pool,
            userId: userId,
          );
        }
        break;
      case GameNames.ludo:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.ludo,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.ludo,
            userId: userId,
          );
        }
        break;
      case GameNames.candyCrush:
        {
          await FirebaseAnalyticsModel.analyticsLogEvent(
            eventName: GAME_CLICK_EVENT_FIREBASE,
            parameters: {
              "gameName": GameNames.candyCrush,
            },
          );
          await _askingPermission(
            context: context,
            game: GameNames.candyCrush,
            userId: userId,
          );
        }
        break;
      default:
        break;
    }
  }

  GameTileDm getGameTileDm(
    String game,
  ) {
    switch (game) {
      case GameNames.fantasy:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.fantasyDangal,
            gameName: "all_games_screen.fantasy".tr(),
          );
        }
        break;
      case GameNames.archery:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.archery,
            gameName: "all_games_screen.archery".tr(),
          );
        }
        break;
      case GameNames.bubbleShooter:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.bubbleShooter,
            gameName: "all_games_screen.bubble_shooter".tr(),
          );
        }
        break;
      case GameNames.knifeCut:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.knifeHit,
            gameName: "all_games_screen.knife_hit".tr(),
          );
        }
        break;
      case GameNames.fruitSplit:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.fruitSplit,
            gameName: "all_games_screen.fruit_split".tr(),
          );
        }
        break;
      case GameNames.carrom:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.carrom,
            gameName: "all_games_screen.carrom".tr(),
          );
        }
        break;
      case GameNames.rummy:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.rummy,
            gameName: "all_games_screen.rummy".tr(),
          );
        }
        break;
      case GameNames.poker:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.poker,
            gameName: "all_games_screen.poker".tr(),
          );
        }
        break;
      case GameNames.callBreak:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.callBreak,
            gameName: "all_games_screen.call_break".tr(),
          );
        }
        break;
      case GameNames.streetRacer:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.streetRacer,
            gameName: "all_games_screen.street_racer".tr(),
          );
        }
        break;
      case GameNames.runnerNumberOne:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.sumoRunner,
            gameName: "all_games_screen.runner".tr(),
          );
        }
        break;
      case GameNames.pool:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.pool,
            gameName: "all_games_screen.pool".tr(),
          );
        }
        break;
      case GameNames.ludo:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.poker,
            gameName: "all_games_screen.ludo".tr(),
          );
        }
        break;
      case GameNames.candyCrush:
        {
          return GameTileDm(
            gameIconPath: AssetPaths.candyClash,
            gameName: "all_games_screen.candy_clash".tr(),
          );
        }
        break;
      default:
        return GameTileDm(
          gameIconPath: StringConstants.emptyString,
          gameName: StringConstants.emptyString,
        );
        break;
    }
  }

  int countOfGames(
    GameType gameTitle,
  ) {
    if (gameTitle == GameType.cardGame) {
      return cardGamesList.length;
    } else if (gameTitle == GameType.casualGame) {
      return casualGamesList.length;
    } else {
      return 3;
    }
  }
}
