import 'dart:convert';

import 'package:connectivity/connectivity.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

import '../../Model/firebase_analytics_model.dart';
import '../../Network/generate_access_token.dart';
import '../../Network/web_socket_helper_service.dart';
import '../../common_widgets/custom_app_bar.dart';
import '../../common_widgets/label_header.dart';
import '../../constants/app_constants.dart';
import '../../constants/color_constants.dart';
import '../../constants/methods/common_methods.dart';
import '../../constants/methods/flavor_info.dart';
import '../../constants/string_constants.dart';
import '../../network_new/constants/response_status.dart';
import '../../utils/singleton.dart';
import 'models/deals_dm.dart';
import 'models/deals_ws_dm.dart';
import 'repos/fetch_deals_repo.dart';
import 'widgets/deals_banner.dart';
import 'widgets/tnc_container.dart';

class DealsScreen extends StatefulWidget {
  final void Function(
    int newIndex,
    String code, {
    bool willPop,
  }) changeFromDealsIndex;
  final String userId;

  const DealsScreen({
    Key key,
    @required this.changeFromDealsIndex,
    @required this.userId,
  }) : super(key: key);

  @override
  _DealsScreenState createState() => _DealsScreenState();
}

class _DealsScreenState extends State<DealsScreen> with WidgetsBindingObserver {
  bool knowMore = false;
  int contentIndex = 0;
  bool isFetched = false;
  List<DealsBanners> listOfBanners;

  @override
  void initState() {
    WidgetsBinding.instance.addObserver(this);
    FirebaseAnalyticsModel.analyticsScreenTracking(
      screenName: DEALS_ROUTE,
    );
    if (Singleton().listOfDealsBanners.isEmpty) {
      callApi();
    } else {
      setState(
        () {
          listOfBanners = Singleton().listOfDealsBanners;
          isFetched = true;
        },
      );
    }

    super.initState();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      listOfBanners = Singleton().listOfDealsBanners;
      isFetched = true;
    }
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
      var repoObj = FetchDealsRepo();
      DealsDM dealsDM = await repoObj.fetchBanners(
        userId: widget.userId,
      );
      if (dealsDM != null) {
        switch (dealsDM.result) {
          case ResponseStatus.success:
            Singleton().listOfDealsBanners = dealsDM.banners;
            listOfBanners = Singleton().listOfDealsBanners;
            setState(
              () {
                isFetched = true;
              },
            );
            break;

          case ResponseStatus.dbError:
            CommonMethods.showSnackBar(
              context,
              "Server Error. Unable to fetch banners",
            );
            break;

          case ResponseStatus.notFound:
            CommonMethods.showSnackBar(
              context,
              "No Deals Available",
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
      } else {
        CommonMethods.showSnackBar(context, 'Something Went Wrong');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: PreferredSize(
          preferredSize: Size(
            double.infinity,
            100,
          ),
          child: CustomAppBar(
            from: "HomeScreen",
            userId: widget.userId,
            bgColor: ColorConstants.appBarBgCol,
          ),
        ),
        backgroundColor: ColorConstants.kBackgroundColor,
        body: Container(
          height: Singleton().deviceSize.height,
          padding: EdgeInsets.only(
            top: 20,
            right: 14,
            left: 12,
          ),
          child: StreamBuilder(
              stream: sockets.streamController.stream,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  var snapBody = jsonDecode(snapshot.data);
                  if (snapBody['type'] == 'sm-banner-change' &&
                      snapBody['source'] == FlavorInfo.source) {
                    if (snapBody['banners'] != null &&
                        snapBody['bannerType'] == 'DEALS') {
                      var dealsWSDM = DealsWSDM.fromJson(
                        snapBody,
                      );
                      Singleton().listOfDealsBanners = dealsWSDM.banners;
                      listOfBanners = Singleton().listOfDealsBanners;
                    } else {
                      callApi();
                    }
                  }
                }
                return Column(
                  children: [
                    //Deals For You Header
                    LabelHeader(
                      label: 'deals_screen.deal_for_you'.tr(),
                    ),
                    SizedBox(
                      height: 22,
                    ),
                    //Deals List View
                    Expanded(
                      child: (!isFetched)
                          ? Center(
                              child: CircularProgressIndicator(),
                            )
                          : ListView.builder(
                              shrinkWrap: true,
                              itemCount: listOfBanners.length,
                              itemBuilder: (BuildContext context, int index) =>
                                  DealsBannerWidget(
                                listOfBanners: listOfBanners,
                                index: index,
                                onTap: () async {
                                  var connectivityResult =
                                      await Connectivity().checkConnectivity();
                                  if (connectivityResult ==
                                      ConnectivityResult.none) {
                                    EasyLoading.showToast(
                                      StringConstants.noInternetConnection,
                                    );
                                  } else {
                                    dealNavigate(
                                      navigateTo:
                                          listOfBanners[index].navigateTo,
                                      promoCode: listOfBanners[index].code,
                                    );
                                  }
                                },
                                knowMore: () {
                                  setState(
                                    () {
                                      contentIndex = index;
                                      knowMore = true;
                                    },
                                  );
                                  showDialog(
                                    context: context,
                                    builder: (context) {
                                      return TncContainer(
                                        index: contentIndex,
                                        listOfBanners: listOfBanners,
                                      );
                                    },
                                  );
                                },
                              ),
                            ),
                    ),
                  ],
                );
              }),
        ),
      ),
    );
  }

  //METHODS
  void dealNavigate({
    @required String navigateTo,
    String promoCode = StringConstants.emptyString,
  }) {
    switch (navigateTo) {
      case 'HOME':
        widget.changeFromDealsIndex(
          FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
          StringConstants.emptyString,
          willPop: true,
        );
        break;

      case 'ALL_GAMES':
        widget.changeFromDealsIndex(
          FlavorInfo.isPS ? AppConstants.homePS : AppConstants.allGames,
          StringConstants.emptyString,
          willPop: true,
        );
        break;

      case 'ADD_CASH':
        widget.changeFromDealsIndex(
          FlavorInfo.isPS ? AppConstants.addCashPS : AppConstants.addCash,
          promoCode,
          willPop: true,
        );
        break;

      case 'SHARE':
        widget.changeFromDealsIndex(
          FlavorInfo.isPS
              ? AppConstants.referAndEarnPS
              : AppConstants.referAndEarn,
          StringConstants.emptyString,
          willPop: true,
        );
        break;

      case 'LEADERBOARD':
        widget.changeFromDealsIndex(
          FlavorInfo.isPS
              ? AppConstants.leaderBoardPS
              : AppConstants.leaderBoard,
          StringConstants.emptyString,
          willPop: true,
        );
        break;

      default:
    }
  }
}
