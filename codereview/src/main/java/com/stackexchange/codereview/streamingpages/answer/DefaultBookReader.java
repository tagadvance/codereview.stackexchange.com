package com.stackexchange.codereview.streamingpages.answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class DefaultBookReader<E> implements BookReader<E> {

	private final Book<E> book;
	private List<E> buffer;
	private int pageNumber, position;

	public DefaultBookReader(Book<E> book) {
		this(book, new ImmutableBookmark());
	}

	public DefaultBookReader(Book<E> book, Bookmark bookmark) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.buffer = new ArrayList<>();
		pageNumber = bookmark.getPage();
		position = bookmark.getPosition();
	}

	@Override
	public List<E> read(int length) {
		for (; buffer.size() < length && pageNumber < book.getPageCount(); pageNumber++, position = 0) {
			List<E> page = book.getPage(pageNumber);
			List<E> sublist = page.subList(position, page.size());
			buffer.addAll(sublist);
		}
		int toIndex = Math.min(length, buffer.size());
		List<E> read = new ArrayList<>(length);
		for (int i = 0; i < toIndex; i++) {
			E e = buffer.remove(0);
			read.add(e);
		}
		return read;
	}

	@Override
	public void close() throws IOException {
		buffer.clear();
		buffer = null;
	}

}