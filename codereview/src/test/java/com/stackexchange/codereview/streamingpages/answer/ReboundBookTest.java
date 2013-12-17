package com.stackexchange.codereview.streamingpages.answer;

import org.junit.Test;

public class ReboundBookTest {
	
	@Test(expected = NullPointerException.class)
	public void testConstructorThrowsNullPointerExceptionIfBookIsNull() {
		new ReboundBook<>(null, 0);
	}
	
}