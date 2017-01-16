/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;

/**
 * A helper class to construct a {@link ChainRunner} from a set of {@link IChainLink}s
 * 
 * @author Simon Goodall
 *
 */
public class ChainBuilder {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final List<IChainLink> chain = new LinkedList<>();

	public ChainBuilder(@NonNull final LNGDataTransformer dataTransformer) {
		this.dataTransformer = dataTransformer;
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	public void addLink(@NonNull final IChainLink link) {
		chain.add(link);
	}

	@NonNull
	public ChainRunner build() {
		return new ChainRunner(dataTransformer, new ArrayList<>(chain));
	}
}