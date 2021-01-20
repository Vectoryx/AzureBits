package main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Classe {

	public static void aggiungiClasse() {
		String _anno_crea, _indirizzo_crea, _classe_crea;

		System.out.print("Inserisci il numero e la sezione della classe che vuoi aggiungere \n--> ");
		_classe_crea = Main.in.nextLine();

		System.out.print("Inserisci l'indirzzo(informatica, grafica, ...) della classe che vuoi aggiungere \n--> ");
		_indirizzo_crea = Main.in.nextLine();

		// TODO: chiedere al profa cosa serve l'anno
		System.out.print("Inserisci l'anno della classe che vuoi aggiungere \n--> ");
		_anno_crea = Main.in.nextLine();

		Main.baseDB.Update("INSERT INTO classi(`id_classe`,`indirizzo`,`anno_scolastico`) VALUES('" + _classe_crea
				+ "','" + _indirizzo_crea + "','" + _anno_crea + "')");

	}

	public static void rimuoviClasse() {

		String _classe_cancella;
		String classe_precedente = "1";
		String classe_corrente = "";
		ResultSet res;

		System.out.println("Classi esistenti");

		try {
			res = Main.baseDB.Query("SELECT * FROM `classi`");

			while (res.next()) {
				classe_corrente = res.getString("id_classe");
				if (classe_corrente.charAt(0) != classe_precedente.charAt(0)) {
					System.out.println();
				}
				System.out.printf("%-6s", classe_corrente);
				classe_precedente = classe_corrente;
			}

					
			System.out.print("\nInserisci il numero e la sezione della classe che vuoi rimuovere (numero^sezione)\n--> ");
			_classe_cancella = Main.in.nextLine();

			Main.baseDB.Update("DELETE FROM `classi` WHERE `id_classe`='" + _classe_cancella + "';");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
