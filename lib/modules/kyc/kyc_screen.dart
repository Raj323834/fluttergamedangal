import 'dart:convert';
import 'dart:io';
import 'dart:math';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_image_compress/flutter_image_compress.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart' as p;
import 'package:path_provider/path_provider.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Model/logging_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/kyc_service.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/custom_picker_field.dart';
import '../../common_widgets/custom_text_form_field.dart';
import '../../common_widgets/dash_line.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/app_constants.dart';
import '../../constants/app_dimens.dart';
import '../../constants/color_constants.dart';
import '../../constants/enum.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../profile/user_profile_screen.dart';
import 'kyc_validators.dart';
import 'models/kyc_document_dm.dart';
import 'repos/fetch_kyc_documents_repo.dart';
import 'widgets/blocked_user_dialog_box.dart';
import 'widgets/choose_file.dart';
import 'widgets/document_status.dart';
import 'widgets/image_from_picker.dart';
import 'widgets/label_and_action_row.dart';
import 'widgets/profile_dialog_box.dart';

class KycScreen extends StatefulWidget {
  KycScreen({
    @required this.userId,
  });

  final String userId;

  @override
  _KycScreenState createState() => _KycScreenState();
}

class _KycScreenState extends State<KycScreen> {
  final _kycValidators = KycValidators();
  final _panController = TextEditingController();
  final _addressProofController = TextEditingController();
  final picker = ImagePicker();

  AddressProofType addressType = AddressProofType.select;
  String fileSizeMax = StringConstants.fileSizeMax,
      documentTypeID = StringConstants.emptyString,
      documentStatusAdd = StringConstants.na,
      documentAddReason = StringConstants.na,
      documentStatusPan = StringConstants.na,
      documentPanReason = StringConstants.na,
      addProofFrontExtension = StringConstants.emptyString,
      addProofBackExtension = StringConstants.emptyString,
      panExtension = StringConstants.emptyString,
      allowedDoc = StringConstants.emptyString,
      firstName = StringConstants.emptyString;
  File _frontImage, _backImage, _panCard;
  String frontImageUrl = StringConstants.emptyString,
      backImageUrl = StringConstants.emptyString,
      panImageUrl = StringConstants.emptyString;
  bool alreadyUploaded = false,
      shouldSubmit = true,
      addressTypeChanged = false,
      isFrontImage = false,
      isBackImage = false,
      isFetchedConfig = false;
  List finalFrontImg, finalBackImg, finalPanImg;
  List<String> addressProofType = [StringConstants.select],
      allowedTypes = [StringConstants.jpg];
  int maxLength = 1, initialItem = 0, minLength = 1;
  String rejectedReasonAdd = StringConstants.emptyString,
      rejectedReasonPan = StringConstants.emptyString;
  var _spaceH = SizedBox(
    height: 1.5.h,
  );

  int selectedProofIndex = 0;

  @override
  void initState() {
    asyncMethod(
      userId: widget.userId,
    );
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: KYC_ROUTE,
    );
    super.initState();
  }

  Future<void> asyncMethod({
    @required String userId,
  }) async {
    firstName = await SharedPrefService.getStringValuesFromSharedPref(
        SharedPrefKeys.firstName);
    if (firstName == StringConstants.emptyString || firstName == null) {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          //complete profile dialog box
          return ProfileDialogBox(
            onTap: () {
              Navigator.push(
                context,
                CupertinoPageRoute(
                  builder: (context) => UserProfileScreen(
                    fromDialog: true,
                    userId: userId,
                  ),
                ),
              );
            },
          );
        },
      );
    }
    await fetchDocumentUser(
      context: context,
      userId: userId,
      firstName: firstName,
    );
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: ColorConstants.kBackgroundColor,
        appBar: PreferredSize(
          preferredSize: Size(
            double.infinity,
            100,
          ),
          child: CustomAppBar(
            from: StringConstants.kyc,
            userId: widget.userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.symmetric(
              vertical: 2.0.h,
              horizontal: 14,
            ),
            child: Center(
              child: Column(
                children: [
                  LabelHeader(
                    label: "kyc_screen.kyc".tr(),
                  ),
                  SizedBox(
                    height: 2.0.h,
                  ),
                  isFetchedConfig
                      ? StreamBuilder(
                          stream: sockets.streamController.stream,
                          builder: (context, snapshot) {
                            try {
                              if (snapshot.hasData) {
                                var snapBody = jsonDecode(snapshot.data);
                                if (snapBody['type'] ==
                                    WebSocketTopics.kycStatus) {
                                  documentStatusAdd =
                                      snapBody['addressResult'] ??
                                          StringConstants.na;
                                  documentStatusPan = snapBody['idResult'] ??
                                      StringConstants.na;
                                  if (documentStatusAdd ==
                                          ResponsesKeys.REJECTED &&
                                      documentStatusPan ==
                                          ResponsesKeys.REJECTED) {
                                    rejectedReasonAdd = snapBody['reason'];
                                    rejectedReasonPan = snapBody['reason'];
                                  }
                                  if (documentStatusAdd ==
                                          ResponsesKeys.REJECTED &&
                                      snapBody['documentType'] !=
                                          StringConstants.pan) {
                                    rejectedReasonAdd = snapBody['reason'];
                                  }
                                  if (documentStatusPan ==
                                          ResponsesKeys.REJECTED &&
                                      snapBody['documentType'] ==
                                          StringConstants.pan) {
                                    rejectedReasonPan = snapBody['reason'];
                                  }
                                  documentAddReason = documentStatusAdd ==
                                          ResponsesKeys.REJECTED
                                      ? snapBody['documentType'] !=
                                              StringConstants.pan
                                          ? snapBody['reason']
                                          : rejectedReasonCheck(
                                              documentStatusAdd,
                                              StringConstants.add)
                                      : _kycValidators.reasonCheck(
                                          documentStatusAdd,
                                          fileSizeMax: fileSizeMax,
                                        );
                                  documentPanReason = documentPanReason ==
                                          ResponsesKeys.REJECTED
                                      ? snapBody['documentType'] ==
                                              StringConstants.pan
                                          ? snapBody['reason']
                                          : rejectedReasonCheck(
                                              documentPanReason,
                                              StringConstants.pan)
                                      : _kycValidators.reasonCheck(
                                          documentPanReason,
                                          fileSizeMax: fileSizeMax,
                                        );
                                }
                              }
                            } catch (e) {}
                            return Column(
                              children: [
                                SizedBox(
                                  height: 2.7.h,
                                ),
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: "kyc_screen.address_proof_type".tr(),
                                  actionRow: CustomPickerField(
                                    selectedName: _kycValidators
                                        .whatAddressTypeString(addressType),
                                    onTap: () async {
                                      FocusScope.of(context).unfocus();
                                      if (documentStatusAdd ==
                                          StringConstants.approved) {
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants
                                              .addressProofAlreadyApproved,
                                        );
                                      } else {
                                        String initialItem = _kycValidators
                                            .whatAddressTypeString(addressType);
                                        selectedProofIndex =
                                            addressProofType.indexOf(
                                          initialItem,
                                        );
                                        CommonMethods
                                            .showCustomPickerBottomSheet(
                                                context: context,
                                                title:
                                                    "kyc_screen.address_proof_type"
                                                        .tr(),
                                                initialItem: initialItem,
                                                list: addressProofType,
                                                onTap: () =>
                                                    onTapAddressProofChanged(
                                                      selectedProofIndex,
                                                      addressType,
                                                    ),
                                                onSelectedItemChanged: (
                                                  index,
                                                ) {
                                                  selectedProofIndex = index;
                                                }).whenComplete(
                                          () {
                                            if (addressTypeChanged) {
                                              _addressProofController.text =
                                                  StringConstants.emptyString;
                                              setState(
                                                () {
                                                  isBackImage = false;
                                                  isFrontImage = false;
                                                  _frontImage = null;
                                                  _backImage = null;
                                                  frontImageUrl =
                                                      StringConstants
                                                          .emptyString;
                                                  backImageUrl = StringConstants
                                                      .emptyString;
                                                },
                                              );
                                            }
                                          },
                                        );
                                      }
                                    },
                                  ),
                                ),
                                _spaceH,
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: _kycValidators.whatAddressTypeLoc(
                                    addressType,
                                  ),
                                  actionRow: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      SizedBox(
                                        height: AppDimens.textFieldH,
                                        child: CustomTextFormField(
                                          regExp: addressType ==
                                                  AddressProofType
                                                      .drivingLicense
                                              ? RegExpMethods.dl
                                              : RegExpMethods.alphaNumeric,
                                          readOnly: (addressType ==
                                                          AddressProofType
                                                              .select &&
                                                      _addressProofController
                                                              .text ==
                                                          StringConstants
                                                              .emptyString) ||
                                                  (documentStatusAdd ==
                                                          StringConstants
                                                              .approvedCapCamel ||
                                                      documentStatusAdd ==
                                                          StringConstants
                                                              .approved)
                                              ? true
                                              : false,
                                          onChanged: (value) {
                                            if (addressType ==
                                                AddressProofType.select) {
                                              FocusScope.of(context).unfocus();
                                            }
                                          },
                                          maxLength: maxLength,
                                          controller: _addressProofController,
                                          keyboardType: addressType ==
                                                  AddressProofType.aadhaar
                                              ? TextInputType.number
                                              : null,
                                        ),
                                      ),
                                      Visibility(
                                        visible: addressType ==
                                            AddressProofType.passport,
                                        child: Padding(
                                          padding: EdgeInsets.only(
                                            left: 0.8.h,
                                            top: 0.3.h,
                                          ),
                                          child: Text(
                                            StringConstants
                                                .findNumAtBackPassport,
                                            textAlign: TextAlign.start,
                                            style: TextStyle(
                                              fontSize: 6.0.sp,
                                              fontWeight: FontWeight.w500,
                                              color: ColorConstants.white
                                                  .withOpacity(0.5),
                                            ),
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                _spaceH,
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: "kyc_screen.Address_proof_fimg".tr(),
                                  actionRow: ChooseFile(
                                    onTap: () {
                                      if (documentStatusAdd !=
                                          StringConstants.approved) {
                                        _showPicker(
                                          context,
                                          StringConstants.front,
                                        );
                                      } else {
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants.docAlreadyApproved,
                                        );
                                      }
                                    },
                                    file: _frontImage,
                                    url: frontImageUrl,
                                    allowedDoc: allowedDoc,
                                    fileSizeMax: fileSizeMax,
                                  ),
                                ),
                                _spaceH,
                                Visibility(
                                  visible: addressType !=
                                      AddressProofType.drivingLicense,
                                  child: LabelAndActionRow(
                                    labelMaxLines: 2,
                                    label: "kyc_screen.Address_proof_bimg".tr(),
                                    actionRow: ChooseFile(
                                      onTap: () {
                                        if (documentStatusAdd !=
                                            StringConstants.approved) {
                                          _showPicker(
                                            context,
                                            StringConstants.back,
                                          );
                                        } else {
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants.docAlreadyApproved,
                                          );
                                        }
                                      },
                                      file: _backImage,
                                      url: backImageUrl,
                                      allowedDoc: allowedDoc,
                                      fileSizeMax: fileSizeMax,
                                    ),
                                  ),
                                ),
                                SizedBox(
                                  height: 2.4.h,
                                ),
                                LabelAndActionRow(
                                  label: StringConstants.emptyString,
                                  actionRow: DocumentStatus(
                                    onTap: () {
                                      setState(
                                        () {
                                          if (documentStatusAdd ==
                                              ResponsesKeys.REJECTED) {
                                            documentAddReason =
                                                rejectedReasonCheck(
                                              documentStatusAdd,
                                              StringConstants.add,
                                            );
                                          } else {
                                            documentAddReason =
                                                _kycValidators.reasonCheck(
                                              documentStatusAdd,
                                              fileSizeMax: fileSizeMax,
                                            );
                                          }
                                        },
                                      );
                                    },
                                    reason: documentAddReason,
                                    statusText: snapshot.hasData &&
                                            jsonDecode(snapshot.data)['type'] ==
                                                WebSocketTopics.kycStatus
                                        ? _kycValidators.statusCheck(
                                              jsonDecode(snapshot.data)[
                                                  'addressResult'],
                                              fileSizeMax: fileSizeMax,
                                            ) ??
                                            StringConstants.na
                                        : _kycValidators.statusCheck(
                                            documentStatusAdd,
                                            fileSizeMax: fileSizeMax,
                                          ),
                                    status: snapshot.hasData &&
                                            jsonDecode(snapshot.data)['type'] ==
                                                WebSocketTopics.kycStatus
                                        ? _kycValidators.statusCheck(
                                              jsonDecode(snapshot.data)[
                                                  'addressResult'],
                                              fileSizeMax: fileSizeMax,
                                            ) ??
                                            StringConstants.na
                                        : _kycValidators.statusCheck(
                                            documentStatusAdd,
                                            fileSizeMax: fileSizeMax,
                                          ),
                                    isInfo: snapshot.hasData &&
                                            jsonDecode(snapshot.data)['type'] ==
                                                WebSocketTopics.kycStatus
                                        ? _kycValidators.reasonCheck(
                                                jsonDecode(snapshot.data)[
                                                    'addressResult'],
                                                fileSizeMax: fileSizeMax) !=
                                            StringConstants.na
                                        : documentAddReason !=
                                            StringConstants.na,
                                  ),
                                ),
                                SizedBox(
                                  height: 2.7.h,
                                ),
                                LabelAndActionRow(
                                  label: StringConstants.emptyString,
                                  actionRow: DashLine(
                                    color:
                                        ColorConstants.white.withOpacity(0.4),
                                  ),
                                ),
                                SizedBox(
                                  height: 2.7.h,
                                ),
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: "kyc_screen.pan_no".tr(),
                                  actionRow: SizedBox(
                                    height: AppDimens.textFieldH,
                                    child: CustomTextFormField(
                                      regExp: RegExpMethods.alphaNumeric,
                                      readOnly: documentStatusPan ==
                                              StringConstants
                                                  .approvedCapCamel ||
                                          documentStatusPan ==
                                              StringConstants.approved,
                                      onChanged: (value) {},
                                      maxLength: 10,
                                      controller: _panController,
                                    ),
                                  ),
                                ),
                                _spaceH,
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: "kyc_screen.pan_fimg".tr(),
                                  actionRow: ChooseFile(
                                    onTap: () {
                                      if (documentStatusPan !=
                                          StringConstants.approved) {
                                        _showPicker(
                                          context,
                                          StringConstants.pan,
                                        );
                                      } else {
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants.docAlreadyApproved,
                                        );
                                      }
                                    },
                                    file: _panCard,
                                    url: panImageUrl,
                                    allowedDoc: allowedDoc,
                                    fileSizeMax: fileSizeMax,
                                  ),
                                ),
                                SizedBox(
                                  height: 2.4.h,
                                ),
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  // crossAxisAlignment: CrossAxisAlignment.start,
                                  label: StringConstants.emptyString,
                                  actionRow: DocumentStatus(
                                    onTap: () {
                                      setState(
                                        () {
                                          if (documentStatusPan ==
                                              ResponsesKeys.REJECTED) {
                                            documentPanReason =
                                                rejectedReasonCheck(
                                                    documentStatusPan,
                                                    StringConstants.pan);
                                          } else {
                                            documentPanReason = _kycValidators
                                                .reasonCheck(documentStatusPan,
                                                    fileSizeMax: fileSizeMax);
                                          }
                                        },
                                      );
                                    },
                                    reason: documentPanReason,
                                    status: documentStatusPan,
                                    statusText: snapshot.hasData &&
                                            jsonDecode(snapshot.data)['type'] ==
                                                WebSocketTopics.kycStatus
                                        ? _kycValidators.statusCheck(
                                                jsonDecode(
                                                    snapshot.data)['idResult'],
                                                fileSizeMax: fileSizeMax) ??
                                            StringConstants.na
                                        : _kycValidators.statusCheck(
                                            documentStatusPan,
                                            fileSizeMax: fileSizeMax),
                                    isInfo: snapshot.hasData &&
                                            jsonDecode(snapshot.data)['type'] ==
                                                WebSocketTopics.kycStatus
                                        ? _kycValidators.reasonCheck(
                                                jsonDecode(
                                                    snapshot.data)['idResult'],
                                                fileSizeMax: fileSizeMax) !=
                                            StringConstants.na
                                        : documentPanReason !=
                                            StringConstants.na,
                                  ),
                                ),
                                SizedBox(
                                  height: 2.7.h,
                                ),
                                LabelAndActionRow(
                                  labelMaxLines: 2,
                                  label: StringConstants.emptyString,
                                  actionRow: DashLine(
                                    color:
                                        ColorConstants.white.withOpacity(0.4),
                                  ),
                                ),
                                SizedBox(
                                  height: 4.0.h,
                                ),
                                // Submit button
                                CustomButton(
                                  width: 47.0.w,
                                  height: AppDimens.buttonH,
                                  buttonText: "kyc_screen.submit".tr(),
                                  isEnabled: !(documentStatusAdd ==
                                          ResponsesKeys.MANUAL_APPROVAL ||
                                      documentStatusPan ==
                                          ResponsesKeys.MANUAL_APPROVAL),
                                  onTap: () async {
                                    setState(
                                      () {
                                        shouldSubmit = true;
                                      },
                                    );
                                    if (documentStatusAdd ==
                                            StringConstants.approved &&
                                        documentStatusPan ==
                                            StringConstants.approved) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.kycAlreadyVerified,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (documentStatusAdd ==
                                            ResponsesKeys.INITIATED ||
                                        documentStatusPan ==
                                            ResponsesKeys.INITIATED) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.kycInProgress,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (documentStatusAdd ==
                                            ResponsesKeys.MANUAL_APPROVAL ||
                                        documentStatusPan ==
                                            ResponsesKeys.MANUAL_APPROVAL) {
                                      CommonMethods.showSnackBar(context,
                                          StringConstants.kycProcessedManually);
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (addressType ==
                                        AddressProofType.select) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.selectAddressProofType,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (_addressProofController
                                        .text.isEmpty) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.enterAddressNo,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (!isFrontImage) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.uploadFrontImage,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (!isBackImage &&
                                        addressType !=
                                            AddressProofType.drivingLicense) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.uploadBackImage,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else if (_panCard == null &&
                                        panImageUrl ==
                                            StringConstants.emptyString) {
                                      CommonMethods.showSnackBar(
                                        context,
                                        StringConstants.uploadPanImage,
                                      );
                                      setState(
                                        () {
                                          shouldSubmit = false;
                                        },
                                      );
                                    } else {
                                      if (!RegExpMethods.panNo
                                          .hasMatch(_panController.text)) {
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants.enterValidPanNo,
                                        );
                                        setState(
                                          () {
                                            shouldSubmit = false;
                                          },
                                        );
                                      }
                                      if (addressType ==
                                          AddressProofType.aadhaar) {
                                        if (!RegExpMethods.aadhaarNo.hasMatch(
                                            _addressProofController.text)) {
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants.enterValidAadhaarNo,
                                          );
                                          setState(
                                            () {
                                              shouldSubmit = false;
                                            },
                                          );
                                        }
                                      } else if (addressType ==
                                          AddressProofType.voterId) {
                                        if (!RegExpMethods.voterNo.hasMatch(
                                            _addressProofController.text)) {
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants.enterValidVoterNo,
                                          );
                                          setState(
                                            () {
                                              shouldSubmit = false;
                                            },
                                          );
                                        }
                                      } else if (addressType ==
                                          AddressProofType.drivingLicense) {
                                        if (_addressProofController
                                                .text.length <
                                            minLength) {
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.minLengthForDLMustBe} " +
                                                minLength.toString(),
                                          );
                                          setState(
                                            () {
                                              shouldSubmit = false;
                                            },
                                          );
                                        }
                                      }
                                    }
                                    if (shouldSubmit) {
                                      showDialog(
                                        context: context,
                                        barrierDismissible: false,
                                        builder: (BuildContext context) {
                                          return Center(
                                            child: CircularProgressIndicator(
                                              backgroundColor:
                                                  ColorConstants.blue,
                                            ),
                                          );
                                        },
                                      );
                                      // String user_id =
                                      //     await SharedPrefService
                                      //         .getStringValuesFromSharedPref(
                                      //             USER_ID);
                                      await LoggingModel.logging(
                                        StringConstants.beforeImgUpload,
                                        "${StringConstants.kycColon} " +
                                            widget.userId,
                                        DateTime.now().toString(),
                                        widget.userId,
                                      );
                                      Map<String, Object> result =
                                          await KycService.uploadDocument(
                                        documentStatusAdd,
                                        documentStatusPan,
                                        _kycValidators
                                            .whatAddressTypeString(addressType),
                                        _addressProofController.text,
                                        finalFrontImg,
                                        finalBackImg,
                                        addProofFrontExtension,
                                        _panController.text,
                                        finalPanImg,
                                        panExtension,
                                        frontImageUrl,
                                        backImageUrl,
                                        panImageUrl,
                                        widget.userId,
                                      );
                                      if (result.containsKey('noInternet')) {
                                        Navigator.pop(context);
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants.noInternetConnection,
                                        );
                                        FocusScope.of(context).unfocus();
                                      } else if (result.containsKey('error')) {
                                        await LoggingModel.logging(
                                          "Server error",
                                          "${StringConstants.kycColon} " +
                                              result.toString(),
                                          DateTime.now().toString(),
                                          widget.userId,
                                        );
                                        Navigator.pop(context);
                                        CommonMethods.showSnackBar(
                                          context,
                                          "${StringConstants.techIssue} : ${StringConstants.tryAfterSomeTime}",
                                        );
                                        FocusScope.of(context).unfocus();
                                      } else {
                                        Map<String, Object> responseMap =
                                            result['data'];
                                        if (responseMap.containsKey('error')) {
                                          await LoggingModel.logging(
                                            "Server error",
                                            "${StringConstants.kycColon} " +
                                                result.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue} : ${StringConstants.tryAfterSomeTime}",
                                          );
                                          FocusScope.of(context).unfocus();
                                        } else if (responseMap
                                                    .containsKey('result') &&
                                                responseMap['result'] ==
                                                    ResponsesKeys
                                                        .TOKEN_EXPIRED ||
                                            responseMap['result'] ==
                                                ResponsesKeys
                                                    .TOKEN_PARSING_FAILED) {
                                          await LoggingModel.logging(
                                            "TOKEN_EXPIRED-TOKEN_PARSING_FAILED",
                                            "Getting token expired and token parsing failed" +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                          bool accessTokenGenerated =
                                              await GenerateAccessToken
                                                  .regenerateAccessToken(
                                            widget.userId,
                                          );
                                          if (accessTokenGenerated) {
                                            await KycService.uploadDocument(
                                              documentStatusAdd,
                                              documentStatusPan,
                                              _kycValidators
                                                  .whatAddressTypeString(
                                                      addressType),
                                              _addressProofController.text,
                                              finalFrontImg,
                                              finalBackImg,
                                              addProofFrontExtension,
                                              _panController.text,
                                              finalPanImg,
                                              panExtension,
                                              frontImageUrl,
                                              backImageUrl,
                                              panImageUrl,
                                              widget.userId,
                                            );
                                          }
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.USER_NOT_FOUND) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue}.",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "USER_NOT_FOUND",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                                ResponsesKeys.SUCCESS ||
                                            responseMap['result'] ==
                                                StringConstants.approved) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants
                                                .docUploadedSuccessfully,
                                          );
                                          FocusScope.of(context).unfocus();
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.INVALID_DOCUMENT) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants.invalidDoc +
                                                ": " +
                                                responseMap[
                                                        'invalid_document_type']
                                                    .toString()
                                                    .replaceAll(
                                                      '_',
                                                      ' ',
                                                    ),
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "INVALID_DOCUMENT",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.UPS_NOT_REACHABLE) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue}. ${StringConstants.tryAgain}",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "UPS_NOT_REACHABLE",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.DOC_UPLOAD_FAILED) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            '${StringConstants.unableToUploadDoc} ${StringConstants.tryAfterSomeTime}',
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "DOC_UPLOAD_FAILED",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys
                                                .CENTRAL_DB_API_FAILURE) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue}. ${StringConstants.tryAfterSomeTime}.",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "CENTRAL_DB_API_FAILURE",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys
                                                .IMAGE_SIZE_LIMIT_REACHED) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.uploadFileLesserThan} " +
                                                (int.parse(fileSizeMax) / 1024)
                                                    .toString() +
                                                "${StringConstants.mbDot}",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "IMAGE_SIZE_LIMIT_REACHED",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.OCR_API_FAILURE) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue}. ${StringConstants.tryAgain}",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "OCR_API_FAILURE",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys
                                                .IMAGE_DOWNLOAD_FAILED) {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.techIssue}. ${StringConstants.tryAgain}",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "IMAGE_DOWNLOAD_FAILED",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.INITIATED) {
                                          Navigator.pop(context);
                                          FocusScope.of(context).unfocus();
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants
                                                .kycApprovalInProgress,
                                          );
                                          setState(
                                            () {
                                              if (documentStatusAdd ==
                                                  StringConstants.approved) {
                                                documentStatusPan =
                                                    ResponsesKeys.INITIATED;
                                                documentPanReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusPan,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                              } else if (documentStatusPan ==
                                                  StringConstants.approved) {
                                                documentStatusAdd =
                                                    ResponsesKeys.INITIATED;
                                                documentAddReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusAdd,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                              } else {
                                                documentStatusAdd =
                                                    ResponsesKeys.INITIATED;
                                                documentAddReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusAdd,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                                documentStatusPan =
                                                    ResponsesKeys.INITIATED;
                                                documentPanReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusPan,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                              }
                                            },
                                          );
                                          CommonMethods.showSnackBar(
                                            context,
                                            StringConstants
                                                .kycApprovalInProgress,
                                          );
                                          FocusScope.of(context).unfocus();
                                          CleverTapPlugin.recordEvent(
                                            StringConstants.kycUpload,
                                            {
                                              StringConstants
                                                  .kycUnderScoreStatus: true,
                                            },
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.DUPLICATE_DOCUMENT) {
                                          Navigator.pop(context);
                                          if (responseMap[
                                              'address_duplicate']) {
                                            setState(
                                              () {
                                                documentStatusAdd =
                                                    ResponsesKeys
                                                        .DUPLICATE_DOCUMENT;
                                                documentAddReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusAdd,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                              },
                                            );
                                          }
                                          if (responseMap['id_duplicate']) {
                                            setState(
                                              () {
                                                documentStatusPan =
                                                    ResponsesKeys
                                                        .DUPLICATE_DOCUMENT;
                                                documentPanReason =
                                                    _kycValidators.reasonCheck(
                                                  documentStatusPan,
                                                  fileSizeMax: fileSizeMax,
                                                );
                                              },
                                            );
                                          }
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "DUPLICATE_DOCUMENT",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else if (responseMap['result'] ==
                                            ResponsesKeys.INVALID_AGE) {
                                          Navigator.pop(context);
                                          setState(
                                            () {
                                              documentStatusAdd =
                                                  ResponsesKeys.INVALID_AGE;
                                              documentAddReason =
                                                  _kycValidators.reasonCheck(
                                                documentStatusAdd,
                                                fileSizeMax: fileSizeMax,
                                              );
                                              documentStatusPan =
                                                  ResponsesKeys.INVALID_AGE;
                                              documentPanReason =
                                                  _kycValidators.reasonCheck(
                                                documentStatusPan,
                                                fileSizeMax: fileSizeMax,
                                              );
                                            },
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "INVALID_AGE",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        } else {
                                          Navigator.pop(context);
                                          CommonMethods.showSnackBar(
                                            context,
                                            "${StringConstants.someProbInServer} ${StringConstants.tryAfterSomeTime}",
                                          );
                                          FocusScope.of(context).unfocus();
                                          await LoggingModel.logging(
                                            "NOT_SUCCESS",
                                            "${StringConstants.kycColon} " +
                                                responseMap.toString(),
                                            DateTime.now().toString(),
                                            widget.userId,
                                          );
                                        }
                                      }
                                    }
                                  },
                                ),
                              ],
                            );
                          },
                        )
                      : CircularProgressIndicator(),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  //METHODS
  Future fetchDocumentUser({
    @required BuildContext context,
    @required String firstName,
    @required String userId,
  }) async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
      FocusScope.of(context).unfocus();
    } else {
      var repoObj = FetchKycDocumentsRepo();
      KycDocumentDM kycDocumentDM = await repoObj.fetchKycDocuments(
        userId: userId,
      );
      if (kycDocumentDM != null) {
        switch (kycDocumentDM.result) {
          case ResponseStatus.success:
            initialisingTheDocuments(
              kycDocumentDM: kycDocumentDM,
            );
            configuringTheDocuments(
              config: kycDocumentDM.config,
            );
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              StringConstants.unableToFetchDoc,
            );
            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await fetchDocumentUser(
                context: context,
                firstName: firstName,
                userId: userId,
              );
            }
            break;

          case ResponseStatus.userDoesNotExist:
            configuringTheDocuments(
              config: kycDocumentDM.config,
            );
            break;

          case ResponseStatus.restrictedActivity:
            CommonMethods.showSnackBar(
              context,
              StringConstants.restrictedActivityMessage,
            );
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return BlockedUserDialogBox(
                  onTap: () {
                    if (firstName == null ||
                        firstName == StringConstants.emptyString) {
                      Navigator.pop(context);
                    }
                  },
                );
              },
            );
            configuringTheDocuments(
              config: kycDocumentDM.config,
            );
            break;

          default:
        }
      } else {
        CommonMethods.showSnackBar(
          context,
          StringConstants.unableToFetchDoc,
        );
      }
    }
  }

  void initialisingTheDocuments({
    @required KycDocumentDM kycDocumentDM,
  }) {
    frontImageUrl = kycDocumentDM.documents[AppConstants.addressDocumentAddress]
                .documentFrontUrl ==
            StringConstants.emptyString
        ? StringConstants.emptyString
        : kycDocumentDM
            .documents[AppConstants.addressDocumentAddress].documentFrontUrl;
    backImageUrl = kycDocumentDM.documents[AppConstants.addressDocumentAddress]
                .documentBackUrl ==
            StringConstants.emptyString
        ? StringConstants.emptyString
        : kycDocumentDM
            .documents[AppConstants.addressDocumentAddress].documentBackUrl;
    panImageUrl = kycDocumentDM
                .documents[AppConstants.panDocumentIndex].documentUrl ==
            StringConstants.emptyString
        ? StringConstants.emptyString
        : kycDocumentDM.documents[AppConstants.panDocumentIndex].documentUrl;
    if (kycDocumentDM.documents.length > 0) {
      setState(
        () {
          alreadyUploaded = true;
          _panController.text = kycDocumentDM
              .documents[AppConstants.panDocumentIndex].documentNumber;
          documentStatusAdd = kycDocumentDM
              .documents[AppConstants.addressDocumentAddress].status;
          documentAddReason = documentStatusAdd == ResponsesKeys.REJECTED
              ? kycDocumentDM
                  .documents[AppConstants.addressDocumentAddress].reason
              : _kycValidators.reasonCheck(
                  documentStatusAdd,
                  fileSizeMax: fileSizeMax,
                );
          rejectedReasonAdd = documentStatusAdd == ResponsesKeys.REJECTED
              ? kycDocumentDM
                  .documents[AppConstants.addressDocumentAddress].reason
              : StringConstants.emptyString;
          documentStatusPan =
              kycDocumentDM.documents[AppConstants.panDocumentIndex].status;
          documentPanReason = documentStatusPan == ResponsesKeys.REJECTED
              ? kycDocumentDM.documents[AppConstants.panDocumentIndex].reason
              : _kycValidators.reasonCheck(
                  documentStatusPan,
                  fileSizeMax: fileSizeMax,
                );
          rejectedReasonPan = documentStatusPan == ResponsesKeys.REJECTED
              ? kycDocumentDM.documents[AppConstants.panDocumentIndex].reason
              : StringConstants.emptyString;
          _addressProofController.text = kycDocumentDM
              .documents[AppConstants.addressDocumentAddress].documentNumber;
          addressType = _kycValidators.whatAddressTypeEnum(kycDocumentDM
              .documents[AppConstants.addressDocumentAddress].documentName);
          frontImageUrl = frontImageUrl;
          backImageUrl = backImageUrl;
          panImageUrl = panImageUrl;
          isFrontImage = frontImageUrl != StringConstants.emptyString;
          isBackImage = backImageUrl != StringConstants.emptyString;
          if (addressType == AddressProofType.aadhaar) {
            maxLength = AppConstants.maxLengthAadhaar;
          } else if (addressType == AddressProofType.voterId) {
            maxLength = AppConstants.maxLengthVoterId;
          } else if (addressType == AddressProofType.passport) {
            maxLength = AppConstants.maxLengthPassport;
          } else if (addressType == AddressProofType.drivingLicense) {
            maxLength = AppConstants.maxLengthDrivingLicense;
            minLength = AppConstants.minLengthDrivingLicense;
          }
        },
      );
      CleverTapPlugin.profileSet(
        {
          StringConstants.kycStatus:
              documentStatusAdd == StringConstants.approved &&
                  documentStatusPan == StringConstants.approved
        },
      );
    }
  }

  void configuringTheDocuments({
    @required Config config,
  }) {
    setState(
      () {
        fileSizeMax = config.filesize.max.toString();
        for (var addressItem in config.documentTypes.address) {
          addressProofType.add(
            addressItem,
          );
        }
        documentTypeID = config.documentTypes.id[AppConstants.configId];
        for (var allowedExtensions in config.allowedTypes) {
          allowedTypes.add(
            allowedExtensions,
          );
          allowedDoc += "$allowedExtensions, ";
        }
        allowedDoc = allowedDoc;
        isFetchedConfig = true;
      },
    );
  }

  Future<File> urlToFile(
    String imageUrl,
    String document,
  ) async {
    var rng = Random();
    Directory tempDir = await getTemporaryDirectory();
    String tempPath = tempDir.path;
    File file = File(
      '$tempPath' + rng.nextInt(100).toString() + '.png',
    );
    String fileName = file.path.split('/').last;
    String ext = fileName.split('.')[1];
    if (document == StringConstants.fI || document == StringConstants.bI) {
      setState(
        () {
          addProofFrontExtension = ext;
        },
      );
    } else {
      setState(
        () {
          panExtension = ext;
        },
      );
    }
    http.Response response = await http.get(
      Uri.parse(
        imageUrl,
      ),
    );
    await file.writeAsBytes(
      response.bodyBytes,
    );
    return file;
  }

  void _showPicker(
    context,
    fnb,
  ) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext bc) {
        //Image pick from camera or gallery
        return ImageFromPicker(
          onTapGallery: () {
            _imgUpload(
              ImageSource.gallery,
              fnb,
            );
            Navigator.of(context).pop();
          },
          onTapCamera: () {
            _imgUpload(
              ImageSource.camera,
              fnb,
            );
            Navigator.of(context).pop();
          },
        );
      },
    );
  }

  _imgUpload(
    ImageSource sourceImg,
    String fnb,
  ) async {
    var image = await picker.pickImage(
      source: sourceImg,
      imageQuality: 100,
    );
    // var image = await picker.getImage(source: sourceImg, imageQuality: 100);
    var img = File(
      image.path,
    );
    var newImg;
    if (img.readAsBytesSync().lengthInBytes / 1024 > 400) {
      newImg = await FlutterImageCompress.compressWithFile(
        image.path,
        quality: 20,
      );
      CommonMethods.printLog(
        StringConstants.emptyString,
        newImg.toString(),
      );
    } else {
      newImg = await FlutterImageCompress.compressWithFile(
        image.path,
        quality: 100,
      );
    }
    if (image != null) {
      if (fnb == StringConstants.front) {
        setState(
          () {
            _frontImage = img;
            isFrontImage = true;
            addProofFrontExtension = p
                .extension(
                  image.path.toString(),
                )
                .split(
                  StringConstants.dot,
                )[1];
            finalFrontImg = newImg;
            final bytes = _frontImage.readAsBytesSync().lengthInBytes;
            if (bytes > int.parse(fileSizeMax) * 1024) {
              CommonMethods.showSnackBar(
                context,
                "${StringConstants.uploadFileLesserThan} " +
                    (int.parse(fileSizeMax) / 1024).toString() +
                    " ${StringConstants.mbDot}",
              );
              _frontImage = null;
              isFrontImage = false;
            } else if (!allowedTypes.contains(addProofFrontExtension)) {
              CommonMethods.showSnackBar(
                context,
                "${StringConstants.fileMustBe} " + allowedDoc,
              );
              _frontImage = null;
              isFrontImage = false;
            } else {
              frontImageUrl = StringConstants.emptyString;
            }
          },
        );
      } else if (fnb == StringConstants.back) {
        setState(
          () {
            isBackImage = true;
            _backImage = img;
            addProofBackExtension = p
                .extension(
                  image.path.toString(),
                )
                .split(
                  StringConstants.dot,
                )[1];
            finalBackImg = newImg;
            final bytes = _backImage.readAsBytesSync().lengthInBytes;
            if (bytes > int.parse(fileSizeMax) * 1024) {
              CommonMethods.showSnackBar(
                context,
                "${StringConstants.uploadFileLesserThan} " +
                    (int.parse(fileSizeMax) / 1024).toStringAsFixed(3) +
                    " ${StringConstants.mbDot}",
              );
              _backImage = null;
              isBackImage = false;
            } else if (!allowedTypes.contains(
              addProofBackExtension,
            )) {
              CommonMethods.showSnackBar(
                context,
                "${StringConstants.fileMustBe} " + allowedDoc,
              );
              _backImage = null;
              isBackImage = false;
            } else {
              backImageUrl = StringConstants.emptyString;
            }
          },
        );
      } else {
        setState(
          () {
            _panCard = img;
            panExtension = p
                .extension(
                  image.path.toString(),
                )
                .split(
                  StringConstants.dot,
                )[1];
            finalPanImg = newImg;
            final bytes = _panCard.readAsBytesSync().lengthInBytes;
            if (bytes > int.parse(fileSizeMax) * 1024) {
              CommonMethods.showSnackBar(
                context,
                '${StringConstants.uploadFileLesserThan} ' +
                    (int.parse(fileSizeMax) / 1024).toStringAsFixed(3) +
                    " ${StringConstants.mbDot}",
              );
              _panCard = null;
            } else if (!allowedTypes.contains(panExtension)) {
              CommonMethods.showSnackBar(
                context,
                "${StringConstants.fileMustBe} " + allowedDoc,
              );
              _panCard = null;
            } else {
              panImageUrl = StringConstants.emptyString;
            }
          },
        );
      }
    } else {
      CommonMethods.showSnackBar(
        context,
        StringConstants.errorSelectingImage,
      );
    }
  }

  onTapAddressProofChanged(
    int index,
    AddressProofType type,
  ) {
    setState(
      () {
        addressType =
            _kycValidators.whatAddressTypeEnum(addressProofType[index]);
        if (type == addressType) {
          addressTypeChanged = false;
        } else if (type != addressType) {
          addressTypeChanged = true;
        }
        if (index == 0) {
          setState(
            () {
              maxLength = 1;
            },
          );
        } else if (index == 1) {
          setState(
            () {
              maxLength = 12;
            },
          );
        } else if (index == 2) {
          setState(
            () {
              maxLength = 10;
            },
          );
        } else if (index == 3) {
          setState(
            () {
              maxLength = 20;
            },
          );
        } else if (index == 4) {
          setState(
            () {
              maxLength = 20;
            },
          );
        }
      },
    );
    Navigator.pop(context);
  }

  String rejectedReasonCheck(
    String status,
    String type,
  ) {
    return type == StringConstants.add ? rejectedReasonAdd : rejectedReasonPan;
  }

  void dispose() {
    _panController.dispose();
    _addressProofController.dispose();
    super.dispose();
  }
}
