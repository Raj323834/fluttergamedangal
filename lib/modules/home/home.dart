import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:connectivity/connectivity.dart';
import '../../constants/deep_link_addresses.dart';
import '../../modules/rewards/login_streak_screen.dart';
import 'package:device_apps/device_apps.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:fluttertoast/fluttertoast.dart' as flutterToast;
import 'package:package_info/package_info.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/app_maintenance.dart';
import '../../Model/dg_apk_download_model.dart';
import '../../Model/logging_model.dart';
import '../../Model/wallet_info_model.dart';
import '../../Network/auth_service.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../constants/app_constants.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../modules/privacy_policy/privacy_policy_screen.dart';
import '../../utils/co_in_auto_update.dart';
import '../../utils/ps_in_app_update.dart';
import '../../utils/shared_pref_service.dart';
import '../add_cash/add_cash.dart';
import '../all_games/all_games_screen.dart';
import '../avatar/avatar_screen.dart.dart';
import '../avatar/models/avatar_data_model.dart';
import '../deals/deals_screen.dart';
import '../deposit/deposit_screen.dart';
import '../homepage/homepage_screen.dart';
import '../kyc/kyc_screen.dart';
import '../leaderboard/leaderboard_screen.dart';
import '../profile/user_profile_screen.dart';
import '../responsible_gaming/responsible_gaming_screen.dart';
import '../share_screen/share_screen.dart';
import '../support/support_screen.dart';
import '../support/support_sub_screen.dart';
import '../terms_and_conditions/terms_and_conditions_screen.dart';
import '../transaction/transaction_screen.dart';
import '../wallet/wallet_screen.dart';
import '../withdrawals/withdrawals_screen.dart';
import 'widgets/drawer_tile.dart';

class HomeScreen extends StatefulWidget {
  static const String routeName = '/home';
  final int landingPage;
  final String routeDetail;
  final String userId;

  const HomeScreen({
    Key key,
    this.landingPage,
    this.routeDetail,
    this.userId,
  }) : super(key: key);

  @override
  HomeScreenState createState() => HomeScreenState();
}

class HomeScreenState extends State<HomeScreen> with WidgetsBindingObserver {
  int currentIndex;
  int avatarId = 1;
  String language = 'English';
  bool isLanguageChanged = false;
  bool isDownloaded = false;

  List<String> languageType = [
    'English',
    'Bengali',
    'Gujrati',
    'Hindi',
    'Marathi',
    'Tamil'
  ];

  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  PageController _pageController;

  FlutterWebviewPlugin fantasyWebviewPlugin;
  String bonusCode = StringConstants.emptyString;
  String currentVersion = "1.0.0";
  String latestVersion = "1.0.0";
  String currentVersionCoIn = "1.0.0";
  String updateDetail = StringConstants.emptyString;
  bool deepLink = false;
  flutterToast.FToast fToast = flutterToast.FToast();
  String _customDgDownloadName;
  List<GlobalKey<NavigatorState>> _navigatorKeys = [
    GlobalKey<NavigatorState>(),
    GlobalKey<NavigatorState>(),
    GlobalKey<NavigatorState>(),
    GlobalKey<NavigatorState>(),
    GlobalKey<NavigatorState>(),
  ];

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    currentIndex = widget.landingPage ?? 0;
    _pageController = PageController(
      initialPage: currentIndex,
    );
    checkMaintenanceWarning();
    asyncMethod();
    fToast.init(context);
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      if (FlavorInfo.isCoInWithoutPoker) {
        versionCheck();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        // let system handle back button if we're on the first route
        final isFirstRouteInCurrentTab =
            !await _navigatorKeys[currentIndex].currentState.maybePop();

        return isFirstRouteInCurrentTab;
      },
      child: MultiProvider(
        providers: [
          ChangeNotifierProvider(
            create: (context) => WalletInfoModel(),
          ),
        ],
        child: SafeArea(
          child: Scaffold(
            key: _scaffoldKey,
            appBar: PreferredSize(
              preferredSize: Size(
                double.infinity,
                8.0.h,
              ),
              child: Container(
                decoration: BoxDecoration(
                  color: ColorConstants.kBackgroundColor,
                ),
                padding: EdgeInsets.symmetric(
                  horizontal: 10,
                ),
                width: double.infinity,
                child: Row(
                  children: [
                    Expanded(
                      flex: 5,
                      child: Row(
                        children: [
                          //Drawer Icon
                          GestureDetector(
                            onTap: () {
                              _scaffoldKey.currentState.openDrawer();
                              FocusScope.of(context).unfocus();
                            },
                            child: Row(
                              children: [
                                Stack(
                                  alignment: Alignment.bottomLeft,
                                  children: [
                                    //PROFILE
                                    CircleAvatar(
                                      radius: 2.8.h,
                                      backgroundImage: NetworkImage(
                                        AvatarDataModel.ids[avatarId - 1],
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(top: 8.0),
                                      child: CircleAvatar(
                                        radius: 1.2.h,
                                        backgroundColor: ColorConstants.white,
                                        child: Icon(
                                          Icons.menu,
                                          color: ColorConstants.black,
                                          size: 1.7.h,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                                SizedBox(
                                  width: 21,
                                ),
                                IconButton(
                                  onPressed: () {
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (context) => LoginStreakScreen(
                                          userId: widget.userId,
                                        ),
                                      ),
                                    );
                                  },
                                  icon: Image.asset(AssetPaths.rewardIcon),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                    Consumer<WalletInfoModel>(
                      builder: (context, walletInfoValue, child) =>
                          StreamBuilder(
                        stream: sockets.streamController.stream,
                        builder: (context, snapshot) {
                          if (snapshot.hasData) {
                            try {
                              var snapBody = jsonDecode(snapshot.data);
                              if (snapBody['type'] ==
                                  WebSocketTopics.userCashBalance) {
                                walletInfoValue.changeCash(
                                  snapBody['deposit'],
                                );
                                walletInfoValue.changeWithdraw(
                                  snapBody['withdrawable'],
                                );
                                walletInfoValue.changeBonus(
                                  snapBody['bonus'],
                                );
                                walletInfoValue.changeGamechips(
                                  snapBody['gameChips'],
                                );
                                walletInfoValue.changeCombined(
                                  snapBody['deposit'],
                                  snapBody['withdrawable'],
                                  snapBody['gameChips'],
                                );
                                walletInfoValue.changePokerchips(
                                  snapBody['pokerChips'],
                                );
                              } else if (snapBody['type'] ==
                                  WebSocketTopics.userFreeBalance) {
                                walletInfoValue.changeChips(
                                  snapBody['playChips'].toInt(),
                                );
                              } else if (snapBody['type'] ==
                                  WebSocketTopics.logout) {
                                LoggingModel.logging(
                                  "loggingOutUser()",
                                  "sm-logout HomeScreen",
                                  DateTime.now().toString(),
                                  widget.userId,
                                );
                                // loggingOutUser();
                              } else if (snapBody['type'] ==
                                  WebSocketTopics.maintenanceWarning) {
                                showMaintenanceWarning(
                                  fToast,
                                  context,
                                  snapBody['maintenanceStartTime'],
                                );
                              } else if (snapBody['type'] ==
                                  WebSocketTopics.startMaintenance) {
                                startMaintenance(
                                  context,
                                  snapBody['maintenanceEndTime'],
                                  fToast,
                                );
                              } else if (snapBody['type'] ==
                                  WebSocketTopics.endMaintenance) {
                                endMaintenance(
                                  context,
                                  fToast,
                                );
                              } else {
                                try {
                                  walletInfoValue.initialValues();
                                } catch (e) {}
                              }
                            } catch (e) {}
                          }
                          //ADD CASH BUTTON
                          return Expanded(
                            flex: 2,
                            child: Padding(
                              padding: const EdgeInsets.symmetric(
                                vertical: 12.0,
                              ),
                              child: GestureDetector(
                                onTap: () {
                                  setState(
                                    () {
                                      bonusCode = StringConstants.emptyString;
                                      currentIndex = FlavorInfo.isPS
                                          ? AppConstants.addCashPS
                                          : AppConstants.addCash;
                                      _pageController.jumpToPage(currentIndex);
                                    },
                                  );
                                },
                                child: Container(
                                  decoration: BoxDecoration(
                                    gradient: ColorConstants.greenGradient,
                                    borderRadius: BorderRadius.circular(
                                      5,
                                    ),
                                  ),
                                  child: Padding(
                                    padding: EdgeInsets.all(
                                      2.0.w,
                                    ),
                                    child: Row(
                                      children: [
                                        //WALLET ICON
                                        Icon(
                                          Icons.account_balance_wallet,
                                          color: ColorConstants.white,
                                          size: 5.1.w,
                                        ),
                                        SizedBox(
                                          width: 4,
                                        ),
                                        //AMOUNT
                                        Center(
                                          child: Text(
                                            (walletInfoValue.combinedValue <
                                                    100000)
                                                ? '₹' +
                                                    (walletInfoValue
                                                                .combinedValue
                                                            as double)
                                                        .toStringAsFixed((walletInfoValue
                                                                    .combinedValue <
                                                                100)
                                                            ? 2
                                                            : (walletInfoValue
                                                                        .combinedValue <
                                                                    1000)
                                                                ? 1
                                                                : 0)
                                                : NumberFormat.compactCurrency(
                                                    decimalDigits: 2,
                                                    symbol: '₹',
                                                    locale: 'en_in',
                                                  ).format(
                                                    walletInfoValue
                                                        .combinedValue,
                                                  ),
                                            overflow: TextOverflow.ellipsis,
                                            style: TextStyle(
                                              fontSize: 10.sp,
                                              color: ColorConstants.white,
                                              fontWeight: FontWeight.w800,
                                            ),
                                          ),
                                        ),
                                        Spacer(),
                                        //ADD ICON
                                        Container(
                                          width: 4.3.w,
                                          child: SvgPicture.asset(
                                            AssetPaths.add,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
            ),
            drawer: Drawer(
              elevation: 10,
              child: Container(
                color: ColorConstants.appBarBgCol,
                child: ListView(
                  padding: EdgeInsets.zero,
                  children: <Widget>[
                    //Header
                    Container(
                      height: 20.0.h,
                      child: DrawerHeader(
                        decoration: BoxDecoration(),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: <Widget>[
                            Image.asset(
                              'assets/images/logo.png',
                            ),
                            Row(
                              children: [
                                Text(
                                  FlavorConfig.instance.variables[
                                              StringConstants.envTypeKey] ==
                                          AppConstants.envTypeDev
                                      ? 'DANGAL GAMES (${StringConstants.envDev})'
                                      : FlavorConfig.instance.variables[
                                                  StringConstants.envTypeKey] ==
                                              AppConstants.envTypeDevPlayStore
                                          ? 'DANGAL GAMES (${StringConstants.envDevPlayStore})'
                                          : 'DANGAL GAMES',
                                  style: TextStyle(
                                    color: ColorConstants.white,
                                    fontSize: 12.5.sp,
                                  ),
                                ),
                              ],
                            ),
                            Row(
                              children: [
                                Text(
                                  'Version : $currentVersion',
                                  style: TextStyle(
                                    color: ColorConstants.lightGrey,
                                    fontSize: 10.0.sp,
                                  ),
                                ),
                              ],
                            )
                          ],
                        ),
                      ),
                    ),
                    //Profile
                    DrawerTile(
                      icon: Icon(
                        Icons.account_circle_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: 'home_screen.profile'.tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.push(
                          context,
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                UserProfileScreen(
                              fromDialog: false,
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Wallet
                    DrawerTile(
                      icon: Icon(
                        Icons.account_balance_wallet_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.wallet".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) => WalletScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Deals
                    DrawerTile(
                      icon: Icon(
                        Icons.local_fire_department_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.deals".tr(),
                      onTap: () {
                        openDeals();
                      },
                    ),
                    //KYC
                    DrawerTile(
                      icon: Icon(
                        Icons.person_search_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: 'home_screen.kyc'.tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) => KycScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Transactions
                    DrawerTile(
                      icon: SvgPicture.asset(
                        AssetPaths.transaction,
                        height: 6.0.w,
                      ),
                      tileName: "home_screen.transactions".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                TransactionScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Deposit History
                    DrawerTile(
                      icon: SvgPicture.asset(
                        AssetPaths.deposit,
                        height: 6.0.w,
                      ),
                      tileName: "home_screen.deposit_history".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                DepositListScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Withdrawal History
                    DrawerTile(
                      icon: SvgPicture.asset(
                        AssetPaths.withdrawalHistory,
                        height: 6.0.w,
                      ),
                      tileName: "home_screen.withdrawal_history".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                WithdrawalListScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Change Avatar
                    DrawerTile(
                      icon: Icon(
                        Icons.person_outline,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.change_avatar".tr(),
                      onTap: () async {
                        Navigator.of(context).pop();
                        int newAvatarId = await Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) => AvatarScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                        if (newAvatarId != null) {
                          setState(
                            () {
                              avatarId = newAvatarId;
                            },
                          );
                        }
                      },
                    ),
                    //Terms and Condition
                    DrawerTile(
                      icon: Icon(
                        Icons.description_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.tnc".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                TermsAndConditionsScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //Responsible Gaming
                    DrawerTile(
                      icon: Icon(
                        Icons.casino_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.rsp_gaming".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                ResponsibleGamingScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    DrawerTile(
                      icon: Icon(
                        Icons.policy_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.pp".tr(),
                      onTap: () {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) =>
                                PrivacyPolicyScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    DrawerTile(
                      icon: Icon(
                        Icons.language,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.change_language".tr(),
                      onTap: () async {
                        await changeLang();
                      },
                    ),
                    //Support
                    DrawerTile(
                      icon: Icon(
                        Icons.support_agent_outlined,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.support".tr(),
                      onTap: () async {
                        Navigator.of(context).pop();
                        Navigator.of(context).push(
                          CupertinoPageRoute(
                            builder: (BuildContext context) => SupportScreen(
                              userId: widget.userId,
                            ),
                          ),
                        );
                      },
                    ),
                    //LogOut
                    DrawerTile(
                      icon: Icon(
                        Icons.logout,
                        color: ColorConstants.lightGrey,
                        size: 6.0.w,
                      ),
                      tileName: "home_screen.logout".tr(),
                      onTap: () async {
                        AuthService.loggingOutUser();
                      },
                    ),
                  ],
                ),
              ),
            ),
            body: PageView(
              physics: NeverScrollableScrollPhysics(),
              controller: _pageController,
              onPageChanged: (page) {
                setState(
                  () {
                    currentIndex = page;
                  },
                );
              },
              children: [
                _buildOffstageNavigator(0),
                _buildOffstageNavigator(1),
                _buildOffstageNavigator(2),
                _buildOffstageNavigator(3),
                _buildOffstageNavigator(4),
              ],
            ),
            bottomNavigationBar: _createBottomNavigationBar(),
          ),
        ),
      ),
    );
  }

  //METHODS
  void changeIndex(int newIndex) {
    setState(
      () {
        currentIndex = newIndex;
        _pageController.jumpToPage(currentIndex);
      },
    );
    EasyLoading.showToast(
      "Payment is Successful",
    );
  }

  void changeFromDealsIndex(
    int newIndex,
    String code, {
    bool willPop = false,
  }) {
    setState(
      () {
        bonusCode = code;
        currentIndex = newIndex;

        if (willPop) {
          Navigator.of(context).pop();
        }

        _pageController.jumpToPage(currentIndex);
      },
    );
  }

  void flutterWebviewInstance(
    FlutterWebviewPlugin webviewInstance,
  ) {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Webview Instance----------",
    );
    setState(
      () {
        fantasyWebviewPlugin = webviewInstance;
      },
    );
  }

  Future<void> asyncMethod() async {
    await avatarCheck();
    await deepLinkCheck();
    await checkPreferredLanguage();
    await versionCheck();
    FlavorInfo.isCom
        ? await checkCoInGameVersionAndRemoveApp()
        : CommonMethods.printLog(
            'Co.in',
            '${FlavorInfo.name}',
          );

    appUpdateSC.stream.listen(
      (isUpdate) async {
        if (isUpdate && isDownloaded) {
          await Future.delayed(
            Duration(
              seconds: 2,
            ),
          );
          showCustomDownloadDialog(context);
        }
      },
    );
  }

  void checkMaintenanceWarning() async {
    bool warningShowed = await SharedPrefService.getBoolValuesFromSharedPref(
          SharedPrefKeys.warningShowed,
        ) ??
        false;
    if (warningShowed) {
      showMaintenanceWarning(
        fToast,
        context,
        await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.maintenanceStartTime,
        ),
      );
    }
    String maintenanceReceived =
        await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
            ) ??
            StringConstants.emptyString;
    if (maintenanceReceived.contains(
      WebSocketTopics.startMaintenance,
    )) {
      sockets.streamController.add(
        maintenanceReceived,
      );
      // wsService.streamController.add(maintenanceReceived);
    }
  }

  Future checkCoInGameVersionAndRemoveApp() async {
    bool isInstalled = await DeviceApps.isAppInstalled(
      "com.app.dangalgames.co.in",
    );
    if (isInstalled) {
      Application app = await DeviceApps.getApp(
        "com.app.dangalgames.co.in",
      );
      setState(
        () {
          currentVersionCoIn = app.versionName;
        },
      );
      uninstallDialog();
    }
  }

  Future<void> checkPreferredLanguage() async {
    String preferredLanguage =
        await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.preferredLanguage,
            ) ??
            StringConstants.emptyString;
    CommonMethods.changeLanguage(
      context: context,
      language: preferredLanguage,
    );
  }

  Future<void> uninstall() async {
    const platform = MethodChannel(
      'com.flutter.uninstall',
    );
    String package = "com.app.dangalgames.co.in";

    var uninstallResult = await platform.invokeMethod(
      "Uninstall",
      {
        "package": package,
      },
    );
    if (uninstallResult == "Cancelled") {
    } else {
      CommonMethods.showSnackBar(
        context,
        "The older App has been uninstalled successfully!!",
      );
    }
  }

  Future<void> deepLinkCheck() async {
    deepLink = await SharedPrefService.getBoolValuesFromSharedPref(
          SharedPrefKeys.deepLink,
        ) ??
        true;

    if (deepLink && widget.routeDetail == DeepLinkAddresses.changeLang) {
      await changeLang();
    } else if (deepLink && widget.routeDetail == DeepLinkAddresses.deals) {
      openDeals();
    }

    Timer(
      Duration(seconds: 3),
      () async {
        await SharedPrefService.addBoolToSharedPref(
          SharedPrefKeys.deepLink,
          false,
        );
      },
    );
  }

  Future<void> avatarCheck() async {
    avatarId = await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.avatarId,
        ) ??
        1;
    setState(
      () {
        avatarId = avatarId;
      },
    );
  }

  Future<void> versionCheck() async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    currentVersion = packageInfo.version;
    if (FlavorInfo.isProdPS) {
      PSInAppUpdate.startFlexible;
    } else {
      latestVersion = await SharedPrefService.getStringValuesFromSharedPref(
        FlavorInfo.isDevPS
            ? SharedPrefKeys.psApkVersion
            : SharedPrefKeys.apkVersion,
      );

      updateDetail = await SharedPrefService.getStringValuesFromSharedPref(
            FlavorInfo.isDevPS
                ? SharedPrefKeys.psUpdateDetails
                : SharedPrefKeys.updateDetails,
          ) ??
          StringConstants.emptyString;
      CommonMethods.printLog(
        StringConstants.emptyString,
        'DG current ver:-> $currentVersion latest ver:-> $latestVersion',
      );

      if (await CoInAutoUpdate.isVersionCheck()) {
        if (latestVersion.compareTo(currentVersion) == 1) {
          setState(
            () {
              updateDetail = updateDetail;
            },
          );
          await _askingPermission(context);
        }
      }
    }
  }

  void pushNotificationAddCash() {
    setState(
      () {
        currentIndex =
            FlavorInfo.isPS ? AppConstants.addCashPS : AppConstants.addCash;
        _pageController.jumpToPage(currentIndex);
      },
    );
  }

  void uninstallDialog() {
    showDialog(
      context: context, // user must tap button!
      builder: (BuildContext context) {
        return WillPopScope(
          onWillPop: () {
            return Future.delayed(Duration.zero).then(
              (value) => false,
            );
          },
          child: Dialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(
                5.0,
              ),
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
                                "Looks like you have an older version of the app. \nProceed to remove the older version?",
                                style: TextStyle(
                                  fontSize: 12.0.sp,
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
                            onTap: () {
                              Navigator.pop(context);
                              uninstall();
                            },
                            child: Align(
                              alignment: Alignment.topCenter,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                  right: 0.0,
                                ),
                                child: Text(
                                  "Uninstall",
                                  style: TextStyle(
                                    fontSize: 13.0.sp,
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

  Widget _createBottomNavigationBar() {
    return Container(
      child: BottomNavigationBar(
        currentIndex: currentIndex,
        onTap: (index) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "index   :   " + index.toString(),
          );
          setState(
            () {
              if (index == 0) {
                // widget.routeGames = StringConstants.emptyString;//TO BE CHECKED
              } else if (index == 2) {
                bonusCode = StringConstants.emptyString;
              }
              currentIndex = index;
              fantasyWebviewPlugin = null;
            },
          );
          _pageController.jumpToPage(index);
        },
        showUnselectedLabels: true,
        backgroundColor: ColorConstants.greyBottomNavigator,
        type: BottomNavigationBarType.fixed,
        unselectedIconTheme: IconThemeData(
          color: ColorConstants.white,
        ),
        unselectedItemColor: ColorConstants.white,
        selectedFontSize: 9.3.sp,
        unselectedFontSize: 9.3.sp,
        elevation: 50.0,
        selectedIconTheme: IconThemeData(
          color: ColorConstants.kSecondaryColor,
        ),
        selectedItemColor: ColorConstants.kSecondaryColor,
        items: FlavorInfo.isPS
            ? [
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.home,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.home_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.home".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.account_balance_wallet_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.account_balance_wallet_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.add_cash".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.share_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.share_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.share".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.leaderboard_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.leaderboard_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.leaderboard".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.support_agent,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.support_agent_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.support".tr(),
                )
              ]
            : [
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.home,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.home_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.home".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.games_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.games_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.all_games".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.account_balance_wallet_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.account_balance_wallet_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.add_cash".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.share_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.share_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.share".tr(),
                ),
                BottomNavigationBarItem(
                  activeIcon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.leaderboard_rounded,
                      size: 4.0.h,
                    ),
                  ),
                  icon: Padding(
                    padding: EdgeInsets.only(bottom: 0.1.h),
                    child: Icon(
                      Icons.leaderboard_outlined,
                      size: 4.0.h,
                    ),
                  ),
                  label: "home_screen.footer.leaderboard".tr(),
                )
              ],
      ),
    );
  }

  Map<String, WidgetBuilder> _routeBuilders(
    BuildContext context,
    int index,
  ) {
    List tabs = FlavorConfig.instance.variables[StringConstants.envTypeKey] ==
                AppConstants.envTypeProdPlayStore ||
            FlavorConfig.instance.variables[StringConstants.envTypeKey] ==
                AppConstants.envTypeDevPlayStore
        ? [
            HomePage(
              changeFromDealsIndex,
              flutterWebviewInstance,
              widget.routeDetail,
              widget.userId,
            ),
            AddCash(
              changeIndex: changeIndex,
              bonusC: bonusCode,
              webviewInstance: fantasyWebviewPlugin,
              userId: widget.userId,
            ),
            ShareScreen(
              userId: widget.userId,
            ),
            LeaderboardScreen(
              userId: widget.userId,
            ),
            SupportSubScreen(
              userId: widget.userId,
            ),
          ]
        : [
            HomePage(
              changeFromDealsIndex,
              flutterWebviewInstance,
              widget.routeDetail,
              widget.userId,
            ),
            AllGamesScreen(
              changeFromDealsIndex,
              flutterWebviewInstance,
              widget.routeDetail,
              widget.userId,
            ),
            // DealsScreen(changeFromDealsIndex),
            AddCash(
              changeIndex: changeIndex,
              bonusC: bonusCode,
              webviewInstance: fantasyWebviewPlugin,
              userId: widget.userId,
            ),
            ShareScreen(
              userId: widget.userId,
            ),
            LeaderboardScreen(
              userId: widget.userId,
            ),
          ];

    return {
      '/': (context) {
        return tabs.elementAt(index);
      },
    };
  }

  Widget _buildOffstageNavigator(int index) {
    var routeBuilders = _routeBuilders(
      context,
      index,
    );

    return Offstage(
      offstage: currentIndex != index,
      child: Navigator(
        key: _navigatorKeys[index],
        onGenerateRoute: (routeSettings) {
          return CupertinoPageRoute(
            builder: (context) => routeBuilders[routeSettings.name](context),
          );
        },
      ),
    );
  }

  void showCustomDownloadDialog(BuildContext context) {
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
                  height: 31.0.h,
                  margin: EdgeInsets.only(
                    top: 45,
                  ),
                  decoration: BoxDecoration(
                    color: ColorConstants.white,
                    shape: BoxShape.rectangle,
                    borderRadius: BorderRadius.circular(30),
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
                      child: Padding(
                        padding: EdgeInsets.only(
                          top: 2.0.h,
                          bottom: 2.0.h,
                        ),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            Align(
                              alignment: Alignment.topCenter,
                              child: Padding(
                                padding: const EdgeInsets.only(
                                  left: 10.0,
                                  right: 10.0,
                                ),
                                child: Text(
                                  "Version $latestVersion is available",
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
                            Expanded(
                              child: ListView(
                                children: [
                                  Column(
                                    children: [
                                      Padding(
                                        padding: EdgeInsets.only(
                                          left: 7.0.w,
                                          right: 4.0.w,
                                        ),
                                        child: Container(
                                          width: 80.0.w,
                                          child: Text(
                                            updateDetail,
                                            style: TextStyle(
                                              fontSize: 11.0.sp,
                                              height: 1.5,
                                              fontWeight: FontWeight.w600,
                                              color: ColorConstants.grey,
                                            ),
                                            textAlign: TextAlign.left,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                            SizedBox(
                              height: 3.0.h,
                            ),
                            GestureDetector(
                              onTap: () async {
                                const platformDir = MethodChannel(
                                  'com.flutter.downloadDir',
                                );
                                String downloadDirectory =
                                    await platformDir.invokeMethod(
                                  "downloadDir",
                                );

                                const platform = MethodChannel(
                                  'com.flutter.install',
                                );
                                await platform.invokeMethod(
                                  "installDG",
                                  {
                                    "fileName": "$_customDgDownloadName",
                                    "packageName": FlavorConfig
                                        .instance.variables["packageName"],
                                    "path": downloadDirectory
                                  },
                                );
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
                          ],
                        ),
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

  Future _askingPermission(BuildContext context) async {
    final PermissionStatus permissionStatus = await _getPermission(context);
    if (permissionStatus == PermissionStatus.granted) {
      CommonMethods.printLog(
          StringConstants.emptyString, "Storage permission granted");
      _customDgDownloadName =
          await SharedPrefService.getStringValuesFromSharedPref(
        FlavorInfo.isPS
            ? SharedPrefKeys.psApkName
            : SharedPrefKeys.customDgDownloadName,
      );

      String dgDownloadUrl = StringConstants.emptyString;

      if (FlavorInfo.isPS) {
        dgDownloadUrl = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.psApkUrl,
        );
      } else if (FlavorInfo.isCom) {
        //TO BE CHANGED
        if (FlavorConfig.instance.variables["is_v8"]) {
          dgDownloadUrl = await SharedPrefService.getStringValuesFromSharedPref(
            SharedPrefKeys.apkUrl64Bit,
          );
        } else {
          dgDownloadUrl = await SharedPrefService.getStringValuesFromSharedPref(
            SharedPrefKeys.apkUrl32Bit,
          );
        }
      } else {
        // if co.in update
        dgDownloadUrl = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.apkUrlCoInUpdate,
        );
      }

      if (!isDownloaded) {
        isDownloaded = await DgApkDownloadModel.downloadDgApk(
          downloadUrl: dgDownloadUrl,
          apkName: _customDgDownloadName,
        );
      }
      CommonMethods.printLog(
        StringConstants.emptyString,
        '${isDownloaded.toString()} ${DgApkDownloadModel.isWebViewOpened}',
      );
      if (!DgApkDownloadModel.isWebViewOpened && isDownloaded) {
        showCustomDownloadDialog(context);
      }
    } else {
      showDialog(
        context: context,
        builder: (BuildContext context) => CupertinoAlertDialog(
          title: Text(
            'Permissions error',
          ),
          content: Text(
            'Please enable storage access '
            'permission in system settings',
          ),
          actions: <Widget>[
            CupertinoDialogAction(
              child: Text(
                'OK',
              ),
              onPressed: () => Navigator.of(context).pop(),
            ),
          ],
        ),
      );
    }
  }

  Future<PermissionStatus> _getPermission(BuildContext context) async {
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

  void showLanguageBottomSheet(
    String language,
    List<String> languageType,
    BuildContext context,
  ) async {
    isLanguageChanged = false;
    CommonMethods.showCustomPickerBottomSheet(
      context: context,
      title: StringConstants.selectALanguage,
      initialItem: language,
      list: languageType,
      onTap: () {
        isLanguageChanged = true;
        Navigator.pop(context);
      },
      onSelectedItemChanged: (index) {
        setState(
          () {
            this.language = language = languageType[index];
          },
        );
      },
    ).whenComplete(
      () async {
        if (isLanguageChanged) {
          CommonMethods.changeLanguage(
            context: context,
            language: language,
          );
          // languageSelected = true;
        }
        await SharedPrefService.addBoolToSharedPref(
          SharedPrefKeys.languageSelected,
          true,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.preferredLanguage,
          language,
        );
      },
    );
  }

  Future changeLang() async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else {
      isLanguageChanged = false;
      showLanguageBottomSheet(
        language,
        languageType,
        context,
      );
    }
  }

  void openDeals() {
    _pageController.jumpToPage(0); //TO BE CHANGED
    if (_scaffoldKey.currentState.isDrawerOpen) {
      Navigator.of(context).pop();
    }
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (context) => DealsScreen(
          changeFromDealsIndex: changeFromDealsIndex,
          userId: widget.userId,
        ),
      ),
    );
  }
}
