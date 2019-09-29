For running project you need to create mysql empty schema with name "chat" and import scripts from dump.msql file
That will create db structure and admin user with 
login "test@mail.ru" and password 123456
Please run mvn clean install before running project in order to create mapper classes 
Thank you

For manageing users please go to Users,to view user profile please click on user name
For manageing rooms please go to Rooms
For admin, to manage chat rooms go to Chat management, you will see the list of users with their current room
For chatting go to Home-chat
Admin can clear room content(messages are stored in db and also users will be kicked out) with clear room button
There are a bad word filter when one tries to add  {"badword1", "badword2", "bad", "word"} one of these words he will be kicked out of roomn
