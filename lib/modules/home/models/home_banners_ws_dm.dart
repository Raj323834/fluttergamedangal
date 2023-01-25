import 'home_banners_dm.dart';

class HomeBannerWSDM {
  int source;
  String bannerType;
  List<Banners> banners;
  String type;

  HomeBannerWSDM({this.source, this.bannerType, this.banners, this.type});

  HomeBannerWSDM.fromJson(Map<String, dynamic> json) {
    source = json['source'];
    bannerType = json['bannerType'];
    if (json['banners'] != null) {
      banners = <Banners>[];
      json['banners'].forEach((v) {
        banners.add(new Banners.fromJson(v));
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
