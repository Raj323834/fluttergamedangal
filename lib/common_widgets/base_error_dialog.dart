import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class BaseErrorDialog extends StatelessWidget {
  const BaseErrorDialog({
    Key key,
    @required this.title,
    @required this.error,
  }) : super(key: key);
  final String title;
  final String error;

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(5.0),
      ),
      elevation: 40,
      backgroundColor: ColorConstants.transparent,
      child: Stack(
        children: [
          Container(
            height: 22.0.h,
            margin: EdgeInsets.only(
              top: 45,
            ),
            decoration: BoxDecoration(
              color: ColorConstants.white,
              shape: BoxShape.rectangle,
              borderRadius: BorderRadius.circular(30),
              boxShadow: [
                BoxShadow(
                  color: ColorConstants.black,
                  offset: Offset(
                    0,
                    10,
                  ),
                  blurRadius: 20,
                ),
              ],
            ),
            child: Padding(
              padding: const EdgeInsets.all(1.0),
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(
                    color: ColorConstants.blue1,
                    width: 0.5.w,
                  ),
                  borderRadius: BorderRadius.circular(30),
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Align(
                      alignment: Alignment.topCenter,
                      child: Padding(
                        padding: const EdgeInsets.only(
                          left: 0.0,
                        ),
                        child: Text(
                          title,
                          style: TextStyle(
                            fontSize: 14.0.sp,
                            fontWeight: FontWeight.w600,
                            color: ColorConstants.grey700,
                          ),
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 2.0.h,
                    ),
                    Align(
                      alignment: Alignment.topCenter,
                      child: Padding(
                        padding: const EdgeInsets.only(
                          left: 10.0,
                          right: 10.0,
                        ),
                        child: Text(
                          error,
                          style: TextStyle(
                            fontSize: 12.0.sp,
                            fontWeight: FontWeight.w600,
                            color: ColorConstants.grey700,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 2.0.h,
                    ),
                    CloseButton(),
                    SizedBox(
                      height: 2.0.h,
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
