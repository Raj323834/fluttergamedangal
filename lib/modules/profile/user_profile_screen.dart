import 'dart:async';
import 'dart:ui';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/auth_service.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/user_profile_service.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/custom_picker_field.dart';
import '../../common_widgets/custom_text_form_field.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/app_dimens.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/reg_exp.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../utils/shared_pref_service.dart';
import '../kyc/kyc_screen.dart';
import '../kyc/widgets/label_and_action_row.dart';
import 'models/gender_item_model.dart';
import 'models/profile_info_dm.dart';
import 'repos/fetch_profile_info_repo.dart';
import 'widgets/text_field_suffix.dart';

class UserProfileScreen extends StatefulWidget {
  UserProfileScreen({
    this.fromDialog,
    this.userId,
  });
  final bool fromDialog;
  final String userId;

  @override
  _UserProfileScreenState createState() => _UserProfileScreenState();
}

class _UserProfileScreenState extends State<UserProfileScreen> {
  static const String TAG = "_UserProfile Screen ";
  String requestIdMobile = StringConstants.emptyString;
  String requestIdEmail = StringConstants.emptyString;

  bool _autoValidate = false;
  final _usernameController = TextEditingController();
  final _firstnameController = TextEditingController();
  final _middleNameController = TextEditingController();
  final _lastnameController = TextEditingController();
  final _mobileController = TextEditingController();
  final _emailController = TextEditingController();
  final _kycController = TextEditingController();
  final _otpController = TextEditingController();
  final _dobController = TextEditingController();
  final _add1Controller = TextEditingController();
  final _add2Controller = TextEditingController();
  final _cityController = TextEditingController();
  final _pinCodeController = TextEditingController();
  final _stateController = TextEditingController();
  final _dialogTextController = TextEditingController();

  final _formUserName = GlobalKey<FormState>();
  final _formFirstName = GlobalKey<FormState>();
  final _formMiddleName = GlobalKey<FormState>();
  final _formLastName = GlobalKey<FormState>();
  final _formMobile = GlobalKey<FormState>();
  final _formEmail = GlobalKey<FormState>();
  final _formDate = GlobalKey<FormState>();
  bool isFetchedConfig = false;
  int usernameUpdateCount;

  List<DropdownMenuItem<GenderItems>> _dropdownMenuItems;
  GenderItems _selectedItem;
  int selectedGenderIndex = 0;

  bool isMobileVerified =
      false; // set according to info received from get data from api
  bool _isMobileEmpty =
      false; // set according to info received from get data from api
  bool isEmailVerified = false;
  bool _isEmailEmpty = false;
  bool _isKYCVerified = false;
  bool _idVerified = false;
  bool _addressVerified = false;
  bool _isUsernameUpdateAllowed = true;
  bool _isDataVisible = true;
  bool isGenderMale = true;
  bool isGenderFemale = false;
  bool isGenderOther = false;
  bool isTimerMobileFinished = false;
  bool isTimerEmailFinished = false;
  bool showResendOTPMobile = false;
  bool showResendOTPEmail = false;
  bool allowEditing = false;
  var dob = StringConstants.emptyString;
  String gender = "Select";
  bool hasChanged = false;
  String initialFirst = StringConstants.emptyString,
      initialMiddle = StringConstants.emptyString,
      initialLast = StringConstants.emptyString,
      initialGender = "UNDEFINED",
      initialDOB = StringConstants.emptyString;
  bool dataReceived = false;
  int avatarId = 1;

  List<GenderItems> _dropdownItems = [
    GenderItems(
      1,
      "    Select",
      "UNDEFINED",
    ),
    GenderItems(
      2,
      "    Female",
      "FEMALE",
    ),
    GenderItems(
      3,
      "    Male",
      "MALE",
    ),
    GenderItems(
      4,
      "    Other",
      "OTHER",
    ),
  ];
  DateTime selectedDOB = DateTime.now();

  Timer _timer;
  int _start = 45;
  bool isTooManyMobileAttempt = false, isTooManyEmailAttempt = false;

  StreamController<int> _events;
  @override
  void initState() {
    _selectedItem = _dropdownItems[0];
    CommonMethods.printLog(
      "*************has_changed*****",
      "240  " + hasChanged.toString(),
    );
    if (widget.fromDialog) {
      _isDataVisible = true;
    }
    fetchAccount(
      userId: widget.userId,
      context: context,
    );
    asyncMethod();
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: PROFILE_ROUTE,
    );
    super.initState();
    _dropdownMenuItems = buildDropDownMenuItems(
      _dropdownItems,
    );
  }

  @override
  Widget build(BuildContext context) {
    SizedBox _spaceH = SizedBox(
      height: 0.8.h,
    );

    return SafeArea(
      child: Scaffold(
          backgroundColor: ColorConstants.kBackgroundColor,
          appBar: PreferredSize(
            preferredSize: Size(
              double.infinity,
              100,
            ),
            child: CustomAppBar(
              from: "userprofile",
              userId: widget.userId,
              bgColor: ColorConstants.appBarBgCol,
            ),
          ),
          body: SingleChildScrollView(
              child: Container(
            alignment: Alignment.center,
            padding: EdgeInsets.symmetric(
              vertical: 2.0.h,
              horizontal: 14,
            ),
            child: isFetchedConfig
                ? Column(
                    children: [
                      GestureDetector(
                        onTap: () {
                          setState(() {
                            _isDataVisible = _isDataVisible;
                          });
                        },
                        child: LabelHeader(
                          label: "profile_screen.profile".tr(),
                        ),
                      ),
                      SizedBox(
                        height: 3.5.h,
                      ),
                      //Profile Heading
                      Visibility(
                        visible: _isDataVisible,
                        child: Column(
                          children: [
                            LabelAndActionRow(
                              label: "profile_screen.username".tr(),
                              labelFontSize: 12.0.sp,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter Username',
                                  inputTextColor: ColorConstants.black,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formUserName,
                                  validator: null,
                                  controller: _usernameController,
                                  readOnly: true,
                                  suffix: TextFieldSuffix(
                                    isTick: !_isUsernameUpdateAllowed,
                                    isBtn: _isUsernameUpdateAllowed,
                                    btnText: "profile_screen.update".tr(),
                                    isInfo: false,
                                    onTapBtn: () {
                                      showDialog(
                                        context: context,
                                        builder: (BuildContext context) {
                                          return updateDataDialog(
                                              context,
                                              "USERNAME",
                                              "Enter Username",
                                              "Enter username to update",
                                              TextInputType.name,
                                              _usernameController,
                                              [
                                                FilteringTextInputFormatter
                                                    .allow(
                                                  RegExpMethods.alphaNumeric,
                                                ),
                                              ],
                                              _usernameController.text
                                                  .trim()
                                                  .toString(),
                                              25);
                                        },
                                      ).whenComplete(
                                        () => _dialogTextController.clear(),
                                      );
                                    },
                                  ),
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.mobile".tr(),
                              labelFontSize: 12.0.sp,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter Mobile Number',
                                  inputTextColor: ColorConstants.black,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formMobile,
                                  validator:
                                      UsernameServiceValidator.mobileValidator,
                                  controller: _mobileController,
                                  keyboardType: TextInputType.number,
                                  readOnly: true,
                                  suffix: TextFieldSuffix(
                                    isTick: !_isMobileEmpty,
                                    isBtn: _isMobileEmpty,
                                    btnText: _isMobileEmpty
                                        ? "profile_screen.add".tr()
                                        : "Edit",
                                    isInfo: !_isMobileEmpty,
                                    onTapBtn: () {
                                      showDialog(
                                              context: context,
                                              builder: (BuildContext context) {
                                                return updateDataDialog(
                                                    context,
                                                    "MOBILE_VERIFY",
                                                    "Enter Mobile Number",
                                                    "Enter the mobile number you want to update.",
                                                    TextInputType.phone,
                                                    _mobileController,
                                                    [
                                                      FilteringTextInputFormatter
                                                          .allow(
                                                        RegExpMethods.digits,
                                                      ),
                                                    ],
                                                    _mobileController.text
                                                        .trim()
                                                        .toString(),
                                                    10);
                                              })
                                          .whenComplete(() =>
                                              _dialogTextController.clear());
                                    },
                                    onTapInfo: () => CommonMethods.showSnackBar(
                                        context,
                                        "Please contact support to update the details"),
                                  ),
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.email".tr(),
                              labelFontSize: 12.0.sp,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter email address',
                                  inputTextColor: ColorConstants.black,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formEmail,
                                  validator:
                                      UsernameServiceValidator.emailValidator,
                                  controller: _emailController,
                                  readOnly: true,
                                  suffix: TextFieldSuffix(
                                    isTick: !_isEmailEmpty,
                                    isBtn: _isEmailEmpty,
                                    btnText: _isEmailEmpty
                                        ? "profile_screen.add".tr()
                                        : "Edit",
                                    isInfo: !_isMobileEmpty,
                                    onTapBtn: () {
                                      showDialog(
                                              context: context,
                                              builder: (BuildContext context) {
                                                return updateDataDialog(
                                                    context,
                                                    "EMAIL_VERIFY",
                                                    "Enter Email",
                                                    "Enter the email you want to update.",
                                                    TextInputType.emailAddress,
                                                    _emailController,
                                                    [],
                                                    _emailController.text
                                                        .trim()
                                                        .toString(),
                                                    90);
                                              })
                                          .whenComplete(() =>
                                              _dialogTextController.clear());
                                    },
                                    onTapInfo: () => CommonMethods.showSnackBar(
                                        context,
                                        "Please contact support to update the details"),
                                  ),
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.kyc".tr(),
                              labelFontSize: 12.0.sp,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField(
                                  hintText: _isKYCVerified
                                      ? "Verified"
                                      : "Not yet done",
                                  hintTextColor: ColorConstants.green,
                                  inputTextColor: ColorConstants.black,
                                  controller: null,
                                  readOnly: true,
                                  suffix: TextFieldSuffix(
                                    isBtn: !_isKYCVerified,
                                    btnText: "profile_screen.verify".tr(),
                                    onTapBtn: () {
                                      Navigator.pop(context);
                                      Navigator.push(
                                        context,
                                        CupertinoPageRoute(
                                            builder: (context) => KycScreen(
                                                userId: widget.userId)),
                                      );
                                    },
                                  ),
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.first_name".tr(),
                              subLabel: "profile_screen.as_per_pan".tr(),
                              labelFontSize: 12.0.sp,
                              isMandatory: true,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter First name',
                                  inputTextColor: ColorConstants.black,
                                  maxLength: 50,
                                  fillColor: true,
                                  textCapitalization: TextCapitalization.none,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formFirstName,
                                  validator:
                                      UsernameServiceValidator.nameValidator,
                                  controller: _firstnameController,
                                  readOnly: !allowEditing,
                                  regExp: RegExpMethods.alphabets,
                                  keyboardType: TextInputType.name,
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.middle_name".tr(),
                              subLabel: "profile_screen.as_per_pan".tr(),
                              labelFontSize: 12.0.sp,
                              isMandatory: false,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter Middle name',
                                  inputTextColor: ColorConstants.black,
                                  maxLength: 50,
                                  fillColor: true,
                                  textCapitalization: TextCapitalization.none,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formMiddleName,
                                  validator: UsernameServiceValidator
                                      .allowEmptyValidator,
                                  controller: _middleNameController,
                                  readOnly: !allowEditing,
                                  regExp: RegExpMethods.alphabets,
                                  keyboardType: TextInputType.name,
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.last_name".tr(),
                              subLabel: "profile_screen.as_per_pan".tr(),
                              labelFontSize: 12.0.sp,
                              isMandatory: true,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter Last name',
                                  inputTextColor: ColorConstants.black,
                                  maxLength: 50,
                                  fillColor: true,
                                  textCapitalization: TextCapitalization.none,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formLastName,
                                  validator:
                                      UsernameServiceValidator.nameValidator,
                                  controller: _lastnameController,
                                  readOnly: !allowEditing,
                                  regExp: RegExpMethods.alphabets,
                                  keyboardType: TextInputType.name,
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.date_of_birth".tr(),
                              subLabel: "profile_screen.as_per_pan".tr(),
                              labelFontSize: 12.0.sp,
                              isMandatory: true,
                              actionRow: SizedBox(
                                height: AppDimens.textFieldH,
                                child: CustomTextFormField.withForm(
                                  hintText: 'Enter Date of Birth',
                                  inputTextColor: ColorConstants.black,
                                  textCapitalization: TextCapitalization.none,
                                  isAutoValidate: _autoValidate,
                                  formKey: _formDate,
                                  validator: UsernameServiceValidator
                                      .allowEmptyValidator,
                                  controller: _dobController,
                                  fillColor: !allowEditing,
                                  readOnly: true,
                                  keyboardType: TextInputType.name,
                                  suffix: TextFieldSuffix(
                                    iconData: Icons.calendar_today_outlined,
                                    onTapIconBtn: () {
                                      FocusScope.of(context)
                                          .requestFocus(new FocusNode());
                                      if (allowEditing) {
                                        _selectDateOfBirth(context);
                                      } else {
                                        CommonMethods.showSnackBar(
                                          context,
                                          StringConstants
                                              .profileUpdationNotAllowed,
                                        );
                                      }
                                    },
                                  ),
                                ),
                              ),
                            ),
                            _spaceH,
                            LabelAndActionRow(
                              label: "profile_screen.gender".tr(),
                              subLabel: "profile_screen.as_per_pan".tr(),
                              labelFontSize: 12.0.sp,
                              isMandatory: true,
                              actionRow: CustomPickerField(
                                padding:
                                    EdgeInsets.only(left: 0.5.h, right: 1.5.h),
                                border: Border.all(
                                    color: ColorConstants.textFieldBorderCol,
                                    width: 0.15.h),
                                gradient: allowEditing
                                    ? ColorConstants.whiteWhiteGradient
                                    : ColorConstants.customLinearGradient(),
                                selectedName: _selectedItem.gender,
                                onTap: () async {
                                  if (allowEditing) {
                                    FocusScope.of(context)
                                        .requestFocus(FocusNode());
                                    CommonMethods.showCustomPickerBottomSheet(
                                        context: context,
                                        title: "profile_screen.gender".tr(),
                                        initialItem: _dropdownItems[
                                                _dropdownItems.indexOf(
                                          _selectedItem,
                                        )]
                                            .gender,
                                        list: _dropdownItems
                                            .map((GenderItems item) =>
                                                item.gender)
                                            .toList(),
                                        onTap: () {
                                          setState(
                                            () {
                                              _selectedItem = _dropdownItems[
                                                  selectedGenderIndex];
                                            },
                                          );
                                          Navigator.pop(context);
                                        },
                                        onSelectedItemChanged: (
                                          index,
                                        ) {
                                          selectedGenderIndex = index;
                                        }).whenComplete(
                                      () {
                                        FocusScope.of(context)
                                            .requestFocus(FocusNode());
                                      },
                                    );
                                  } else {
                                    CommonMethods.showSnackBar(
                                      context,
                                      StringConstants.profileUpdationNotAllowed,
                                    );
                                  }
                                },
                              ),
                            ),
                          ],
                        ),
                      ), //Username
                      SizedBox(
                        height: 4.0.h,
                      ),
                      CustomButton(
                        height: AppDimens.textFieldH,
                        width: 47.0.w,
                        buttonText: "profile_screen.submit".tr(),
                        isEnabled: allowEditing,
                        onTap: () async {
                          if (allowEditing) {
                            if (_firstnameController.text.trim() == "") {
                              CommonMethods.showSnackBar(
                                  context, "Enter First name");
                              cleverTapProfileUpdate(false, true);
                            } else if (isValidName(_firstnameController.text)) {
                              CommonMethods.showSnackBar(context,
                                  "Enter a valid First name. Name cannot have special characters");
                              cleverTapProfileUpdate(false, true);
                            } else if (isValidName(
                                _middleNameController.text)) {
                              CommonMethods.showSnackBar(context,
                                  "Enter a valid Middle name. Name cannot have special characters");
                              cleverTapProfileUpdate(false, true);
                            } else if (_lastnameController.text.trim() == "") {
                              CommonMethods.showSnackBar(
                                  context, "Enter Last name");
                              cleverTapProfileUpdate(false, true);
                            } else if (isValidName(_lastnameController.text)) {
                              CommonMethods.showSnackBar(context,
                                  "Enter a valid last name. Name cannot have special characters");
                              cleverTapProfileUpdate(false, true);
                            } else if (dob == "") {
                              CommonMethods.showSnackBar(
                                  context, "Select Date of Birth");
                              cleverTapProfileUpdate(false, true);
                            } else if ((_selectedItem.apiName == "UNDEFINED") ||
                                (_selectedItem.apiName == "undefined") ||
                                (_selectedItem.apiName == "Undefined")) {
                              CommonMethods.showSnackBar(
                                  context, "Select  Gender");
                              cleverTapProfileUpdate(false, true);
                            } else if ((initialFirst ==
                                    _firstnameController.text) &&
                                (initialMiddle == _middleNameController.text) &&
                                (initialLast == _lastnameController.text) &&
                                (initialGender.toLowerCase() ==
                                    _selectedItem.apiName.toLowerCase()) &&
                                (!hasChanged)) {
                              CommonMethods.showSnackBar(
                                  context, "Please update and click submit");
                              cleverTapProfileUpdate(false, true);
                            } else {
                              CommonMethods.printLog(
                                  "initial_first  ", initialFirst);
                              CommonMethods.printLog("final_first  ",
                                  _firstnameController.text.toString());
                              CommonMethods.printLog(
                                  "initial_middle  ", initialMiddle);
                              CommonMethods.printLog("final_middle  ",
                                  _middleNameController.text.toString());
                              CommonMethods.printLog(
                                  "initial_last  ", initialLast.toString());
                              CommonMethods.printLog("final_last  ",
                                  _lastnameController.text.trim().toString());
                              CommonMethods.printLog(
                                  "initial_dob  ", initialDOB.toString());
                              CommonMethods.printLog(
                                  "final_dob  ",
                                  selectedDOB.millisecondsSinceEpoch
                                      .toString());
                              CommonMethods.printLog(
                                  "initial_gender  ", initialGender.toString());
                              CommonMethods.printLog("final_gender  ",
                                  _selectedItem.apiName.toString());
                              _updateUserProfileDetails(
                                  _firstnameController.text.trim(),
                                  _middleNameController.text.trim(),
                                  _lastnameController.text.trim(),
                                  selectedDOB.millisecondsSinceEpoch,
                                  _mobileController.text.trim(),
                                  _selectedItem.apiName,
                                  widget.userId);
                            }
                          } else {
                            CommonMethods.showSnackBar(
                              context,
                              StringConstants.profileUpdationNotAllowed,
                            );
                            cleverTapProfileUpdate(
                              false,
                              true,
                            );
                          }
                        },
                      )
                    ],
                  )
                : CircularProgressIndicator(),
          ))),
    );
  }

  //METHODS
  void startTimer() {
    _start = 45;
    const oneSec = const Duration(
      seconds: 1,
    );
    _timer = new Timer.periodic(
      oneSec,
      (Timer timer) {
        if (_start == 0) {
          _events.add(0);
          setState(
            () {
              timer.cancel();
              isTimerMobileFinished = true;
              isTimerEmailFinished = true;
              isTooManyMobileAttempt = false;
              isTooManyEmailAttempt = false;
            },
          );
        } else {
          setState(
            () {
              _start = _start - 1;
              _events.add(
                _start,
              );
            },
          );
        }
      },
    );
  }

  Future fetchAccount({
    @required String userId,
    @required BuildContext context,
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
      var repoObj = FetchProfileInfoRepo();
      ProfileInfoDM profileInfoDM = await repoObj.fetchProfileInfo(
        userId: userId,
      );
      setState(
        () {
          isFetchedConfig = true;
        },
      );
      if (profileInfoDM != null) {
        switch (profileInfoDM.result) {
          case ResponseStatus.success:
            setState(
              () {
                dataReceived = true;
                _usernameController.text = profileInfoDM.userName;
                // responseMap['avatarId'];
                _firstnameController.text = profileInfoDM.firstName;

                _middleNameController.text = profileInfoDM.middleName;
                _lastnameController.text = profileInfoDM.lastName;
                // _dobController.text = responseMap['dob'];
                initialFirst = profileInfoDM.firstName;
                initialMiddle = profileInfoDM.middleName;
                initialLast = profileInfoDM.lastName;
                _add1Controller.text = profileInfoDM.address;
                _add2Controller.text = profileInfoDM.address2;
                _cityController.text = profileInfoDM.city;
                _pinCodeController.text = profileInfoDM.pin;
                _stateController.text = profileInfoDM.state;
                _emailController.text = profileInfoDM.email;
                _mobileController.text = profileInfoDM.mobile;
                isMobileVerified = profileInfoDM.mobileVerified;
                isEmailVerified = profileInfoDM.emailVerified;
                // _isKYCVerified = responseMap['kycVerified'];
                _idVerified = profileInfoDM.idVerified;
                _addressVerified = profileInfoDM.addressVerified;

                if (_idVerified || _addressVerified) {
                  allowEditing = false;
                } else {
                  allowEditing = true;
                }
                if (_idVerified && _addressVerified) {
                  _isKYCVerified = true;
                } else {
                  _isKYCVerified = false;
                }
                if (_mobileController.text == StringConstants.emptyString)
                  _isMobileEmpty = true;
                CommonMethods.printLog(
                  TAG,
                  " _isMobileEmpty  : " + _isMobileEmpty.toString(),
                );
                if (_emailController.text == StringConstants.emptyString)
                  _isEmailEmpty = true;
                usernameUpdateCount = profileInfoDM.usernameUpdateCount;
                if (usernameUpdateCount == 0) {
                  _isUsernameUpdateAllowed = true;
                  CommonMethods.printLog(
                    StringConstants.emptyString,
                    "Usernmae Count : " + usernameUpdateCount.toString(),
                  );
                } else {
                  _isUsernameUpdateAllowed = false;
                }
                if (profileInfoDM.dob == StringConstants.emptyString) {
                  _dobController.text = dob;
                  CommonMethods.printLog(
                    StringConstants.emptyString,
                    "responseMap['dob']  :" + profileInfoDM.dob,
                  );
                } else if ((profileInfoDM.dob != StringConstants.emptyString) ||
                    (profileInfoDM.dob != null)) {
                  // selectedDOB=responseMap['dob'];
                  CommonMethods.printLog(
                    StringConstants.emptyString,
                    "responseMap['dob']" + profileInfoDM.dob,
                  ); // 12345
                  var myInt = int.parse(
                    profileInfoDM.dob,
                  );
                  assert(myInt is int);

                  var date = DateTime.fromMillisecondsSinceEpoch(
                    myInt,
                  );
                  selectedDOB = date;
                  dob = "${date.day}/${date.month}/${date.year}";
                  _dobController.text = dob;
                  CommonMethods.printLog(
                    StringConstants.emptyString,
                    "dob  : " + dob.toString(),
                  );
                }

                gender = profileInfoDM.gender;
                initialGender = profileInfoDM.gender;
                initialDOB = profileInfoDM.dob;
                if ((gender == 'Male') ||
                    (gender == 'MALE') ||
                    (gender == 'male')) {
                  CommonMethods.printLog(
                    "USerPRofile",
                    "Gender : " + gender.toString(),
                  );
                  _selectedItem = _dropdownMenuItems[2].value;
                } else if ((gender == 'Female') ||
                    (gender == 'FEMALE') ||
                    (gender == 'female')) {
                  CommonMethods.printLog(
                    "USerPRofile",
                    "Gender : " + gender.toString(),
                  );
                  _selectedItem = _dropdownMenuItems[1].value;
                } else if ((gender == 'Other') ||
                    (gender == 'OTHER') ||
                    (gender == 'other')) {
                  CommonMethods.printLog(
                    "USerPRofile",
                    "Gender : " + gender.toString(),
                  );
                  _selectedItem = _dropdownMenuItems[3].value;
                } else if ((gender == 'undefined') ||
                    (gender == 'UNDEFINED') ||
                    (gender == 'Undefined')) {
                  CommonMethods.printLog(
                    "USerPRofile",
                    "Gender : " + gender.toString(),
                  );
                  _selectedItem = _dropdownMenuItems[0].value;
                }
              },
            );
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              "Database Error in loading data",
            );
            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await fetchAccount(
                userId: userId,
                context: context,
              );
            }
            break;

          default:
            CommonMethods.showCustomDialog(
              title: 'Oops!',
              error: "Error in loading the user info",
              context: context,
            );
        }
      } else {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Error in loading the user info",
          context: context,
        );
      }
    }
  }

  bool isValidName(
    String name,
  ) {
    final nameRegExp = RegExp(
      r'[!@#<>?":_`~;[\]\\|=+)(*&^%\s-]',
    );
    return nameRegExp.hasMatch(
      name,
    );
  }

  Future asyncMethod() async {
    avatarId = await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.avatarId,
        ) ??
        1;
    setState(
      () {
        avatarId = avatarId;
      },
    );
  }

  void eventsMethod() {
    if (_events != null) {
      _events.sink.close();
    }
    _events = StreamController<int>();
    _events.add(45);
  }

  int convertDateToTimeStamp(
    DateTime date,
  ) {
    DateTime dateofBirth = DateTime(
      date.year,
      date.month,
      date.day,
      12,
      0,
      0,
      0,
      0,
    ); //DateTime.
    CommonMethods.printLog(
      "dateofBirth",
      dateofBirth.toString(),
    );
    var myTimeStamp = dateofBirth.second; //To TimeStamp
    return myTimeStamp;
  }

  List<DropdownMenuItem<GenderItems>> buildDropDownMenuItems(
    List listItems,
  ) {
    List<DropdownMenuItem<GenderItems>> items = [];
    for (GenderItems listItem in listItems) {
      items.add(
        DropdownMenuItem(
          child: Text(
            listItem.gender,
          ),
          value: listItem,
        ),
      );
    }
    return items;
  }

  Future _selectDateOfBirth(
    BuildContext context,
  ) async {
    final today = DateTime.now();
    final age18Above = DateTime(today.year - 18, today.month, today.day);
    final DateTime picked = await showDatePicker(
      context: context,
      initialDate: age18Above,
      firstDate: DateTime(1900),
      lastDate: age18Above,
    );

    if (picked != null && picked != selectedDOB) {
      hasChanged = true;

      selectedDOB = picked;
      CommonMethods.printLog(
        TAG,
        convertDateToTimeStamp(selectedDOB).toString(),
      );
      var myInt = convertDateToTimeStamp(selectedDOB) * 1000;
      assert(myInt is int);

      // var date = DateTime.fromMillisecondsSinceEpoch(myInt * 1000);
      // var date = DateTime.fromMillisecondsSinceEpoch(myInt);

      setState(
        () {
          selectedDOB = picked;
          dob = "${selectedDOB.day}/${selectedDOB.month}/${selectedDOB.year}";
          _dobController.text = dob;
        },
      );
    } else {
      CommonMethods.printLog(
        TAG,
        convertDateToTimeStamp(selectedDOB).toString(),
      );
    }

    if (picked != null && picked != selectedDOB) {}
  }

  Widget updateDataDialog(
    BuildContext context,
    String from,
    String header,
    String description,
    TextInputType _keyboardType,
    TextEditingController _textcontroller,
    List<TextInputFormatter> inputformatters,
    String oldValue,
    int length,
  ) {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "updateDataDialog",
    );
    return Dialog(
      elevation: 40,
      backgroundColor: Colors.transparent,
      child: Container(
        height: 30.0.h,
        decoration: BoxDecoration(
          border: Border.all(
            color: ColorConstants.blue1,
          ),
          borderRadius: BorderRadius.circular(
            AppDimens.circularBorder,
          ),
          color: Colors.white,
        ),
        child: Column(
          children: [
            SizedBox(
              height: 2.1.h,
            ),
            Center(
              child: Container(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Center(
                    child: Text(
                      description,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 10.2.sp,
                        fontWeight: FontWeight.bold,
                        color: ColorConstants.blue1,
                      ),
                    ),
                  ),
                ),
              ),
            ),
            SizedBox(
              height: 2.1.h,
            ),
            Padding(
              padding: const EdgeInsets.only(
                left: 10.0,
                right: 10.0,
              ),
              child: Container(
                height: 8.0.h,
                child: TextFormField(
                  maxLength: length,
                  controller: _dialogTextController,
                  keyboardType: _keyboardType,
                  inputFormatters: inputformatters,
                  maxLines: 1,
                  decoration: InputDecoration(
                    counterText: StringConstants.emptyString,
                    contentPadding: EdgeInsets.only(
                      left: 10,
                      top: 10,
                      bottom: 10,
                    ),
                    isCollapsed: true,
                    border: OutlineInputBorder(),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.all(
                        Radius.circular(5.0),
                      ),
                    ),
                    labelStyle: TextStyle(
                      fontSize: 10.2.sp,
                      color: ColorConstants.dark1,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ),
            SizedBox(
              height: 2.1.h,
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: GestureDetector(
                onTap: () async {
                  String pattern = r'^(?:[+0][1-9])?[0-9]{10,12}$';
                  RegExp regExp = RegExp(pattern);
                  Pattern patternEmail =
                      r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
                  RegExp regexEmail = RegExp(patternEmail);
                  String userId =
                      await SharedPrefService.getStringValuesFromSharedPref(
                    SharedPrefKeys.userID,
                  );
                  if (from == 'USERNAME') {
                    if (_dialogTextController.text.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter username',
                          ),
                        ),
                      );
                    } else if (_dialogTextController.text == oldValue) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'You have entered the already existing Username',
                          ),
                        ),
                      );
                    } else {
                      _updateUsername(
                        userId,
                        _dialogTextController.text.trim(),
                        oldValue,
                      );
                    }
                  } else if (from == 'MOBILE_VERIFY') {
                    if (_dialogTextController.text.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter mobile number',
                          ),
                        ),
                      );
                    } else if (!regExp.hasMatch(
                      _dialogTextController.text.toString(),
                    )) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter a valid mobile number',
                          ),
                        ),
                      );
                    } else if (_dialogTextController.text.length > 10 ||
                        (_dialogTextController.text.length < 10)) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter a valid mobile number',
                          ),
                        ),
                      );
                    } else if (_dialogTextController.text == oldValue) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'You have entered the already existing mobile number',
                          ),
                        ),
                      );
                    } else {
                      startTimer();
                      eventsMethod();
                      setState(() {
                        isTimerMobileFinished = false;
                        isTooManyMobileAttempt = false;
                      });
                      requestForOTPMobile(
                          _dialogTextController.text.toString().trim(),
                          oldValue,
                          "VERIFY_MOBILE",
                          userId);
                    }
                  } else if (from == 'EMAIL_VERIFY') {
                    if (_dialogTextController.text.isEmpty) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter email id',
                          ),
                        ),
                      );
                    } else if (!regexEmail.hasMatch(
                      _dialogTextController.text.toString(),
                    )) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'Please enter a valid email id',
                          ),
                        ),
                      );
                    } else if (_dialogTextController.text == oldValue) {
                      _dialogTextController.clear();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(
                            'You have entered the already existing email',
                          ),
                        ),
                      );
                    } else {
                      startTimer();
                      eventsMethod();
                      setState(
                        () {
                          isTimerEmailFinished = false;
                          isTooManyEmailAttempt = false;
                        },
                      );
                      requestForOTPEmail(
                        _dialogTextController.text.toString().trim(),
                        oldValue,
                        userId,
                      );
                    }
                  }
                  Navigator.pop(context);
                  _dialogTextController.clear();
                },
                child: Container(
                  height: 4.5.h,
                  width: 27.0.w,
                  decoration: BoxDecoration(
                    gradient: ColorConstants.blueDarkGradient,
                    borderRadius: BorderRadius.circular(
                      AppDimens.circularBorder,
                    ),
                  ),
                  child: Center(
                    child: Text(
                      "profile_screen.submit".tr(),
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future _handleMobileVerification(
    String mobileNumber,
    String otp,
    String requestId,
    String oldMobileNumber,
    String userId,
  ) async {
    Map<String, Object> result = await UserProfileService.verifyMobile(
      mobileNumber,
      otp,
      requestId,
      oldMobileNumber,
      userId,
    );

    if (result.containsKey('noInternet')) {
      Navigator.pop(context);
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      _timer.cancel();
    } else if (result.containsKey('error')) {
      Navigator.pop(context);
      _timer.cancel();
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'].toString(),
      );
      setState(
        () {
          _mobileController.text = oldMobileNumber;
        },
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
      cleverTapVerificationStatus(
        "MOBILE",
        false,
        mobileNumber,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        Navigator.pop(context);
        _timer.cancel();
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await _handleMobileVerification(
            mobileNumber,
            otp,
            requestId,
            oldMobileNumber,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        Navigator.pop(context);
        _timer.cancel();
        _isMobileEmpty = false;
        // String status = data['status'];
        setState(() {
          isMobileVerified = true;
          FocusScope.of(context).unfocus();
          _mobileController.text = mobileNumber;
        });
        CommonMethods.showSnackBar(
            context, "Mobile number is successfully verified.");
        cleverTapVerificationStatus(
          "MOBILE",
          true,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_OTP) {
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Please enter a valid OTP.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_REQUEST_ID) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "User does not exist with provided user id in system.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.COMMUNICATION_NOT_REACHABLE) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again!",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.AUTH_NOT_REACHABLE) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again!",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.OTP_NOT_VERIFIED) {
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Please enter a valid OTP.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.TOO_MANY_ATTEMPTS) {
        CommonMethods.printLog(
          TAG,
          "old_mobile_number : " + oldMobileNumber.toString(),
        );
        setState(
          () {
            _mobileController.text = oldMobileNumber;
            isTooManyMobileAttempt = true;
          },
        );
        _timer.cancel();
        startTimer();
        CommonMethods.showSnackBar(
          context,
          "You have made too many attempts. Please wait for some time.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.OTP_NOT_GENERATED) {
        CommonMethods.printLog(
          TAG,
          "old_mobile_number : " + oldMobileNumber.toString(),
        );
        setState(
          () {
            _mobileController.text = oldMobileNumber;
            isTooManyMobileAttempt = true;
          },
        );
        _timer.cancel();
        CommonMethods.showSnackBar(
          context,
          "Your OTP has been expired. Please resend OTP.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.pop(context);
        _timer.cancel();
        CommonMethods.printLog(
          TAG,
          "old_mobile_number : " + oldMobileNumber.toString(),
        );
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else if (data['result'] == ResponsesKeys.REDIS_ERROR) {
        Navigator.pop(context);
        _timer.cancel();
        CommonMethods.printLog(
            TAG, "old_mobile_number : " + oldMobileNumber.toString());
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      } else {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _mobileController.text = oldMobileNumber;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again! ",
        );
        cleverTapVerificationStatus(
          "MOBILE",
          false,
          mobileNumber,
        );
      }
    }
  }

  Future _handleEmailVerification(
    String email,
    String otp,
    String requestId,
    String emailUpdate,
    String oldEmail,
    String userId,
  ) async {
    CommonMethods.printLog(
      TAG,
      "----------Email Verification----------",
    );
    Map<String, Object> result = await UserProfileService.verifyEmail(
      email,
      otp,
      requestId,
      emailUpdate,
      oldEmail,
      userId,
    );

    if (result.containsKey('noInternet')) {
      Navigator.pop(context);
      _timer.cancel();
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      Navigator.pop(context);
      _timer.cancel();
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'],
      );
      setState(
        () {
          _emailController.text = oldEmail;
        },
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again!",
      );
      cleverTapVerificationStatus(
        "EMAIL",
        false,
        email,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        Navigator.pop(context);
        _timer.cancel();
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await _handleEmailVerification(
            email,
            otp,
            requestId,
            emailUpdate,
            oldEmail,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        Navigator.pop(context);
        _timer.cancel();
        // String status = data['status'];
        _isEmailEmpty = false;
        setState(
          () {
            isEmailVerified = true;
            FocusScope.of(context).unfocus();
            _emailController.text = email;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Email verified successfully",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          true,
          email,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_OTP) {
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Please enter a valid OTP.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_REQUEST_ID) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.OTP_NOT_VERIFIED) {
        CommonMethods.printLog(TAG, "_emailController = old_email");
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Please enter a valid OTP.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "User not found",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.EMAIL_IN_USE) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Email already in use.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.COMMUNICATION_NOT_REACHABLE) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Please try again",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.AUTH_NOT_REACHABLE) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Please try again",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.TOO_MANY_ATTEMPTS) {
        CommonMethods.printLog(
          TAG,
          "old_email : " + oldEmail.toString(),
        );
        setState(
          () {
            _mobileController.text = oldEmail;
            isTooManyEmailAttempt = true;
          },
        );
        _timer.cancel();
        startTimer();
        CommonMethods.showSnackBar(
          context,
          "You have made too many attempts. Please wait for some time",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.OTP_NOT_GENERATED) {
        CommonMethods.printLog(
          TAG,
          "old_email : " + oldEmail.toString(),
        );
        setState(
          () {
            _mobileController.text = oldEmail;
            isTooManyMobileAttempt = true;
          },
        );
        _timer.cancel();
        CommonMethods.showSnackBar(
          context,
          "Your OTP has been expired. Please resend OTP.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else if (data['result'] == ResponsesKeys.REDIS_ERROR) {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "	Internal server error. Please try again.",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      } else {
        Navigator.pop(context);
        _timer.cancel();
        setState(
          () {
            _emailController.text = oldEmail;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again!",
        );
        cleverTapVerificationStatus(
          "EMAIL",
          false,
          email,
        );
      }
    }
  }

  Future _updateUsername(
    String userId,
    String newUsername,
    String oldUsername,
  ) async {
    CommonMethods.printLog(
        StringConstants.emptyString, "----------Update Username----------");
    Map<String, Object> result = await UserProfileService.updateUsername(
      userId,
      newUsername,
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
          StringConstants.emptyString, result['error'].toString());
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
    } else {
      Map data = result['data'];

      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again! ",
        );
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await _updateUsername(
            userId,
            newUsername,
            oldUsername,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        // String status = data['status'];
        setState(() {
          _isUsernameUpdateAllowed = false;
          _usernameController.text = newUsername;
        });
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.userName,
          newUsername,
        );
        await SharedPrefService.addIntToSharedPref(
          SharedPrefKeys.usernameUpdateCount,
          1,
        );
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              'Username updated successfully',
            ),
          ),
        );
        String fName = initialFirst == StringConstants.emptyString
            ? StringConstants.emptyString
            : initialFirst;
        String mName = initialMiddle == StringConstants.emptyString
            ? StringConstants.emptyString
            : " " + initialMiddle;
        String lName = initialLast == StringConstants.emptyString
            ? StringConstants.emptyString
            : " " + initialLast;
        CleverTapPlugin.recordEvent("Profile Created", {
          'fullname': fName + mName + lName,
          'username': newUsername,
          'month_of_birth': selectedDOB.month
        });
        CleverTapPlugin.profileSet(
          {
            "Username": newUsername,
          },
        );
      } else if (data['result'] == ResponsesKeys.INVALID_USER_NAME) {
        setState(() {
          _usernameController.text = oldUsername;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              data['Invalid Username'],
            ),
          ),
        );
      } else if (data['result'] == ResponsesKeys.CONTAINS_BAD_WORD) {
        setState(
          () {
            _usernameController.text = oldUsername;
          },
        );
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              "Username cannot contain bad words.Please choose another username.",
            ),
          ),
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        setState(
          () {
            _usernameController.text = oldUsername;
          },
        );
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              "User not found",
            ),
          ),
        );
      } else if (data['result'] == ResponsesKeys.USERNAME_IN_USE) {
        setState(
          () {
            _usernameController.text = oldUsername;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "Username already in use",
        );
      } else {
        setState(
          () {
            _usernameController.text = oldUsername;
          },
        );
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again!",
        );
      }
    }
  }

  Future _updateUserProfileDetails(
    String firstName,
    String middleName,
    String lastName,
    int dateOfBirth,
    String mobile,
    String gender,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Update User Info----------",
    );
    Map<String, Object> result = await UserProfileService.updateUserProfileInfo(
      firstName,
      middleName,
      lastName,
      dateOfBirth,
      mobile,
      gender,
      userId,
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'].toString(),
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        cleverTapProfileUpdate(
          false,
          false,
        );
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await _updateUserProfileDetails(
            firstName,
            middleName,
            lastName,
            dateOfBirth,
            mobile,
            gender,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.printLog(
          TAG,
          "HERE SUCCESSFUL",
        );
        CommonMethods.showSnackBar(
          context,
          "User Information updated successfully",
        );
        await SharedPrefService.storeInSharedPref(
          SharedPrefKeys.firstName,
          _firstnameController.text,
        );
        await SharedPrefService.storeInSharedPref(
          SharedPrefKeys.middleName,
          _middleNameController.text,
        );
        await SharedPrefService.storeInSharedPref(
          SharedPrefKeys.lastName,
          _lastnameController.text,
        );
        setState(
          () {
            initialGender = gender;
            initialFirst = firstName;
            initialMiddle = middleName;
            initialLast = lastName;
            initialDOB = convertDateToTimeStamp(
              selectedDOB,
            ).toString();
            hasChanged = false;
          },
        );
        String fName = initialFirst == StringConstants.emptyString
            ? StringConstants.emptyString
            : initialFirst;
        String mName = initialMiddle == StringConstants.emptyString
            ? StringConstants.emptyString
            : " " + initialMiddle;
        String lName = initialLast == StringConstants.emptyString
            ? StringConstants.emptyString
            : " " + initialLast;
        CleverTapPlugin.recordEvent("Profile Created", {
          'fullname': fName + mName + lName,
          'username': _usernameController.text,
          'month_of_birth': selectedDOB.month
        });
        CleverTapPlugin.recordEvent(
          "Dob",
          {
            'date_of_birth': initialDOB,
          },
        );
        cleverTapProfileUpdate(
          true,
          false,
        );
        CleverTapPlugin.profileSet(
          {
            "First Name": firstName,
          },
        );
      } else if (data['result'] == ResponsesKeys.INVALID_USER_NAME) {
        CommonMethods.showSnackBar(
          context,
          "Invalid Username",
        );
        cleverTapProfileUpdate(
          false,
          true,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_DOB) {
        CommonMethods.showSnackBar(
          context,
          "Age cannot be greater than 100 years",
        );
        cleverTapProfileUpdate(
          false,
          true,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        CommonMethods.showSnackBar(
          context,
          "User not found",
        );
        cleverTapProfileUpdate(
          false,
          true,
        );
      } else if (data['result'] == ResponsesKeys.INVALID_STATE) {
        CommonMethods.showSnackBar(
          context,
          "Invalid State",
        );
        cleverTapProfileUpdate(
          false,
          true,
        );
      } else {
        CommonMethods.showSnackBar(
          context,
          "There was a problem! Please try again!",
        );
        cleverTapProfileUpdate(
          false,
          true,
        );
      }
    }
  }

  Future requestForOTPMobile(
    String number,
    String oldNumber,
    String template,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      '-----------OTP REQUEST----------',
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      number.toString(),
    );

    Map<String, Object> result = await AuthService.requestUSerPRofileOTPMobile(
      number,
      userId,
    );

    CommonMethods.printLog(
      StringConstants.emptyString,
      result.toString(),
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      _timer.cancel();
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'].toString(),
      );
      Navigator.of(context).pop();
      _timer.cancel();
      CommonMethods.showCustomDialog(
        title: 'Oops!',
        error: "Something went wrong. Try again",
        context: context,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        _timer.cancel();
      } else if ((data['result'] == ResponsesKeys.TOKEN_EXPIRED) ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await requestForOTPMobile(
            number,
            oldNumber,
            template,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.EMAIL_IN_USE) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Email already in use",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.MOBILE_IN_USE) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Mobile already in use",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.REDIS_ERROR) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data.containsValue(ResponsesKeys.OTP_SENT)) {
        if (data.containsKey('request_id')) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "data['request_id']  :  " + data['request_id'],
          );
          requestIdMobile = data['request_id'];
          showEnterOTPMobileDialog(
            "Verify Mobile",
            context,
            number,
            oldNumber,
            requestIdMobile,
          );
        }
      } else {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try Again",
          context: context,
        );
      }
    }
  }

  Future requestForOTPEmail(
    String newEmail,
    String oldEmail,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      '-----------EMAIL OTP REQUEST----------',
    );
    CommonMethods.printLog(
      StringConstants.emptyString,
      newEmail.toString(),
    );

    Map<String, Object> result = await AuthService.requestUSerPRofileOTPEmail(
      newEmail,
      userId,
    );

    CommonMethods.printLog(
      StringConstants.emptyString,
      result.toString(),
    );
    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      _timer.cancel();
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
          StringConstants.emptyString, result['error'].toString());
      Navigator.of(context).pop();
      _timer.cancel();
      CommonMethods.showCustomDialog(
        title: 'Oops!',
        error: "Something went wrong. Try again",
        context: context,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
        _timer.cancel();
      } else if ((data['result'] == 'TOKEN_EXPIRED') ||
          (data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED)) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await requestForOTPEmail(
            newEmail,
            oldEmail,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.EMAIL_IN_USE) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Email already in use",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.MOBILE_IN_USE) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Mobile already in use",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.REDIS_ERROR) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      } else if (data.containsValue(ResponsesKeys.OTP_SENT)) {
        if (data.containsKey('request_id')) {
          CommonMethods.printLog(
            StringConstants.emptyString,
            "data['request_id']  :  " + data['request_id'].toString(),
          );
          requestIdEmail = data['request_id'];
          showEnterOTPEmailDialog(
            "Verify Email",
            context,
            newEmail,
            oldEmail,
            requestIdEmail,
          );
        }
      } else {
        _timer.cancel();
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Something went wrong. Try again",
          context: context,
        );
      }
    }
  }

  Future resendOTPMobile({
    @required String number,
    @required String requestId,
    @required String template,
    @required String userId,
  }) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      '-----------EMAIL OTP REQUEST----------',
    );

    Map<String, Object> result = await AuthService.requestResendOTPMobile(
      number,
      requestId,
      template,
      StringConstants.emptyString,
      userId,
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'].toString(),
      );
      CommonMethods.showCustomDialog(
        title: 'Oops!',
        error: "Something went wrong. Try again",
        context: context,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
      } else if (data['result'] == 'TOKEN_EXPIRED') {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await resendOTPMobile(
            number: number,
            requestId: requestIdMobile,
            template: template,
            userId: userId,
          );
        }
      } else if (data['result'] == 'OTP_RESENT') {
        CommonMethods.printLog(
          StringConstants.emptyString,
          "data['request_id']  :  " + data['request_id'].toString(),
        );
        requestIdMobile = data['request_id'];
        startTimer();
        CommonMethods.showSnackBar(context, "OTP SENT");
      } else if (data['result'] == 'DB_ERROR') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Database Error ",
          context: context,
        );
      } else if (data['result'] == 'REDIS_ERROR') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Database Error ",
          context: context,
        );
      } else if (data['result'] == 'REQUEST_ID_MISSING') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Technical Error ",
          context: context,
        );
      } else {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Error in resending OTP ",
          context: context,
        );
      }
    }
  }

  Future resendOTPEamil(
    String email,
    String requestId,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      '-----------EMAIL OTP REQUEST----------',
    );
    String username = await SharedPrefService.getStringValuesFromSharedPref(
      SharedPrefKeys.userName,
    );

    Map<String, Object> result = await AuthService.requestResendOTPEmail(
      email,
      requestId,
      username,
      userId,
    );

    if (result.containsKey('noInternet')) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'].toString(),
      );
      CommonMethods.showCustomDialog(
        title: 'Oops!',
        error: "Something went wrong. Try again",
        context: context,
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        CommonMethods.showSnackBar(
          context,
          "Something went wrong. Try again",
        );
      } else if (data['result'] == 'TOKEN_EXPIRED') {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(
          userId,
        );
        if (accessTokenGenerated) {
          await resendOTPEamil(
            email,
            requestId,
            userId,
          );
        }
      } else if (data['result'] == 'OTP_RESENT') {
        CommonMethods.printLog(
          StringConstants.emptyString,
          "data['request_id']  :  " + data['request_id'].toString(),
        );
        requestIdEmail = data['request_id'];
        startTimer();
        CommonMethods.showSnackBar(
          context,
          "OTP RESEND",
        );
      } else if (data['result'] == 'DB_ERROR') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Database Error ",
          context: context,
        );
      } else if (data['result'] == 'REDIS_ERROR') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Database Error ",
          context: context,
        );
      } else if (data['result'] == 'REQUEST_ID_MISSING') {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Technical Error ",
          context: context,
        );
      } else {
        CommonMethods.showCustomDialog(
          title: 'Oops!',
          error: "Error in resending OTP ",
          context: context,
        );
      }
    }
  }

  void cleverTapVerificationStatus(
    String what,
    bool status,
    String value,
  ) {
    if (what == "MOBILE") {
      var eventData = {
        'mobile_number': value,
        'mobile_verified': status,
      };
      CommonMethods.printLog(
        "CleverTap Mobile Verification",
        eventData.toString(),
      );
      CleverTapPlugin.recordEvent(
        "Mobile Status",
        eventData,
      );
    } else if (what == "EMAIL") {
      var eventData = {
        'email_id': value,
        'email_verified': status,
      };
      CommonMethods.printLog(
        "CleverTap Email Verification",
        eventData.toString(),
      );
      CleverTapPlugin.recordEvent(
        "Email Status",
        eventData,
      );
    }
  }

  void cleverTapProfileUpdate(
    bool updated,
    bool incomplete,
  ) {
    var eventData = {
      'profile_updated': updated,
      'profile_incomplete': incomplete
    };
    CommonMethods.printLog(
      "CleverTap Profile Update",
      eventData.toString(),
    );
    CleverTapPlugin.recordEvent(
      "Profile Updated",
      eventData,
    );
  }

  void showEnterOTPMobileDialog(
    String title,
    BuildContext context,
    String newValue,
    String oldValue,
    String requestId,
  ) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return StatefulBuilder(
          builder: (
            context,
            StateSetter setState,
          ) {
            return customOTPMobileDialog(
              context,
              title,
              newValue,
              oldValue,
              requestId,
            );
          },
        );
      },
    );
  }

  Widget customOTPMobileDialog(
    BuildContext context,
    String header,
    String newMobile,
    String oldValue,
    String requestId,
  ) {
    // startMobileTimer();
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: StreamBuilder<int>(
        stream: _events.stream,
        builder: (
          BuildContext context,
          AsyncSnapshot<int> snapshot,
        ) {
          return Dialog(
            elevation: 40,
            backgroundColor: Colors.transparent,
            child: Container(
              height: 43.0.h,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(
                  AppDimens.circularBorder,
                ),
                color: Colors.white,
              ),
              child: Column(
                children: [
                  Container(
                    height: 8.0.h,
                    decoration: BoxDecoration(
                      gradient: ColorConstants.blueDarkGradient,
                      borderRadius: BorderRadius.circular(
                        AppDimens.circularBorder,
                      ),
                    ),
                    child: Center(
                      child: Text(
                        header,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontSize: 17.0.sp,
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 3.0.h,
                  ),
                  Center(
                    child: Container(
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Center(
                          child: Text(
                            StringConstants.otpDialogBoxText,
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontSize: 10.2.sp,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 1.5.h,
                  ),
                  Padding(
                    padding: const EdgeInsets.only(
                      left: 25,
                      right: 25,
                    ),
                    child: GestureDetector(
                      onTap: () {
                        if (isTooManyMobileAttempt) {
                          CommonMethods.showSnackBar(
                            context,
                            'Too many attempts, please wait for sometime',
                          );
                        }
                      },
                      child: Container(
                        height: 6.0.h,
                        child: TextFormField(
                          controller: _otpController,
                          enabled: !isTooManyMobileAttempt,
                          keyboardType: TextInputType.number,
                          maxLength: 6,
                          decoration: InputDecoration(
                            contentPadding: EdgeInsets.only(
                              bottom: 3.0.sp,
                              left: 5,
                            ),
                            border: OutlineInputBorder(),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(
                                  5.0,
                                ),
                              ),
                            ),
                            labelText: '',
                            counterText: '',
                            labelStyle: TextStyle(
                              fontSize: 12.0.sp,
                              color: ColorConstants.dark1,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 1.5.h,
                  ),
                  Visibility(
                    visible: !isTimerMobileFinished,
                    child: Padding(
                      padding: const EdgeInsets.only(
                        top: 0.0,
                      ),
                      child: Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text(
                              snapshot.hasData
                                  ? "0" +
                                      (snapshot.data ~/ 60).toString() +
                                      " : " +
                                      (snapshot.data % 60)
                                          .toString()
                                          .padLeft(2, '0')
                                  : '',
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 7.6.sp,
                              ),
                            ),
                          ]),
                    ),
                  ),
                  Visibility(
                    // visible: showResendOTPMobile,
                    visible: isTimerMobileFinished,
                    child: Padding(
                      padding: const EdgeInsets.only(
                        right: 30.0,
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          GestureDetector(
                            onTap: () async {
                              setState(
                                () {
                                  isTimerMobileFinished = false;
                                  isTooManyMobileAttempt = false;
                                },
                              );
                              // startMobileTimer();
                              await resendOTPMobile(
                                number: newMobile,
                                requestId: requestIdMobile,
                                template: "VERIFY_MOBILE",
                                userId: widget.userId,
                              );
                            },
                            child: Align(
                              alignment: Alignment.centerRight,
                              child: Text(
                                "RESEND OTP",
                                style: TextStyle(
                                    color: Colors.black,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 9.0.sp),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 3.0.h,
                  ),
                  Align(
                    alignment: Alignment.bottomCenter,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        GestureDetector(
                          onTap: () async {
                            FocusScope.of(context).unfocus();
                            if (isTooManyMobileAttempt) {
                              CommonMethods.showSnackBar(
                                context,
                                "Too many attempts, please wait for sometime",
                              );
                            } else {
                              if (_otpController.text.toString().length < 6) {
                                CommonMethods.showSnackBar(
                                  context,
                                  "Enter a valid OTP",
                                );
                              } else {
                                _handleMobileVerification(
                                  newMobile,
                                  _otpController.text.toString(),
                                  requestIdMobile,
                                  oldValue,
                                  widget.userId,
                                );
                                _otpController.clear();
                              }
                            }
                          },
                          child: Container(
                            height: 4.5.h,
                            width: 25.0.w,
                            decoration: BoxDecoration(
                              gradient: ColorConstants.blueDarkGradient,
                              borderRadius: BorderRadius.circular(
                                7,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Apply",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 10.3.sp,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                        SizedBox(
                          width: 5.0.w,
                        ),
                        GestureDetector(
                          onTap: () {
                            Navigator.pop(context);
                            _otpController.clear();
                            _timer.cancel();
                            // if(_timerMobile!=null)
                            // _timerMobile.cancel();
                            cleverTapVerificationStatus(
                              "MOBILE",
                              false,
                              newMobile,
                            );
                          },
                          child: Container(
                            height: 4.5.h,
                            width: 25.0.w,
                            decoration: BoxDecoration(
                              gradient: ColorConstants.blueDarkGradient,
                              borderRadius: BorderRadius.circular(
                                7,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Cancel",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 10.3.sp,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  void showEnterOTPEmailDialog(
    String title,
    BuildContext context,
    String newEmail,
    String oldEmail,
    String requestId,
  ) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return customOTPEmailDialog(
          context,
          title,
          newEmail,
          oldEmail,
          requestId,
        );
      },
    );
  }

  Widget customOTPEmailDialog(
    BuildContext context,
    String header,
    String newEmail,
    String oldEmail,
    String requestId,
  ) {
    // startEmailTimer();
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: StreamBuilder<int>(
        stream: _events.stream,
        builder: (BuildContext context, AsyncSnapshot<int> snapshot) {
          return Dialog(
            elevation: 40,
            backgroundColor: Colors.transparent,
            child: Container(
              height: 43.0.h,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(
                  AppDimens.circularBorder,
                ),
                color: Colors.white,
              ),
              child: Column(
                children: [
                  Container(
                    height: 8.0.h,
                    decoration: BoxDecoration(
                      gradient: ColorConstants.blueDarkGradient,
                      borderRadius: BorderRadius.circular(
                        7,
                      ),
                    ),
                    child: Center(
                      child: Text(
                        header,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontSize: 17.0.sp,
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 3.0.h,
                  ),
                  Center(
                    child: Container(
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Center(
                          child: Text(
                            StringConstants.emailDialogText,
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontSize: 10.2.sp,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 1.5.h,
                  ),
                  Padding(
                    padding: const EdgeInsets.only(
                      left: 25,
                      right: 25,
                    ),
                    child: Container(
                      height: 6.0.h,
                      child: GestureDetector(
                        onTap: () {
                          if (isTooManyEmailAttempt) {
                            CommonMethods.showSnackBar(
                              context,
                              'Too many attempts, please wait for sometime',
                            );
                          }
                        },
                        child: TextFormField(
                          controller: _otpController,
                          enabled: !isTooManyEmailAttempt,
                          keyboardType: TextInputType.number,
                          maxLength: 6,
                          decoration: InputDecoration(
                            contentPadding: EdgeInsets.only(
                              bottom: 3.0.sp,
                              left: 5,
                            ),
                            border: OutlineInputBorder(),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(
                                  5.0,
                                ),
                              ),
                            ),
                            labelText: '',
                            counterText: '',
                            labelStyle: TextStyle(
                              fontSize: 12.0.sp,
                              color: ColorConstants.dark1,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 1.5.sp,
                  ),
                  Visibility(
                    visible: !isTimerEmailFinished,
                    child: Padding(
                      padding: const EdgeInsets.only(
                        top: 0.0,
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            snapshot.hasData
                                ? "0" +
                                    (snapshot.data ~/ 60).toString() +
                                    " : " +
                                    (snapshot.data % 60).toString().padLeft(
                                          2,
                                          '0',
                                        )
                                : '',
                            style: TextStyle(
                              color: Colors.black,
                              fontSize: 7.6.sp,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Visibility(
                    visible: isTimerEmailFinished,
                    child: Padding(
                      padding: const EdgeInsets.only(
                        right: 30.0,
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          GestureDetector(
                            onTap: () {
                              setState(
                                () {
                                  isTimerEmailFinished = false;
                                  isTooManyEmailAttempt = false;
                                },
                              );
                              // startMobileTimer();
                              resendOTPEamil(
                                newEmail,
                                requestId,
                                widget.userId,
                              );
                            },
                            child: Align(
                              alignment: Alignment.centerRight,
                              child: Text(
                                "RESEND OTP",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 9.0.sp,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 3.0.h,
                  ),
                  Align(
                    alignment: Alignment.bottomCenter,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        GestureDetector(
                          onTap: () async {
                            FocusScope.of(context).unfocus();
                            if (isTooManyMobileAttempt) {
                              CommonMethods.showSnackBar(
                                context,
                                "Too many attempts, please wait for sometime",
                              );
                            } else {
                              if (_otpController.text.toString().length < 6) {
                                CommonMethods.showSnackBar(
                                  context,
                                  "Enter a valid OTP",
                                );
                              } else {
                                _handleEmailVerification(
                                  newEmail,
                                  _otpController.text.toString(),
                                  requestIdEmail,
                                  "true",
                                  oldEmail,
                                  widget.userId,
                                );

                                _otpController.clear();
                              }
                            }
                          },
                          child: Container(
                            height: 4.5.h,
                            width: 25.0.w,
                            decoration: BoxDecoration(
                              gradient: ColorConstants.blueDarkGradient,
                              borderRadius: BorderRadius.circular(
                                7,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Apply",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 10.3.sp,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                        SizedBox(
                          width: 5.0.w,
                        ),
                        GestureDetector(
                          onTap: () {
                            Navigator.pop(context);
                            _otpController.clear();
                            _timer.cancel();
                            // if(_timerMobile!=null)
                            // _timerMobile.cancel();
                            cleverTapVerificationStatus(
                              "EMAIL",
                              false,
                              newEmail,
                            );
                          },
                          child: Container(
                            height: 4.5.h,
                            width: 25.0.w,
                            decoration: BoxDecoration(
                              gradient: ColorConstants.blueDarkGradient,
                              borderRadius: BorderRadius.circular(
                                7,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Cancel",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 10.3.sp,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _firstnameController.dispose();
    _middleNameController.dispose();
    _lastnameController.dispose();
    _mobileController.dispose();
    _emailController.dispose();
    _kycController.dispose();
    _otpController.dispose();
    _dobController.dispose();
    _add1Controller.dispose();
    _add2Controller.dispose();
    _cityController.dispose();
    _pinCodeController.dispose();
    _stateController.dispose();
    if (_timer != null) {
      _timer.cancel();
    }
    super.dispose();
  }
}
