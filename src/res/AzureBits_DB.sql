-- tebelle per gli utenti

CREATE TABLE amministratori(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	password VARCHAR(50),
	hasLoggedOnce INT DEFAULT 0
);

CREATE TABLE docenti(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	password VARCHAR(50),
	hasLoggedOnce INT DEFAULT 0
);

CREATE TABLE classi(
	id_classe VARCHAR(5) NOT NULL PRIMARY KEY,
	indirizzo VARCHAR(20),
	anno_scolastico VARCHAR(10)
);

CREATE TABLE studente(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	password VARCHAR(50),
	hasLoggedOnce INT DEFAULT 0,
	id_classe VARCHAR(5),
	FOREIGN KEY (id_classe) REFERENCES classi (id_classe)
);

docenti_classiCREATE TABLE docenti_classi (
	id_docente INT NOT NULL,
	id_classe VARCHAR(5) NOT NULL,
	FOREIGN KEY (id_docente) REFERENCES docenti (ID),
	FOREIGN KEY (id_classe) REFERENCES classi (id_classe)
);

-- tabelle per la gestione dei test

CREATE TABLE risposte(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	testo VARCHAR(20),
	correzzione VARCHAR(10),
	img_url VARCHAR(50)
);

CREATE TABLE domande(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	testo VARCHAR(20),
	img_url VARCHAR(50),
	materia VARCHAR(15),
	punteggio INT,
	tipo INT
);

CREATE TABLE argomenti(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	titolo VARCHAR(30)
);

CREATE TABLE test(
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	titolo VARCHAR(30),
	tempo TIME,
	voto_massimo INT,
	attivo INT,
	penalita INT
);

CREATE TABLE test_domande(
	id_test INT,
	id_domande INT,
	FOREIGN KEY (id_domande) REFERENCES domande (ID),
	FOREIGN KEY (id_test) REFERENCES test (ID)
);

CREATE TABLE test_argomenti(
	id_test INT,
	id_argomenti INT,
	FOREIGN KEY (id_argomenti) REFERENCES argomenti (ID),
	FOREIGN KEY (id_test) REFERENCES test (ID)
);

-- tabella per la gestione dei comandi

CREATE TABLE comandi(
	comando VARCHAR(50) PRIMARY KEY NOT NULL,
	privilegi INT,
	descrizione VARCHAR(50)
);
