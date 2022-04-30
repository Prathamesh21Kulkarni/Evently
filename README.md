# Evently

## Inspiration
How many fascinating event posters do you pass by every day, whether on street walls, school walls, or college walls? Have you ever forgotten to remember or record the details of an event? It occurs to us all the time:). Don't worry, we've got you covered. We were wondering how smooth the process would become if we are able to automatically record, remember, notify and alert us with just a click of an image.


## What it does
**Evenlty** is an android application that does the work of automatically remebering, recording, and notifying the user about the event with just a snap of an image of an event poster. The user just needs to take a picture of the poster and Evently will extract all the necessary information such as **Date, Time, Location, Event name** from the poster, In addition, It will also add the given event to google calendar which helps to recall. We have added one input field for inviting people to this event, so that if user wants to share that event with others, he can add emails in that field and google calendar will send notifications to invited people.

## How we built it
As this is the android application, we built it using android studio and java programming language. This project is divided into 2 subparts - one is text extraction from image using OCR and next is filtering that text to add it as an event in google calendar.  We used the **Calendar Intent** in android studio so that the extracted text can be passed to google calendar application. In addition to this, we have take care of misextracted text using the input fileds given in app hence user can put details manually for the misextracted text.

## Challenges we ran into
Filtering of the extracted text is most challenging part in this project. We want the title of an event, starting date and time, ending date and time, location of an event, etc from the extracted text. For this filtering part, we searched for our problems on various platforms and got the solutions using regex in the java. It was the most time consuming task in the whole project work because we want better accuracy.

## Accomplishments that we're proud of
Despite being new with the technologies which we used, we successfully completed the project in the given timeline. We are satisfied with what we have built. Got to learn new framework and 
techstack

## What we learned
We learned about OCR technology and internal working of it. Also we got opportunity to learn about regex string matching patterns and  about the google calendar intent. We got to know about this intent,  how it helps the developers to smoothen the overall event adding work.

## What's next for Evently
we are planning to add some advance features in future like recording the location of the user where photo was clicked and presenting it in some form of map/analytics/statistics. We are
also thinking about advancing the use case of the app like using it for totally different application.
Example- Detect whether the given prodcut has expired or not (cross checking the expiray date).

## Media
<img src = "https://user-images.githubusercontent.com/72391096/166097727-e68dbea8-8a08-41f5-aa27-00c72dc87578.jpeg" width = "300" height = "600"> <img src = "https://user-images.githubusercontent.com/72391096/166097730-5603da58-a1c5-49e9-8900-338b410e1b17.jpeg" width = "300" height = "600">
<img src = "https://user-images.githubusercontent.com/72391096/166097733-51624869-a8f4-49c6-b7c6-5b001f8422de.jpeg" width = "300" height = "600"> <img src = "https://user-images.githubusercontent.com/72391096/166097735-4ee01927-940a-4596-b90c-e9b5f802ee92.jpeg" width = "300" height = "600">
<img src = "https://user-images.githubusercontent.com/72391096/166097738-12994b00-7d67-42a3-b41d-c5a4032b9289.jpeg" width = "300" height = "600">
