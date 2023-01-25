import 'dart:async';
import 'dart:convert';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:dashed_container/dashed_container.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:razorpay_flutter/razorpay_flutter.dart';
import 'package:sizer/sizer.dart';
import 'package:toast/toast.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Model/logging_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/payment_service.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../../utils/singleton.dart';
import '../kyc/kyc_screen.dart';
import 'models/promo_codes_dm.dart';
import 'repos/fetch_promocodes_repo.dart';
import 'widgets/apply_promocode.dart';
import 'widgets/money_container.dart';

class AddCash extends StatefulWidget {
  final void Function(int newIndex) changeIndex;
  final String bonusC;
  final String userId;
  final FlutterWebviewPlugin webviewInstance;

  const AddCash({
    Key key,
    @required this.changeIndex,
    @required this.bonusC,
    @required this.userId,
    @required this.webviewInstance,
  }) : super(key: key);

  @override
  _AddCashState createState() => _AddCashState();
}

class _AddCashState extends State<AddCash> {
  final List<String> moneyContainerValues = [
    "200",
    "500",
    "1000",
    "10000",
  ];
  String mid = FlavorConfig.instance.variables["paytm_mid"],
      orderId = StringConstants.emptyString;
  bool amount = false;

  String orderAmount = StringConstants.emptyString;
  String promocode = StringConstants.emptyString,
      promotext = StringConstants.emptyString,
      bonusId = StringConstants.emptyString;
  int minAmount = 50, maxAmount = 500000, minAmountServer = 50;
  String referenceId;
  String signature;
  String txMsg;
  String paymentMode;
  String txTime;
  String txStatus;
  String gateway = "razorpay";
  String transactionId = StringConstants.emptyString;
  DateTime currentBackPressTime;

  final _cashController = TextEditingController();
  final _promoController = TextEditingController();

  List<Bonuses> bonuses = [];
  int itemCount = 0;
  bool isPromoApplied = false, isPromoFetched = false, isAddCash = false;
  List<bool> moneyContainerSelected = [
    false,
    false,
    false,
    false,
  ];

  Razorpay _razorpay = Razorpay();

  @override
  void initState() {
    asyncMethod(widget.userId);
    if (widget.webviewInstance != null) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "----------Webview Instance not null----------",
      );
    }
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: ADD_CASH_ROUTE,
    );
    super.initState();
    razorPayEventListeners();
  }

  void dispose() {
    _cashController.dispose();
    _promoController.dispose();
    _razorpay.clear();
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
      child: SafeArea(
        child: Scaffold(
          resizeToAvoidBottomInset: false,
          body: Container(
            height: Singleton().deviceSize.height,
            color: ColorConstants.kBackgroundColor,
            child: SingleChildScrollView(
              child: Center(
                child: Padding(
                  padding: EdgeInsets.symmetric(
                    horizontal: 10.0.w,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      //Add Cash Header
                      Padding(
                        padding: EdgeInsets.only(
                          top: 5.0.h,
                          bottom: 1.0.h,
                        ),
                        child: Text(
                          "add_cash_screen.add_cash".tr(),
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: ColorConstants.white,
                            fontSize: 20.0.sp,
                          ),
                        ),
                      ),
                      //Enter Amount Text
                      Text(
                        "add_cash_screen.enter_amt".tr(),
                        style: TextStyle(
                          color: ColorConstants.lightGrey,
                        ),
                      ),
                      SizedBox(
                        height: 0.5.h,
                      ),
                      //Enter Amount Text Field
                      TextFormField(
                        maxLength: 7,
                        onChanged: (value) {
                          setState(
                            () {
                              moneyContainerSelected = [
                                false,
                                false,
                                false,
                                false,
                              ];
                            },
                          );
                          if (_cashController.text.length == 0) {
                            setState(
                              () {
                                isAddCash = true;
                              },
                            );
                          } else {
                            setState(
                              () {
                                isAddCash = false;
                              },
                            );
                          }
                          try {
                            if (int.parse(_cashController.text) < minAmount) {
                              setState(
                                () {
                                  _promoController.clear();
                                  minAmount = minAmountServer;
                                  bonusId = StringConstants.emptyString;
                                },
                              );
                            }
                            if (int.parse(value) < minAmount ||
                                int.parse(value) > 500000) {
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
                        maxLines: 1,
                        inputFormatters: [
                          FilteringTextInputFormatter.allow(
                            RegExpMethods.digits,
                          ),
                        ],
                        keyboardType: TextInputType.number,
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 15.sp,
                        ),
                        controller: _cashController,
                        decoration: InputDecoration(
                          fillColor: ColorConstants.white,
                          counterText: StringConstants.emptyString,
                          contentPadding: EdgeInsets.only(
                            left: 3.0.w,
                            top: 2.0.h,
                            bottom: 2.0.h,
                          ),
                          isCollapsed: true,
                          border: OutlineInputBorder(),
                          enabledBorder: OutlineInputBorder(),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(
                              width: 0.5.w,
                              color: ColorConstants.grey,
                            ),
                            borderRadius: BorderRadius.all(
                              Radius.circular(
                                2.0.w,
                              ),
                            ),
                          ),
                          filled: true,
                          labelStyle: TextStyle(
                            fontSize: 12.0.sp,
                            color: ColorConstants.black,
                          ),
                        ),
                      ),
                      SizedBox(
                        height: 1.0.h,
                      ),
                      //Money Containers (200,500,1000,10000)
                      Row(
                        children: [
                          moneyContainer(
                            moneyContainerValues[0],
                            0,
                          ),
                          moneyContainer(
                            moneyContainerValues[1],
                            1,
                          ),
                          moneyContainer(
                            moneyContainerValues[2],
                            2,
                          ),
                          moneyContainer(
                            moneyContainerValues[3],
                            3,
                          ),
                        ],
                      ),
                      SizedBox(
                        height: 1.0.h,
                      ),
                      //Selected Promo Code
                      Visibility(
                        visible: _promoController.text.isNotEmpty,
                        child: Column(
                          children: [
                            SizedBox(
                              height: 2.0.h,
                            ),
                            Padding(
                              padding: const EdgeInsets.only(
                                left: 20,
                                right: 20,
                              ),
                              child: DashedContainer(
                                child: Container(
                                  height: 7.h,
                                  width: double.infinity,
                                  decoration: BoxDecoration(
                                    color: ColorConstants.white.withOpacity(
                                      0.9,
                                    ),
                                  ),
                                  child: Row(
                                    children: [
                                      //Promo Code
                                      Expanded(
                                        flex: 3,
                                        child: Center(
                                          child: Text(
                                            promocode,
                                            style: TextStyle(
                                              fontSize: 10.0.sp,
                                              fontWeight: FontWeight.bold,
                                              color: ColorConstants.black,
                                            ),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        height: 4.6.h,
                                        width: 1,
                                        decoration: BoxDecoration(
                                          border: Border.all(
                                            color: ColorConstants.white
                                                .withOpacity(
                                              0.9,
                                            ),
                                          ),
                                        ),
                                      ),
                                      //Promo Text
                                      Expanded(
                                        flex: 7,
                                        child: Text(
                                          promotext,
                                          style: TextStyle(
                                            fontSize: 8.5.sp,
                                            fontWeight: FontWeight.bold,
                                          ),
                                          textAlign: TextAlign.center,
                                        ),
                                      ),
                                      //Cancel Promo
                                      Align(
                                        alignment: Alignment.topRight,
                                        child: Padding(
                                          padding: EdgeInsets.only(
                                            top: 2,
                                            right: 2,
                                          ),
                                          child: GestureDetector(
                                            onTap: () {
                                              setState(
                                                () {
                                                  _promoController.clear();
                                                  minAmount = minAmountServer;
                                                  bonusId = StringConstants
                                                      .emptyString;
                                                },
                                              );
                                            },
                                            child: Icon(
                                              Icons.cancel,
                                              size: 15.sp,
                                              color: ColorConstants.grey,
                                            ),
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                dashColor: ColorConstants.white,
                                strokeWidth: 2,
                              ),
                            ),
                            SizedBox(
                              height: 1.0.h,
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        height: 2.0.h,
                      ),
                      //Promocodes & Offers Text
                      Text(
                        "Promocodes & Offers",
                        style: TextStyle(
                          color: ColorConstants.white,
                          fontWeight: FontWeight.bold,
                          fontSize: 18.0.sp,
                        ),
                      ),
                      SizedBox(
                        height: 2.0.h,
                      ),
                      Container(
                        height: 18.0.h,
                        child: itemCount == 0
                            ? Center(
                                child: !isPromoFetched
                                    ? CircularProgressIndicator()
                                    : Text(
                                        "You do not have any Bonuses right now!",
                                        style: TextStyle(
                                          color: ColorConstants.grey,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                              )
                            : Container(
                                height: 18.0.h,
                                child: ListView.builder(
                                  itemCount: bonuses.length,
                                  itemBuilder:
                                      (BuildContext context, int index) {
                                    return Visibility(
                                      visible: bonuses[index].isVisibleInUI ==
                                              "true" &&
                                          bonuses[index].applicable,
                                      child: Align(
                                        child: Padding(
                                          padding: const EdgeInsets.only(
                                            left: 5,
                                            right: 5,
                                          ),
                                          child: Column(
                                            children: [
                                              GestureDetector(
                                                onTap: () {
                                                  if (bonuses[index]
                                                      .applicable) {
                                                    minAmount = bonuses[index]
                                                        .minAmount
                                                        .toInt();
                                                    if (_cashController
                                                        .text.isEmpty) {
                                                      CommonMethods.showSnackBar(
                                                          context,
                                                          "Add ₹" +
                                                              minAmount
                                                                  .toString() +
                                                              " to avail this offer");
                                                    } else if (int.parse(
                                                            _cashController
                                                                .text) <
                                                        minAmount) {
                                                      int currentAmount =
                                                          int.parse(
                                                        _cashController.text,
                                                      );
                                                      CommonMethods
                                                          .showSnackBar(
                                                        context,
                                                        "Add ₹" +
                                                            (minAmount -
                                                                    currentAmount)
                                                                .toString() +
                                                            " more to avail this offer",
                                                      );
                                                    } else {
                                                      promocode =
                                                          bonuses[index].code;
                                                      promotext = bonuses[index]
                                                          .description;
                                                      bonusId =
                                                          bonuses[index].id;
                                                      setState(
                                                        () {
                                                          _promoController
                                                                  .text =
                                                              bonuses[index]
                                                                  .code;
                                                        },
                                                      );
                                                    }
                                                  } else {
                                                    CommonMethods.showSnackBar(
                                                      context,
                                                      bonuses[index].error,
                                                    );
                                                  }
                                                },
                                                child: Container(
                                                  height: 6.0.h,
                                                  width: 75.0.w,
                                                  child: Row(
                                                    children: [
                                                      //Promo Code
                                                      Expanded(
                                                        flex: 3,
                                                        child: Container(
                                                          decoration:
                                                              BoxDecoration(
                                                            color:
                                                                ColorConstants
                                                                    .grey,
                                                            borderRadius:
                                                                BorderRadius
                                                                    .only(
                                                              topLeft: Radius
                                                                  .circular(
                                                                2.0.w,
                                                              ),
                                                              bottomLeft: Radius
                                                                  .circular(
                                                                2.0.w,
                                                              ),
                                                            ),
                                                          ),
                                                          child: Center(
                                                            child: Text(
                                                              bonuses[index]
                                                                  .code,
                                                              style: TextStyle(
                                                                fontSize:
                                                                    10.5.sp,
                                                                color: Colors
                                                                    .black,
                                                                fontWeight:
                                                                    FontWeight
                                                                        .bold,
                                                              ),
                                                            ),
                                                          ),
                                                        ),
                                                      ),
                                                      //Promo Text
                                                      Expanded(
                                                        flex: 6,
                                                        child: Container(
                                                          padding: EdgeInsets
                                                              .symmetric(
                                                            horizontal: 5,
                                                          ),
                                                          height: 6.0.h,
                                                          decoration:
                                                              BoxDecoration(
                                                            color:
                                                                ColorConstants
                                                                    .grey,
                                                            borderRadius:
                                                                BorderRadius
                                                                    .only(
                                                              topRight: Radius
                                                                  .circular(
                                                                2.0.w,
                                                              ),
                                                              bottomRight:
                                                                  Radius
                                                                      .circular(
                                                                2.0.w,
                                                              ),
                                                            ),
                                                          ),
                                                          child: Center(
                                                            child: Text(
                                                              bonuses[index]
                                                                  .description,
                                                              style: TextStyle(
                                                                fontSize:
                                                                    9.0.sp,
                                                                color: Colors
                                                                    .black,
                                                                fontWeight:
                                                                    FontWeight
                                                                        .bold,
                                                              ),
                                                              textAlign:
                                                                  TextAlign
                                                                      .justify,
                                                            ),
                                                          ),
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                ),
                                              ),
                                              SizedBox(
                                                height: 0.8.h,
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                    );
                                  },
                                ),
                              ),
                      ),
                      SizedBox(
                        height: 3.0.h,
                      ),
                      //Check-Apply Coupon
                      Center(
                        child: GestureDetector(
                          onTap: () {
                            FocusScope.of(context).unfocus();
                            showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return ApplyPromocode(
                                  onApply: () {
                                    bool breaks = false;
                                    isPromoApplied = true;
                                    if (_promoController.text.isEmpty) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        "Add some promocode!",
                                      );
                                    } else if (_promoController.text.length >
                                        0) {
                                      for (var i = 0; i < bonuses.length; i++) {
                                        if (_promoController.text
                                                .toLowerCase() ==
                                            bonuses[i].code.toLowerCase()) {
                                          breaks = true;
                                          if (bonuses[i].applicable) {
                                            minAmount =
                                                bonuses[i].minAmount.toInt();
                                            if (_cashController.text.isEmpty) {
                                              CommonMethods.showSnackBar(
                                                context,
                                                "Add ₹" +
                                                    minAmount.toString() +
                                                    " to avail this offer",
                                              );
                                            } else if (int.parse(
                                                    _cashController.text) <
                                                minAmount) {
                                              int currentAmount = int.parse(
                                                _cashController.text,
                                              );
                                              CommonMethods.showSnackBar(
                                                context,
                                                "Add ₹" +
                                                    (minAmount - currentAmount)
                                                        .toString() +
                                                    " more to avail this offer",
                                              );
                                            } else {
                                              setState(
                                                () {
                                                  promocode = bonuses[i].code;
                                                  promotext =
                                                      bonuses[i].description;
                                                  bonusId = bonuses[i].id;
                                                  amount = false;
                                                },
                                              );
                                              Navigator.pop(context);
                                            }
                                          } else {
                                            CommonMethods.showSnackBar(
                                              context,
                                              bonuses[i].error,
                                            );
                                          }
                                          break;
                                        } else {
                                          setState(
                                            () {
                                              promocode =
                                                  StringConstants.emptyString;
                                              promotext =
                                                  StringConstants.emptyString;
                                              minAmount = minAmountServer;
                                              bonusId =
                                                  StringConstants.emptyString;
                                            },
                                          );
                                        }
                                      }
                                      if (!breaks) {
                                        CommonMethods.showSnackBar(
                                          context,
                                          "This Promocode does not Exists.",
                                        );
                                      }
                                    }
                                    FocusScope.of(context).unfocus();
                                  },
                                  onCancel: () {
                                    setState(
                                      () {
                                        _promoController.clear();
                                        promocode = StringConstants.emptyString;
                                        promotext = StringConstants.emptyString;
                                        minAmount = minAmountServer;
                                        bonusId = StringConstants.emptyString;
                                      },
                                    );
                                    Navigator.pop(context);
                                  },
                                  onClear: () {
                                    setState(
                                      () {
                                        _promoController.clear();
                                      },
                                    );
                                  },
                                  promocodeController: _promoController,
                                );
                              },
                            ).whenComplete(
                              () {
                                if (!isPromoApplied) {
                                  _promoController.clear();
                                }
                                FocusScope.of(context).unfocus();
                              },
                            );
                          },
                          child: Container(
                            padding: EdgeInsets.symmetric(
                              vertical: 0,
                              horizontal: 10,
                            ),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(
                                10.w,
                              ),
                              color: ColorConstants.white,
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceAround,
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Image.asset(
                                  'assets/images/coupon_icon.webp',
                                ),
                                Text(
                                  'Check/Apply Coupons',
                                  style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 12.0.sp,
                                    color: ColorConstants.black,
                                  ),
                                ),
                                Icon(
                                  Icons.arrow_forward_ios,
                                  size: 15.0.sp,
                                )
                              ],
                            ),
                          ),
                        ),
                      ),
                      SizedBox(
                        height: 4.0.h,
                      ),
                      //Pay Button
                      Center(
                        child: GestureDetector(
                          onTap: () async {
                            FocusScope.of(context).unfocus();
                            bool isKycVerified = await SharedPrefService
                                .getBoolValuesFromSharedPref(
                                    SharedPrefKeys.addVerified);

                            if (_cashController.text.isEmpty) {
                              CommonMethods.showSnackBar(
                                context,
                                "Enter some amount",
                              );
                            } else if (FlavorInfo.isPS && !isKycVerified) {
                              CommonMethods.showSnackBar(
                                context,
                                "Your KYC is not verified",
                              );
                              showDialog(
                                context: context,
                                builder: (BuildContext context) {
                                  return customKycDialogBox(
                                    context: context,
                                    userId: widget.userId,
                                  );
                                },
                              );
                            } else if (isAddCash) {
                              CommonMethods.showSnackBar(
                                context,
                                "Please wait...",
                              );
                            } else if (int.parse(_cashController.text) >=
                                    minAmount &&
                                int.parse(_cashController.text) <= maxAmount) {
                              setState(
                                () {
                                  isAddCash = true;
                                },
                              );
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext context) {
                                  return WillPopScope(
                                    onWillPop: () {
                                      return Future.delayed(
                                        Duration.zero,
                                      ).then(
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

                              //CLEVERTAP
                              CleverTapPlugin.recordEvent(
                                "Deposit Initiated",
                                {
                                  'amount_di': _cashController.text,
                                  'promo_code_di': bonusId
                                },
                              );
                              //FACEBOOK
                              const platform = MethodChannel(
                                'com.flutter.fbevents',
                              );
                              platform.invokeMethod(
                                "customEvents",
                                {
                                  "eventName": "DEPOSIT INITIATED",
                                },
                              );

                              await handlePaymentRazorpay(
                                _cashController.text,
                              );

                              Navigator.of(
                                context,
                                rootNavigator: true,
                              ).pop();
                              setState(
                                () {
                                  isAddCash = false;
                                },
                              );
                            } else {
                              CommonMethods.showSnackBar(
                                context,
                                "Enter Amount in range ₹$minAmount - ₹$maxAmount.",
                              );
                            }
                          },
                          child: Container(
                            padding: EdgeInsets.symmetric(
                              vertical: 10,
                            ),
                            width: 40.w,
                            alignment: Alignment.center,
                            decoration: BoxDecoration(
                              gradient: isAddCash
                                  ? null
                                  : ColorConstants.greenGradient,
                              color: isAddCash ? ColorConstants.grey : null,
                              boxShadow: isAddCash
                                  ? [
                                      BoxShadow(
                                        color: ColorConstants.black.withOpacity(
                                          0.5,
                                        ),
                                        spreadRadius: 5,
                                        blurRadius: 7,
                                        offset: Offset(
                                          3,
                                          6,
                                        ),
                                      )
                                    ]
                                  : null,
                              borderRadius: BorderRadius.circular(
                                2.0.w,
                              ),
                            ),
                            child: Text(
                              'PAY',
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                color: ColorConstants.white,
                                fontWeight: FontWeight.bold,
                                fontSize: 14.0.sp,
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
        ),
      ),
    );
  }

  //METHODS
  void razorPayEventListeners() {
    _razorpay.on(
      Razorpay.EVENT_PAYMENT_SUCCESS,
      _handlePaymentSuccess,
    );
    _razorpay.on(
      Razorpay.EVENT_PAYMENT_ERROR,
      _handlePaymentError,
    );
    _razorpay.on(
      Razorpay.EVENT_EXTERNAL_WALLET,
      _handleExternalWallet,
    );
  }

  Future<void> _handlePaymentSuccess(
    PaymentSuccessResponse response,
  ) async {
    CommonMethods.printLog(
      "Razorpay Event Listener: ",
      "SUCCESS",
    );
    LoggingModel.logging(
      "Transaction ID : " + transactionId,
      "_handlePaymentSuccess : " + response.orderId,
      DateTime.now().toString(),
      widget.userId,
    );

    verifyTransactionRazorpay(
      transactionId,
      response.orderId,
      response.paymentId,
      response.signature,
      "PAID",
      "Success",
      bonusId,
      widget.userId,
    );
  }

  Future<void> _handlePaymentError(
    PaymentFailureResponse response,
  ) async {
    CommonMethods.printLog(
      "Razorpay Event Listener ERROR: ",
      response.message,
    );
    LoggingModel.logging(
      "Transaction ID : " + transactionId,
      "_handlePaymentError : " + response.message,
      DateTime.now().toString(),
      widget.userId,
    );
    if (response.message.contains(ResponsesKeys.ERROR)) {
      Map<String, dynamic> responseBody = json.decode(response.message);
      if (responseBody[ResponsesKeys.ERROR]['reason'] == "payment_cancelled") {
        verifyTransactionRazorpay(
          transactionId,
          StringConstants.emptyString,
          StringConstants.emptyString,
          StringConstants.emptyString,
          ResponsesKeys.CANCELLED,
          responseBody[ResponsesKeys.ERROR]['description'],
          bonusId,
          widget.userId,
        );
      } else {
        verifyTransactionRazorpay(
          transactionId,
          StringConstants.emptyString,
          StringConstants.emptyString,
          StringConstants.emptyString,
          ResponsesKeys.FAILED,
          responseBody[ResponsesKeys.ERROR]['description'],
          bonusId,
          widget.userId,
        );
      }
    } else {
      verifyTransactionRazorpay(
        transactionId,
        StringConstants.emptyString,
        StringConstants.emptyString,
        StringConstants.emptyString,
        ResponsesKeys.CANCELLED,
        StringConstants.noInternetConnection +
            " or " +
            "user pressed back button",
        bonusId,
        widget.userId,
      );
    }
  }

  void _handleExternalWallet(
    ExternalWalletResponse response,
  ) {
    CommonMethods.printLog(
      "Razorpay Event Listener: ",
      "EXTERNAL WALLET",
    );
    CommonMethods.printLog(
      "Razorpay Event Listener: ",
      response.walletName,
    );
  }

  Future<void> asyncMethod(
    String userId,
  ) async {
    await handlePromocode(
      userId,
    );
  }

  Future<void> handlePromocode(
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Promocode Handle----------",
    );
    var repoObj = FetchPromoCodesRepo();
    PromoCodesDM promoCodesDM = await repoObj.fetchPromoCodes(
      userId: widget.userId,
    );

    if (promoCodesDM != null) {
      switch (promoCodesDM.result) {
        case ResponseStatus.success:
          setState(
            () {
              bonuses = promoCodesDM.bonuses;
              minAmountServer = promoCodesDM.minimumDepositAmount.toInt();
              _cashController.text =
                  '${promoCodesDM.defaultDepositAmount.toInt()}';
              minAmount = minAmountServer;
              for (int i = 0; i < bonuses.length; i++) {
                if (promoCodesDM.bonuses[i].isVisibleInUI == 'true' &&
                    promoCodesDM.bonuses[i].applicable) {
                  itemCount += 1;
                }
              }
              isPromoFetched = true;
              for (int i = 0; i < bonuses.length; i++) {
                if (promoCodesDM.bonuses[i].code == widget.bonusC) {
                  if (promoCodesDM.bonuses[i].applicable) {
                    _promoController.text = widget.bonusC;
                    promocode = widget.bonusC;
                    promotext = promoCodesDM.bonuses[i].description;
                    bonusId = promoCodesDM.bonuses[i].id;
                    minAmount = promoCodesDM.bonuses[i].minAmount.toInt();
                    _cashController.text =
                        promoCodesDM.bonuses[i].minAmount.toInt().toString();
                  } else {
                    CommonMethods.showSnackBar(
                      context,
                      promoCodesDM.bonuses[i].error,
                    );
                  }
                  break;
                }
              }
            },
          );
          break;

        case ResponseStatus.dbError:
          CommonMethods.showSnackBar(
            context,
            "Server Error. Unable to fetch any BONUS",
          );
          setState(
            () {
              isPromoFetched = true;
            },
          );
          break;

        case ResponseStatus.supportContactNotAvailable:
          break;

        case ResponseStatus.tokenExpired:
        case ResponseStatus.tokenParsingFailed:
          bool accessTokenGenerated =
              await GenerateAccessToken.regenerateAccessToken(
            userId,
          );
          if (accessTokenGenerated) {
            await handlePromocode(
              userId,
            );
          }
          break;

        case ResponseStatus.dbOperationFailed:
          CommonMethods.showSnackBar(
            context,
            "Server Error. Unable to fetch any BONUS",
          );
          setState(
            () {
              isPromoFetched = true;
            },
          );
          break;

        case ResponseStatus.noBonusExist:
          setState(
            () {
              isPromoFetched = true;
            },
          );
          break;

        default:
          CommonMethods.showSnackBar(
            context,
            "Server Error.Unable to fetch any BONUS",
          );
      }
      setState(
        () {
          isPromoFetched = true;
        },
      );

      gateway = promoCodesDM.gateway ?? "razorpay";
    } else {
      CommonMethods.showSnackBar(context, 'Something Went Wrong');
    }
  }

  Future<void> handlePaymentRazorpay(
    String cash,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Payment Handle----------",
    );
    Map<String, Object> result =
        await PaymentService.initiateTransactionRazorpay(
      cash,
      bonusId,
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
        "Unable to initiate transaction",
      );
    } else {
      Map<String, Object> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Unable to initiate transaction",
        );
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          widget.userId,
        );
        if (accessTokenGenerated) {
          await handlePaymentRazorpay(cash);
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        transactionId = responseMap['transactionId'];
        LoggingModel.logging(
          "Transaction ID  : $transactionId",
          "Order ID  : $responseMap['orderId']" + "Response Data: $responseMap",
          DateTime.now().toString(),
          widget.userId,
        );
        await startTransactionRazorpay(
          cash,
          responseMap['orderId'],
        );
      } else if (responseMap['result'] == ResponsesKeys.FAILED) {
        CommonMethods.showSnackBar(
          context,
          "Unable to initiate transaction",
        );
      } else if (responseMap['result'] == ResponsesKeys.RESTRICTED_ACTIVITY) {
        CommonMethods.showSnackBar(
          context,
          "This functionality has been blocked for your account based on your location/KYC/account history.",
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "Unable to initiate transaction",
        );
      }
    }
  }

  Future<void> startTransactionRazorpay(
    String cash,
    String orderId,
  ) async {
    var options = {
      "key": FlavorConfig.instance.variables["razorpay_key"],
      "amount": cash,
      "currency": "INR",
      "name": "Dangal Games",
      "order_id": orderId,
      "retry": {"enabled": false},
      "prefill": {
        "name": await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.firstName,
            ) +
            await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.middleName,
            ) +
            await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.lastName,
            ),
        "contact": await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.mobileNumber,
        )
      }
    };

    LoggingModel.logging(
      "RazorPay properties",
      options.toString(),
      DateTime.now().toString(),
      widget.userId,
    );

    try {
      _razorpay.open(options);
    } catch (ex) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "Error: " + ex.toString(),
      );
      CommonMethods.showSnackBar(
        context,
        "Sorry, unable to complete your transaction. Please try again.",
      );
    }
  }

  Future<void> verifyTransactionCashfree(
      String orderAmount,
      String orderId,
      String referenceId,
      String txStatus,
      String txTime,
      String paymentMode,
      String txMsg,
      String signature,
      String bonusId,
      String userId) async {
    Map<String, dynamic> result =
        await PaymentService.verifyTransactioncashfree(
            orderAmount,
            orderId,
            referenceId,
            txStatus,
            txTime,
            paymentMode,
            txMsg,
            signature,
            bonusId,
            userId);
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.showSnackBar(
        context,
        "Cannot add cash, please try again after sometime!!!",
      );
    } else {
      Map<String, dynamic> responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Please try after some time.",
        );
      } else if (responseMap.containsKey('result') &&
              responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(userId);
        if (accessTokenGenerated) {
          await verifyTransactionCashfree(
            orderAmount,
            orderId,
            referenceId,
            txStatus,
            txTime,
            paymentMode,
            txMsg,
            signature,
            bonusId,
            userId,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        if (responseMap['tx_status'] == 'FAILED') {
          Toast.show(
            "There was some error during payment! Please try again",
            duration: 5,
            gravity: 0,
            backgroundColor: ColorConstants.red,
            textStyle: TextStyle(
              color: ColorConstants.white,
            ),
          );
        } else if (responseMap['tx_status'] == 'SUCCESS') {
          _cashController.clear();
          _promoController.clear();
          await SharedPrefService.addStringToSharedPref(
            SharedPrefKeys.cashAmount,
            responseMap['balance'].toString(),
          );
          if (widget.webviewInstance != null) {
            CommonMethods.printLog(
              StringConstants.emptyString,
              "----------Webview Instance Show----------",
            );
            widget.webviewInstance.show();
          } else {
            widget.changeIndex(0);
          }

          //FACEBOOK

          const platform = MethodChannel(
            'com.flutter.fbevents',
          );
          platform.invokeMethod(
            "customEvents",
            {"eventName": "DEPOSIT DONE"},
          );
        }
      } else if (responseMap['result'] ==
          ResponsesKeys.STATUS_ALREADY_UPDATED) {
        _cashController.clear();
        _promoController.clear();
        if (widget.webviewInstance != null) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "----------Webview Instance Show----------",
          );
          widget.webviewInstance.show();
        } else {
          widget.changeIndex(0);
        }
      } else if (responseMap['result'] == ResponsesKeys.TXN_FAILURE) {
        Toast.show(
          "There was some error during payment! Please try again",
          duration: 5,
          gravity: 0,
          backgroundColor: ColorConstants.red,
          textStyle: TextStyle(
            color: ColorConstants.white,
          ),
        );
      } else if (responseMap['result'] == ResponsesKeys.FAILED) {
        Toast.show(
          "There was some error during payment! Please try again",
          duration: 5,
          gravity: 0,
          backgroundColor: ColorConstants.red,
          textStyle: TextStyle(
            color: ColorConstants.white,
          ),
        );
      } else if (responseMap['result'] == ResponsesKeys.INVALID_SIGNATURE) {
        CommonMethods.showSnackBar(
          context,
          "Technical issue. Please try after some time.",
        );
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        CommonMethods.showSnackBar(
          context,
          "Technical issue. Please try after some time.",
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_UPDATE_FAILED) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Please try after some time.",
        );
      } else if (responseMap['result'] == ResponsesKeys.CANCELLED) {
      } else {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Please try after some time.",
        );
      }
    }
  }

  Future<void> verifyTransactionRazorpay(
      String transactionId,
      String orderId,
      String paymentId,
      String signature,
      String status,
      String message,
      String bonusId,
      String userId) async {
    Map<String, dynamic> result =
        await PaymentService.verifyTransactionRazorpay(
      transactionId,
      orderId,
      paymentId,
      signature,
      status,
      message,
      bonusId,
      userId,
    );

    if (result.containsKey("noInternet")) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey(ResponsesKeys.ERROR)) {
      CommonMethods.showSnackBar(
        context,
        "Cannot add cash, please try again after sometime!!!",
      );
    } else {
      Map<String, dynamic> responseMap = result[ResponsesKeys.DATA];
      if (responseMap.containsKey(ResponsesKeys.ERROR)) {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Please try after some time.",
        );
      } else if (responseMap.containsKey(ResponsesKeys.RESULT) &&
              responseMap[ResponsesKeys.RESULT] ==
                  ResponsesKeys.TOKEN_EXPIRED ||
          responseMap[ResponsesKeys.RESULT] ==
              ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await verifyTransactionRazorpay(
            transactionId,
            orderId,
            paymentId,
            signature,
            status,
            message,
            bonusId,
            userId,
          );
        }
      } else if (responseMap[ResponsesKeys.RESULT] == ResponsesKeys.SUCCESS) {
        if (responseMap["status"] == ResponsesKeys.FAILED) {
          EasyLoading.showToast(
            "There was some error during payment! Please try again",
          );
        } else if (responseMap["status"] == "PAID" ||
            responseMap["status"] == ResponsesKeys.CAPTURED) {
          _cashController.clear();
          _promoController.clear();
          if (widget.webviewInstance != null) {
            widget.webviewInstance.show();
          } else {
            widget.changeIndex(0);
          }
          //FACEBOOK

          const platform = MethodChannel('com.flutter.fbevents');
          platform.invokeMethod(
            "customEvents",
            {"eventName": "DEPOSIT DONE"},
          );
        }
      } else if (responseMap[ResponsesKeys.RESULT] == ResponsesKeys.FAILED) {
        Toast.show(
          "There was some error during payment! Please try again.",
          duration: 5,
          gravity: 0,
          backgroundColor: ColorConstants.red,
          textStyle: TextStyle(
            color: ColorConstants.white,
          ),
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "Technical Issue. Please try after some time.",
        );
      }
    }
  }

  Widget moneyContainer(
    String cash,
    int index,
  ) {
    return MoneyContainer(
      cash: cash,
      isSelected: moneyContainerSelected[index],
      onTap: () {
        setState(
          () {
            _cashController.text = cash;
            isAddCash = false;
            moneyContainerSelected = [
              false,
              false,
              false,
              false,
            ];
            moneyContainerSelected[index] = true;
          },
        );
      },
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
                "before doing deposit.",
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
                      Navigator.pushReplacement(
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
}
