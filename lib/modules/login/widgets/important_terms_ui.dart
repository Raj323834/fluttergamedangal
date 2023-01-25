import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/app_constants.dart';
import '../../../constants/asset_paths.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/string_constants.dart';
import '../../terms_and_conditions/terms_and_conditions_screen.dart';
import 'agree_or_disagree_button.dart';

class ImportantTermsUI extends StatelessWidget {
  const ImportantTermsUI({
    Key key,
    @required this.iAgree,
  }) : super(key: key);
  final Function iAgree;
  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(
          10.0,
        ),
      ),
      elevation: 40,
      backgroundColor: ColorConstants.transparent,
      child: Stack(
        children: [
          Container(
            padding: EdgeInsets.only(
              left: AppConstants.padding,
              top: AppConstants.avatarRadius + AppConstants.padding,
              right: AppConstants.padding,
              bottom: AppConstants.padding,
            ),
            margin: EdgeInsets.only(
              top: AppConstants.avatarRadius,
            ),
            decoration: BoxDecoration(
              shape: BoxShape.rectangle,
              image: DecorationImage(
                image: AssetImage(
                  AssetPaths.loginEmailBg,
                ),
                fit: BoxFit.cover,
              ),
              borderRadius: BorderRadius.circular(
                AppConstants.padding,
              ),
              boxShadow: [
                BoxShadow(
                  color: ColorConstants.black,
                  offset: Offset(
                    0,
                    10,
                  ),
                  blurRadius: 20,
                ),
              ],
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  StringConstants.importantTerms,
                  style: TextStyle(
                    fontSize: 17.0.sp,
                    fontWeight: FontWeight.w600,
                    color: ColorConstants.textYellow,
                  ),
                ),
                SizedBox(
                  height: 1.7.h,
                ),
                Text(
                  StringConstants.byProceedingYouAgree,
                  style: TextStyle(
                    fontSize: 9.5.sp,
                    color: ColorConstants.textYellow,
                  ),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: 1.7.h,
                ),
                Text(
                  StringConstants.myAgeIs18,
                  style: TextStyle(
                    fontSize: 9.5.sp,
                    color: ColorConstants.textYellow,
                  ),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: 4.8.h,
                ),
                Text(
                  StringConstants.dangalGamesFairplay,
                  style: TextStyle(
                    fontSize: 13.0.sp,
                    fontWeight: FontWeight.w600,
                    color: ColorConstants.textYellow,
                  ),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: 1.7.h,
                ),
                Text(
                  StringConstants.dangalGamesHasStrictPolicies,
                  style: TextStyle(
                    fontSize: 9.0.sp,
                    color: ColorConstants.textYellow,
                  ),
                  textAlign: TextAlign.center,
                ),
                SizedBox(
                  height: 3.4.h,
                ),
                //Terms And Conditions Button
                Padding(
                  padding: const EdgeInsets.all(
                    2.0,
                  ),
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(
                          10.0,
                        ),
                        side: BorderSide(
                          color: ColorConstants.textYellow,
                        ),
                      ),
                      primary: ColorConstants.transparent,
                      onPrimary: ColorConstants.textYellow,
                    ),
                    onPressed: () {
                      Navigator.push(
                        context,
                        CupertinoPageRoute(
                          builder: (BuildContext context) =>
                              TermsAndConditionsScreen(
                            userId: '',
                            isLoggedIn: false,
                          ),
                        ),
                      );
                    },
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Text(
                          StringConstants.termsAndConditions,
                          style: TextStyle(
                            fontSize: 10.0.sp,
                            color: ColorConstants.textYellow,
                          ),
                          textAlign: TextAlign.center,
                          maxLines: 1,
                        ),
                        Icon(
                          Icons.arrow_right,
                          color: ColorConstants.textYellow,
                        ),
                      ],
                    ),
                  ),
                ),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    //I Agree Button
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.only(
                          top: 10.0,
                          left: 30.0,
                          right: 10.0,
                        ),
                        child: AgreeOrDisagreeButton(
                          isAgreeButton: true,
                          onTap: iAgree,
                        ),
                      ),
                    ),
                    //I Disagree Button
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.only(
                          top: 10.0,
                          left: 10.0,
                          right: 30.0,
                        ),
                        child: AgreeOrDisagreeButton(
                          isAgreeButton: false,
                          onTap: () {
                            Navigator.of(context).pop();
                            FocusScope.of(context).unfocus();
                          },
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          //Logo
          Positioned(
            left: AppConstants.padding,
            right: AppConstants.padding,
            child: CircleAvatar(
              backgroundColor: ColorConstants.transparent,
              radius: AppConstants.avatarRadius,
              child: Image.asset(
                AssetPaths.logo,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
