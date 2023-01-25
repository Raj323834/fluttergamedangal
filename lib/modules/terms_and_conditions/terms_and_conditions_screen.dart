import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_html_widget.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/flavor_info.dart';

class TermsAndConditionsScreen extends StatelessWidget {
  final String userId;
  final bool isLoggedIn;
  const TermsAndConditionsScreen({
    Key key,
    @required this.userId,
    this.isLoggedIn = true,
  }) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        appBar: isLoggedIn
            ? PreferredSize(
                preferredSize: Size(
                  double.infinity,
                  100,
                ),
                child: CustomAppBar(
                  from: "terms",
                  userId: userId,
                  bgColor: ColorConstants.appBarBgCol,
                ),
              )
            : AppBar(
                backgroundColor: ColorConstants.transparent,
                shadowColor: ColorConstants.transparent,
              ),
        body: CustomHtmlWidget(
          path: path,
        ),
      ),
    );
  }

  String get path => FlavorInfo.isPS
      ? AssetPaths.tncPSHtml
      : (FlavorInfo.isCoIn)
          ? AssetPaths.tncCoInHtml
          : AssetPaths.tncHtml;
}
