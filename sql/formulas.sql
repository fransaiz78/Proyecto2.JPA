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

insert into Moleculas(id, nombre, pesoMolecular, formula) values(moleculasId_SEQ.nextval, 'Agua', 18, 'H2O');

insert into Composicion(simbolo, idMolecula, nroAtomos) values('H', moleculasId_SEQ.currval, 2);
insert into Composicion(simbolo, idMolecula, nroAtomos) values('O', moleculasId_SEQ.currval, 1);

--select * from Moleculas
--select * from Composicion

commit;
exit;