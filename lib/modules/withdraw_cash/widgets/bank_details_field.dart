import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class BankDetailsField extends StatelessWidget {
  const BankDetailsField({
    Key key,
    @required this.title,
    @required this.keyboardType,
    @required this.controller,
    @required this.maxLength,
    @required this.regex,
  }) : super(key: key);
  final String title;
  final TextInputType keyboardType;
  final TextEditingController controller;
  final int maxLength;
  final RegExp regex;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(
        left: 15,
        right: 15,
      ),
      child: Container(
        child: Row(
          children: [
            Expanded(
              flex: 4,
              child: Text(
                title,
                style: TextStyle(
                  fontSize: 8.0.sp,
                  color: ColorConstants.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Expanded(
              flex: 1,
              child: Text(
                ":",
                style: TextStyle(
                  fontSize: 8.0.sp,
                  color: ColorConstants.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Expanded(
              flex: 7,
              child: SizedBox(
                height: 3.0.h,
                child: TextFormField(
                  textCapitalization: title == "Account Holder"
                      ? TextCapitalization.words
                      : TextCapitalization.characters,
                  inputFormatters: [
                    FilteringTextInputFormatter.allow(regex),
                  ],
                  maxLength: maxLength,
                  controller: controller,
                  style: TextStyle(
                    fontSize: 9.0.sp,
                    color: ColorConstants.white,
                  ),
                  keyboardType: keyboardType,
                  decoration: InputDecoration(
                    counterText: StringConstants.emptyString,
                    contentPadding: EdgeInsets.only(
                      left: 5,
                    ),
                    border: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: ColorConstants.white,
                      ),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: ColorConstants.white,
                      ),
                      borderRadius: BorderRadius.all(
                        Radius.circular(
                          5.0,
                        ),
                      ),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: ColorConstants.white,
                      ),
                      borderRadius: BorderRadius.all(
                        Radius.circular(
                          5.0,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
