import '../constants/methods/common_methods.dart';
import '../constants/shared_pref_keys.dart';
import '../utils/shared_pref_service.dart';

class GamesOrderingModel {
  List<String> allGames;
  List<String> sportsGames;
  List<String> cardGames;
  List<String> casualGames;

  GamesOrderingModel.initializeTheGamesOrder(
    List gamesOrder,
  ) {
    allGames = [
      gamesOrder[0]['name'],
      gamesOrder[1]['name'],
      gamesOrder[2]['name']
    ].cast<String>();
    for (var i = 0; i < 3; i++) {
      if (gamesOrder[i]['name'] == 'Sports Games') {
        sportsGames = gamesOrder[i]['games'].cast<String>();
      } else if (gamesOrder[i]['name'] == 'Card Games') {
        cardGames = gamesOrder[i]['games'].cast<String>();
      } else if (gamesOrder[i]['name'] == 'Casual Games') {
        casualGames = gamesOrder[i]['games'].cast<String>();
      }
    }
  }
}

class AllGamesModel {
  static storeGamesOrderInSharedPref(
    List gamesOrder,
  ) async {
    CommonMethods.printLog(
      "GAMES ORDERING",
      gamesOrder.toString(),
    );
    GamesOrderingModel gamesOrderingModel =
        GamesOrderingModel.initializeTheGamesOrder(
      gamesOrder,
    );
    await SharedPrefService.addListToSharedPref(
      SharedPrefKeys.allGames,
      gamesOrderingModel.allGames,
    );
    await SharedPrefService.addListToSharedPref(
      SharedPrefKeys.sportsGames,
      gamesOrderingModel.sportsGames,
    );
    await SharedPrefService.addListToSharedPref(
      SharedPrefKeys.cardGames,
      gamesOrderingModel.cardGames,
    );
    await SharedPrefService.addListToSharedPref(
      SharedPrefKeys.casualGames,
      gamesOrderingModel.casualGames,
    );
  }
}
