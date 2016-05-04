CREATE TABLE Alarms (ID int not null autoincrement, 
		     time int not null, 
		     repeat int not null default 5, 
		     sound int not null default 80,
		     melody varchar(255) not null,
		     vibro int not null default 1);
CREATE TABLE Social (ID int not null autoincrement, 
		     name varchar(64) not null);
CREATE TABLE Public (ID int not null autoincrement, 
		     owner int not null,
		     social int not null);

