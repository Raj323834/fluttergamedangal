import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

import '../../Network/web_socket_helper_service.dart';
import '../../Model/dg_apk_download_model.dart';
import '../../constants/color_constants.dart';
import '../../constants/web_socket_topics.dart';
import '../../utils/singleton.dart';

class PokerWebview extends StatefulWidget {
  final FlutterWebviewPlugin flutterWebviewPlugin;

  const PokerWebview({
    Key key,
    @required this.flutterWebviewPlugin,
  }) : super(key: key);

  // PokerWebview(FlutterWebviewPlugin flutterWebviewPlugin) {
  //   _flutterWebviewPlugin = flutterWebviewPlugin;
  // }

  @override
  State<PokerWebview> createState() => _PokerWebviewState();
}

class _PokerWebviewState extends State<PokerWebview> {
  @override
  void initState() {
    super.initState();
    DgApkDownloadModel.isWebViewOpened = true;
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: WillPopScope(
        onWillPop: () {
          widget.flutterWebviewPlugin.evalJavascript(
            "window.terminateWebView()",
          );
          return Future.delayed(Duration.zero).then(
            (value) => false,
          ); //TO BE CHANGED
        },
        child: Scaffold(
          body: StreamBuilder(
            stream: sockets.streamController.stream,
            builder: (context, snapshot) {
              try {
                if (snapshot.hasData) {
                  var snapBody = jsonDecode(snapshot.data);
                  if (snapBody['type'] == WebSocketTopics.logout) {
                    closingWebviewInstance();
                  }
                }
              } catch (e) {}
              return Container(
                height: Singleton().deviceSize.height,
                width: Singleton().deviceSize.width,
                decoration: BoxDecoration(
                  color: ColorConstants.black,
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    GestureDetector(
                      onTap: () {
                        FocusScope.of(context).unfocus();
                        closingWebviewInstance();
                        Navigator.of(context).pop();
                      },
                      child: Icon(
                        Icons.cancel_rounded,
                        color: Colors.transparent,
                        size: 30,
                      ),
                    ),
                  ],
                ),
              );
            },
          ),
        ),
      ),
    );
  }

  //METHODS
  void closingWebviewInstance() {
    widget.flutterWebviewPlugin.reloadUrl(
      'https://www.google.com/',
    );
    widget.flutterWebviewPlugin.close();
    DgApkDownloadModel.checkUpdate();
  }
}
