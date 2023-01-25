import 'dart:ui';

import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/network_post_request.dart';
import '../../Network/generate_access_token.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/custom_button.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/responses_keys.dart';
import '../../network_new/constants/url_constants.dart';
import '../../utils/shared_pref_service.dart';
import 'models/avatar_data_model.dart';

class AvatarScreen extends StatefulWidget {
  final String userId;

  const AvatarScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  _AvatarScreenState createState() => _AvatarScreenState();
}

class _AvatarScreenState extends State<AvatarScreen> {
  int avatarId = 1, initialAvatarId = 1;
  String avatarUrl = StringConstants.emptyString;

  @override
  void initState() {
    asyncMethod();
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: AVATAR_ROUTE,
    );
    super.initState();
  }

  Future<void> asyncMethod() async {
    avatarId = await SharedPrefService.getIntValuesFromSharedPref(
          SharedPrefKeys.avatarId,
        ) ??
        1;
    avatarUrl = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.avatarURL,
        ) ??
        StringConstants.emptyString;
    setState(
      () {
        avatarId = avatarId;
        initialAvatarId = avatarId;
      },
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
            from: "userprofile",
            userId: widget.userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        body: Padding(
          padding: EdgeInsets.symmetric(
            vertical: 2.0.h,
            horizontal: 14,
          ),
          child: Column(
            children: [
              //Change Avatar Lable
              LabelHeader(
                label: 'Change Avatar',
              ),
              SizedBox(
                height: 25,
              ),
              //Selected Avatar
              CircleAvatar(
                radius: 90,
                backgroundImage: NetworkImage(
                  AvatarDataModel.ids[avatarId - 1],
                ),
              ),
              SizedBox(
                height: 40,
              ),
              //Avatar GridView
              Container(
                height: 184,
                child: GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 4,
                    crossAxisSpacing: 20,
                    mainAxisSpacing: 20,
                  ),
                  itemCount: AvatarDataModel.ids.length,
                  itemBuilder: (BuildContext context, int index) {
                    return GestureDetector(
                      onTap: () {
                        setState(
                          () {
                            avatarId = index + 1;
                          },
                        );
                      },
                      child: CircleAvatar(
                        backgroundColor: (avatarId == index + 1)
                            ? ColorConstants.textFieldBorderCol
                            : ColorConstants.transparent,
                        radius: 38,
                        child: CircleAvatar(
                          radius: 32,
                          backgroundImage: NetworkImage(
                            AvatarDataModel.ids[index],
                          ),
                        ),
                      ),
                    );
                  },
                ),
              ),
              SizedBox(
                height: 40,
              ),
              CustomButton(
                buttonText: "change_avatar_screen.submit".tr(),
                height: 40,
                width: 50.w,
                onTap: () async {
                  if (initialAvatarId == avatarId) {
                    CommonMethods.showSnackBar(
                      context,
                      "This is the same Avatar",
                    );
                  } else {
                    showDialog(
                      context: context,
                      barrierDismissible: false,
                      builder: (BuildContext context) {
                        return WillPopScope(
                          onWillPop: () {
                            return Future.delayed(Duration.zero).then(
                              (value) => false,
                            );
                          },
                          child: Center(
                            child: CircularProgressIndicator(
                              backgroundColor: ColorConstants.blue,
                            ),
                          ),
                        );
                      },
                    );
                    await _updateAvatarId(
                      avatarId,
                      widget.userId,
                    );
                  }
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  //METHODS
  Future _updateAvatarId(
    int id,
    String userId,
  ) async {
    CommonMethods.printLog(
      StringConstants.emptyString,
      "----------Update Username----------",
    );
    Map<String, Object> result = await updateAvatarId(
      id,
      userId,
    );

    if (result.containsKey('noInternet')) {
      Navigator.pop(context);
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
    } else if (result.containsKey('error')) {
      Navigator.pop(context);
      CommonMethods.printLog(
        StringConstants.emptyString,
        result['error'],
      );
      CommonMethods.showSnackBar(
        context,
        "There was a problem! Please try again! ",
      );
    } else {
      Map data = result['data'];
      if (data.containsKey('error')) {
        Navigator.pop(context);
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
          await _updateAvatarId(
            id,
            userId,
          );
        }
      } else if (data['result'] == ResponsesKeys.SUCCESS) {
        CommonMethods.showSnackBar(
          context,
          "Avatar is successfully updated",
        );
        await SharedPrefService.addIntToSharedPref(
          SharedPrefKeys.avatarId,
          id,
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.avatarURL,
          data['avatarUrl'],
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.lrAvatarURL,
          data['lrAvatarUrl'],
        );
        await SharedPrefService.addStringToSharedPref(
          SharedPrefKeys.pokerAvatarUrl,
          data['pokerAvatarUrl'],
        );
        Navigator.pop(context);
        Navigator.pop(
          context,
          id,
        );
      } else if (data['result'] == ResponsesKeys.USER_NOT_FOUND) {
        Navigator.pop(context);
        CommonMethods.showSnackBar(
          context,
          "Internal Server error. Please try Again",
        );
      } else if (data['result'] == ResponsesKeys.DB_ERROR) {
        Navigator.pop(context);
        CommonMethods.showSnackBar(
          context,
          "Internal Server error. Please try Again",
        );
      } else if (data['result'] == ResponsesKeys.INVALID_AVATAR_ID) {
        Navigator.pop(context);
        CommonMethods.showSnackBar(
          context,
          "Internal Server error. Please try Again",
        );
      } else {
        Navigator.pop(context);
        CommonMethods.showSnackBar(
          context,
          "Internal Server error. Please try Again",
        );
      }
    }
  }

  static Future<Map<String, Object>> updateAvatarId(
    int id,
    String userId,
  ) async {
    Map<String, Object> result = Map();

    Map requestData = {
      "avatar_id": id,
    };
    result = await NetworkPostRequest.putRequestWithAccess(
      requestData,
      UrlConstants.updateAvatarIdUrl,
      userId,
    );

    return result;
  }
}
