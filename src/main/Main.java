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

	// TODO: input(char type) funzione di input che automaticamente controlla la
	// correttezza dell tipo di dato

	// dati dell'utente corrente, utili anche per altre classi
	static int U_Privilegio = 0;
	static String U_Name = "";
	static String U_Password = "";
	static int U_ID = 0;

	static Comando comandi[] = { // lista dei comandi possibili
			// new Comando("aggiungiutente", 2, "Aggiunge un utente al database"), // Done:
			// new Comando("rimuoviutente", 2, "Rimuove un utente dal database"), // Done:
			// new Comando("modificautente", 2, "Modifica alcuni paramentri di un utente"),
			// //
			// new Comando("aggiungiclasse", 2, "aggiungi una classe di studenti"), // Done:
			// new Comando("rimuoviclasse", 2, "Cancella una classe di studenti (non
			// cancella gli studenti)"), // Done:
			// new Comando("modificaclasse", 2, "Aggiunge o rimuove specifici studenti da
			// una classe"), //
			// new Comando("aggiungitest", 1, "aggiungi un test in base alle domande ed ai
			// parametri selezionati"), //
			// new Comando("rimuovitest", 1, "Elimina nella sua interezza un test"), //
			// new Comando("modificatest", 1, "Aggiunge o rimuova alcune domande dal test"),
			// //
			new Comando("aggiungidomanda", 1, "aggiungi una domanda con delle risposte"), //
			// new Comando("rimuovidomanda", 1, "Elimina una domanda dal database"), //
			// new Comando("modificadomanda", 1, "Modifica una domanda nel database"), //
			// new Comando("lista", 1, "Mostra la lista di tutti gli utenti nel database"),
			// // Done:
			new Comando("listadomande", 1, "Mostra la lista di tutte le domande con relative risposte"), // Done:
			// new Comando("iniziatest", 0, "Inizia un test dato e permettendo di riposndere
			// a tutte le domande"), //
			// new Comando("valutatest", 0, "Valuta il test appena fatto"), //
			new Comando("esci", 0, "Esci dal programma"), // Done:
			new Comando("?", 0, "Mostra lista dei comandi"), // Done:
			new Comando("aiuto", 0, "Mostra descrizione dei comandi") }; // Done:

	public static void main(String[] args) {
		/*
		 * for (int i = 57; i < 143; i++) {
		 * baseDB.Update("UPDATE `docenti` SET `password`='" + Utente.genPassword()
		 * +"' WHERE `ID`=" + i + ";"); }
		 */

		// numero di tentavi che l'utente dispone, se li esaurisce il programma si
		// chiude
		int tentativiLogin = 4;

		while (true) {

			// controllo i tentativi rimasti
			if (tentativiLogin == 0) {
				System.out.println("Troppi tentativi di login falliti");
				return;
			}

			// gestisco il login con username e passwd
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

		System.out.println();

		// permetto all'utente di gestire i comandi in base al privilegio
		String input;
		Comando command;

		while (true) {
			System.out.print(U_Name + Utente.getPrompt(U_Privilegio) + " "); // aspetta l'input
			input = in.nextLine();

			command = parse(input); // analizza input

			execute(command); // execute command
		}

	}

	/**
	 * Chiede in input username e password per eseguire il login, confronta i dati
	 * ricevuti dal database e definisce i privilegi dell'utente corrente
	 * 
	 * @return
	 */
	public static boolean login() {
		System.out.print("Inserisci il tuo username e la password (admin, Admin)\n--> ");
		String userNameInput = "admin"; // in.nextLine();
		System.out.print("--> ");
		String userPasswdInput = "Admin"; // in.nextLine();

		// dato che uso due tabelle per tipo gli utenti devo controllarle entrambe per
		// eseguire il login
		String tables[] = { "studenti", "docenti" };

		try {

			for (int i = 0; i < tables.length; i++) {

				ResultSet res = baseDB.Query("SELECT * FROM `" + tables[i] + "` WHERE `username`='" + userNameInput
						+ "' AND `password`='" + userPasswdInput + "';");

				// next si posizione sulla prima linea, se il metodo ritorna false significa che
				// non ci sono utenti cin quell'username e quella password, quidi il login è
				// fallito
				if (res.next()) {

					U_Name = userNameInput;
					U_Password = userPasswdInput;
					U_ID = res.getInt("ID");

					// definisco il privilgio dell'utente
					try {
						if (res.getInt("admin") == 1) {
							U_Privilegio = 2;
						} else {
							U_Privilegio = i;
						}

					} catch (SQLException e) {
						U_Privilegio = i;
					}

					// 0 significa che non ha ancora eseguito l'accesso e quindi deve inserire una
					// password propria
					if (res.getInt("hasLoggedOnce") == 0) {

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

		// trovo un comando simile all'input
		for (int i = 0; i < comandi.length; i++) {
			if (comandi[i].getNome().contains(arguments[0].toLowerCase())) {
				current = comandi[i];
				break;
			}
		}

		// tronco l'array omettendo il primo valore
		if (arguments.length > 1) {
			current.setArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
		}

		// caso se non trovo nessun comando simile o se il privilegio è insuffiente
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
	 * Esegue, con routine specifiche, il comando preso in input
	 * 
	 * @param command
	 */
	public static void execute(Comando command) {

		String[] arguments = command.getNome().split(" ");

		switch (arguments[0]) {

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "aggiungiutente":
				Utente.aggiungiUtente();
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
			case "aggiungiclasse":
				Classe.aggiungiClasse();
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "rimuoviclasse":
				Classe.rimuoviClasse();
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "aggiungidomanda":
				Domanda.aggiungiDomanda();
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
			case "listadomande":

				try {
					ResultSet domande;
					ResultSet risposte;

					domande = baseDB.Query("SELECT * FROM `domande`;");

					while (domande.next()) {
						System.out.printf("Domanda: materia, argomento = %s, %s punti=%d testo=%s, \n",
								domande.getString("materia"), domande.getString("id_argomento"),
								domande.getInt("punteggio"), domande.getString("testo"));
						risposte = baseDB
								.Query("SELECT * FROM `risposte` WHERE `id_domanda`=" + domande.getInt("ID") + ";");
						while (risposte.next()) {
							System.out.printf("Risposta: testo=%s, correzione=%s\n", risposte.getString("testo"),
									risposte.getInt("correzione") == 1 ? "giusta" : "sbagliata");
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
			case "aiuto":
				Comando obj = parse(arguments[1]);
				System.out.print(obj.getDescrizione());
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			case "?":
				System.out.println("Lista comandi consentiti:");

				for (int i = 0; i < comandi.length; i++) {
					System.out.println(comandi[i].getNome());
				}
				break;

			/*-----------------------------------------------------------------------------------------------------------------------*/
			default:
				break;
		}

	}

	public static String inputString() {
		return "";
	}

	public static int inputInt() {
		return 0;
	}

}
