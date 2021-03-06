package com.github.soniex2.nbx.api.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import com.github.soniex2.nbx.api.nbs.NBSHeader;
import com.github.soniex2.nbx.api.nbs.NBSOldSong;

public class NBXOutputStream extends LittleEndianDataOutputStream {

	private static final byte[] FILE_HEADER = new byte[] { -0x7F, 'N', 'B',
			'X', 0x0D, 0x0A, 0x1A, 0x0A };

	public NBXOutputStream(OutputStream os) throws IOException {
		super(os);
		write(FILE_HEADER);
	}

	/**
	 * Writes a NBX chunk to the file.
	 * 
	 * @param id
	 *            the ID - a 4-byte long ASCII string identifying this chunk
	 * @param data
	 *            the data
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeChunk(String id, byte[] data) throws IOException {
		byte[] identifier = id.getBytes("US-ASCII");
		if (identifier.length != 4) {
			throw new IllegalArgumentException("ID length must match 4!");
		}
		writeInt(data.length);
		write(identifier);
		write(data);
		CRC32 crc = new CRC32();
		crc.update(identifier);
		crc.update(data);
		writeInt((int) (crc.getValue() & 0xFFFFFFFFL));
		crc.reset();
	}

	/**
	 * Writes a song to the stream, automagically adding a "SEND" chunk.
	 * 
	 * @param header
	 *            the {@link NBSHeader} for the song
	 * @param song
	 *            the {@link NBSOldSong} itself
	 * @see #writeSong(NBSHeader, NBSOldSong, boolean)
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeSong(NBSHeader header, NBSOldSong song) throws IOException {
		writeSong(header, song, true);
	}

	/**
	 * Writes a song to the stream.
	 * 
	 * @param header
	 *            the {@link NBSHeader} for the song
	 * @param song
	 *            the {@link NBSOldSong} itself
	 * @param end
	 *            to add a "SEND" chunk after the data
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeSong(NBSHeader header, NBSOldSong song, boolean end)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NBSOutputStream os = new NBSOutputStream(baos);
		os.writeHeader(header);
		os.writeSong(song);
		writeChunk("SDAT", baos.toByteArray());
		os.close();
		if (end)
			writeChunk("SEND", new byte[0]);
	}
}
