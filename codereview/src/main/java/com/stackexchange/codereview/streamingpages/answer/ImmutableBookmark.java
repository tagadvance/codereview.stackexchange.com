package com.stackexchange.codereview.streamingpages.answer;

import com.google.common.base.Preconditions;

public class ImmutableBookmark implements Bookmark {

	public static final int MIN_PAGE = 0, MIN_POSITION = 0;

	private final int page, position;

	public ImmutableBookmark() {
		this(MIN_PAGE, MIN_POSITION);
	}
	
	public ImmutableBookmark(int page) {
		this(page, MIN_POSITION);
	}

	public ImmutableBookmark(int page, int position) {
		super();
		Preconditions.checkArgument(page >= MIN_PAGE, "page must be >= "
				+ MIN_PAGE);
		Preconditions.checkArgument(position >= MIN_POSITION,
				"position must be >= " + MIN_POSITION);
		this.page = page;
		this.position = position;
	}

	public int getPage() {
		return page;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return ImmutableBookmark.class.getSimpleName() + " [page=" + page
				+ ", position=" + position + "]";
	}

}