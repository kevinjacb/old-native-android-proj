import 'package:flutter/material.dart';
import 'package:world_time/world_time.dart';

class ChooseLocation extends StatefulWidget {
  const ChooseLocation({Key? key}) : super(key: key);

  @override
  _ChooseLocationState createState() => _ChooseLocationState();
}

class _ChooseLocationState extends State<ChooseLocation> {

  List<WorldTime> locations = [
    WorldTime(url: 'Europe/London', location: 'London', flag: 'uk.png'),
    WorldTime(url: 'Europe/Berlin', location: 'Athens', flag: 'greece.png'),
    WorldTime(url: 'Africa/Cairo', location: 'Cairo', flag: 'egypt.png'),
    WorldTime(url: 'Africa/Nairobi', location: 'Nairobi', flag: 'kenya.png'),
    WorldTime(url: 'America/Chicago', location: 'Chicago', flag: 'usa.png'),
    WorldTime(url: 'America/New_York', location: 'New York', flag: 'usa.png'),
    WorldTime(url: 'Asia/Seoul', location: 'Seoul', flag: 'south_korea.png'),
    WorldTime(url: 'Asia/Jakarta', location: 'Jakarta', flag: 'indonesia.png'),
  ];


  void getData(){
    Future.delayed(Duration(seconds: 3),(){
      print('juli');
    });
  }

  @override
  void initState() {
    super.initState();
    getData();
    print('initstate func ran');
  }

  void getTime(WorldTime worldTime) async{
    await worldTime.worldTime();
    Navigator.pop(context,{
      'time' : worldTime.time,
      'location' : worldTime.location,
      'isday' : worldTime.isday
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Choose the Location'),
        centerTitle: true,
      ),
      body: ListView.builder(
          itemCount: locations.length,
        itemBuilder: (context,index){
            return Padding(
              padding: const EdgeInsets.symmetric(vertical: 2, horizontal: 4),
              child: Card(
                margin: EdgeInsets.all(4),
                child: ListTile(
                  onTap: (){
                    getTime(locations[index]);
                    print(locations[index].location);
                  },
                  leading: CircleAvatar(
                      backgroundImage: AssetImage('images/'+locations[index].flag.toString()),
                  ),
                  title: Text(locations[index].location),
                ),
                color: Colors.grey[200],
              ),
            );
        },
      ),
    );
  }
}
