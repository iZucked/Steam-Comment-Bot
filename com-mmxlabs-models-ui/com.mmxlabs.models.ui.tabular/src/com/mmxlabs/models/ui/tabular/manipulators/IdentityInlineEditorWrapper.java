/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;

/**
 * Generic identity wrapper class to support easy wrapping of inline editors.
 * 
 * @author tobi
 *
 * @param <T>: the attribute/reference manipulator being wrapped
 */
public abstract class IdentityInlineEditorWrapper implements IInlineEditor {
	protected final @NonNull IInlineEditor wrapped;

	protected IdentityInlineEditorWrapper(final @NonNull IInlineEditor wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void addNotificationChangedListener(@NonNull IInlineEditorExternalNotificationListener listener) {
		wrapped.addNotificationChangedListener(listener);
	}
	
	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		return wrapped.createControl(parent, dbc, toolkit);
	}
	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, Collection<EObject> range) {
		dialogContext.getDialogController().setEditorVisibility(object, getFeature(), isEditorVisible());
		wrapped.display(dialogContext, scenario, object, range);
	}
	@Override
	public EObject getEditorTarget() {
		return wrapped.getEditorTarget();
	}
	@Override
	public ETypedElement getFeature() {
		return wrapped.getFeature();
	}
	@Override
	public Label getLabel() {
		return wrapped.getLabel();
	}
	@Override
	public boolean hasLabel() {
		return wrapped.hasLabel();
	}
	@Override
	public boolean isEditorEnabled() {
		return wrapped.isEditorEnabled();
	}
	@Override
	public boolean isEditorLocked() {
		return wrapped.isEditorLocked();
	}
	@Override
	public boolean isEditorVisible() {
		return wrapped.isEditorVisible();
	}
	@Override
	public void processValidation(IStatus status) {
		wrapped.processValidation(status);
	}
	@Override
	public void removeNotificationChangedListener(@NonNull IInlineEditorExternalNotificationListener listener) {
		wrapped.removeNotificationChangedListener(listener);
	}
	@Override
	public void setCommandHandler(ICommandHandler handler) {
		wrapped.setCommandHandler(handler);
	}
	@Override
	public void setEditorEnabled(boolean enabled) {
		wrapped.setEditorEnabled(enabled);
	}
	@Override
	public void setEditorLocked(boolean locked) {
		wrapped.setEditorLocked(locked);
	}
	@Override
	public void setEditorVisible(boolean visible) {
		wrapped.setEditorVisible(visible);
	}
	@Override
	public void setLabel(Label label) {
		wrapped.setLabel(label);
	}
	@Override
	public @Nullable Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		return wrapped.createLayoutData(root, value, control);
	}
	@Override
	public IInlineEditor getProxy() {
		return wrapped.getProxy();
	}
	@Override
	public boolean needsFullWidth() {
		return wrapped.needsFullWidth();
	}
}
