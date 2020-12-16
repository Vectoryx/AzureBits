package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	static Utente utenteAttivo;
	static Scanner in = new Scanner(System.in, "UTF-8"); // questo scanner viene utilizzato in tutti il programma perchè
															// se chiudo un'altro scanner in un'altra classe questo da
															// problemi
	static DBOperations baseDB = new DBOperations();

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
		String command;

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

		String tables[] = {"studenti", "docenti", "amministratori" }; // dato che uso tre tabelle per tipo di utente
																		// devo controllrle tutte per eseguire il login

		String query;

		try {

			for (int i = 0; i < tables.length; i++) {

				query = "SELECT * FROM `" + tables[i] + "` WHERE `username`='" + userNameInput + "' AND `password`='"
						+ userPasswdInput + "';";

				ResultSet res = baseDB.Query(query); // eseguo la query

				if (res.next()) {	// next si posizione sulla prima linea, se il metodo ritorna false significa che
									// il login è fallito

					utenteAttivo = new Utente(userNameInput, i);

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

	public static String parse(String userInput) {

		ResultSet res = baseDB.Query("SELECT * FROM `comandi` WHERE `comando` LIKE '%" + userInput + "%';");

		/*
		 * for (int i = 0; i < commands.length; i++) { if
		 * (commands[i].contains(userInput.toLowerCase())) { return commands[i]; } }
		 */

		try {
			if (res.next()) {
				if (res.getInt("privilegio") <= utenteAttivo.getPriviledge()) {
					return res.getString("comando");
				} else {
					System.out.println("Non hai il permesso di eseguire questo comando");
				}
			} else {
				System.out.println(userInput + " non riconosciuto, per una lista di comandi digita ? o aiuto");
				return "none";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "none";

	}

	public static void execute(String command) {

		String[] arguments = command.split(" ");

		switch (arguments[0]) {

			case "aggiungiutente":
				Utente.createUser();
				break;

			case "rimuoviutente":
				Utente.deleteUser();
				break;

			case "lista":

				String tables[] = { "student", "docent", "amministrator" };

				try {
					for (int i = 0; i < tables.length; i++) {
						ResultSet utenti = baseDB.Query("SELECT * FROM `" + tables[i] + "i`;");

						while (utenti.next()) {
							System.out.printf("ID = %s, Username = %s, tipo = " + tables[i] + "e \n",
									utenti.getString("ID"), utenti.getString("username"));
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

			case "aiuto": // TODO: descrivere il comando presente dopo aiuto

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

			default:
				break;
		}

	}

}
