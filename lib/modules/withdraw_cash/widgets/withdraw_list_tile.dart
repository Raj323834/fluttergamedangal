import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';

class WithdrawListTile extends StatelessWidget {
  const WithdrawListTile({
    Key key,
    @required this.onTap,
    @required this.isExpanded,
    @required this.title,
  }) : super(key: key);
  final Function onTap;
  final bool isExpanded;
  final String title;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(
            width: 2.5.w,
          ),
          Text(
            title,
            style: TextStyle(
              fontSize: 12.0.sp,
              fontWeight: FontWeight.bold,
              color: ColorConstants.white,
            ),
          ),
          Spacer(),
          isExpanded
              ? Transform.rotate(
                  angle: 90 * math.pi / 180,
                  child: Icon(
                    Icons.double_arrow,
                    color: ColorConstants.white,
                    size: 20,
                  ),
                )
              : Transform.rotate(
                  angle: 180 * math.pi / 90,
                  child: Icon(
                    Icons.double_arrow,
                    color: ColorConstants.white,
                    size: 20,
                  ),
                ),
        ],
      ),
    );
  }
}
