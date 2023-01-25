import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../home/models/home_banners_dm.dart';

class BannersUI extends StatelessWidget {
  BannersUI({
    Key key,
    @required this.listOfBanners,
    this.isDummy = false,
  }) : super(key: key);

  final List<Banners> listOfBanners;
  final bool isDummy;

  @override
  Widget build(BuildContext context) {
    return CarouselSlider(
      options: CarouselOptions(
        height: 15.0.h,
        autoPlay: true,
        enableInfiniteScroll: true,
        initialPage: 0,
        pageSnapping: false,
      ),
      items: List.generate(
        (isDummy) ? 4 : listOfBanners.length,
        (index) {
          return Builder(
            builder: (_) {
              return Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 5,
                ),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(
                      15,
                    ),
                    image: (isDummy)
                        ? null
                        : DecorationImage(
                            image: NetworkImage(
                              listOfBanners[index].url,
                            ),
                            fit: BoxFit.fill,
                          ),
                  ),
                  width: 90.0.w,
                  height: 100,
                ),
              );
            },
          );
        },
      ),
    );
  }
}
