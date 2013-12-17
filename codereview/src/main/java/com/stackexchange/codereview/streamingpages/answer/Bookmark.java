package com.stackexchange.codereview.streamingpages.answer;

interface Bookmark {

	/**
	 * 
	 * @return currentpage (inclusive)
	 */
	public int getPage();

	/**
	 * 
	 * @return position relative to current page (inclusive)
	 */
	public int getPosition();

}