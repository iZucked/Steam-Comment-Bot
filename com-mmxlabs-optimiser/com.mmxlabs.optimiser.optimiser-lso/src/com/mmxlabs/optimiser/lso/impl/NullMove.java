/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.INullMove;

public class NullMove implements INullMove {

	private String generator;
	private String failure;
	
	
	public NullMove(String generator, String failure){
		this.failure = failure;
		this.generator = generator;
	}

	@Override
	public Collection<@NonNull IResource> getAffectedResources() {
		// returns an empty list
		return Collections.emptyList();
	}

	@Override
	public void apply(@NonNull IModifiableSequences sequences) {
	}

	@Override
	public boolean validate(@NonNull ISequences sequences) {
		return false;

	}

	@Override
	public String getFailure() {
		return failure;
	}

	@Override
	public void setFailure(String failure) {
		this.failure = failure;
	}
	
	@Override
	public String getGenerator(){
		return generator;
	}
	
	@Override
	public void setGenerator(String generator){
		this.generator = generator;
	}
	
	@Override
	public String getFullMessage(){
		return this.generator + ": " + this.failure;
	}
}
