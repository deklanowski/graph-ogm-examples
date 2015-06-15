package de.jotschi.examples;

import java.util.Iterator;
import java.util.List;

public class Page<T> implements Iterable<T> {

	private List<? extends T> wrappedList;
	private long totalElements;
	private long numberOfElements;
	private long pageNumber;
	private long totalPages;

	public Page(List<? extends T> wrappedList, long totalElements, long pageNumber, long totalPages, long numberOfElements) {
		this.wrappedList = wrappedList;
		this.totalElements = totalElements;
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
		this.numberOfElements = numberOfElements;
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) wrappedList.iterator();
	}

	public int getSize() {
		return wrappedList.size();
	}

	public long getTotalElements() {
		return totalElements;
	}

	public long getNumber() {
		return pageNumber;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public long getNumberOfElements() {
		return numberOfElements;
	}
}
