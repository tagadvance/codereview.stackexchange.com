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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + page;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableBookmark other = (ImmutableBookmark) obj;
		if (page != other.page)
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ImmutableBookmark.class.getSimpleName() + " [page=" + page
				+ ", position=" + position + "]";
	}

}