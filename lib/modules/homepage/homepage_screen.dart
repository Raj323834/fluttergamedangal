import 'dart:convert';
import 'dart:developer';
import 'dart:ui';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

import '../../Games/poker/poker_web_view.dart';
import '../../Model/dg_apk_download_model.dart';
import '../../Model/firebase_analytics_model.dart';
import '../../Network/GameHelperService/poker_helper_service.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/geo_restriction_service.dart';
import '../../Network/web_socket_helper_service.dart';
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
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/games_data_source.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../home/models/home_banners_dm.dart';
import '../home/models/home_banners_ws_dm.dart';
import '../home/repos/fetch_banners_repo.dart';
import 'widgets/banners.dart';
import 'widgets/rummy_or_poker_widget.dart';

class HomePage extends StatefulWidget {
  final void Function(
    int newIndex,
    String code,
  ) changeFromDealsIndex;

  final void Function(
    FlutterWebviewPlugin webviewInstance,
  ) webViewInstance;

  final String routeGames;
  final String userId;

  HomePage(this.changeFromDealsIndex, this.webViewInstance, this.routeGames,
      this.userId,
      {Key key})
      : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with WidgetsBindingObserver {
  String userId = '';
  var flutterWebviewPlugin = FlutterWebviewPlugin();
  TextEditingController _dialogtextcontroller = TextEditingController();
  int usernameUpdateCount = 0;
  String userName = '';
  int numberOfBanners;
  int numberOfBannersPS;
  String leagueCode = StringConstants.emptyString;
  List<Banners> listOfBanners;
  bool isFetched = false;

  @override
  void initState() {
    WidgetsBinding.instance.addObserver(this);
    getUserId();
    if (Singleton().listOfBanners.isEmpty) {
      callApi();
    } else {
      setState(
        () {
          listOfBanners = Singleton().listOfBanners;
          isFetched = true;
        },
      );
    }

    super.initState();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      listOfBanners = Singleton().listOfBanners;
      isFetched = true;
    }
  }

  void getUserId() async {
    userId = widget.userId;
    usernameUpdateCount = await SharedPrefService.getIntValuesFromSharedPref(
      SharedPrefKeys.usernameUpdateCount,
    );
    userId = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userID,
    );
  }

  callApi() async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showCustomDialog(
        context: context,
        error: StringConstants.noInternetConnection,
        title: StringConstants.emptyString,
      );
    }
    var repoObj = FetchBannersRepo();
    HomeBannersDM homeBannersDM = await repoObj.fetchBanners(
      userId: widget.userId,
    );
    if (homeBannersDM != null) {
      switch (homeBannersDM.result) {
        case ResponseStatus.success:
          Singleton().listOfBanners = homeBannersDM.banners;
          listOfBanners = Singleton().listOfBanners;

          setState(
            () {
              isFetched = true;
            },
          );
          break;

        case ResponseStatus.dbError:
          CommonMethods.showSnackBar(
            context,
            "Server Error. Unable to fetch banners",
          );
          break;

        case ResponseStatus.notFound:
          CommonMethods.showSnackBar(
            context,
            "No Banners Available",
          );
          break;

        case ResponseStatus.tokenExpired:
          bool accessTokenGenerated =
              await GenerateAccessToken.regenerateAccessToken(
            widget.userId,
          );
          if (accessTokenGenerated) {
            await callApi();
          }
          break;
        default:
      }
    } else {
      CommonMethods.showSnackBar(context, 'Something Went Wrong');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ColorConstants.kBackgroundColor,
      body: SingleChildScrollView(
        child: Container(
          margin: EdgeInsets.only(
            top: 10,
          ),
          child: StreamBuilder(
              stream: sockets.streamController.stream,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  var snapBody = jsonDecode(snapshot.data);
                  if (snapBody['type'] == 'sm-banner-change' &&
                      snapBody['source'] == FlavorInfo.source &&
                      snapBody['bannerType'] == 'HOME') {
                    if (snapBody['banners'] != null) {
                      var homeBannersWSDM = HomeBannerWSDM.fromJson(
                        snapBody,
                      );
                      Singleton().listOfBanners = homeBannersWSDM.banners;
                      listOfBanners = Singleton().listOfBanners;
                    } else {
                      callApi();
                    }
                  }
                }
                return Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    //Carousel
                    (isFetched)
                        ? BannersUI(
                            listOfBanners: listOfBanners,
                          )
                        : BannersUI(
                            listOfBanners: listOfBanners,
                            isDummy: true,
                          ),
                    SizedBox(
                      height: 10,
                    ),
                    FlavorInfo.isPS || FlavorInfo.isCoInWithoutPoker
                        ?
                        //Fantasy
                        RummyOrPokerWidget(
                            path: AssetPaths.fantasyHome,
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
                                GamesDataSource(
                                  context: context,
                                  changeFromDealsIndex:
                                      widget.changeFromDealsIndex,
                                  flutterWebviewInstance:
                                      widget.webViewInstance,
                                  routeGames: widget.routeGames,
                                  userId: userId,
                                ).onTapGame(
                                  gameType: GameType.sportsGame,
                                  index: 0,
                                  userId: userId,
                                );
                              }
                            },
                            isRummy: false,
                          )
                        :
                        //Poker
                        RummyOrPokerWidget(
                            path: (DateTime.now().isBefore(
                                      DateTime(
                                        2022,
                                        10,
                                        3,
                                      ),
                                    ) ||
                                    DateTime.now().isAfter(
                                      DateTime(
                                        2022,
                                        10,
                                        16,
                                        23,
                                        59,
                                        59,
                                      ),
                                    ))
                                ? AssetPaths.pokerHome
                                : 'assets/images/poker_dds.webp',
                            onTap: () async {
                              //FireBase Analytics
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
                              //Fetch Username
                              var username = await SharedPrefService
                                  .getStringValuesFromSharedPref(
                                SharedPrefKeys.userName,
                              );
                              if (username == StringConstants.emptyString ||
                                  username == null) {
                                showDialog(
                                  context: context,
                                  builder: (_) {
                                    return CompleteProfileDialog(
                                      userId: userId,
                                    );
                                  },
                                );
                              } else {
                                //Starting Poker
                                CommonMethods.showSnackBar(
                                  context,
                                  StringConstants.pleaseWait,
                                );
                                showCircularProgressIndicator(
                                  context,
                                );
                                bool geoRes =
                                    await GeorestrictionService.gameplayAllowed(
                                  context,
                                  GameNames.poker,
                                  userId,
                                );
                                Navigator.of(
                                  context,
                                  rootNavigator: true,
                                ).pop();
                                if (geoRes) {
                                  usernameUpdateCount = await SharedPrefService
                                      .getIntValuesFromSharedPref(
                                    SharedPrefKeys.usernameUpdateCount,
                                  );
                                  if (usernameUpdateCount != 0) {
                                    await getPokerUrl(
                                      context: context,
                                      userID: userId,
                                      flutterWebviewPlugin:
                                          flutterWebviewPlugin,
                                    );
                                  } else {
                                    showDialog(
                                      context: context,
                                      builder: (_) {
                                        return UpdateUsernameDialog(
                                          header: "USERNAME",
                                          description:
                                              "Enter Username to Update",
                                          textInputType: TextInputType.name,
                                          oldValue: userName,
                                          textEditingController:
                                              _dialogtextcontroller,
                                          inputformatters: [
                                            FilteringTextInputFormatter.allow(
                                              RegExpMethods.alphaNumeric,
                                            )
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
                            },
                            isRummy: false,
                          ),
                    //Rummy
                    RummyOrPokerWidget(
                      path: (DateTime.now().isBefore(
                                DateTime(
                                  2022,
                                  10,
                                  3,
                                ),
                              ) ||
                              DateTime.now().isAfter(
                                DateTime(
                                  2022,
                                  10,
                                  31,
                                  23,
                                  59,
                                  59,
                                ),
                              ))
                          ? 'assets/images/RummyIcon.webp'
                          : 'assets/images/rummy_dds.webp',
                      onTap: () async {
                        //FireBase Analytics
                        await FirebaseAnalyticsModel.analyticsLogEvent(
                          eventName: GAME_CLICK_EVENT_FIREBASE,
                          parameters: {
                            "gameName": GameNames.rummy,
                          },
                        );
                        //CleverTap Events
                        CleverTapPlugin.recordEvent(
                          StringConstants.gameLaunched,
                          {
                            StringConstants.gameName: GameNames.rummy,
                          },
                        );
                        //Starting Rummy
                        showCircularProgressIndicator(
                          context,
                        );
                        bool geoRes =
                            await GeorestrictionService.gameplayAllowed(
                          context,
                          GameNames.rummy,
                          userId,
                        );
                        Navigator.of(
                          context,
                          rootNavigator: true,
                        ).pop();
                        if (geoRes) {
                          usernameUpdateCount = await SharedPrefService
                              .getIntValuesFromSharedPref(
                            SharedPrefKeys.usernameUpdateCount,
                          );
                          if (usernameUpdateCount != 0) {
                            const platform = MethodChannel(
                              UrlConstants.rummyMethodChannel,
                            );
                            String userName = await SharedPrefService
                                .getStringValuesFromSharedPref(
                              SharedPrefKeys.userName,
                            );
                            String assetImage = await SharedPrefService
                                .getStringValuesFromSharedPref(
                              SharedPrefKeys.lrAvatarURL,
                            );
                            await SharedPrefService.addBoolToSharedPref(
                              SharedPrefKeys.rummyOpened,
                              true,
                            );
                            CommonMethods.devLog(
                              'Sending UserId To Rummy: $userId',
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
                              widget.changeFromDealsIndex(
                                FlavorInfo.isPS
                                    ? AppConstants.addCashPS
                                    : AppConstants.addCash,
                                StringConstants.emptyString,
                              );
                            }
                          } else {
                            showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return UpdateUsernameDialog(
                                  header: "USERNAME",
                                  description: "Enter Username to Update",
                                  textInputType: TextInputType.name,
                                  oldValue: userName,
                                  textEditingController: _dialogtextcontroller,
                                  inputformatters: [
                                    FilteringTextInputFormatter.allow(
                                      RegExpMethods.alphaNumeric,
                                    )
                                  ],
                                  length: 25,
                                );
                              },
                            ).whenComplete(
                              () => _dialogtextcontroller.clear(),
                            );
                          }
                        }
                      },
                      isRummy: true,
                    )
                  ],
                );
              }),
        ),
      ),
    );
  }

  //Methods
  Future<void> getPokerUrl({
    @required BuildContext context,
    @required String userID,
    @required FlutterWebviewPlugin flutterWebviewPlugin,
  }) async {
    //Getting User Details
    String firstName = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.firstName,
    );
    String username = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );
    String avatarUrl = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.pokerAvatarUrl,
    );
    //Launching Poker
    Map<String, Object> result = await PokerHelperService.pokerLaunch(
      username,
      firstName,
      "MOBILE",
      avatarUrl,
      userID,
    );
    CommonMethods.printLog(
      "result POKER URL---- >",
      result.toString(),
    );
    if (result.containsKey(
      'noInternet',
    )) {
      Navigator.of(context).pop();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      showDialog(
        context: context,
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
              data['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userID,
        );
        if (accessTokenGenerated) {
          await getPokerUrl(
            context: context,
            userID: userID,
            flutterWebviewPlugin: flutterWebviewPlugin,
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
                  Navigator.of(context, rootNavigator: true).pop();
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
                  widget.changeFromDealsIndex(
                    2,
                    StringConstants.emptyString,
                  );
                  DgApkDownloadModel.checkUpdate();
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
                } else if (message.message.contains("tour_clevertap")) {
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
              Singleton().deviceSize.width,
              Singleton().deviceSize.height,
            ),
          );
          flutterWebviewPlugin.show();
          //Navigating To Poker Webview
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

  Future<dynamic> showCircularProgressIndicator(
    BuildContext context,
  ) {
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) {
        return Center(
          child: CircularProgressIndicator(
            backgroundColor: ColorConstants.blue,
          ),
        );
      },
    );
  }
}
