import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/app_dimens.dart';
import '../constants/color_constants.dart';

class CustomHeader extends StatelessWidget {
  CustomHeader({
    @required this.title,
    @required this.asset,
  });
  final String title;
  final String asset;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: AppDimens.homeWidgetWidth,
      height: AppDimens.headerHeight,
      decoration: BoxDecoration(
        gradient: ColorConstants.blueDarkGradient,
        borderRadius: BorderRadius.circular(
          AppDimens.circularBorder,
        ),
      ),
      child: Stack(
        children: [
          Center(
            child: Padding(
              padding: EdgeInsets.only(
                left: 8.0.w,
                right: 8.0.w,
              ),
              child: Text(
                title,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontSize: 15.0.sp,
                  fontWeight: FontWeight.bold,
                  color: ColorConstants.white,
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(
              right: 10,
            ),
            child: Align(
              alignment: Alignment.centerRight,
              child: Image.asset(
                asset,
                color: ColorConstants.white,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
