package com.github.soniex2.nbx.api.nbs;

public class NBSInstrument {
	
	private String name;
	private String fileName;
	private byte pitch;
	private byte pressKey;
	
	public NBSInstrument()
	{
		
	}
	public NBSInstrument(String name,String fileName,byte pitch,byte pressKey)
	{
		this.name=name;
		this.fileName=fileName;
		this.pitch=pitch;
		this.pressKey=pressKey;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the pitch
	 */
	public byte getPitch() {
		return pitch;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(byte pitch) {
		this.pitch = pitch;
	}
	/**
	 * @return the pressKey
	 */
	public byte getPressKey() {
		return pressKey;
	}
	/**
	 * @param pressKey the pressKey to set
	 */
	public void setPressKey(byte pressKey) {
		this.pressKey = pressKey;
	}
	public NBSInstrument copy() {
		return new NBSInstrument(name, fileName, pitch, pressKey);
	}

}
