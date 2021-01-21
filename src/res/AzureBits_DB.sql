-- tebelle per gli utenti e classi
CREATE TABLE docenti( 
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	password VARCHAR(50),
	hasLoggedOnce INT DEFAULT 0,
	admin INT DEFAULT 0
);

CREATE TABLE classi(
	id_classe VARCHAR(5) NOT NULL PRIMARY KEY,
	indirizzo VARCHAR(20),
	anno_scolastico VARCHAR(10)
);

CREATE TABLE studenti(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	password VARCHAR(50),
	hasLoggedOnce INT DEFAULT 0,
	id_classe VARCHAR(5),
	FOREIGN KEY (id_classe) REFERENCES classi (id_classe)
);

CREATE TABLE docenti_classi (
	id_docente INT NOT NULL,
	id_classe VARCHAR(5) NOT NULL,
	FOREIGN KEY (id_docente) REFERENCES docenti (ID),
	FOREIGN KEY (id_classe) REFERENCES classi (id_classe)
);

-- tabelle per la gestione dei test

CREATE TABLE argomenti(
	titolo VARCHAR(30) NOT NULL PRIMARY KEY
);

CREATE TABLE domande(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	testo VARCHAR(150),
	img_url VARCHAR(50),
	materia VARCHAR(15),
	punteggio INT,
	tipo INT,
	creatore INT,
	id_argomento VARCHAR(30),
	FOREIGN KEY (creatore) REFERENCES docenti (ID),
	FOREIGN KEY (id_argomento) REFERENCES argomenti (titolo)
);

CREATE TABLE risposte(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	testo VARCHAR(150),
	correzione VARCHAR(10),
	img_url VARCHAR(50),
	id_domanda INT,
	FOREIGN KEY (id_domanda) REFERENCES domande (ID)
);

CREATE TABLE test(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	titolo VARCHAR(30),
	tempo TIME,
	voto_massimo INT,
	attivo INT,
	penalita INT,
	creatore INT,
	FOREIGN KEY (creatore) REFERENCES docenti (ID)
);

CREATE TABLE test_domande(
	id_test INT,
	id_domande INT,
	FOREIGN KEY (id_domande) REFERENCES domande (ID),
	FOREIGN KEY (id_test) REFERENCES test (ID)
);

CREATE TABLE test_argomenti(
	id_test INT,
	id_argomenti VARCHAR(30),
	FOREIGN KEY (id_argomenti) REFERENCES argomenti (titolo),
	FOREIGN KEY (id_test) REFERENCES test (ID)
);

CREATE TABLE studenti_test(
	id_studente INT,
	id_test INT,
	FOREIGN KEY (id_studente) REFERENCES studenti (ID),
	FOREIGN KEY (id_test) REFERENCES test (ID)
);

CREATE DATABASE AzureBits
USE AzureBits
