/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;

import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;

/**
 * Validation related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * 
 */
public class EObjectTableViewerValidationSupport {

	private final ColumnViewer viewer;

	protected IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
//			final HashSet<Object> updates = new HashSet<Object>();
//			for (final Map.Entry<Object, IStatus> entry : validationErrors.entrySet()) {
//				if (!entry.getValue().isOK())
//					updates.add(entry.getKey());
//			}

			validationErrors.clear();

			processStatus(status, true);
		}
	};

	private IStatusProvider statusProvider;

	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	/**
	 */
	public EObjectTableViewerValidationSupport(ColumnViewer viewer) {
		this.viewer = viewer;
	}

	protected void processStatus(final boolean update) {
		if (statusProvider != null) {
			processStatus(statusProvider.getStatus(), update);
		}
	}

	protected void processStatus(final IStatus status, final boolean update) {
		recursiveProcessStatus(status, update);
	}

	private void recursiveProcessStatus(final IStatus status, final boolean update) {
		if (status == null)
			return;
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				recursiveProcessStatus(s, update);
			}
		}
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus detailConstraintStatus = (IDetailConstraintStatus) status;
			if (!status.isOK()) {
				// Set raw object...
				setStatus(detailConstraintStatus.getTarget(), status);
				// ..also the transformed object. In some cases we the transformed object could be an table local data such as an artificial row object
				updateObject(getElementForValidationTarget(detailConstraintStatus.getTarget()), status, update);

				for (final EObject e : detailConstraintStatus.getObjects()) {
					setStatus(e, status);
					updateObject(getElementForValidationTarget(e), status, update);
				}
			}
		}
	}

	public EObject getElementForValidationTarget(EObject e) {
		return e;
	}

	/**
	 */
	public void setStatus(final Object e, final IStatus s) {
		final IStatus existing = validationErrors.get(e);
		if (existing == null || s.getSeverity() > existing.getSeverity()) {
			validationErrors.put(e, s);
		}
	}

	public void setStatusProvider(final IStatusProvider statusProvider) {
		this.statusProvider = statusProvider;
		statusProvider.addStatusChangedListener(statusChangedListener);
	}

	/**
	 */
	protected void updateObject(final EObject object, final IStatus status, final boolean update) {
		if (object != null) {
			setStatus(object, status);
			if (update) {
				viewer.update(object, null);
			}
		}
	}

	public IStatusProvider getStatusProvider() {
		return statusProvider;
	}

	public Map<Object, IStatus> getValidationErrors() {
		return validationErrors;
	}

	public void dispose() {
		validationErrors.clear();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
		}
	}
}
