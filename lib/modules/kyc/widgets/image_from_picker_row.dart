import 'package:flutter/material.dart';

class ImageFromPickerRow extends StatelessWidget {
  const ImageFromPickerRow(
      {@required this.title,
      @required this.iconData,
      @required this.onTap,
      Key key})
      : super(key: key);
  final String title;
  final IconData iconData;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(
        iconData,
      ),
      title: Text(
        title,
      ),
      onTap: onTap,
    );
  }
}
