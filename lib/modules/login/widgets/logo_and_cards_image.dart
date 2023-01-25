import 'package:flutter/material.dart';

import '../../../constants/asset_paths.dart';

class LogoAndCardsImage extends StatelessWidget {
  const LogoAndCardsImage({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: AlignmentDirectional.topStart,
      children: [
        Image.asset(
          AssetPaths.cards,
          fit: BoxFit.scaleDown,
        ),
        Image.asset(
          AssetPaths.logo,
          height: 77,
          fit: BoxFit.fill,
        ),
      ],
    );
  }
}
