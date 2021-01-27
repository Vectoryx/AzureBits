package main;

public class Comando {

	private String nome;
	private int privilegio;
	private String descrizione;
	private String args[];

	public Comando(String nom, int priv, String desc) {
		this.nome = nom;
		this.privilegio = priv;
		this.descrizione = desc;
	}

	public Comando(String nom, int priv, String desc, String[] args) {
		this.nome = nom;
		this.privilegio = priv;
		this.descrizione = desc;
		this.args = args;
	}

	String getNome() {
		return this.nome;
	}

	int getPrivilegio() {
		return this.privilegio;
	}

	String getDescrizione() {
		return this.descrizione;
	}

	void setArguments(String[] args) {
		this.args = args;
	}

	String[] getArguments() {
		return this.args;
	}
}
