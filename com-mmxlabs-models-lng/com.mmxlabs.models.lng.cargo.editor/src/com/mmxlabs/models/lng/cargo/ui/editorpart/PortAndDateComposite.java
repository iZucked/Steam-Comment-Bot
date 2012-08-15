package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.presentation.composites.SlotInlineEditorWrapper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

public class PortAndDateComposite extends Composite implements IDisplayComposite, IInlineEditorContainer {

	private Slot slot;
	protected final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	private ICommandHandler commandHandler;

	public PortAndDateComposite(final Composite parent, final int style, final Slot slot) {
		super(parent, style);
		setLayout(new GridLayout(4, false));

		addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__CONTRACT)));
		addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__PORT)));
		addInlineEditor(new SlotInlineEditorWrapper(ComponentHelperUtils.createDefaultEditor(CargoPackage.eINSTANCE.getSlot(), CargoPackage.Literals.SLOT__WINDOW_START)));

	}

	public Date getDate() {
		return slot.getWindowStart();
	}

	public Port getPort() {
		return slot.getPort();
	}

	public Contract getContract() {
		return slot.getContract();
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {
		editors.add(editor);
		editor.setCommandHandler(commandHandler);
		editor.createControl(this);
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject value, final Collection<EObject> range) {
		if (value instanceof Slot) {
			slot = (Slot) value;
		} else {
			slot = null;
		}
		for (final IInlineEditor editor : editors) {
			editor.setCommandHandler(commandHandler);
			editor.display(location, root, value, range);
		}

	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {

	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		for (final IInlineEditor editor : editors) {
			editor.processValidation(status);
		}
	}
}