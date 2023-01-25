import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class DetailRow extends StatelessWidget {
  const DetailRow({
    Key key,
    @required this.title,
    @required this.value,
  }) : super(key: key);
  final String title;
  final String value;
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          flex: 5,
          child: Align(
            alignment: Alignment.centerLeft,
            child: Text(
              title,
              style: TextStyle(
                fontSize: 11.0.sp,
                fontWeight: FontWeight.bold,
                color: ColorConstants.white,
              ),
            ),
          ),
        ),
        Expanded(
          flex: 1,
          child: Text(
            ":",
            style: TextStyle(
              fontSize: 11.0.sp,
              color: ColorConstants.white,
            ),
          ),
        ),
        Expanded(
          flex: 8,
          child: Text(
            value,
            style: TextStyle(
              fontSize: 10.8.sp,
              fontWeight: FontWeight.normal,
              color: ColorConstants.white,
            ),
          ),
        ),
      ],
    );
  }
}
