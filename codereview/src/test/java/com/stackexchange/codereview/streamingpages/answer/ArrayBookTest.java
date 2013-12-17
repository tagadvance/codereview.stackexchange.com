package com.stackexchange.codereview.streamingpages.answer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ArrayBookTest {

	private String[][] data;

	@Before
	public void initialize() {
		data = new String[][] {
				{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" },
				{ "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" },
				{ "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" },
				{ "31", "32", "33", "34", "35", "36", "37", "38", "39", "40" },
				{ "41", "42", "43", "44", "45", "46", "47", }, };
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorThrowsNullPointerExceptionIfDataIsNull() {
		new ArrayBook<>(null);
	}

	@Test
	public void testPageCountIs5() {
		int expectedPageCount = 5;
		Book<String> book = new ArrayBook<>(data);
		int pageCount = book.getPageCount();
		Assert.assertEquals(expectedPageCount, pageCount);
	}

	@Test
	public void testPageSizeIs10() {
		int expectedPageSize = 10;
		Book<String> book = new ArrayBook<>(data);
		int pageSize = book.getPageSize();
		Assert.assertEquals(expectedPageSize, pageSize);
	}

}