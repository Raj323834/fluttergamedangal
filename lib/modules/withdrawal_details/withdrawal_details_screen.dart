import '../withdrawals/models/withdrawals_dm.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/detail_row.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/string_constants.dart';

class WithdrawalDetailsScreen extends StatefulWidget {
  final WithdrawalDetails withdrawaldesc;
  final String userId;

  WithdrawalDetailsScreen({
    @required this.withdrawaldesc,
    @required this.userId,
  });

  @override
  WithdrawalDetailsScreenState createState() => WithdrawalDetailsScreenState();
}

class WithdrawalDetailsScreenState extends State<WithdrawalDetailsScreen> {
  @override
  void initState() {
    super.initState();
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: WITHDRAWAL_DESC_ROUTE,
    );
    CommonMethods.printLog(
      "withdrawaldesc",
      widget.withdrawaldesc.toString(),
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
              //Label
              LabelHeader(
                label: "Withdrawal Details",
              ),
              SizedBox(
                height: 7.h,
              ),
              //Details Widget
              Container(
                height: 40.h,
                padding: const EdgeInsets.symmetric(
                  horizontal: 30.0,
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    //Id
                    DetailRow(
                      title: StringConstants.id,
                      value: widget.withdrawaldesc.id,
                    ),
                    //Amount
                    DetailRow(
                      title: StringConstants.amount,
                      value: '\u{20B9}' + " " + widget.withdrawaldesc.amount,
                    ),
                    //Withdrawal Time
                    DetailRow(
                      title: 'Withdrawal Time',
                      value: formatDate(
                        widget.withdrawaldesc.withdrawalTime.toString(),
                      ),
                    ),
                    //Wallet Fullfilled Time
                    DetailRow(
                      title: 'Wallet Fullfilled Time',
                      value: (widget.withdrawaldesc.fulfilledTime != 0)
                          ? formatDate(
                              widget.withdrawaldesc.fulfilledTime.toString(),
                            )
                          : StringConstants.na,
                    ),
                    //Status
                    DetailRow(
                      title: 'Status',
                      value: widget.withdrawaldesc.status,
                    ),
                    //Reason
                    Visibility(
                      visible: !(widget.withdrawaldesc.reason ==
                              StringConstants.emptyString ||
                          widget.withdrawaldesc.reason == null ||
                          widget.withdrawaldesc.reason.toString() == "null"),
                      child: DetailRow(
                        title: 'Reason',
                        value: widget.withdrawaldesc.reason,
                      ),
                    )
                  ],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }

  //METHODS
  String formatDate(dynamic time) {
    String formattedDateTime;

    // final df =  DateFormat('dd-MM-yyyy hh:mm a');
    final df = DateFormat(
      'dd-MM-yyyy  hh:mm a',
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
      "",
      "******************************************************",
    );
    CommonMethods.printLog(
      "",
      "time   :   " +
          time +
          "    formatted_time  :  " +
          formattedDateTime.toString(),
    );
    CommonMethods.printLog(
      "",
      "******************************************************",
    );
    return formattedDateTime;
  }
}
