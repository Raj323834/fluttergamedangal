import 'package:flutter/material.dart';

import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';

class OrWidget extends StatelessWidget {
  const OrWidget({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: AlignmentDirectional.center,
      children: [
        Divider(
          color: ColorConstants.white,
        ),
        Container(
          height: 25,
          width: 25,
          color: ColorConstants.kBackgroundColor,
          child: Center(
            child: Text(
              StringConstants.or,
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w700,
                color: ColorConstants.white,
              ),
            ),
          ),
        ),
      ],
    );
  }
}