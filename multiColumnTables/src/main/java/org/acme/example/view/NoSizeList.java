package org.acme.example.view;

import java.util.ArrayList;

/**
 * Implements a list that doesn't throw {@link IndexOutOfBoundsException} when
 * getting or setting elements past the end of the list. Instead, on a get, a
 * null is returned whenever the index is out of bounds. On a set, nulls are
 * inserted to boost the list capacity before setting the new element.
 *
 * @param <T> the element type stored in the list
 */
public class NoSizeList<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public T get(int index) {
		if (index >= size()) {
			return null;
		} else {
			return super.get(index);
		}
	}

	@Override
	public T set(int index, T element) {
		while (index >= size()) {
			add(null);
		}
		
		// OK, index < size()
		return super.set(index, element);
	}
	
	/**
	 * Truncates the list to the indicated size. Elements from the end
	 * of the list are removed, in turn, until the size is equal or less
	 * than the desired size.
	 * 
	 * @param newSize the desired size of the list
	 */
	public void truncate(int newSize) {
		if (newSize >= 0) {
			while (size() > newSize) {
				remove(size() - 1);
			}
		}
	}

}
