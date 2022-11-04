package com.mmxlabs.lingo.reports.views.scenarioDiff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl;
import com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class ScenarioDiffUtil {
	public static List<ScenarioDiffData> pairingListElement(List<String> str1, List<String> str2) {
		List<ScenarioDiffData> res = new ArrayList<>();
		if (str1.size() == 1) {
			str1 = new ArrayList<>();
		}
		if (str2.size() == 1) {
			str1 = new ArrayList<>();
		}
		if (str1.size() >= str2.size()) {
			int i = 0;
			while (i < str2.size()) {
				String s1 = str1.get(i);
				String s2 = str2.get(i);
				Map<String, String> map = new HashMap<>();
				map.put("", s2);
				map.put("pinned", s1);
				ScenarioDiffData d = new ScenarioDiffData(map, "");
				res.add(d);
				i++;
			}
			for (int j = str2.size(); j < str1.size(); j++) {
				String s1 = str1.get(j);
				Map<String, String> map = new HashMap<>();
				map.put("", "");
				map.put("pinned", s1);
				ScenarioDiffData d = new ScenarioDiffData(map, "");
				res.add(d);
			}
		} else {
			int i = 0;
			while (i < str1.size()) {
				String s1 = str1.get(i);
				String s2 = str2.get(i);
				Map<String, String> map = new HashMap<>();
				map.put("", s2);
				map.put("pinned", s1);
				ScenarioDiffData d = new ScenarioDiffData(map, "");
				res.add(d);
				i++;
			}
			for (int j = str1.size(); j < str2.size(); j++) {
				String s2 = str2.get(j);
				Map<String, String> map = new HashMap<>();
				map.put("pinned", "");
				map.put("", s2);
				ScenarioDiffData d = new ScenarioDiffData(map, "");
				res.add(d);
			}

		}
		return res;
	}

	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	public static String fetchRowName(EObject input, EStructuralFeature f) {
		String labelText = "";
		if (input != null) {
			final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(input, IItemPropertySource.class);

			for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(input)) {

				if (f.equals(descriptor.getFeature(input))) {
					labelText = descriptor.getDisplayName(input);

					return labelText;

				}
			}
		}
		labelText = f.getName();
		return labelText;
	}

	public static List<ScenarioDiffData> extraDataGen(EObject eo1, EObject eo2) {
		List<ScenarioDiffData> result = new ArrayList<>();
		if (eo1 instanceof Port) {
			Port o = (Port) eo1;
			Port ot = (Port) eo2;

			if (!o.getName().equals(ot.getName()))
				;
			{
				Map<String, String> childMap = new HashMap<String, String>();
				childMap.put("pinned", o.getName());
				childMap.put("", ot.getName());
				ScenarioDiffData child = new ScenarioDiffData(childMap, "Port Name");
				result.add(child);
			}

			Location lo = o.getLocation();
			Location lot = ot.getLocation();
			List<EStructuralFeature> lfs = PortPackage.eINSTANCE.getLocation().getEStructuralFeatures();
			result.addAll(diffDataGen(lo, lot, lfs, new HashSet<>()));

		}
		if (eo1 instanceof VesselEvent) {
			VesselCharter vo = (VesselCharter) ((VesselEvent) eo1).getVesselAssignmentType();
			VesselCharter vot = (VesselCharter) ((VesselEvent) eo2).getVesselAssignmentType();
			Vessel v1 = vo.getVessel();
			Vessel v2 = vot.getVessel();
			if (!v1.getName().equals(v2.getName())) {
				Map<String, String> childMap = new HashMap<String, String>();
				childMap.put("pinned", v1.getName());
				childMap.put("", v2.getName());
				ScenarioDiffData child = new ScenarioDiffData(childMap, "Vessel");
				result.add(child);
			}
		}

		if (eo1 instanceof LoadSlot) {

			LoadSlot e1 = (LoadSlot) eo1;
			LoadSlot e2 = (LoadSlot) eo2;
			if (e1.getCargo().getVesselAssignmentType() == null || getVesselHelper(e1.getCargo().getVesselAssignmentType()) == null && e2.getCargo().getVesselAssignmentType() == null
					|| getVesselHelper(e2.getCargo().getVesselAssignmentType()) == null) {
				return result;
			}
			if (e1.getCargo().getVesselAssignmentType() == null || getVesselHelper(e1.getCargo().getVesselAssignmentType()) == null) {

				Map<String, String> childMap = new HashMap<>();
				childMap.put("pinned", "");
				childMap.put("", ((VesselCharter) e2.getCargo().getVesselAssignmentType()).getVessel().getName());
				ScenarioDiffData child = new ScenarioDiffData(childMap, "Vessel");
				result.add(child);
				return result;
			}
			if (e2.getCargo().getVesselAssignmentType() == null || getVesselHelper(e2.getCargo().getVesselAssignmentType()) == null) {

				Map<String, String> childMap = new HashMap<>();
				childMap.put("pinned", ((VesselCharter) e1.getCargo().getVesselAssignmentType()).getVessel().getName());
				childMap.put("", "");
				ScenarioDiffData child = new ScenarioDiffData(childMap, "Vessel");
				result.add(child);
				return result;
			}

			Vessel v1 = getVesselHelper(e1.getCargo().getVesselAssignmentType());
			Vessel v2 = getVesselHelper(e2.getCargo().getVesselAssignmentType());
			if (!v1.getName().equals(v2.getName())) {
				Map<String, String> childMap = new HashMap<String, String>();
				childMap.put("pinned", v1.getName());
				childMap.put("", v2.getName());
				ScenarioDiffData child = new ScenarioDiffData(childMap, "Vessel");
				result.add(child);
			}
		}
		return result;
	}

	public static Vessel getVesselHelper(VesselAssignmentType vat) {
		Vessel v = null;
		if (vat == null) {
			return v;
		}
		if (vat instanceof CharterInMarket) {
			return ((CharterInMarket) vat).getVessel();
		}
		if (vat instanceof VesselCharter) {
			return ((VesselCharter) vat).getVessel();
		}
		if (vat instanceof CharterInMarketOverride) {
			return ((CharterInMarketOverride) vat).getCharterInMarket().getVessel();
		}

		return v;
	}

	public static List<ScenarioDiffData> diffSingleDataGen(EObject eo1, EObject eo2, List<EStructuralFeature> features, Set<EStructuralFeature> blackList1) {
		List<ScenarioDiffData> result = new ArrayList<>();
		if (features != null && !features.isEmpty()) {
			for (EStructuralFeature f : features) {
				if (f instanceof EAttribute) {
					if ((eo1 == null || eo1.eGet(f) == null) && (eo2 != null && eo2.eGet(f) != null)) {

						if (eo2.eGet(f) instanceof Integer i) {
							if (i == 0)
								continue;

						}
						if (eo2.eGet(f) instanceof Boolean b) {
							if (!b)
								continue;

						}
						if (eo2.eGet(f).toString().equals("") || eo2.eGet(f).toString().equals("None") || eo2.eGet(f).toString().equals("NOTSET")) {
							continue;
						}

						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", "");
						childMap.put("", eo2.eGet(f).toString());
						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo2, f));
						result.add(child);
						continue;
					}
					if ((eo2 == null || eo2.eGet(f) == null) && (eo1 != null && eo1.eGet(f) != null)) {

						if (eo1.eGet(f) instanceof Integer i) {
							if (i == 0)
								continue;

						}
						if (eo1.eGet(f) instanceof Boolean b) {
							if (!b)
								continue;

						}
						if (eo1.eGet(f).toString().equals("") || eo1.eGet(f).toString().equals("None") || eo1.eGet(f).toString().equals("NOTSET")) {
							continue;
						}

						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", eo1.eGet(f).toString());
						childMap.put("", "");
						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
						result.add(child);
						continue;
					}
					if (eo1 != null && eo2 != null && eo1.eGet(f) != null && eo2.eGet(f) != null && !eo1.eGet(f).equals(eo2.eGet(f))) {

						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", eo1.eGet(f).toString());
						childMap.put("", eo2.eGet(f).toString());
						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
						result.add(child);
						continue;
					}
				} else {

					if (eo1.eGet(f) == eo2.eGet(f)) {
						continue;
					}

					if (eo1.eGet(f) == null) {
						if (eo2.eGet(f) instanceof NamedObject e2 && !(eo2.eGet(f).toString().equals("") || eo2.eGet(f).toString().equals("None") || eo2.eGet(f).toString().equals("NOTSET"))) {
							Map<String, String> childMap = new HashMap<String, String>();
							childMap.put("pinned", "");
							childMap.put("", e2.getName());
							ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
							result.add(child);
							continue;
						}
						if (eo2.eGet(f) instanceof List elist && !elist.isEmpty()) {
							if (((EReference) f).getEReferenceType().getEAllSuperTypes().contains(MMXCorePackage.eINSTANCE.getNamedObject())) {
								List<NamedObject> elist2 = (List<NamedObject>) elist;
								Set<NamedObject> eSet2 = elist2.stream().collect(Collectors.toSet());
								List<NamedObject> res2 = new ArrayList<>();
								List<String> str2 = new ArrayList<>();
								StringBuilder sb2 = new StringBuilder();

								for (NamedObject no : eSet2) {
									res2.add(no);
								}
								for (NamedObject no : res2) {
									str2.add(no.getName());
								}
								Collections.sort(str2);
								for (String s : str2) {
									sb2.append(s);
									sb2.append("\n, ");
								}
								if (!res2.isEmpty()) {
									Map<String, String> parentMap = new HashMap<>();
									parentMap.put("", sb2.toString());
									parentMap.put("pinned", "");
									ScenarioDiffData parent = new ScenarioDiffData(parentMap, fetchRowName(eo1, f));

									List<ScenarioDiffData> children = pairingListElement(new ArrayList<>(), str2);

									for (ScenarioDiffData sdd : children) {
										parent.children.add(sdd);
										sdd.parent = parent;
									}

									result.add(parent);
								}
							}
							continue;
						}
						if (f.getName().equals("priceInfo") && eo2 instanceof PurchaseContract || eo2 instanceof SalesContract) {
							ExpressionPriceParametersImpl dse2 = (ExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse2, null, dse2.eClass().getEStructuralFeatures(), blackList1);
							// System.out.println(singleData);
							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("priceInfo") && eo2 instanceof SpotMarket) {
							DateShiftExpressionPriceParametersImpl dse2 = (DateShiftExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse2, null, dse2.eClass().getEStructuralFeatures(), blackList1);
							// System.out.println(singleData);
							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						continue;
					}

					if (eo2.eGet(f) == null) {
						if (eo1.eGet(f) instanceof NamedObject e1 && !(eo1.eGet(f).toString().equals("") || eo1.eGet(f).toString().equals("None") || eo1.eGet(f).toString().equals("NOTSET"))) {
							Map<String, String> childMap = new HashMap<String, String>();
							childMap.put("pinned", "");
							childMap.put("", e1.getName());
							ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
							result.add(child);
							continue;
						}
						if (eo1.eGet(f) instanceof List elist && !elist.isEmpty()) {
							if (((EReference) f).getEReferenceType().getEAllSuperTypes().contains(MMXCorePackage.eINSTANCE.getNamedObject())) {
								List<NamedObject> elist1 = (List<NamedObject>) elist;
								Set<NamedObject> eSet1 = elist1.stream().collect(Collectors.toSet());
								List<NamedObject> res1 = new ArrayList<>();
								List<String> str1 = new ArrayList<>();
								StringBuilder sb1 = new StringBuilder();

								for (NamedObject no : eSet1) {
									res1.add(no);
								}
								for (NamedObject no : res1) {
									str1.add(no.getName());
								}
								Collections.sort(str1);
								for (String s : str1) {
									sb1.append(s);
									sb1.append("\n, ");
								}
								if (!res1.isEmpty()) {
									Map<String, String> parentMap = new HashMap<>();
									parentMap.put("", sb1.toString());
									parentMap.put("pinned", "");
									ScenarioDiffData parent = new ScenarioDiffData(parentMap, fetchRowName(eo1, f));

									List<ScenarioDiffData> children = pairingListElement(str1, new ArrayList<>());
									for (ScenarioDiffData sdd : children) {
										parent.children.add(sdd);
										sdd.parent = parent;
									}

									result.add(parent);
								}
							}
							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof PurchaseContract || eo1 instanceof SalesContract) {
							ExpressionPriceParametersImpl dse1 = (ExpressionPriceParametersImpl) eo1.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, null, dse1.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof SpotMarket) {
							DateShiftExpressionPriceParametersImpl dse1 = (DateShiftExpressionPriceParametersImpl) eo1.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, null, dse1.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						continue;
					}

					if (!(eo1.eGet(f) == null) && !(eo2.eGet(f) == null)) {
						if (eo1.eGet(f) instanceof NamedObject) {

							NamedObject e1 = (NamedObject) eo1.eGet(f);
							NamedObject e2 = (NamedObject) eo2.eGet(f);
							if (e1.getName().equalsIgnoreCase(e2.getName())) {

							} else {
								Map<String, String> childMap = new HashMap<String, String>();
								childMap.put("pinned", e1.getName());
								childMap.put("", e2.getName());
								ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
								result.add(child);
							}
							continue;
						}
					}

				}
			}

		}
		return result;
	}

	public static List<ScenarioDiffData> diffDataGen(EObject eo1, EObject eo2, List<EStructuralFeature> features, Set<EStructuralFeature> blackList1) {
		List<ScenarioDiffData> result = new ArrayList<>();
		if (features != null && !features.isEmpty()) {
			for (EStructuralFeature f : features) {
				if (blackList1.contains(f)) {
					continue;
				}

				if (eo1.eGet(f) == eo2.eGet(f)) {
					continue;
				}

				if (f instanceof EAttribute) {

					if ((eo1.eGet(f) == null) && (eo2.eGet(f) != null) && !(eo2.eGet(f).toString().equals("") || eo2.eGet(f).toString().equals("None") || eo2.eGet(f).toString().equals("NOTSET"))) {

						if (eo2.eGet(f) instanceof Integer i) {
							if (i == 0)
								continue;
						}
						if (eo2.eGet(f) instanceof Boolean b) {
							if (!b)
								continue;
						}

						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", "");
						childMap.put("", eo2.eGet(f).toString());

						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo2, f));
						result.add(child);
						continue;
					}
					if ((eo2.eGet(f) == null) && !(eo1.eGet(f).toString().equals("") || eo1.eGet(f).toString().equals("None") || eo1.eGet(f).toString().equals("NOTSET"))) {

						if (eo1.eGet(f) instanceof Integer i) {
							if (i == 0)
								continue;
						}
						if (eo1.eGet(f) instanceof Boolean b) {
							if (!b)
								continue;
						}

						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", eo1.eGet(f).toString());
						childMap.put("", "");
						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
						result.add(child);
						continue;
					}
					if (eo1.eGet(f) != null && eo2.eGet(f) != null && !eo1.eGet(f).equals(eo2.eGet(f))) {
						Map<String, String> childMap = new HashMap<String, String>();
						childMap.put("pinned", eo1.eGet(f).toString());
						childMap.put("", eo2.eGet(f).toString());
						ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
						result.add(child);
						continue;
					}
				} else {

					if (eo1.eGet(f) == null) {
						if (eo2.eGet(f) instanceof NamedObject e2 && !(eo2.eGet(f).toString().equals("") || eo2.eGet(f).toString().equals("None") || eo2.eGet(f).toString().equals("NOTSET"))) {
							Map<String, String> childMap = new HashMap<String, String>();
							childMap.put("pinned", "");
							childMap.put("", e2.getName());
							ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
							result.add(child);
							continue;
						}
						if (eo2.eGet(f) instanceof List elist && !elist.isEmpty()) {
							if (((EReference) f).getEReferenceType().getEAllSuperTypes().contains(MMXCorePackage.eINSTANCE.getNamedObject())) {
								List<NamedObject> elist2 = (List<NamedObject>) elist;
								Set<NamedObject> eSet2 = elist2.stream().collect(Collectors.toSet());
								List<NamedObject> res2 = new ArrayList<>();
								List<String> str2 = new ArrayList<>();
								StringBuilder sb2 = new StringBuilder();

								for (NamedObject no : eSet2) {
									res2.add(no);
								}
								for (NamedObject no : res2) {
									str2.add(no.getName());
								}
								Collections.sort(str2);
								for (String s : str2) {
									sb2.append(s);
									sb2.append("\n, ");
								}
								if (!res2.isEmpty()) {
									Map<String, String> parentMap = new HashMap<>();
									parentMap.put("", sb2.toString());
									parentMap.put("pinned", "");
									ScenarioDiffData parent = new ScenarioDiffData(parentMap, fetchRowName(eo1, f));

									List<ScenarioDiffData> children = pairingListElement(new ArrayList<>(), str2);

									for (ScenarioDiffData sdd : children) {
										parent.children.add(sdd);
										sdd.parent = parent;
									}

									result.add(parent);
								}
							}
							continue;
						}
						if (f.getName().equals("priceInfo") && eo2 instanceof PurchaseContract || eo2 instanceof SalesContract) {
							ExpressionPriceParametersImpl dse2 = (ExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse2, null, dse2.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("priceInfo") && eo2 instanceof SpotMarket) {
							DateShiftExpressionPriceParametersImpl dse2 = (DateShiftExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse2, null, dse2.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						continue;
					}

					if (eo2.eGet(f) == null) {
						if (eo1.eGet(f) instanceof NamedObject e1 && !(eo1.eGet(f).toString().equals("") || eo1.eGet(f).toString().equals("None") || eo1.eGet(f).toString().equals("NOTSET"))) {
							Map<String, String> childMap = new HashMap<String, String>();
							childMap.put("pinned", "");
							childMap.put("", e1.getName());
							ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
							result.add(child);
							continue;
						}
						if (eo1.eGet(f) instanceof List elist && !elist.isEmpty()) {
							if (((EReference) f).getEReferenceType().getEAllSuperTypes().contains(MMXCorePackage.eINSTANCE.getNamedObject())) {
								List<NamedObject> elist1 = (List<NamedObject>) elist;
								Set<NamedObject> eSet1 = elist1.stream().collect(Collectors.toSet());
								List<NamedObject> res1 = new ArrayList<>();
								List<String> str1 = new ArrayList<>();
								StringBuilder sb1 = new StringBuilder();

								for (NamedObject no : eSet1) {
									res1.add(no);
								}
								for (NamedObject no : res1) {
									str1.add(no.getName());
								}
								Collections.sort(str1);
								for (String s : str1) {
									sb1.append(s);
									sb1.append("\n, ");
								}
								if (!res1.isEmpty()) {
									Map<String, String> parentMap = new HashMap<>();
									parentMap.put("", sb1.toString());
									parentMap.put("pinned", "");
									ScenarioDiffData parent = new ScenarioDiffData(parentMap, fetchRowName(eo1, f));

									List<ScenarioDiffData> children = pairingListElement(str1, new ArrayList<>());
									for (ScenarioDiffData sdd : children) {
										parent.children.add(sdd);
										sdd.parent = parent;
									}

									result.add(parent);
								}
							}
							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof PurchaseContract || eo1 instanceof SalesContract) {
							ExpressionPriceParametersImpl dse1 = (ExpressionPriceParametersImpl) eo1.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, null, dse1.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof SpotMarket) {
							DateShiftExpressionPriceParametersImpl dse1 = (DateShiftExpressionPriceParametersImpl) eo1.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, null, dse1.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						continue;
					}

					if (!(eo1.eGet(f) == null) && !(eo2.eGet(f) == null)) {
						if (eo1.eGet(f) instanceof NamedObject) {

							NamedObject e1 = (NamedObject) eo1.eGet(f);
							NamedObject e2 = (NamedObject) eo2.eGet(f);
							if (e1.getName().equalsIgnoreCase(e2.getName())) {

							} else {
								Map<String, String> childMap = new HashMap<String, String>();
								childMap.put("pinned", e1.getName());
								childMap.put("", e2.getName());
								ScenarioDiffData child = new ScenarioDiffData(childMap, fetchRowName(eo1, f));
								result.add(child);
							}
							continue;
						}

						if (eo1.eGet(f) instanceof List elist) {
							if (((EReference) f).getEReferenceType().getEAllSuperTypes().contains(MMXCorePackage.eINSTANCE.getNamedObject())) {
								List<NamedObject> elist1 = (List<NamedObject>) elist;
								List<NamedObject> elist2 = (List<NamedObject>) eo2.eGet(f);
								Set<NamedObject> eSet1 = elist1.stream().collect(Collectors.toSet());
								Set<NamedObject> eSet2 = elist2.stream().collect(Collectors.toSet());
								List<NamedObject> res1 = new ArrayList<>();
								List<NamedObject> res2 = new ArrayList<>();
								List<String> str1 = new ArrayList<>();
								List<String> str2 = new ArrayList<>();

								String s1 = "";
								String s2 = "";

								StringBuilder sb1 = new StringBuilder();
								StringBuilder sb2 = new StringBuilder();
								for (NamedObject no : eSet1) {
									boolean exclude = true;
									for (NamedObject pivot : eSet2) {
										if (pivot.getName().equalsIgnoreCase(no.getName())) {
											exclude = false;
										}
									}
									if (exclude) {
										res1.add(no);
									}

								}
								for (NamedObject no : eSet2) {
									boolean exclude = true;
									for (NamedObject pivot : eSet1) {
										if (pivot.getName().equalsIgnoreCase(no.getName())) {
											exclude = false;
										}
									}
									if (exclude) {
										res2.add(no);
									}
								}

								for (NamedObject no : res1) {
									str1.add(no.getName());

								}

								for (NamedObject no : res2) {
									str2.add(no.getName());
								}
								Collections.sort(str1);
								Collections.sort(str2);
								for (String s : str1) {
									s1 = s;
									sb1.append(s);
									sb1.append("\n, ");
								}
								for (String s : str2) {
									s2 = s;
									sb2.append(s);
									sb2.append("\n, ");
								}
								if (!res1.isEmpty() || !res2.isEmpty()) {

									Map<String, String> parentMap = new HashMap<>();
									ScenarioDiffData parent = new ScenarioDiffData(parentMap, fetchRowName(eo1, f));
									if (str1.size() <= 1 && str2.size() <= 1) {
										parentMap.put("", s2);
										parentMap.put("pinned", s1);
									} else {
										parentMap.put("", sb2.toString());
										parentMap.put("pinned", sb1.toString());
										List<ScenarioDiffData> children = pairingListElement(str1, str2);
										for (ScenarioDiffData sdd : children) {
											parent.children.add(sdd);
											sdd.parent = parent;
										}
									}

									result.add(parent);
								}
							}
							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof PurchaseContract || eo1 instanceof SalesContract && eo1.eGet(f) instanceof ExpressionPriceParametersImpl) {
							ExpressionPriceParametersImpl dse1 = (ExpressionPriceParametersImpl) eo1.eGet(f);
							ExpressionPriceParametersImpl dse2 = (ExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, dse2, dse1.eClass().getEStructuralFeatures(), blackList1);
							// System.out.println(singleData);
							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("priceInfo") && eo1 instanceof SpotMarket) {
							DateShiftExpressionPriceParametersImpl dse1 = (DateShiftExpressionPriceParametersImpl) eo1.eGet(f);
							DateShiftExpressionPriceParametersImpl dse2 = (DateShiftExpressionPriceParametersImpl) eo2.eGet(f);
							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(dse1, dse2, dse1.eClass().getEStructuralFeatures(), blackList1);

							for (ScenarioDiffData onePiece : singleData) {
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("requiredHeel") && eo1 instanceof CharterOutEvent) {

							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen((EObject) eo1.eGet(f), (EObject) eo2.eGet(f),
									((EObject) eo1.eGet(f)).eClass().getEStructuralFeatures(), blackList1);
							for (ScenarioDiffData onePiece : singleData) {
								onePiece.id = "Delivery Heel " + onePiece.id;
								result.add(onePiece);
							}

							continue;
						}
						if (f.getName().equals("availableHeel") && eo1 instanceof CharterOutEvent) {

							List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen((EObject) eo1.eGet(f), (EObject) eo2.eGet(f),
									((EObject) eo1.eGet(f)).eClass().getEStructuralFeatures(), blackList1);
							for (ScenarioDiffData onePiece : singleData) {
								onePiece.id = "Redelivery Heel " + onePiece.id;
								result.add(onePiece);
							}

							continue;
						}

					}

				}
			}

		}
		return result;
	}

}
