package main;

import java.util.Random;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utente {

	public final static int STUDENTE = 0;
	public final static int PROFESSORE = 1;
	public final static int AMMINISTRATORE = 2;

	private static final char prompt[] = { '@', '>', '#' }; // il grado di privilegio è anche riflettuto dal prompt
	private static final String tabelle[] = { "studenti", "docenti" };
	private static final String campi[][] = { { "username", "password", "hasLoggedOnce", "id_classe" },
			{ "username", "password", "hasLoggedOnce", "admin" } };

	private static Random randGen = new Random();

	public static char getPrompt(int privilegio) {
		return prompt[privilegio];
	}

	/**
	 * Genera una password temporanea randomica di 8 caratteri composta di tre
	 * numeri e 5 lettere Serve come password "placeholder" per il primo accesso
	 * deigli utenti registrati dell'admin
	 */
	public static String genPassword() {
		String res = ""; // la password da comporre passo passo

		res += (randGen.nextInt(899) + 100); // tre numeri casuali tra 100 e 999

		char temp = 'a';
		for (int i = 0; i < 5; i++) {
			temp = (char) ('a' + randGen.nextInt(25)); // lettera a caso tra 'a' e 'z'
			res += temp;
		}

		return res; // aggiorna la password dell'utente con quella appena creata
	}

	/**
	 * Controlla se la password data e' valida, quindi comprende lettere maiuscole,
	 * minuscolle, simboli, numeri.
	 * 
	 * @param pass la password da controllare
	 * @return se la password e' valida o no
	 */
	public static char validaPassword(String pass) {
		return 0;
		/*
		 * String SpecialChar = "-.,+;:_^@#?=)(/&%$£!|\n\\"; char[] passChar =
		 * pass.toCharArray(); boolean[] requirements = new boolean[4]; // 0 = lettere
		 * maiuscole, 1 = lettere minuscole, 2 = numeri, 3 = simboli for (int i = 0; i <
		 * passChar.length; i++) { if (Character.isUpperCase(passChar[i])) { // lettere
		 * miuscole requirements[0] = true; } else if
		 * (Character.isLowerCase(passChar[i])) { // lettere minuscole requirements[1] =
		 * true; } else if (Character.isDigit(passChar[i])) { // numeri requirements[2]
		 * = true; } else if (SpecialChar.contains(passChar[i] + "")) { // caratteri
		 * speciali requirements[3] = true; } else { return passChar[i]; } }
		 * 
		 * for (int i = 0; i < requirements.length; i++) { if (requirements[i] != true)
		 * { return 1; // manca un tipo di carattere nella password } }
		 * 
		 * return 0; // tutto apposto
		 */
	}

	/**
	 * Cambia la password con una data dall'utente, chiedendo la conferma.
	 */
	public static String cambiaPassword() {

		String nuovaPasswdInputOrg, nuovaPasswdInputCp;

		while (true) {
			System.out.print("Inserisci la nuova password\n--> ");
			nuovaPasswdInputOrg = Main.in.nextLine(); // nuova password originale

			System.out.print("Reinserisci la nuova password\n--> ");
			nuovaPasswdInputCp = Main.in.nextLine(); // la stessa password per essere sicuri che sia stata
														// scritta bene

			char exit_code = validaPassword(nuovaPasswdInputOrg);
			if (exit_code == 0) {
				if (nuovaPasswdInputCp.equals(nuovaPasswdInputOrg)) {
					break;
				} else {
					System.out.print("Le due password inserite non corrispondo");
				}
			} else if (exit_code == 1) {

				System.out.print(
						"La password deve contenere almeno una lettere maiscola, una lettera minuscola, un numero, un simbolo speciale,"
								+ " e deve essere lunga almeno 8 caratteri");
			} else {
				System.out.printf("Il carattere %s non è permesso\n", exit_code);
			}
		}

		return nuovaPasswdInputOrg;
	}

	/**
	 * Chiede valore per valore i dati necessari per creare un Utente
	 * 
	 * @return l'utente appena creato
	 */
	public static void aggiungiUtente() {

		System.out.print("Inserisci l'username dell'utente che vuoi aggiugere\n--> ");
		String _Username = Main.in.nextLine();

		System.out.print(
				"Inserisci il grado di privilegio dell'utente che vuoi aggiugere (studente, professore, amministratore)\n--> ");
		String _Priviledge = Main.in.nextLine();

		int priviledge = 0;

		if (_Priviledge.equals("studente") || _Priviledge.equals("0")) {
			priviledge = 0;
		} else if (_Priviledge.equals("professore") || _Priviledge.equals("1")) {
			priviledge = 1;
		} else if (_Priviledge.equals("amministratore") || _Priviledge.equals("2")) {
			priviledge = 2;
		}

		String table;
		if (priviledge == 2) {
			table = "amministratore";
		} else {
			table = tabelle[priviledge];
		}

		String _Password = genPassword();
		Main.baseDB.Update("INSERT INTO " + table + " (username, password, hasLoggedOnce) VALUES ('" + _Username
				+ "', '" + _Password + "', 0);");

		if (priviledge == 0) {

			System.out.println("Inserisci la sezione dell'utente che vuoi aggiugere tra le classi presenti:");

			ResultSet res = Main.baseDB.Query("SELECT `id_classe` FROM classi");

			try {
				while (res.next()) {
					System.out.println(res.getString("id_classe"));
				}
			} catch (SQLException e) {
			}

			System.out.print("--> ");
			String _Sezione = Main.in.nextLine();

			Main.baseDB.Update("UPDATE " + table + " SET id_classe = '" + _Sezione + "' WHERE username = '" + _Username
					+ "' AND password = '" + _Password + "';");
		}

	}

	public static int trovaUtente(String prompt) throws SQLException {
		System.out.print(prompt);
		String _Username = Main.in.nextLine();

		int _N_docenti = 0;
		int _N_studenti = 0;
		ResultSet temp;

		// trovo i docenti con l'username simile
		temp = Main.baseDB.Query("SELECT `ID`, `username` FROM `docenti` WHERE `username` LIKE '%" + _Username + "%';");

		while (temp.next()) {
			System.out.printf("id = %d, nomeUtente = %s\n", temp.getInt("ID"), temp.getString("username"));

			_N_docenti++;
		}

		// per differenziare gli id inevitabilemte ripetuti tra docennti e studenti
		// metto un "offset" a tutti gli id degli studenti, l'offset è l'id maggiore
		// nella tabella docenti
		temp = Main.baseDB.Query("SELECT MAX(ID) as max FROM docenti");
		int max_doc_ID;
		if (temp.next()) {
			max_doc_ID = temp.getInt("max");
		} else {
			max_doc_ID = 0;
		}

		// trovo gli studenti con l'username simile
		temp = Main.baseDB.Query(
				"SELECT `ID`, `username`, `id_classe` FROM `studenti` WHERE `username` LIKE '%" + _Username + "%';");

		while (temp.next()) {
			_N_studenti++;
			System.out.printf("id = %d, nomeUtente = %s, classe = %s\n", (temp.getInt("ID") + max_doc_ID),
					temp.getString("username"), temp.getString("id_classe"));

		}

		System.out.printf("%d utenti trovati con '%s' presente nel nome. ", (_N_docenti + _N_studenti), _Username);
		System.out.print("Quale vuoi cancellare? (Digita l'id)\n-->");
		int inputID = Main.in.nextInt();
		Main.in.nextLine();

		return inputID;

	}

	/**
	 * Chiede in input una parte dell'username dell'utente, cerca tutti gli username
	 * che contengono quel nome, e richiede l'id di chi deve essere cancellato
	 * 
	 */
	public static void rimuoviUtente() {

		try {

			// trovo l'utente richiesto
			int U_ID = trovaUtente("Inserisci l'username dell'utente che vuoi cancellare\n--> ");

			// TODO: ottimizzare, rimuovere la necessità di ricalcolare il massimo dell'id
			// docente
			ResultSet temp = Main.baseDB.Query("SELECT MAX(ID) as max FROM docenti");
			int max_doc_ID;
			if (temp.next()) {
				max_doc_ID = temp.getInt("max");
			} else {
				max_doc_ID = 0;
			}

			// determino in base all'id selezionato quale id effettivo e quale tabella usare
			String tabella = "docenti";
			if (U_ID > max_doc_ID) {
				tabella = "studenti";
				U_ID -= max_doc_ID;
			}

			// cancello utente
			Main.baseDB.Query("DELETE FROM `" + tabella + "` WHERE `ID`=" + U_ID + "");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Modifica un utente
	 * 
	 */
	public static void modificaUtente() {

		try {

			// trovo l'utente richiesto
			int U_ID = trovaUtente("Inserisci l'username dell'utente che vuoi modificare\n--> ");

			// TODO: ottimizzare, rimuovere la necessità di ricalcolare il massimo dell'id
			// docente
			ResultSet temp = Main.baseDB.Query("SELECT MAX(ID) as max FROM docenti");
			int max_doc_ID;
			if (temp.next()) {
				max_doc_ID = temp.getInt("max");
			} else {
				max_doc_ID = 0;
			}

			// determino in base all'id selezionato quale id effettivo e quale tabella usare
			int tabella = 1;
			if (U_ID > max_doc_ID) {
				tabella = 0;
				U_ID -= max_doc_ID;
			}

			// modifico l'utente (username, passwd, hasLoggedOnce, id_classe) modificabili

			temp = Main.baseDB.Query("SELECT * FROM `" + tabelle[tabella] + "` WHERE `ID`=" + U_ID + ";");
			temp.next();
			String U_name = temp.getString("username");

			String campo;
			String valore;
			boolean condition = true;
			while (condition) {
				System.out.printf("Seleziona un campo che vuoi modificare di %s :", U_name);
				for (int i = 0; i < campi[tabella].length; i++) {
					System.out.printf("%s ", campi[tabella][i]);
				}

				System.out.print("(inserisci esci per uscire) \n--> ");
				campo = Main.in.nextLine();

				for (int i = 0; i < campi[tabella].length; i++) {

					if (campi[tabella][i].equals(campo)) {
						System.out.print("Inserisci il nuovo valore\n--> ");
						valore = Main.in.nextLine();
						Main.baseDB.Update("UPDATE `" + tabelle[tabella] + "` SET `" + campi[tabella][i] + "`='"
								+ valore + "' WHERE `ID`=" + U_ID + ";");
					} else if (campo.equals("esci")) {
						condition = false;
						break;
					} else {
						System.out.print("campo non riconosciuto");
					}
				}
			}

		} catch (SQLException e) {

		}

	}

}
