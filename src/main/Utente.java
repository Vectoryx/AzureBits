package main;

import java.util.Random;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utente {

	public final static int STUDENTE = 0;
	public final static int PROFESSORE = 1;
	public final static int AMMINISTRATORE = 2;

	public static final String privilegi[] = {"studente", "professore", "amministratore"};
	public static final char prompt[] = { '@', '>', '#' }; // il grado di privilegio è anche riflettuto dal prompt

	private Random randGen = new Random();

	private String userName;
	private int privilegio;
	private int classe;
	private String sezione;
	private String password;

	public Utente(String nome, int privilegio, int classe, String sezione) {
		this.userName = nome;
		this.privilegio = privilegio;
		genPassword();
	}

	/**
	 * Ritorna la password, lo specifico perchè il valore Password è privato
	 * 
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Ritorna l'username, dato che il valore è privato
	 * 
	 * @return l'username
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return il prompt appropriato per privilegio che questo utente ha
	 */
	public char getPrompt() {
		return prompt[this.privilegio];
	}

	/**
	 * Genera una password temporanea randomica di 8 caratteri composta di tre
	 * numeri e 5 lettere Serve come password "placeholder" per il primo accesso
	 * deigli utenti registrati dell'admin
	 */
	private void genPassword() {
		String res = ""; // la password da comporre passo passo

		res += (randGen.nextInt(899) + 100); // tre numeri casuali tra 100 e 999

		char temp = 'a';
		for (int i = 0; i < 5; i++) {
			temp = (char) ('a' + randGen.nextInt(25)); // lettera a caso tra 'a' e 'z'
			res += temp;
		}

		this.password = res; // aggiorna la password dell'utente con quella appena creata
	}

	/**
	 * Controlla se la password data e' valida, quindi comprende lettere maiuscole,
	 * minuscolle, simboli, numeri.
	 * 
	 * @param pass la password da controllare
	 * @return se la password e' valida o no
	 */
	public char validaPassword(String pass) {
		return 0;
		/*
		String SpecialChar = "-.,+;:_^@#?=)(/&%$£!|\n\\";
		char[] passChar = pass.toCharArray();
		boolean[] requirements = new boolean[4];
		// 0 = lettere maiuscole, 1 = lettere minuscole, 2 = numeri, 3 = simboli
		for (int i = 0; i < passChar.length; i++) {
			if (Character.isUpperCase(passChar[i])) { // lettere miuscole
				requirements[0] = true;
			} else if (Character.isLowerCase(passChar[i])) { // lettere minuscole
				requirements[1] = true;
			} else if (Character.isDigit(passChar[i])) { // numeri
				requirements[2] = true;
			} else if (SpecialChar.contains(passChar[i] + "")) { // caratteri speciali
				requirements[3] = true;
			} else {
				return passChar[i];
			}
		}

		for (int i = 0; i < requirements.length; i++) {
			if (requirements[i] != true) {
				return 1; // manca un tipo di carattere nella password
			}
		}

		return 0; // tutto apposto*/
	}

	/**
	 * Semplicemente controlla che la password data corrsiponda a quella di questo
	 * utente
	 * 
	 * @param pass la password da controllare
	 * @return
	 */
	public boolean checkPassword(String pass) { //
		return (pass.equals(this.password));
	}

	/**
	 * Cambia la password con una data dall'utente, chiedendo la conferma.
	 */
	public void cambiaPassword() {


		String nuovaPasswdInputOrg, nuovaPasswdInputCp;

		while (true) {
			System.out.print("Inserisci la nuova password\n-->");
			nuovaPasswdInputOrg = Main.in.nextLine(); // nuova password originale
			System.out.print("Reinserisci la nuova password\n-->");
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
				System.out.print("Il carattere " + exit_code + " non è permesso\n");
			}
		}


		password = nuovaPasswdInputOrg;
	}

	/**
	 * Chiede valore per valore i dati necessari per creare un Utente
	 * 
	 * @return l'utente appena creato
	 */
	public static Utente createUser() {

		System.out.print("Inserisci l'UserName dell'utente che vuoi aggiugere\n--> ");
		String tempUsername = Main.in.nextLine();

		System.out.print("Inserisci il numero della classe dell'utente che vuoi aggiugere\n--> ");
		int tempClasse = Main.in.nextInt();
		Main.in.nextLine(); // nextint non cancella il carattere \n dallo stream

		System.out.print("Inserisci la sezione dell'utente che vuoi aggiugere\n--> ");
		String tempSezione = Main.in.nextLine();

		System.out.print("Inserisci il grado di privilegio dell'utente che vuoi aggiugere\n--> ");
		String tempPriviledge = Main.in.nextLine();

		int priviledge = 0;

		if (tempPriviledge.equals("studente") || tempPriviledge.equals("0")) {
			priviledge = 0;
		} else if (tempPriviledge.equals("professore") || tempPriviledge.equals("1")) {
			priviledge = 1;
		} else if (tempPriviledge.equals("amministratore") || tempPriviledge.equals("2")) {
			priviledge = 2;
		}

		Utente tempUser = new Utente(tempUsername, priviledge, tempClasse, tempSezione);
		

		Main.baseDB.Update("INSERT INTO utenti (username, password, hasLoggedOnce, numero_classe, sezione_classe, privilegio) VALUES ('" + tempUsername + "', '"
				+ tempUser.password + "', 0, " + tempClasse + ", '" + tempSezione + "', " + priviledge + ");");

		return tempUser;

	}

	/**
	 * Chiede in input una parte dell'username dell'utente, cerca tutti gli username
	 * che contengono quel nome, e richiede l'id di chi deve essere cancellato
	 * 
	 */
	public static void deleteUser() {

		System.out.print("Inserisci l'username dell'utente che vuoi cancellare\n--> ");
		String inputUsername = Main.in.nextLine();

		ResultSet res = Main.baseDB
				.Query("SELECT `ID`, `username` FROM `utenti` WHERE `username` LIKE '%" + inputUsername + "%';");
		try {
			int count = 0;
			while (res.next()) {
				System.out.println("id = " + res.getInt("ID") + ", nomeUtente = " + res.getString("username") +
									", classe = " + res.getString("sezione") + "^" + res.getInt("classe"));
				count++;
			}

			System.out.println(count + " utenti trovati con '" + inputUsername + "' presente nel nome.");
			System.out.print("Quale vuoi cancellare? (Digita l'id)\n-->");
			int inputID = Main.in.nextInt();

			Main.baseDB.Update("DELETE FROM `utenti` WHERE `ID`=" + inputID + ";");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
