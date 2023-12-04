import 'package:flutter/material.dart';
import 'Quotes.dart';

class QuoteCard extends StatelessWidget {
  Quote quote;
  Function delete;
  QuoteCard({required this.quote,required this.delete});
  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.fromLTRB(16,16,16,0),
      child: Padding(
        padding: EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Text(
              quote.quote,
              style: TextStyle(
                  color: Colors.blueGrey,
                  fontSize: 20,
                  fontWeight: FontWeight.bold
              ),
            ),
            SizedBox(height: 5,),
            Text(
              '-${quote.author}',
              style: TextStyle(
                  color: Colors.blueGrey,
                  //fontSize: ,
                  fontWeight: FontWeight.bold
              ),
            ),
            SizedBox(height: 8.0,),
            TextButton.icon(onPressed: (){delete();}, icon: Icon(Icons.delete) , label: Text('Delete'))
          ],
        ),
      ),
    );
  }
}
