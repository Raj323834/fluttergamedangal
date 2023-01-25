import '../../../constants/color_constants.dart';
import 'package:flutter/material.dart';

class TimerWidget extends StatelessWidget {
  const TimerWidget({
    Key key,
    @required this.isTimerFinished,
    @required int start,
  })  : _start = start,
        super(key: key);

  final bool isTimerFinished;
  final int _start;

  @override
  Widget build(BuildContext context) {
    return Visibility(
      visible: !isTimerFinished,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            "0" +
                (_start ~/ 60).toString() +
                " : " +
                (_start % 60).toString().padLeft(
                      2,
                      '0',
                    ),
            style: TextStyle(
              color: ColorConstants.white,
              fontSize: 15,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }
}
