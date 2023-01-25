import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:clevertap_plugin/clevertap_plugin.dart';
import 'package:connectivity/connectivity.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_fgbg/flutter_fgbg.dart';
import 'package:flutter_flavor/flutter_flavor.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import '../Model/all_games_model.dart';
import '../Model/logging_model.dart';
import '../Model/store_user_info_helper.dart';
import '../common_models/user_info_dm.dart';
import '../common_repos/fetch_user_info_repo.dart';
import '../constants/methods/common_methods.dart';
import '../constants/methods/flavor_info.dart';
import '../constants/shared_pref_keys.dart';
import '../constants/string_constants.dart';
import '../constants/web_socket_topics.dart';
import '../modules/deals/models/deals_ws_dm.dart';
import '../modules/home/models/home_banners_ws_dm.dart';
import '../network_new/constants/response_status.dart';
import '../network_new/constants/responses_keys.dart';
import '../network_new/constants/url_constants.dart';
import '../utils/shared_pref_service.dart';
import '../utils/singleton.dart';
import 'auth_service.dart';
import 'generate_access_token.dart';

// Application-level global variable to access the WebSockets
WebSocketHelperService sockets = WebSocketHelperService();

class WebSocketHelperService {
  // The WebSocket "open" channel
  WebSocketChannel _channel;
  ObserverList<Function> _listeners = ObserverList<Function>();
  final streamController = StreamController.broadcast();
  StreamSubscription<FGBGType> fgbgSubscription;

  var userId;

  Timer timer;
  Timer fgbgTimer = Timer(
    Duration(
      seconds: 0,
    ),
    () {},
  );

  fgbgSub() {
    if (fgbgSubscription == null) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "Creating a  Background and Foreground subscription",
      );
      fgbgSubscription = FGBGEvents.stream.listen(
        (
          event,
        ) async {
          if (event == FGBGType.background) {
            CommonMethods.printLog(
              StringConstants.emptyString,
              "App in background",
            );
            fgbgTimer.cancel();
            fgbgTimer = Timer(Duration(minutes: 30), () {
              CommonMethods.printLog(
                StringConstants.emptyString,
                "Closing Websocket",
              );
              SystemNavigator.pop();
            });
          } else {
            CommonMethods.printLog(
              StringConstants.emptyString,
              "App in Foreground",
            );
            String websocketMaintenanceReceived =
                await SharedPrefService.getStringValuesFromSharedPref(
                      SharedPrefKeys.websocketMaintenanceReceived,
                    ) ??
                    {"type": "ping"}.toString();
            streamController.add(
              websocketMaintenanceReceived,
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
              {"type": "ping"}.toString(),
            );
            fgbgTimer.cancel();
          }
        },
      );
    } else {
      CommonMethods.printLog(
        StringConstants.emptyString,
        "Re-creating a  Background and Foreground subscription",
      );
      fgbgSubscription.cancel();
      fgbgSubscription = FGBGEvents.stream.listen(
        (
          event,
        ) async {
          if (event == FGBGType.background) {
            CommonMethods.printLog(
              StringConstants.emptyString,
              "App in background",
            );
            fgbgTimer.cancel();
            fgbgTimer = Timer(
              Duration(minutes: 30),
              () {
                CommonMethods.printLog(
                  StringConstants.emptyString,
                  "Closing Websocket",
                );
                SystemNavigator.pop();
              },
            );
          } else {
            CommonMethods.printLog(
              StringConstants.emptyString,
              "App in Foreground",
            );
            String websocketMaintenanceReceived =
                await SharedPrefService.getStringValuesFromSharedPref(
                      SharedPrefKeys.websocketMaintenanceReceived,
                    ) ??
                    {
                      "type": "ping",
                    }.toString();
            streamController.add(
              websocketMaintenanceReceived,
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
              {"type": "ping"}.toString(),
            );
            fgbgTimer.cancel();
          }
        },
      );
    }
  }

  Future<bool> checkInternetConnection() async {
    // CommonMethods.printLog("INTERNET_LOG", "No internet conection");
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      await Future.delayed(
        Duration(
          seconds: 1,
        ),
      );

      return await checkInternetConnection();
    }
    return true;
  }

  Future<void> reconnect() async {
    try {
      await LoggingModel.logging(
        "websocket reconnecting",
        "reconnect",
        DateTime.now().toString(),
        await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.userID,
        ),
      );
      await SharedPrefService.addStringToSharedPref(
        SharedPrefKeys.websocketMaintenanceReceived,
        {"type": "ping"}.toString(),
      );
      if (timer != null) {
        timer.cancel();
      }

      if (!(await checkInternetConnection())) {
        await reset();
        // await reconnect();
      } else {
        CommonMethods.printLog(
          StringConstants.emptyString,
          "Internet is... Alive",
        );

        CommonMethods.printLog(
          StringConstants.emptyString,
          DateTime.now().toString() + " Starting connection attempt...",
        );
        _channel = IOWebSocketChannel.connect(
          FlavorConfig.instance.variables["webSocketUrl"],
          pingInterval: Duration(
            seconds: 3,
          ),
        );
        CommonMethods.printLog(
          StringConstants.emptyString,
          DateTime.now().toString() + " Connection attempt completed.",
        );

        userId = await SharedPrefService.getStringValuesFromSharedPref(
          SharedPrefKeys.userID,
        );

        if (userId != null && userId != StringConstants.emptyString) {
          await getUserInfo(
            userId,
          );
          await sockets.send(
            json.encode(
              {
                "type": WebSocketTopics.initiate,
                "userId": userId,
              },
            ),
          );
        }
        if (timer != null) {
          if (!timer.isActive) {
            timer = Timer.periodic(
              Duration(
                seconds: 3,
              ),
              (
                Timer t,
              ) async {
                await sockets.send(
                  json.encode(
                    {
                      "type": WebSocketTopics.clientPing,
                    },
                  ),
                );
              },
            );
          }
        } else {
          timer = Timer.periodic(
            Duration(
              seconds: 3,
            ),
            (
              Timer t,
            ) async {
              await sockets.send(
                json.encode(
                  {
                    "type": WebSocketTopics.clientPing,
                  },
                ),
              );
            },
          );
        }
        startStreamConnection();
      }
    } catch (e) {
      CommonMethods.printLog(
        StringConstants.emptyString,
        e.toString(),
      );
    }
  }

  startStreamConnection() {
    wserror(err) async {
      CommonMethods.printLog(
        StringConstants.emptyString,
        DateTime.now().toString() + " Connection error: $err",
      );
      _channel = null;

      await reconnect();
    }

    wsDone() {
      reconnect();
    }

    if (_channel.closeCode == null)
      _channel.stream.listen(
        (data) async {
          log(data);
          streamController.add(data);
          var snapBody = jsonDecode(data);
          if (snapBody['type'] == WebSocketTopics.userCashBalance) {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.cashAmount,
              snapBody['deposit'].toString(),
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.withdrawAmount,
              snapBody['withdrawable'].toString(),
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.bonusAmount,
              snapBody['bonus'].toString(),
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.gameChipsAmount,
              snapBody['gameChips'].toString(),
            );
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.pokerChipsAmount,
              snapBody['pokerChips'].toString(),
            );

            CleverTapPlugin.profileSet(
              {
                "Deposit Cash": snapBody['deposit'].toString(),
                "Game Chips": snapBody['gameChips'].toString(),
                "Bonus Cash": snapBody['bonus'].toString(),
                "Withdraw Cash": snapBody['withdrawable'].toString(),
              },
            );
          } else if (snapBody['type'] == WebSocketTopics.userFreeBalance) {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.chipsAmount,
              snapBody['playChips'].toString(),
            );
          } else if (snapBody['type'] == WebSocketTopics.gameOrder) {
            await AllGamesModel.storeGamesOrderInSharedPref(
              snapBody['gameCategories'],
            );
          } else if (snapBody['type'] == WebSocketTopics.maintenanceWarning) {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
              data.toString(),
            );
          } else if (snapBody['type'] == WebSocketTopics.startMaintenance) {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
              data.toString(),
            );
          } else if (snapBody['type'] == WebSocketTopics.endMaintenance) {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.websocketMaintenanceReceived,
              data.toString(),
            );
          } else if (snapBody['type'] == WebSocketTopics.logout) {
            AuthService.loggingOutUser();
            await LoggingModel.logging(
              "websocket initiate",
              "sm-logout received",
              DateTime.now().toString(),
              await SharedPrefService.getStringValuesFromSharedPref(
                SharedPrefKeys.userID,
              ),
            );
            if (await SharedPrefService.getBoolValuesFromSharedPref(
                    'RUMMY_OPENED') ??
                false) {
              const platform = MethodChannel(
                UrlConstants.rummyMethodChannel,
              );
              await platform.invokeMethod(
                "closeRummyDangal",
              );
            }
          } else if (snapBody['type'] == WebSocketTopics.recievedInitiate) {
            await LoggingModel.logging(
              "websocket initiate",
              "sm-initiate-received-ws",
              DateTime.now().toString(),
              await SharedPrefService.getStringValuesFromSharedPref(
                SharedPrefKeys.userID,
              ),
            );
          } else if (snapBody['type'] == 'sm-user-state') {
            await SharedPrefService.addStringToSharedPref(
              SharedPrefKeys.state,
              snapBody['state'],
            );
          } else if (snapBody['type'] == WebSocketTopics.kycStatus) {
            CleverTapPlugin.profileSet(
              {
                "KYC Status": snapBody['idResult'] == ResponsesKeys.APPROVED &&
                    snapBody['addressResult'] == ResponsesKeys.APPROVED
              },
            );
          } else if (snapBody['type'] == 'sm-banner-change' &&
              snapBody['source'] == FlavorInfo.source &&
              snapBody['bannerType'] == 'HOME') {
            if (snapBody['banners'] != null) {
              var homeBannersWSDM = HomeBannerWSDM.fromJson(
                snapBody,
              );
              Singleton().listOfBanners = homeBannersWSDM.banners;
            }
          } else if (snapBody['type'] == 'sm-banner-change' &&
              snapBody['source'] == FlavorInfo.source &&
              snapBody['bannerType'] == 'DEALS') {
            if (snapBody['banners'] != null) {
              var dealsWSDM = DealsWSDM.fromJson(
                snapBody,
              );
              Singleton().listOfDealsBanners = dealsWSDM.banners;
            }
          }
        },
        onDone: wsDone,
        onError: wserror,
        cancelOnError: false,
      );
  }

  // Closes the WebSocket communication
  Future<void> reset() async {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }

    if (_channel != null) {
      if (_channel.closeCode == null) {
        CommonMethods.printLog(
          StringConstants.emptyString,
          "Channel Closed",
        );

        try {
          await _channel.sink.close();
          _channel.changeSink(
            (p0) => null,
          );

          LoggingModel.logging(
            "Closing channel ",
            "Closing channel : ",
            DateTime.now().toString(),
            await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.userID,
            ),
          );
        } catch (e) {
          LoggingModel.logging(
            "Socket exception Channel Close",
            "Socket exception : $e",
            DateTime.now().toString(),
            await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.userID,
            ),
          );
        }
      }
    }
  }

  // Sends a message to the server
  send(
    String message,
  ) async {
    if (message.contains('cm-initiate')) {
      log(
        message,
        level: 2,
      );
    }
    if (_channel != null) {
      if (_channel.sink != null) {
        // debugPrint( DateTime.now().toString() + "    :  " + message);
        try {
          _channel.sink.add(message);
          if (message.contains("cm-initiate")) {
            LoggingModel.logging(
              "websocket initiate",
              "initiate send",
              DateTime.now().toString(),
              await SharedPrefService.getStringValuesFromSharedPref(
                SharedPrefKeys.userID,
              ),
            );
          }
        } catch (e) {
          LoggingModel.logging(
            "Socket exception",
            "Socket exception : $e and $message",
            DateTime.now().toString(),
            await SharedPrefService.getStringValuesFromSharedPref(
              SharedPrefKeys.userID,
            ),
          );
        }
      }
    }
  }

  Future<void> getUserInfo(
    String userId,
  ) async {
    var connectivityResult = await Connectivity().checkConnectivity();
    if (connectivityResult == ConnectivityResult.none) {
      //TO BE ADDED
    } else {
      var repoObj = FetchUserInfoRepo();
      UserInfoDM userInfoDM = await repoObj.fetchUserInfo(
        userId: userId,
      );
      if (userInfoDM != null) {
        switch (userInfoDM.result) {
          case ResponseStatus.success:
            storeUserInfo(userInfoDM);
            storeWalletInfo(
              userInfoDM.walletInfo[0].amount.toString(),
              userInfoDM.walletInfo[1].amount.toString(),
              userInfoDM.playChips.toString(),
              userInfoDM.walletInfo[3].amount.toString(),
              userInfoDM.walletInfo[2].amount.toString(),
              userInfoDM.walletInfo[4].amount.toString(),
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          case ResponseStatus.dbError:
            storeWalletInfo(
              "0.0",
              "0.0",
              "10000.0",
              "0.0",
              "0.0",
              "0.0",
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          case ResponseStatus.tokenExpired:
          case ResponseStatus.tokenParsingFailed:
            bool accessTokenGenerated =
                await GenerateAccessToken.regenerateAccessToken(
              userId,
            );
            if (accessTokenGenerated) {
              await getUserInfo(
                userId,
              );
            }
            break;

          case ResponseStatus.userNotFound:
            storeWalletInfo(
              "0.0",
              "0.0",
              "10000.0",
              "0.0",
              "0.0",
              "0.0",
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          case ResponseStatus.upsNotReachable:
            storeWalletInfo(
              "0.0",
              "0.0",
              "10000.0",
              "0.0",
              "0.0",
              "0.0",
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          case ResponseStatus.walletServiceNotReachable:
            storeWalletInfo(
              "0.0",
              "0.0",
              "10000.0",
              "0.0",
              "0.0",
              "0.0",
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          case ResponseStatus.walletDoesNotExist:
            storeWalletInfo(
              "0.0",
              "0.0",
              "10000.0",
              "0.0",
              "0.0",
              "0.0",
            );
            storingMandatoryInfo(
              userInfoDM,
              false,
              StringConstants.emptyString,
            );
            break;

          default:
        }
      } else {
        Fluttertoast.showToast(
          msg: "Unable to fetch Wallet info",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          backgroundColor: Colors.black54,
          textColor: Colors.white,
          fontSize: 16.0,
        );
      }
    }
  }

  // Adds a callback to be invoked in case of incoming
  // notification
  addListener(Function callback) {
    _listeners.add(callback);
  }

  removeListener(Function callback) {
    _listeners.remove(callback);
  }

  // Callback which is invoked each time that we are receiving
  // a message from the server
  // _onReceptionOfMessageFromServer(message) async {
  //   CommonMethods.printLog(StringConstants.emptyString, "HeartBeat: " + message.toString());
  //   _listeners.forEach((Function callback) {
  //     callback(message);
  //   });
  // }
}
