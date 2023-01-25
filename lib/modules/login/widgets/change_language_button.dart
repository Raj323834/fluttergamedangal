import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class ChangeLanguageButton extends StatelessWidget {
  const ChangeLanguageButton({
    Key key,
    @required this.onTap,
  }) : super(key: key);
  final VoidCallback onTap;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Text(
        StringConstants.changeLanguage,
        style: TextStyle(
          color: ColorConstants.white,
          fontSize: 10.5.sp,
          fontWeight: FontWeight.w400,
        ),
      ),
    );
  }
}
