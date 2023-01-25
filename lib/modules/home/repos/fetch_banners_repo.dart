import 'dart:convert';

import 'package:flutter/cupertino.dart';

import '../../../network_new/http_service.dart';
import '../models/home_banners_dm.dart';

class FetchBannersRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<HomeBannersDM> fetchBanners({
    @required String userId,
  }) async {
    HomeBannersDM homeBannersDM;
    try {
      var response = await httpServices.fetchBanners(
        userId: userId,
      );
      homeBannersDM = HomeBannersDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
    }
    return homeBannersDM;
  }
}
