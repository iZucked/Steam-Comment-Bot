package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProvider;

/**
 * @since 2.0
 */
public class HashMapCharterMarketProviderEditor implements ICharterMarketProviderEditor {

	private static class DefaultCharterMarketOptions implements CharterMarketOptions {

		private final int price;

		private final int dateKey;

		public DefaultCharterMarketOptions(final int dateKey, final int price) {
			this.dateKey = dateKey;
			this.price = price;
		}

		@Override
		public int getCharterPrice() {
			return price;
		}

	}

	@Inject
	private IDateKeyProvider dateKeyProvider;

	private final String name;

	private final Map<IVesselClass, List<ICurve>> charterInOptions = new HashMap<IVesselClass, List<ICurve>>();
	private final Map<IVesselClass, List<ICurve>> charterOutOptions = new HashMap<IVesselClass, List<ICurve>>();

	public HashMapCharterMarketProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public Collection<CharterMarketOptions> getCharterInOptions(final IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterInOptions, vesselClass, dateKey);
	}

	@Override
	public Collection<CharterMarketOptions> getCharterOutOptions(final IVesselClass vesselClass, final int time) {
		final int dateKey = dateKeyProvider.getDateKeyFromHours(time);
		return getOptions(charterOutOptions, vesselClass, dateKey);
	}

	@Override
	public void addCharterInOption(final IVesselClass vesselClass, final ICurve curve) {
		addOptions(charterInOptions, vesselClass, curve);
	}

	@Override
	public void addCharterOutOption(final IVesselClass vesselClass, final ICurve curve) {
		addOptions(charterOutOptions, vesselClass, curve);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		charterInOptions.clear();
		charterOutOptions.clear();
	}

	private Collection<CharterMarketOptions> getOptions(final Map<IVesselClass, List<ICurve>> options, final IVesselClass vesselClass, final int dateKey) {

		if (options.containsKey(vesselClass)) {
			final List<ICurve> entry = options.get(vesselClass);
			final List<CharterMarketOptions> result = new ArrayList<CharterMarketOptions>(entry.size());
			for (final ICurve curve : entry) {
				result.add(new DefaultCharterMarketOptions(dateKey, (int) curve.getValueAtPoint(dateKey)));
			}
			return result;
		}

		return Collections.emptySet();
	}

	private void addOptions(final Map<IVesselClass, List<ICurve>> options, final IVesselClass vesselClass, final ICurve curve) {
		final List<ICurve> entry;
		if (options.containsKey(vesselClass)) {
			entry = options.get(vesselClass);
		} else {
			entry = new LinkedList<ICurve>();
			options.put(vesselClass, entry);
		}

		entry.add(curve);
	}
}
