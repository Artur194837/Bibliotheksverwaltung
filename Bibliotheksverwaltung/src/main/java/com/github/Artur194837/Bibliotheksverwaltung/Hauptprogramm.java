package com.github.Artur194837.Bibliotheksverwaltung;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Hauptprogramm {
	public static String leerzeichenEntfernen(String s) {
		int i = 0;
		
		while(s.charAt(i) == ' ')
			i++;
		
		return s.substring(i, s.length());
	}
	
	public static void main(String [] args) {
		Nutzer nutzer = new Nutzer("Max", "Mustermann"); //Aktuell noch statisch, da noch kein Login implementiert wurde
		
		System.out.println("Eingeloggt als Max Mustermann.");
		System.out.println("Benutzen Sie den Befehl help wenn Sie Hilfe benötigen.");
		
		Bibliotheksverwaltung bibliotheksverwaltung = new Bibliotheksverwaltung();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			String input = sc.nextLine();
			
			String befehl = "";
			
			int i = 0;
			
			while(i < input.length() && input.charAt(i) != ' ') {
				befehl += input.charAt(i);
				i++;
			}
				
			if(i >= input.length())
				i--;
			
			input = input.substring(i + 1, input.length()); //Für den Befehl wird kein StringTokenizer verwendet, da direkt nach jedem Befehl kein Komma kommt
			
			StringTokenizer tokenizer = new StringTokenizer(input, ","); //Strings bei Komma trennen
			
			switch(befehl) {
				case "hinzufügen":
					try {
						String titel = leerzeichenEntfernen(tokenizer.nextToken());
						
						String autor = leerzeichenEntfernen(tokenizer.nextToken());
					
						String isbn = leerzeichenEntfernen(tokenizer.nextToken());
						
						Buch b = new Buch(titel, autor, isbn);
						
						bibliotheksverwaltung.buchHinzufuegen(b);
					}
					catch(NoSuchElementException e) {
						System.out.println("Ein, oder mehrere Parameter wurden vergessen. Die richtige Form ist: add {titel}, {autor}, {isbn}");
					}
					break;
				case "auflisten":
					Iterator<Entry<Buch, Status>> iterator = bibliotheksverwaltung.getMap().entrySet().iterator();
					
					while(iterator.hasNext()) {
						Entry<Buch, Status> entry = iterator.next();
						
						System.out.println(entry.getKey());
						System.out.println(entry.getValue());
						System.out.println();
					}
					break;
				case "ausleihen":
					try {
						int id = Integer.parseInt(tokenizer.nextToken());
						
						Optional<Buch> geliehenesBuch = bibliotheksverwaltung.ausleihen(id, nutzer);
						
						if(geliehenesBuch.isPresent())
							System.out.println(geliehenesBuch.get());
						else
							System.out.println("Die angegebene ID existiert nicht.");
					}
					catch(NoSuchElementException e) {
						System.out.println("Es wurde keine ID angegeben.");
					}
					catch(NumberFormatException e) {
						System.out.println("In der ID sind ungültige Zeichen wie Buchstaben.");
					}
					catch(BuchBereitsAusgeliehenException e) {
						System.out.println(e.getMessage());
					}
					break;
				case "zurueckgeben":
					try {
						int id = Integer.parseInt(tokenizer.nextToken());
						
						bibliotheksverwaltung.zurueckgeben(id);
					}
					catch(NoSuchElementException e) {
						System.out.println("Es wurde keine ID angegeben.");
					}
					catch(NumberFormatException e) {
						System.out.println("In der ID sind ungültige Zeichen wie Buchstaben.");
					}
					break;
				case "suchen":
					try {
						String titel = tokenizer.nextToken();
						
						Buch b = bibliotheksverwaltung.sucheBuch(titel);
						
						System.out.println(b);
					}
					catch(NoSuchElementException e) {
						System.out.println("Es wurde kein Titel angegeben.");
					}
					break;
				default:
					System.out.println("add: fügt ein Buch zur Bibliotheksverwaltung hinzu (z.B. add Faust, Goethe, 123)");
					System.out.println("list: listet alle Bücher auf");
					System.out.println("ausleihen: leiht ein Buch aus (z.B. ausleihen 1)");
					System.out.println("zurückgeben: gibt ein Buch zurück (z.B. zurückgeben 1)");
			}
		}
	}
}
