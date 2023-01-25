class KycDocumentDM {
  String result;
  List<Documents> documents;
  Config config;
  String userId;

  KycDocumentDM({this.result, this.documents, this.config, this.userId});

  KycDocumentDM.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    if (json['documents'] != null) {
      documents = <Documents>[];
      json['documents'].forEach((v) {
        documents.add(new Documents.fromJson(v));
      });
    }
    config =
        json['config'] != null ? new Config.fromJson(json['config']) : null;
    userId = json['user_id'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.documents != null) {
      data['documents'] = this.documents.map((v) => v.toJson()).toList();
    }
    if (this.config != null) {
      data['config'] = this.config.toJson();
    }
    data['user_id'] = this.userId;
    return data;
  }
}

class Documents {
  String status;
  String reason;
  String documentType;
  String documentName;
  String documentNumber;
  String documentUrl;
  String documentFrontUrl;
  String documentBackUrl;

  Documents(
      {this.status,
      this.reason,
      this.documentType,
      this.documentName,
      this.documentNumber,
      this.documentUrl,
      this.documentFrontUrl,
      this.documentBackUrl});

  Documents.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    reason = json['reason'];
    documentType = json['document_type'];
    documentName = json['document_name'];
    documentNumber = json['document_number'];
    documentUrl = json['document_url'];
    documentFrontUrl = json['document_front_url'];
    documentBackUrl = json['document_back_url'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['reason'] = this.reason;
    data['document_type'] = this.documentType;
    data['document_name'] = this.documentName;
    data['document_number'] = this.documentNumber;
    data['document_url'] = this.documentUrl;
    data['document_front_url'] = this.documentFrontUrl;
    data['document_back_url'] = this.documentBackUrl;
    return data;
  }
}

class Config {
  String result;
  DocumentTypes documentTypes;
  Filesize filesize;
  List<String> allowedTypes;

  Config({this.result, this.documentTypes, this.filesize, this.allowedTypes});

  Config.fromJson(Map<String, dynamic> json) {
    result = json['result'];
    documentTypes = json['documentTypes'] != null
        ? new DocumentTypes.fromJson(json['documentTypes'])
        : null;
    filesize = json['filesize'] != null
        ? new Filesize.fromJson(json['filesize'])
        : null;
    allowedTypes = json['allowedTypes'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['result'] = this.result;
    if (this.documentTypes != null) {
      data['documentTypes'] = this.documentTypes.toJson();
    }
    if (this.filesize != null) {
      data['filesize'] = this.filesize.toJson();
    }
    data['allowedTypes'] = this.allowedTypes;
    return data;
  }
}

class DocumentTypes {
  List<String> address;
  List<String> id;

  DocumentTypes({this.address, this.id});

  DocumentTypes.fromJson(Map<String, dynamic> json) {
    address = json['address'].cast<String>();
    id = json['id'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['address'] = this.address;
    data['id'] = this.id;
    return data;
  }
}

class Filesize {
  int max;

  Filesize({this.max});

  Filesize.fromJson(Map<String, dynamic> json) {
    max = json['max'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['max'] = this.max;
    return data;
  }
}
