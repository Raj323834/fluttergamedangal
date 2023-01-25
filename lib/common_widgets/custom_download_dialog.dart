import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';
import '../games/all_games_helper_service.dart';

class CustomDownloadDialog extends StatefulWidget {
  final Function downLoadButtonClickCallback;
  final String gameName;

  const CustomDownloadDialog({
    Key key,
    @required this.downLoadButtonClickCallback,
    @required this.gameName,
  }) : super(key: key);

  @override
  CustomDownloadDialogState createState() => CustomDownloadDialogState();
}

class CustomDownloadDialogState extends State<CustomDownloadDialog> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: Dialog(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(
            5.0,
          ),
        ),
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Stack(
          children: <Widget>[
            Container(
              height: 25.0.h,
              margin: EdgeInsets.only(
                top: 45,
              ),
              decoration: BoxDecoration(
                  color: ColorConstants.white,
                  shape: BoxShape.rectangle,
                  borderRadius: BorderRadius.circular(
                    30,
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: ColorConstants.black,
                      offset: Offset(0, 10),
                      blurRadius: 20,
                    ),
                  ]),
              child: Padding(
                padding: const EdgeInsets.all(1.0),
                child: Container(
                  decoration: BoxDecoration(
                    border: Border.all(
                      color: ColorConstants.blue1,
                      width: 0.5.w,
                    ),
                    borderRadius: BorderRadius.circular(
                      30,
                    ),
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      SizedBox(
                        height: 2.0.h,
                      ),
                      Align(
                        alignment: Alignment.topCenter,
                        child: Padding(
                          padding: const EdgeInsets.only(
                            left: 10.0,
                            right: 10.0,
                          ),
                          child: Text(
                            "New version of " +
                                AllGamesHelperService.getGameNameToDisplay(
                                  widget.gameName,
                                ) +
                                "\n is available",
                            style: TextStyle(
                              fontSize: 15.0.sp,
                              fontWeight: FontWeight.w600,
                              color: ColorConstants.grey700,
                            ),
                            textAlign: TextAlign.center,
                          ),
                        ),
                      ),
                      SizedBox(
                        height: 2.0.h,
                      ),
                      GestureDetector(
                        onTap: () {
                          Navigator.pop(context);
                          this.widget.downLoadButtonClickCallback(
                                widget.gameName,
                              );
                        },
                        child: Align(
                          alignment: Alignment.topCenter,
                          child: Padding(
                            padding: const EdgeInsets.only(
                              right: 0.0,
                            ),
                            child: Text(
                              "Update",
                              style: TextStyle(
                                fontSize: 16.0.sp,
                                fontWeight: FontWeight.bold,
                                color: ColorConstants.blue600,
                              ),
                            ),
                          ),
                        ),
                      ),
                      SizedBox(
                        height: 2.0.h,
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
