package com.stackexchange.codereview.streamingpages.answer;

import java.util.List;

public interface Book<E> {

	public int getPageCount();

	/**
	 * 
	 * @param pageNumber
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public List<E> getPage(int pageNumber);

	public int getPageSize();

}