import 'package:flutter/material.dart';
import 'package:sizer/sizer.dart';

import '../../../common_widgets/assets_icon_button.dart';
import '../../../common_widgets/custom_button.dart';
import '../../../common_widgets/custom_icon_button.dart';

class TextFieldSuffix extends StatelessWidget {
  const TextFieldSuffix({
    Key key,
    this.isTick=false,
    this.isBtn=false,
    this.isInfo=false,
    this.btnText='',
    this.iconData,
    this.onTapBtn,
    this.onTapIconBtn,
    this.onTapTick,
    this.onTapInfo,
  }) : super(key: key);

  final bool isTick;
  final bool isBtn;
  final IconData iconData;
  final bool isInfo;
  final String btnText;
  final VoidCallback onTapTick;
  final VoidCallback onTapBtn;
  final VoidCallback onTapIconBtn;
  final VoidCallback onTapInfo;

  @override
  Widget build(BuildContext context) {
    SizedBox _spaceW = SizedBox(
      width: 0.5.h,
    );
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 1.0, vertical: 5.0,),
      child: Row(
        children: <Widget>[
          if (isTick)
            AssetsIconButton(onTap: onTapTick,path: 'assets/images/green_tick.png',),
          if (isTick && isBtn)
            _spaceW,
          if (isBtn)
          CustomButton(buttonText: btnText, fontSize: 8.0.sp, onTap: onTapBtn,),
          if (isInfo || isBtn)
            _spaceW,
          if (isInfo)
            AssetsIconButton(onTap: onTapInfo,path: 'assets/images/eye_icon.png',),
          if(iconData!=null)
            CustomIconButton(iconData: iconData, onTap: onTapIconBtn,),
          if(iconData!=null)
            _spaceW,
        ],
      ),
    );
  }
}
