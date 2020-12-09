package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	static Utente utenteAttivo;
	static Scanner in = new Scanner(System.in, "UTF-8"); 	// questo scanner viene utilizzato in tutti il programma perchè
															// se chiudo un'altro scanner in un'altra classe questo da
															// problemi
	static DBOperations baseDB = new DBOperations();

	private static String[] commands = { "aggiungiutente", "rimuoviutente", "lista", "esci", "?", "aiuto"};

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

		try {

			ResultSet res = baseDB.Query("SELECT * FROM `utenti` WHERE `username`='" + userNameInput
					+ "' AND `password`='" + userPasswdInput + "';"); // eseguo la query

			if (res.next()) { // next si posizione sulla prima linea, se il metodo ritorna false significa che
								// il login è fallito

				utenteAttivo = new Utente(userNameInput, res.getInt("privilegio"), res.getInt("numero_classe"),
						res.getString("sezione_classe"));

				if (res.getInt("hasLoggedOnce") == 0) {
					System.out.print(
							"\n--ATTENZIONE QUESTO ACCOUNT HA UNA PASSWORD GENERATA AUTOMATICAMENTE, E' NECESSARIA CAMBIARLA IMMEDIATAMENTE\n");
					utenteAttivo.cambiaPassword();
					baseDB.Update("UPDATE utenti SET password='" + utenteAttivo.getPassword() + "' WHERE ID="
							+ res.getInt("ID"));
					baseDB.Update("UPDATE utenti SET hasLoggedOnce=1 WHERE ID=" + res.getInt("ID"));
				}
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String parse(String userInput) {

		for (int i = 0; i < commands.length; i++) {
			if (commands[i].contains(userInput.toLowerCase())) {
				return commands[i];
			}
		}

		System.out.println(userInput + " non riconosciuto, per una lista di comandi digita ? o aiuto");
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
				ResultSet res = baseDB.Query("SELECT * FROM `utenti`;");

				try {
					while (res.next()) {
						System.out.printf("ID = %s, Username = %s, Classe = %d^%s, tipo = %s \n", res.getString("ID"),
								res.getString("username"), res.getInt("numero_classe"), res.getString("sezione_classe"),
								Utente.privilegi[res.getInt("privilegio")]);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case "esci":
				baseDB.close();
				System.exit(0);
				break;

			case "?":
			case "aiuto":
				System.out.println("Lista comandi consentiti:");
				for (int i = 0; i < commands.length; i++) {
					System.out.println(commands[i]);
				}

			default:
				break;
		}

	}

}
