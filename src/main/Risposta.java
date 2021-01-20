package main;

public class Risposta {
	
	public static void aggiungiRisposta(int id_domanda, int tipo) {
		String _testo, _t_correzione;
		int _correzione = 0;

		// testo
		System.out.print("inserisci il testo della risposta \n--> ");
		_testo = Main.in.nextLine();

		if (tipo == 1 || tipo == 2) {
			System.out.print("Questa risposta è corretta? (si, no) \n--> ");
			_t_correzione = Main.in.nextLine().toLowerCase();

			if (_t_correzione.equals("si")) {
				_correzione = 1;
			} else {
				_correzione = 0;
			}


		} // altri tipi di domande/risposte

		Main.baseDB.Update("INSERT INTO `risposte` (testo, correzione, id_domanda) VALUES ('" + _testo + "', " + _correzione + ", " + id_domanda + ")");
	}

	public static void rimuoviRisposta() {

	}

	public static void modificaRisposta() {

	}

}
