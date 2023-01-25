import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';
import '../constants/string_constants.dart';

class BaseRetryDialog extends StatelessWidget {
  const BaseRetryDialog({
    Key key,
    @required this.onTap,
  }) : super(key: key);

  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(
          5.0,
        ),
      ),
      elevation: 40,
      backgroundColor: Colors.transparent,
      child: Stack(
        children: [
          Container(
            height: 25.0.h,
            margin: EdgeInsets.only(
              top: 45,
            ),
            decoration: BoxDecoration(
              color: Colors.white,
              shape: BoxShape.rectangle,
              borderRadius: BorderRadius.circular(
                30,
              ),
              boxShadow: [
                BoxShadow(
                  color: Colors.black,
                  offset: Offset(
                    0,
                    10,
                  ),
                  blurRadius: 20,
                ),
              ],
            ),
            child: Padding(
              padding: const EdgeInsets.all(
                1.0,
              ),
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(
                    color: ColorConstants.blue1,
                    width: 0.5.w,
                  ),
                  borderRadius: BorderRadius.circular(
                    30,
                  ),
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
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
                          StringConstants.noInternetConnectionPleaseRetry,
                          style: TextStyle(
                            fontSize: 15.0.sp,
                            fontWeight: FontWeight.w600,
                            color: Colors.grey[700],
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 2.0.h,
                    ),
                    GestureDetector(
                      onTap: onTap,
                      child: Align(
                        alignment: Alignment.topCenter,
                        child: Padding(
                          padding: const EdgeInsets.only(
                            right: 0.0,
                          ),
                          child: Text(
                            StringConstants.retry,
                            style: TextStyle(
                              fontSize: 16.0.sp,
                              fontWeight: FontWeight.bold,
                              color: Colors.blue[600],
                            ),
                          ),
                        ),
                      ),
                    ),
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
