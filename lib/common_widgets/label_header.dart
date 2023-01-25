import 'package:flutter/material.dart';

import '../constants/color_constants.dart';

class LabelHeader extends StatelessWidget {
  const LabelHeader({
    @required this.label,
    this.fontSize=22,
    Key key,
  }) : super(key: key);

  final String label;
  final double fontSize;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(
          label,
          maxLines: 1,
          style: TextStyle(
            fontSize: fontSize,
            fontWeight: FontWeight.bold,
            color: ColorConstants.white,
          ),
        ),
        Spacer(),
      ],
    );
  }
}
