import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../models/deals_dm.dart';

class TncContainer extends StatelessWidget {
  const TncContainer({
    Key key,
    @required this.index,
    @required this.listOfBanners,
  }) : super(key: key);
  final int index;
  final List<DealsBanners> listOfBanners;

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
            height: 60.0.h,
            padding: EdgeInsets.only(
              bottom: 22,
              left: 22,
              right: 22,
              top: 30,
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
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'BENEFITS',
                      style: TextStyle(
                        fontSize: 10.0.sp,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(
                      height: 0.5.h,
                    ),
                    Text(
                      listOfBanners[index].benefits.toUpperCase(),
                      textAlign: TextAlign.justify,
                      style: TextStyle(
                        fontSize: 9.0.sp,
                      ),
                    ),
                    SizedBox(
                      height: 1.0.h,
                    ),
                    Text(
                      "TERMS & CONDITIONS",
                      style: TextStyle(
                        fontSize: 10.0.sp,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(
                      height: 0.5.h,
                    ),
                    ListView.builder(
                      physics: NeverScrollableScrollPhysics(),
                      shrinkWrap: true,
                      itemCount: listOfBanners[index].termsAndConditions.length,
                      itemBuilder: (context, i) {
                        return Text(
                          '● ' + listOfBanners[index].termsAndConditions[i],
                          textAlign: TextAlign.justify,
                          style: TextStyle(
                            fontSize: 9.0.sp,
                          ),
                        );
                      },
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
