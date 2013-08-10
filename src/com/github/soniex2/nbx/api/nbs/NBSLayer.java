package com.github.soniex2.nbx.api.nbs;

import java.util.Arrays;
import java.util.Iterator;

public class NBSLayer implements Iterable<NBSBlock> {

	private NBSBlock[] ticks;
	private String name;
	private byte volume;
	/**
	 * @return the ticks
	 */
	public NBSBlock[] getTicks() {
		return ticks;
	}

	/**
	 * @param block the block to set
	 * @param tick the tick of the block
	 */
	public void setTick(NBSBlock block,int tick) {
		this.ticks = ticks;
	}
	
	/**
	 * 
	 * @param tick tick to get from
	 * @return the block
	 */
	public NBSBlock getTick(int tick) {
		return ticks[tick];
	}

	/**
	 * @param ticks the ticks to set
	 */
	public void setTicks(NBSBlock[] ticks) {
		this.ticks = ticks;
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
	 * @return the volume
	 */
	public byte getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(byte volume) {
		this.volume = volume;
	}

	public NBSLayer(int ticks)
	{
		this.ticks=new NBSBlock[ticks];
	}

	@Override
	public Iterator<NBSBlock> iterator() {
		// TODO Auto-generated method stub
		return Arrays.asList(ticks).iterator();
	}

	public NBSLayer copy() {
		NBSLayer newLayer=new NBSLayer(this.ticks.length);
		for(int i=0;i<this.ticks.length;i++)
		{
			newLayer.setTick(this.getTick(i).copy(), i);
		}
		newLayer.name=this.name;
		newLayer.volume=this.volume;
		return newLayer;
	}
}
