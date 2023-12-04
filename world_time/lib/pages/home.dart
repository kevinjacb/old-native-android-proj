import 'package:flutter/material.dart';
import 'dart:convert';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  String data = '';
  Map dataMap = {};

  @override
  Widget build(BuildContext context) {
    dataMap = dataMap.isEmpty ? ModalRoute.of(context)!.settings.arguments as Map<String, Object> : dataMap;
    //dataMap = json.decode(data);
    print(dataMap);

    String state = dataMap['isday'] ? 'day.jpg' : 'night.jpg';
    Color txtColor = dataMap['isday'] ? Colors.black : Colors.blue;

    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
            image: DecorationImage(
          image: AssetImage('lib/assets/$state'),
          fit: BoxFit.cover,
        )),
        child: Padding(
          padding: const EdgeInsets.fromLTRB(0, 50, 0, 0),
          child: Column(
            children: <Widget>[
              Center(
                child: SafeArea(
                    child: TextButton.icon(
                  onPressed: () async{
                    dynamic results = await Navigator.pushNamed(context, '/location');
                    setState(() {
                      dataMap['time'] = results['time'];
                      dataMap['location'] = results['location'];
                      dataMap['isday'] = results['isday'];
                    });
                  },
                  icon: Icon(
                    Icons.edit_location,
                    color: Colors.white,
                  ),
                  label: Text('Choose Location',
                      style: TextStyle(
                        color: Colors.white,
                      )),
                )),
              ),
              SizedBox(
                height: 130,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    dataMap['location'],
                    style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 20,
                        letterSpacing: 5,
                        color: txtColor),
                  ),
                ],
              ),
              SizedBox(
                height: 20,
              ),
              Text(
                dataMap['time'],
                style: TextStyle(
                    fontSize: 30,
                    fontWeight: FontWeight.bold,
                    letterSpacing: 9,
                    color: txtColor),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
