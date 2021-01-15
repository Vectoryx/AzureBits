package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

	static Scanner in = new Scanner(System.in, "UTF-8"); // questo scanner viene utilizzato in tutti il programma perchè
															// se chiudo un'altro scanner in un'altra classe questo da
															// problemi
	static DBOperations baseDB = new DBOperations();

	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	static int U_Privilegio = 0;
	static String U_Name = "";
	static String U_Password = "";

	static Comando comandi[] = { // Semplice lista dei comandi possibili
			new Comando("aggiungiutente", 2, "Aggiunge un utente al database"), // Done:
			new Comando("rimuoviutente", 2, "Rimuove un utente dal database"), // Done:
			new Comando("modificautente", 2, "Modifica alcuni paramentri di un utente"), //
			new Comando("creaclasse", 2, "Crea una classe di studenti"), // Done:
			new Comando("cancellaclasse", 2, "Cancella una classe di studenti (non cancella gli studenti)"), // Done:
			new Comando("modificaclasse", 2, "Aggiunge o rimuove specifici studenti da una classe"), //
			new Comando("createst", 1, "Crea un test in base alle domande ed ai parametri selezionati"), //
			new Comando("eliminatest", 1, "Elimina nella sua interezza un test"), //
			new Comando("modificatest", 1, "Aggiunge o rimuova alcune domande dal test"), //
			new Comando("creadomanda", 1, "Crea una domanda con delle risposte"), //
			new Comando("eliminadomanda", 1, "Elimina una domanda dal database"), //
			new Comando("lista", 1, "Mostra la lista di tutti gli utenti nel database"), // Done:
			new Comando("iniziatest", 0, "Inizia un test dato e permettendo di riposndere a tutte le domande"), //
			new Comando("valutatest", 0, "Valuta il test appena fatto"), //
			new Comando("esci", 0, "Esci dal programma"), // Done:
			new Comando("?", 0, "Mostra lista dei comandi"), // Done:
			new Comando("aiuto", 0, "Mostra descrizione dei comandi") }; // Done:

	public static void main(String[] args) {
		int tentativiLogin = 4; // numero di tentavi che l'utente dispone, se li esaurisce il programma si
								// chiude
		while (tentativiLogin >= 0) {
			System.out.printf("(admin, admin) Hai %d tentativi\n\n", tentativiLogin);
			boolean loginSuccess = login();

			if (loginSuccess) {
				System.out.print("Login avvenuto con successo\n");
				break;
			} else {
				System.out.println("Login fallito, credenziali non corrette\n");
				tentativiLogin--;
			}
		}

		// permetto all'utente di gestire i comandi in base al privilegio

		String input;
		Comando command;

		while (true) {
			System.out.print(U_Name + "" + Utente.getPrompt(U_Privilegio));// wait for input
			input = in.nextLine();

			command = parse(input); // parse input

			execute(command); // execute command

		}

	}

	public static boolean login() {
		// System.out.print("Inserisci il tuo username e la password \n--> ");
		String userNameInput = "admin"; // in.nextLine();
		// System.out.print(" --> ");
		String userPasswdInput = "Admin"; // in.nextLine();

		String tables[] = { "studenti", "docenti" }; // dato che uso due tabelle per tipo di utente
														// devo controllrle entrambe per eseguire il login

		String query; // stringa di supporto

		try {

			for (int i = 0; i < tables.length; i++) {

				query = "SELECT * FROM `" + tables[i] + "` WHERE `username`='" + userNameInput + "' AND `password`='"
						+ userPasswdInput + "';";

				ResultSet res = baseDB.Query(query); // eseguo la query

				if (res.next()) { // next si posizione sulla prima linea, se il metodo ritorna false significa che
									// il login è fallito

					U_Name = userNameInput;
					U_Password = userPasswdInput;

					try {
						if (res.getInt("admin") == 1) {
							U_Privilegio = 2;
						} else {
							U_Privilegio = i;
						}
					} catch (SQLException e) {
						U_Privilegio = i;
					}

					if (res.getInt("hasLoggedOnce") == 0) { // 0 significa che non ha ancora eseguito l'accesso

						System.out.print("\nE' necessario cambiare la password.\n");
						U_Password = Utente.cambiaPassword();

						baseDB.Update("UPDATE `" + tables[i] + "` SET password='" + U_Password + "' WHERE ID="
								+ res.getInt("ID"));

						baseDB.Update("UPDATE `" + tables[i] + "` SET hasLoggedOnce=1 WHERE ID=" + res.getInt("ID"));
					}
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Data una stringa restituisce il comando corretto e relativi argomenti,
	 * controlla eventualmente se l'utente ha i privilegi necessari per il comando
	 * 
	 * @param userInput
	 * @return
	 */
	public static Comando parse(String userInput) {

		String[] arguments = userInput.split(" ");

		Comando current = new Comando("None", 3, "None");

		for (int i = 0; i < comandi.length; i++) {
			if (comandi[i].getNome().contains(arguments[0].toLowerCase())) {
				current = comandi[i];
			}
		}

		if (arguments.length > 1) {
			current.setArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
		}

		if (current.getNome().equals("None")) {
			System.out.println(userInput + " non riconosciuto, per una lista di comandi digita ? o aiuto");
			return current;
		} else if (current.getPrivilegio() > U_Privilegio) {
			System.out.println("Non hai il permesso di eseguire " + userInput);
			return new Comando("None", 3, "None");
		} else {
			return current;
		}

	}

	/**
	 * Esegue con, routine specifiche, il comando preso in input
	 * 
	 * @param command
	 */
	public static void execute(Comando command) {

		String[] arguments = command.getNome().split(" ");

		switch (arguments[0]) {

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "aggiungiutente":
				Utente.creaUtente();
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "rimuoviutente":
				Utente.rimuoviUtente();
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "modificautente":
				Utente.modificaUtente();
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "lista":

				String tables[] = { "student", "docent" };

				try {
					String type;
					for (int i = 0; i < tables.length; i++) {
						ResultSet utenti = baseDB.Query("SELECT * FROM `" + tables[i] + "i`;");

						while (utenti.next()) {
							if (tables[i].equals("docent") && utenti.getInt("admin") > 0) {
								type = "amministratore";
							} else {
								type = tables[i] + "e";
							}

							if (i != 0) {
								System.out.printf("ID = %s, Username = %s, tipo = %s \n", utenti.getString("ID"),
										utenti.getString("username"), type);
							} else {
								System.out.printf("ID = %s, Username = %s, tipo = %s classe = %s \n",
										utenti.getString("ID"), utenti.getString("username"), type,
										utenti.getString("id_classe"));
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "esci":
				baseDB.close();
				System.exit(0);
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "creaclasse":
				String _anno_crea, _indirizzo_crea, _classe_crea;

				System.out.print("Inserisci il numero e la sezione della classe che vuoi aggiungere \n--> ");
				_classe_crea = in.nextLine();

				System.out.print(
						"Inserisci l'indirzzo(informatica, grafica, ...) della classe che vuoi aggiungere \n--> ");
				_indirizzo_crea = in.nextLine();

				System.out.print("Inserisci l'anno della classe che vuoi aggiungere \n--> "); // TODO: chiedere al prof
				_anno_crea = in.nextLine(); // a cosa serve l'anno

				baseDB.Update("INSERT INTO classi(`id_classe`,`indirizzo`,`anno_scolastico`) VALUES('" + _classe_crea
						+ "','" + _indirizzo_crea + "','" + _anno_crea + "')");

				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "cancellaclasse":

				String _classe_cancella;

				System.out.print("Inserisci il numero e la sezione della classe che vuoi togliere \n--> ");
				_classe_cancella = in.nextLine();
				/*
				 * System.out.print("Inserisci l'anno della classe che vuoi togliere \n--> ");
				 * _anno_cancella = in.nextLine();
				 */
				baseDB.Update("DELETE FROM `classi` WHERE `id_classe`='" + _classe_cancella + "';");

				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "aiuto":
				Comando obj = parse(arguments[1]);
				System.out.print(obj.getDescrizione());
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "?":
				System.out.println("Lista comandi consentiti:");
				ResultSet comandi = baseDB.Query("SELECT * FROM `comandi`;");

				try {
					while (comandi.next()) {
						System.out.printf("Nome: %-20s Privilegio necessario: %s \n", comandi.getString("comando"),
								Utente.getPrompt(comandi.getInt("privilegio")));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				/*-----------------------------------------------------------------------------------------------------------------------*/
			default:
				break;
		}

	}

}
