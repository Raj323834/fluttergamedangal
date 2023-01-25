import '../../../constants/asset_paths.dart';
import '../../../constants/color_constants.dart';
import '../../../utils/singleton.dart';
import 'package:flutter/material.dart';

class ReferAndEarnImage extends StatelessWidget {
  const ReferAndEarnImage({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: Singleton().deviceSize.width,
      height: 154,
      decoration: BoxDecoration(
        color: ColorConstants.white,
        borderRadius: BorderRadius.circular(
          10,
        ),
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(
          10,
        ),
        child: Image.asset(
          AssetPaths.referAndEarnImage,
          fit: BoxFit.fill,
        ),
      ),
    );
  }
}
