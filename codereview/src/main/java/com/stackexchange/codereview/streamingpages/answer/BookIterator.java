package com.stackexchange.codereview.streamingpages.answer;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;

public class BookIterator<E> implements Iterator<List<E>> {

	private final Book<E> book;
	private final BookReader<E> bookReader;
	private int pagesRead;

	public BookIterator(final Book<E> book) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.bookReader = new DefaultBookReader<>(book);
	}

	@Override
	public boolean hasNext() {
		return (pagesRead < book.getPageCount());
	}

	@Override
	public List<E> next() {
		List<E> next = bookReader.read(book.getPageSize());
		if (next.isEmpty()) {
			throw new NoSuchElementException();
		}
		this.pagesRead++;
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
