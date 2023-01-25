class ErrorDm {
  int status;
  bool data;
  String message;

  ErrorDm({
    this.status,
    this.data,
    this.message,
  });

  ErrorDm.fromJson(Map<String, dynamic> json) {
    if (json["status"] is int) {
      status = json["status"];
    }
    if (json["data"] is bool) {
      data = json["data"];
    }
    if (json["message"] is String) {
      message = json["message"];
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data["status"] = status;
    data["data"] = this.data;
    data["message"] = message;
    return data;
  }
}
