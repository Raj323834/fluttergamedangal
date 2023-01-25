import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/deals_dm.dart';

class FetchDealsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<DealsDM> fetchBanners({
    @required String userId,
  }) async {
    DealsDM dealsDM;
    try {
      var response = await httpServices.fetchBanners(
        userId: userId,
        isDeals: true,
      );
      dealsDM = DealsDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
    }
    return dealsDM;
  }
}
