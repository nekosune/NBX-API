package com.github.soniex2.nbx.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NBSSong implements Iterable<NBSTick> {

	private ArrayList<NBSTick> song = new ArrayList<>();
	private short layers = 1;
	private String[] layerNames;
	private byte[] layerVolumes;
	private int modCount = 0;

	public NBSSong(short layers) {
		this.layers = layers;
		layerNames = new String[layers];
		layerVolumes = new byte[layers];
		Arrays.fill(layerVolumes, (byte) 64);
	}

	public NBSSong(short ticks, short layers) {
		this(layers);
		song.ensureCapacity(ticks);
	}

	public short getTicks() {
		return (short) song.size();
	}

	public short getLayers() {
		return layers;
	}

	public void addTick(NBSTick tick) {
		addTick(song.size(), tick);
	}

	public void addTick(int index, NBSTick tick) {
		if (tick.getLayers() != layers) {
			throw new IllegalArgumentException(
					"Tick layer count doesn't match song layer count");
		}
		if (index > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Too many ticks!");
		}
		if (song.size() >= Short.MAX_VALUE) {
			throw new IllegalStateException("Too many ticks!");
		}
		song.add(index, tick);
		modCount++;
	}

	public void delTick(short index) {
		song.remove(index);
		modCount++;
	}

	public void resize(short layers) {
		if (layers < 1) {
			throw new IllegalArgumentException(
					"Tick must have at least one layer");
		}
		Iterator<NBSTick> iterator = song.iterator();
		while (iterator.hasNext()) {
			iterator.next().resize(layers);
		}
		String[] newLayerNames = new String[layers];
		System.arraycopy(layerNames, 0, newLayerNames, 0,
				layers < layerNames.length ? layers : layerNames.length);
		layerNames = newLayerNames;
		byte[] newLayerVolumes = new byte[layers];
		System.arraycopy(layerVolumes, 0, newLayerVolumes, 0,
				layers < layerVolumes.length ? layers : layerVolumes.length);
		layerVolumes = newLayerVolumes;
		modCount++;
	}

	public NBSTick getTick(short index) {
		return song.get(index);
	}

	public String getLayerName(short layer) {
		return layerNames[layer];
	}

	public byte getLayerVolume(short layer) {
		return layerVolumes[layer];
	}

	public void setLayerName(short layer, String name) {
		layerNames[layer] = name;
	}

	public void setLayerVolume(short layer, byte volume) {
		if (volume < 0 || volume > 100)
			throw new IllegalArgumentException(
					"Valid range for volume is 0-100");
		layerVolumes[layer] = volume;
	}

	@Override
	public Iterator<NBSTick> iterator() {
		return new Iterator<NBSTick>() {

			private int cursor = 0;
			private int expectedModCount = modCount;

			@Override
			public boolean hasNext() {
				return cursor != song.size();
			}

			@Override
			public NBSTick next() {
				checkForComodification();
				try {
					int i = cursor;
					NBSTick next = song.get(i);
					cursor = i + 1;
					return next;
				} catch (IndexOutOfBoundsException e) {
					checkForComodification();
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			public void checkForComodification() {
				if (expectedModCount != modCount)
					throw new ConcurrentModificationException();

			}

		};
	}
}
