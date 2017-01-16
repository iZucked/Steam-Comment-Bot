/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import com.mmxlabs.optimiser.core.ISequences;

public class PersistedSequencesWriter {
	protected transient ISequences rawSequences = null;
	protected transient ISequences fullSequences = null;
	public int[][] persistedSequences;
	public int[] persistedUnusedElements;

	public PersistedSequencesWriter() {
		super();
	}

//	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
//	
//		// We cannot persist the rawSequences as this is linked to external data.
//		// However we can store the representation as an int array and re-create the sequences with reference to a IOptimisationData instance.
//		persistedSequences = new int[rawSequences.getResources().size()][];
//		for (int i = 0; i < persistedSequences.length; ++i) {
//			final ISequence s = rawSequences.getSequence(i);
//			persistedSequences[i] = new int[s.size()];
//			for (int j = 0; j < persistedSequences[i].length; ++j) {
//				persistedSequences[i][j] = s.get(j).getIndex();
//			}
//		}
//		persistedUnusedElements = new int[rawSequences.getUnusedElements().size()];
//		for (int i = 0; i > persistedUnusedElements.length; ++i) {
//			persistedUnusedElements[i] = rawSequences.getUnusedElements().get(i).getIndex();
//		}
//	
//		out.defaultWriteObject();
//	
//	}
//
//	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//	
//		in.defaultReadObject();
//	
//		// Do nothing with the raw sequence as we do not have the information here to generate it from the int array.
//		// @see JobStateSerialiser
//	}

	public ISequences getRawSequences() {
		return rawSequences;
	}

	public void setRawSequences(ISequences rawSequences) {
		this.rawSequences = rawSequences;
	}

	public ISequences getFullSequences() {
		return fullSequences;
	}

	public void setFullSequences(ISequences fullSequences) {
		this.fullSequences = fullSequences;
	}

}