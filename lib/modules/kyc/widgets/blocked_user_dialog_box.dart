import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';
import '../../../constants/app_constants.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class BlockedUserDialogBox extends StatelessWidget {
  const BlockedUserDialogBox({
    Key key,
    @required this.onTap,
  }) : super(key: key);

  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => null,
      child: Dialog(
        elevation: 40,
        backgroundColor: Colors.transparent,
        child: Container(
          height: 23.0.h,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(
              AppConstants.circularBorder,
            ),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              SizedBox(
                height: 4.0.h,
              ),
              Padding(
                padding: EdgeInsets.only(
                  left: 2.0.w,
                  right: 2.0.w,
                ),
                child: Text(
                  StringConstants.restrictedActivityMessage,
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 13.5.sp,
                    color: ColorConstants.grey700,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              SizedBox(
                height: 2.5.h,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () {
                      Navigator.of(context)
                        ..pop()
                        ..pop();
                      onTap();
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        gradient: ColorConstants.blueDarkGradient,
                        borderRadius: BorderRadius.circular(
                          5,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          StringConstants.ok,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                            color: ColorConstants.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
