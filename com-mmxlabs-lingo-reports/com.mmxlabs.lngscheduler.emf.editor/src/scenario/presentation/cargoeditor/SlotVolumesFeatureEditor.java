/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import scenario.presentation.cargoeditor.celleditors.RangeCellEditor;

/**
 * A feature editor for setting min/max volume on slots. Slightly hacky, but
 * nevermind.
 * 
 * @author hinton
 * 
 */
public class SlotVolumesFeatureEditor implements IFeatureEditor {
	private EditingDomain editingDomain;

	public SlotVolumesFeatureEditor(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {

		return new BaseFeatureManipulator(path, field, editingDomain) {
			@Override
			public void setFromEditorValue(final EObject row, final Object value) {
				final Integer[] editorValue = (Integer[]) value;
				final Slot target = (Slot) getTarget(row);
				
				final Integer[] currentValue = (Integer[]) getEditorValue(row);
				if (currentValue.equals(editorValue)) return;
				
				final EFactory ecf = EcoreFactory.eINSTANCE;
				final EDataType maxqType = CargoPackage.eINSTANCE.getSlot_MaxQuantity().getEAttributeType();
				final EDataType minqType = CargoPackage.eINSTANCE.getSlot_MinQuantity().getEAttributeType();
				
				final CompoundCommand cc = new CompoundCommand(
						CompoundCommand.LAST_COMMAND_ALL);
				cc.append(editingDomain.createCommand(SetCommand.class,
						new CommandParameter(target, CargoPackage.eINSTANCE.getSlot_MinQuantity(), 
						ecf.createFromString(minqType, editorValue[0].toString())		
						)));
				cc.append(editingDomain.createCommand(SetCommand.class,
						new CommandParameter(target, CargoPackage.eINSTANCE.getSlot_MaxQuantity(), 
								ecf.createFromString(maxqType, editorValue[1].toString())		
						)));
				
				editingDomain.getCommandStack().execute(cc);
			}

			@Override
			public String getStringValue(final EObject row) {
				final Slot target = (Slot) getTarget(row);
				return target.getMinQuantity() + " to " + target.getMaxQuantity();
			}

			@Override
			public Object getEditorValue(final EObject row) {
				final Slot target = (Slot) getTarget(row);
				
				return new Integer[]{target.getMinQuantity(), target.getMaxQuantity()};
			}

			@Override
			public CellEditor createCellEditor(final Composite parent) {
				return new RangeCellEditor(parent, SWT.NONE);
			}

			@Override
			public boolean canModify(final EObject row) {
				return true;
			}
		};
	}

}
