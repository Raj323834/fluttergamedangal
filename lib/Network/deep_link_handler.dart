import 'package:flutter/cupertino.dart';

import '../constants/app_constants.dart';
import '../constants/deep_link_addresses.dart';
import '../constants/game_names.dart';
import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../modules/home/home.dart';
import '../modules/kyc/kyc_screen.dart';
import '../utils/shared_pref_service.dart';

class DeepLinkHandler {
  static const _TAG = "DeepLinkHandler";

  static final DeepLinkHandler _instance = DeepLinkHandler._internal();

  factory DeepLinkHandler() {
    return _instance;
  }

  DeepLinkHandler._internal();

  String getDeepLinkAddress({
    @required String data,
  }) {
    String deepLinkAddress = StringConstants.emptyString;
    CommonMethods.printLog(
      _TAG,
      'getDeepLinkAddress data=>$data',
    );
    if (data.contains(
      DeepLinkAddresses.referCode,
    )) {
      String referCode = data.split(
        "refer-code/",
      )[1];
      storeReferCodeInSharedPref(
        referCode,
      );
    } else if (data.contains(
      DeepLinkAddresses.fantasyDangal,
    )) {
      storeLeagueCodeInSharedPref(
        data.split(
          "fantasy-dangal/",
        )[1],
      );
    } else if (data.contains(
      DeepLinkAddresses.kyc,
    )) {
      deepLinkAddress = DeepLinkAddresses.kyc;
    } else if (data.contains(
      DeepLinkAddresses.addCash,
    )) {
      deepLinkAddress = DeepLinkAddresses.addCash;
    } else if (data.contains(
      DeepLinkAddresses.raf,
    )) {
      deepLinkAddress = DeepLinkAddresses.raf;
    } else if (data.contains(
      DeepLinkAddresses.home,
    )) {
      deepLinkAddress = DeepLinkAddresses.home;
    } else if (data.contains(
      DeepLinkAddresses.rummy,
    )) {
      deepLinkAddress = DeepLinkAddresses.rummy;
    } else if (data.contains(
      DeepLinkAddresses.leaderBoard,
    )) {
      deepLinkAddress = DeepLinkAddresses.leaderBoard;
    } else if (data.contains(
      DeepLinkAddresses.changeLang,
    )) {
      deepLinkAddress = DeepLinkAddresses.changeLang;
    } else if (data.contains(
      DeepLinkAddresses.deals,
    )) {
      deepLinkAddress = DeepLinkAddresses.deals;
    }
    else if (FlavorInfo.isPS) {
      deepLinkAddress = StringConstants.emptyString;
    } else {
      if (data.contains(
        DeepLinkAddresses.allGamesLobby,
      )) {
        deepLinkAddress = DeepLinkAddresses.allGamesLobby;
      } else if (data.contains(
        DeepLinkAddresses.ballPool,
      )) {
        deepLinkAddress = DeepLinkAddresses.ballPool;
      } else if (data.contains(
        DeepLinkAddresses.archery,
      )) {
        deepLinkAddress = DeepLinkAddresses.archery;
      } else if (data.contains(
        DeepLinkAddresses.bubbleShooter,
      )) {
        deepLinkAddress = DeepLinkAddresses.bubbleShooter;
      } else if (data.contains(
        DeepLinkAddresses.callbreak,
      )) {
        deepLinkAddress = DeepLinkAddresses.callbreak;
      } else if (data.contains(
        DeepLinkAddresses.candyClash,
      )) {
        deepLinkAddress = DeepLinkAddresses.candyClash;
      } else if (data.contains(
        DeepLinkAddresses.carrom,
      )) {
        deepLinkAddress = DeepLinkAddresses.carrom;
      } else if (data.contains(
        DeepLinkAddresses.fantasy,
      )) {
        deepLinkAddress = DeepLinkAddresses.fantasy;
      } else if (data.contains(
        DeepLinkAddresses.fruitSplit,
      )) {
        deepLinkAddress = DeepLinkAddresses.fruitSplit;
      } else if (data.contains(
        DeepLinkAddresses.knifeHit,
      )) {
        deepLinkAddress = DeepLinkAddresses.knifeHit;
      } else if (data.contains(
        DeepLinkAddresses.ludo,
      )) {
        deepLinkAddress = DeepLinkAddresses.ludo;
      } else if (data.contains(
        DeepLinkAddresses.poker,
      )) {
        deepLinkAddress = DeepLinkAddresses.poker;
      } else if (data.contains(
        DeepLinkAddresses.streetRacer,
      )) {
        deepLinkAddress = DeepLinkAddresses.streetRacer;
      } else if (data.contains(
        DeepLinkAddresses.runner,
      )) {
        deepLinkAddress = DeepLinkAddresses.runner;
      }
    }
    return deepLinkAddress;
  }

  void storeReferCodeInSharedPref(
    String referCode,
  ) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.referralCode,
      referCode,
    );
  }

  void storeLeagueCodeInSharedPref(
    String leagueCode,
  ) async {
    await SharedPrefService.addStringToSharedPref(
      SharedPrefKeys.leagueCode,
      leagueCode,
    );
  }

  Future<void> navigateToLink({
    @required context,
    @required String deepLinkAddress,
    @required String userId,
  }) async {
    CommonMethods.printLog(
      _TAG,
      'navigateToLink deepLinkAddress=>$deepLinkAddress',
    );
    if (deepLinkAddress == null) deepLinkAddress = StringConstants.emptyString;
    switch (deepLinkAddress) {
      case DeepLinkAddresses.kyc:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            true,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage:
                    FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
          Navigator.of(context).push(
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  KycScreen(
                userId: userId,
              ),
            ),
          );
        }
        break;

      case DeepLinkAddresses.addCash:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            true,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: FlavorInfo.isPS
                    ? AppConstants.addCashPS
                    : AppConstants.addCash,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.raf:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: FlavorInfo.isPS
                    ? AppConstants.referAndEarnPS
                    : AppConstants.referAndEarn,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.ballPool:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.pool,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.archery:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.archery,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.bubbleShooter:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.bubbleShooter,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.callbreak:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.callBreak,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.candyClash:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.candyCrush,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.carrom:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.carrom,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.fantasy:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.fantasy,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.fruitSplit:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.fruitSplit,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.knifeHit:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.knifeCut,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.ludo:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.ludo,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.poker:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.poker,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.rummy:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.rummy,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.streetRacer:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.streetRacer,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.runner:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: AppConstants.allGames,
                routeDetail: GameNames.runnerNumberOne,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;

      case DeepLinkAddresses.leaderBoard:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            true,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: FlavorInfo.isPS
                    ? AppConstants.leaderBoardPS
                    : AppConstants.leaderBoard,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
      case DeepLinkAddresses.home:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage:
                    FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
      case DeepLinkAddresses.allGamesLobby:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage: FlavorInfo.isPS
                    ? AppConstants.homePS
                    : AppConstants.allGames,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
      case DeepLinkAddresses.deals:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            true,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage:
                    FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
                routeDetail: DeepLinkAddresses.deals,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
      case DeepLinkAddresses.changeLang:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            true,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage:
                    FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
                routeDetail: DeepLinkAddresses.changeLang,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
      default:
        {
          await SharedPrefService.addBoolToSharedPref(
            SharedPrefKeys.deepLink,
            false,
          );
          Navigator.pushAndRemoveUntil(
            context,
            CupertinoPageRoute(
              builder: (
                BuildContext context,
              ) =>
                  HomeScreen(
                landingPage:
                    FlavorInfo.isPS ? AppConstants.homePS : AppConstants.home,
                routeDetail: StringConstants.emptyString,
                userId: userId,
              ),
            ),
            (route) => false,
          );
        }
        break;
    }
  }
}
