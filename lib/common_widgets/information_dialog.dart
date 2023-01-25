import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class InformationDialog extends StatelessWidget {
  const InformationDialog({
    Key key,
    @required this.title,
    @required this.content,
  }) : super(key: key);
  final String title;
  final String content;
  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(
          10.0,
        ),
      ),
      child: Stack(
        alignment: AlignmentDirectional.topEnd,
        children: [
          Container(
            padding: EdgeInsets.all(
              33,
            ),
            constraints: BoxConstraints(
              maxHeight: 50.h,
            ),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(
                10.0,
              ),
            ),
            child: RawScrollbar(
              thumbColor: ColorConstants.greyScrollBar,
              crossAxisMargin: -25,
              mainAxisMargin: 18,
              isAlwaysShown: true,
              thickness: 7,
              radius: Radius.circular(
                10,
              ),
              child: SingleChildScrollView(
                physics: BouncingScrollPhysics(),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      title,
                      style: TextStyle(
                        fontWeight: FontWeight.w700,
                        fontSize: 16,
                      ),
                    ),
                    SizedBox(
                      height: 18,
                    ),
                    Text(
                      content,
                    ),
                  ],
                ),
              ),
            ),
          ),
          CloseButton(
            color: ColorConstants.lightGrey,
          ),
        ],
      ),
    );
  }
}
