package com.stackexchange.codereview.streamingpages.answer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheBook<E> implements Book<E> {

	private Book<E> book;
	private final LoadingCache<Integer, List<E>> localCache;

	public CacheBook(Book<E> book) {
		super();
		this.book = Preconditions.checkNotNull(book, "book must not be null");
		this.localCache = CacheBuilder.newBuilder().build(
				new InnerCacheLoader());
	}

	@Override
	public int getPageCount() {
		return book.getPageCount();
	}

	@Override
	public List<E> getPage(int pageNumber) {
		try {
			return localCache.get(pageNumber);
		} catch (ExecutionException e) {
			Throwable cause = Throwables.getRootCause(e);
			// as this is an example I don't really care
			Throwables.propagate(cause);
		}
		return null; // dead code
	}

	@Override
	public int getPageSize() {
		return book.getPageSize();
	}

	private class InnerCacheLoader extends CacheLoader<Integer, List<E>> {

		@Override
		public List<E> load(Integer pageNumber) throws Exception {
			return book.getPage(pageNumber);
		}

	}

}