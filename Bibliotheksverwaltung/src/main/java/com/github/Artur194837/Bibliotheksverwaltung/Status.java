package com.github.Artur194837.Bibliotheksverwaltung;

public class Status {
	private boolean geliehen;
	private Nutzer nutzer;
	
	public Status(boolean geliehen, Nutzer nutzer) {
		this.geliehen = geliehen;
		this.nutzer = nutzer;
	}

	public boolean isGeliehen() {
		return geliehen;
	}

	public void setGeliehen(boolean geliehen) {
		this.geliehen = geliehen;
	}

	public Nutzer getNutzer() {
		return nutzer;
	}

	public void setNutzer(Nutzer nutzer) {
		this.nutzer = nutzer;
	}

	@Override
	public String toString() {
		return "Status [geliehen=" + geliehen + ", nutzer=" + nutzer + "]";
	}
	
	
}
