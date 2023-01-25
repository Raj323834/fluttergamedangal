import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/app_dimens.dart';
import '../constants/color_constants.dart';

class CustomPickerField extends StatelessWidget {
  const CustomPickerField({
    this.selectedName,
    this.gradient=ColorConstants.itemPickerGreyGradient,
    this.border,
    this.padding,
    this.onTap,
    Key key}) : super(key: key);
  final String selectedName;
  final Gradient gradient;
  final BoxBorder border;
  final EdgeInsetsGeometry padding;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: AppDimens.textFieldH,
        decoration: BoxDecoration(
          gradient: gradient,
          border: border,
          borderRadius: BorderRadius.circular(
            AppDimens.circularBorder,
          ),
        ),
        child: Padding(
          padding: padding??EdgeInsets.only(left: 1.5.h, right: 1.5.h),
          child: Row(
            children: [
              Text(
                selectedName,
                style: TextStyle(
                  fontSize: AppDimens.fontSize,
                  color: ColorConstants.black,
                  fontWeight: FontWeight.w500,
                ),
              ),
              Spacer(),
              Icon(
                Icons.arrow_drop_down_circle,
                size: AppDimens.iconSize,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
