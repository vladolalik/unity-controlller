package com.bachelor.networking;

import java.io.Serializable;

public class ImageStorage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4211896143527956950L;
	boolean run;
	
	public ImageStorage(boolean run){
		this.run=run;
	}
	
	public boolean getData(){
		return run;
	}
	
	public void setData(boolean run){
		this.run=run;
	}
	
	
}
