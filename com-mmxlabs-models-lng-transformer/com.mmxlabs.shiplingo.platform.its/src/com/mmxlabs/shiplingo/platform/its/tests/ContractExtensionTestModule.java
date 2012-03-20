package com.mmxlabs.shiplingo.platform.its.tests;

import java.util.Collections;

import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformerWrapper;

public class ContractExtensionTestModule extends AbstractModule {

	@Override
	protected void configure() {
		final SimpleContractTransformer sct = new SimpleContractTransformer();

		final ContractTransformer transformer = new ContractTransformerWrapper(sct, sct.getContractEClasses());
//		bind(ContractTransformer.class).toInstance(transformer);
		bind(TypeLiterals.iterable(ContractTransformer.class)).toInstance(Collections.singleton(transformer));
//		bind(TypeLiterals.iterable(ContractTransformer.class)).to(service(transformer)());
	}
	
//	@Provides
//	public Iterable<ContractTransformer> getTT() {
//		final SimpleContractTransformer sct = new SimpleContractTransformer();
//	
//		return Collections.<ContractTransformer>singleton(new ContractTransformerWrapper(sct, sct.getContractEClasses()));
//	}

}
