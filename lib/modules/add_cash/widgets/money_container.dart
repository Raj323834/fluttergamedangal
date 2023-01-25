import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';

class MoneyContainer extends StatelessWidget {
  const MoneyContainer({
    Key key,
    @required this.cash,
    @required this.isSelected,
    @required this.onTap,
  }) : super(key: key);

  final String cash;
  final bool isSelected;
  final Function onTap;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        GestureDetector(
          onTap: onTap,
          child: Container(
            height: 4.0.h,
            width: 16.0.w,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(
                20.0.w,
              ),
              color: isSelected
                  ? ColorConstants.greyTextColor
                  : ColorConstants.grey350,
              border: Border.all(
                color: ColorConstants.greyTextColor,
              ),
            ),
            child: Center(
              child: Text(
                "₹ " + cash,
                style: TextStyle(
                  fontSize: 10.0.sp,
                  fontWeight: FontWeight.bold,
                  color:
                      isSelected ? ColorConstants.white : ColorConstants.black,
                ),
              ),
            ),
          ),
        ),
        SizedBox(
          width: 2.0.w,
        )
      ],
    );
  }
}
