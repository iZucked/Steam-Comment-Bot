/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * Wrapper class implementation of {@link IInlineEditor} intended to be sub-classed to allow control enablement change on notifications. Sub-classes should implement
 * {@link MMXAdapterImpl#reallyNotifyChanged(org.eclipse.emf.common.notify.Notification)} and call {@link #setEnabled(boolean)}
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public abstract class IInlineEditorEnablementWrapper extends MMXAdapterImpl implements IInlineEditor {

	protected EObject input;
	protected Collection<EObject> ranges;

	protected final IInlineEditor wrapped;

	/**
	 * @since 2.0
	 */
	protected abstract boolean respondToNotification(final Notification notification);
	/**
	 * @since 2.0
	 */
	protected abstract boolean isEnabled();

	private final DisposeListener disposeListener = new DisposeListener() {
		@Override
		public void widgetDisposed(final DisposeEvent e) {
			if (input != null) {
				input.eAdapters().remove(IInlineEditorEnablementWrapper.this);
			}

			if (ranges != null) {
				for (final EObject eObj : ranges) {
					eObj.eAdapters().remove(IInlineEditorEnablementWrapper.this);
				}
			}

			e.widget.removeDisposeListener(this);
		}

	};

	public IInlineEditorEnablementWrapper(final IInlineEditor wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void setLabel(final Label label) {
		wrapped.setLabel(label);

	}

	@Override
	public void setCommandHandler(final ICommandHandler handler) {
		wrapped.setCommandHandler(handler);

	}

	@Override
	public void processValidation(final IStatus status) {
		wrapped.processValidation(status);
	}

	@Override
	public EStructuralFeature getFeature() {
		return wrapped.getFeature();
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		if (this.input != null) {
			this.input.eAdapters().remove(this);
		}
		if (this.ranges != null) {
			for (final EObject eObj : this.ranges) {
				eObj.eAdapters().remove(this);
			}
		}

		wrapped.display(location, scenario, object, range);
		this.input = object;

		if (this.input != null) {
			this.input.eAdapters().add(this);
		}

		this.ranges = range;
		if (this.ranges != null) {
			for (final EObject eObj : this.ranges) {
				eObj.eAdapters().add(this);
			}
		}
		
		setEditorEnabled(isEnabled());		
	}

	@Override
	public Control createControl(final Composite parent) {

		final Control c = wrapped.createControl(parent);
		c.addDisposeListener(disposeListener);

		return c;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setEditorEnabled(final boolean enabled) {
		wrapped.setEditorEnabled(enabled);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setEditorVisible(final boolean visible) {
		wrapped.setEditorVisible(visible);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setEditorLocked(final boolean locked) {
		wrapped.setEditorLocked(locked);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public boolean isEditorEnabled() {
		return wrapped.isEditorEnabled();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public boolean isEditorVisible() {
		return wrapped.isEditorVisible();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public boolean isEditorLocked() {
		return wrapped.isEditorLocked();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public EObject getEditorTarget() {
		return wrapped.getEditorTarget();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public Label getLabel() {
		return wrapped.getLabel();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void addNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
		wrapped.addNotificationChangedListener(listener);

	}	

	/**
	 * @since 2.0
	 */
	@Override
	public void removeNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
		wrapped.removeNotificationChangedListener(listener);
	}

	/**
	 * @since 2.0
	 */
	@Override
	protected void missedNotifications(final List<Notification> missed) {
		for (final Notification n : missed) {
			reallyNotifyChanged(n);
		}
		super.missedNotifications(missed);
	}
	
	/**
	 * Return the wrapped editor instance.
	 * 
	 * @since 2.0
	 * @return
	 */
	public IInlineEditor getWrapped() {
		return wrapped;
	}

	
	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (respondToNotification(notification)) {
			setEditorEnabled(isEnabled());
		}
	}	

	
};