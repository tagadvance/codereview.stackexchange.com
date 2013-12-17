package com.stackexchange.codereview.streamingpages.answer;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CacheBookTest {

	private static final int PAGE_COUNT = 10, PAGE_SIZE = 10;

	@Mock
	Book<Integer> sourceBook, cacheBook;

	@Before
	public void initialize() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sourceBook.getPageCount()).thenReturn(PAGE_COUNT);
		Mockito.when(sourceBook.getPageSize()).thenReturn(PAGE_SIZE);
		List<Integer> page0 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Mockito.when(sourceBook.getPage(0)).thenReturn(page0);
		List<Integer> page1 = Arrays.asList(11, 12, 13, 14, 15);
		Mockito.when(sourceBook.getPage(1)).thenReturn(page1);
		Mockito.when(sourceBook.getPage(2)).thenThrow(
				new IndexOutOfBoundsException());
		cacheBook = new CacheBook<>(sourceBook);
	}

	@Test
	public void testCachePageCountEqualsSourcePageCount() {
		Assert.assertEquals(sourceBook.getPageCount(), cacheBook.getPageCount());
	}

	@Test
	public void testCachePagePositionEqualsSourcePageSize() {
		Assert.assertEquals(sourceBook.getPageSize(), cacheBook.getPageSize());
	}

	@Test
	public void testCachePage0EqualsSourcePage0() {
		int page = 0;
		Assert.assertEquals(sourceBook.getPage(page), cacheBook.getPage(page));
	}

	@Test
	public void testCachePage1EqualsSourcePage1() {
		int page = 1;
		Assert.assertEquals(sourceBook.getPage(page), cacheBook.getPage(page));
	}

}