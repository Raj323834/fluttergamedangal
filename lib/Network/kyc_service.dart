import 'dart:io';

import '../constants/methods/common_methods.dart';
import '../network_new/constants/responses_keys.dart';
import '../network_new/constants/url_constants.dart';
import 'network_post_request.dart';

class KycService {
  static const String TAG = "KYCService  ";
  static Future<Map<String, Object>> uploadDocument(
    String documentStatusAdd,
    String documentStatusPan,
    String addProofType,
    String addProofNumber,
    List frontAdd,
    List backAdd,
    String addFrontFileFormat,
    String panNumber,
    List panImage,
    String panFileFormat,
    String fImageUrl,
    String bImageUrl,
    String pImageUrl,
    String userId,
  ) async {
    if (addProofType == "Aadhaar") {
      addProofType = "Aadhar";
    }
    if (fImageUrl.length == 0) {
      fImageUrl = null;
    }
    if (bImageUrl.length == 0) {
      bImageUrl = null;
    }
    if (pImageUrl.length == 0) {
      pImageUrl = null;
    } else {
      panFileFormat = null;
    }
    if (addProofType == 'Driving Licence') {
      if (fImageUrl != null) {
        addFrontFileFormat = null;
      }
    } else {
      if (fImageUrl != null && bImageUrl != null) {
        addFrontFileFormat = null;
      }
    }

    Map requestData;
    if (addProofType == "Aadhar" &&
        (documentStatusPan == ResponsesKeys.APPROVED ||
            documentStatusPan == 'Approved')) {
      CommonMethods.printLog(
        "",
        "IF AADHAR and PAN APPROVEDDDDDDDDDD",
      );
      requestData = {
        "address_proof_type": addProofType.toUpperCase().replaceAll(
              " ",
              "_",
            ),
        "address_proof_number": addProofNumber,
        "address_file_format": addFrontFileFormat == "jpg"
            ? "JPEG"
            : addFrontFileFormat == null
                ? null
                : addFrontFileFormat.toUpperCase(),
        "pan_number": panNumber,
        "pan_file_Format": null,
        "address_front_data": frontAdd,
        "address_back_data": addProofType == 'Driving Licence' ? null : backAdd,
        "pan_data": null,
        "address_front_file_url": fImageUrl,
        "address_back_file_url": bImageUrl,
        "pan_file_url": null
      };
    } else if (documentStatusAdd == ResponsesKeys.APPROVED ||
        documentStatusAdd == 'Approved') {
      CommonMethods.printLog(
        "",
        "ADD APPROVEDDDDDDDDDD",
      );
      requestData = {
        "address_proof_type": null,
        "address_proof_number": null,
        "address_file_format": null,
        "pan_number": panNumber,
        "pan_file_Format": panFileFormat == "jpg"
            ? "JPEG"
            : panFileFormat == null
                ? null
                : panFileFormat.toUpperCase(),
        "address_front_data": null,
        "address_back_data": null,
        "pan_data": panImage,
        "address_front_file_url": null,
        "address_back_file_url": null,
        "pan_file_url": pImageUrl
      };
    } else if (documentStatusPan == ResponsesKeys.APPROVED ||
        documentStatusPan == 'Approved') {
      CommonMethods.printLog(
        "",
        "PAN APPROVEDDDDDDDDDD",
      );
      requestData = {
        "address_proof_type": addProofType.toUpperCase().replaceAll(
              " ",
              "_",
            ),
        "address_proof_number": addProofNumber,
        "address_file_format": addFrontFileFormat == "jpg"
            ? "JPEG"
            : addFrontFileFormat == null
                ? null
                : addFrontFileFormat.toUpperCase(),
        "pan_number": null,
        "pan_file_Format": null,
        "address_front_data": frontAdd,
        "address_back_data": addProofType == 'Driving Licence' ? null : backAdd,
        "pan_data": null,
        "address_front_file_url": fImageUrl,
        "address_back_file_url": bImageUrl,
        "pan_file_url": null
      };
    } else {
      CommonMethods.printLog(
        "",
        "NO APPROVEDDDDDDDDDD",
      );
      requestData = {
        "address_proof_type": addProofType.toUpperCase().replaceAll(
              " ",
              "_",
            ),
        "address_proof_number": addProofNumber,
        "address_front_data": frontAdd,
        "address_back_data": addProofType == 'Driving Licence' ? null : backAdd,
        "address_file_format": addFrontFileFormat == "jpg"
            ? "JPEG"
            : addFrontFileFormat == null
                ? null
                : addFrontFileFormat.toUpperCase(),
        "pan_number": panNumber,
        "pan_file_Format": panFileFormat == "jpg"
            ? "JPEG"
            : panFileFormat == null
                ? null
                : panFileFormat.toUpperCase(),
        "pan_data": panImage,
        "address_front_file_url": fImageUrl,
        "address_back_file_url": bImageUrl,
        "pan_file_url": pImageUrl
      };
    }

    Map<String, Object> result =
        await NetworkPostRequest.postRequestHttpClientWithAccessKyc(
      requestData,
      UrlConstants.uploadDocumentUrl,
      userId,
    );

    CommonMethods.printLog(
      "",
      "Submit KYC :--> " + result.toString(),
    );
    return result;
  }

  static parseImage(File image) {
    final bytes = image.readAsBytesSync();
    return bytes;
  }
}
