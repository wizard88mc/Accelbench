package org.unipd.nbeghin.models;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private String sex;
	private String age;
	private String height;
	private String mode;
	private String shoes;
	private String action;
	private int testData = 0;
	private static Map<String, String> associations = new HashMap<String, String>();
	
	static {
		associations.put("Tasca bassa giacca", "TASCA_GIACCA_BASSA");
		associations.put("Tasca alta giacca", "TASCA_GIACCA_ALTA");
		associations.put("Mano", "MANO");
		associations.put("Telefonata", "TELEFONATA");
		associations.put("Borsa", "BORSA");
		associations.put("Zaino", "ZAINO");
		associations.put("Tasca pantaloni davanti", "TASCA_PANTALONI_DAVANTI");
		associations.put("Tasca pantaloni dietro", "TASCA_PANTALONI_DIETRO");
		associations.put("Con tacco", "TACCO");
		associations.put("Ginniche", "GINNASTICA");
	}
	
	public Settings(String sex, String age, String height, String shoes, 
			String mode, int testData) {
		this.sex = sex; this.age = age; this.height = height; this.shoes = associations.get(shoes);
		this.mode = associations.get(mode); this.testData = testData;
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
	
	public String getMode() {
		return this.mode;
	}
	
	public String getShoes() {
		return this.shoes;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public int getTestData() {
		return this.testData;
	}
}
