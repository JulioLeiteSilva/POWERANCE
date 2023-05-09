import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {


  ImagePicker imagePicker = ImagePicker();
  File? imagemSelecionada;



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Imagens'),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          imagemSelecionada == null
              ? Container()
              : Image.file(imagemSelecionada!),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(onPressed: (){
                pegarImagemGaleria();
              }, child: Text('Foto da crianca fudida'),),
              ElevatedButton(onPressed: (){
                pegarImagemCamera();
                }, child: Text('Camera'),),

            ],
          )
        ],
      ),
    );
  }

  pegarImagemGaleria() async {
    final PickedFile? imagemTemporaria =
        await  imagePicker.getImage(source: ImageSource.gallery);
    if(imagemTemporaria != null){
      imagemSelecionada = File(imagemTemporaria.path);

    }
  }
  pegarImagemCamera() async {
    final PickedFile? imagemTemporaria =
    await  imagePicker.getImage(source: ImageSource.camera);
    if(imagemTemporaria != null){
      imagemSelecionada = File(imagemTemporaria.path);

    }
  }
}

