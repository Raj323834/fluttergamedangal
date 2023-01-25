import 'dart:convert';
import 'dart:math' as math;

import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Model/wallet_info_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../Network/withdrawal_service.dart';
import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../kyc/kyc_screen.dart';
import '../profile/user_profile_screen.dart';
import 'models/last_withdrawal_dm.dart';
import 'repos/fetch_last_withdrawal_repo.dart';
import 'widgets/bank_details_field.dart';
import 'widgets/loyalty_benefits_field.dart';
import 'widgets/withdraw_list_tile.dart';

class WithdrawalScreen extends StatefulWidget {
  final String userId;
  const WithdrawalScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  _WithdrawalScreenState createState() => _WithdrawalScreenState();
}

class _WithdrawalScreenState extends State<WithdrawalScreen> {
  final _holderController = TextEditingController();
  final _accNumberController = TextEditingController();
  final _reAccNumberController = TextEditingController();
  final _ifscController = TextEditingController();
  final _amountController = TextEditingController();
  final _upiController = TextEditingController();

  bool amount = false,
      isFetchedAccount = false,
      isBankDetails = false,
      upiDetails = false,
      isLoyaltyBenefits = true;

  double balance = 0.0,
      minConfigured = 0.0,
      maxConfigured = 0.0,
      dailyTotalMaxInstantAmount = 0.0,
      dailyMaxUserWithdrawalAmount = 0.0,
      dailyMaxInstantAmountPerWithdrawal = 0.0;

  int dailyWithdrawalCount = 0;

  String holder = StringConstants.emptyString,
      accNum = StringConstants.emptyString,
      reAccNum = StringConstants.emptyString,
      ifsc = StringConstants.emptyString,
      vpa = StringConstants.emptyString;
  @override
  void initState() {
    fetchAccount(
      userId: widget.userId,
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: WITHDRAW_CASH_ROUTE,
    );
    super.initState();
  }

  void dispose() {
    _holderController.dispose();
    _accNumberController.dispose();
    _reAccNumberController.dispose();
    _ifscController.dispose();
    _amountController.dispose();
    _upiController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
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
        body: MultiProvider(
          providers: [
            ChangeNotifierProvider(
              create: (context) => WalletInfoModel(),
            ),
          ],
          child: Padding(
            padding: EdgeInsets.symmetric(
              horizontal: 14,
              vertical: 2.0.h,
            ),
            child: Consumer<WalletInfoModel>(
              builder: (
                context,
                walletInfoValue,
                child,
              ) {
                return SingleChildScrollView(
                  child: Column(
                    children: [
                      //Lable Header
                      LabelHeader(
                        label: 'Withdraw Cash',
                      ),
                      SizedBox(
                        height: 4.0.h,
                      ),
                      isFetchedAccount
                          ?
                          //Withdraw Container
                          Container(
                              width: double.infinity,
                              padding: EdgeInsets.all(
                                18,
                              ),
                              decoration: BoxDecoration(
                                border: Border.all(),
                                color: ColorConstants.greyContainer,
                                borderRadius: BorderRadius.circular(
                                  2.0.w,
                                ),
                              ),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  //Withdrawable Balance
                                  StreamBuilder(
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
                                      return Text(
                                        "Withdrawable Balance: ₹ " +
                                            walletInfoValue.withdrawalValue
                                                .toString(),
                                        style: TextStyle(
                                          fontSize: 20,
                                          fontWeight: FontWeight.w700,
                                          color: ColorConstants.white,
                                        ),
                                      );
                                    },
                                  ),
                                  SizedBox(
                                    height: 6.0.h,
                                  ),
                                  //Enter Amount Widget
                                  TextFormField(
                                    style: TextStyle(
                                      color: ColorConstants.white,
                                    ),
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                        RegExp(
                                          "[0-9]",
                                        ),
                                      ),
                                    ],
                                    onChanged: (value) {
                                      try {
                                        setState(
                                          () {
                                            balance = double.parse(
                                              walletInfoValue.withdrawalValue,
                                            );
                                          },
                                        );
                                        if (double
                                                    .parse(value) <
                                                minConfigured ||
                                            double.parse(value) >
                                                maxConfigured ||
                                            double.parse(value) > balance) {
                                          setState(
                                            () {
                                              amount = true;
                                            },
                                          );
                                        } else {
                                          setState(
                                            () {
                                              amount = false;
                                            },
                                          );
                                        }
                                      } catch (err) {}
                                    },
                                    maxLength: 6,
                                    controller: _amountController,
                                    keyboardType: TextInputType.phone,
                                    decoration: InputDecoration(
                                      isCollapsed: true,
                                      contentPadding: EdgeInsets.only(
                                        left: 10,
                                        top: 8.0.sp,
                                        bottom: 8.0.sp,
                                      ),
                                      border: OutlineInputBorder(
                                        borderSide: BorderSide(
                                          color: ColorConstants.white,
                                        ),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderRadius: BorderRadius.all(
                                          Radius.circular(5.0),
                                        ),
                                        borderSide: BorderSide(
                                          color: ColorConstants.white,
                                        ),
                                      ),
                                      focusedBorder: OutlineInputBorder(
                                        borderRadius: BorderRadius.all(
                                          Radius.circular(5.0),
                                        ),
                                        borderSide: BorderSide(
                                          color: ColorConstants.white,
                                        ),
                                      ),
                                      labelText: 'Enter Amount',
                                      counterText: StringConstants.emptyString,
                                      labelStyle: TextStyle(
                                        fontSize: 16,
                                        color: ColorConstants.grey600,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                  SizedBox(
                                    height: 6,
                                  ),
                                  // Error Text
                                  Visibility(
                                    visible: amount &&
                                        _amountController.text.isNotEmpty &&
                                        double.parse(
                                              _amountController.text,
                                            ) <
                                            minConfigured,
                                    child: Column(
                                      children: [
                                        Text(
                                          double.parse(
                                                    walletInfoValue
                                                        .withdrawalValue,
                                                  ) <
                                                  minConfigured
                                              ? "Minimum withdrawal amount is ₹ " +
                                                  minConfigured
                                                      .toInt()
                                                      .toString()
                                              : "Minimum withdrawable amount must be ₹ " +
                                                  minConfigured
                                                      .toInt()
                                                      .toString(),
                                          style: TextStyle(
                                            color: ColorConstants.red,
                                            fontSize: 8.0.sp,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  //Error Text
                                  Visibility(
                                    visible: amount &&
                                        _amountController.text.isNotEmpty &&
                                        double.parse(
                                              _amountController.text,
                                            ) >
                                            maxValue() &&
                                        double.parse(
                                              _amountController.text,
                                            ) >=
                                            minConfigured,
                                    child: Column(
                                      children: [
                                        Text(
                                          double.parse(
                                                    walletInfoValue
                                                        .withdrawalValue,
                                                  ) <
                                                  minConfigured
                                              ? "Minimum withdrawal amount is ₹ " +
                                                  minConfigured
                                                      .toInt()
                                                      .toString()
                                              : "Amount must be less than ₹ " +
                                                  maxValue().toInt().toString(),
                                          style: TextStyle(
                                            color: ColorConstants.red,
                                            fontSize: 8.0.sp,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  SizedBox(
                                    height: 3.0.h,
                                  ),
                                  //Bank Details Button
                                  WithdrawListTile(
                                    onTap: () {
                                      setState(
                                        () {
                                          isBankDetails = !isBankDetails;
                                        },
                                      );
                                    },
                                    title: 'Bank Details',
                                    isExpanded: isBankDetails,
                                  ),
                                  SizedBox(
                                    height: 0.6.h,
                                  ),
                                  //Bank Details
                                  Visibility(
                                    visible: isBankDetails,
                                    child: Padding(
                                      padding: const EdgeInsets.symmetric(
                                        vertical: 10.0,
                                      ),
                                      child: Column(
                                        children: [
                                          //Account Holder Name
                                          BankDetailsField(
                                            title: "Account Holder Name",
                                            keyboardType: TextInputType.name,
                                            controller: _holderController,
                                            maxLength: null,
                                            regex: RegExp(
                                              "[a-z A-Z]",
                                            ),
                                          ),

                                          SizedBox(
                                            height: 1.0.h,
                                          ),
                                          //Account Number
                                          BankDetailsField(
                                            title: "Account Number",
                                            keyboardType: TextInputType.phone,
                                            controller: _accNumberController,
                                            maxLength: 18,
                                            regex: RegExp(
                                              "[0-9]",
                                            ),
                                          ),
                                          SizedBox(
                                            height: 1.0.h,
                                          ),
                                          //Re-Enter Account Number
                                          BankDetailsField(
                                            title: "Re-enter Account Number",
                                            keyboardType: TextInputType.phone,
                                            controller: _reAccNumberController,
                                            maxLength: 18,
                                            regex: RegExp(
                                              "[0-9]",
                                            ),
                                          ),
                                          SizedBox(
                                            height: 1.0.h,
                                          ),
                                          //IFSC Code
                                          BankDetailsField(
                                            title: "IFSC Code",
                                            keyboardType: TextInputType.name,
                                            controller: _ifscController,
                                            maxLength: 11,
                                            regex: RegExp(
                                              "[a-zA-Z0-9]",
                                            ),
                                          ),
                                          SizedBox(
                                            height: 2.h,
                                          ),
                                          //Submit Bank Details
                                          GestureDetector(
                                            onTap: () async {
                                              FocusScope.of(context).unfocus();
                                              if (_holderController
                                                  .text.isEmpty) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "Enter the name of the Account Holder",
                                                );
                                              } else if (WithdrawalServiceValidator
                                                      .accountNumberValidator(
                                                    _accNumberController.text,
                                                  ) ||
                                                  ('0'
                                                          .allMatches(
                                                            _accNumberController
                                                                .text,
                                                          )
                                                          .length ==
                                                      _accNumberController
                                                          .text.length)) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  'Enter a valid Account number',
                                                );
                                              } else if (_accNumberController
                                                      .text !=
                                                  _reAccNumberController.text) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  'Account numbers must be equal!',
                                                );
                                              } else if (WithdrawalServiceValidator
                                                  .ifscCodeValidator(
                                                _ifscController.text,
                                              )) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  'Enter a valid IFSC Code',
                                                );
                                              } else if (_amountController
                                                          .text ==
                                                      StringConstants
                                                          .emptyString ||
                                                  int.parse(
                                                        _amountController.text,
                                                      ) ==
                                                      0) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  'Enter some amount',
                                                );
                                              } else if (int.parse(
                                                        _amountController.text,
                                                      ) <
                                                      minConfigured ||
                                                  int.parse(
                                                        _amountController.text,
                                                      ) >
                                                      maxConfigured ||
                                                  int.parse(
                                                        _amountController.text,
                                                      ) >
                                                      balance) {
                                                if (balance == 0.0) {
                                                  CommonMethods.showSnackBar(
                                                    context,
                                                    "You must have a minimum Balance of $minConfigured to withdraw.",
                                                  );
                                                } else if (balance <
                                                    minConfigured) {
                                                  CommonMethods.showSnackBar(
                                                    context,
                                                    "Minimum withdrawable amount is $minConfigured",
                                                  );
                                                } else if (int.parse(
                                                      _amountController.text,
                                                    ) >
                                                    maxConfigured) {
                                                  CommonMethods.showSnackBar(
                                                    context,
                                                    "Maximum withdrawal amount must be ₹ " +
                                                        maxValue()
                                                            .toInt()
                                                            .toString(),
                                                  );
                                                } else if (double.parse(
                                                      _amountController.text,
                                                    ) >
                                                    balance) {
                                                  CommonMethods.showSnackBar(
                                                    context,
                                                    "Maximum withdrawal amount must be ₹ " +
                                                        maxValue()
                                                            .toInt()
                                                            .toString(),
                                                  );
                                                } else if (double.parse(
                                                      _amountController.text,
                                                    ) <
                                                    minConfigured) {
                                                  CommonMethods.showSnackBar(
                                                    context,
                                                    "Minimum withdrawable amount must be ₹ " +
                                                        minConfigured
                                                            .toInt()
                                                            .toString(),
                                                  );
                                                }
                                              } else {
                                                showDialog(
                                                  context: context,
                                                  barrierDismissible: false,
                                                  builder:
                                                      (BuildContext context) {
                                                    return Center(
                                                      child:
                                                          CircularProgressIndicator(
                                                        backgroundColor:
                                                            ColorConstants.blue,
                                                      ),
                                                    );
                                                  },
                                                );
                                                if (_holderController.text ==
                                                        holder &&
                                                    _accNumberController.text ==
                                                        accNum &&
                                                    _reAccNumberController
                                                            .text ==
                                                        reAccNum &&
                                                    _ifscController.text ==
                                                        ifsc) {
                                                  await _handleWithdrawCash(
                                                    mode: ResponsesKeys.ACCOUNT,
                                                    amount:
                                                        _amountController.text,
                                                    accNum: _accNumberController
                                                        .text,
                                                    ifscCode:
                                                        _ifscController.text,
                                                    name:
                                                        _holderController.text,
                                                    vpa: StringConstants
                                                        .emptyString,
                                                    isNew: false,
                                                  );
                                                } else {
                                                  await _handleWithdrawCash(
                                                    mode: ResponsesKeys.ACCOUNT,
                                                    amount:
                                                        _amountController.text,
                                                    accNum: _accNumberController
                                                        .text,
                                                    ifscCode:
                                                        _ifscController.text,
                                                    name:
                                                        _holderController.text,
                                                    vpa: StringConstants
                                                        .emptyString,
                                                    isNew: true,
                                                  );
                                                }
                                              }
                                            },
                                            child: Container(
                                              width: 25.0.w,
                                              height: 34,
                                              decoration: BoxDecoration(
                                                gradient:
                                                    ColorConstants.goldGradient,
                                                borderRadius:
                                                    BorderRadius.circular(
                                                  5,
                                                ),
                                              ),
                                              child: Center(
                                                child: Text(
                                                  "Submit",
                                                  style: TextStyle(
                                                    color: ColorConstants.white,
                                                    fontWeight: FontWeight.bold,
                                                    fontSize: 9.0.sp,
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                  SizedBox(
                                    height: 2.0.h,
                                  ),
                                  //UPI & Wallet Button
                                  WithdrawListTile(
                                    onTap: () {
                                      setState(
                                        () {
                                          upiDetails = !upiDetails;
                                        },
                                      );
                                    },
                                    isExpanded: upiDetails,
                                    title: "UPI & Wallet",
                                  ),
                                  SizedBox(
                                    height: 2.h,
                                  ),
                                  //Enter UPI Details
                                  Visibility(
                                    visible: upiDetails,
                                    child: Column(
                                      children: [
                                        Padding(
                                          padding: const EdgeInsets.symmetric(
                                            horizontal: 15,
                                          ),
                                          child:
                                              //Enter UPI Field
                                              Container(
                                            height: 5.0.h,
                                            decoration: BoxDecoration(
                                              borderRadius: BorderRadius.all(
                                                Radius.circular(
                                                  5,
                                                ),
                                              ),
                                            ),
                                            child: TextFormField(
                                              style: TextStyle(
                                                color: ColorConstants.white,
                                              ),
                                              controller: _upiController,
                                              keyboardType:
                                                  TextInputType.emailAddress,
                                              decoration: InputDecoration(
                                                prefixIcon: Padding(
                                                  padding: const EdgeInsets
                                                      .symmetric(
                                                    vertical: 6,
                                                  ),
                                                  child: Image.asset(
                                                    AssetPaths.bhimUPI,
                                                    height: 4.0.h,
                                                  ),
                                                ),
                                                contentPadding: EdgeInsets.only(
                                                  top: 5.0.sp,
                                                ),
                                                border: OutlineInputBorder(
                                                  borderSide: BorderSide(
                                                    color: ColorConstants.white,
                                                  ),
                                                ),
                                                enabledBorder:
                                                    OutlineInputBorder(
                                                  borderRadius:
                                                      BorderRadius.all(
                                                    Radius.circular(
                                                      5.0,
                                                    ),
                                                  ),
                                                  borderSide: BorderSide(
                                                    color: ColorConstants.white,
                                                  ),
                                                ),
                                                focusedBorder:
                                                    OutlineInputBorder(
                                                  borderRadius:
                                                      BorderRadius.all(
                                                    Radius.circular(
                                                      5.0,
                                                    ),
                                                  ),
                                                  borderSide: BorderSide(
                                                    color: ColorConstants.white,
                                                  ),
                                                ),
                                                labelText: 'Enter BHIM/UPI',
                                                labelStyle: TextStyle(
                                                  fontSize: 9.4.sp,
                                                  color: ColorConstants.white,
                                                  fontWeight: FontWeight.bold,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        SizedBox(
                                          height: 2.h,
                                        ),
                                        //Submit UPI
                                        GestureDetector(
                                          onTap: () async {
                                            FocusScope.of(context).unfocus();
                                            if (_upiController.text.isEmpty) {
                                              CommonMethods.showSnackBar(
                                                context,
                                                'Please enter your UPI ID',
                                              );
                                            } else if (_amountController
                                                    .text.isEmpty ||
                                                double.parse(
                                                      _amountController.text,
                                                    ) ==
                                                    0) {
                                              CommonMethods.showSnackBar(
                                                context,
                                                'Please enter some amount',
                                              );
                                            } else if (double.parse(
                                                      _amountController.text,
                                                    ) <
                                                    minConfigured ||
                                                double.parse(
                                                      _amountController.text,
                                                    ) >
                                                    maxConfigured ||
                                                double.parse(
                                                      _amountController.text,
                                                    ) >
                                                    balance) {
                                              if (balance == 0.0) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "You do not have enough balance to withdraw.",
                                                );
                                              } else if (balance <
                                                  minConfigured) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "Minimum withdrawable amount is $minConfigured",
                                                );
                                              } else if (double.parse(
                                                      _amountController.text) >
                                                  maxConfigured) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "Maximum withdrawal amount must be ₹ " +
                                                      maxValue()
                                                          .toInt()
                                                          .toString(),
                                                );
                                              } else if (double.parse(
                                                    _amountController.text,
                                                  ) >
                                                  balance) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "Maximum withdrawal amount must be ₹ " +
                                                      maxValue()
                                                          .toInt()
                                                          .toString(),
                                                );
                                              } else if (double.parse(
                                                    _amountController.text,
                                                  ) <
                                                  minConfigured) {
                                                CommonMethods.showSnackBar(
                                                  context,
                                                  "Minimum withdrawable amount must be ₹ " +
                                                      minConfigured
                                                          .toInt()
                                                          .toString(),
                                                );
                                              }
                                            } else {
                                              showDialog(
                                                context: context,
                                                barrierDismissible: false,
                                                builder:
                                                    (BuildContext context) {
                                                  return Center(
                                                    child:
                                                        CircularProgressIndicator(
                                                      backgroundColor:
                                                          ColorConstants.blue,
                                                    ),
                                                  );
                                                },
                                              );
                                              if (_upiController.text == vpa) {
                                                await _handleWithdrawCash(
                                                  mode: "UPI",
                                                  amount:
                                                      _amountController.text,
                                                  accNum: StringConstants
                                                      .emptyString,
                                                  ifscCode: StringConstants
                                                      .emptyString,
                                                  name: StringConstants
                                                      .emptyString,
                                                  vpa: _upiController.text,
                                                  isNew: false,
                                                );
                                              } else {
                                                await _handleWithdrawCash(
                                                  mode: "UPI",
                                                  amount:
                                                      _amountController.text,
                                                  accNum: StringConstants
                                                      .emptyString,
                                                  ifscCode: StringConstants
                                                      .emptyString,
                                                  name: StringConstants
                                                      .emptyString,
                                                  vpa: _upiController.text,
                                                  isNew: true,
                                                );
                                              }
                                            }
                                          },
                                          child: Container(
                                            width: 25.0.w,
                                            height: 34,
                                            decoration: BoxDecoration(
                                              gradient:
                                                  ColorConstants.goldGradient,
                                              borderRadius:
                                                  BorderRadius.circular(
                                                5,
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "Submit",
                                                style: TextStyle(
                                                  color: ColorConstants.white,
                                                  fontWeight: FontWeight.bold,
                                                  fontSize: 9.0.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        SizedBox(
                                          height: 3.0.h,
                                        ),
                                      ],
                                    ),
                                  ),
                                  //Loyalty Benefits
                                  WithdrawListTile(
                                    onTap: () {
                                      setState(
                                        () {
                                          isLoyaltyBenefits =
                                              !isLoyaltyBenefits;
                                        },
                                      );
                                    },
                                    isExpanded: isLoyaltyBenefits,
                                    title: 'Loyalty Benefits',
                                  ),
                                  SizedBox(
                                    height: 2.5.h,
                                  ),
                                  Visibility(
                                    visible: isLoyaltyBenefits,
                                    child: Padding(
                                      padding: const EdgeInsets.symmetric(
                                        horizontal: 15,
                                      ),
                                      child: Column(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        children: [
                                          Center(
                                            child: Text(
                                              'Congrats! You\'re eligible for instant withdrawal',
                                              style: TextStyle(
                                                fontSize: 16,
                                                fontWeight: FontWeight.w400,
                                                color: ColorConstants.white,
                                              ),
                                              textAlign: TextAlign.center,
                                            ),
                                          ),
                                          SizedBox(
                                            height: 2.h,
                                          ),
                                          LoyaltyBenefitsField(
                                            title: StringConstants
                                                .totalAmountYouCanWithdraw,
                                            value: '₹' +
                                                dailyTotalMaxInstantAmount
                                                    .toString(),
                                          ),
                                          SizedBox(
                                            height: 1.5.h,
                                          ),
                                          LoyaltyBenefitsField(
                                            title: StringConstants
                                                .noOfInstantWithdrawals,
                                            value:
                                                dailyWithdrawalCount.toString(),
                                          ),
                                          SizedBox(
                                            height: 1.5.h,
                                          ),
                                          LoyaltyBenefitsField(
                                            title: 'Max amount per withdrawal',
                                            value: '₹' +
                                                dailyMaxInstantAmountPerWithdrawal
                                                    .toString(),
                                          ),
                                          SizedBox(
                                            height: 3.h,
                                          ),
                                          Text(
                                            StringConstants.loyaltyBenefitsNote,
                                            style: TextStyle(
                                              fontSize: 12,
                                              fontWeight: FontWeight.w500,
                                              // fontStyle: FontStyle.italic,
                                              color: ColorConstants
                                                  .textFieldTextCol,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  )
                                ],
                              ),
                            )
                          : CircularProgressIndicator(),
                    ],
                  ),
                );
              },
            ),
          ),
        ),
      ),
    );
  }

  //METHODS
  fetchAccount({
    @required String userId,
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
      var repoObj = FetchLastWithdrawalRepo();
      LastWithdrawalDM lastWithdrawalDM =
          await repoObj.fetchLastSuccessfullWithdrawal(
        userId: userId,
      );
      if (lastWithdrawalDM != null) {
        switch (lastWithdrawalDM.result) {
          case ResponseStatus.success:
            switch (lastWithdrawalDM.lastPaymentMode) {
              case ResponseStatus.account:
                setState(
                  () {
                    _holderController.text = lastWithdrawalDM.accountHolderName;
                    holder = lastWithdrawalDM.accountHolderName;
                    _accNumberController.text = lastWithdrawalDM.accountNumber;
                    accNum = lastWithdrawalDM.accountNumber;
                    _reAccNumberController.text =
                        lastWithdrawalDM.accountNumber;
                    reAccNum = lastWithdrawalDM.accountNumber;
                    _ifscController.text = lastWithdrawalDM.ifscCode;
                    ifsc = lastWithdrawalDM.ifscCode;
                    minConfigured = lastWithdrawalDM.minWithdrawalAmount;
                    maxConfigured = lastWithdrawalDM.maxWithdrawalAmount;
                    isFetchedAccount = true;
                    _upiController.text = lastWithdrawalDM.vpa;
                    vpa = lastWithdrawalDM.vpa;
                    dailyTotalMaxInstantAmount =
                        lastWithdrawalDM.dailyTotalMaxInstantAmount;
                    dailyMaxUserWithdrawalAmount =
                        lastWithdrawalDM.dailyMaxUserWithdrawalAmount;
                    dailyMaxInstantAmountPerWithdrawal =
                        lastWithdrawalDM.dailyMaxInstantAmountPerWithdrawal;
                    dailyWithdrawalCount =
                        lastWithdrawalDM.dailyMaxInstantCount.toInt();
                    isBankDetails = true;
                  },
                );
                break;

              case ResponseStatus.upi:
                setState(
                  () {
                    _holderController.text = lastWithdrawalDM.accountHolderName;
                    holder = lastWithdrawalDM.accountHolderName;
                    _accNumberController.text = lastWithdrawalDM.accountNumber;
                    accNum = lastWithdrawalDM.accountNumber;
                    _reAccNumberController.text =
                        lastWithdrawalDM.accountNumber;
                    reAccNum = lastWithdrawalDM.accountNumber;
                    _ifscController.text = lastWithdrawalDM.ifscCode;
                    ifsc = lastWithdrawalDM.ifscCode;
                    _upiController.text = lastWithdrawalDM.vpa;
                    vpa = lastWithdrawalDM.vpa;
                    minConfigured = lastWithdrawalDM.minWithdrawalAmount;
                    maxConfigured = lastWithdrawalDM.maxWithdrawalAmount;
                    dailyTotalMaxInstantAmount =
                        lastWithdrawalDM.dailyTotalMaxInstantAmount;
                    dailyMaxUserWithdrawalAmount =
                        lastWithdrawalDM.dailyMaxUserWithdrawalAmount;
                    dailyMaxInstantAmountPerWithdrawal =
                        lastWithdrawalDM.dailyMaxInstantAmountPerWithdrawal;
                    dailyWithdrawalCount =
                        lastWithdrawalDM.dailyMaxInstantCount.toInt();
                    isFetchedAccount = true;
                    upiDetails = true;
                  },
                );
                break;
              default:
            }

            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await fetchAccount(
                userId: userId,
              );
            }
            break;

          case ResponseStatus.kycNotVerified:
            setState(
              () {
                isFetchedAccount = true;
              },
            );
            if (!lastWithdrawalDM.idVerified |
                !lastWithdrawalDM.addressVerified) {
              CommonMethods.showSnackBar(
                context,
                "Your KYC is not verified",
              );
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return customKycDialogBox(
                    context: context,
                    userId: userId,
                  );
                },
              );
            } else if (!lastWithdrawalDM.mobileVerified) {
              CommonMethods.showSnackBar(
                context,
                "Your Mobile Number is not verified",
              );
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return customUserDialogBox(
                    context: context,
                    buttonText: "Verify Mobile",
                    header: "You must verify Mobile Number",
                    userId: userId,
                  );
                },
              );
            } else if (!lastWithdrawalDM.emailVerified) {
              CommonMethods.showSnackBar(
                context,
                "Your Email Id is not verified",
              );
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return customUserDialogBox(
                    context: context,
                    buttonText: "Verify Email",
                    header: "You must verify Email Id",
                    userId: userId,
                  );
                },
              );
            }
            break;

          case ResponseStatus.noSuccessfulWithdrawalExist:
            setState(
              () {
                minConfigured = lastWithdrawalDM.minWithdrawalAmount;
                maxConfigured = lastWithdrawalDM.maxWithdrawalAmount;
                dailyTotalMaxInstantAmount =
                    lastWithdrawalDM.dailyTotalMaxInstantAmount;
                dailyMaxUserWithdrawalAmount =
                    lastWithdrawalDM.dailyMaxUserWithdrawalAmount;
                dailyMaxInstantAmountPerWithdrawal =
                    lastWithdrawalDM.dailyMaxInstantAmountPerWithdrawal;
                dailyWithdrawalCount =
                    lastWithdrawalDM.dailyMaxInstantCount.toInt();
                isFetchedAccount = true;
              },
            );
            break;

          case ResponseStatus.upsNotReachable:
            CommonMethods.showSnackBar(
              context,
              'Unable to fetch last withdrawal',
            );
            break;

          default:
            CommonMethods.showSnackBar(
              context,
              'Unable to fetch last withdrawal',
            );
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          'Unable to fetch last withdrawal',
        );
      }
    }
  }

  Widget title(
    BuildContext context,
    String title,
  ) {
    return Padding(
      padding: const EdgeInsets.only(
        left: 15,
        right: 15,
      ),
      child: Container(
        height: 5.0.h,
        decoration: BoxDecoration(
          gradient: ColorConstants.blueDarkGradient,
          borderRadius: BorderRadius.circular(
            2.0.w,
          ),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(
              width: 2.5.w,
            ),
            Text(
              title,
              style: TextStyle(
                fontSize: 11.0.sp,
                fontWeight: FontWeight.bold,
                color: ColorConstants.white,
              ),
            ),
            Spacer(),
            Transform.rotate(
              angle: 90 * math.pi / 180,
              child: Icon(
                Icons.double_arrow,
                color: ColorConstants.white,
              ),
            ),
            SizedBox(
              width: 2.5.w,
            )
          ],
        ),
      ),
    );
  }

  Widget customKycDialogBox({
    @required BuildContext context,
    @required String userId,
  }) {
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
          height: 23.0.h,
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
                "You must complete your KYC",
                style: TextStyle(
                  fontSize: 13.5.sp,
                  color: ColorConstants.grey700,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Text(
                "before doing any Withdrawal.",
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
                      Navigator.pop(context);
                      Navigator.push(
                        context,
                        CupertinoPageRoute(
                          builder: (context) => KycScreen(
                            userId: userId,
                          ),
                        ),
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
                          "Complete KYC",
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
                            fontSize: 13.5.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget customUserDialogBox({
    @required BuildContext context,
    @required String buttonText,
    @required String header,
    @required String userId,
  }) {
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
          height: 23.0.h,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(2.0.w),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              SizedBox(
                height: 4.0.h,
              ),
              Text(
                header,
                style: TextStyle(
                  fontSize: 13.5.sp,
                  color: ColorConstants.grey700,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Text(
                "before doing any Withdrawal.",
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
                      Navigator.pop(context);
                      Navigator.push(
                        context,
                        CupertinoPageRoute(
                          builder: (context) => UserProfileScreen(
                            fromDialog: true,
                            userId: userId,
                          ),
                        ),
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
                          buttonText,
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
                            fontSize: 13.5.sp,
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

  _handleWithdrawCash({
    @required String mode,
    @required String amount,
    @required String accNum,
    @required String ifscCode,
    @required String name,
    @required String vpa,
    @required bool isNew,
  }) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Withdrawing Cash----------",
    );
    Map<String, Object> result = await WithdrawalService.withdrawTransaction(
      amount,
      accNum,
      ifscCode,
      name,
      mode,
      vpa,
      isNew,
      widget.userId,
    );
    if (result.containsKey('noInternet')) {
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      Navigator.of(
        context,
        rootNavigator: true,
      ).pop();
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (context) {
          return BaseErrorDialog(
            title: StringConstants.emptyString,
            error:
                "There was some error during transaction. Please try after sometime!",
          );
        },
      );
    } else {
      Map<String, Object> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "There was some error during transaction. Please try after sometime!",
            );
          },
        );
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          widget.userId,
        );
        if (accessTokenGenerated) {
          await _handleWithdrawCash(
            mode: mode,
            amount: amount,
            accNum: accNum,
            ifscCode: ifscCode,
            name: name,
            vpa: vpa,
            isNew: isNew,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        String id = responseMap['withdrawal_id'].toString();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  'Your withdrawal has been successfully placed. Reference no : $id',
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.BANNED_STATE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "This functionality is not available in your state",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.DAILY_AMOUNT_LIMIT_EXCEEDED) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "You cannot place withdrawal as you have reached Daily Amount Limit $dailyMaxUserWithdrawalAmount",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.RESTRICTED_ACTIVITY) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "This functionality has been blocked for your account based on your location/KYC/account history.",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.DAILY_COUNT_LIMIT_EXCEEDED) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  'You cannot place withdrawal as you have reached Daily Withdrawal Count Limit $dailyWithdrawalCount',
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.INVALID_WITHDRAWAL_AMOUNT) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: 'Amount must be in range $minConfigured - $maxConfigured',
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.INSUFFICIENT_BALANCE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Your balance is not sufficient to place Withdrawal",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.ACCOUNT_NUMBER_MISSING) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Please provide Account Number",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.IFSC_CODE_MISSING) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Please provide IFSC Code",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.ACCOUNT_HOLDER_NAME_MISSING) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Please provide Account Holder Name",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.VPA_MISSING) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Please provide UPI Id",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Internal server error. Please try again",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.INVALID_IFSC_CODE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Please provide valid IFSC Code",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.INVALID_ACCOUNT_HOLDER_NAME) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "You cannot place withdrawal as account holder name does not match with your profile name",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.UPS_NOT_REACHABLE) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Internal server error. Please try again",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.DEBIT_CURRENCY_FAILED) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: "Internal server error. Please try again",
            );
          },
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DISABLED) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "Withdrawal is rejected as your wallet is Disabled currently",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.PENDING_WITHDRAWAL_EXIST) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "You cannot place withdrawal as you already have a pending withdrawal",
            );
          },
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.PENDING_WITHDRAWAL_EXIST) {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error:
                  "Withdrawal is rejected as your wallet is Disabled currently",
            );
          },
        );
      } else {
        Navigator.of(
          context,
          rootNavigator: true,
        ).pop();
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.emptyString,
              error: 'Internal server error. Please try again',
            );
          },
        );
      }
    }
  }

  double maxValue() {
    if (balance < maxConfigured) {
      return balance;
    } else {
      return maxConfigured;
    }
  }
}
