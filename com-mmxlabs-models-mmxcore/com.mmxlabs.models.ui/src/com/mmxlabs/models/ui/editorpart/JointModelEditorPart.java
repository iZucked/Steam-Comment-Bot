package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;

/**
 * An editor part for editing MMX Root Objects.
 * 
 * TODO top level object may want to contribute functionality 
 * 
 * @author hinton
 *
 */
public class JointModelEditorPart extends MultiPageEditorPart implements IEditorPart, IEditingDomainProvider {
	private JointModel jointModel;
	private MMXRootObject rootObject;
	
	private AdapterFactoryEditingDomain editingDomain;
	private ComposedAdapterFactory adapterFactory;
	private BasicCommandStack commandStack;
	private  IReferenceValueProviderProvider referenceValueProviderCache = null;
	
	private List<IJointModelEditorContribution> contributions = new LinkedList<IJointModelEditorContribution>();
	
	public JointModelEditorPart() {
		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input)
			throws PartInitException {
		final JointModel jointModel = (JointModel) input.getAdapter(JointModel.class);
		if (jointModel == null) {
			throw new PartInitException("Could not adapt input to JointModel");
		} else {
			try {
				final MMXRootObject root = jointModel.load();
				if (root == null) {
					throw new PartInitException("JointModel has no root object");
				}
				this.jointModel = jointModel;
				this.rootObject = root;
				// set up adapter factory
				adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
				adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
				//TODO do we need more stuff here? I (tom) am not currently sure what this stuff really does.
				
				commandStack = new BasicCommandStack();
				// create editing domain
				editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
				// initialize extensions
				for (final MMXSubModel subModel : root.getSubModels()) {
					final IJointModelEditorContribution contribution = Activator.getDefault().getJointModelEditorContributionRegistry().createEditorContribution(subModel.eClass());
					if (contribution != null) {
						contribution.init(this, root, subModel.getSubModelInstance());
						contributions.add(contribution);
					}
				}
				referenceValueProviderCache = new ReferenceValueProviderCache(rootObject);
			} catch (MigrationException e) {
				throw new PartInitException("Error migrating joint model", e);
			} catch (IOException e) {
				throw new PartInitException("IO Exception loading joint model", e);
			}
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	protected void createPages() {
		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.addPages(getContainer());
		}
	}
	
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return referenceValueProviderCache;
	}

	/**
	 * Get a reference value provider for the given reference on the given EClass. Delegates to the result of
	 * {@link #getReferenceValueProviderCache()}
	 * 
	 * @param owner
	 * @param reference
	 * @return
	 */
	public IReferenceValueProvider getReferenceValueProvider(EClass owner,
			EReference reference) {
		return referenceValueProviderCache.getReferenceValueProvider(owner,
				reference);
	}
}
