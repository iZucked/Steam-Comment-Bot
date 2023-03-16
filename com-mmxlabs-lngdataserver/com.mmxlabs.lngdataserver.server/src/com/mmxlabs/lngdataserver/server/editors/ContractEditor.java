/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.html.HtmlEscapers;
import com.mmxlabs.lngdataserver.server.api.IDatasetEditor;
import com.mmxlabs.lngdataserver.server.editors.model.EditorObject;
import com.mmxlabs.lngdataserver.server.editors.model.LayoutBuilder;
import com.mmxlabs.lngdataserver.server.editors.model.LayoutBuilder.RootBuilder;
import com.mmxlabs.lngdataserver.server.editors.model.layout.ILayoutElement;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ContractEditor implements IDatasetEditor {

	private final ComposedAdapterFactory FACTORY = createAdapterFactory();

	@Override
	public String getEditorName() {
		return "contracts";
	}

	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	@Override
	public String getJSONData(final IScenarioDataProvider sdp, final ServletRequest request) throws Exception {

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(sdp);

		final JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

		final ObjectMapper mapper = new ObjectMapper(jsonFactory);
		mapper.registerModule(new EMFJacksonModule());

		final List<EditorObject> objects = new LinkedList<>();
		final List<Contract> eobjs = new LinkedList<>();
		eobjs.addAll(commercialModel.getPurchaseContracts());
		eobjs.addAll(commercialModel.getSalesContracts());

		for (final var input : eobjs) {
			final EditorObject editor = new EditorObject();
			editor.displayName = input.getName();
			editor.id = input.getUuid();

			final IStatus status = runValidation(sdp, input);

//			List<ILayoutElement> priceInfoFeatures= new LinkedList<>();
//			{
//				LNGPriceCalculatorParameters pp = input.getPriceInfo();
//				if (pp instanceof ExpressionPriceParameters epp) {
//					LayoutBuilder builder2 = new LayoutBuilder(epp);
//
//					builder2 //
//							.withEditor().forAttributeAndStatus(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, status).make() //
//							.build();
//					priceInfoFeatures = builder2.build();
//				}
//			}
//			
			RootBuilder builder =  new LayoutBuilder().begin(sdp) //

			.withGroup() //.withLabel("HELLO") 
			.withRow() //
				.withColumn() //
				.withGroup().withLabel("Main")
				.withColumn() //
					.withEditor(input).forAttributeAndStatus(MMXCorePackage.Literals.NAMED_OBJECT__NAME, status).make() //
					.withRow() //
						.makeLabel("Volume") //
						.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__MIN_QUANTITY, status).withoutlabel().make() //
						.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__MAX_QUANTITY, status).withoutlabel().make() //
					.make() //

					.withRow() //
						.withColumn() //
							.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__START_DATE, status).make() //
							.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__END_DATE, status).make() //
						.make() //
					.make() //
					
					.addRelectively(input, status) //

					.make() //
					.make() //
				.make() //
				.withColumn() //
				.withGroup().withLabel("Pricing")
				.withColumn() //
//					.withChildren(priceInfoFeatures) //
				.addRelectively(input.getPriceInfo(), status) //
				.make() //
				.make() //
				.make() //

				.withColumn() //
				.withGroup().withLabel("Restrictions")
				.withColumn() //
				.withRow() //
					.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE, status).make() //
					.withEditor(input).forReferenceAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS, status).make() //
					.make() //
					.withRow() //
					.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE, status).make() //
					.withEditor(input).forReferenceAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS, status).make() //
					.make() //
					.withRow() //
					.withEditor(input).forAttributeAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE, status).make() //
					.withEditor(input).forReferenceAndStatus(CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS, status).make() //
					.make() //

//					.withChildren(priceInfoFeatures) //
				.make() //
				.make() //
				
				.make() //
			.make() //
			// Add remaining fields reflectively
			.make(); 

			editor.root = builder.build();
			
			objects.add(editor);
		}
 
		return mapper.writeValueAsString(objects);
	}

	private IStatus runValidation(final IScenarioDataProvider sdp, final Contract input) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

//			validator.addConstraintFilter((constraint, target) -> {
//
//				// If the set is populated, then we only want to target specific constraint ids.
//				if (!onlyConstraints.isEmpty() && !onlyConstraints.contains(constraint.getId())) {
//					return false;
//				}
//
//				for (final Category cat : constraint.getCategories()) {
//					if (cat.getId().endsWith(".base")) {
//						return true;
//					} else if (optimising && cat.getId().endsWith(".optimisation")) {
//						return true;
//					} else if (!optimising && cat.getId().endsWith(".evaluation")) {
//						return true;
//					}
//				}
//
//				return false;
//			});
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(sdp, false, false);
			return helper.runValidation(validator, extraContext, input, null);
		});
		return status;
	}

	@Override
	public String update(final IScenarioDataProvider sdp, final HttpServletRequest r) throws Exception {
		final ObjectMapper m = new ObjectMapper();
		final UpdateRecord ur = m.readValue(r.getInputStream(), UpdateRecord.class);
		final EditingDomain editingDomain = sdp.getEditingDomain();
		final CommandStack commandStack = sdp.getCommandStack();

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(sdp);

		final List<Contract> eobjs = new LinkedList<>();
		eobjs.addAll(commercialModel.getPurchaseContracts());
		eobjs.addAll(commercialModel.getSalesContracts());

		for (final var pc : eobjs) {
			if (Objects.equals(pc.getUuid(), ur.name)) {
				final EStructuralFeature eStructuralFeature = pc.eClass().getEStructuralFeature(ur.field);
				if (eStructuralFeature != null) {
					final EFactory eFactoryInstance = eStructuralFeature.getEType().getEPackage().getEFactoryInstance();

//					If refrence - convert to Paur abnd grab UUID

					final Object newValue = eFactoryInstance.createFromString((EDataType) eStructuralFeature.getEType(), ur.value.toString());

					final Command c = SetCommand.create(editingDomain, pc, eStructuralFeature, newValue);
					RunnerHelper.syncExecDisplayOptional(() -> commandStack.execute(c));

					final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
					validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

//					validator.addConstraintFilter((constraint, target) -> {
					//
//						// If the set is populated, then we only want to target specific constraint ids.
//						if (!onlyConstraints.isEmpty() && !onlyConstraints.contains(constraint.getId())) {
//							return false;
//						}
					//
//						for (final Category cat : constraint.getCategories()) {
//							if (cat.getId().endsWith(".base")) {
//								return true;
//							} else if (optimising && cat.getId().endsWith(".optimisation")) {
//								return true;
//							} else if (!optimising && cat.getId().endsWith(".evaluation")) {
//								return true;
//							}
//						}
					//
//						return false;
//					});
					final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
						final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(sdp, false, false);
						return helper.runValidation(validator, extraContext, pc, null);
					});

					final StringBuilder sb = new StringBuilder();
					final int severity = checkStatus(status, IStatus.OK, pc, eStructuralFeature, sb);
//					field.severity = severity;
//					field.msg = sb.toString();
//					
//					int ii = 0;
//					HTMLC
//					URLEncoder.
					return String.format("{ \"severity\" : %d, \"msg\" : \"%s\" }", severity, HtmlEscapers.htmlEscaper().escape(sb.toString().replace("\n", "\\n")));
				}
			}
		}

		return "{}";
	}

	static class UpdateRecord {
		public String name;
		public String field;
		public Object value;
	}

	/**
	 * Check status message applies to this editor.
	 * 
	 * @param status
	 * @return
	 */
	protected int checkStatus(final IStatus status, int currentSeverity, final EObject input, final ETypedElement typedElement, final StringBuilder sb) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				final int severity = checkStatus(element, currentSeverity, input, typedElement, sb);

				// Is severity worse, then note it
				if (severity > currentSeverity) {
					currentSeverity = element.getSeverity();
				}

			}
		}
		if (status instanceof final IDetailConstraintStatus element) {
			final Collection<EObject> objects = element.getObjects();
			if (objects.contains(input)) {
				if (element.getFeaturesForEObject(input).contains(typedElement)) {

					sb.append(element.getMessage());
					sb.append("\n");

					// Is severity worse, then note it
					if (element.getSeverity() > currentSeverity) {
						currentSeverity = element.getSeverity();
					}

					return currentSeverity;
				}
			}
		}

		return currentSeverity;
	}

	private String getDisplayLabel(final Contract input, final ETypedElement typedElement) {
		// Set to blank by default - and replace below if the feature is
		// found
		String toolTip = "";
		String labelText = "";
		// This will fetch the property source of the input object
		final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);
		// Iterate through the property descriptors to find a matching
		// descriptor for the feature
		for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

			if (typedElement.equals(descriptor.getFeature(input))) {
				// Found match

//							this.propertyDescriptor = descriptor;

				toolTip = descriptor.getDescription(input).replace("{0}", EditorUtils.unmangle(input.eClass().getName()).toLowerCase());

				labelText = descriptor.getDisplayName(input);
				break;
			}
		}
		return labelText;
	}
}
