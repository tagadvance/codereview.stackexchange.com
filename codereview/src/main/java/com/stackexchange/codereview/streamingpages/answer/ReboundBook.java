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
	private final int pageCount;
	
	public ReboundBook(Book<E> book, int newPageSize) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.newPageSize = newPageSize;
		this.index = new DefaultReindex(this.book, this);
		this.pageCount = calculatePageCount();
	}
	
	public int calculatePageCount() {
		int pageCount = book.getPageCount();
		int pageSize = book.getPageSize();
		int product = pageCount * pageSize;
		product -= (pageSize - book.getPage(pageCount - 1).size()); // FIXME: this feels dirty TODO: support pages of varying size
		BigDecimal total = BigDecimal.valueOf(product);
		BigDecimal divisor = BigDecimal.valueOf(newPageSize);
		BigDecimal totalPageCount = total.divide(divisor, RoundingMode.CEILING);
		return totalPageCount.intValue();
	}
	
	@Override
	public int getPageCount() {
		return this.pageCount;
	}

	@Override
	public List<E> getPage(int pageNumber) {
		Bookmark bookmark = new ImmutableBookmark(pageNumber);
		Bookmark oldBookmark = index.lookupOldLocation(bookmark);
		try (BookReader<E> bookReader = new DefaultBookReader<>(this.book,
				oldBookmark);) {
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