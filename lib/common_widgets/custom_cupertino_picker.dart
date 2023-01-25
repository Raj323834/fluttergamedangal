import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';
import '../constants/string_constants.dart';

class CustomCupertinoPicker extends StatelessWidget {
  const CustomCupertinoPicker({
    Key key,
    this.title='',
    @required this.onTap,
    @required this.onSelectedItemChanged,
    @required this.initialItem,
    @required this.list,
  }) : super(key: key);

  final String title;
  final VoidCallback onTap;
  final ValueChanged onSelectedItemChanged;
  final String initialItem;
  final List<String> list;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: ColorConstants.kBackgroundColor,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(
            10,
          ),
          topRight: Radius.circular(
            10,
          ),
        ),
      ),
      height: 37.0.h,
      padding: EdgeInsets.all(
        16,
      ),
      child: Column(
        children: [
          Align(
            alignment: Alignment.topRight,
            child: Container(
              height: 30,
              width: 75,
              decoration: BoxDecoration(
                gradient: ColorConstants.greenGradient,
                borderRadius: BorderRadius.circular(
                  5,
                ),
              ),
              child: ElevatedButton(
                onPressed: onTap,
                style: ElevatedButton.styleFrom(
                  primary: Colors.transparent,
                  shadowColor: Colors.transparent,
                ),
                child: Text(
                  StringConstants.done,
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ),
          ),
          Text(
            title,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 17.0.sp,
              color: ColorConstants.textYellow,
            ),
          ),
          Expanded(
            child: CupertinoPicker(
              scrollController: FixedExtentScrollController(
                initialItem: list.indexOf(
                  initialItem,
                ),
              ),
              itemExtent: 40,
              backgroundColor: Colors.transparent,
              children: List<Widget>.generate(
                list.length,
                (index) {
                  return Center(
                    child: Text(
                      list[index],
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: ColorConstants.textYellow,
                      ),
                    ),
                  );
                },
              ),
              onSelectedItemChanged: onSelectedItemChanged,
            ),
          ),
        ],
      ),
    );
  }
}
