DROP TABLE IF EXISTS Geocodigo;
DROP TABLE IF EXISTS GeocodigoInverso;
DROP TABLE IF EXISTS CalculoRuta;

CREATE TABLE Geocodigo(
 id int(100),
 destino varchar (30),
 PRIMARY KEY (id));
 
 CREATE TABLE GeocodigoInverso(
 id int(100),
 longitud float,
 latitud float,
 PRIMARY KEY (id));
 
 CREATE TABLE CalculoRuta(
 id int(100),
 origen varchar (30),
 destino varchar(30),
 PRIMARY KEY (id));