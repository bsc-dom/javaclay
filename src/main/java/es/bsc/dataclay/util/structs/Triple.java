
/**
 * @file Triple.java
 * @date May 23, 2013
 */
package es.bsc.dataclay.util.structs;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a tuple of three elements.
 * 
 * @param <X>
 *            First element type
 * @param <Y>
 *            Second element type
 * @param <Z>
 *            Third element type
 */
public final class Triple<X, Y, Z> implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 3242912965955081414L;
	/** First element. */
	private X first;
	/** Second element. */
	private Y second;
	/** Third element. */
	private Z third;

	/**
	 * Triple empty constructor
	 */
	public Triple() {

	}

	/**
	 * Triple constructor
	 * @param newfirst
	 *            First element
	 * @param newsecond
	 *            Second element
	 * @param newthird
	 *            Third element
	 */
	public Triple(final X newfirst, final Y newsecond, final Z newthird) {
		this.setFirst(newfirst);
		this.setSecond(newsecond);
		this.setThird(newthird);
	}

	/**
	 * Get the Tuple::first
	 * @return the first
	 */
	public X getFirst() {
		return this.first;
	}

	/**
	 * Set the Tuple::first
	 * @param newfirst
	 *            the first to set
	 */
	public void setFirst(final X newfirst) {
		this.first = newfirst;
	}

	/**
	 * Get the Tuple::second
	 * @return the second
	 */
	public Y getSecond() {
		return this.second;
	}

	/**
	 * Set the Tuple::second
	 * @param newsecond
	 *            the second to set
	 */
	public void setSecond(final Y newsecond) {
		this.second = newsecond;
	}

	/**
	 * Get the Tuple::third
	 * @return the third
	 */
	public Z getThird() {
		return this.third;
	}

	/**
	 * Set the Tuple::third
	 * @param newthird
	 *            the third to set
	 */
	public void setThird(final Z newthird) {
		this.third = newthird;
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof Triple) {
			final Triple<?, ?, ?> other = (Triple<?, ?, ?>) t;
			return other.getFirst().equals(this.getFirst()) && other.getSecond().equals(this.getSecond())
					&& other.getThird().equals(this.getThird());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second, third);
	}

	@Override
	public String toString() {
		return "{" + this.getFirst().toString() + ", " + this.getSecond().toString() + ", " + this.getThird().toString() + "}";
	}
}
