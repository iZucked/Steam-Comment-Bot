/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.fleet.VesselStateAttributes;
import scenario.presentation.cargoeditor.celleditors.VesselStateAttributesDialog;

/**
 * A feature editor for vessel state attributes. Uses
 * {@link VesselStateAttributesDialog} for editing.
 * 
 * @author hinton
 * 
 */
public class VesselStateAttributesEditor implements IFeatureEditor {
	private EditingDomain editingDomain;

	public VesselStateAttributesEditor(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * scenario.presentation.cargoeditor.IFeatureEditor#getFeatureManipulator
	 * (java.util.List, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public IFeatureManipulator getFeatureManipulator(List<EReference> path,
			EStructuralFeature field) {

		return new BaseFeatureManipulator(path, field, editingDomain) {
			@Override
			public void setFromEditorValue(EObject row, Object value) {
				doSetCommand(getTarget(row), value);
			}

			@Override
			public String getStringValue(EObject row) {
				return "str";
			}

			@Override
			public Object getEditorValue(EObject row) {
				return getFieldValue(row);
			}

			@Override
			public CellEditor createCellEditor(final Composite parent) {
				return new DialogCellEditor(parent) {
					@Override
					protected Object openDialogBox(
							final Control cellEditorWindow) {
						final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(
								cellEditorWindow.getShell(),
								(SWT.DIALOG_TRIM & ~SWT.CLOSE)
										| SWT.APPLICATION_MODAL);

						return dlg.open((VesselStateAttributes) getValue());
					}

//					@Override
//					protected boolean dependsOnExternalFocusListener() {
//						return false;
//					}
				};
			}

			@Override
			public boolean canModify(EObject row) {
				return true;
			}
		};
	}

}
