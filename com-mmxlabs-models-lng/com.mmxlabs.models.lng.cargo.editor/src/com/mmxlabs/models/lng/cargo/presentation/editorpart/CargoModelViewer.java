package com.mmxlabs.models.lng.cargo.presentation.editorpart;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class CargoModelViewer extends ScenarioTableViewer {
	private JointModelEditorPart part;

	public CargoModelViewer(final Composite parent, final int style,
			final JointModelEditorPart part) {
		super(parent, style);
		this.part = part;
	}

	@Override
	public void init(AdapterFactory adapterFactory, EReference... path) {
		super.init(adapterFactory, path);
		final TypesPackage lngPkg = TypesPackage.eINSTANCE;
		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = part.getReferenceValueProviderCache();
		final EditingDomain editingDomain = part.getEditingDomain();
		
		addTypicalColumn(
				"ID",
				new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain));
		// and so on
		addTypicalColumn(
				"Load Port",
				new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_LoadSlot());
		addTypicalColumn(
				"Load Contract",
				new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_LoadSlot());
		
		addTypicalColumn(
				"Discharge Port",
				new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_DischargeSlot());
		addTypicalColumn(
				"Discharge Contract",
				new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_DischargeSlot());
	}
}
