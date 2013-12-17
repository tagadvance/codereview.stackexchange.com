package com.stackexchange.codereview.streamingpages.answer;

import java.io.Closeable;
import java.util.List;

public interface BookReader<E> extends Closeable, AutoCloseable {

	/**
	 * 
	 * @param length
	 * @return
	 */
	public List<E> read(int length);

}