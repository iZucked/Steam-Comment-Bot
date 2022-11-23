/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.service.AbstractTraversalStrategy;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.rcp.common.ServiceHelper;

/**
 * Based on {@link Recursive} but adapter to filter out particular features by default. In particular we can avoid various generated result sub-model trees and alternative embdedded scenarios such as
 * sandboxes.
 * 
 * @author Simon Goodall
 *
 */
public class LNGRecursiveValidationStrategy extends AbstractTraversalStrategy {
	private Collection<EObject> roots;
	private boolean contextChanged = true;

	private final Set<EReference> ignoredFeatures = new HashSet<>();
	private final Set<EReference> alwaysIgnoredFeatures = new HashSet<>();
	private final Set<EReference> ignoredUnlessFullFeatures = new HashSet<>();

	public LNGRecursiveValidationStrategy() {
		//// Ignore these sub-model trees for performance reasons (i.e. we know the tree is big and has no need for validation)

		// Ignore sandbox results
		alwaysIgnoredFeatures.add(AnalyticsPackage.eINSTANCE.getOptionAnalysisModel_Results());
		// Ignore optimisation results
		alwaysIgnoredFeatures.add(AnalyticsPackage.eINSTANCE.getAnalyticsModel_Optimisations());
		// Ignore any solution option schedule models (although probably covered by the above)
		alwaysIgnoredFeatures.add(AnalyticsPackage.eINSTANCE.getSolutionOption_ScheduleModel());

		//// These are sub models we want to validate as an "extra target" as they are effectively mini scenarios.
		// Ignore sandboxes scenario
		ignoredUnlessFullFeatures.add(AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels());
		ignoredUnlessFullFeatures.add(AnalyticsPackage.eINSTANCE.getAnalyticsModel_SwapValueMatrixModels());

	}

	@Override
	public void startTraversal(final Collection<? extends EObject> traversalRoots, final IProgressMonitor progressMonitor) {

		roots = new ArrayList<>(traversalRoots);
		ignoredFeatures.clear();
		ignoredFeatures.addAll(alwaysIgnoredFeatures);

		// Assume a full model validation by default unless the extra validation context tells us otherwise
		final boolean[] fullValidation = new boolean[] { true };

		ServiceHelper.withOptionalServiceConsumer(IValidationService.class, vs -> {
			if (vs != null) {
				final IExtraValidationContext ctx = vs.getExtraContext();
				if (ctx != null) {
					fullValidation[0] = ctx.isFullModelValidation();
				}
			}
		});

		// If we are not doing a full model validation, include these extra sub-model features to ignore.
		if (!fullValidation[0]) {
			ignoredFeatures.addAll(ignoredUnlessFullFeatures);
		}

		super.startTraversal(traversalRoots, progressMonitor);
	}

	private Collection<EObject> getRoots() {
		return roots;
	}

	/*
	 * (non-Javadoc) Implements the inherited method.
	 */
	@Override
	protected int countElements(final Collection<? extends EObject> ignored) {
		return countRecursive(getRoots());
	}

	private int countRecursive(final Collection<? extends EObject> elements) {
		int result = 0;

		result = elements.size();

		for (final EObject next : elements) {
			result = result + countRecursive(filteredContents(next));
		}

		return result;
	}

	private List<EObject> filteredContents(final EObject parent) {
		final List<EObject> contents = new LinkedList<>();

		for (final EReference ref : parent.eClass().getEAllReferences()) {
			if (!ref.isContainment()) {
				continue;
			}
			if (ignoredFeatures.contains(ref)) {
				continue;
			}
			if (ref.isMany()) {
				final List<EObject> children = (List<EObject>) parent.eGet(ref);
				if (children != null) {
					contents.addAll(children);
				}
			} else {
				final EObject child = (EObject) parent.eGet(ref);
				if (child != null) {
					contents.add(child);
				}
			}
		}
		return contents;

	}

	/*
	 * (non-Javadoc) Implements the inherited method.
	 */
	@Override
	protected Iterator<? extends EObject> createIterator(final Collection<? extends EObject> ignored) {

		return new EcoreUtil.ContentTreeIterator<EObject>(getRoots()) {
			private static final long serialVersionUID = -1;

			@Override
			public Iterator<EObject> getChildren(final Object obj) {
				if (obj == getRoots()) {
					return new Iterator<EObject>() {
						private final Iterator<EObject> delegate = getRoots().iterator();

						public boolean hasNext() {
							return delegate.hasNext();
						}

						public EObject next() {
							// if I'm being asked for my next element, then
							// we are stepping to another traversal root
							contextChanged = true;

							return delegate.next();
						}

						public void remove() {
							delegate.remove();
						}
					};
				} else {
					return super.getChildren(obj);
				}
			}

			/**
			 * Returns an iterator over the {@link EObject#eContents() children} of the given parent EObject.
			 * 
			 * @param eObject
			 *            the parent object.
			 * @return the children iterator.
			 */
			protected Iterator<? extends EObject> getEObjectChildren(final EObject eObject) {
				// Note: Super class had different checks for proxies.
				return filteredContents(eObject).iterator();
			}

			@Override
			public EObject next() {
				// this will be set to true again the next time we test hasNext() at
				// the traversal root level
				contextChanged = false;

				return super.next();
			}
		};
	}

	@Override
	public boolean isClientContextChanged() {
		return contextChanged;
	}

	private Set<EObject> makeTargetsDisjoint(final Collection<? extends EObject> objects) {
		final Set<EObject> result = new java.util.HashSet<>();

		// ensure that any contained (descendent) elements of other elements
		// that we include are not included, because they will be
		// traversed by recursion, anyway

		for (final EObject target : objects) {
			// EcoreUtil uses the InternalEObject interface to check
			// containment, so we do the same. Also, we kip up a level to
			// the immediate container for efficiency: an object is its
			// own ancestor, so we can "pre-step" up a level to avoid the
			// cost of doing it individually for every miss in the collection
			if (!EcoreUtil.isAncestor(objects, ((InternalEObject) target).eInternalContainer())) {
				result.add(target);
			}
		}

		return result;
	}
};