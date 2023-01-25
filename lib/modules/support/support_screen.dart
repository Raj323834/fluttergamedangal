import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../common_widgets/custom_app_bar.dart';
import '../../constants/color_constants.dart';
import 'support_sub_screen.dart';

class SupportScreen extends StatelessWidget {
  final String userId;

  const SupportScreen({
    Key key,
    this.userId,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: PreferredSize(
          preferredSize: Size(
            double.infinity,
            100,
          ),
          child: CustomAppBar(
            from: "support",
            userId: userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        backgroundColor: ColorConstants.kBackgroundColor,
        body: SupportSubScreen(
          userId: userId,
        ),
      ),
    );
  }
}
