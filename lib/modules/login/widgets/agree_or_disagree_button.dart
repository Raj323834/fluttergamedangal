import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class AgreeOrDisagreeButton extends StatelessWidget {
  const AgreeOrDisagreeButton({
    Key key,
    @required this.onTap,
    @required this.isAgreeButton,
  }) : super(key: key);
  final VoidCallback onTap;
  final bool isAgreeButton;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 5.2.h,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [
              ColorConstants.lightYellow,
              ColorConstants.brownishYellow,
            ],
            begin: Alignment.topLeft,
            end: Alignment.bottomLeft,
            stops: [
              0.0,
              0.8,
            ],
            tileMode: TileMode.clamp,
          ),
          borderRadius: BorderRadius.circular(
            10.0,
          ),
        ),
        child: Center(
          child: Text(
            (isAgreeButton == true)
                ? StringConstants.iAgree
                : StringConstants.iDisagree,
            style: TextStyle(
              fontSize: 10.0.sp,
              fontWeight: FontWeight.w700,
              color: ColorConstants.textBrown,
            ),
          ),
        ),
      ),
    );
  }
}
