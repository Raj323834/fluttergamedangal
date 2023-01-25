import 'package:flutter/material.dart';

class GridViewIcon extends StatelessWidget {
  const GridViewIcon({
    Key key,
    @required this.onTap,
    @required this.assetPath,
  }) : super(key: key);
  final void Function() onTap;
  final String assetPath;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Image.asset(
        assetPath,
        fit: BoxFit.fill,
      ),
    );
  }
}
