import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/generate_access_token.dart';
import '../../constants/color_constants.dart';
import '../../constants/enum.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/shared_pref_keys.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../../utils/shared_pref_service.dart';
import 'models/referral_code_dm.dart';
import 'repos/fetch_refferal_repo.dart';
import 'widgets/copy_referral_code.dart';
import 'widgets/refer_and_earn_image.dart';
import 'widgets/share_icon.dart';

class ShareScreen extends StatefulWidget {
  final String userId;

  ShareScreen({
    @required this.userId,
  });

  @override
  _ShareScreenState createState() => _ShareScreenState();
}

class _ShareScreenState extends State<ShareScreen> {
  String referralCode = '-';
  String downloadURL = StringConstants.emptyString;
  DateTime currentBackPressTime;

  @override
  void initState() {
    fetchReferralCode();
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: RAF_ROUTE,
    );
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final String inviteMessage = CommonMethods.getInviteText(
      downloadURL: downloadURL,
      referralCode: referralCode,
    );

    return WillPopScope(
      onWillPop: () {
        DateTime now = DateTime.now();
        if (currentBackPressTime == null ||
            now.difference(currentBackPressTime) >
                Duration(
                  seconds: 2,
                )) {
          currentBackPressTime = now;
          CommonMethods.showSnackBar(
            context,
            StringConstants.pleaseClickBackAgainToExit,
          );
          return Future.value(
            false,
          );
        }
        return Future.value(
          true,
        );
      },
      child: Scaffold(
        body: Container(
          margin: EdgeInsets.only(
            top: 36,
          ),
          padding: EdgeInsets.symmetric(
            horizontal: 22,
          ),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                //Refer & Earn Text Widget
                Text(
                  "raf_screen.raf".tr(),
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 20.0,
                    fontWeight: FontWeight.w800,
                    color: ColorConstants.white,
                  ),
                ),
                SizedBox(
                  height: 13,
                ),
                //Refer & Earn Image
                ReferAndEarnImage(),
                SizedBox(
                  height: 40,
                ),
                //Your Referal Code Widget
                CopyReferralCodeWidget(
                  referralCode: referralCode,
                ),
                SizedBox(
                  height: 40,
                ),
                //Share App Widget
                Padding(
                  padding: EdgeInsets.symmetric(
                    horizontal: 50,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        margin: EdgeInsets.only(
                          left: 12,
                        ),
                        child: Text(
                          StringConstants.share,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                            fontSize: 14.0,
                            color: ColorConstants.white,
                          ),
                        ),
                      ),
                      Center(
                        child: GridView.count(
                          crossAxisCount: 4,
                          shrinkWrap: true,
                          mainAxisSpacing: 8,
                          crossAxisSpacing: 8,
                          children: [
                            //Share On Whatsapp
                            ShareIcon(
                              inviteMessage: inviteMessage,
                              shareIconType: ShareIconType.whatsapp,
                            ),
                            //Share On Telegram
                            ShareIcon(
                              inviteMessage: inviteMessage,
                              shareIconType: ShareIconType.telegram,
                            ),
                            //Share On Messages
                            ShareIcon(
                              inviteMessage: inviteMessage,
                              shareIconType: ShareIconType.messages,
                            ),
                            //Generic Share
                            ShareIcon(
                              inviteMessage: inviteMessage,
                              shareIconType: ShareIconType.share,
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                )
              ],
            ),
          ),
        ),
        backgroundColor: ColorConstants.kBackgroundColor,
      ),
    );
  }

  //METHODS

  //Fetch Referral Code
  fetchReferralCode() async {
    //download url for PlayStore app
    var repoObj = FetchReferralRepo();
    ReferralCodeDM referralCodeDM = await repoObj.fetchRefferal(
      userId: widget.userId,
    );

    if (referralCodeDM != null) {
      switch (referralCodeDM.result) {
        case ResponseStatus.success:
          await SharedPrefService.addStringToSharedPref(
            SharedPrefKeys.referCode,
            referralCodeDM.referralCode,
          );
          await SharedPrefService.addStringToSharedPref(
            SharedPrefKeys.downloadURL,
            referralCodeDM.apkLink,
          );
          setState(() {
            referralCode = referralCodeDM.referralCode;
            downloadURL = referralCodeDM.apkLink;
          });
          break;

        case ResponseStatus.dbError:
          CommonMethods.showSnackBar(
            context,
            "Server Error. Unable to fetch any BONUS",
          );
          break;

        case ResponseStatus.tokenExpired:
        case ResponseStatus.tokenParsingFailed:
          bool accessTokenGenerated =
              await GenerateAccessToken.regenerateAccessToken(
            widget.userId,
          );
          if (accessTokenGenerated) {
            fetchReferralCode();
          }
          break;
        default:
      }
    } else {
      CommonMethods.showSnackBar(
        context,
        StringConstants.unableToFetchYourReferralCode,
      );
    }
  }
}
