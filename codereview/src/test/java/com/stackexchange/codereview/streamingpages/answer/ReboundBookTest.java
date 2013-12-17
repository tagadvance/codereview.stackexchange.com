package com.stackexchange.codereview.streamingpages.answer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Joiner;

public class ReboundBookTest {

	@Mock
	private Book<Integer> book;

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
		new ReboundBook<>(null, 0);
	}

	@Test
	public void testPageCount() {
		int newPageSize = 3;
		Book<Integer> reboundBook = new ReboundBook<>(book, newPageSize);
		int expected = 16;
		Assert.assertEquals(expected, reboundBook.getPageCount());
	}

	@Test
	public void testPagesAndElementsAreReadInOrder() {
		Mockito.when(book.getPageCount()).thenReturn(3);
		Mockito.when(book.getPageSize()).thenReturn(5);
		int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 6, 7, 8, 9, 10 }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new Integer[] { 11, 12 }));

		int newPageSize = 3;
		Book<Integer> reboundBook = new ReboundBook<>(book, newPageSize);
		Iterator<List<Integer>> i = new BookIterator<>(reboundBook);
		{
			List<Integer> page0 = i.next();
			String expected = toString(1, 2, 3);
			Assert.assertEquals(expected, toString(page0.toArray()));
		}
		{
			List<Integer> page1 = i.next();
			String expected = toString(4, 5, 6);
			Assert.assertEquals(expected, toString(page1.toArray()));
		}
		{
			List<Integer> page2 = i.next();
			String expected = toString(7, 8, 9);
			Assert.assertEquals(expected, toString(page2.toArray()));
		}
		{
			List<Integer> page3 = i.next();
			String expected = toString(10, 11, 12);
			Assert.assertEquals(expected, toString(page3.toArray()));
		}
	}

	private static String toString(Object... o) {
		return Joiner.on(", ").join(o);
	}

}