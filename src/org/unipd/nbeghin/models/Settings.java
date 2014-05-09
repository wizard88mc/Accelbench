package org.unipd.nbeghin.models;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private String sex;
	private String age;
	private String height;
	private String position;
	private String shoes;
	private String action;
	private static Map<String, String> associations = new HashMap<String, String>();
	
	static {
		associations.put("Tasca bassa giacca", "TASCA_GIACCA_BASSA");
		associations.put("Tasca alta giacca", "TASCA_ALTA_GIACCA");
		associations.put("Mano", "MANO");
		associations.put("Telefonata", "TELEFONATA");
		associations.put("Borsa", "BORSA");
		associations.put("Zaino", "ZAINO");
		associations.put("Tasca pantaloni davanti", "TASCA_PANTALONI_DAVANTI");
		associations.put("Tasca pantaloni dietro", "TASCA_PANTALONI_DIETRO");
	}
	
	public Settings(String sex, String age, String height, String shoes, String position) {
		this.sex = sex; this.age = age; this.height = height; this.shoes = shoes;
		this.position = associations.get(position);
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public String getAge() {
		return this.age;
	}
	
	public String getHeight() {
		return this.height;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String getShoes() {
		return this.shoes;
	}
	
	public String getAction() {
		return this.action;
	}
}
