import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_widget_from_html/flutter_widget_from_html.dart';
import 'package:sizer/sizer.dart';

import '../../constants/color_constants.dart';
import '../constants/methods/common_methods.dart';

class CustomHtmlWidget extends StatelessWidget {
  final String path;
  const CustomHtmlWidget({
    Key key,
    @required this.path,
  }) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Container(
        padding: EdgeInsets.symmetric(
          vertical: 2.0.h,
          horizontal: 14,
        ),
        margin: EdgeInsets.symmetric(
          vertical: 2.0.h,
          horizontal: 14,
        ),
        decoration: BoxDecoration(
          border: Border.all(
            color: ColorConstants.grey,
          ),
          color: ColorConstants.white,
          borderRadius: BorderRadius.circular(
            10,
          ),
        ),
        child: FutureBuilder<String>(
          future: CommonMethods.getHtmlFromAsset(path: path),
          builder: (context,snapshot){
            if(snapshot.hasData){
              return HtmlWidget(snapshot.data);
            }
            return CircularProgressIndicator();
          },
        ),
      ),
    );
  }
}
