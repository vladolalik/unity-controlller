package com.bachelor.networking;

import java.io.Serializable;

public class DataStorage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4949000136927964987L;
	private String text;
	
	public DataStorage(String text){
		this.text=text;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text=text;
	}
}
