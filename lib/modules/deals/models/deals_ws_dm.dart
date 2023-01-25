import 'deals_dm.dart';

class DealsWSDM {
  int source;
  String bannerType;
  List<DealsBanners> banners;
  String type;

  DealsWSDM({this.source, this.bannerType, this.banners, this.type});

  DealsWSDM.fromJson(Map<String, dynamic> json) {
    source = json['source'];
    bannerType = json['bannerType'];
    if (json['banners'] != null) {
      banners = <DealsBanners>[];
      json['banners'].forEach((v) {
        banners.add(new DealsBanners.fromJson(v));
      });
    }
    type = json['type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['source'] = this.source;
    data['bannerType'] = this.bannerType;
    if (this.banners != null) {
      data['banners'] = this.banners.map((v) => v.toJson()).toList();
    }
    data['type'] = this.type;
    return data;
  }
}
