import '../../../constants/asset_paths.dart';
import '../../../constants/color_constants.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

class FantasyDangalBanner extends StatelessWidget {
  const FantasyDangalBanner({
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
        height: 18.0.h,
        width: 85.0.w,
        decoration: BoxDecoration(
          image: DecorationImage(
            image: AssetImage(
              iconPath,
            ),
            fit: BoxFit.fill,
          ),
        ),
        child: Column(
          children: [
            SizedBox(
              height: 0.5.h,
            ),
            Text(
              gameName,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 16.0.sp,
                color: ColorConstants.white,
              ),
            ),
            Padding(
              padding: EdgeInsets.only(
                top: 8.5.h,
                left: 50.0.w,
              ),
              child: Container(
                height: 5.0.h,
                width: 30.0.w,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    fit: BoxFit.fitWidth,
                    image: AssetImage(
                      AssetPaths.playNow,
                    ),
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
