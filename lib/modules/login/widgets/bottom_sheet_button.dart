import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

class BottomSheetButton extends StatelessWidget {
  const BottomSheetButton({
    Key key,
    @required this.onPressed,
    @required this.isCancel,
  }) : super(key: key);
  final Function() onPressed;
  final bool isCancel;
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 30.0.w,
      height: 40,
      decoration: (!isCancel)
          ? BoxDecoration(
              gradient: ColorConstants.greenGradient,
              borderRadius: BorderRadius.circular(
                5,
              ),
            )
          : null,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          primary: Colors.transparent,
          shadowColor: Colors.transparent,
          side: (isCancel) ? BorderSide() : null,
        ),
        child: Text(
          (isCancel) ? StringConstants.cancel : StringConstants.apply,
          style: TextStyle(
            fontSize: 13.0.sp,
            fontWeight: FontWeight.w500,
            color: (isCancel)
                ? ColorConstants.greyTextColor
                : ColorConstants.white,
          ),
        ),
      ),
    );
  }
}
