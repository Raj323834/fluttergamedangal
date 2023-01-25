import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/color_constants.dart';
import '../models/deals_dm.dart';

class DealsBannerWidget extends StatelessWidget {
  const DealsBannerWidget({
    Key key,
    @required this.index,
    @required this.onTap,
    @required this.knowMore,
    @required this.listOfBanners,
  }) : super(key: key);
  final int index;
  final Function onTap;
  final Function knowMore;
  final List<DealsBanners> listOfBanners;
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Stack(
          alignment: Alignment.bottomRight,
          children: [
            GestureDetector(
              onTap: onTap,
              child: Container(
                height: 18.0.h,
                decoration: BoxDecoration(
                  color: ColorConstants.kBackgroundColor,
                  borderRadius: BorderRadius.circular(
                    2.0.w,
                  ),
                  image: DecorationImage(
                    image: NetworkImage(
                      listOfBanners[index].url,
                    ),
                    fit: BoxFit.fill,
                  ),
                ),
              ),
            ),
            GestureDetector(
              onTap: knowMore,
              child: Image(
                image: AssetImage(
                  'assets/images/know_more.webp',
                ),
                height: 2.8.h,
              ),
            ),
          ],
        ),
        SizedBox(
          height: 2.0.h,
        )
      ],
    );
  }
}
