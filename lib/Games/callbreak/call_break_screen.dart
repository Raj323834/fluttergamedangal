import 'dart:async';
import 'dart:convert';
import 'dart:convert' as convert;
import 'dart:io';
import 'dart:ui';

import 'package:android_intent/android_intent.dart';
import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:device_apps/device_apps.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:http/http.dart' as http;
import 'package:location/location.dart';
import 'package:path_provider/path_provider.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/wallet_info_model.dart';
import '../../Network/GameHelperService/artoon_helper_service.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/wallet_service.dart';
import '../../common_widgets/update_username_dialog.dart';
import '../../constants/app_constants.dart';
import '../../constants/color_constants.dart';
import '../../constants/game_names.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../modules/home/home.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';

class CallBreakScreen extends StatefulWidget {
  final String userId;

  const CallBreakScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  _CallBreakScreenState createState() => _CallBreakScreenState();
}

class _CallBreakScreenState extends State<CallBreakScreen> {
  var lobbyInfo;
  String localPath, currentVersion, latestVersion;
  bool downloaded;
  int progress = 0;
  int percentage = 0;

  int usernameUpdateCount = 0;
  String username = StringConstants.emptyString;
  final _dialogtextcontroller = TextEditingController();
  static const percentageStream = const EventChannel(
    'com.app.dangalgames/downloadEvents',
  );

  StreamSubscription downloadPercentageSubscription;

  Location location = Location();

  @override
  void initState() {
    asyncMethod();
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
                child: downloaded == null || lobbyInfo == null
                    ? Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          CircularProgressIndicator(
                            backgroundColor: ColorConstants.grey,
                          ),
                          Visibility(
                            visible: downloaded == null,
                            child: Column(
                              children: [
                                SizedBox(
                                  height: 1.0.h,
                                ),
                                Text(
                                  "Downloading Callbreak...",
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
                                            overflow: TextOverflow.ellipsis,
                                            textAlign: TextAlign.center,
                                            style: TextStyle(
                                              color: ColorConstants.blue,
                                              fontWeight: FontWeight.bold,
                                              fontSize: 11.0.sp,
                                            ),
                                          ),
                                          lobbyInfo[index]['isRealMoneyGame']
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
                                      child: Column(
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
                                          Text(
                                            lobbyInfo[index]
                                                    ["prizeDistributions"]
                                                .length
                                                .toString(),
                                            style: TextStyle(
                                              fontSize: 8.0.sp,
                                              fontWeight: FontWeight.w900,
                                            ),
                                          ),
                                          SizedBox(
                                            height: 2.0.h,
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
                                                if (!await location
                                                    .serviceEnabled()) {
                                                  location.requestService();
                                                  return;
                                                }
                                                lobbyInfo[index]["player_id"] =
                                                    await SharedPrefService
                                                        .getStringValuesFromSharedPref(
                                                  SharedPrefKeys.userID,
                                                );
                                                lobbyInfo[index]
                                                        ["player_name"] =
                                                    await SharedPrefService
                                                        .getStringValuesFromSharedPref(
                                                  SharedPrefKeys.userName,
                                                );
                                                lobbyInfo[index]
                                                        ["profile_pic"] =
                                                    await SharedPrefService
                                                        .getStringValuesFromSharedPref(
                                                  SharedPrefKeys.lrAvatarURL,
                                                );
                                                CommonMethods.printLog(
                                                  "Call break game open",
                                                  lobbyInfo[index].toString(),
                                                );
                                                if (lobbyInfo[index]
                                                    ['isRealMoneyGame']) {
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
                                                    await checkCallbreakBalance(
                                                        lobbyInfo[index],
                                                        lobbyInfo[index]['_id'],
                                                        "Callbreak",
                                                        lobbyInfo[index]
                                                                ['entryFee']
                                                            .toString(),
                                                        widget.userId);
                                                  } else {
                                                    //showDialogToUpdateUSername
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
                                                        }).whenComplete(
                                                      () =>
                                                          _dialogtextcontroller
                                                              .clear(),
                                                    );
                                                  }
                                                } else {
                                                  double gameChips = double.parse(
                                                      await SharedPrefService
                                                          .getStringValuesFromSharedPref(
                                                    SharedPrefKeys.chipsAmount,
                                                  ));
                                                  if (gameChips >=
                                                      lobbyInfo[index]
                                                          ["entryFee"]) {
                                                    openCallbreak(
                                                      lobbyInfo[index],
                                                    );
                                                  } else {
                                                    showDialog(
                                                      context: context,
                                                      builder: (BuildContext
                                                          context) {
                                                        return addReloadCoinDialogBox(
                                                          context,
                                                        );
                                                      },
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
                                                        ['isRealMoneyGame']
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
                                                    !lobbyInfo[index]
                                                        ['isRealMoneyGame']
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
                                                        style: TextStyle(
                                                          color: ColorConstants
                                                              .grey700,
                                                          fontSize: 8.0.sp,
                                                        ),
                                                        overflow: TextOverflow
                                                            .ellipsis,
                                                      ),
                                                    ],
                                                  )
                                          ],
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                              SizedBox(
                                height: 1.5.h,
                              )
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
  void asyncMethod() async {
    await _findLocalPath();
    percentage = await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.callBreakPercent,
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
    bool isInstalled = await DeviceApps.isAppInstalled(
      "com.callbreak",
    );
    if (!isInstalled) {
      await appDownload();
    } else {
      Application app = await DeviceApps.getApp(
        'com.callbreak',
      );
      setState(
        () {
          downloaded = true;
          currentVersion = app.versionName;
        },
      );
    }
    await callBreakApi(
      isInstalled,
      widget.userId,
    );
  }

  callBreakApi(
    bool installed,
    String userId,
  ) async {
    String accessToken = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.accessToken,
    );
    try {
      final response = await http.get(
        Uri.parse(
          UrlConstants.callBreakLobbyUrl,
        ),
        headers: {
          HttpHeaders.contentTypeHeader: "application/json",
          'token': FlavorConfig.instance.variables["callbreakToken"],
          HttpHeaders.authorizationHeader: "Bearer $accessToken",
          "identifier": userId != null ? userId : StringConstants.emptyString,
          StringConstants.source: FlavorInfo.source.toString()
        },
      ).timeout(
        Duration(
          seconds: 20,
        ),
      );
      Map<String, dynamic> responseMap = json.decode(
        response.body,
      );
      CommonMethods.printLog(
        "CallBreakLobby",
        responseMap.toString(),
      );

      if (responseMap.containsKey('error') && responseMap['error'] == "true") {
        CommonMethods.showSnackBar(
          context,
          "Server error",
        );
        Navigator.pop(context);
      } else if (responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await callBreakApi(
            installed,
            userId,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        setState(
          () {
            lobbyInfo = responseMap["data"]["list"];
            latestVersion = responseMap["apkVersion"].toString();
          },
        );
        if (installed) {
          await checkupdate(
            currentVersion,
            latestVersion,
          );
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          "Server error",
        );
        Navigator.pop(context);
      }
    } catch (e) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        e.toString(),
      );
      CommonMethods.showSnackBar(
        context,
        "Server Error!",
      );
      Navigator.pop(context);
    }
  }

  Future<void> checkupdate(
    String currentVersion,
    String latestVersion,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Current callbreak version:-> " + currentVersion,
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Latest callbreak version:-> " + latestVersion,
    );
    if (double.parse(
          currentVersion,
        ) <
        double.parse(
          latestVersion,
        )) {
      showCustomDownload(context);
    }
  }

  Future<void> uninstall() async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "Uninstalling...",
    );
    const platform = MethodChannel(
      'com.flutter.uninstall',
    );
    String package = 'com.callbreak';

    var uninstallResult = await platform.invokeMethod(
      "Uninstall",
      {
        "package": package,
      },
    );
    if (uninstallResult == "Cancelled") {
      checkupdate(
        currentVersion,
        latestVersion,
      );
    } else {
      await appDownload();
    }
  }

  _findLocalPath() async {
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

  checkCallbreakBalance(
    dynamic lobbyInfo,
    String lobbyId,
    String game,
    String entryFee,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "checkCallbreakBalance",
    );
    Map<String, Object> result = await ArtoonHelperService.getBalanceCheck(
      lobbyId,
      game,
      entryFee,
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
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "Something went wrong. Try again",
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await checkCallbreakBalance(
            lobbyInfo,
            lobbyId,
            game,
            entryFee,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        openCallbreak(lobbyInfo);
        setState(
          () {
            FocusScope.of(context).unfocus();
          },
        );
      } else if (data['result'] == ResponsesKeys.INSUFFICIENT_BALANCE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.printLog(
          "TAG",
          "INSUFFICIENT_BALANCE",
        );
        showDialog(
          context: context,
          builder: (BuildContext context) {
            return addCashDialogBox(context);
          },
        );
        setState(
          () {
            FocusScope.of(context).unfocus();
          },
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.printLog(
          "TAG",
          "DB_ERROR",
        );
        CommonMethods.showSnackBar(
          context,
          "There was some issue. Try after some time.",
        );
        setState(
          () {
            FocusScope.of(context).unfocus();
          },
        );
      } else if (data['result'] == ResponsesKeys.WALLET_SERVICE_NOT_REACHABLE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.printLog(
          "TAG",
          "WALLET_SERVICE_NOT_REACHABLE",
        );
        CommonMethods.showSnackBar(
          context,
          "Unable to fetch Wallet Info",
        );
        setState(
          () {
            FocusScope.of(context).unfocus();
          },
        );
      } else if (data['result'] == ResponsesKeys.RESTRICTED_ACTIVITY) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
      } else if (data['result'] == ResponsesKeys.ACTIVITY_BLOCKED_FOR_USER) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
      } else if (data['result'] == ResponsesKeys.GAME_BLOCKED_FOR_USER) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.showSnackBar(
          context,
          StringConstants.restrictedActivityMessage,
        );
      } else {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        CommonMethods.showSnackBar(
          context,
          "Problem in launching app",
        );
      }
    }
  }

  Widget addCashDialogBox(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: Dialog(
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Container(
          height: 20.0.h,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(
              2.0.w,
            ),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              SizedBox(
                height: 4.0.h,
              ),
              Text(
                "You must Add Cash before \n playing the game",
                style: TextStyle(
                  fontSize: 13.5.sp,
                  color: ColorConstants.grey700,
                  fontWeight: FontWeight.bold,
                ),
              ),
              SizedBox(
                height: 2.5.h,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                      Navigator.pushAndRemoveUntil(
                        context,
                        CupertinoPageRoute(
                          builder: (BuildContext context) => HomeScreen(
                            landingPage: AppConstants.addCash,
                            routeDetail: StringConstants.emptyString,
                            userId: widget.userId,
                          ),
                        ),
                        (route) => false,
                      );
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        gradient: ColorConstants.blueDarkGradient,
                        borderRadius: BorderRadius.circular(5),
                      ),
                      child: Center(
                        child: Text(
                          "Add Cash",
                          style: TextStyle(
                            color: ColorConstants.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 2.0.w,
                  ),
                  GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        border: Border.all(
                          color: ColorConstants.grey,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "Go Back",
                          style: TextStyle(
                            color: ColorConstants.grey,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  appDownload() async {
    setState(
      () {
        downloaded = null;
      },
    );
    const platform = MethodChannel(
      'com.flutter.download',
    );
    await platform.invokeMethod(
      "apkDownload",
      {
        "downloadUrl": UrlConstants.callBreakDownload,
        "gameName": GameNames.callBreak,
        "fileName": UrlConstants.callBreakInstall
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
    const platform = MethodChannel('com.flutter.install');
    await platform.invokeMethod(
      "install",
      {
        "fileName": UrlConstants.callBreakInstall,
        "packageName": FlavorConfig.instance.variables["packageName"]
      },
    );
  }

  Future<void> openCallbreak(var lobbyJson) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Opening Callbreak----------",
    );
    bool isInstalled = await DeviceApps.isAppInstalled(
      "com.callbreak",
    );
    if (isInstalled) {
      if (lobbyJson["isRealMoneyGame"]) {
        var eventData = {
          "game_name": "Callbreak",
          "game_amount": lobbyJson["entryFee"],
          "game_type": lobbyJson["name"],
        };
        CommonMethods.printLog(
          "CleverTap Cash game Played",
          eventData.toString(),
        );
        CleverTapPlugin.recordEvent(
          "Cash game Played",
          eventData,
        );
      } else {
        CleverTapPlugin.recordEvent(
          "Free game Played",
          {
            "game_name": "Callbreak",
          },
        );
      }
      var body = convert.jsonEncode(lobbyJson);
      CommonMethods.printLog(
          StringConstants.emptyString,
          'openCallBreak=$body');
      try {
        final AndroidIntent intent = AndroidIntent(
          action: "android.intent.action.MAIN",
          componentName: "com.callbreak.LoginActivity",
          package: "com.callbreak",
          arguments: {
            "data": body,
          },
        );
        intent.launch();
      } catch (e) {
        CommonMethods.printLog(
          StringConstants.emptyString,
          "Unable to launch Call break",
        );
      }
    } else {
      appDownload();
    }
  }

  void showCustomDownload(BuildContext context) {
    showDialog(
      context: context,
      barrierDismissible: false, // user must tap button!
      builder: (BuildContext context) {
        return WillPopScope(
          onWillPop: () {
            return Future.delayed(Duration.zero).then(
              (value) => false,
            );
          },
          child: Dialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(5.0),
            ),
            elevation: 40,
            backgroundColor: ColorConstants.transparent,
            child: Stack(
              children: <Widget>[
                Container(
                  height: 25.0.h,
                  margin: EdgeInsets.only(
                    top: 45,
                  ),
                  decoration: BoxDecoration(
                    color: ColorConstants.white,
                    shape: BoxShape.rectangle,
                    borderRadius: BorderRadius.circular(
                      30,
                    ),
                    boxShadow: [
                      BoxShadow(
                        color: ColorConstants.black,
                        offset: Offset(0, 10),
                        blurRadius: 20,
                      ),
                    ],
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(1.0),
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border.all(
                          color: ColorConstants.blue1,
                          width: 0.5.w,
                        ),
                        borderRadius: BorderRadius.circular(
                          30,
                        ),
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: <Widget>[
                          SizedBox(
                            height: 2.0.h,
                          ),
                          Align(
                            alignment: Alignment.topCenter,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                left: 10.0,
                                right: 10.0,
                              ),
                              child: Text(
                                "New version of callbreak\nis available",
                                style: TextStyle(
                                  fontSize: 15.0.sp,
                                  fontWeight: FontWeight.w600,
                                  color: ColorConstants.grey700,
                                ),
                                textAlign: TextAlign.center,
                              ),
                            ),
                          ),
                          SizedBox(
                            height: 2.0.h,
                          ),
                          GestureDetector(
                            onTap: () async {
                              Navigator.pop(context);
                              await uninstall();
                            },
                            child: Align(
                              alignment: Alignment.topCenter,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                  right: 0.0,
                                ),
                                child: Text(
                                  "Update",
                                  style: TextStyle(
                                    fontSize: 16.0.sp,
                                    fontWeight: FontWeight.bold,
                                    color: ColorConstants.blue600,
                                  ),
                                ),
                              ),
                            ),
                          ),
                          SizedBox(
                            height: 2.0.h,
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget addReloadCoinDialogBox(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: Dialog(
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Container(
          height: 21.0.h,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(
              2.0.w,
            ),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              SizedBox(
                height: 4.0.h,
              ),
              Text(
                "You must Add Cash before\nplaying the game",
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 13.5.sp,
                  color: ColorConstants.grey700,
                  fontWeight: FontWeight.bold,
                ),
              ),
              SizedBox(
                height: 2.5.h,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () async {
                      Navigator.pop(context);
                      String gChips =
                          await SharedPrefService.getStringValuesFromSharedPref(
                        SharedPrefKeys.chipsAmount,
                      );
                      if (gChips.toString().length >= 5) {
                        CommonMethods.showSnackBar(
                          context,
                          "You cannot reload more than 10000 chips",
                        );
                      } else {
                        await reloadingCoins(context);
                      }
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        gradient: ColorConstants.blueDarkGradient,
                        borderRadius: BorderRadius.circular(
                          5,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "Reload Coins",
                          style: TextStyle(
                            color: ColorConstants.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 2.0.w,
                  ),
                  GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        border: Border.all(
                          color: ColorConstants.grey,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "Go Back",
                          style: TextStyle(
                            color: ColorConstants.grey,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> reloadingCoins(BuildContext context) async {
    Map<String, Object> result = await WalletService.reloadCoins(
      context,
      widget.userId,
    );
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.showSnackBar(
        context,
        "Technical Issue. Unable to reload coins",
      );
    } else {
      Map<String, dynamic> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          widget.userId,
        );
        if (accessTokenGenerated) {
          await reloadingCoins(context);
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.showSnackBar(
          context,
          "Coins have been added successfully",
        );

        // Provider.of<WalletInfoModel>(context, listen: false)
        //     .changeChips(responseMap['current_chips'].toInt());
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DOES_NOT_EXIST) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
      }
    }
  }
}
