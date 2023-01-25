import '../../../constants/methods/reg_exp.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sizer/sizer.dart';

import 'bottom_sheet_button.dart';

class EnterReferralCodeWidget extends StatelessWidget {
  const EnterReferralCodeWidget({
    Key key,
    @required this.referCodeController,
    @required this.onApply,
  }) : super(key: key);
  final TextEditingController referCodeController;
  final VoidCallback onApply;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(
        left: 30,
        right: 30,
        top: 30,
        bottom: MediaQuery.of(context).viewInsets.bottom,
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            StringConstants.useCodeForExtraRewards,
            style: TextStyle(
              fontSize: 15.0.sp,
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(
            height: 3.0.h,
          ),
          TextFormField(
            controller: referCodeController,
            inputFormatters: [
              FilteringTextInputFormatter.allow(
                RegExpMethods.alphaNumeric,
              ),
            ],
            maxLength: 12,
            decoration: InputDecoration(
              contentPadding: EdgeInsets.only(
                top: 8.0.sp,
                left: 8.0.sp,
              ),
              enabledBorder: OutlineInputBorder(
                borderSide: BorderSide(
                  color: ColorConstants.greyBorderColor,
                ),
                borderRadius: BorderRadius.all(
                  Radius.circular(
                    5.0,
                  ),
                ),
              ),
              focusedBorder: OutlineInputBorder(
                borderSide: BorderSide(
                  color: ColorConstants.black,
                ),
                borderRadius: BorderRadius.all(
                  Radius.circular(
                    5.0,
                  ),
                ),
              ),
              filled: true,
              fillColor: Colors.white,
              counterText: StringConstants.emptyString,
              hintText: StringConstants.enterCodeHere,
              hintStyle: TextStyle(
                fontSize: 11.0.sp,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
          SizedBox(
            height: 3.0.h,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              //Cancel Button
              BottomSheetButton(
                onPressed: () {
                  Navigator.pop(context);
                  FocusScope.of(context).unfocus();
                },
                isCancel: true,
              ),
              SizedBox(
                width: 7.w,
              ),
              //Apply Button
              BottomSheetButton(
                onPressed: onApply,
                isCancel: false,
              ),
            ],
          ),
          SizedBox(
            height: 3.0.h,
          )
        ],
      ),
    );
  }
}
