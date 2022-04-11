/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelDecorator;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.NavigatorDecoratingLabelProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.common.Pair;
import com.mmxlabs.rcp.common.DualImageProgressRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.ui.IProgressProvider;
import com.mmxlabs.scenario.service.ui.IProgressProvider.RunType;
import com.mmxlabs.scenario.service.ui.IScenarioVersionService;

/**
 * Fork of the {@link NavigatorDecoratingLabelProvider} to override
 * {@link #getColumnImage(Object, int)};
 */
@SuppressWarnings("restriction")
public class ScenarioServiceNavigatorDecoratingLabelProvider extends DecoratingStyledCellLabelProvider
		implements IPropertyChangeListener, ILabelProvider, IColorProvider, ITableLabelProvider, ITableFontProvider {

	private Image bcImage;

	private static class StyledLabelProviderAdapter implements IStyledLabelProvider, ITableLabelProvider, IColorProvider, IFontProvider, ITableFontProvider {

		private final ILabelProvider provider;

		public StyledLabelProviderAdapter(final ILabelProvider provider) {
			this.provider = provider;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.
		 * IStyledLabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(final Object element) {
			return provider.getImage(element);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.
		 * IStyledLabelProvider#getStyledText(java.lang.Object)
		 */
		@Override
		public StyledString getStyledText(final Object element) {
			if (provider instanceof IStyledLabelProvider slp) {
				return slp.getStyledText(element);
			}
			String text = provider.getText(element);
			if (text == null)
				text = ""; //$NON-NLS-1$
			return new StyledString(text);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.
		 * viewers.ILabelProviderListener)
		 */
		@Override
		public void addListener(final ILabelProviderListener listener) {
			provider.addListener(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		@Override
		public void dispose() {
			provider.dispose();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.
		 * Object, java.lang.String)
		 */
		@Override
		public boolean isLabelProperty(final Object element, final String property) {
			return provider.isLabelProperty(element, property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface
		 * .viewers.ILabelProviderListener)
		 */
		@Override
		public void removeListener(final ILabelProviderListener listener) {
			provider.removeListener(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
		 */
		@Override
		public Color getBackground(final Object element) {
			if (provider instanceof IColorProvider) {
				return ((IColorProvider) provider).getBackground(element);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
		 */
		@Override
		public Color getForeground(final Object element) {
			if (provider instanceof IColorProvider) {
				return ((IColorProvider) provider).getForeground(element);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
		 */
		@Override
		public Font getFont(final Object element) {
			if (provider instanceof IFontProvider) {
				return ((IFontProvider) provider).getFont(element);
			}
			return null;
		}

		@Override
		public Font getFont(final Object element, final int columnIndex) {
			// While we implement ITableFontProvider, the common navigator label provider
			// does not, so just call getFont. We need to implement ITableFontProvider so
			// getFont will be called at all.
			return getFont(element);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.
		 * Object, int)
		 */
		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			if (provider instanceof ITableLabelProvider) {
				return ((ITableLabelProvider) provider).getColumnImage(element, columnIndex);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 * int)
		 */
		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			if (provider instanceof ITableLabelProvider) {
				return ((ITableLabelProvider) provider).getColumnText(element, columnIndex);
			}
			return null;
		}
	}

	private ServiceTracker<IProgressProvider, IProgressProvider> progressProviderTracker;
	private ServiceTracker<IScenarioVersionService, IScenarioVersionService> scenarioVersionsTracker;
	private ServiceTracker<IBaseCaseVersionsProvider, IBaseCaseVersionsProvider> baseCaseTracker;

	/**
	 * Creates a {@link ScenarioServiceNavigatorDecoratingLabelProvider}
	 * 
	 * @param commonLabelProvider the label provider to use
	 */
	public ScenarioServiceNavigatorDecoratingLabelProvider(final ILabelProvider commonLabelProvider) {
		super(new StyledLabelProviderAdapter(commonLabelProvider), PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.StyledCellLabelProvider#initialize(org.eclipse.
	 * jface.viewers.ColumnViewer, org.eclipse.jface.viewers.ViewerColumn)
	 */
	@Override
	public void initialize(final ColumnViewer viewer, final ViewerColumn column) {
		PlatformUI.getPreferenceStore().addPropertyChangeListener(this);
		JFaceResources.getColorRegistry().addListener(this);

		setOwnerDrawEnabled(showColoredLabels());

		super.initialize(viewer, column);

		initTracker();

		bcImage = CommonImages.getImage(IconPaths.BaseFlag, IconMode.Enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		PlatformUI.getPreferenceStore().removePropertyChangeListener(this);
		JFaceResources.getColorRegistry().removeListener(this);

		if (scenarioVersionsTracker != null) {
			scenarioVersionsTracker.getService().removeChangedListener(changeListenerScenarios);
			scenarioVersionsTracker.close();
		}
		if (baseCaseTracker != null) {
			baseCaseTracker.getService().removeChangedListener(changeListenerBaseCase);
			baseCaseTracker.close();
		}
		if (progressProviderTracker != null) {
			IProgressProvider service = progressProviderTracker.getService();
			if (service != null) {
				service.removeChangedListener(changeListenerProgress);
			}
			progressProviderTracker.close();
		}
	}

	private IScenarioVersionService.IChangedListener changeListenerScenarios = this::refresh;
	private IBaseCaseVersionsProvider.IBaseCaseChanged changeListenerBaseCase = this::refresh;
	private IProgressProvider.IProgressChanged changeListenerProgress = element -> {
		if (element != null) {
			ViewerHelper.refresh(getViewer(), element, false);
		} else {
			this.refresh();
		}
	};

	private void initTracker() {
		Bundle bundle = FrameworkUtil.getBundle(ScenarioServiceNavigatorDecoratingLabelProvider.class);
		{
			scenarioVersionsTracker = new ServiceTracker<IScenarioVersionService, IScenarioVersionService>(bundle.getBundleContext(), IScenarioVersionService.class, null) {
				@Override
				public IScenarioVersionService addingService(final ServiceReference<IScenarioVersionService> reference) {
					final IScenarioVersionService provider = super.addingService(reference);
					provider.addChangedListener(changeListenerScenarios);
					RunnerHelper.asyncExec(ScenarioServiceNavigatorDecoratingLabelProvider.this::refresh);

					return provider;
				}

				@Override
				public void removedService(final ServiceReference<IScenarioVersionService> reference, final IScenarioVersionService provider) {
					provider.removeChangedListener(changeListenerScenarios);
					super.removedService(reference, provider);
				}
			};
			scenarioVersionsTracker.open();
			final IScenarioVersionService s2 = scenarioVersionsTracker.getService();
			if (s2 != null) {
				s2.addChangedListener(changeListenerScenarios);
			}
		}
		{
			baseCaseTracker = new ServiceTracker<IBaseCaseVersionsProvider, IBaseCaseVersionsProvider>(bundle.getBundleContext(), IBaseCaseVersionsProvider.class, null) {

				@Override
				public IBaseCaseVersionsProvider addingService(final ServiceReference<IBaseCaseVersionsProvider> reference) {
					final IBaseCaseVersionsProvider provider = super.addingService(reference);
					provider.addChangedListener(changeListenerBaseCase);
					RunnerHelper.asyncExec(ScenarioServiceNavigatorDecoratingLabelProvider.this::refresh);

					return provider;
				}

				@Override
				public void removedService(final ServiceReference<IBaseCaseVersionsProvider> reference, final IBaseCaseVersionsProvider provider) {
					provider.removeChangedListener(changeListenerBaseCase);
					super.removedService(reference, provider);
				}
			};
			baseCaseTracker.open();
			final IBaseCaseVersionsProvider s2 = baseCaseTracker.getService();
			if (s2 != null) {
				s2.addChangedListener(changeListenerBaseCase);
			}
		}
		{
			progressProviderTracker = new ServiceTracker<IProgressProvider, IProgressProvider>(bundle.getBundleContext(), IProgressProvider.class, null) {
				@Override
				public IProgressProvider addingService(final ServiceReference<IProgressProvider> reference) {
					final IProgressProvider provider = super.addingService(reference);
					provider.addChangedListener(changeListenerProgress);
					RunnerHelper.asyncExec(ScenarioServiceNavigatorDecoratingLabelProvider.this::refresh);

					return provider;
				}

				@Override
				public void removedService(final ServiceReference<IProgressProvider> reference, final IProgressProvider provider) {
					provider.removeChangedListener(changeListenerProgress);
					super.removedService(reference, provider);
				}
			};
			progressProviderTracker.open();
			final IProgressProvider s2 = progressProviderTracker.getService();
			if (s2 != null) {
				s2.addChangedListener(changeListenerProgress);
			}
		}
	}

	private void refresh() {
		final ColumnViewer viewer = getViewer();
		if (viewer == null) {
			return;
		}
		final boolean showColoredLabels = showColoredLabels();
		if (showColoredLabels != isOwnerDrawEnabled()) {
			setOwnerDrawEnabled(showColoredLabels);
			viewer.refresh();
		} else if (showColoredLabels) {
			viewer.refresh();
		}
	}

	private static boolean showColoredLabels() {
		return PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.USE_COLORED_LABELS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.
	 * jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		final String property = event.getProperty();
		if (property.equals(JFacePreferences.QUALIFIER_COLOR) || property.equals(JFacePreferences.COUNTER_COLOR) || property.equals(JFacePreferences.DECORATIONS_COLOR)
				|| property.equals(IWorkbenchPreferenceConstants.USE_COLORED_LABELS)) {

			RunnerHelper.asyncExec(() -> refresh());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(final Object element) {
		return getStyledText(element).getString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.
	 * Object, int)
	 */
	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {

		if (columnIndex == ScenarioServiceNavigator.COLUMN_NAME_IDX) {

			IProgressProvider service = progressProviderTracker.getService();
			if (service != null) {
				ScenarioFragment fragment = null;
				ScenarioInstance instance = null;
				if (element instanceof ScenarioFragment) {
					fragment = (ScenarioFragment) element;
					instance = fragment.getScenarioInstance();
				}
				if (element instanceof ScenarioInstance) {
					instance = (ScenarioInstance) element;
				}
				if (instance != null) {
					Pair<Double, RunType> progress = service.getProgress(instance, fragment);
					if (progress != null) {
						if (progress.getSecond() == RunType.Local) {
							return DualImageProgressRenderer.renderProgress(CommonImages.IconPaths.Play_16, progress.getFirst());
						} else {
							return DualImageProgressRenderer.renderProgress(CommonImages.IconPaths.CloudPlay_16, progress.getFirst());
						}
					}
				}
			}
		}
		final Image image = ((StyledLabelProviderAdapter) getStyledStringProvider()).getColumnImage(element, columnIndex);

		if (columnIndex == ScenarioServiceNavigator.COLUMN_NAME_IDX) {

			IBaseCaseVersionsProvider service = baseCaseTracker.getService();
			if (service != null && element == service.getBaseCase()) {
				return bcImage;
			}

			if (this.getLabelDecorator() == null) {
				return image;
			}
			Image decorated = null;
			if (this.getLabelDecorator() instanceof LabelDecorator) {
				decorated = ((LabelDecorator) this.getLabelDecorator()).decorateImage(image, element, getDecorationContext());
			} else {
				decorated = this.getLabelDecorator().decorateImage(image, element);
			}
			if (decorated != null)
				return decorated;

			return image;
		}

		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 * int)
	 */
	@Override
	public String getColumnText(final Object element, final int columnIndex) {

		if (columnIndex == ScenarioServiceNavigator.COLUMN_STATUS_IDX) {
			if (element instanceof ScenarioInstance scenarioInstance) {
				IScenarioVersionService service = scenarioVersionsTracker.getService();
				if (service != null) {
					if (service.differentToBaseCase(scenarioInstance)) {
						return "↑";
					}
				}
			}
			return "";
		}

		return ((StyledLabelProviderAdapter) getStyledStringProvider()).getColumnText(element, columnIndex);
	}

	@Override
	public Font getFont(final Object element, final int columnIndex) {
		return ((StyledLabelProviderAdapter) getStyledStringProvider()).getFont(element, columnIndex);
	}

	@Override
	public Color getForeground(Object element) {
		return ((StyledLabelProviderAdapter) getStyledStringProvider()).getForeground(element);
	}
}
