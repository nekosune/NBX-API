package com.github.soniex2.nbx.api.nbs;

import java.io.IOException;

import com.github.soniex2.nbx.api.IBlockPlayer;
import com.github.soniex2.nbx.api.stream.LittleEndianDataInputStream;

public class NBSSong {

	private NBSHeader header;
	
	private NBSLayer[] layers;
	private NBSInstrument[] customInstruments=new NBSInstrument[9];
	
	public short pointer=0;
	
	
	public NBSSong(NBSHeader header)
	{
		this.header=header;
		this.layers=new NBSLayer[header.getLayers()];
		for(int i=0;i<this.layers.length;i++)
		{
			this.layers[i]=new NBSLayer(this.header.getTicks());
		}
	}
	public double getTicks()
	{
		return header.getTicks();
	}
	
	public String getLength()
	{
		double x = (double) getTicks() / ((double) header.getTempo() / 100.0);
		x -= 1.0 / ((double) header.getTempo() / 100.0);
		x *= 1000.0;
		int ms = (int) x % 1000;
		x /= 1000.0;
		int s = (int) x % 60;
		x /= 60.0;
		int m = (int) x % 60;
		x /= 60.0;
		int h = (int) x;
		return (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m + ":"
				+ (s < 10 ? "0" : "") + s + ";"
				+ (ms < 100 ? ms < 10 ? "00" : "0" : "") + ms;
	}
	
	public NBSBlock[] GetTick(int tick)
	{
		NBSBlock[] blocks=new NBSBlock[this.header.getLayers()];
		for(int i=0;i<this.header.getLayers();i++)
			blocks[i]=this.layers[i].getTick(tick);
		return blocks;
	}
	
	public void play(IBlockPlayer bp)
	{
		float tempo = header.getTempo() / 100;
		for (; pointer < this.getTicks(); pointer++) {
			NBSBlock[] tick=this.GetTick(pointer);
			for(NBSBlock block:tick)
				if(block!=null)
				{
					bp.play(block);
				}
			try {
				Thread.sleep((long) ((1.0F / tempo) * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
					
		}
	}
	/**
	 * Constructs a new song from stream
	 * @param stream Stream to read from
	 * @return returns a new song
	 * @throws IOException
	 */
	public static NBSSong fromStream(LittleEndianDataInputStream stream) throws IOException
	{
		NBSHeader header=NBSHeader.fromStream(stream);
		NBSSong song=new NBSSong(header);
		short jump=-1;
		short layer=-1;
		int ticks=0;
		jump=stream.readShort();
		while(jump!=0)
		{
			ticks+=jump;
			layer=stream.readShort();
			while(layer!=0)
			{
				byte inst=stream.readByte();
				byte key=stream.readByte();
				song.layers[layer].setTick(new NBSBlock(inst, key), ticks);
				layer=stream.readShort();
			}
			layer=-1;
			jump=stream.readShort();
		}
		
		// Should be done with notes now
		for(int i=0;i<header.getLayers();i++)
		{
			song.layers[i].setName(stream.readASCII());
			song.layers[i].setVolume(stream.readByte());
		}
		byte num=stream.readByte();
		int j=0;
		for(int i=0;i<num;i++)
		{
			
			song.customInstruments[j++]=new NBSInstrument(stream.readASCII(),stream.readASCII(),stream.readByte(),stream.readByte());
			
		}
		return song;
	}
	
	public NBSSong Copy()
	{
		NBSHeader newHeader=this.header.copy();
		NBSSong newSong=new NBSSong(newHeader);
		for(int i=0;i<layers.length;i++)
		{
			NBSLayer layer=layers[i];
			newSong.layers[i]=layer.copy();
		}
		
		for(int i=0;i<customInstruments.length;i++)
		{
			NBSInstrument inst=customInstruments[i];
			if(inst!=null)
			{
				newSong.customInstruments[i]=inst.copy();
			}
		}
		return newSong;
	}
	
}
