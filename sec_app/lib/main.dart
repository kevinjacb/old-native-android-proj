import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MaterialApp(
    home: Home()
));

class Home extends StatelessWidget {
  const Home({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return  Scaffold(
        appBar: AppBar(
          title: Text('My first app'),
          centerTitle: true,
          backgroundColor: Colors.black12,
        ),
        body: Row(
          children: <Widget>[
            Expanded(
              flex: 3,
              child: Container(
                padding: EdgeInsets.all(30),
                child: Text('1'),
                color: Colors.cyan,
              ),
            ),
            Expanded(
              flex: 2,
              child: Container(
                padding: EdgeInsets.all(30),
                child: Text('2'),
                color: Colors.cyanAccent,
              ),
            ),
            Expanded(
              flex: 1,
              child: Container(
                padding: EdgeInsets.all(30),
                child: Text('3'),
                color: Colors.greenAccent,
              ),
            )
          ],
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () { },
          child: Text('Sup'),
          backgroundColor: Colors.amberAccent,
        )
    );
  }
}
