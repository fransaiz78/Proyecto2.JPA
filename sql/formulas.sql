DROP TABLE Elementos cascade constraint;
DROP TABLE Moleculas cascade constraint;
DROP TABLE Composicion cascade constraint;
DROP SEQUENCE moleculasId_SEQ;

CREATE TABLE Elementos (
  simbolo varchar(3),
  nombre varchar(20) UNIQUE NOT NULL,
  pesoAtomico integer NOT NULL,
  PRIMARY KEY(simbolo)
);

CREATE TABLE Moleculas (
  id integer,
  nombre varchar(20) UNIQUE NOT NULL,
  pesoMolecular integer NOT NULL,
  formula varchar(20) UNIQUE NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE Composicion (
  simbolo varchar(3),
  idMolecula integer,
  nroAtomos integer NOT NULL,  
  PRIMARY KEY(simbolo, idMolecula),
  FOREIGN KEY(simbolo) REFERENCES Elementos(simbolo),
  FOREIGN KEY(IdMolecula) REFERENCES Moleculas(id)
);

CREATE SEQUENCE moleculasId_SEQ; 

insert into Elementos(simbolo, nombre, pesoAtomico) values ('H','Hidrogeno', 1);
insert into Elementos(simbolo, nombre, pesoAtomico) values('O','Oxigeno', 18);

--insert into Moleculas(id, nombre, pesoMolecular, formula) values(1, 'Agua', 20, 'H2O');

--insert into Composicion(simbolo, idMolecula, nroAtomos) values('H', 1, 2);
--insert into Composicion(simbolo, idMolecula, nroAtomos) values('O', 1, 1);

insert into Moleculas(id, nombre, pesoMolecular, formula) values(2, 'AguaOxigenada', 38, 'H2O2');

insert into Composicion(simbolo, idMolecula, nroAtomos) values('H', 2, 2);
insert into Composicion(simbolo, idMolecula, nroAtomos) values('O', 2, 2);

--select * from Moleculas
--select * from Composicion

commit;
exit;