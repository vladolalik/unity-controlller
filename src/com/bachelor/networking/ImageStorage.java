package com.bachelor.networking;

import java.io.Serializable;

public class ImageStorage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4211896143527956950L;
	byte[] data;
	
	public ImageStorage(byte[] data){
		this.data=data;
	}
	
	public byte[] getData(){
		return data;
	}
}
