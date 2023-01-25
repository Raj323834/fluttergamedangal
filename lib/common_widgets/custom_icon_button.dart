import 'package:flutter/material.dart';

import '../constants/color_constants.dart';

class CustomIconButton extends StatelessWidget {
  const CustomIconButton({Key key, @required this.iconData, @required this.onTap,}) : super(key: key);
  final IconData iconData;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
            border: Border.all(color: Colors.transparent),
            color: Colors.transparent,
            borderRadius: BorderRadius.all(Radius.circular(2))),
        child: Center(
          child: Icon(iconData,color: ColorConstants.black,),
        ),
      ),
    );
  }
}
