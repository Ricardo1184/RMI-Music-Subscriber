-----------------------------/--------------------------\------------------------------------
-----------------------------|--REFERENCES FOR CREATOR--|------------------------------------
-----------------------------\--------------------------/------------------------------------

--CONSTRAINT fk_TABLE_Attribute foreign key(local attribute)REFERENCES TABLE(foreign attribute) on delete cascade

--INSERT INTO TABLE VALUES(attribute1,attribute2,attribute3)
--VALUES(attribute1_value,attribute2_value,attribute3_value)


-----------------------------/----------------------------\----------------------------------
-----------------------------|--DROPPING EXISTING TABLES--|----------------------------------
-----------------------------\----------------------------/----------------------------------

drop table ACCOUNT cascade constraints;
drop table ARTIST cascade constraints;
drop table SUBSCRIPTION cascade constraints;


-----------------------------/-------------------\-------------------------------------------
-----------------------------|--CREATING TABLES--|-------------------------------------------
-----------------------------\-------------------/-------------------------------------------

create table ACCOUNT(
Name			VARCHAR2(50) 		primary key
);

create table ARTIST(
Name			VARCHAR2(50)		primary key
);

create table SUBSCRIPTION(
Account_Name	VARCHAR2(50)		,
Artist_Name		VARCHAR2(50)			,


CONSTRAINT fk_SUBSCRIPTION_Account_Name FOREIGN KEY(Account_Name)REFERENCES ACCOUNT(Name) ON DELETE CASCADE,
CONSTRAINT fk_SUBSCRIPTION_Artist_Name FOREIGN KEY(Artist_Name)REFERENCES ARTIST(Name) ON DELETE CASCADE,

primary key(Account_Name,Artist_Name)
);

SAVEPOINT sp_Empty;
-----------------------------------/-------------\-------------------------------------------
-----------------------------------|--TEST DATA--|-------------------------------------------
-----------------------------------\-------------/-------------------------------------------
ROLLBACK sp_Empty;

INSERT INTO ACCOUNT(Name)
VALUES('Rico');
INSERT INTO ACCOUNT(Name)
VALUES('Remy');


INSERT INTO ARTIST(Name)
VALUES('Green Day');
INSERT INTO ARTIST(Name)
VALUES('All Time Low');
INSERT INTO ARTIST(Name)
VALUES('Marco Borsato');


INSERT INTO SUBSCRIPTION(Account_Name,Artist_Name)
VALUES('Rico','Green Day');
INSERT INTO SUBSCRIPTION(Account_Name,Artist_Name)
VALUES('Rico','All Time Low');
INSERT INTO SUBSCRIPTION(Account_Name,Artist_Name)
VALUES('Remy','All Time Low');
INSERT INTO SUBSCRIPTION(Account_Name,Artist_Name)
VALUES('Remy','Marco Borsato');


