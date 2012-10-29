/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.extensions;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;

/**
 * Implementation of {@link ContractTransformer} for programmatic instantation and injection.
 * 
 * @author Simon Goodall
 * 
 */
public class ContractTransformerWrapper implements ContractTransformer {

	private final ITransformerExtension contractTransformer;
	private final ModelClass modelClasses[];

	/**
	 * @since 2.0
	 */
	public ContractTransformerWrapper(final ITransformerExtension contractTransformer, final Collection<EClass> contractEClasses) {
		this.contractTransformer = contractTransformer;
		modelClasses = new ModelClass[contractEClasses.size()];
		int idx = 0;
		for (final EClass cls : contractEClasses) {
			final ModelClass mc = new ModelClass() {

				@Override
				public String getTransformer() {
					return cls.getInstanceClass().getCanonicalName();
				}
			};
			modelClasses[idx++] = mc;
		}
	}

	@Override
	public ModelClass[] getModelClass() {
		return modelClasses;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public ITransformerExtension createTransformer() {
		return contractTransformer;
	}
}
