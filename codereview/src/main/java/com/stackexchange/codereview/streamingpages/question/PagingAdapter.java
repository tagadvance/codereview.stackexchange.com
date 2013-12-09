package com.stackexchange.codereview.streamingpages.question;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;

public abstract class PagingAdapter<T> implements Iterable<List<T>> {

	private final int callerPageSize;
	private final int sourcePageSize;

	private final LoadingCache<Integer, List<T>> localCache = CacheBuilder
			.newBuilder().build(new CacheLoader<Integer, List<T>>() {
				@Override
				public List<T> load(Integer sourcePageNumber) throws Exception {
					return fetchPageFromSource(sourcePageNumber);
				}
			});

	public PagingAdapter(int callerPageSize, int sourcePageSize) {
		Preconditions.checkArgument(callerPageSize > 0,
				"callerPageSize must be greater than 0.");
		Preconditions.checkArgument(sourcePageSize > 0,
				"sourcePageSize must be greater than 0.");
		this.callerPageSize = callerPageSize;
		this.sourcePageSize = sourcePageSize;
	}

	/**
	 * Returns a List of objects from the source for the specified page.
	 * 
	 * <p>
	 * If the returned List is smaller in size than the source page size, that
	 * means there are no further source pages.
	 * 
	 * @param sourcePageNumber
	 *            The requested page.
	 * @return The List of objects.
	 * @throws Exception
	 */
	protected abstract List<T> fetchPageFromSource(int sourcePageNumber)
			throws Exception;

	/**
	 * Returns a List of objects for the specified page, as defined by the
	 * caller page size.
	 * 
	 * <p>
	 * If the returned List is smaller in size than the caller page size, that
	 * means there are no further pages available.
	 * 
	 * @param callerPageNumber
	 *            The requested page.
	 * @return The List of objects.
	 */
	public List<T> getPage(int callerPageNumber) {

		Preconditions.checkArgument(callerPageNumber > 0,
				"callerPageNumber must be greater than 0.");

		List<T> callerPage = Lists.newArrayListWithCapacity(callerPageSize);

		int numLeftToFetch;
		while ((numLeftToFetch = callerPageSize - callerPage.size()) > 0) {
			int fetchFromAbsoluteIndex = (callerPageNumber * callerPageSize)
					- numLeftToFetch;
			int sourcePageNumber = IntMath.divide(fetchFromAbsoluteIndex + 1,
					sourcePageSize, RoundingMode.CEILING);
			List<T> sourcePage;
			try {
				sourcePage = localCache.get(sourcePageNumber);
			} catch (ExecutionException exception) {
				throw Throwables.propagate(exception.getCause());
			}
			int sourcePageStartAbsoluteIndex = (sourcePageNumber - 1)
					* sourcePageSize;
			int addFromIndex = fetchFromAbsoluteIndex
					- sourcePageStartAbsoluteIndex;
			int addToIndex = Math.min(addFromIndex + numLeftToFetch,
					sourcePage.size());
			if (addFromIndex >= addToIndex) {
				break;
			}
			callerPage.addAll(sourcePage.subList(addFromIndex, addToIndex));
			if (sourcePageSize - sourcePage.size() > 0) {
				break; // this optimization assumes a partial page is the last
						// one
			}
		}

		return callerPage;
	}

	@Override
	public Iterator<List<T>> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<List<T>> {

		private int currentPageNumber = 0;
		private List<T> currentPage = null;

		@Override
		public boolean hasNext() {
			initIfNeeded();
			return !currentPage.isEmpty();
		}

		@Override
		public List<T> next() {
			initIfNeeded();
			List<T> temp = currentPage;
			currentPage = (currentPage.size() < callerPageSize) ? Collections
					.<T> emptyList() // this optimization assumes a partial page
										// is the last one
					: getPage(++currentPageNumber);
			return temp;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void initIfNeeded() {
			if (currentPage == null) {
				currentPage = getPage(++currentPageNumber);
			}
		}
	}

	/*** TESTING ***/

	public static void main(String[] args) {
		testHelper(10);
		testHelper(5);
		testHelper(3);
		testHelper(13);
		testHelper(58);
	}

	private static void testHelper(int callerPageSize) {
		final TestSource ts = TestSource.INSTANCE;
		PagingAdapter<String> pg = new PagingAdapter<String>(callerPageSize,
				ts.getPageSize()) {
			@Override
			protected List<String> fetchPageFromSource(int sourcePageNumber) {
				return ts.get(sourcePageNumber);
			}
		};
		System.out.println("caller page size = " + callerPageSize);
		int pageNum = 0;
		for (List<String> lst : pg) {
			System.out.println("page " + ++pageNum + " (" + lst.size() + "): "
					+ lst);
		}
		System.out.println();
	}

	private static class TestSource {

		public static final TestSource INSTANCE = new TestSource();

		private final String[][] store = {
				{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" },
				{ "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" },
				{ "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" },
				{ "31", "32", "33", "34", "35", "36", "37", "38", "39", "40" },
				{ "41", "42", "43", "44", "45", "46", "47", }, };

		private TestSource() {
		}

		public List<String> get(int page) {
			try {
				return Arrays.asList(store[page - 1]);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				return Collections.emptyList();
			}
		}

		public int getPageSize() {
			return store[0].length;
		}

		public int getNumPages() {
			return store.length;
		}
	}
	
}