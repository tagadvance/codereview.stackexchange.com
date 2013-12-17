package com.stackexchange.codereview.streamingpages.answer;

public interface Reindex {

	public Bookmark lookupOldLocation(Bookmark newBookmark);

}