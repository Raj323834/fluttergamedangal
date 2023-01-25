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
import '../deposit/models/deposits_dm.dart';

class DepositDetailsScreen extends StatefulWidget {
  final Details depositDetails;
  final String userId;
  DepositDetailsScreen({
    @required this.depositDetails,
    @required this.userId,
  });

  @override
  DepositDetailsScreenState createState() => DepositDetailsScreenState();
}

class DepositDetailsScreenState extends State<DepositDetailsScreen> {
  @override
  void initState() {
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: DEPOSIT_DESC_ROUTE,
    );
    super.initState();
    CommonMethods.printLog(
      "Aparna",
      "userId  : " + widget.userId,
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
                label: "Deposit Details",
              ),
              SizedBox(
                height: 7.h,
              ),
              //Details Widget
              Container(
                height: 40.h,
                padding: const EdgeInsets.symmetric(
                  horizontal: 28.0,
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    //Id
                    DetailRow(
                      title: StringConstants.id,
                      value: widget.depositDetails.id.toString(),
                    ),
                    //Amount
                    DetailRow(
                      title: StringConstants.amount,
                      value: '\u{20B9}' + " " + widget.depositDetails.amount,
                    ),
                    //Date
                    DetailRow(
                      title: "Deposit Time",
                      value: formatDate(
                        widget.depositDetails.depositTime.toString(),
                      ),
                    ),
                    //Status
                    DetailRow(
                      title: 'Status',
                      value: widget.depositDetails.status,
                    ),
                    //Description
                    DetailRow(
                      title: StringConstants.description,
                      value: (widget.depositDetails.description == null ||
                              widget.depositDetails.description == "null")
                          ? StringConstants.na
                          : widget.depositDetails.description,
                    ),
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

    // final df = new DateFormat('dd-MM-yyyy hh:mm a');
    final df = new DateFormat(
      'dd-MM-yyyy  hh:mm a',
    );
    int time1 = int.parse(time);
    formattedDateTime = df
        .format(
          new DateTime.fromMillisecondsSinceEpoch(
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
      "time   :   " + time + "    formatted_time  :  " + formattedDateTime,
    );
    CommonMethods.printLog(
      "",
      "******************************************************",
    );
    return formattedDateTime;
  }
}
