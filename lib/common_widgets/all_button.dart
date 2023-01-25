import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../constants/color_constants.dart';

class AllButton extends StatelessWidget {
  const AllButton({
    Key key,
    @required this.onTap,
    @required this.selectedItem,
  }) : super(key: key);
  final Function onTap;
  final String selectedItem;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 4.5.h,
        decoration: BoxDecoration(
          gradient: ColorConstants.greyButtonGradient,
          borderRadius: BorderRadius.circular(5),
          border: Border.all(),
        ),
        child: Padding(
          padding: const EdgeInsets.only(
            left: 8.0,
          ),
          child: Row(
            children: [
              Expanded(
                flex: 3,
                child: Text(
                  selectedItem,
                  style: TextStyle(
                    fontSize: 8.5.sp,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              Expanded(
                flex: 1,
                child: Icon(
                  Icons.arrow_drop_down_circle,
                  size: 20,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
