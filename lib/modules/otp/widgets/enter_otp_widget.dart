import '../../../constants/color_constants.dart';
import '../../../constants/methods/reg_exp.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:otp_text_field/otp_field_style.dart';
import 'package:otp_text_field/otp_text_field.dart';
import 'package:otp_text_field/style.dart';

class EnterOtpWidget extends StatelessWidget {
  const EnterOtpWidget({
    Key key,
    @required this.onCompleted,
    @required this.otpFieldController,
  }) : super(key: key);
  final Function(String) onCompleted;
  final OtpFieldController otpFieldController;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        //Enter OTP Text
        Text(
          "otp_screen.enter_otp".tr(),
          style: TextStyle(
            color: ColorConstants.lightGreyText,
            fontSize: 15,
            fontWeight: FontWeight.w400,
          ),
        ),
        SizedBox(
          height: 12,
        ),
        //Otp Field
        Container(
          height: 34,
          width: double.infinity,
          padding: EdgeInsets.symmetric(
            horizontal: 60,
          ),
          child: OTPTextField(
            length: 6,
            controller: otpFieldController,
            style: TextStyle(
              fontSize: 14,
              color: ColorConstants.white,
            ),
            textFieldAlignment: MainAxisAlignment.spaceAround,
            fieldStyle: FieldStyle.box,
            otpFieldStyle: OtpFieldStyle(
              borderColor: ColorConstants.greyBorderColor,
              enabledBorderColor: ColorConstants.greyBorderColor,
              focusBorderColor: ColorConstants.greyBorderColor,
              errorBorderColor: ColorConstants.red,
            ),
            outlineBorderRadius: 5,
            fieldWidth: 30,
            spaceBetween: 10,
            inputFormatter: [
              FilteringTextInputFormatter.allow(
                RegExpMethods.digits,
              ),
            ],
            keyboardType: TextInputType.number,
            onChanged: (pin) {},
            onCompleted: onCompleted,
          ),
        ),
      ],
    );
  }
}
