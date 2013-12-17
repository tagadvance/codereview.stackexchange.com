package com.stackexchange.codereview.streamingpages.answer;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

public class ArrayBook<E> implements Book<E> {

	/**
	 * I am using an array because that is what was used in the example;
	 * however, it could be backed by anything, e.g. an expensive database
	 * query.
	 */
	private E[][] data;

	public ArrayBook(E[][] data) {
		super();
		this.data = Preconditions.checkNotNull(data, "data must not be null");
	}

	@Override
	public int getPageCount() {
		return data.length;
	}

	@Override
	public List<E> getPage(int pageNumber) {
		return Arrays.asList(data[pageNumber]);
	}

	@Override
	public int getPageSize() {
		/*
		 * here we assume all pages are of the exact same size (except the last
		 * one) TODO: write precondition/unit test to prove it
		 */
		return getPage(0).size();
	}

}