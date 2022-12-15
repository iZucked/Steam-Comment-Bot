package com.mmxlabs.models.lng.transfers.editor.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class TransferAgreementTopDetailComposite extends DefaultTopLevelComposite {

	private Composite owner = this;
	
	private Runnable resizeAction = () -> {
		if (!TransferAgreementTopDetailComposite.this.isDisposed()) {
			TransferAgreementTopDetailComposite.this.layout(true, true);
		}
	};
	private IStatus status;
	public IDialogEditingContext getDialogContext() {
		return dialogContext;
	}
	
	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	public TransferAgreementTopDetailComposite(Composite parent, int style, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}
	
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
		containerComposite.setLayout(new GridLayout());
		containerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Group g = new Group(containerComposite, SWT.NONE);
		toolkit.adapt(g);

		g.setText("Transfer Agreement");
		g.setLayout(new GridLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel = new TransferAgreementDetailComposite(g, SWT.NONE, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
		
		doDisplay(object);
		
		this.setLayout(new GridLayout(1, true));
	}
	
	protected void doDisplay(final EObject object) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PRICING_BASES) && //
				LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PREFERRED_PRICING_BASES)) {
			if (!this.dialogContext.isMultiEdit() && object instanceof final TransferAgreement ta) {
				PreferredPricingBasisTableCreator.createPrefferedPBsTable(owner, toolkit, dialogContext, commandHandler, ta, //
						TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS, //
						statusProvider, resizeAction);
			}
			resizeAction.run();
		}
	}
	
	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		this.status = status;
		statusProvider.fireStatusChanged(status);
		topLevel.displayValidationStatus(status);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany();
	}

}
