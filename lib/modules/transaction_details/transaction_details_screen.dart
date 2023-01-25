import 'package:dangal_games_demo/modules/transaction/models/transactions_dm.dart';
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

class TransactionDetailsScreen extends StatefulWidget {
  final WalletInfo transactiondesc;
  final String userId;
  TransactionDetailsScreen({
    @required this.transactiondesc,
    @required this.userId,
  });

  @override
  _TransactionDetailsScreenState createState() =>
      _TransactionDetailsScreenState();
}

class _TransactionDetailsScreenState extends State<TransactionDetailsScreen> {
  @override
  void initState() {
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: TRANSACTION_DESC_ROUTE,
    );
    super.initState();
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
                label: "Transaction Details",
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
                      value: widget.transactiondesc.id,
                    ),
                    //Amount
                    DetailRow(
                      title: StringConstants.amount,
                      value: '\u{20B9}' + " " + widget.transactiondesc.amount,
                    ),
                    //Date
                    DetailRow(
                      title: StringConstants.date,
                      value: formatDate(
                        widget.transactiondesc.transactionTime,
                      ),
                    ),
                    //Wallet Name
                    DetailRow(
                      title: StringConstants.walletName,
                      value: widget.transactiondesc.walletName,
                    ),
                    //Transaction Type
                    DetailRow(
                      title: StringConstants.transactionType,
                      value: widget.transactiondesc.transactionType,
                    ),
                    //Description
                    DetailRow(
                      title: StringConstants.description,
                      value: widget.transactiondesc.description,
                    ),
                  ],
                ),
              ),
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
      "time   :   " + time + "    formatted_time  :  " + formattedDateTime,
    );
    CommonMethods.printLog(
      "",
      "******************************************************",
    );
    return formattedDateTime;
  }
}
