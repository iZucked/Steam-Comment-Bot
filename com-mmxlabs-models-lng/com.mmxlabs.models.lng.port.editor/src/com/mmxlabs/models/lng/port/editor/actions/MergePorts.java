/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.editor.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.provider.PortItemProviderAdapterFactory;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.LockableAction;

/**
 * Action designed to merge together different port records. TODO - this can probably be made more generic
 * 
 * @author Simon Goodall
 * 
 */
public class MergePorts extends LockableAction {
	protected final IScenarioEditingLocation part;
	private final ScenarioTableViewer viewer;

	public MergePorts(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super("Merge Ports");
		this.viewer = viewer;
		this.part = part;

		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/merge.gif"));
	}

	// @Override
	// public void setEnabled(boolean enabled) {
	// // TODO Auto-generated method stub
	// super.setEnabled(enabled);
	// }
	//
	// @Override
	// public boolean isEnabled() {
	// if (viewer.getSelection() instanceof IStructuredSelection) {
	// IStructuredSelection iStructuredSelection = (IStructuredSelection) viewer.getSelection();
	// return iStructuredSelection.size() > 1;
	// }
	// return false;
	// }

	@Override
	public void run() {

		final ISelection selection = viewer.getSelection();
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}

		final List<Port> selectedPorts = new LinkedList<Port>();
		final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
		while (itr.hasNext()) {
			final Object o = itr.next();
			if (o instanceof Port) {
				selectedPorts.add((Port) o);
			}
		}
		if (selectedPorts.size() < 2) {
			return;
		}
		final ListDialog dialog = new ListDialog(part.getShell());
		dialog.setContentProvider(new ArrayContentProvider());
		dialog.setLabelProvider(new AdapterFactoryLabelProvider(new PortItemProviderAdapterFactory()));
		dialog.setInput(selectedPorts);

		dialog.setTitle("Merge Ports");
		dialog.setMessage("Choose one port to merge into");

		if (dialog.open() == Dialog.OK) {

			final Object[] res = dialog.getResult();

			if (res.length == 1) {
				try {
					part.setDisableCommandProviders(true);

					final EditingDomain domain = part.getEditingDomain();

					final Port retained = (Port) res[0];
					selectedPorts.remove(retained);

					final Command cmd = merge(domain, part.getRootObject(), retained, selectedPorts);
					if (cmd.canExecute()) {
						part.getEditingDomain().getCommandStack().execute(cmd);
					} else {
						throw new RuntimeException("Error running import command");
					}
				} finally {
					part.setDisableCommandProviders(false);
				}
			}
		}
	}

	public Command merge(final EditingDomain ed, final MMXRootObject rootObject, final Port retained, final Collection<Port> merges) {

		// Step 1. Choose real port

		final CompoundCommand cmd = new CompoundCommand("Merge ports");

		final Set<String> otherNames = new LinkedHashSet<String>();
		otherNames.add(retained.getName());
		otherNames.addAll(retained.getOtherNames());

		final Set<PortCapability> portCapabilities = new HashSet<PortCapability>(retained.getCapabilities());

		// Create a new location object to replace existing one
		final Location loc = PortFactory.eINSTANCE.createLocation();

		// Copy initial fields
		if (retained.getLocation() != null) {
			updateLocation(loc, retained);
		}

		String timeZone = retained.getTimeZone();
		String atobviacCode = retained.getAtobviacCode();
		String dataloyCode = retained.getDataloyCode();

		boolean updatedTimeZone = timeZone != null && !timeZone.isEmpty();
		boolean updatedAToBViaCCode = atobviacCode != null && !atobviacCode.isEmpty();
		boolean updatedDataloyCode = dataloyCode != null && !dataloyCode.isEmpty();

		for (final Port p : merges) {
			// Copy in names
			otherNames.add(p.getName());
			otherNames.addAll(p.getOtherNames());

			if (p.getLocation() != null) {
				// Update fields
				updateLocation(loc, p);
			}

			// Merge capabilities
			portCapabilities.addAll(p.getCapabilities());

			// Replace all references with retained port
			cmd.append(replace(ed, p, retained, rootObject));

			if (!updatedTimeZone) {
				if (p.getTimeZone() != null && !p.getTimeZone().isEmpty()) {
					timeZone = p.getTimeZone();
					updatedTimeZone = true;
				}
			}
			if (!updatedAToBViaCCode) {
				if (p.getAtobviacCode() != null && !p.getAtobviacCode().isEmpty()) {
					atobviacCode = p.getAtobviacCode();
					updatedAToBViaCCode = true;
				}
			}
			if (!updatedDataloyCode) {
				if (p.getDataloyCode() != null && !p.getDataloyCode().isEmpty()) {
					dataloyCode = p.getDataloyCode();
					updatedDataloyCode = true;
				}
			}
		}

		otherNames.remove(retained.getName());

		// Update Other names
		cmd.append(SetCommand.create(ed, retained, MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), otherNames));
		// Update location
		cmd.append(SetCommand.create(ed, retained, PortPackage.eINSTANCE.getPort_Location(), loc));
		// Update capabilities
		cmd.append(SetCommand.create(ed, retained, PortPackage.eINSTANCE.getPort_Capabilities(), portCapabilities));
		if (updatedAToBViaCCode) {
			cmd.append(SetCommand.create(ed, retained, PortPackage.eINSTANCE.getPort_AtobviacCode(), atobviacCode));
		}
		if (updatedDataloyCode) {
			cmd.append(SetCommand.create(ed, retained, PortPackage.eINSTANCE.getPort_DataloyCode(), dataloyCode));
		}
		if (updatedTimeZone) {
			cmd.append(SetCommand.create(ed, retained, PortPackage.eINSTANCE.getPort_TimeZone(), timeZone));
		}
		// Remove old references
		cmd.append(DeleteCommand.create(ed, merges));
		return cmd;
	}

	private void updateLocation(final Location loc, final Port p) {
		if (loc.getCountry() == null) {
			loc.setCountry(p.getLocation().getCountry());
		}
		if (loc.getLat() == 0.0) {
			loc.setLat(p.getLocation().getLat());
		}
		if (loc.getLon() == 0.0) {
			loc.setLon(p.getLocation().getLon());
		}
	}

	/**
	 * Create a command that replaces the old object with the new object in the given root object's tree
	 * 
	 * @param oldObject
	 * @param newObject
	 * @param rootObject
	 * @return
	 */
	private Command replace(final EditingDomain domain, final EObject oldObject, final EObject newObject, final MMXRootObject rootObject) {
		final CompoundCommand result = new CompoundCommand();
		result.append(IdentityCommand.INSTANCE);
		result.setDescription("Replacing " + oldObject + " with " + newObject);
		if (oldObject == null)
			return result;

		// update old references
		final Collection<Setting> refsToOldObject = EcoreUtil.UsageCrossReferencer.find(oldObject, rootObject);
		for (final Setting setting : refsToOldObject) {
			final EObject eObject = setting.getEObject();
			if (setting.getEStructuralFeature().isMany()) {
				result.append(ReplaceCommand.create(domain, eObject, setting.getEStructuralFeature(), oldObject, Collections.singleton(newObject)));
			} else {
				result.append(SetCommand.create(domain, eObject, setting.getEStructuralFeature(), newObject));
			}
		}

		if (newObject != null) {
			// recurse on contents
			final EList<EReference> newContainments = newObject.eClass().getEAllContainments();
			for (final EReference reference : oldObject.eClass().getEAllContainments()) {
				if (!reference.isMany() && newContainments.contains(reference)) {
					result.append(replace(domain, (EObject) oldObject.eGet(reference), (EObject) newObject.eGet(reference), rootObject));
				}
			}
		}

		return result;
	}
}
