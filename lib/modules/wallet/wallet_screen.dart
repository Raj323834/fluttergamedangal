import 'dart:convert';
import 'dart:ui';

import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Model/wallet_info_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/poker_chips_service.dart';
import '../../Network/wallet_service.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/information_dialog.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/app_constants.dart';
import '../../constants/color_constants.dart';
import '../../constants/enum.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../network_new/constants/responses_keys.dart';
import '../home/home.dart';
import '../withdraw_cash/withdraw_cash_screen.dart';
import 'widgets/cash_tile.dart';

class WalletScreen extends StatefulWidget {
  final String userId;

  const WalletScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  _WalletScreenState createState() => _WalletScreenState();
}

class _WalletScreenState extends State<WalletScreen> {
  final GlobalKey<ScaffoldState> _scaffoldKey1 = GlobalKey<ScaffoldState>();
  final verticalSpace = SizedBox(
    height: 1.8.h,
  );

  bool reload = false;

  @override
  void initState() {
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: WALLET_ROUTE,
    );
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        key: _scaffoldKey1,
        backgroundColor: ColorConstants.kBackgroundColor,
        appBar: PreferredSize(
          preferredSize: Size(
            double.infinity,
            100,
          ),
          child: CustomAppBar(
            from: "wallet",
            userId: widget.userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        body: Padding(
          padding: EdgeInsets.symmetric(
            horizontal: 14.0,
            vertical: 3.0.h,
          ),
          child: MultiProvider(
            providers: [
              ChangeNotifierProvider(
                create: (context) => WalletInfoModel(),
              ),
            ],
            child: Consumer<WalletInfoModel>(
              builder: (context, walletInfoValue, child) {
                return StreamBuilder(
                  stream: sockets.streamController.stream,
                  builder: (context, snapshot) {
                    try {
                      if (snapshot.hasData) {
                        var snapBody = jsonDecode(
                          snapshot.data,
                        );
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
                          walletInfoValue.changePokerchips(
                            snapBody['pokerChips'],
                          );
                        } else if (snapBody['type'] ==
                            WebSocketTopics.userFreeBalance) {
                          walletInfoValue.changeChips(
                            snapBody['playChips'].toInt(),
                          );
                        }
                      }
                    } catch (e) {}
                    return SingleChildScrollView(
                      child: Column(
                        children: [
                          //Lable
                          LabelHeader(
                            label: "wallet_screen.wallet".tr(),
                          ),
                          SizedBox(
                            height: 4.h,
                          ),
                          //Withdraw Cash
                          CashTile(
                            cashType: CashType.withdraw,
                            typeOfCash: "wallet_screen.withdraw_cash".tr(),
                            amount: walletInfoValue.withdrawalValue,
                            buttonText: "wallet_screen.withdrawal".tr(),
                            onTap: () {
                              Navigator.push(
                                context,
                                CupertinoPageRoute(
                                  builder: (context) => WithdrawalScreen(
                                    userId: widget.userId,
                                  ),
                                ),
                              );
                            },
                          ),
                          verticalSpace,
                          //Deposit Cash
                          CashTile(
                            cashType: CashType.deposit,
                            typeOfCash: "wallet_screen.deposit_cash".tr(),
                            amount: walletInfoValue.cashValue,
                            onTap: () {
                              Navigator.pushAndRemoveUntil(
                                context,
                                CupertinoPageRoute(
                                  builder: (BuildContext context) => HomeScreen(
                                    landingPage: FlavorInfo.isPS
                                        ? AppConstants.addCashPS
                                        : AppConstants.addCash,
                                    routeDetail: StringConstants.emptyString,
                                    userId: widget.userId,
                                  ),
                                ),
                                (route) => false,
                              );
                            },
                            buttonText: "wallet_screen.add_cash".tr(),
                          ),
                          verticalSpace,
                          //Game Chips
                          CashTile(
                            cashType: CashType.gameChip,
                            typeOfCash: "wallet_screen.game_chips".tr(),
                            amount: walletInfoValue.gameChipsValue,
                            onTap: () {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (_) {
                                  return InformationDialog(
                                    title: 'Game Chips',
                                    content: StringConstants.gameChipsLearnMore,
                                  );
                                },
                              );
                            },
                            buttonText: "wallet_screen.learn_more".tr(),
                          ),
                          verticalSpace,
                          //Bonus Cash
                          CashTile(
                            cashType: CashType.bonus,
                            typeOfCash: "wallet_screen.bonus_cash".tr(),
                            amount: walletInfoValue.bonusValue,
                            onTap: () {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (_) {
                                  return InformationDialog(
                                    title: 'Bonus Cash',
                                    content:
                                        StringConstants.bonusChipsLearnMore,
                                  );
                                },
                              );
                            },
                            buttonText: "wallet_screen.learn_more".tr(),
                          ),
                          verticalSpace,
                          //Poker Chips
                          if (FlavorInfo.showPokerChips)
                            CashTile(
                              cashType: CashType.pokerChip,
                              typeOfCash: "wallet_screen.poker_chips".tr(),
                              amount: walletInfoValue.pokerChipsValue,
                              onTap: () {
                                if (walletInfoValue.pokerChipsValue
                                        .toString() ==
                                    "0.00") {
                                  CommonMethods.showSnackBar(context,
                                      "You do not have enough Poker Chips");
                                } else {
                                  showDialog(
                                      context: context,
                                      barrierDismissible: false,
                                      builder: (BuildContext context) {
                                        return WillPopScope(
                                          onWillPop: () {
                                            return Future.delayed(Duration.zero)
                                                .then(
                                              (value) => false,
                                            );
                                          },
                                          child: Center(
                                            child: CircularProgressIndicator(
                                              backgroundColor: Colors.grey,
                                            ),
                                          ),
                                        );
                                      });
                                  getPokerTds(
                                    userId: widget.userId,
                                  );
                                }
                              },
                              buttonText: "wallet_screen.transfer".tr(),
                            ),
                          if (FlavorInfo.showPokerChips) verticalSpace,
                          //Reload Coins
                          CashTile(
                            cashType: CashType.freeChip,
                            typeOfCash: "wallet_screen.reload_coins".tr(),
                            amount: walletInfoValue.chipsValue,
                            onTap: () async {
                              if (walletInfoValue.chipsValue
                                      .toString()
                                      .length >=
                                  5) {
                                CommonMethods.showSnackBar(context,
                                    "You cannot reload more than 10000 chips");
                              } else {
                                await reloadingCoins(
                                  context: context,
                                  userId: widget.userId,
                                );
                              }
                            },
                            buttonText: '',
                          ),
                        ],
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ),
      ),
    );
  }

  //METHODS
  void getPokerTds({
    @required String userId,
  }) async {
    Map<String, dynamic> response = await PokerChipsService.getPokerTds(
      userId,
    );
    if (response.containsKey(ResponsesKeys.ERROR)) {
      CommonMethods.showSnackBar(
        context,
        ResponsesKeys.SERVER_ERROR_MSG,
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else {
      Map<String, dynamic> responseMap = response[ResponsesKeys.DATA];
      if (responseMap.containsKey(ResponsesKeys.ERROR)) {
        CommonMethods.showSnackBar(
          context,
          ResponsesKeys.SERVER_ERROR_MSG,
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      } else if (responseMap[ResponsesKeys.RESULT] ==
              ResponsesKeys.TOKEN_EXPIRED ||
          responseMap[ResponsesKeys.RESULT] ==
              ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          widget.userId,
        );
        if (accessTokenGenerated) {
          getPokerTds(
            userId: userId,
          );
        }
      } else if (responseMap[ResponsesKeys.RESULT] == ResponsesKeys.SUCCESS) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        if (responseMap["pokerTds"] <= 0.0) {
          showCustomDialogTds(
              error: "Are you sure you want to continue?",
              context: context,
              userId: userId);
        } else if (responseMap["pokerTds"] > 0.0) {
          showCustomDialogTds(
              error:
                  "₹ ${responseMap["pokerTds"]} TDS will be deducted.\nAre you sure you want to continue?",
              context: context,
              userId: userId);
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          ResponsesKeys.SERVER_ERROR_MSG,
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      }
    }
  }

  void pokerToDgTransfer({
    @required String userId,
  }) async {
    Map<String, dynamic> response = await PokerChipsService.pokerToDgTransfer(
      userId,
    );
    if (response.containsKey(ResponsesKeys.ERROR)) {
      CommonMethods.showSnackBar(
        context,
        ResponsesKeys.SERVER_ERROR_MSG,
      );
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
    } else {
      Map<String, dynamic> responseMap = response[ResponsesKeys.DATA];
      if (responseMap.containsKey(ResponsesKeys.ERROR)) {
        CommonMethods.showSnackBar(
          context,
          ResponsesKeys.SERVER_ERROR_MSG,
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      } else if (responseMap[ResponsesKeys.RESULT] ==
              ResponsesKeys.TOKEN_EXPIRED ||
          responseMap[ResponsesKeys.RESULT] ==
              ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          pokerToDgTransfer(
            userId: userId,
          );
        }
      } else if (responseMap[ResponsesKeys.RESULT] == ResponsesKeys.SUCCESS) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      } else {
        CommonMethods.showSnackBar(
          context,
          ResponsesKeys.SERVER_ERROR_MSG,
        );
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
      }
    }
  }

  Future<void> reloadingCoins({
    @required BuildContext context,
    @required String userId,
  }) async {
    Map<String, Object> result = await WalletService.reloadCoins(
      context,
      userId,
    );
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      setState(() {
        reload = false;
      });
    } else if (result.containsKey('error')) {
      CommonMethods.showSnackBar(
        context,
        "Technical Issue. Unable to reload coins",
      );
      setState(
        () {
          reload = false;
        },
      );
    } else {
      Map<String, dynamic> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
        setState(() {
          reload = false;
        });
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          widget.userId,
        );
        if (accessTokenGenerated) {
          await reloadingCoins(
            context: context,
            userId: userId,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.showSnackBar(
          context,
          "Coins have been added successfully",
        );
        setState(
          () {
            reload = true;
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
        setState(
          () {
            reload = false;
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DOES_NOT_EXIST) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
        setState(
          () {
            reload = false;
          },
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Unable to reload coins",
        );
        setState(
          () {
            reload = false;
          },
        );
      }
    }
  }

  void showCustomDialogTds({
    @required String error,
    @required BuildContext context,
    @required String userId,
  }) {
    showDialog(
      context: context,
      barrierDismissible: false, // user must tap button!
      builder: (BuildContext context) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(
              5.0,
            ),
          ),
          elevation: 40,
          backgroundColor: ColorConstants.transparent,
          child: Stack(
            children: [
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
                  padding: const EdgeInsets.all(
                    1.0,
                  ),
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
                      children: [
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
                              error,
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
                        Row(
                          children: [
                            Expanded(
                              flex: 5,
                              child: GestureDetector(
                                onTap: () {
                                  showDialog(
                                    context: context,
                                    barrierDismissible: false,
                                    builder: (BuildContext context) {
                                      return WillPopScope(
                                        onWillPop: () {
                                          return Future.delayed(Duration.zero)
                                              .then(
                                            (value) => false,
                                          );
                                        },
                                        child: Center(
                                          child: CircularProgressIndicator(
                                            backgroundColor:
                                                ColorConstants.grey,
                                          ),
                                        ),
                                      );
                                    },
                                  );
                                  pokerToDgTransfer(
                                    userId: userId,
                                  );
                                },
                                child: Center(
                                  child: Text(
                                    StringConstants.ok,
                                    style: TextStyle(
                                      fontSize: 13.0.sp,
                                      fontWeight: FontWeight.bold,
                                      color: Colors.blue[600],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 5,
                              child: GestureDetector(
                                onTap: () {
                                  Navigator.of(context).pop();
                                },
                                child: Center(
                                  child: Text(
                                    "Cancel",
                                    style: TextStyle(
                                      fontSize: 13.0.sp,
                                      fontWeight: FontWeight.bold,
                                      color: Colors.blue[600],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ],
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
        );
      },
    );
  }
}
