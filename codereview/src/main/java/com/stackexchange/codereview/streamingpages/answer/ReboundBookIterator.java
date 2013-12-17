package com.stackexchange.codereview.streamingpages.answer;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;

public class ReboundBookIterator<E> implements Iterator<List<E>> {

	private final Book<E> book;
	private final BookReader<E> bookReader;
	private final int newPageSize;
	private int pagesRead;

	public ReboundBookIterator(final Book<E> book, final int newPageSize) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.bookReader = new DefaultBookReader<>(book);
		Preconditions.checkArgument(newPageSize >= 1, "newPageSize < 1");
		this.newPageSize = newPageSize;
	}

	@Override
	public boolean hasNext() {
		return (pagesRead < book.getPageCount());
	}

	@Override
	public List<E> next() {
		List<E> next = bookReader.read(newPageSize);
		if (next.isEmpty()) {
			throw new NoSuchElementException();
		}
		this.pagesRead += next.size();
		return next;
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}

}
