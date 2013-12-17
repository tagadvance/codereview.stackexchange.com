package com.stackexchange.codereview.streamingpages.answer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultReindexTest {

	// Old Book Visualization
	// {
	// { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" },
	// { "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" },
	// { "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" },
	// { "31", "32", "33", "34", "35", "36", "37", "38", "39", "40" },
	// { "41", "42", "43", "44", "45", "46", "47", },
	// }

	// New Book Visualization
	// {
	// { "1", "2", "3", },
	// { "4", "5", "6", },
	// { "7", "8", "9", },
	// { "10", "11", "12" },
	// { ... }
	// }

	@Mock
	private Book<?> oldBook, newBook;
	private Reindex index;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		int oldPageCount = 5, oldPageSize = 10;
		prepareBookStub(oldBook, oldPageCount, oldPageSize);

		int newPageCount = 4, newPageSize = 3;
		prepareBookStub(newBook, newPageCount, newPageSize);

		index = new DefaultReindex(oldBook, newBook);
	}

	private static void prepareBookStub(Book<?> book, int pageCount,
			int pageSize) {
		Mockito.when(book.getPageCount()).thenReturn(pageCount);
		Mockito.when(book.getPageSize()).thenReturn(pageSize);
		Mockito.verify(book, Mockito.never()).getPage(0);
	}

	@Test(expected = NullPointerException.class)
	public void testRebindThrowsNullPointerExceptionIfOldBookIsNull() {
		new DefaultReindex(null, newBook);
	}

	@Test(expected = NullPointerException.class)
	public void testRebindThrowsNullPointerExceptionIfNewBookIsNull() {
		new DefaultReindex(oldBook, null);
	}

	@Test(expected = NullPointerException.class)
	public void testRebindThrowsNullPointerExceptionIfArgumentIsNull() {
		index.lookupOldLocation(null);
	}

	@Test
	public void testLookupOldLocation() { // TODO: improve the name of this test
		int expectedPage = 1, expectedPosition = 0;
		Bookmark expectedBookmark = new ImmutableBookmark(expectedPage,
				expectedPosition);

		int page = 3, position = 1;
		Bookmark newBookmark = new ImmutableBookmark(page, position);
		Bookmark oldBookmark = index.lookupOldLocation(newBookmark);

		Assert.assertEquals(expectedBookmark, oldBookmark);
	}

}