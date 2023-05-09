import 'package:flutter/material.dart';
import 'package:teste_camera/HomePage.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Imagens',
      theme: ThemeData(

        primarySwatch: Colors.blue,
      ),
      home: HomePage(),
    );
  }
}
