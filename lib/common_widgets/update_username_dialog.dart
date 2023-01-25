import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sizer/sizer.dart';

import '../Network/generate_access_token.dart';
import '../Network/user_profile_service.dart';
import '../constants/color_constants.dart';
import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../network_new/constants/responses_keys.dart';
import '../utils/shared_pref_service.dart';

class UpdateUsernameDialog extends StatefulWidget {
  final String header, description, oldValue;
  final TextInputType textInputType;
  final TextEditingController textEditingController;
  final List<TextInputFormatter> inputformatters;
  final int length;

  const UpdateUsernameDialog({
    Key key,
    this.header,
    this.description,
    this.oldValue,
    this.textInputType,
    this.textEditingController,
    this.inputformatters,
    this.length,
  }) : super(key: key);

  @override
  State<UpdateUsernameDialog> createState() => _UpdateUsernameDialogState();
}

class _UpdateUsernameDialogState extends State<UpdateUsernameDialog> {
  bool errorVisible = false;

  String errorMessage = StringConstants.emptyString;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return Future.delayed(Duration.zero).then(
          (value) => false,
        );
      },
      child: Dialog(
        elevation: 40,
        backgroundColor: ColorConstants.transparent,
        child: Container(
          height: 30.0.h,
          decoration: BoxDecoration(
            border: Border.all(
              color: ColorConstants.blue1,
            ),
            borderRadius: BorderRadius.circular(
              2.0.w,
            ),
            color: ColorConstants.white,
          ),
          child: Column(
            children: [
              SizedBox(
                height: 2.1.h,
              ),
              Center(
                child: Container(
                  child: Padding(
                    padding: const EdgeInsets.all(
                      8.0,
                    ),
                    child: Center(
                      child: Text(
                        StringConstants.enterUsernameToUpdate,
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
                  child: TextFormField(
                    maxLength: widget.length,
                    controller: widget.textEditingController,
                    keyboardType: widget.textInputType,
                    inputFormatters: widget.inputformatters,
                    onChanged: (value) {
                      if (errorVisible) {
                        setState(
                          () {
                            errorVisible = false;
                          },
                        );
                      }
                    },
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
                          Radius.circular(
                            5.0,
                          ),
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
              Padding(
                padding: EdgeInsets.only(
                  left: 2.0.w,
                  right: 2.0.w,
                ),
                child: SizedBox(
                  height: 2.0.h,
                  child: Center(
                    child: Visibility(
                      visible: errorVisible,
                      child: Text(
                        errorMessage,
                        overflow: TextOverflow.visible,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.red,
                          fontSize: 10.0.sp,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              SizedBox(
                height: 4.1.h,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () async {
                      String userId =
                          await SharedPrefService.getStringValuesFromSharedPref(
                        SharedPrefKeys.userID,
                      );

                      FocusManager.instance.primaryFocus?.unfocus();

                      if (widget.textEditingController.text.isEmpty) {
                        setState(
                          () {
                            errorMessage =
                                StringConstants.pleaseEnterUsernameToUpdate;
                            errorVisible = true;
                          },
                        );
                      } else if (widget.textEditingController.text ==
                          widget.oldValue) {
                        widget.textEditingController.clear();
                        setState(
                          () {
                            errorMessage = StringConstants
                                .youHaveEnteredAlreadyExistingUsername;
                            errorVisible = true;
                          },
                        );
                      } else {
                        updateUsername(
                          userId,
                          widget.textEditingController.text.trim(),
                          widget.oldValue,
                        );
                      }
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        gradient: ColorConstants.blueDarkGradient,
                        borderRadius: BorderRadius.circular(
                          5,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "profile_screen.submit".tr(),
                          style: TextStyle(
                            color: ColorConstants.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 2.0.w,
                  ),
                  GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: Container(
                      height: 5.0.h,
                      width: 30.0.w,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(
                          5,
                        ),
                        border: Border.all(
                          color: ColorConstants.grey,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          StringConstants.goBack,
                          style: TextStyle(
                            color: ColorConstants.grey,
                            fontWeight: FontWeight.bold,
                            fontSize: 10.0.sp,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              )
            ],
          ),
        ),
      ),
    );
  }

  //Methods
  Future updateUsername(
    String userId,
    String newUsername,
    String oldUsername,
  ) async {
    CommonMethods.printLog(
      "",
      "----------Update Username----------",
    );
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
        "",
        result['error'].toString(),
      );
      setState(
        () {
          errorMessage = "There was a problem! Please try again!";
          errorVisible = true;
        },
      );
    } else {
      Map data = result['data'];

      if (data.containsKey('error')) {
        setState(
          () {
            errorMessage = "There was a problem! Please try again!";
            errorVisible = true;
          },
        );
      } else if (data['result'] == ResponsesKeys.TOKEN_EXPIRED ||
          data['result'] == ResponsesKeys.TOKEN_PARSING_FAILED) {
        bool accessTokenGenerated =
            await GenerateAccessToken.regenerateAccessToken(userId);
        if (accessTokenGenerated) {
          await updateUsername(
            userId,
            newUsername,
            oldUsername,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        Navigator.pop(context);
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.userName,
          newUsername,
        );
        await SharedPrefService.addIntToSharedPref(
          SharedPrefKeys.usernameUpdateCount,
          1,
        );
        CommonMethods.showSnackBar(
          context,
          "Username updated successfully",
        );
        CleverTapPlugin.profileSet({
          "Username": newUsername,
        });
      } else if (data['result'] == ResponsesKeys.INVALID_USER_NAME) {
        setState(
          () {
            errorMessage = "Invalid Username";
            errorVisible = true;
          },
        );
      } else if (data['result'] == ResponsesKeys.CONTAINS_BAD_WORD) {
        setState(
          () {
            errorMessage =
                "Username cannot contain bad words. Please choose another username.";
            errorVisible = true;
          },
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        setState(
          () {
            errorMessage = "User not found";
            errorVisible = true;
          },
        );
      } else if (data['result'] == ResponsesKeys.USERNAME_IN_USE) {
        setState(
          () {
            errorMessage = "Username already in use";
            errorVisible = true;
          },
        );
      } else {
        setState(
          () {
            errorMessage = "There was a problem! Please try again!";
            errorVisible = true;
          },
        );
      }
    }
  }
}
