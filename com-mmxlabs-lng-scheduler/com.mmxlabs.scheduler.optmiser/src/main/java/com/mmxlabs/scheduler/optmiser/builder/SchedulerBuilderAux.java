package com.mmxlabs.scheduler.optmiser.builder;

import java.util.List;

import com.mmxlabs.optimiser.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.providers.ISequenceElementProvider;

public class SchedulerBuilderAux {

	public IOrderedSequenceElementsDataComponentProvider<ISequenceElement> buildOrderedSequences(
			final String name,
			final ISequenceElementProvider elementProvider,
			final List<ICargo> cargoes) {

		final OrderedSequenceElementsDataComponentProvider<ISequenceElement> provider = new OrderedSequenceElementsDataComponentProvider<ISequenceElement>(
				name);

		for (final ICargo cargo : cargoes) {

			final ISequenceElement previousElement = elementProvider.getSequenceElement(cargo,
					cargo.getLoadPort());
			final ISequenceElement nextElement = elementProvider.getSequenceElement(cargo,
					cargo.getDischargePort());

			provider.setElementOrder(previousElement, nextElement);
		}
		return provider;
	}
}
