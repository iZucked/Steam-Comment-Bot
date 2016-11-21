package com.mmxlabs.models.lng.port.ui.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;

public class PortSetValueProvider extends BaseReferenceValueProvider {
	protected List<Pair<String, EObject>> cachedValues = null;
	protected final @NonNull PortModel portModel;

	public PortSetValueProvider(final @NonNull PortModel portModel) {
		this.portModel = portModel;
		portModel.eAdapters().add(this);
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
		if (cachedValues == null) {
			cacheValues();
		}
		return cachedValues;
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		if (target instanceof APortSet<?>) {
			return true;
		}

		return super.isRelevantTarget(target, feature);
	}

	@Override
	protected void cacheValues() {

		final List<Pair<String, EObject>> values = new LinkedList<>();
		final Pair<String, EObject> none = getEmptyObject();
		if (none != null) {
			values.add(none);
		}
		{
			final List<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

			for (final Port object : portModel.getPorts()) {
				result.add(new Pair<String, EObject>(getName(null, null, object) + "", object));
			}

			Collections.sort(result, comparator);
			values.addAll(result);
		}
		{
			final List<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();
			
			for (final EObject object : portModel.getPortGroups()) {
				result.add(new Pair<String, EObject>(getName(null, null, object) + "", object));
			}
			
			Collections.sort(result, comparator);
			values.addAll(result);
		}
		{
			final List<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

			for (final EObject object : portModel.getSpecialPortGroups()) {
				result.add(new Pair<String, EObject>(getName(null, null, object) + "", object));
			}

			Collections.sort(result, comparator);
			values.addAll(result);
		}
		{
			final List<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

			final Set<String> seenNames = new HashSet<>();
			for (final EObject object : portModel.getPortCountryGroups()) {
				final String name = getName(null, null, object);
				if (name != null) {
					result.add(new Pair<String, EObject>(name + "", object));
					seenNames.add(name.toLowerCase());
				}
			}
			for (final Port port : portModel.getPorts()) {
				final Location location = port.getLocation();
				if (location != null) {
					final String countryName = location.getCountry();
					if (countryName != null && !countryName.isEmpty()) {
						final String lcn = countryName.toLowerCase();
						if (seenNames.add(lcn)) {
							final PortCountryGroup pcg = PortFactory.eINSTANCE.createPortCountryGroup();
							pcg.setName(countryName);
							result.add(new Pair<String, EObject>(countryName + "", pcg));
						}
					}
				}
			}

			Collections.sort(result, comparator);
			values.addAll(result);
		}

		cachedValues = values;

	}

	protected Pair<String, EObject> getEmptyObject() {
		return null;
	}

	@Override
	public void dispose() {
		portModel.eAdapters().remove(this);
	}
}
