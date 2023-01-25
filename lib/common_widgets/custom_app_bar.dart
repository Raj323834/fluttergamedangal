import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../Model/wallet_info_model.dart';
import '../Network/web_socket_helper_service.dart';
import '../constants/app_constants.dart';
import '../constants/asset_paths.dart';
import '../constants/color_constants.dart';
import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/string_constants.dart';
import '../constants/web_socket_topics.dart';
import '../modules/home/home.dart';
import '../utils/shared_pref_service.dart';

class CustomAppBar extends StatefulWidget {
  final String from;
  final String userId;
  final Color bgColor;

  CustomAppBar({
    this.from = StringConstants.emptyString,
    this.userId = StringConstants.emptyString,
    this.bgColor,
  });

  @override
  _CustomAppBarState createState() => _CustomAppBarState();
}

class _CustomAppBarState extends State<CustomAppBar> {
  bool reload = false;
  int avatarId = 1;

  @override
  void initState() {
    CommonMethods.printLog(
      "USERID test: CUSTOMAPPBAR : ",
      widget.userId,
    );
    asyncMethod();
    super.initState();
  }

  Future<void> asyncMethod() async {
    avatarId = await SharedPrefService.getIntValuesFromSharedPref(
          'avatarId',
        ) ??
        1;
    setState(
      () {
        avatarId = avatarId;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (context) => WalletInfoModel(),
        ),
      ],
      child: Container(
        height: 8.0.h,
        decoration: BoxDecoration(
          color: ColorConstants.kBackgroundColor,
        ),
        width: double.infinity,
        padding: EdgeInsets.symmetric(
          horizontal: 10,
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Expanded(
              flex: 7,
              child: Row(
                children: [
                  //Back Arrow
                  GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                      FocusScope.of(context).unfocus();
                    },
                    child: Icon(
                      Icons.arrow_back,
                      color: ColorConstants.white,
                    ),
                  ),
                ],
              ),
            ),
            Consumer<WalletInfoModel>(
              builder: (
                context,
                walletInfoValue,
                child,
              ) =>
                  StreamBuilder(
                stream: sockets.streamController.stream,
                builder: (context, snapshot) {
                  try {
                    if (snapshot.hasData) {
                      var snapBody = jsonDecode(
                        snapshot.data,
                      );
                      if (snapBody['type'] == WebSocketTopics.userCashBalance) {
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
                      }
                    }
                  } catch (e) {}
                  //ADD CASH BUTTON
                  return Expanded(
                    flex: 3,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(
                        vertical: 12.0,
                      ),
                      child: GestureDetector(
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
                        child: Container(
                          decoration: BoxDecoration(
                            gradient: ColorConstants.greenGradient,
                            borderRadius: BorderRadius.circular(
                              5,
                            ),
                          ),
                          child: Padding(
                            padding: EdgeInsets.all(2.0.w),
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
                                Center(
                                  child: Text(
                                    (walletInfoValue.combinedValue < 100000)
                                        ? '₹' +
                                            (walletInfoValue.combinedValue
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
                                            walletInfoValue.combinedValue,
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
                                Container(
                                  height: 4.3.w,
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
    );
  }
}
