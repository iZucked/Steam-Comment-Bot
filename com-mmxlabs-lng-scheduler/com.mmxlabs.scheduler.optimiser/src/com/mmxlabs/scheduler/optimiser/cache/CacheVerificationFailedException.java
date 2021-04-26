package com.mmxlabs.scheduler.optimiser.cache;

public class CacheVerificationFailedException extends RuntimeException {
	public CacheVerificationFailedException() {
		super("Cache verification failed");
	}
}
