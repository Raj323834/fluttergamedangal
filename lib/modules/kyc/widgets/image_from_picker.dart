import 'package:flutter/material.dart';
import '../../../constants/string_constants.dart';
import 'image_from_picker_row.dart';

class ImageFromPicker extends StatelessWidget {
  const ImageFromPicker({
    @required this.onTapGallery,
    @required this.onTapCamera,
    Key key,
  }) : super(key: key);

  final VoidCallback onTapGallery;
  final VoidCallback onTapCamera;

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Container(
        child: Wrap(
          children: <Widget>[
            //Image Picker Item
            ImageFromPickerRow(
              title: StringConstants.photoLibrary,
              iconData: Icons.photo_library,
              onTap: onTapGallery,
            ),
            ImageFromPickerRow(
              title: StringConstants.camera,
              iconData: Icons.photo_camera,
              onTap: onTapCamera,
            )
          ],
        ),
      ),
    );
  }
}
