import '../../../constants/color_constants.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

class LabelAndActionRow extends StatelessWidget {
  const LabelAndActionRow({
    @required this.label,
    this.subLabel='',
    this.isMandatory=false,
    @required this.actionRow,
    this.labelFontSize,
    this.labelMaxLines=1,
    this.crossAxisAlignment = CrossAxisAlignment.center,
    Key key,
  }) : super(key: key);
  final String label;
  final String subLabel;
  final bool isMandatory;
  final double labelFontSize;
  final int labelMaxLines;
  final Widget actionRow;
  final CrossAxisAlignment crossAxisAlignment;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        crossAxisAlignment: crossAxisAlignment,
        children: [
          Expanded(
            flex: 3,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Flexible(
                      child: Text(
                        label,
                        maxLines: labelMaxLines,
                        style: TextStyle(
                          fontSize: labelFontSize ?? 9.2.sp,
                          fontWeight: FontWeight.bold,
                          color: ColorConstants.white,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    ),
                    if(isMandatory)
                    SizedBox(width: 2.0,),
                    if(isMandatory)
                      Text(
                        '*',
                        style: TextStyle(
                          fontSize: 9.0.sp,
                          fontWeight: FontWeight.bold,
                          color: Colors.red,
                        ),
                      ),
                  ],
                ),
                if(subLabel.isNotEmpty) Text(
                  subLabel,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 6.8.sp,
                    fontWeight: FontWeight.normal,
                    color: ColorConstants.white,
                  ),
                ),
              ],
            ),
          ),
          SizedBox(width: 0.8.h,),
          Expanded(
            flex: 7,
            child: actionRow,
          ),
        ],
      ),
    );
  }
}
