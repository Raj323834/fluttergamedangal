import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

class RummyOrPokerWidget extends StatelessWidget {
  const RummyOrPokerWidget({
    Key key,
    @required this.path,
    @required this.onTap,
    @required this.isRummy,
  }) : super(key: key);
  final String path;
  final void Function() onTap;
  final bool isRummy;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Image.asset(
        path,
        height: (isRummy) ? 33.0.h : 28.0.h,
      ),
    );
  }
}
