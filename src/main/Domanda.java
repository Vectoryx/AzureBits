package main;

import java.sql.ResultSet;
import java.sql.SQLException;
class Domanda {

	public static void aggiungiDomanda() {
		try {

			String _testo, _materia, _argomento;
			int _punteggio, _n_domande, _tipo = 1;

			/*
			 * i tipi di domanda sono: 1 = risposta multipla 2 = vero/falso
			 */
			ResultSet argomenti = Main.baseDB.Query("SELECT * FROM `argomenti`");

			// acquisisco le variabili necessarie

			// materia
			System.out.print("Di che materia e' questa domanda? \n--> ");
			_materia = Main.in.nextLine();

			System.out.println("Elenco degli argomenti presenti nel database, (vengono aggiunti automaticamente)");
			while (argomenti.next()) {
				System.out.println(argomenti.getString("titolo"));
			}

			// argomento
			System.out.print("\nChe argomento tratta questa domanda? \n--> ");
			_argomento = Main.in.nextLine().toLowerCase();

			argomenti = Main.baseDB.Query("SELECT * FROM `argomenti` WHERE `titolo`='" + _argomento + "';");
			if (!argomenti.next()) {
				Main.baseDB.Update("INSERT INTO `argomenti` VALUES ('" + _argomento + "');");
			}

			// punteggio
			System.out.print("Quanti punti vale questa domanda? \n--> ");
			_punteggio = Main.inputInt();

			// testo
			System.out.print("Inserisci il testo della domanda \n--> ");
			_testo = Main.in.nextLine();

			// inserisco le domande

			System.out.print("Quante risposte comprende questa domanda? \n--> ");
			_n_domande = Main.inputInt();

			// inserisco la domanda nel DB
			Main.baseDB.Update("INSERT INTO `domande` (testo, materia, punteggio, tipo, creatore, id_argomento) VALUES ('"
					+ _testo + "','" + _materia + "'," + _punteggio + "," + _tipo + "," + Main.U_ID + ",'" + _argomento
					+ "');");

			// ricavo l'id della domanda appena creata
			ResultSet _ID_D = Main.baseDB.Query("SELECT ID FROM `domande` WHERE testo='" + _testo + "' AND materia='"
					+ _materia + "' AND punteggio=" + _punteggio + " AND tipo=" + _tipo + " AND creatore=" + Main.U_ID
					+ " AND id_argomento='" + _argomento + "';");
			_ID_D.next();
			int id_domanda = _ID_D.getInt("ID");

			for (int i = 0; i < _n_domande; i++) {
				Risposta.aggiungiRisposta(id_domanda, _tipo);
			}

		} catch (SQLException e) {

		}

	}

	public static void rimuoviDomanda() {

	}

	public static void modificaDomanda() {

	}

}
