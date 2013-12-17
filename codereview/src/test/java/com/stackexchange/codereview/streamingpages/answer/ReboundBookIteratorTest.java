package com.stackexchange.codereview.streamingpages.answer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Joiner;

/**
 * 
 * @see <a href="http://stackoverflow.com/a/14920413/625688">How to JUnit test a
 *      iterator</a>
 */
public class ReboundBookIteratorTest {

	@Mock
	private Book<String> book;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testHasNextReturnsFalseOnEmptyCollection() {
		// empty book
		Mockito.when(book.getPageCount()).thenReturn(0);
		Mockito.when(book.getPageSize()).thenReturn(0);
		final int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber)).thenThrow(
				new IndexOutOfBoundsException());

		int newPageSize = 1;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		boolean expected = false;
		Assert.assertEquals(expected, i.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextThrowsNoSuchElementException() {
		// empty book
		Mockito.when(book.getPageCount()).thenReturn(0);
		Mockito.when(book.getPageSize()).thenReturn(0);
		final int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber)).thenThrow(
				new IndexOutOfBoundsException());

		int newPageSize = 1;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		i.next();
	}

	@Test
	public void testHasNextReturnsTrueSeveralTimes() {
		// book with single page and single element
		Mockito.when(book.getPageCount()).thenReturn(1);
		Mockito.when(book.getPageSize()).thenReturn(1);
		final int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber)).thenReturn(
				Arrays.asList(new String[] { "one" }));

		int newPageSize = 1;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		boolean expected = true;
		int arbitraryInt = 2;
		for (int j = 0; j < arbitraryInt; j++) {
			Assert.assertEquals(expected, i.hasNext());
		}
	}

	@Test
	public void testHasNextReturnsTrueThenFalseAfterItemIsRead() {
		// book with single page and single element
		Mockito.when(book.getPageCount()).thenReturn(1);
		Mockito.when(book.getPageSize()).thenReturn(1);
		final int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber)).thenReturn(
				Arrays.asList(new String[] { "one" }));

		int newPageSize = 1;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		{
			boolean expected = true;
			Assert.assertEquals(expected, i.hasNext());
		}

		{
			List<String> next = i.next();
			String expected = "one";
			Assert.assertEquals(expected, next.get(0));
		}

		{
			boolean expected = false;
			int arbitraryInt = 2;
			for (int j = 0; j < arbitraryInt; j++) {
				Assert.assertEquals(expected, i.hasNext());
			}
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveThrowsUnsupportedOperationException() {
		// empty book
		Mockito.when(book.getPageCount()).thenReturn(0);
		Mockito.when(book.getPageSize()).thenReturn(0);
		final int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber)).thenThrow(
				new IndexOutOfBoundsException());

		int newPageSize = 1;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		i.remove();
	}

	@Test()
	public void testPagesAndElementsAreReadInOrder() {
		Mockito.when(book.getPageCount()).thenReturn(3);
		Mockito.when(book.getPageSize()).thenReturn(5);
		int pageNumber = 0;
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new String[] { "1", "2", "3", "4", "5" }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new String[] { "6", "7", "8", "9", "10" }));
		Mockito.when(book.getPage(pageNumber++)).thenReturn(
				Arrays.asList(new String[] { "11", "12" }));

		int newPageSize = 3;
		Iterator<List<String>> i = new ReboundBookIterator<>(book, newPageSize);
		{
			List<String> page0 = i.next();
			String expected = toString(1, 2, 3);
			Assert.assertEquals(expected, toString(page0.toArray()));
		}
		{
			List<String> page1 = i.next();
			String expected = toString(4, 5, 6);
			Assert.assertEquals(expected, toString(page1.toArray()));
		}
		{
			List<String> page2 = i.next();
			String expected = toString(7, 8, 9);
			Assert.assertEquals(expected, toString(page2.toArray()));
		}
		{
			List<String> page3 = i.next();
			String expected = toString(10, 11, 12);
			Assert.assertEquals(expected, toString(page3.toArray()));
		}
	}

	private static String toString(Object... o) {
		return Joiner.on(", ").join(o);
	}
}