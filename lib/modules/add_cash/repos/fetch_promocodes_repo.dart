import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../constants/methods/common_methods.dart';
import '../../../network_new/http_service.dart';
import '../models/promo_codes_dm.dart';

class FetchPromoCodesRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<PromoCodesDM> fetchPromoCodes({
    @required String userId,
  }) async {
    PromoCodesDM promoCodesDM;

    try {
      var response = await httpServices.fetchPromoCodes(
        userId: userId,
      );

      promoCodesDM = PromoCodesDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      CommonMethods.devLog('THIS IS THE ERROR ${e.toString()}');
    }
    return promoCodesDM;
  }
}
