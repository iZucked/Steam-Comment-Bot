package com.mmxlabs.models.lng.pricing.util;

import java.time.YearMonth;
import java.util.function.Consumer;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingFactory;

public class DataIndexBuilder<T extends NamedIndexContainer<Double>> {
	private final T index;
	private final Consumer<T> buildAction;

	public DataIndexBuilder(final T index, final Consumer<T> buildAction) {
		this.index = index;
		this.buildAction = buildAction;
	}

	public DataIndexBuilder<T> addIndexPoint(final YearMonth date, final double value) {
		final IndexPoint<Double> ip = PricingFactory.eINSTANCE.createIndexPoint();
		ip.setDate(date);
		ip.setValue(value);

		((DataIndex<Double>) index.getData()).getPoints().add(ip);

		return this;
	}

	public T build() {
		buildAction.accept(index);
		return index;
	}
}
