import 'dart:convert';

import 'package:flutter/material.dart';

import '../../../network_new/http_service.dart';
import '../models/kyc_document_dm.dart';

class FetchKycDocumentsRepo {
  //Http Client
  HttpServices httpServices = HttpServices();

  //METHODS
  Future<KycDocumentDM> fetchKycDocuments({
    @required String userId,
  }) async {
    KycDocumentDM kycDocumentDM;

    try {
      var response = await httpServices.fetchKycDocsAndConfig(
        userId: userId,
      );

      kycDocumentDM = KycDocumentDM.fromJson(
        jsonDecode(
          response,
        ),
      );
    } catch (e) {
      //Will Return ErrorDm
      print('THIS IS THE ERROR $e');
    }
    return kycDocumentDM;
  }
}
