class DealsDM {
  String result;
  List<DealsBanners> banners;

  DealsDM({this.result, this.banners});

  DealsDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['banners'] != null) {
      banners = <DealsBanners>[];
      json['banners'].forEach((v) {
        banners.add(new DealsBanners.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.banners != null) {
      data['banners'] = this.banners.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class DealsBanners {
  String url;
  String navigateTo;
  String benefits;
  List<String> termsAndConditions;
  String code;

  DealsBanners(
      {this.url,
      this.navigateTo,
      this.benefits,
      this.termsAndConditions,
      this.code});

  DealsBanners.fromJson(Map<String, dynamic> json) {
    url = json['url'];
    navigateTo = json['navigateTo'];
    benefits = json['benefits'];
    termsAndConditions = json['termsAndConditions'].cast<String>();
    code = json['code'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['url'] = this.url;
    data['navigateTo'] = this.navigateTo;
    data['benefits'] = this.benefits;
    data['termsAndConditions'] = this.termsAndConditions;
    data['code'] = this.code;
    return data;
  }
}
