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
import '../deposit_details/deposit_details_screen.dart';
import 'models/deposit_list_item_model.dart';
import 'models/deposits_dm.dart';
import 'repos/fetch_deposits_repo.dart';

class DepositListScreen extends StatefulWidget {
  final String userId;

  const DepositListScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  DepositListScreenState createState() => DepositListScreenState();
}

class DepositListScreenState extends State<DepositListScreen> {
  List<DropdownMenuItem<DepositListItem>> _dropdownMenuItems;
  DepositListItem _selectedItem;
  List<Details> deposits = [];
  bool isListVisible = false;

  List<DepositListItem> _dropdownItems = [
    DepositListItem(
      1,
      "Pending",
      "Pending",
    ),
    DepositListItem(
      2,
      "All ",
      StringConstants.emptyString,
    ),
    DepositListItem(
      3,
      "Success",
      "Success",
    ),
    DepositListItem(
      4,
      "Failed",
      "Failed",
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
    selectedDatefrom = formatDateInit(
      DateTime.now(),
    );
    selectedDateTo = formatDateInit(
      DateTime.now(),
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      "_selectedItem  :  " + _selectedItem.name.toString(),
    );
    getDepositListData(
      fromTime: selectedDatefrom.millisecondsSinceEpoch,
      endTime: selectedDateTo.millisecondsSinceEpoch,
      status: _dropdownItems[0].apiName,
      userId: widget.userId,
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: DEPOSIT_HISTORY_ROUTE,
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
                      label: "deposit_history_screen.deposit".tr(),
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
                    ), //All
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
                    ), //Apply Button
                    CustomButton(
                      buttonText: "transaction_screen.apply".tr(),
                      height: 40,
                      width: 50.w,
                      onTap: () async {
                        final difference = selectedDateTo
                            .difference(
                              selectedDatefrom,
                            )
                            .inDays;
                        FocusScope.of(context).requestFocus(
                          FocusNode(),
                        );
                        if (selectedDatefrom.isAfter(
                          selectedDateTo,
                        )) {
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
                          getDepositListData(
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
              Expanded(
                child: Visibility(
                  visible: isListVisible,
                  child: Container(
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
                          height: 20,
                        ),
                        Row(
                          children: [
                            Expanded(
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
                            Expanded(
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
                              itemCount: deposits.length,
                              itemBuilder: (BuildContext context, int index) {
                                return Align(
                                  child: Column(
                                    children: [
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.of(context).push(
                                            CupertinoPageRoute(
                                              builder: (BuildContext context) =>
                                                  DepositDetailsScreen(
                                                depositDetails: deposits[index],
                                                userId: widget.userId,
                                              ),
                                            ),
                                          );
                                        },
                                        child: Row(
                                          children: [
                                            Expanded(
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    formatDate(
                                                      deposits[index]
                                                              .depositTime
                                                          .toString(),
                                                    ),
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
                                              child: Container(
                                                width: 90,
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    deposits[index].status,
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
                                              child: Container(
                                                decoration: BoxDecoration(),
                                                child: Center(
                                                  child: Text(
                                                    deposits[index].amount,
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
              )
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

  List<DropdownMenuItem<DepositListItem>> buildDropDownMenuItems(
    List withdrawalListItems,
  ) {
    List<DropdownMenuItem<DepositListItem>> items = [];
    for (DepositListItem listItem in withdrawalListItems) {
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

  Future getDepositListData({
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
      var repoObj = FetchDepositsRepo();
      DepositsDM depositsDM = await repoObj.fetchDeposits(
        fromTime: fromTime,
        endTime: endTime,
        status: status,
        userId: userId,
      );
      if (depositsDM != null) {
        switch (depositsDM.result) {
          case ResponseStatus.success:
            deposits = depositsDM.details;
            setState(
              () {
                if (deposits.length == 0) {
                  isListVisible = false;
                  CommonMethods.showSnackBar(
                    context,
                    "No deposit exists for user",
                  );
                } else {
                  isListVisible = true;
                }
              },
            );
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

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await getDepositListData(
                fromTime: fromTime,
                endTime: endTime,
                status: status,
                userId: userId,
              );
            }
            break;

          case ResponseStatus.invalidDateRange:
            setState(
              () {
                isListVisible = false;
              },
            );
            CommonMethods.showSnackBar(
              context,
              "Invalid Date Range.",
            );
            break;

          case ResponseStatus.noDepositsExist:
            setState(
              () {
                isListVisible = false;
              },
            );

            CommonMethods.showSnackBar(
              context,
              "No deposit exists for user",
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

  String formatDate(String time) {
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
}
