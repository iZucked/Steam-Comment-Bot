package com.mmxlabs.common;

import java.util.List;
import java.util.Random;

/**
 * A class which adds some convenience methods to a Random
 * @author hinton
 *
 */
public class RandomHelper extends Random {
	private Random delegate;
	public RandomHelper(Random delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Return an integer between from 0 to n-1 which is not equal to d, drawn from a uniform distribution.
	 * @param n
	 * @param d
	 * @return
	 */
	public int nextDifferentInt(int n, int d) {
		final int k = nextInt(n-1);
		if (k >= d) {
			return k + 1;
		} else {
			return k;
		}
	}
	
	/**
	 * Uniformly randomly pick an element from a list
	 * @param <T>
	 * @param collection
	 * @return
	 */
	public <T> T chooseElementFrom(List<T> collection) {
		return collection.get(nextInt(collection.size()-1));
	}
	
	public void setSeed(long seed) {
		delegate.setSeed(seed);
	}
	public void nextBytes(byte[] bytes) {
		delegate.nextBytes(bytes);
	}
	public int nextInt() {
		return delegate.nextInt();
	}
	public int nextInt(int n) {
		return delegate.nextInt(n);
	}
	public String toString() {
		return delegate.toString();
	}
	public long nextLong() {
		return delegate.nextLong();
	}
	public boolean nextBoolean() {
		return delegate.nextBoolean();
	}
	public float nextFloat() {
		return delegate.nextFloat();
	}
	public double nextDouble() {
		return delegate.nextDouble();
	}
	public double nextGaussian() {
		return delegate.nextGaussian();
	}
}
