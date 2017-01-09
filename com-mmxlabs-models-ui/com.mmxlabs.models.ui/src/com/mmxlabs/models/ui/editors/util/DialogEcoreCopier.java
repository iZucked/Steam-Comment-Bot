/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;

/**
 * A class to maintain changes in e.g a Dialog where we wish to edit objects but have a Cancel button to trash changes.s Based on code from EMF forum
 * http://www.eclipse.org/forums/index.php?t=msg&th=200576/
 * 
 */
public class DialogEcoreCopier {
	private final List<EObject> originals = new ArrayList<EObject>();
	private final List<EObject> copies = new ArrayList<EObject>();
	private final EcoreUtil.Copier copier;
	private ChangeRecorder changeRecorder;

	private final Set<EObject> objectsAdded = new HashSet<>();
	private final Set<EObject> objectsRemoved = new HashSet<>();

	private final EContentAdapter adapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

			super.notifyChanged(notification);

			if (notification.getFeature() instanceof EReference) {
				final EReference eReference = (EReference) notification.getFeature();

				// What about multiple add/remove

				if (eReference.isContainment() && notification.getEventType() == Notification.ADD) {
					final Object newValue = notification.getNewValue();
					if (newValue instanceof EObject) {
						objectsAdded.add((EObject) newValue);
						objectsRemoved.remove(newValue);
					}
				} else if (eReference.isContainment() && notification.getEventType() == Notification.REMOVE) {
					final Object oldValue = notification.getOldValue();
					if (oldValue instanceof EObject) {
						if (objectsAdded.contains(oldValue)) {
							objectsAdded.remove(oldValue);
						} else {
							objectsRemoved.add((EObject) oldValue);
						}
					}
				}
			}

		}
	};

	public DialogEcoreCopier() {
		copier = new EcoreUtil.Copier();
	}

	public EObject copy(final EObject original) {
		final EObject copy = copier.copy(original);

		copy.eAdapters().add(adapter);

		originals.add(original);
		copies.add(copy);

		return copy;
	}

	public List<EObject> copyAll(final List<EObject> theOriginals) {

		final List<EObject> newCopies = new ArrayList<>(theOriginals.size());
		for (final EObject original : theOriginals) {
			// Check to see if object has already been copied to avoid duplicate copies
			if (copier.get(original) != null) {
				newCopies.add(copier.get(original));
			} else {
				newCopies.add(copy(original));
			}
		}

		return newCopies;
	}

	/**
	 * Lookup an original object after a copy has been made.
	 * 
	 * @param copy
	 * @return
	 */
	public EObject getOriginal(final EObject copy) {
		return copier.get(copy);
	}

	public void record() {
		copier.copyReferences();

		this.changeRecorder = new ChangeRecorder(copies);
	}

	public Command createEditCommand(final EditingDomain domain) {
		if (changeRecorder == null) {
			throw new IllegalStateException("Change record already cleaned up.");
		}

		final ChangeDescription changeDescription = changeRecorder.endRecording();
		changeRecorder = null;
		if (isEmpty(changeDescription)) {
			return null;
		}

		// work out what we are supposed to do with added and removed objects
		// generate the map and feature paths and pass into here.
		//
		final Map<Pair<EObject, EReference>, Collection<EObject>> objectsAddedMap = new HashMap<>();
		for (final EObject eObj : this.objectsAdded) {
			eObj.eAdapters().remove(adapter);
			final EObject copy = eObj.eContainer();
			if (copies.contains(copy)) {
				final EObject original = originals.get(copies.indexOf(copy));
				final Pair<EObject, EReference> p = new Pair<>(original, eObj.eContainmentFeature());
				if (objectsAddedMap.containsKey(p)) {
					objectsAddedMap.get(p).add(eObj);
				} else {
					final Set<EObject> s = new HashSet<>();
					s.add(eObj);
					objectsAddedMap.put(p, s);
				}
			}
		}
		final Collection<EObject> originalObjectsRemoved = new ArrayList<>(objectsRemoved.size());
		for (final EObject eObj : this.objectsRemoved) {
			eObj.eAdapters().remove(adapter);
			for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
				if (e.getValue() == eObj) {
					originalObjectsRemoved.add(e.getKey());
				}
			}

		}

		changeDescription.applyAndReverse();
		rewriteReferences(changeDescription);

		// ensure added and removed lists do not intersect!
		{
			final Set<EObject> copy = new HashSet<>(objectsAdded);
			copy.removeAll(objectsRemoved);
			assert copy.equals(objectsAdded);
		}

		return new ChangeDescriptionCommand(domain, changeDescription, originals, objectsAddedMap, originalObjectsRemoved);
	}

	private boolean isEmpty(final ChangeDescription changeDescription) {
		return changeDescription.getObjectChanges().isEmpty() && changeDescription.getObjectsToAttach().isEmpty() && changeDescription.getObjectsToDetach().isEmpty()
				&& changeDescription.getResourceChanges().isEmpty();
	}

	private void rewriteReferences(final EObject object) {
		final Collection<EObject> values = new ArrayList<>(copier.values());
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(values, object);

		for (final Map.Entry<EObject, EObject> entry : copier.entrySet()) {
			final EObject original = entry.getKey();
			final EObject copy = entry.getValue();

			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(copy);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (needsToBeReplaced(setting)) {
						EcoreUtil.replace(setting, copy, original);
					}
				}
			}
		}
	}

	private boolean needsToBeReplaced(final EStructuralFeature.Setting setting) {
		final EStructuralFeature feature = setting.getEStructuralFeature();
		return !feature.isDerived() && feature.isChangeable();
	}
}
