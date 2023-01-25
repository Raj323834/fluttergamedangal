import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/app_constants.dart';
import '../constants/color_constants.dart';
import '../constants/string_constants.dart';
import '../modules/home/home.dart';

class CustomAddCashDialog extends StatefulWidget {
  final String userId;

  const CustomAddCashDialog({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  CustomAddCashDialogState createState() => CustomAddCashDialogState();
}

class CustomAddCashDialogState extends State<CustomAddCashDialog> {
  @override
  Widget build(BuildContext context) {
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
          height: 20.0.h,
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
                "You must Add Cash before\nplaying the game",
                textAlign: TextAlign.center,
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
                      Navigator.pushAndRemoveUntil(
                        context,
                        CupertinoPageRoute(
                          builder: (BuildContext context) => HomeScreen(
                            landingPage: AppConstants.addCash,
                            routeDetail: StringConstants.emptyString,
                            userId: widget.userId,
                          ),
                        ),
                        (route) => false,
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
                          "Add Cash",
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
                            fontSize: 10.0.sp,
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
}
