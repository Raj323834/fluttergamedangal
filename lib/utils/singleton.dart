import 'dart:ui';

import '../modules/deals/models/deals_dm.dart';
import '../modules/home/models/home_banners_dm.dart';

class Singleton {
  static final Singleton _singleton = Singleton._internal();

  factory Singleton() {
    return _singleton;
  }

  Singleton._internal();

  Size deviceSize; // will be nullable after migration

  List<Banners> _listOfBanners;

  List<Banners> get listOfBanners =>
      _listOfBanners != null ? _listOfBanners : [];

  set listOfBanners(List<Banners> value) {
    _listOfBanners = value;
  }

  List<DealsBanners> _listOfDealsBanners;
  List<DealsBanners> get listOfDealsBanners =>
      _listOfDealsBanners != null ? _listOfDealsBanners : [];
  set listOfDealsBanners(List<DealsBanners> value) {
    _listOfDealsBanners = value;
  }
}
