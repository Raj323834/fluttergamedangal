import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../constants/asset_paths.dart';
import '../constants/color_constants.dart';
import '../constants/string_constants.dart';

class Footer extends StatelessWidget {
  const Footer({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        //Icons
        SvgPicture.asset(
          AssetPaths.footerIcons,
        ),
        SizedBox(
          height: 20,
        ),
        //TnC Text
        Text(
          StringConstants.byLoggingIn,
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: 8,
            fontWeight: FontWeight.w500,
            color: ColorConstants.white,
          ),
        ),
      ],
    );
  }
}
