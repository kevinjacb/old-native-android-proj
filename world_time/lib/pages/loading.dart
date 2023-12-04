import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'dart:convert';
import 'package:world_time/world_time.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';

class Loading extends StatefulWidget {
  const Loading({Key? key}) : super(key: key);

  @override
  _LoadingState createState() => _LoadingState();
}

class _LoadingState extends State<Loading> {

  Future<void> getTime() async{
    WorldTime worldTime = new WorldTime(url: 'Asia/Kolkata',location :'India', flag: "");
    await worldTime.worldTime();
    Navigator.pushReplacementNamed(context, '/home',arguments: {
      'time' : worldTime.time,
      'location' : worldTime.location,
      'isday' : worldTime.isday
    });

    //print('Nice one');
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getTime();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.brown,
      body: Center(
        child: SpinKitFadingCube(
          itemBuilder: (BuildContext context, int index) {
            return DecoratedBox(
              decoration: BoxDecoration(
                color: index.isEven ? Colors.white : Colors.black,
              ),
            );
          },
        ),
      ),
    );
  }
}
