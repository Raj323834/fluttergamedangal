import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';

import '../../constants/enum.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/responses_keys.dart';

class KycValidators {
  String statusCheck(
    String status, {
    @required String fileSizeMax,
  }) {
    if (status == ResponsesKeys.APPROVED) {
      return StringConstants.approved;
    } else if (status == ResponsesKeys.DB_ERROR) {
      return "Server Error";
    } else if (status == ResponsesKeys.FILE_CREATION_FAILED) {
      return "Server Error";
    } else if (status == ResponsesKeys.DOC_UPLOAD_FAILED) {
      return "Server Error.";
    } else if (status == ResponsesKeys.UPS_NOT_REACHABLE) {
      return "Server Error";
    } else if (status == ResponsesKeys.USER_NOT_FOUND) {
      return "Invalid User";
    } else if (status == ResponsesKeys.SUCCESS) {
      return StringConstants.approved;
    } else if (status == ResponsesKeys.INVALID_DOCUMENT) {
      return StringConstants.invalidDoc;
    } else if (status == ResponsesKeys.INVALID_AADHAR) {
      return StringConstants.invalidDoc;
    } else if (status == ResponsesKeys.CENTRAL_DB_API_FAILURE) {
      return "Server Error";
    } else if (status == ResponsesKeys.IMAGE_SIZE_LIMIT_REACHED) {
      return "${StringConstants.uploadFileLesserThan} " +
          (int.parse(fileSizeMax) / 1024).toStringAsFixed(3) +
          " ${StringConstants.mbDot}";
    } else if (status == ResponsesKeys.IMAGE_DOWNLOAD_FAILED) {
      return "Server Error";
    } else if (status == ResponsesKeys.INVALID_IMAGE) {
      return "Invalid Image";
    } else if (status == ResponsesKeys.OCR_API_FAILURE) {
      return "Server Error";
    } else if (status == ResponsesKeys.PROFILE_MISMATCH) {
      return "Profile Mismatch";
    } else if (status == ResponsesKeys.REJECTED) {
      return "Rejected";
    } else if (status == ResponsesKeys.UPLOADED) {
      return "Uploaded";
    } else if (status == ResponsesKeys.INITIATED) {
      return "Initiated";
    } else if (status == ResponsesKeys.API_FAILURE) {
      return "Server Error";
    } else if (status == ResponsesKeys.DUPLICATE_DOCUMENT) {
      return "Duplicate Document";
    } else if (status == ResponsesKeys.INVALID_AADHAR_FRONT_IMAGE) {
      return StringConstants.invalidDoc;
    } else if (status == ResponsesKeys.INVALID_ADDRESS_FRONT_IMAGE) {
      return StringConstants.invalidDoc;
    } else if (status == ResponsesKeys.INVALID_ADDRESS_BACK_IMAGE) {
      return StringConstants.invalidDoc;
    } else if (status == ResponsesKeys.MANUAL_APPROVAL) {
      return "Pending";
    } else if (status == ResponsesKeys.INVALID_AGE) {
      return "Rejected";
    } else {
      return "NA";
    }
  }

  String reasonCheck(
    String status, {
    @required String fileSizeMax,
  }) {
    if (status == ResponsesKeys.APPROVED) {
      return "Documents uploaded Successfully";
    } else if (status == ResponsesKeys.DB_ERROR) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.FILE_CREATION_FAILED) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.API_FAILURE) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.DOC_UPLOAD_FAILED) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.UPS_NOT_REACHABLE) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.USER_NOT_FOUND) {
      return "User does not exist";
    } else if (status == ResponsesKeys.INVALID_IMAGE) {
      return "Provided image is not valid.";
    } else if (status == ResponsesKeys.SUCCESS) {
      return "Documents uploaded Successfully";
    } else if (status == ResponsesKeys.INVALID_DOCUMENT) {
      return "Provided document number is not valid";
    } else if (status == ResponsesKeys.INVALID_AADHAR) {
      return "Provided Aadhaar or Pan number is not valid.";
    } else if (status == ResponsesKeys.CENTRAL_DB_API_FAILURE) {
      return "Internal server error. Please try again";
    } else if (status == ResponsesKeys.IMAGE_SIZE_LIMIT_REACHED) {
      return "${StringConstants.uploadFileLesserThan} " +
          (int.parse(fileSizeMax) / 1024).toStringAsFixed(3) +
          " ${StringConstants.mbDot}";
    } else if (status == ResponsesKeys.IMAGE_DOWNLOAD_FAILED) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.DUPLICATE_DOCUMENT) {
      return "Provided document is in use.";
    } else if (status == ResponsesKeys.OCR_API_FAILURE) {
      return "Internal server error. Please try again.";
    } else if (status == ResponsesKeys.PROFILE_MISMATCH) {
      return "Mismatch in profile. Your kyc will be processed manually.";
    } else if (status == ResponsesKeys.REJECTED) {
      return "Your Documents have been Rejected.";
    } else if (status == ResponsesKeys.UPLOADED) {
      return "Documents uploaded Successfully";
    } else if (status == "NA") {
      return "NA";
    } else if (status == ResponsesKeys.INITIATED) {
      return "Your KYC approval is in progress.";
    } else if (status == ResponsesKeys.INVALID_AADHAR_FRONT_IMAGE) {
      return "Please provide aadhaar front image.";
    } else if (status == ResponsesKeys.INVALID_ADDRESS_FRONT_IMAGE) {
      return "Please provide address front image.";
    } else if (status == ResponsesKeys.INVALID_ADDRESS_BACK_IMAGE) {
      return "Please provide address back image.";
    } else if (status == ResponsesKeys.MANUAL_APPROVAL) {
      return "Your KYC will be processed manually within 24~48 hours";
    } else if (status == ResponsesKeys.INVALID_AGE) {
      return "Your KYC is rejected, as you are not 18 yrs old";
    } else {
      return "Server Error";
    }
  }

  String whatAddressTypeLoc(
    AddressProofType type,
  ) {
    if (type == AddressProofType.passport) {
      return "kyc_screen.passport_no".tr();
    } else if (type == AddressProofType.aadhaar) {
      return "kyc_screen.aadhaar_no".tr();
    } else if (type == AddressProofType.voterId) {
      return "kyc_screen.voter_no".tr();
    } else if (type == AddressProofType.drivingLicense) {
      return "kyc_screen.dl_no".tr();
    } else {
      return "kyc_screen.address_proof_no".tr();
    }
  }

  String whatAddressTypeString(
    AddressProofType type,
  ) {
    if (type == AddressProofType.passport) {
      return StringConstants.passport;
    } else if (type == AddressProofType.aadhaar) {
      return StringConstants.aadhaar;
    } else if (type == AddressProofType.voterId) {
      return StringConstants.voterId;
    } else if (type == AddressProofType.drivingLicense) {
      return StringConstants.drivingLicense;
    } else {
      return StringConstants.select;
    }
  }

  AddressProofType whatAddressTypeEnum(
    String type,
  ) {
    if (type == StringConstants.passport) {
      return AddressProofType.passport;
    } else if (type == StringConstants.aadhaar) {
      return AddressProofType.aadhaar;
    } else if (type == StringConstants.voterId) {
      return AddressProofType.voterId;
    } else if (type == StringConstants.drivingLicense) {
      return AddressProofType.drivingLicense;
    } else {
      return AddressProofType.select;
    }
  }
}
