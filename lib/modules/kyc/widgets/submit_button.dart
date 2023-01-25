import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../../../network_new/constants/responses_keys.dart';

class SubmitButton extends StatelessWidget {
  const SubmitButton({
    Key key,
    @required this.documentStatusAdd,
    @required this.documentStatusPan,
    @required this.onTap,
  }) : super(key: key);

  final String documentStatusAdd;
  final String documentStatusPan;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 4.5.h,
        width: 23.0.w,
        decoration: BoxDecoration(
          gradient: documentStatusAdd == ResponsesKeys.MANUAL_APPROVAL ||
                  documentStatusPan == ResponsesKeys.MANUAL_APPROVAL
              ? null
              : ColorConstants.blueDarkGradient,
          color: documentStatusAdd == ResponsesKeys.MANUAL_APPROVAL ||
                  documentStatusPan == ResponsesKeys.MANUAL_APPROVAL
              ? ColorConstants.grey
              : null,
          borderRadius: BorderRadius.circular(
            10,
          ),
        ),
        child: Center(
          child: Text(
            "kyc_screen.submit".tr(),
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              color: ColorConstants.white,
              fontWeight: FontWeight.bold,
              fontSize: 9.0.sp,
            ),
          ),
        ),
      ),
    );
  }
}
