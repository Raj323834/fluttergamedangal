import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';

class ApplyPromocode extends StatelessWidget {
  const ApplyPromocode({
    Key key,
    @required this.onApply,
    @required this.onCancel,
    @required this.onClear,
    @required this.promocodeController,
  }) : super(key: key);

  final Function onApply;
  final Function onCancel;
  final Function onClear;
  final TextEditingController promocodeController;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: Dialog(
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Container(
          height: 25.0.h,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(
              2.0.w,
            ),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              Container(
                padding: EdgeInsets.symmetric(
                  vertical: 15,
                ),
                decoration: BoxDecoration(
                  color: ColorConstants.black87,
                  borderRadius: BorderRadius.circular(
                    1.9.w,
                  ),
                ),
                child: Center(
                  child: Text(
                    "Apply Promocode",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 14.0.sp,
                      color: ColorConstants.white,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              SizedBox(
                height: 4.5.h,
              ),
              Padding(
                padding: const EdgeInsets.only(
                  left: 15,
                  right: 15,
                ),
                child: Container(
                  height: 4.5.h,
                  child: TextFormField(
                    maxLines: 1,
                    controller: promocodeController,
                    decoration: InputDecoration(
                      suffixIcon: GestureDetector(
                        onTap: onClear,
                        child: Icon(
                          Icons.cancel_rounded,
                          color: ColorConstants.grey700,
                        ),
                      ),
                      contentPadding: EdgeInsets.only(
                        left: 10,
                        top: 6.0.sp,
                      ),
                      isCollapsed: true,
                      border: OutlineInputBorder(),
                      focusedBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.all(
                          Radius.circular(
                            5.0,
                          ),
                        ),
                      ),
                      filled: true,
                      labelText: "Enter code",
                      labelStyle: TextStyle(
                        fontSize: 9.5.sp,
                        color: ColorConstants.grey700,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
              SizedBox(
                height: 3.0.h,
              ),
              Align(
                alignment: Alignment.bottomCenter,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    GestureDetector(
                      onTap: onApply,
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 6,
                        ),
                        width: 25.0.w,
                        decoration: BoxDecoration(
                          gradient: ColorConstants.greenGradient,
                          borderRadius: BorderRadius.circular(
                            2.0.w,
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "Apply",
                            style: TextStyle(
                              color: ColorConstants.white,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                    SizedBox(
                      width: 3.0.w,
                    ),
                    GestureDetector(
                      onTap: onCancel,
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 5,
                        ),
                        width: 25.0.w,
                        decoration: BoxDecoration(
                          border: Border.all(
                            color: ColorConstants.grey700,
                          ),
                          borderRadius: BorderRadius.circular(
                            2.0.w,
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "Cancel",
                            style: TextStyle(
                              color: ColorConstants.grey700,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 0.4.h,
              )
            ],
          ),
        ),
      ),
    );
  }
}
