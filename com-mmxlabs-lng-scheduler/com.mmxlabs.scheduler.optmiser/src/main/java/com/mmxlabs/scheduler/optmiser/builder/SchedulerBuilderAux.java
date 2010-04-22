package com.mmxlabs.scheduler.optmiser.builder;

import java.util.List;

import com.mmxlabs.optimiser.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.providers.ISequenceElementProvider;

public class SchedulerBuilderAux {

	public <T> IOrderedSequenceElementsDataComponentProvider<T> buildOrderedSequences(
			final String name,
			final ISequenceElementProvider<T> elementProvider,
			final List<ICargo> cargoes) {

		final OrderedSequenceElementsDataComponentProvider<T> provider = new OrderedSequenceElementsDataComponentProvider<T>(
				name);

		for (final ICargo cargo : cargoes) {

			final T previousElement = elementProvider.getSequenceElement(cargo,
					cargo.getLoadPort());
			final T nextElement = elementProvider.getSequenceElement(cargo,
					cargo.getDischargePort());

			provider.setElementOrder(previousElement, nextElement);
		}
		return provider;
	}
}
