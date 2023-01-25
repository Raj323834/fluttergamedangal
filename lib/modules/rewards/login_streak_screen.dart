import 'package:connectivity/connectivity.dart';
import '../../Network/generate_access_token.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/string_constants.dart';
import '../../modules/rewards/models/login_streak_model.dart';
import '../../modules/rewards/repos/login_streak_repo.dart';
import '../../network_new/constants/response_status.dart';
import 'package:sizer/sizer.dart';

import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/asset_paths.dart';
import '../../constants/color_constants.dart';
import 'package:flutter/material.dart';

class LoginStreakScreen extends StatefulWidget {
  final String userId;

  const LoginStreakScreen({
    Key key,
    @required this.userId,
  }) : super(key: key);

  @override
  State<LoginStreakScreen> createState() => _LoginStreakScreenState();
}

class _LoginStreakScreenState extends State<LoginStreakScreen> {
  int userStreaks = 0;
  int totalStreaks = 0;
  double totalIncentive = 0;
  double maxIncentive = 0;
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
      var repoObj = LoginStreakRepo();
      LoginStreakModel loginStreakModel = await repoObj.fetchLoginStreak(
        userId: widget.userId,
      );
      if (loginStreakModel != null) {
        switch (loginStreakModel.result) {
          case ResponseStatus.success:
            userStreaks = loginStreakModel.userDailyLoginStreak;
            totalStreaks = loginStreakModel.applicableDailyLoginStreak;
            totalIncentive = loginStreakModel.totalIncentiveAmount;
            maxIncentive = loginStreakModel.maxIncentiveAmount;
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              "Server Error. Unable to fetch Streak",
            );
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
        CommonMethods.showSnackBar(context, StringConstants.somethingWentWrong);
      }
    }
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
              //Change Avatar Label
              LabelHeader(
                label: 'Rewards',
              ),
              SizedBox(
                height: 25,
              ),
              Container(
                margin:
                const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
                // height: 100,
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(10.0),
                    color: const Color(0xFF49350C)),
                child: SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        padding:
                        const EdgeInsets.symmetric(horizontal: 12, vertical: 12,),
                        child: Text(
                          'Login Daily for ${totalStreaks>0?'$totalStreaks':'-'} days and get a chance to win upto Rs.${maxIncentive>0?maxIncentive:'-'} with a guaranteed reward of Rs. ${totalIncentive>0?totalIncentive:'-'}',
                          style: textStyle(),
                        ),
                      ),
                      const SizedBox(
                        height: 15,
                      ),
                      Container(
                        padding: const EdgeInsets.only(
                            left: 13, right: 23, bottom: 5),
                        child: Row(
                          mainAxisSize: MainAxisSize.max,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Flexible(
                              child: RichText(
                                text: TextSpan(
                                  text: 'Your Login Streak is ',
                                  style: textStyle(),
                                  children: <TextSpan>[
                                    TextSpan(
                                        text:
                                        userStreaks>1?'$userStreaks days':'$userStreaks day',
                                        style: textStyle(color: Color(0xFF5AE460)))
                                  ],
                                ),
                              ),
                            ),
                            TextButton(
                              style: TextButton.styleFrom(
                                padding: EdgeInsets.zero,
                                minimumSize: const Size(80, 25),
                                backgroundColor: totalStreaks>0 && userStreaks==totalStreaks
                                    ? const Color(0xFF75812C)
                                    : const Color(0xFFBCBEB2),
                              ),
                              onPressed: () {
                                if(totalStreaks>0 && userStreaks==totalStreaks){
                                  CommonMethods.showSnackBar(context, StringConstants.rewardCreditedMsg);
                                }else{
                                  CommonMethods.showSnackBar(context, StringConstants.rewardPendingMsg);
                                }
                              },
                              child: const Text(
                                'CLAIM',
                                style: TextStyle(
                                    fontSize: 15, color: Colors.white),
                              ),
                            ),
                          ],
                        ),
                      )
                    ],
                  ),
                ),
              ),
              const SizedBox(
                height: 41,
              ),
              Container(
                height: 200,
                margin: const EdgeInsets.symmetric(horizontal: 40),
                decoration: BoxDecoration(
                    image: const DecorationImage(
                        image: AssetImage(AssetPaths.rewardBanner),
                        fit: BoxFit.fill),
                    borderRadius: BorderRadius.circular(10.0),
                    color: const Color(0xFF2F2F2F)),
              ),
            ],
          ),
        ),
      ),
    );
  }

  textStyle({Color color = Colors.white}) => TextStyle(
    fontWeight: FontWeight.w400,
    color: color,
    fontSize: 14,
    letterSpacing: 0.8,
    height: 1.3,
  );

}