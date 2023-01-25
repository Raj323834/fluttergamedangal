import 'dart:ui';

import 'package:flutter/material.dart';

class ColorConstants {
  static const kBackgroundColor = Color(0xFF121212);
  static const greyBottomNavigator = Color(
    0XFF2F2F2F,
  );
  static const greyBackgroundColor = Color(0xFF272727);
  static const kPrimaryColor = Color(0xFFFFBD73);
  static const kSecondaryColor = Color(0xFFFFC000);
  static const white = Color(0xFFFFFFFF);
  static const blue = Colors.blue;
  static const black = Colors.black;
  static const orange = Colors.orange;
  static const greenText = Color(0xFF1DFA4E);
  static const transparent = Colors.transparent;
  static const greyBorderColor = Color(0xFF757575);
  static final greyTextColor = Colors.grey[800];
  static final grey900 = Colors.grey[900];
  static final lightGreyText = Color(0xFF9B9B9B);
  static final purpleBorder = Color(0xFF8100BE);
  static final offWhiteTextColor = Colors.white.withOpacity(0.6);
  static final black54 = Colors.black54;
  static final black87 = Colors.black87;
  static final greyNote = Color(0xFFCCCCCC);
  static final golderBorder = Color(
    0xffC1AE00,
  );
  static final greyContainer = Color(
    0xff363333,
  ).withOpacity(0.65);

  static const Color blue1 = const Color.fromARGB(255, 33, 47, 103);
  static const Color dark1 = const Color.fromARGB(255, 53, 8, 27);
  static const Color grey = const Color.fromARGB(255, 178, 178, 178);

  static const LinearGradient blueDarkGradient = LinearGradient(
    colors: [
      blue1,
      dark1,
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static LinearGradient greenGradient = LinearGradient(
    colors: [
      Colors.green.shade800,
      Colors.green.shade600,
      Colors.green.shade800,
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.4, 0.8],
    tileMode: TileMode.clamp,
  );
  static LinearGradient greyGradient = LinearGradient(
    colors: [
      Color(0xFF3F3C3C),
      Color(0xFF181818),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );
  static LinearGradient greyButtonGradient = LinearGradient(
    colors: [
      Color(0xFFCFCFCF),
      Color(0xFF8F8F8F),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );
  static LinearGradient greySupportButton = LinearGradient(
    colors: [
      Color(0xFF2D2C29),
      Color(0xFF45433D).withOpacity(0.04),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );
  static LinearGradient whiteWhiteGradient = LinearGradient(
    colors: [
      Colors.white,
      Colors.white,
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static LinearGradient grey300Gradient = LinearGradient(
    colors: [
      Colors.grey[300],
      Colors.grey[300],
    ],
    begin: Alignment.centerLeft,
    end: Alignment.centerRight,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static const LinearGradient itemPickerGreyGradient = LinearGradient(
    colors: [
      Color(0xFFCFCFCF),
      Color(0xFF8F8F8F),
    ],
    begin: Alignment.centerLeft,
    end: Alignment.centerRight,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static const LinearGradient goldGradient = LinearGradient(
    colors: [
      Color(0xFFC1AE00),
      Color(0xFF6D6300),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [
      0.0,
      0.8,
    ],
    tileMode: TileMode.clamp,
  );

  static const LinearGradient goldGradientLight = LinearGradient(
    colors: [
      Color(0xFFFFE600),
      Color(0xFF7D7100),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [
      0.0,
      0.8,
    ],
    tileMode: TileMode.clamp,
  );

  static const LinearGradient greyWhiteGradient = LinearGradient(
    colors: [grey, white],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static const LinearGradient verifyBtnGradient = LinearGradient(
    colors: [
      Color(0xFFA8A8A8),
      Color(0xFFA8A8A8),
    ],
    begin: Alignment.topLeft,
    end: Alignment.bottomLeft,
    stops: [0.0, 0.8],
    tileMode: TileMode.clamp,
  );

  static LinearGradient customLinearGradient({Color col1, Color col2}) {
    return LinearGradient(
      colors: [
        col1 ?? Colors.grey[300],
        col2 ?? Colors.grey[300],
      ],
      begin: Alignment.centerLeft,
      end: Alignment.centerRight,
      stops: [0.0, 0.8],
      tileMode: TileMode.clamp,
    );
  }

  static const Color lightGrey = Color(0xFF999999);
  static const Color greyScrollBar = Color(0xFFD9D9D9);
  static const Color mustardYellow = const Color.fromARGB(255, 210, 210, 210);
  static const Color brownishYellow = const Color.fromARGB(255, 160, 128, 67);
  static const Color lightYellow = const Color.fromARGB(255, 254, 236, 140);
  static const Color textYellow = const Color.fromARGB(255, 245, 228, 178);
  static const Color textBrown = const Color.fromARGB(255, 73, 35, 21);

  static const Color kycBackground = Color(0xFFeaeaea);

  static final grey700 = Colors.grey[700];
  static final grey350 = Colors.grey[350];
  static final grey600 = Colors.grey[600];
  static final blue600 = Colors.blue[600];
  static const green = Colors.green;
  static const red = Colors.red;
  static final red900 = Colors.red[900];

  static const viewFileCol = Color(0xFF0094FF);
  static const textFieldBorderCol = Color(0xFF8100BE);
  static const textFieldTextCol = Color(0xFF9B9B9B);

  static const appBarBgCol = Color(0XFF2F2F2F);


  static Color getColor(Set<MaterialState> states, Color color) {
     const Set<MaterialState> interactiveStates = <MaterialState>{
       MaterialState.pressed,
       MaterialState.hovered,
       MaterialState.focused,
     };
     if (states.any(interactiveStates.contains)) {
       return grey;
     }
     return color;
   }
}
