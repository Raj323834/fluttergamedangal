import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';

import '../../../constants/color_constants.dart';

class ResendOtpButton extends StatelessWidget {
  const ResendOtpButton({
    Key key,
    @required this.onTap,
    @required this.isTimerFinished,
  }) : super(key: key);
  final Function() onTap;
  final bool isTimerFinished;
  @override
  Widget build(BuildContext context) {
    return Visibility(
      visible: isTimerFinished,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          GestureDetector(
            onTap: onTap,
            child: Text(
              "otp_screen.resend_otp".tr(),
              style: TextStyle(
                color: ColorConstants.white,
                fontWeight: FontWeight.w700,
                fontSize: 13,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
