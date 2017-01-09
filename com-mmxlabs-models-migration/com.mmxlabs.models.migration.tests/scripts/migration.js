/*
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
		{
					print(oldFeatures);
					print(v2Object);

			// Convert unknown features
			if (oldFeatures.containsKey(v2Object)) {
					print("Yup");

				var anyType = oldFeatures.get(v2Object);
				var itr = anyType.getAnyAttribute().iterator();
				while (itr.hasNext()) {
					var e = itr.next();
					var eStructuralFeature = e.getEStructuralFeature();
					// Copy value from attributeB to attributeC
				print(eStructuralFeature.getName());
					if (eStructuralFeature.getName().equals("attributeB")) {
						// Need to parse string as the de-serialization does not know what type the object is.
						// TODO: Need scripting API to manage this better
						var newValue = new java.lang.Integer(java.lang.Integer.parseInt(e.getValue().toString()));
						// Update the new value.
						v2Object.eSet(v2Object.eClass().getEStructuralFeature("attributeC"), newValue);
					}
				}
			}
		}