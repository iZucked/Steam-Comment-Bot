/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.rvps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;

public abstract class ScenarioRVP extends EContentAdapter implements IReferenceValueProvider {
	protected final EAttribute nameAttribute;

	@Override
	public boolean updateOnChangeToFeature(final Object changedFeature) {
		return false;
	}

	public ScenarioRVP(final EAttribute nameAttribute) {
		super();
		this.nameAttribute = nameAttribute;
	}

	protected ArrayList<Pair<String, EObject>> getSortedNames(final EList<? extends EObject> objects, final EAttribute nameAttribute) {
		final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

		for (final EObject object : objects) {
			result.add(new Pair<String, EObject>(object.eGet(nameAttribute).toString(), object));
		}

		Collections.sort(result, new Comparator<Pair<String, ?>>() {
			@Override
			public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		});

		return result;
	}

	@Override
	public String getName(final EObject referer, final EReference reference, final EObject target) {
		if (target == null) {
			return "empty";
		}
		return (String) target.eGet(nameAttribute);
	}

	@Override
	public void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
		if (!notification.isTouch() && isRelevantTarget(notification.getNotifier(), notification.getFeature())) {
			cacheValues();
		}
	}

	protected boolean isRelevantTarget(final Object target, final Object feature) {
		return feature.equals(nameAttribute);
	}

	protected abstract void cacheValues();

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
		if (referenceValue == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(new Pair<Notifier, List<Object>>(referenceValue, Collections.singletonList((Object) nameAttribute)));
	}
}