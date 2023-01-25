import 'package:connectivity/connectivity.dart';
import 'repos/fetch_withdrawals_repo.dart';
import '../../network_new/constants/response_status.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:intl/intl.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/withdrawal_service.dart';
import '../../common_widgets/all_button.dart';
import '../../common_widgets/base_error_dialog.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/from_or_to_widget.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/responses_keys.dart';
import '../withdrawal_details/withdrawal_details_screen.dart';
import 'models/withdrawal_list_item_model.dart';
import 'models/withdrawals_dm.dart';

class WithdrawalListScreen extends StatefulWidget {
  final String userId;

  WithdrawalListScreen({
    @required this.userId,
  });

  @override
  WithdrawalListScreenScreenState createState() =>
      WithdrawalListScreenScreenState();
}

class WithdrawalListScreenScreenState extends State<WithdrawalListScreen> {
  List<DropdownMenuItem<WithdrawalListItem>> _dropdownMenuItems;
  WithdrawalListItem _selectedItem;
  List<WithdrawalDetails> withdrawals = [];
  int cancelWithdrawalId = -1;
  bool isListVisible = false;
  bool isCancelClickable = true;

  List<WithdrawalListItem> _dropdownItems = [
    WithdrawalListItem(
      1,
      "All ",
      StringConstants.emptyString,
    ),
    WithdrawalListItem(
      2,
      "Pending",
      "Pending",
    ),
    WithdrawalListItem(
      3,
      "Initiated",
      "Initiated",
    ),
    WithdrawalListItem(
      4,
      "Fulfilled",
      "Fulfilled",
    ),
    WithdrawalListItem(
      5,
      "Failed",
      "Failed",
    ),
    WithdrawalListItem(
      7,
      "In Progress",
      "In Progress",
    ),
  ];

  DateTime selectedDatefrom;
  DateTime selectedDateTo;

  @override
  void initState() {
    super.initState();
    initializeDateFormatting();
    _dropdownMenuItems = buildDropDownMenuItems(
      _dropdownItems,
    );
    _selectedItem = _dropdownMenuItems[0].value;
    CommonMethods.printLog(
      StringConstants.emptyString,
      "_selectedItem  :  " + _selectedItem.name.toString(),
    );
    selectedDatefrom = formatDateInit(
      DateTime.now(),
    );
    selectedDateTo = formatDateInit(
      DateTime.now(),
    );
    getWithdrawalListData(
      fromTime: selectedDatefrom.millisecondsSinceEpoch,
      endTime: selectedDateTo.millisecondsSinceEpoch,
      status: _dropdownItems[0].apiName,
      userId: widget.userId,
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: WITHDRAWAL_HISTORY_ROUTE,
    );
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
            from: "trans",
            userId: widget.userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        body: Padding(
          padding: EdgeInsets.symmetric(
            vertical: 2.0.h,
            horizontal: 14,
          ),
          child: Column(
            children: [
              Expanded(
                child: Column(
                  children: [
                    LabelHeader(
                      label: "withdrawal_history_screen.withdrawals".tr(),
                    ),
                    SizedBox(
                      height: 25,
                    ),
                    //From Widget
                    FromOrTo(
                      onTap: () {
                        _selectDateFrom(context);
                      },
                      isFrom: true,
                      selectedDate: selectedDatefrom,
                    ),
                    SizedBox(
                      height: 25,
                    ),
                    //To
                    FromOrTo(
                      onTap: () {
                        _selectDateTo(context);
                      },
                      isFrom: false,
                      selectedDate: selectedDateTo,
                    ),
                    SizedBox(
                      height: 25,
                    ),
                    //All Button
                    Row(
                      children: [
                        Expanded(
                          flex: 2,
                          child: SizedBox(),
                        ),
                        Expanded(
                          flex: 3,
                          child: AllButton(
                            onTap: () async {
                              await showModalBottomSheet(
                                context: context,
                                builder: (context) {
                                  return Container(
                                    child: Column(
                                      mainAxisSize: MainAxisSize.min,
                                      children: [
                                        Row(
                                          mainAxisSize: MainAxisSize.min,
                                          children: [
                                            Expanded(
                                              child: Container(
                                                height: 30.0.h,
                                                child: _buildItemPicker(
                                                  StringConstants.emptyString,
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ],
                                    ),
                                  );
                                },
                              ).whenComplete(
                                () {},
                              );
                            },
                            selectedItem: _selectedItem.name,
                          ),
                        ),
                      ],
                    ),
                    SizedBox(
                      height: 25,
                    ),
                    //Apply Button
                    CustomButton(
                      buttonText: "withdrawal_history_screen.apply".tr(),
                      height: 40,
                      width: 50.w,
                      onTap: () async {
                        final difference =
                            selectedDateTo.difference(selectedDatefrom).inDays;
                        FocusScope.of(context).requestFocus(FocusNode());
                        if (selectedDatefrom.isAfter(selectedDateTo)) {
                          CommonMethods.showSnackBar(
                              context, " From date should be before To date");
                        } else if (difference > 7) {
                          CommonMethods.showSnackBar(
                              context, "Interval should be 7 days or less");
                        } else {
                          // final userId = await SharedPrefService
                          //     .getStringValuesFromSharedPref(USER_ID);
                          getWithdrawalListData(
                            fromTime: selectedDatefrom.millisecondsSinceEpoch,
                            endTime: selectedDateTo.millisecondsSinceEpoch,
                            status: _selectedItem.apiName,
                            userId: widget.userId,
                          );
                        }
                      },
                    ),
                    SizedBox(
                      height: 25,
                    ),
                  ],
                ),
              ),
              //List Of Withdrawals
              Expanded(
                child: Visibility(
                  visible: isListVisible,
                  child: Container(
                    height: 60.0.h,
                    child: Column(
                      children: [
                        Text(
                          StringConstants.note,
                          style: TextStyle(
                            color: ColorConstants.greyNote,
                            fontWeight: FontWeight.w400,
                          ),
                        ),
                        SizedBox(
                          height: 28,
                        ),
                        Row(
                          children: [
                            Expanded(
                              flex: 2,
                              child: Center(
                                child: Text(
                                  'WID',
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 4,
                              child: Center(
                                child: Text(
                                  'Date',
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 3,
                              child: Center(
                                child: Text(
                                  "Amount",
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 3,
                              child: Center(
                                child: Text(
                                  "Status",
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                            SizedBox(
                              width: 1.2.w,
                            ),
                            Expanded(
                              flex: 3,
                              child: Center(
                                child: Text(
                                  "Action",
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                        SizedBox(
                          height: 1.5.h,
                        ),
                        Divider(
                          color: ColorConstants.grey,
                        ),
                        Expanded(
                          child: CupertinoScrollbar(
                            child: ListView.builder(
                              itemCount: withdrawals.length,
                              itemBuilder: (BuildContext context, int index) {
                                return Align(
                                  child: Column(
                                    children: [
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.of(context).push(
                                            CupertinoPageRoute(
                                              builder: (BuildContext context) =>
                                                  WithdrawalDetailsScreen(
                                                withdrawaldesc:
                                                    withdrawals[index],
                                                userId: widget.userId,
                                              ),
                                            ),
                                          );
                                        },
                                        child: Row(
                                          children: [
                                            Expanded(
                                              flex: 2,
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    withdrawals[index].id,
                                                    style: TextStyle(
                                                      fontSize: 11,
                                                      color:
                                                          ColorConstants.white,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                                    textAlign: TextAlign.center,
                                                  ),
                                                ),
                                              ),
                                            ),
                                            Expanded(
                                              flex: 4,
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    formatDate(withdrawals[
                                                                index]
                                                         .withdrawalTime
                                                        .toString()),
                                                    style: TextStyle(
                                                      fontSize: 9.0.sp,
                                                      color:
                                                          ColorConstants.white,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                                    textAlign: TextAlign.center,
                                                  ),
                                                ),
                                              ),
                                            ),
                                            Expanded(
                                              flex: 3,
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    withdrawals[index]
                                                     .amount,
                                                    style: TextStyle(
                                                      fontSize: 9.0.sp,
                                                      color:
                                                          ColorConstants.white,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ),
                                            Expanded(
                                              flex: 3,
                                              child: Container(
                                                width: 90,
                                                child: GestureDetector(
                                                  onTap: () {
                                                    if (withdrawals[index]
                                                    .status ==
                                                        "Initiated") {
                                                      Navigator.of(context)
                                                          .push(
                                                        CupertinoPageRoute(
                                                          builder: (BuildContext
                                                                  context) =>
                                                              WithdrawalDetailsScreen(
                                                            withdrawaldesc:
                                                                withdrawals[
                                                                    index],
                                                            userId:
                                                                widget.userId,
                                                          ),
                                                        ),
                                                      );
                                                    }
                                                  },
                                                  child: Center(
                                                    child: Text(
                                                      withdrawals[index]
                                                   .status,
                                                      style: TextStyle(
                                                        decoration: (withdrawals[
                                                                        index].status ==
                                                                "Initiated")
                                                            ? TextDecoration
                                                                .underline
                                                            : TextDecoration
                                                                .none,
                                                        fontSize: 9.0.sp,
                                                        color: (withdrawals[
                                                                        index].status ==
                                                                "Initiated")
                                                            ? ColorConstants
                                                                .blue
                                                            : ColorConstants
                                                                .white,
                                                        fontWeight:
                                                            FontWeight.bold,
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ),
                                            Expanded(
                                              flex: 3,
                                              child: Container(
                                                width: 90,
                                                child: GestureDetector(
                                                  onTap: () async {
                                                    // if(isCancelClickable){
                                                    //   isCancelClickable=false;
                                                    if (withdrawals[index]
                                                         .isCancellable==
                                                        true) {
                                                      cancelWithdrawalId =
                                                          int.parse(
                                                        withdrawals[index]
                                                           .id,
                                                      );
                                                      showDialog(
                                                        context: context,
                                                        barrierDismissible:
                                                            false,
                                                        builder: (BuildContext
                                                            context) {
                                                          return Center(
                                                            child:
                                                                CircularProgressIndicator(
                                                              backgroundColor:
                                                                  ColorConstants
                                                                      .blue,
                                                            ),
                                                          );
                                                        },
                                                      );
                                                      cancelWithdrawal(
                                                        widget.userId,
                                                        cancelWithdrawalId,
                                                      );
                                                    } else {
                                                      Navigator.of(context)
                                                          .push(
                                                        CupertinoPageRoute(
                                                          builder: (BuildContext
                                                                  context) =>
                                                              WithdrawalDetailsScreen(
                                                            withdrawaldesc:
                                                                withdrawals[
                                                                    index],
                                                            userId:
                                                                widget.userId,
                                                          ),
                                                        ),
                                                      );
                                                    }
                                                  },
                                                  child: Center(
                                                    child: Center(
                                                      child: Text(
                                                        withdrawals[index].isCancellable
                                                            ? "Cancel"
                                                            : "Details",
                                                        style: TextStyle(
                                                          decoration:
                                                              TextDecoration
                                                                  .underline,
                                                          fontSize: 9.0.sp,
                                                          color: withdrawals[
                                                                      index].isCancellable
                                                              ? ColorConstants
                                                                  .red
                                                              : ColorConstants
                                                                  .blue,
                                                          fontWeight:
                                                              FontWeight.bold,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      Divider(
                                        color: ColorConstants.grey,
                                      ),
                                    ],
                                  ),
                                );
                              },
                            ),
                          ),
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
  DateTime formatDateInit(DateTime time2) {
    String formattedDateTime;
    final df = DateFormat(
      'yyyy-MM-dd',
    );
    formattedDateTime = df.format(time2).toString();
    DateTime selectedDate = DateTime.parse(
      formattedDateTime,
    );
    return selectedDate;
  }

  Widget _buildItemPicker(String type) {
    CommonMethods.printLog(
      StringConstants.emptyString,
      type.toString(),
    );
    return CupertinoPicker(
      scrollController: FixedExtentScrollController(
        initialItem: _dropdownItems.indexOf(
          _selectedItem,
        ),
      ),
      itemExtent: 40,
      backgroundColor: ColorConstants.kBackgroundColor,
      children: List<Widget>.generate(
        _dropdownItems.length,
        (index) {
          return Center(
            child: Text(
              _dropdownItems[index].name,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: ColorConstants.textYellow,
              ),
            ),
          );
        },
      ),
      onSelectedItemChanged: (index) {
        setState(
          () {
            _selectedItem = _dropdownItems[index];
          },
        );
      },
    );
  }

  Future<void> _selectDateFrom(BuildContext context) async {
    final DateTime picked = await showDatePicker(
      context: context,
      initialDate: selectedDatefrom,
      firstDate: DateTime.now().subtract(
        Duration(
          days: 90,
        ),
      ),
      lastDate: DateTime.now(),
    );

    if (picked != null && picked != selectedDatefrom)
      setState(
        () {
          selectedDatefrom = picked;
          // _isFromDateSelected=true;
        },
      );
  }

  String formatText(String text) {
    if (text == "promo_bonus") {
      return "Promo Bonus";
    } else if (text == "locked_bonus") {
      return "Locked Bonus";
    } else if (text == "withdrawal") {
      return "Withdrawal";
    } else if (text == "deposit") {
      return "Deposit";
    }
    return StringConstants.emptyString;
  }

  Future<void> _selectDateTo(BuildContext context) async {
    final DateTime picked = await showDatePicker(
        context: context,
        initialDate: selectedDateTo,
        firstDate: DateTime.now().subtract(
          Duration(
            days: 90,
          ),
        ),
        lastDate: DateTime.now());
    if (picked != null && picked != selectedDateTo)
      setState(
        () {
          selectedDateTo = picked;
          // _isToDateSelected=true;
        },
      );
  }

  List<DropdownMenuItem<WithdrawalListItem>> buildDropDownMenuItems(
    List withdrawalListItems,
  ) {
    List<DropdownMenuItem<WithdrawalListItem>> items = [];
    for (WithdrawalListItem listItem in withdrawalListItems) {
      items.add(
        DropdownMenuItem(
          child: Text(
            listItem.name,
          ),
          value: listItem,
        ),
      );
    }
    return items;
  }

  Future getWithdrawalListData({
    @required dynamic fromTime,
    @required int endTime,
    @required String status,
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
      var repoObj = FetchWithdrawalsRepo();
      WithdrawalsDM withdrawalsDM = await repoObj.fetchWithdrawals(
        fromTime: fromTime,
        endTime: endTime,
        status: status,
        userId: userId,
      );
      if (withdrawalsDM != null) {
        switch (withdrawalsDM.result) {
          case ResponseStatus.success:
            withdrawals = withdrawalsDM.details;
            setState(
              () {
                if (withdrawals.length == 0) {
                  isListVisible = false;
                  CommonMethods.showSnackBar(
                    context,
                    StringConstants.noWithdrawlExist,
                  );
                } else {
                  isListVisible = true;
                }
              },
            );
            break;

          case ResponseStatus.invalidDateRange:
            setState(
              () {
                isListVisible = false;
              },
            );
            CommonMethods.showSnackBar(
              context,
              StringConstants.invalidDateRange,
            );
            break;

          case ResponseStatus.userNotExist:
            setState(
              () {
                isListVisible = false;
              },
            );
            CommonMethods.showSnackBar(
              context,
              "User does not exists.",
            );
            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await getWithdrawalListData(
                fromTime: fromTime,
                endTime: endTime,
                status: status,
                userId: userId,
              );
            }
            break;

          case ResponseStatus.dbError:
            setState(
              () {
                isListVisible = false;
              },
            );
            CommonMethods.showSnackBar(
              context,
              "Internal server error. Please try again.",
            );
            break;

          case ResponseStatus.noWithdrawalsExist:
            setState(
              () {
                isListVisible = false;
              },
            );
            CommonMethods.showSnackBar(
              context,
              StringConstants.noWithdrawlExist,
            );
            break;

          default:
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          StringConstants.somethingWentWrong,
        );
      }
    }
  }

  Future cancelWithdrawal(String userId, int withdrawalId) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      '----------cancelling Withdrawal----------',
    );

    Map<String, Object> result = await WithdrawalService.cancelWithdrawal(
      userId,
      withdrawalId,
    );
    isCancelClickable = true;
    if (result.containsKey('noInternet')) {
      Navigator.pop(context);
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      Navigator.pop(context);
      showDialog(
        context: context,
        builder: (context) {
          return BaseErrorDialog(
            title: StringConstants.oops,
            error: StringConstants.somethingWentWrongTryAgain,
          );
        },
      );
      return;
    } else {
      Navigator.pop(context);
      Map responseMap = result['data'];
      if (responseMap.containsKey('error')) {
        showDialog(
          context: context,
          builder: (context) {
            return BaseErrorDialog(
              title: StringConstants.oops,
              error: StringConstants.somethingWentWrongTryAgain,
            );
          },
        );
        return;
      } else if (responseMap.containsKey('result') &&
              (responseMap['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (responseMap['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await cancelWithdrawal(
            userId,
            withdrawalId,
          );
        }
      } else if (responseMap['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.showSnackBar(
          context,
          "Withdrawal has been cancelled successfully",
        );
        getWithdrawalListData(
          fromTime: selectedDatefrom.millisecondsSinceEpoch,
          endTime: selectedDateTo.millisecondsSinceEpoch,
          status: _selectedItem.apiName,
          userId: userId,
        );
      } else if (responseMap['result'] == ResponsesKeys.DB_ERROR) {
        CommonMethods.showSnackBar(
          context,
          "Database Error",
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.CANCEL_WITHDRAWAL_NOT_ALLOWED) {
        CommonMethods.showSnackBar(
          context,
          "Cancellation of this withdrawal isn’t allowed",
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.CANCEL_WITHDRAWAL_FAILED) {
        CommonMethods.showSnackBar(
          context,
          "Cancellation of withdrawal failed",
        );
      } else if (responseMap['result'] ==
          ResponsesKeys.CREDIT_CURRENCY_FAILED) {
        CommonMethods.showSnackBar(
          context,
          "Refund of cancelled withdrawal amount failed",
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DISABLED) {
        CommonMethods.showSnackBar(
          context,
          "Wallet is disabled",
        );
      } else if (responseMap['result'] == ResponsesKeys.WALLET_DOES_NOT_EXIST) {
        CommonMethods.showSnackBar(
          context,
          "Wallet does not exist",
        );
      }
    }
  }

  String formatDate(dynamic time) {
    String formattedDateTime;

    final df = DateFormat('dd-MM-yyyy \n hh:mm a');
    int time1 = int.parse(time);
    formattedDateTime = df
        .format(
          DateTime.fromMillisecondsSinceEpoch(
            time1,
          ),
        )
        .toString();

    CommonMethods.printLog(
      StringConstants.emptyString,
      "******************************************************",
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "time   :   " + time + "    formatted_time  :  " + formattedDateTime,
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "******************************************************",
    );
    return formattedDateTime;
  }
}
