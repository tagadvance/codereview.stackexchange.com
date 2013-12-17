package com.stackexchange.codereview.streamingpages.answer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class ReboundBook<E> implements Book<E> {

	private final Book<E> book;
	private final int newPageSize;
	private final Reindex index;

	public ReboundBook(Book<E> book, int newPageSize) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.newPageSize = newPageSize;
		this.index = new DefaultReindex(this.book, this);
	}

	@Override
	public int getPageCount() {
		int pageCount = book.getPageCount();
		int pageSize = book.getPageSize();
		BigDecimal total = BigDecimal.valueOf(pageCount * pageSize);
		BigDecimal divisor = BigDecimal.valueOf(newPageSize);
		total.divide(divisor, RoundingMode.CEILING);
		return total.intValue();
	}

	@Override
	public List<E> getPage(int pageNumber) {
		Bookmark bookmark = new ImmutableBookmark(pageNumber);
		Bookmark oldBookmark = index.lookupOldLocation(bookmark);
		try (BookReader<E> bookReader = new DefaultBookReader<>(this.book, oldBookmark);) {
			return bookReader.read(this.newPageSize);
		} catch (IOException e) {
			// as this is an example I don't really care
			Throwables.propagate(e);
		}
		return null; // dead code
	}

	@Override
	public int getPageSize() {
		return newPageSize;
	}

}