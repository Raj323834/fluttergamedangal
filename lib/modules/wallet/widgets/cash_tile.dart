import 'package:flutter/cupertino.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:sizer/sizer.dart';

import '../../../constants/asset_paths.dart';
import '../../../constants/color_constants.dart';
import '../../../constants/enum.dart';

class CashTile extends StatelessWidget {
  const CashTile({
    Key key,
    @required this.typeOfCash,
    @required this.amount,
    @required this.onTap,
    @required this.buttonText,
    this.buttonIcon,
    @required this.cashType,
  }) : super(key: key);

  final String typeOfCash;
  final dynamic amount;
  final String buttonText;
  final dynamic buttonIcon;
  final CashType cashType;
  final void Function() onTap;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 8.h,
      decoration: BoxDecoration(
        border: Border.all(
          color: ColorConstants.white,
        ),
        borderRadius: BorderRadius.circular(
          5,
        ),
      ),
      child: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: 14,
        ),
        child: Row(
          children: [
            //CashType & Amount
            Expanded(
              flex: 5,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  //CashType
                  Text(
                    typeOfCash,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontWeight: FontWeight.w800,
                      color: ColorConstants.white,
                      fontSize: 11.sp,
                    ),
                  ),
                  SizedBox(
                    height: 2,
                  ),
                  //Amount
                  Text(
                    (cashType == CashType.pokerChip ||
                            cashType == CashType.freeChip)
                        ? amount.toString()
                        : '\u{20B9}' + " " + amount.toString(),
                    style: TextStyle(
                      fontSize: 9.0.sp,
                      color: ColorConstants.white,
                    ),
                  ),
                ],
              ),
            ),
            //Button
            Expanded(
              flex: 3,
              child: Padding(
                padding: const EdgeInsets.symmetric(
                  vertical: 10.0,
                ),
                child: GestureDetector(
                  onTap: onTap,
                  child: Container(
                    decoration: BoxDecoration(
                      gradient: (cashType == CashType.deposit)
                          ? ColorConstants.greenGradient
                          : ColorConstants.greyGradient,
                      borderRadius: BorderRadius.circular(
                        5,
                      ),
                    ),
                    child: Padding(
                      padding: EdgeInsets.symmetric(
                        vertical: 2.0.w,
                        horizontal: 3.0.w,
                      ),
                      child: (cashType == CashType.freeChip)
                          ? Container(
                              child: SvgPicture.asset(
                                AssetPaths.reload,
                              ),
                            )
                          : Row(
                              children: [
                                Center(
                                  child: Text(
                                    buttonText,
                                    overflow: TextOverflow.ellipsis,
                                    style: TextStyle(
                                      fontSize: 10.5.sp,
                                      color: ColorConstants.white,
                                      fontWeight: FontWeight.w800,
                                    ),
                                  ),
                                ),
                                Spacer(),
                                Container(
                                  height: 2.2.h,
                                  child: SvgPicture.asset(
                                    (cashType == CashType.withdraw)
                                        ? AssetPaths.withdraw
                                        : (cashType == CashType.deposit)
                                            ? AssetPaths.add
                                            : (cashType == CashType.gameChip ||
                                                    cashType == CashType.bonus)
                                                ? AssetPaths.info
                                                : (cashType ==
                                                        CashType.pokerChip)
                                                    ? AssetPaths.transfer
                                                    : AssetPaths.reload,
                                  ),
                                ),
                              ],
                            ),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
