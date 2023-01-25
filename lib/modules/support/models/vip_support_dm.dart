class VipSupportDM {
  String result;
  String supportContactNumber;

  VipSupportDM({
    this.result,
    this.supportContactNumber,
  });

  VipSupportDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    supportContactNumber = json['supportContactNumber'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    data['supportContactNumber'] = this.supportContactNumber;
    return data;
  }
}
