import 'package:data_lists/QuoteCard.dart';
import 'package:flutter/material.dart';
import 'Quotes.dart';

void main() => runApp(MaterialApp(
  home: Quotes(),
));

class Quotes extends StatefulWidget {
  const Quotes({Key? key}) : super(key: key);

  @override
  _QuotesState createState() => _QuotesState();
}

class _QuotesState extends State<Quotes> {

  List <Quote> quotes = [
    Quote(author: 'Dalai Lama', quote: 'The purpose of our lives is to be happy.'),
    Quote(author: 'John Lennon',quote: 'Life is what happens when you’re busy making other plans.'),
    Quote(author: 'Stephen King', quote: 'Get busy living or get busy dying.'),
    Quote(quote: 'You only live once, but if you do it right, once is enough.', author: 'Mae West'),
    Quote(author: 'Thomas A. Edison', quote: 'Many of life’s failures are people who did not realize how close they were to success when they gave up.')
  ];
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Quotes Baby!'),
        centerTitle: true,
        backgroundColor: Colors.blueGrey,
      ),
      body: Column(
        children: quotes.map((e) => QuoteCard(
            quote: e,
          delete: (){
              setState(() {
                quotes.remove(e);
              });
          },
        )).toList(),
      )
    );
  }
}
