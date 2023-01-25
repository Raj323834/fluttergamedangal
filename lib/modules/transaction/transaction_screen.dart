import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:intl/intl.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/generate_access_token.dart';
import '../../common_widgets/all_button.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/from_or_to_widget.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../transaction_details/transaction_details_screen.dart';
import 'models/list_item_model.dart';
import 'models/transactions_dm.dart';
import 'repos/fetch_transactions_repo.dart';

class TransactionScreen extends StatefulWidget {
  final String userId;

  const TransactionScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  _TransactionScreenState createState() => _TransactionScreenState();
}

class _TransactionScreenState extends State<TransactionScreen> {
  List<DropdownMenuItem<ListItem>> _dropdownMenuItems;
  ListItem _selectedItem;
  List<WalletInfo> transactions = [], visibleUI = [];
  bool isListVisible = false;

  List<ListItem> _dropdownItems = [
    ListItem(
      1,
      "All ",
      StringConstants.emptyString,
    ),
    ListItem(
      2,
      "Withdrawable",
      "Withdrawable",
    ),
    ListItem(
      3,
      "Deposit",
      "Deposit",
    ),
    ListItem(
      4,
      "Bonus",
      "Bonus",
    ),
    ListItem(
      5,
      "Game Chips",
      "Game Chips",
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
    selectedDatefrom = formatDateInit(
      DateTime.now(),
    );
    selectedDateTo = formatDateInit(
      DateTime.now(),
    );
    _selectedItem = _dropdownMenuItems[0].value;
    CommonMethods.printLog(
      StringConstants.emptyString,
      "_selectedItem  :  " + _selectedItem.name.toString(),
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: TRANSACTION_HISTORY_ROUTE,
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
              //Lable
              Expanded(
                child: Column(
                  children: [
                    LabelHeader(
                      label: "transaction_screen.transactions".tr(),
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
                    //All
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
                      buttonText: "transaction_screen.apply".tr(),
                      height: 40,
                      width: 50.w,
                      onTap: () async {
                        final difference =
                            selectedDateTo.difference(selectedDatefrom).inDays;
                        FocusScope.of(context).requestFocus(
                          FocusNode(),
                        );
                        if (selectedDatefrom.isAfter(selectedDateTo)) {
                          CommonMethods.showSnackBar(
                            context,
                            " From date should be before To date",
                          );
                        } else if (difference > 7) {
                          CommonMethods.showSnackBar(
                            context,
                            "Interval should be 7 days or less",
                          );
                        } else {
                          getTansactionData(
                            widget.userId,
                            selectedDatefrom.millisecondsSinceEpoch,
                            selectedDateTo.millisecondsSinceEpoch,
                            _selectedItem.apiName,
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
              //List Of Transactions
              Expanded(
                child: Visibility(
                  visible: isListVisible,
                  child: Container(
                    padding: EdgeInsets.only(
                      top: 12,
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
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
                                  StringConstants.date,
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
                              flex: 2,
                              child: Center(
                                child: Text(
                                  StringConstants.walletName,
                                  style: TextStyle(
                                    fontSize: 9.5.sp,
                                    color: ColorConstants.white,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 2,
                              child: Center(
                                child: Text(
                                  StringConstants.amount,
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
                              itemCount: transactions.length,
                              itemBuilder: (BuildContext context, int index) {
                                return Align(
                                  child: Column(
                                    children: [
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.of(context).push(
                                            CupertinoPageRoute(
                                              builder: (BuildContext context) =>
                                                  TransactionDetailsScreen(
                                                transactiondesc:
                                                    transactions[index],
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
                                                    formatDate(
                                                      transactions[index]
                                                          .transactionTime,
                                                    ),
                                                    style: TextStyle(
                                                      fontSize: 8.0.sp,
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
                                              flex: 2,
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    transactions[index]
                                                        .walletName,
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
                                              flex: 2,
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    transactions[index].amount,
                                                    style: TextStyle(
                                                      decoration: TextDecoration
                                                          .underline,
                                                      fontSize: 9.0.sp,
                                                      color:
                                                          ColorConstants.blue,
                                                      fontWeight:
                                                          FontWeight.bold,
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
                                      SizedBox(
                                        height: 0.7.h,
                                      ),
                                    ],
                                  ),
                                );
                              },
                            ),
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
        lastDate: DateTime.now());

    if (picked != null && picked != selectedDatefrom)
      setState(
        () {
          selectedDatefrom = picked;
          // _isFromDateSelected=true;
        },
      );
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
      lastDate: DateTime.now(),
    );
    if (picked != null && picked != selectedDateTo)
      setState(
        () {
          selectedDateTo = picked;
          // _isToDateSelected=true;
        },
      );
  }

  List<DropdownMenuItem<ListItem>> buildDropDownMenuItems(
    List listItems,
  ) {
    List<DropdownMenuItem<ListItem>> items = [];
    for (ListItem listItem in listItems) {
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

  Future getTansactionData(
    String userId,
    dynamic fromTime,
    dynamic endTime,
    String walletName,
  ) async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
      FocusScope.of(context).unfocus();
    } else {
      var repoObj = FetchTransactionsRepo();
      TransactionsDM transactionsDM = await repoObj.fetchTransactions(
        userId: userId,
        fromTime: fromTime,
        endTime: endTime,
        walletName: walletName,
      );
      if (transactionsDM != null) {
        switch (transactionsDM.result) {
          case ResponseStatus.success:
            transactions = transactionsDM.walletInfo;
            setState(
              () {
                if (transactions.length == 0) {
                  isListVisible = false;
                  CommonMethods.showSnackBar(
                    context,
                    "No transactions between above dates",
                  );
                } else {
                  isListVisible = true;
                }
              },
            );
            break;

          case ResponseStatus.invalidDateRange:
            CommonMethods.showSnackBar(
              context,
              "Invalid Date Range.",
            );
            break;

          case ResponseStatus.userNotExist:
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
              await getTansactionData(
                userId,
                fromTime,
                endTime,
                walletName,
              );
            }
            break;
          default:
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          StringConstants.unableToFetchActiveLeaderboards,
        );
      }
    }
  }

  String formatDate(dynamic time) {
    String formattedDateTime;

    // final df =  DateFormat('dd-MM-yyyy hh:mm a');
    final df = DateFormat(
      'dd-MM-yyyy \n hh:mm a',
    );
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

  DateTime formatDateInit(
    DateTime time2,
  ) {
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
}
