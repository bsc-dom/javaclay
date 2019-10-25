
/**
 * @file Tuple.java
 * @date May 23, 2013
 */
package es.bsc.dataclay.util.structs;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a tuple.
 * 
 * @param <X>
 *            First element type
 * @param <Y>
 *            Second element type
 */
public final class Tuple<X, Y> implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 4814332679297004307L;
	
	// Set to public for YAML processing
	//CHECKSTYLE:OFF
	/** First element. */
	public X first;
	/** Second element. */
	public Y second;
	//CHECKSTYLE:ON

	/**
	 * Tuple empty constructor.
	 */
	public Tuple() {

	}

	/**
	 * Tuple constructor
	 * @param newfirst
	 *            First element.
	 * @param newsecond
	 *            Second element.
	 */
	public Tuple(final X newfirst, final Y newsecond) {
		this.setFirst(newfirst);
		this.setSecond(newsecond);
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

	@Override
	public boolean equals(final Object t) {
		if (t instanceof Tuple) {
			final Tuple<?, ?> other = (Tuple<?, ?>) t;
			return other.getFirst().equals(this.getFirst()) && other.getSecond().equals(this.getSecond());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public String toString() {
		return "{" + this.getFirst().toString() + ", " + this.getSecond().toString() + "}";
	}
}
