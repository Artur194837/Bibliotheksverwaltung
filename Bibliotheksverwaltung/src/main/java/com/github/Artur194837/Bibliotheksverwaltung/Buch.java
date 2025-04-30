package com.github.Artur194837.Bibliotheksverwaltung;

public class Buch {
	private String titel;
	private String autor;
	private String isbn;
	private int id;
	
	public Buch(String titel, String autor, String isbn, int id) {
		this.titel = titel;
		this.autor = autor;
		this.isbn = isbn;
		this.id = id;
	}

	public Buch(String titel, String autor, String isbn) {
		super();
		this.titel = titel;
		this.autor = autor;
		this.isbn = isbn;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Override
	public String toString() {
		return "Buch [titel=" + titel + ", autor=" + autor + ", isbn=" + isbn + ", id=" + id + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
