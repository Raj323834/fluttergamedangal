import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';

class DrawerTile extends StatelessWidget {
  const DrawerTile({
    Key key,
    @required this.icon,
    @required this.tileName,
    @required this.onTap,
  }) : super(key: key);
  final Widget icon;
  final String tileName;
  final Function() onTap;
  @override
  Widget build(BuildContext context) {
    return ListTile(
      contentPadding: EdgeInsets.symmetric(
        horizontal: 5.w,
        vertical: 0,
      ),
      minLeadingWidth: 10,
      visualDensity: VisualDensity(
        vertical: -3,
      ),
      leading: icon,
      title: Text(
        tileName,
        style: TextStyle(
          color: ColorConstants.lightGrey,
        ),
      ),
      onTap: onTap,
    );
  }
}
