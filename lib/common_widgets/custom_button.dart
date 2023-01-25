import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class CustomButton extends StatelessWidget {
  const CustomButton({
    @required this.buttonText,
    @required this.onTap,
    this.fontSize,
    this.isEnabled = true,
    this.height,
    this.width,
    this.gradient = ColorConstants.goldGradient,
    this.fontWeight = FontWeight.w700,
    this.alignment = Alignment.center,
    this.textAlign = TextAlign.center,
    Key key,
  }) : super(key: key);
  final String buttonText;
  final VoidCallback onTap;
  final bool isEnabled;
  final double fontSize;
  final FontWeight fontWeight;
  final double height;
  final double width;
  final Gradient gradient;
  final TextAlign textAlign;
  final Alignment alignment;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: height,
        width: width,
        padding: EdgeInsets.all(1.0.h),
        decoration: BoxDecoration(
          gradient: isEnabled ? gradient : null,
          color: isEnabled ? null : ColorConstants.grey,
          borderRadius: BorderRadius.circular(
            5.0,
          ),
        ),
        child: Align(
          alignment: alignment,
          child: Text(
            '$buttonText',
            textAlign: textAlign,
            style: TextStyle(
              fontSize: fontSize ?? 11.0.sp,
              fontWeight: fontWeight,
              color: ColorConstants.white,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ),
      ),
    );
  }
}
