package com.stackexchange.codereview.streamingpages.answer;

import com.google.common.base.Preconditions;

class DefaultReindex implements Reindex {

	private Book<?> oldBook, newBook;

	public DefaultReindex(Book<?> oldBook, Book<?> newBook) {
		super();
		this.oldBook = Preconditions.checkNotNull(oldBook);
		this.newBook = Preconditions.checkNotNull(newBook);
	}

	@Override
	public Bookmark lookupOldLocation(Bookmark newBookmark) {
		Preconditions.checkNotNull(newBookmark, "bookmark must not be null");
		int absoluteIndex = (newBook.getPageSize() * newBookmark.getPage())
				+ newBookmark.getPosition();
		int newBookPosition = absoluteIndex % oldBook.getPageSize();
		int newBookPage = absoluteIndex / oldBook.getPageSize();
		return new ImmutableBookmark(newBookPage, newBookPosition);
	}

}