class DepositsDM {
  String result;
  List<Details> details;

  DepositsDM({this.result, this.details});

  DepositsDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['details'] != null) {
      details = <Details>[];
      json['details'].forEach((v) {
        details.add(new Details.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.details != null) {
      data['details'] = this.details.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Details {
  String id;
  String orderid;
  String amount;
  String status;
  String description;
  int depositTime;

  Details(
      {this.id,
      this.orderid,
      this.amount,
      this.status,
      this.description,
      this.depositTime});

  Details.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    orderid = json['orderid'];
    amount = json['amount'];
    status = json['status'];
    description = json['description'];
    depositTime = json['deposit_time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['orderid'] = this.orderid;
    data['amount'] = this.amount;
    data['status'] = this.status;
    data['description'] = this.description;
    data['deposit_time'] = this.depositTime;
    return data;
  }
}
