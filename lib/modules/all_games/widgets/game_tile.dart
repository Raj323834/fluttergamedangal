import '../../../constants/color_constants.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

class GameTile extends StatelessWidget {
  const GameTile({
    Key key,
    @required this.onTap,
    @required this.iconPath,
    @required this.gameName,
  }) : super(key: key);
  final void Function() onTap;
  final String iconPath;
  final String gameName;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          image: DecorationImage(
            image: AssetImage(
              iconPath,
            ),
          ),
        ),
        child: Column(
          children: [
            Spacer(),
            Container(
              height: 5.0.h,
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    ColorConstants.transparent,
                    Colors.black87.withOpacity(
                      0.8,
                    )
                  ],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  stops: [
                    0.0,
                    0.8,
                  ],
                  tileMode: TileMode.clamp,
                ),
              ),
              child: Center(
                child: Text(
                  gameName,
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: ColorConstants.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 8.0.sp,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
