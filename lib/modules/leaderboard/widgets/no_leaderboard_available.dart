import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';
import 'package:flutter/material.dart';

class NoLeaderBoardAvailable extends StatelessWidget {
  const NoLeaderBoardAvailable({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text(
        StringConstants.noLeaderboardsCurrently,
        style: TextStyle(
          fontSize: 20,
          color: ColorConstants.greyTextColor,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}
