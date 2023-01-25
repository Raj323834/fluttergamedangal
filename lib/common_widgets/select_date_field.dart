import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class SelectDateField extends StatelessWidget {
  const SelectDateField({
    Key key,
    @required this.selectedDate,
  }) : super(key: key);

  final DateTime selectedDate;

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.centerRight,
      children: [
        Container(
          width: double.infinity,
          decoration: BoxDecoration(
            color: ColorConstants.white,
            borderRadius: BorderRadius.circular(5),
            border: Border.all(),
          ),
          child: Padding(
            padding: const EdgeInsets.all(
              10.0,
            ),
            child: Text(
              "${selectedDate.toLocal()}".split(' ')[0],
              style: TextStyle(
                fontSize: 9.5.sp,
              ),
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(
            right: 8.0,
          ),
          child: Icon(
            Icons.calendar_today,
            size: 18,
          ),
        ),
      ],
    );
  }
}
