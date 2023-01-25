import 'dart:async';

import 'package:flutter/services.dart';

import '../constants/methods/common_methods.dart';

abstract class Bloc {
  void dispose();
}

class DeepLinkBloc extends Bloc {
  //Event Channel creation
  static const stream = const EventChannel(
    'https.dangalgames.sng.link/events',
  );

  //Method channel creation
  static const platform = const MethodChannel(
    'https.dangalgames.sng.link/channel',
  );

  StreamController<String> _stateController = StreamController();

  Stream<String> get state => _stateController.stream;

  Sink<String> get stateSink => _stateController.sink;

  //Adding the listener into contructor
  DeepLinkBloc() {
    CommonMethods.printLog(
      'DeepLinkBloc',
      'DeepLinkBloc init',
    );
    //Checking application start by deep link
    startUri().then(_onRedirected);
    //Checking broadcast stream, if deep link was clicked in opened appication
    stream.receiveBroadcastStream().listen(
          (d) => _onRedirected(d),
        );
  }

  _onRedirected(String uri) {
    if (uri == null || uri.isEmpty) return;
    // Here can be any uri analysis, checking tokens etc, if it’s necessary
    // Throw deep link URI into the BloC's stream
    CommonMethods.printLog(
      'DeepLinkBloc',
      '_onRedirected=>$uri',
    );
    stateSink.add(uri);
  }

  @override
  void dispose() {
    _stateController.close();
  }

  Future<String> startUri() async {
    try {
      return platform.invokeMethod(
        'initialLink',
      );
    } on PlatformException catch (e) {
      return "Failed to Invoke: '${e.message}'.";
    }
  }
}
