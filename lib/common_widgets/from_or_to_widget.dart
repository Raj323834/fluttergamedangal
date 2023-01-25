import 'package:flutter/material.dart';

import '../constants/color_constants.dart';
import 'select_date_field.dart';

class FromOrTo extends StatelessWidget {
  const FromOrTo({
    Key key,
    @required this.onTap,
    @required this.isFrom,
    @required this.selectedDate,
  }) : super(key: key);
  final Function onTap;
  final bool isFrom;
  final DateTime selectedDate;
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          flex: 2,
          child: Text(
            isFrom ? 'From' : 'To',
            style: TextStyle(
              color: ColorConstants.white,
              fontWeight: FontWeight.bold,
              fontSize: 18,
            ),
          ),
        ),
        Expanded(
          flex: 3,
          child: GestureDetector(
            onTap: onTap,
            child: SelectDateField(
              selectedDate: selectedDate,
            ),
          ),
        ),
      ],
    );
  }
}
