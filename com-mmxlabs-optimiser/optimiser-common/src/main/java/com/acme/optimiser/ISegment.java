package com.acme.optimiser;

public interface ISegment<T> extends Iterable<T> {

	int size();
	
	T get(int index);
	
	int getSequenceStart();
	
	int getSequenceEnd();
	
	ISequence<T> getSequence();
	
}
