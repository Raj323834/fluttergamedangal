import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/generate_access_token.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import 'models/vip_support_dm.dart';
import 'repos/vip_support_repo.dart';
import 'widgets/vip_container.dart';

class SupportSubScreen extends StatefulWidget {
  final String userId;

  const SupportSubScreen({
    Key key,
    this.userId,
  }) : super(key: key);

  @override
  State<SupportSubScreen> createState() => _SupportSubScreenState();
}

class _SupportSubScreenState extends State<SupportSubScreen> {
  String supportContactNumber;
  bool isFetched = false;

  @override
  void initState() {
    super.initState();
    callApi();
  }

  callApi() async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      CommonMethods.showSnackBar(
        context,
        StringConstants.noInternetConnection,
      );
      Navigator.pop(context);
      FocusScope.of(context).unfocus();
    } else {
      var repoObj = VipSupportRepo();
      VipSupportDM vipSupportDm = await repoObj.fetchUserStats(
        userId: widget.userId,
      );
      if (vipSupportDm != null) {
        switch (vipSupportDm.result) {
          case ResponseStatus.success:
            supportContactNumber = vipSupportDm.supportContactNumber;
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              "Server Error. Unable to fetch any BONUS",
            );
            break;

          case ResponseStatus.supportContactNotAvailable:
            break;

          case ResponseStatus.tokenExpired:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              widget.userId,
            );
            if (accessTokenGenerated) {
              await callApi();
            }
            break;
          default:
        }
        setState(
          () {
            isFetched = true;
          },
        );
      } else {
        CommonMethods.showSnackBar(context, 'Something Went Wrong');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: SUPPORT_ROUTE,
    );
    return Scaffold(
      backgroundColor: ColorConstants.kBackgroundColor,
      body: Padding(
        padding: EdgeInsets.symmetric(
          vertical: 2.0.h,
          horizontal: 14,
        ),
        child: SingleChildScrollView(
          child: Column(
            children: [
              LabelHeader(
                label: "support_screen.support".tr(),
              ),
              SizedBox(
                height: 30,
              ),
              ClipRRect(
                borderRadius: BorderRadius.circular(
                  10,
                ),
                child: Image.asset(
                  'assets/images/gifs/support.gif',
                ),
              ),
              SizedBox(
                height: 6.h,
              ),
              Text(
                'Dangal Games is proud to have a reliable and dedicated service team working for you. Whenever you feel stuck, or in need of a quick solution to your query, you can contact us via the below given Dangal Games email address, and we will get back to you as soon as possible.',
                style: TextStyle(
                  color: ColorConstants.greyNote,
                  fontWeight: FontWeight.w500,
                  fontSize: 14,
                ),
              ),
              SizedBox(
                height: 7.h,
              ),
              Container(
                decoration: BoxDecoration(
                  gradient: ColorConstants.greySupportButton,
                  borderRadius: BorderRadius.all(
                    Radius.circular(
                      10,
                    ),
                  ),
                  border: Border.all(
                    color: ColorConstants.golderBorder,
                  ),
                ),
                child: ElevatedButton(
                  onPressed: () async {
                    var connectivityResult =
                        await (Connectivity().checkConnectivity());
                    if (connectivityResult == ConnectivityResult.none) {
                      CommonMethods.showSnackBar(
                        context,
                        StringConstants.noInternetConnection,
                      );
                    } else {
                      _launchURL(email: FlavorInfo.supportEmail);
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    splashFactory: NoSplash.splashFactory,
                    primary: Colors.transparent,
                    shadowColor: Colors.transparent,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        Icons.mail,
                        color: ColorConstants.white,
                      ),
                      SizedBox(
                        width: 8,
                      ),
                      Text(
                        FlavorInfo.supportEmail,
                        style: TextStyle(
                          color: ColorConstants.white,
                          fontWeight: FontWeight.w700,
                          fontSize: 16,
                        ),
                      )
                    ],
                  ),
                ),
              ),
              SizedBox(
                height: 7.h,
              ),
              (isFetched)
                  ? VipContainer(
                      supportContactNumber: supportContactNumber,
                    )
                  : CircularProgressIndicator()
            ],
          ),
        ),
      ),
    );
  }

  _launchURL({
    @required String email,
  }) async {
    final url = Uri.encodeFull(
      'mailto:$email',
    );
    if (await canLaunchUrl(
      Uri.parse(
        url,
      ),
    )) {
      await launchUrl(
        Uri.parse(
          url,
        ),
      );
    } else {
      throw 'Could not launch $url';
    }
  }
}
