import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';
import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../games/all_games_helper_service.dart';
import '../utils/shared_pref_service.dart';

class CustomAddReloadCoinDialog extends StatefulWidget {
  final String userId;

  const CustomAddReloadCoinDialog({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  CustomAddReloadCoinDialogState createState() =>
      CustomAddReloadCoinDialogState();
}

class CustomAddReloadCoinDialogState extends State<CustomAddReloadCoinDialog> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(
          Duration.zero,
        ).then(
          (value) => false,
        );
      },
      child: Dialog(
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Container(
          height: 21.0.h,
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
                    onTap: () async {
                      Navigator.pop(context);
                      String gChips =
                          await SharedPrefService.getStringValuesFromSharedPref(
                        SharedPrefKeys.chipsAmount,
                      );
                      if (gChips.toString().length >= 5) {
                        CommonMethods.showSnackBar(
                          context,
                          "You cannot reload more than 10000 chips",
                        );
                      } else {
                        await AllGamesHelperService.reloadCoins(
                          context,
                          widget.userId,
                        );
                      }
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
                          "Reload Coins",
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
              )
            ],
          ),
        ),
      ),
    );
  }
}
