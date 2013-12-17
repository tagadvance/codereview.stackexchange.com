package com.stackexchange.codereview.streamingpages.answer;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Joiner;

public class DefaultBookReaderTest {

	@Mock
	private Book<Integer> book;
	@Mock
	private Bookmark bookmark;

	@Before
	public void initialize() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(book.getPageCount()).thenReturn(5);
		Mockito.when(book.getPageSize()).thenReturn(10);
		int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 11, 12, 13, 14, 15, 16, 17, 18,
						19, 20 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 21, 22, 23, 24, 25, 26, 27, 28,
						29, 30 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 31, 32, 33, 34, 35, 36, 37, 38,
						39, 40 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 41, 42, 43, 44, 45, 46, 47, }));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorThrowsNullPointerExceptionIfBookIsNull() {
		new DefaultBookReader<>(null, bookmark);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorThrowsNullPointerExceptionIfBookmarkIsNull() {
		new DefaultBookReader<>(book, null);
	}

	@Test
	public void testRead3ElementsFromFirstPage() {
		Object[] expected = new Integer[] { 5, 6, 7 };

		int page = 0, position = 4;
		Bookmark bookmark = new ImmutableBookmark(page, position);
		DefaultBookReader<Integer> reader = new DefaultBookReader<>(book,
				bookmark);
		int length = 3;
		List<Integer> list = reader.read(length);
		Assert.assertEquals(toString(expected), toString(list.toArray()));
	}

	@Test
	public void testRead2ElementsFromFirstPageAnd2ElementsFromSecondPage() {
		Object[] expected = new Integer[] { 9, 10, 11, 12 };

		int page = 0, position = 8;
		Bookmark bookmark = new ImmutableBookmark(page, position);
		DefaultBookReader<Integer> reader = new DefaultBookReader<>(book,
				bookmark);
		int length = 4;
		List<Integer> list = reader.read(length);
		Assert.assertEquals(toString(expected), toString(list.toArray()));
	}

	@Test
	public void testReadEndOfBook() {
		Object[] expected = new Integer[] { 45, 46, 47 };

		int page = 4, position = 4;
		Bookmark bookmark = new ImmutableBookmark(page, position);
		DefaultBookReader<Integer> reader = new DefaultBookReader<>(book,
				bookmark);
		int length = 10;
		List<Integer> list = reader.read(length);
		Assert.assertEquals(toString(expected), toString(list.toArray()));
	}

	private static String toString(Object... o) {
		return Joiner.on(", ").join(o);
	}

}
