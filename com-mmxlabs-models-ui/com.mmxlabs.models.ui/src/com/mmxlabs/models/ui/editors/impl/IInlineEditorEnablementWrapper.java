package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * Wrapper class implementation of {@link IInlineEditor} intended to be sub-classed to allow control enablement change on notifications. Sub-classes should implement
 * {@link MMXAdapterImpl#reallyNotifyChanged(org.eclipse.emf.common.notify.Notification)} and call {@link #setEnabled(boolean)}
 * 
 * @author Simon Goodall
 * 
 */
public abstract class IInlineEditorEnablementWrapper extends MMXAdapterImpl implements IInlineEditor {

	protected EObject input;

	protected final IInlineEditor wrapped;

	private final DisposeListener disposeListener = new DisposeListener() {
		@Override
		public void widgetDisposed(final DisposeEvent e) {
			if (input != null) {
				input.eAdapters().remove(IInlineEditorEnablementWrapper.this);
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
	public void display(final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		if (this.input != null) {
			this.input.eAdapters().remove(this);
		}

		wrapped.display(scenario, object, range);
		this.input = object;

		if (this.input != null) {
			this.input.eAdapters().add(this);
		}
	}

	@Override
	public Control createControl(final Composite parent) {

		final Control c = wrapped.createControl(parent);
		c.addDisposeListener(disposeListener);

		return c;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		wrapped.setEnabled(enabled);
	}

};