/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

/**
 * This is an interface for an inline editor. It is given an input, and should generate commands and send them to the appropriate command processor when stuff happens. It doesn't handle layout or
 * provision of labels or anything.
 * 
 * @author Tom Hinton
 * 
 */
public interface IInlineEditor {
	public void setCommandHandler(final ICommandHandler handler);

	public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range);

	/**
	 * @since 5.0
	 */
	public Control createControl(final Composite parent, EMFDataBindingContext dbc, final FormToolkit toolkit);

	void processValidation(IStatus status);

	public EStructuralFeature getFeature();

	public EObject getEditorTarget();

	void setLabel(Label label);

	/**
	 * @return
	 * @since 2.0
	 */
	Label getLabel();

	/**
	 * @since 2.0
	 */
	void setEditorLocked(boolean locked);

	/**
	 * @since 2.0
	 */
	boolean isEditorLocked();

	/**
	 * @since 2.0
	 */
	void setEditorEnabled(boolean enabled);

	/**
	 * @since 2.0
	 */
	boolean isEditorEnabled();

	/**
	 * @since 2.0
	 */
	void setEditorVisible(boolean visible);

	/**
	 * @since 2.0
	 */
	boolean isEditorVisible();

	/**
	 * @since 2.0
	 */
	void addNotificationChangedListener(IInlineEditorExternalNotificationListener listener);

	/**
	 * @since 2.0
	 */
	void removeNotificationChangedListener(IInlineEditorExternalNotificationListener listener);

}