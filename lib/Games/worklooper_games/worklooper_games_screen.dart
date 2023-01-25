import 'dart:async';
import 'dart:ui';

import 'package:device_apps/device_apps.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/wallet_info_model.dart';
import '../../common_widgets/custom_add_reload_coin_dialog.dart';
import '../../common_widgets/custom_download_dialog.dart';
import '../../common_widgets/update_username_dialog.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../all_games_helper_service.dart';
import '../custom_launch_game_calls.dart';

class WorklooperGamesScreen extends StatefulWidget {
  final String gameName;
  final String userId;

  const WorklooperGamesScreen({
    Key key,
    @required this.gameName,
    @required this.userId,
  }) : super(key: key);

  @override
  _WorklooperGamesScreenState createState() => _WorklooperGamesScreenState();
}

class _WorklooperGamesScreenState extends State<WorklooperGamesScreen> {
  var lobbyInfo;
  String localPath, currentVersion, latestVersion, apkDownloadUrl;
  bool downloaded;
  int progress = 0;
  int percentage = 5;
  bool apkInstalled = false;
  int usernameUpdateCount = 0;
  String username = StringConstants.emptyString;
  final _dialogtextcontroller = TextEditingController();

  static const percentageStream = const EventChannel(
    'com.app.dangalgames/downloadEvents',
  );

  StreamSubscription downloadPercentageSubscription;

  @override
  void initState() {
    checkGameVersionAndDownloadApp(
      widget.gameName,
    );
    super.initState();
  }

  @override
  void dispose() {
    if (downloadPercentageSubscription != null) {
      downloadPercentageSubscription.cancel();
    }
    const platform = MethodChannel(
      'com.flutter.download',
    );
    platform.invokeMethod(
      "cancelDownload",
    );
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        backgroundColor: ColorConstants.transparent,
        elevation: 0,
        leading: IconButton(
          icon: Icon(
            Icons.arrow_back_ios,
            color: ColorConstants.black,
          ),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: MultiProvider(
        providers: [
          ChangeNotifierProvider(
            create: (context) => WalletInfoModel(),
          ),
        ],
        child: Container(
          height: Singleton().deviceSize.height,
          width: Singleton().deviceSize.width,
          decoration: BoxDecoration(
            image: DecorationImage(
              image: AssetImage(
                'assets/images/background.webp',
              ),
              fit: BoxFit.cover,
            ),
          ),
          child: Column(
            children: [
              Expanded(
                child: downloaded == null ||
                        lobbyInfo == null ||
                        downloaded == false
                    ? Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          CircularProgressIndicator(
                            backgroundColor: ColorConstants.grey,
                          ),
                          Visibility(
                            visible: downloaded == false,
                            child: Column(
                              children: [
                                SizedBox(
                                  height: 1.0.h,
                                ),
                                Text(
                                  "Downloading " +
                                      AllGamesHelperService
                                          .getGameNameToDisplay(
                                        widget.gameName,
                                      ) +
                                      " ...",
                                  style: TextStyle(
                                    color: ColorConstants.black,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 12.0.sp,
                                  ),
                                ),
                                SizedBox(
                                  height: 1.0.h,
                                ),
                                Text(
                                  progress.toString() + " %",
                                  style: TextStyle(
                                    color: ColorConstants.black,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 12.0.sp,
                                  ),
                                ),
                                SizedBox(
                                  height: 1.0.h,
                                ),
                                Text(
                                  "Please Wait",
                                  style: TextStyle(
                                    color: ColorConstants.black,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 12.0.sp,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      )
                    : Container(
                        child: ListView.builder(
                          shrinkWrap: true,
                          itemCount: lobbyInfo.length,
                          itemBuilder: (
                            BuildContext context,
                            int index,
                          ) =>
                              Column(
                            children: [
                              Container(
                                width: 85.0.w,
                                decoration: BoxDecoration(
                                  color: ColorConstants.white,
                                  borderRadius: BorderRadius.circular(
                                    2.0.w,
                                  ),
                                ),
                                child: Row(
                                  children: [
                                    Expanded(
                                      flex: 4,
                                      child: Column(
                                        children: [
                                          SizedBox(
                                            height: 1.5.h,
                                          ),
                                          Text(
                                            lobbyInfo[index]['name'],
                                            style: TextStyle(
                                              color: ColorConstants.blue,
                                              fontWeight: FontWeight.bold,
                                              fontSize: 11.0.sp,
                                            ),
                                          ),
                                          lobbyInfo[index]['type'] == "CASH"
                                              ? Center(
                                                  child: Text(
                                                    '\u{20B9}' +
                                                        " " +
                                                        lobbyInfo[index]
                                                                ['prize']
                                                            .toString(),
                                                    style: TextStyle(
                                                      color: ColorConstants.red,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                      fontSize: 11.0.sp,
                                                    ),
                                                  ),
                                                )
                                              : Row(
                                                  children: [
                                                    Spacer(),
                                                    Container(
                                                      height: 2.5.h,
                                                      child: Image.asset(
                                                        'assets/images/coins_icon.png',
                                                        color:
                                                            ColorConstants.red,
                                                      ),
                                                    ),
                                                    Text(
                                                      " " +
                                                          lobbyInfo[index]
                                                                  ['prize']
                                                              .toString(),
                                                      style: TextStyle(
                                                        color:
                                                            ColorConstants.red,
                                                        fontWeight:
                                                            FontWeight.bold,
                                                        fontSize: 11.0.sp,
                                                      ),
                                                    ),
                                                    Spacer(),
                                                  ],
                                                ),
                                          Text(
                                            "Prize",
                                            style: TextStyle(
                                              fontSize: 8.0.sp,
                                              color: ColorConstants.grey700,
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                          SizedBox(
                                            height: 1.5.h,
                                          ),
                                        ],
                                      ),
                                    ),
                                    Expanded(
                                      flex: 3,
                                      child: Row(
                                        children: [
                                          Container(
                                            decoration: BoxDecoration(
                                              border: Border.all(
                                                color: ColorConstants.grey,
                                              ),
                                            ),
                                            child: Padding(
                                              padding: const EdgeInsets.all(
                                                2.0,
                                              ),
                                              child: Text(
                                                lobbyInfo[index]
                                                            ['allowedPlayers']
                                                        .toString() +
                                                    "P",
                                                style: TextStyle(
                                                  color: ColorConstants.grey,
                                                  fontSize: 7.0.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                          SizedBox(
                                            width: 1.5.w,
                                          ),
                                          Column(
                                            children: [
                                              SizedBox(
                                                height: 2.0.h,
                                              ),
                                              Text(
                                                "Winner",
                                                style: TextStyle(
                                                  fontWeight: FontWeight.w900,
                                                  fontSize: 12.0.sp,
                                                ),
                                              ),
                                              Row(
                                                children: [
                                                  Image.asset(
                                                    'assets/images/winner.png',
                                                    height: 1.3.h,
                                                  ),
                                                  SizedBox(
                                                    width: 0.5.w,
                                                  ),
                                                  Text(
                                                    lobbyInfo[index][
                                                            "prizeDistributionRatio"]
                                                        .length
                                                        .toString(),
                                                    style: TextStyle(
                                                      fontSize: 9.0.sp,
                                                      fontWeight:
                                                          FontWeight.w900,
                                                    ),
                                                  ),
                                                ],
                                              ),
                                              SizedBox(
                                                height: 2.0.h,
                                              ),
                                            ],
                                          ),
                                        ],
                                      ),
                                    ),
                                    Expanded(
                                      flex: 4,
                                      child: Align(
                                        alignment: Alignment.center,
                                        child: Column(
                                          children: [
                                            GestureDetector(
                                              onTap: () async {
                                                var param = {
                                                  "lobby_id": lobbyInfo[index]
                                                      ["lobbyId"],
                                                  "player_id":
                                                      await SharedPrefService
                                                          .getStringValuesFromSharedPref(
                                                    SharedPrefKeys.userID,
                                                  ),
                                                  "player_username":
                                                      await SharedPrefService
                                                          .getStringValuesFromSharedPref(
                                                    SharedPrefKeys.userName,
                                                  ),
                                                  "profile_url":
                                                      await SharedPrefService
                                                          .getStringValuesFromSharedPref(
                                                    SharedPrefKeys.lrAvatarURL,
                                                  ),
                                                  "total_winning_amount":
                                                      lobbyInfo[index]["prize"]
                                                };
                                                if (lobbyInfo[index]["type"] !=
                                                    "CASH") {
                                                  var chips = double.parse(
                                                    await SharedPrefService
                                                        .getStringValuesFromSharedPref(
                                                      SharedPrefKeys
                                                          .chipsAmount,
                                                    ),
                                                  );
                                                  if (chips <
                                                      lobbyInfo[index]
                                                          ['entryFee']) {
                                                    showDialog(
                                                      context: context,
                                                      builder: (BuildContext
                                                          context) {
                                                        return CustomAddReloadCoinDialog(
                                                          userId: widget.userId,
                                                        );
                                                      },
                                                    );
                                                  } else {
                                                    openGame(
                                                      param,
                                                      index,
                                                      widget.gameName,
                                                    );
                                                  }
                                                } else {
                                                  usernameUpdateCount =
                                                      await SharedPrefService
                                                          .getIntValuesFromSharedPref(
                                                    SharedPrefKeys
                                                        .usernameUpdateCount,
                                                  );
                                                  if (usernameUpdateCount !=
                                                      0) {
                                                    showDialog(
                                                      context: context,
                                                      barrierDismissible: false,
                                                      builder: (BuildContext
                                                          context) {
                                                        return WillPopScope(
                                                          onWillPop: () {
                                                            return Future
                                                                .delayed(
                                                              Duration.zero,
                                                            ).then(
                                                              (value) => false,
                                                            );
                                                          },
                                                          child: Center(
                                                            child:
                                                                CircularProgressIndicator(
                                                              backgroundColor:
                                                                  ColorConstants
                                                                      .blue,
                                                            ),
                                                          ),
                                                        );
                                                      },
                                                    );
                                                    await checkGameBalance(
                                                      lobbyInfo[index]
                                                          ['entryFee'],
                                                      param,
                                                      index,
                                                      widget.gameName,
                                                    );
                                                  } else {
                                                    showDialog(
                                                      context: context,
                                                      builder: (BuildContext
                                                          context) {
                                                        return UpdateUsernameDialog(
                                                          header: "USERNAME",
                                                          description:
                                                              "Enter Username to Update",
                                                          textInputType:
                                                              TextInputType
                                                                  .name,
                                                          oldValue: username,
                                                          textEditingController:
                                                              _dialogtextcontroller,
                                                          inputformatters: [
                                                            FilteringTextInputFormatter
                                                                .allow(
                                                              RegExpMethods
                                                                  .alphaNumeric,
                                                            ),
                                                          ],
                                                          length: 25,
                                                        );
                                                      },
                                                    ).whenComplete(
                                                      () =>
                                                          _dialogtextcontroller
                                                              .clear(),
                                                    );
                                                  }
                                                }
                                              },
                                              child: Container(
                                                height: 5.0.h,
                                                width: 16.0.w,
                                                decoration: BoxDecoration(
                                                  gradient: ColorConstants
                                                      .blueDarkGradient,
                                                  borderRadius:
                                                      BorderRadius.circular(
                                                    2.0.w,
                                                  ),
                                                ),
                                                child: lobbyInfo[index]
                                                            ['type'] ==
                                                        "CASH"
                                                    ? Center(
                                                        child: Text(
                                                          '\u{20B9}' +
                                                              lobbyInfo[index][
                                                                      'entryFee']
                                                                  .toString(),
                                                          style: TextStyle(
                                                            color:
                                                                ColorConstants
                                                                    .white,
                                                            fontWeight:
                                                                FontWeight.bold,
                                                            fontSize: 8.0.sp,
                                                          ),
                                                        ),
                                                      )
                                                    : Row(
                                                        children: [
                                                          Spacer(),
                                                          Container(
                                                            height: 2.5.h,
                                                            child: Image.asset(
                                                              'assets/images/coins_icon.png',
                                                            ),
                                                          ),
                                                          Text(
                                                            " " +
                                                                lobbyInfo[index]
                                                                        [
                                                                        'entryFee']
                                                                    .toString(),
                                                            style: TextStyle(
                                                              color:
                                                                  ColorConstants
                                                                      .white,
                                                              fontWeight:
                                                                  FontWeight
                                                                      .bold,
                                                              fontSize: 8.0.sp,
                                                            ),
                                                          ),
                                                          Spacer(),
                                                        ],
                                                      ),
                                              ),
                                            ),
                                            percentage == 0 ||
                                                    lobbyInfo[index]['type'] !=
                                                        "CASH"
                                                ? SizedBox.shrink()
                                                : Column(
                                                    children: [
                                                      SizedBox(
                                                        height: 0.5.h,
                                                      ),
                                                      Text(
                                                        'Use ' +
                                                            '\u{20B9}' +
                                                            ' ' +
                                                            (lobbyInfo[index][
                                                                        'entryFee'] *
                                                                    percentage /
                                                                    100)
                                                                .toString() +
                                                            " Game chips",
                                                        overflow: TextOverflow
                                                            .ellipsis,
                                                        style: TextStyle(
                                                          color: ColorConstants
                                                              .grey700,
                                                          fontSize: 8.0.sp,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                              SizedBox(
                                height: 1.5.h,
                              ),
                            ],
                          ),
                        ),
                      ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  //METHODS
  Future<void> downLoadButtonClickCallback(
    gameName,
  ) async {
    await uninstall(
      gameName,
    );
  }

  gameNotInstalledCallback(
    String gameName,
  ) {
    appDownload(
      gameName,
    );
  }

  void checkGameVersionAndDownloadApp(
    String gameName,
  ) async {
    bool isInstalled = await CommonMethods.checkIfPackageIsInstalled(
      AllGamesHelperService.getGamePackageName(
        gameName,
      ),
    );
    if (isInstalled) {
      downloaded = true;
    }
    await getGameLobbyAndDownloadUpdateStatus(
      gameName,
      isInstalled,
    );
    await findLocalPath();
    percentage = await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.workLooperPercent,
        ) ??
        0;
    usernameUpdateCount = await SharedPrefService.getIntValuesFromSharedPref(
      SharedPrefKeys.usernameUpdateCount,
    );
    username = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );
    setState(
      () {
        percentage = percentage;
      },
    );

    if (!isInstalled) {
      await appDownload(
        gameName,
      );
    }
  }

  getGameLobbyAndDownloadUpdateStatus(
    String gameName,
    bool installed,
  ) async {
    Map<String, dynamic> responseMap =
        await AllGamesHelperService.getGameLobbyApi(
      context,
      installed,
      gameName,
      widget.userId,
    );
    if (responseMap.containsKey('result') &&
        responseMap['result'] == ResponsesKeys.SUCCESS) {
      setState(
        () {
          lobbyInfo = responseMap["data"];
          latestVersion = responseMap["APK_Version"].toString();
          apkDownloadUrl = responseMap["apkDownloadUrl"];
        },
      );
      if (installed) {
        Application app = await DeviceApps.getApp(
          AllGamesHelperService.getGamePackageName(
            gameName,
          ),
        );
        setState(
          () {
            downloaded = true;
            currentVersion = app.versionName;
          },
        );
        await checkAppUpdate(
          gameName,
          currentVersion,
          latestVersion,
        );
      }
    }
  }

  Future<void> checkAppUpdate(
    gameName,
    String currentVersion,
    String latestVersion,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Current version:-> " + currentVersion,
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Latest version:-> " + latestVersion,
    );
    try {
      if (double.parse(currentVersion) <
          double.parse(
            latestVersion,
          )) {
        showDialog(
          context: context,
          barrierDismissible: false, // user must tap button!
          builder: (BuildContext context) {
            return CustomDownloadDialog(
              downLoadButtonClickCallback: downLoadButtonClickCallback,
              gameName: gameName,
            );
          },
        );
      }
    } catch (e) {}
  }

  Future<void> uninstall(
    String gameName,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Uninstalling...",
    );
    const platform = MethodChannel(
      'com.flutter.uninstall',
    );
    String package = AllGamesHelperService.getGamePackageName(
      gameName,
    );

    var uninstallResult = await platform.invokeMethod(
      "Uninstall",
      {
        "package": package,
      },
    );
    if (uninstallResult == "Cancelled") {
      checkAppUpdate(
        gameName,
        currentVersion,
        latestVersion,
      );
    } else {
      await appDownload(
        gameName,
      );
    }
  }

  findLocalPath() async {
    final directory = await getExternalStorageDirectory();
    CommonMethods.printLog(
      "Path for Downloading",
      directory.path.toString(),
    );
    setState(
      () {
        localPath = directory.path.toString();
      },
    );
  }

  checkGameBalance(
    var entryFee,
    Map param,
    int index,
    String gameName,
  ) async {
    String result = await AllGamesHelperService.checkGameBalance(
      context,
      entryFee,
      param,
      index,
      gameName,
      widget.userId,
    );

    setState(
      () {
        FocusScope.of(context).unfocus();
      },
    );
    if (result == ResponsesKeys.SUCCESS) {
      openGame(
        param,
        index,
        gameName,
      );
    }
  }

  appDownload(String gameName) async {
    setState(
      () {
        downloaded = false;
      },
    );
    const platform = MethodChannel(
      'com.flutter.download',
    );
    await platform.invokeMethod(
      "apkDownload",
      {
        "downloadUrl": apkDownloadUrl,
        "gameName": gameName,
        "fileName": AllGamesHelperService.getGameInstallApk(
          gameName,
        )
      },
    );
    downloadPercentageSubscription =
        percentageStream.receiveBroadcastStream().listen(
      (event) {
        int currentDownloadedBytes = int.parse(
          event.split(" ")[0],
        );
        int totalDownloadedBytes = int.parse(
          event.split(" ")[1],
        );
        progress = (currentDownloadedBytes * 100) ~/ totalDownloadedBytes;
        setState(
          () {},
        );
        if (progress == 100) {
          appInstall();
        }
      },
    );
  }

  void appInstall() async {
    setState(
      () {
        downloaded = true;
      },
    );
    AllGamesHelperService.allGamesAppInstall(
      widget.gameName,
    );
  }

  Future<void> openGame(
    var lobbyJson,
    int index,
    String gameName,
  ) async {
    CustomLaunchGameCalls(
      this.gameNotInstalledCallback,
    ).openGame(
      lobbyJson,
      index,
      lobbyInfo,
      gameName,
    );
  }
}
