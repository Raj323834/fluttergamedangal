import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:sizer/sizer.dart';
import 'package:easy_localization/easy_localization.dart';

import '../../../constants/app_constants.dart';
import '../../../constants/color_constants.dart';

class ProfileDialogBox extends StatelessWidget {
  const ProfileDialogBox({
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
        backgroundColor: ColorConstants.transparent,
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
              Text(
                "kyc_screen.you_must_complete".tr(),
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  fontSize: 13.5.sp,
                  color: ColorConstants.grey700,
                  fontWeight: FontWeight.bold,
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
                          "kyc_screen.complete_profile".tr(),
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
                  SizedBox(
                    width: 2.0.w,
                  ),
                  GestureDetector(
                    onTap: () {
                      Navigator.of(context)
                        ..pop()
                        ..pop();
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(
                          5,
                        ),
                        border: Border.all(
                          color: ColorConstants.grey,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "kyc_screen.go_back".tr(),
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                            color: ColorConstants.grey,
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
