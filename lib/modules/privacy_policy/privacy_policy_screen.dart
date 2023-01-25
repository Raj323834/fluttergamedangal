import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_html_widget.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/flavor_info.dart';

class PrivacyPolicyScreen extends StatelessWidget {
  final String userId;

  const PrivacyPolicyScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        appBar: PreferredSize(
          preferredSize: Size(
            double.infinity,
            100,
          ),
          child: CustomAppBar(
            from: "PrivacyPolicy",
            userId: userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        body: CustomHtmlWidget(
          path: path,
        ),
      ),
    );
  }

  String get path => FlavorInfo.isPS ? AssetPaths.ppPSHtml : AssetPaths.ppHtml;
}
