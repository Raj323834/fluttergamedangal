import '../network_new/constants/url_constants.dart';
import 'network_get_request.dart';
import 'network_post_request.dart';

class PokerChipsService {
  static Future<Map<String, dynamic>> getPokerTds(
    String userId,
  ) async {
    Map<String, dynamic> result =
        await NetworkGetRequest.getRequestWithoutQueryParameter(
      "Poker chips TDS",
      UrlConstants.getPokerTdsUrl + userId,
      userId,
    );
    return result;
  }

  static Future<Map<String, dynamic>> pokerToDgTransfer(
    String userId,
  ) async {
    Map<String, dynamic> result = await NetworkPostRequest.putRequestWithAccess(
      {},
      UrlConstants.pokerToDgWalletUrl + userId,
      userId,
    );
    return result;
  }
}
