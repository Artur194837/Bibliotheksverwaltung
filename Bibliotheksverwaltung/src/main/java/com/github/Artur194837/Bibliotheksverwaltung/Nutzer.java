package com.github.Artur194837.Bibliotheksverwaltung;

public class Nutzer {
	private String vorname;
	private String nachname;
	public Nutzer(String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	@Override
	public String toString() {
		return "Nutzer [vorname=" + vorname + ", nachname=" + nachname + "]";
	}
}
