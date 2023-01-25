import 'package:flutter/material.dart';

import '../../../constants/color_constants.dart';

class LoyaltyBenefitsField extends StatelessWidget {
  const LoyaltyBenefitsField({
    Key key,
    @required this.title,
    @required this.value,
  }) : super(key: key);
  final String title;
  final String value;
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Expanded(
          flex: 7,
          child: Text(
            title,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: ColorConstants.white,
            ),
          ),
        ),
        SizedBox(
          width: 8,
        ),
        Expanded(
          child: Text(
            ':',
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: ColorConstants.white,
            ),
          ),
        ),
        Expanded(
          flex: 3,
          child: Text(
            value,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: ColorConstants.green,
            ),
          ),
        ),
      ],
    );
  }
}
