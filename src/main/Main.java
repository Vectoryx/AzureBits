package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

	static Utente utenteAttivo;
	static Scanner in = new Scanner(System.in, "UTF-8"); // questo scanner viene utilizzato in tutti il programma perchè
															// se chiudo un'altro scanner in un'altra classe questo da
															// problemi
	static DBOperations baseDB = new DBOperations();

	static Comando comandi[] = { new Comando("aggiungiutente", 2, "Aggiunge un utente al database"),
			new Comando("rimuoviutente", 2, "Rimuove un utente dal database"),
			new Comando("cancellaclasse", 2, "Cancella una classe di studenti (non cancella gli studenti)"),
			new Comando("creaclasse", 2, "Crea una classe di studenti"),
			new Comando("modificaclasse", 2, "Aggiunge o rimuove specifici studenti da una classe"),
			new Comando("modificautente", 2, "Modifica alcuni paramentri di un utente"),
			new Comando("eliminatest", 1, "Elimina nella sua interezza un test"),
			new Comando("modificatest", 1, "Aggiunge o rimuova alcune domande dal test"),
			new Comando("eliminadomanda", 1, "Elimina una domanda dal database"),
			new Comando("createst", 1, "Crea un test in base alle domande ed ai parametri selezionati"),
			new Comando("creadomanda", 1, "Crea una domanda con delle risposte"),
			new Comando("lista", 1, "Mostra la lista di tutti gli utenti nel database"),
			new Comando("iniziatest", 0, "Inizia un test dato e permettendo di riposndere a tutte le domande"),
			new Comando("esci", 0, "Esci dal programma"), new Comando("?", 0, "Mostra lista dei comandi"),
			new Comando("aiuto", 0, "Mostra descrizione dei comandi"),
			new Comando("valutatest", 0, "Valuta il test appena fatto")};

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
			System.out.print(utenteAttivo.getUserName() + "" + utenteAttivo.getPrompt());// wait for input
			input = in.nextLine();

			command = parse(input); // parse input

			execute(command); // execute command

		}

	}

	public static boolean login() {
		System.out.print("Inserisci il tuo username e la password \n--> ");
		String userNameInput = in.nextLine();
		System.out.print("--> ");
		String userPasswdInput = in.nextLine();

		String tables[] = {"studenti", "docenti"}; // dato che uso due tabelle per tipo di utente
														// devo controllrle entrambe per eseguire il login

		String query; // stringa di supporto

		try {

			for (int i = 0; i < tables.length; i++) {

				query = "SELECT * FROM `" + tables[i] + "` WHERE `username`='" + userNameInput + "' AND `password`='"
						+ userPasswdInput + "';";

				ResultSet res = baseDB.Query(query); // eseguo la query

				if (res.next()) { // next si posizione sulla prima linea, se il metodo ritorna false significa che
									// il login è fallito

					try {
						if (res.getInt("admin") == 1) {
							utenteAttivo = new Utente(userNameInput, 2);
						} else {
							utenteAttivo = new Utente(userNameInput, i);
						}
					} catch (SQLException e) {}

					if (res.getInt("hasLoggedOnce") == 0) { // 0 significa che non ha ancora eseguito l'accesso

						System.out.print("\nE' necessario cambiare la password.\n");
						utenteAttivo.cambiaPassword();

						query = "UPDATE `" + tables[i] + "` SET password='" + utenteAttivo.getPassword() + "' WHERE ID="
								+ res.getInt("ID");
						baseDB.Update(query);
						query = "UPDATE `" + tables[i] + "` SET hasLoggedOnce=1 WHERE ID=" + res.getInt("ID");
						baseDB.Update(query);
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
		} else if (current.getPrivilegio() > utenteAttivo.getPriviledge()) {
			System.out.println("Non hai il permesso di eseguire " + userInput);
			return new Comando("None", 3, "None");
		} else {
			return current;
		}

	}

	public static void execute(Comando command) {

		String[] arguments = command.getNome().split(" ");

		switch (arguments[0]) {

			case "aggiungiutente":
				Utente.createUser();
				break;

			case "rimuoviutente":
				Utente.deleteUser();
				break;

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

							System.out.printf("ID = %s, Username = %s, tipo = %s \n",
								utenti.getString("ID"), utenti.getString("username"), type);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case "esci":
				baseDB.close();
				System.exit(0);
				break;

			case "aiuto":
				Comando obj = parse(arguments[1]);
				System.out.print(obj.getDescrizione());
				break;

			case "?":
				System.out.println("Lista comandi consentiti:");
				ResultSet comandi = baseDB.Query("SELECT * FROM `comandi`;");

				try {
					while (comandi.next()) {
						System.out.printf("Nome: %-20s Privilegio necessario: %s \n", comandi.getString("comando"),
								Utente.privilegi[comandi.getInt("privilegio")]);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
			case "creaclasse":
				String _anno, _indirzzo, _classe;

				System.out.print("Inserisci il numero e la sezione della classe che vuoi aggiungere \n--> ");
				_classe = in.nextLine();

				System.out.print("Inserisci l'indirzzo(informatica, grafica, ...) della classe che vuoi aggiungere \n--> ");
				_indirzzo = in.nextLine();

				System.out.print("Inserisci l'anno della classe che vuoi aggiungere \n--> "); // TODO: chiedere al prof a cosa serve l'anno
				_anno = in.nextLine();

				baseDB.Update("INSERT INTO classi(`id_classe`,`indirizzo`,`anno_scolastico`) VALUES('" + _classe + "','" + _indirzzo + "','" + _anno + "')");

			default:
				break;
		}

	}

}
