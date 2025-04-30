package com.github.Artur194837.Bibliotheksverwaltung;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Bibliotheksverwaltung {
	private Map<Buch, Status> map = new HashMap<Buch, Status>(); //Hashmap zur Zuordnung von Stati zu Büchern
	
	public Bibliotheksverwaltung() {
		load(); //.json Datei laden
	}
	
	public void buchHinzufuegen(Buch b) {
		if(b.getId() == 0) { //Bedeutung: Keine ID wurde vergeben
			if(map.size() > 0) { //Bücher wund Stati wurden bereits angelegt
				int maxID = 0;
				
				Iterator<Buch> iterator = map.keySet().iterator();
				
				while(iterator.hasNext()) { //Größte vergebene ID finden
					int id = iterator.next().getId();
					
					if(id > maxID)
						maxID = id;
				}
				
				b.setId(maxID + 1); //ID auf die größte ID + 1 setzen
			}
			else
				b.setId(1);
		}
		map.put(b, new Status(false, null)); //Nicht ausgeliehen, da gerade erst hinzugefügt
		save();
	}
	
	public Buch sucheBuch(String titel) {
		int maxUebereinstimmendeZeichen = 0;
		Buch maxBuch = null; //Buchtitel mit den meisten übereinstimmenden Zeichen
		
		Iterator<Buch> iterator = map.keySet().iterator();
		
		while(iterator.hasNext()) {
			Buch b = iterator.next();
			int maxUebereinstimmung = 0;
			for(int k = 0; k + titel.length() - 1 < b.getTitel().length(); k++) { //Startindex im Buch ab dem verglichen wird
				int j = 0; //Index im angegebenen Titel
				
				int anzahlUebereinstimmend = 0;
				
				while(j < titel.length() && j + k < b.getTitel().length()) { //Verhindert das auf einen Index außerhalb des Buchtitels zugegriffen wird
					if(titel.charAt(j) == b.getTitel().charAt(k + j))
						anzahlUebereinstimmend++;
				
					j++;
				}
				
				if(anzahlUebereinstimmend > maxUebereinstimmung)
					maxUebereinstimmung = anzahlUebereinstimmend;	
			}
			if(maxUebereinstimmung > maxUebereinstimmendeZeichen) {
				maxUebereinstimmendeZeichen = maxUebereinstimmung;
				maxBuch = b;
			}
		}
		
		return maxBuch;
	}
	
	public Optional<Buch> ausleihen(int id, Nutzer n) throws BuchBereitsAusgeliehenException {
		Iterator<Entry <Buch, Status>> iterator = map.entrySet().iterator();
		
		Buch gefundenesBuch = null;
		
		while(iterator.hasNext()) {
			Entry<Buch, Status> entry = iterator.next();
			
			if(entry.getKey().getId() == id) {
				Status status = entry.getValue();
				if(status.isGeliehen())
					throw new BuchBereitsAusgeliehenException("Das Buch wurde bereits ausgeliehen");
				status.setGeliehen(true);
				status.setNutzer(n);
				
				gefundenesBuch = entry.getKey();
			}
		}
		
		save();
		
		if(gefundenesBuch != null) 
			return Optional.of(gefundenesBuch);
		else
			return Optional.empty();
	}
	
	public void zurueckgeben(int id) {
		Iterator<Entry <Buch, Status>> iterator = map.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<Buch, Status> entry = iterator.next();
			
			if(entry.getKey().getId() == id) {
				entry.getValue().setGeliehen(false);
				entry.getValue().setNutzer(null);
			}
		}
		
		save();
	}

	public Map<Buch, Status> getMap() {
		return map;
	}

	public void setMap(Map<Buch, Status> map) {
		this.map = map;
	}

	public void save() {
		JSONObject jsonObject = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		
		Iterator<Entry<Buch, Status>> iterator = map.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<Buch, Status> entry = iterator.next();
			
			Buch b = entry.getKey();
			Status s = entry.getValue();

			JSONObject parent = new JSONObject();
			JSONObject buch = new JSONObject();
			
			buch.put("titel", b.getTitel());
			buch.put("autor", b.getAutor());
			buch.put("isbn", b.getIsbn());
			buch.put("id", b.getId());
			
			parent.put("buch", buch);
			
			JSONObject nutzer = new JSONObject();
			
			if(s.getNutzer() != null) {
				nutzer.put("vorname", s.getNutzer().getVorname());
				nutzer.put("nachname", s.getNutzer().getNachname());
			}
			else {
				nutzer.put("vorname", "null");
				nutzer.put("nachname", "null");
			}
			
			JSONObject status = new JSONObject();
			
			status.put("geliehen", s.isGeliehen());
			status.put("nutzer", nutzer);
			
			parent.put("status", status);
			
			jsonArray.put(parent);
		}
		
		jsonObject.put("save", jsonArray);
		
		try {
			FileWriter fw = new FileWriter("save.json");
			fw.write(jsonObject.toString());
			fw.flush();
			fw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		try (FileReader reader = new FileReader("save.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONArray jsonArray = jsonObject.getJSONArray("save");
            
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonObjectParent = jsonArray.getJSONObject(i);
            	
            	JSONObject jsonObjectBuch = jsonObjectParent.getJSONObject("buch");
            	
            	JSONObject jsonObjectStatus = jsonObjectParent.getJSONObject("status");
            	
            	Buch b = new Buch(jsonObjectBuch.getString("titel"), jsonObjectBuch.getString("autor"), jsonObjectBuch.getString("isbn"), jsonObjectBuch.getInt("id"));
            	
            	Nutzer nutzer = null;
            	
            	if(!jsonObjectStatus.getJSONObject("nutzer").getString("vorname").equals("null") && !jsonObjectStatus.getJSONObject("nutzer").getString("nachname").equals("null"))
            		nutzer = new Nutzer(jsonObjectStatus.getJSONObject("nutzer").getString("vorname"), jsonObjectStatus.getJSONObject("nutzer").getString("nachname"));
            		
            	Status s = new Status(jsonObjectStatus.getBoolean("geliehen"), nutzer);
            	
            	map.put(b, s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
