package com.stackexchange.codereview.streamingpages.answer;

import org.junit.Test;

import com.stackexchange.codereview.streamingpages.answer.ImmutableBookmark;

public class ImmutableBookmarkTest {

	private final int passingPage = 0, passingPosition = 0, failingPage = -1,
			failingPosition = -1;

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorThrowsIllegalArgumentExceptionIfPageIsLessThanZero() {
		new ImmutableBookmark(failingPage, passingPosition);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorThrowsIllegalArgumentExceptionIfPositionIsLessThanZero() {
		new ImmutableBookmark(passingPage, failingPosition);
	}

	public void testConstructorAcceptsValidArguments() {
		new ImmutableBookmark(passingPage, passingPosition);
	}

}
