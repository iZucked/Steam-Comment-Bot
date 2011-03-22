package com.mmxlabs.rcp.navigator.scenario;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.rcp.navigator.Activator;
import com.mmxlabs.rcp.navigator.ecore.EcoreLabelProvider;

public class ScenarioLabelProvider extends WorkbenchLabelProvider implements
		ICommonLabelProvider, ITableLabelProvider {

	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	Image getCachedImage(final Object key) {

		if (imageCache.containsKey(key)) {
			return imageCache.get(key);
		}

		ImageDescriptor desc = null;
		if (key instanceof JobState) {
			JobState state = (JobState) key;

			switch (state) {
			case CANCELLED:
				return Display.getDefault().getSystemImage(SWT.ICON_ERROR);
			case CANCELLING:
				return Display.getDefault().getSystemImage(SWT.ICON_ERROR);

			case COMPLETED:

				desc = Activator
						.getImageDescriptor("/icons/elcl16/terminate_co.gif");
				break;
			case INITIALISED:
				desc = Activator
						.getImageDescriptor("/icons/elcl16/terminate_co.gif");
				break;
			case PAUSED:
				desc = Activator
						.getImageDescriptor("/icons/elcl16/suspend_co.gif");
				break;
			case PAUSING:
				desc = Activator
						.getImageDescriptor("/icons/dlcl16/suspend_co.gif");
				break;
			case RESUMING:
				desc = Activator
						.getImageDescriptor("/icons/dlcl16/resume_co.gif");
				break;
			case RUNNING:
				desc = Activator
						.getImageDescriptor("/icons/elcl16/resume_co.gif");
				break;
			case UNKNOWN:
				return Display.getDefault().getSystemImage(SWT.ICON_WARNING);
			}
		} else {
			desc = Activator.getImageDescriptor(key.toString());
		}

		// Cache image
		if (desc != null) {
			final Image img = desc.createImage();
			imageCache.put(key, img);
			return img;
		}

		return null;

	}

	@Override
	public void dispose() {

		for (final Image image : imageCache.values()) {
			image.dispose();
		}

		imageCache.clear();

		super.dispose();
	}

	EcoreLabelProvider ecoreLabelProvider = new EcoreLabelProvider();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {

			if (element instanceof IAdaptable) {
				Object scenario = ((IAdaptable) element)
						.getAdapter(Scenario.class);
				if (scenario != null) {
					return ecoreLabelProvider.getImage(scenario);
				}
			}

			return getImage(element);
		}

		if (columnIndex == 1) {
			if (element instanceof IResource) {
				final IManagedJob job = com.mmxlabs.jobcontoller.Activator.getDefault().getJobManager().findJobForResource((IResource) element);

				if (job != null) {
					return getCachedImage(job.getJobState());
				}
			}

		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0) {

			
			if (element instanceof IResource) {
				return ((IResource) element).getName();
			}
			return getText(element);
		}

		if (columnIndex == 1) {
			if (element instanceof IResource) {
				final IManagedJob job = com.mmxlabs.jobcontoller.Activator.getDefault().getJobManager().findJobForResource((IResource) element);
				if (job != null) {
					return job.getJobState().toString();
				}
				return "";
			}
		}

		else if (element instanceof Scenario) {
			return "" + ((Scenario) element).getVersion();
		} else if (element instanceof Schedule) {
			return ((Schedule) element).getName();
//		} else if (element instanceof ScenarioTreeNodeClass) {
//			Scenario scenario = ((ScenarioTreeNodeClass) element).getScenario();
//			if (scenario != null) {
//				return scenario.getName();
//			}
		}
		// TODO Auto-generated method stub
		return "";// + columnIndex;
	}

	@Override
	public void restoreState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDescription(Object anElement) {
		if (anElement instanceof IResource) {
			return ((IResource) anElement).getFullPath().makeRelative()
					.toString();
		}
		return null;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return super.isLabelProperty(element, property);
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		super.addListener(listener);
	}

	@Override
	protected void fireLabelProviderChanged(LabelProviderChangedEvent event) {
		// TODO Auto-generated method stub
		super.fireLabelProviderChanged(event);
	}
}
