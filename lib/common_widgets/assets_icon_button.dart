import 'package:flutter/material.dart';

class AssetsIconButton extends StatelessWidget {
  const AssetsIconButton({Key key, @required this.path, @required this.onTap,}) : super(key: key);
  final String path;
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
          child: Image.asset(
            path,
          ),
        ),
      ),
    );
  }
}
