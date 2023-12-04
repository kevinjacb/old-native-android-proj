import 'package:http/http.dart';
import 'dart:convert';
import 'package:intl/intl.dart';

class WorldTime{

  String url, time = "", location,flag;
  bool isday = false;

  WorldTime({required this.url,required this.location,required this.flag});

  Future<void> worldTime() async{
    Response response = await get(Uri.parse('http://worldtimeapi.org/api/timezone/$url'));
    Map data = jsonDecode(response.body);
    //print(data);
    //print(data['datetime']);
    String datetime = data['datetime'].toString().substring(0,26);
    DateTime now = DateTime.parse(datetime);
    //now = now.add(Duration(hours : int.parse(data['utc_offset'].toString().substring(1,5))));
    isday = now.hour > 6 && now.hour < 19 ? true : false;
    print(datetime);
    time = DateFormat.jm().format(now);
  }
}