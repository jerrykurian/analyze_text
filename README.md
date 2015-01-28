Objective and scope
1) The project will accept user feedback via a smart phone web app.
2) Analyze the feedback and identify if the sentiment expressed is positive or negative
3) Send back appropriate response expressing satisfaction or regret to the user
4) Attach an m-coupon with the response
5) Point user to an FB app (not included) for sharing,  in case they sent a positive feedback
6) Analyze the feedback further to identify the topic of feedback.
7) Send an alert notification to a "contact" at retailer, in case of negative feedback

Architecture:

The application has 2 aspects
1) Receiving feedback from customers of retailers and analyze them
2) Display a dashboard for the retailers to make sense of the feedback.

The application uses the TypeSafe stack to provide the following functionalities
1) HTTP server to receive feedback
2) Scala language for coding the text analysis logic
3) Actors and Akka for processing the text a-synchronously
4) Akka for scheduling tasks

The code is organized in the play framework style with the following major components
1) Controllers for handing the feedback messages sent in by users. This code will be found in the package "controllers"
2) Text analysis engine library code that will analyze the text message received. This code will be found in the package "engine" 
3) The data model classes can be found in the package "models"
4) The services code that work with the engine library and store data in db via the model classes can be found in the package "services"
5) The UI view templates are in the "views" package

Controllers
The controllers make use of the standard Play controllers. The controller class for creating and listing Feedbacks is Feedback.scala

Engine
The text analysis engine library code has the classes that analyzes text. It does so using class EnglishText. The EnglishText class represents 
a String object as an EnglishText object (assuming the text is in English).
This package defines implicits using Implicits.scala, which implicitly assigns text analysis and transformation functions to all String objects.
The EnglishText class extends certain traits like SmsDictionary and EnglishGrammar which are useful in providing following functionalities
1) Convert all text lingo used in the text message into its corresponding expanded english format.
2) Extract parts of speech from the text, which are further used for finding out the topic and category of conversation

The approach of using implicits makes it very easy to support different languages. We could potentially create classes like SpanishText, ArabicText 
etc, which could be returned by the Implicits.scala after looking at the encoding type of the string object.

The EnglishText class defines a "transformer" method which is a higher order function, which can be used to perform various transformations like
converting text from sms lingo based messages to expanded messages. Converting a text into a representation that contains its parts of speeches.

Services
The services package has two important classes "TextAnalyzer" and "TextHandlerService"
The TextAnalyzer is an actor that initialized extraction of details like text topics and categories, asynchronously
The TextHandlerService is the main ervice class that has the workflow for analyzing the text and storing relevant details in the database.

The services also includes "sentiments.FailedMessageRetryService" that creates a scheduler which re-runs failed feedbacks

The "urlshortener.GoogleShortener" class calls the external Google URL shortener api to get a shortener url to FB app url, which can be 
sent as a response to a positive message

Dependencies
The external dependencies required for the project are mentioned in the DEPENDENCIES file located adjacent to this one

HOW TO
A list of answers for how to start and use the app are mentioned in the HOWTO file located adjacent to this one
