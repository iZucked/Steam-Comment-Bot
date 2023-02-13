#
# Copyright (C) Minimax Labs Ltd., 2010 - 2023
# All rights reserved.
#

import requests
import csv
import sys
import os
import shutil
from collections import defaultdict 

if len(sys.argv) < 2:
	print "please provide a csv folder"
	sys.exit(1)

# # Read in initial data
vessels_map = {}
vessels_ordered = []
with open(sys.argv[1] + "/Vessels.csv") as csv_file:
	reader = csv.DictReader(csv_file)
	# fieldnames_vessels = reader_vessels.fieldnames
	for row in reader:
		vessels_map[row['name'].strip()] = row
		vessels_ordered.append(row)

vessel_classes_map = {}
fieldnames_vessels = []
with open(sys.argv[1] + "/Vessel Classes.csv") as csv_file:
	reader = csv.DictReader(csv_file)
	fieldnames_vessels = reader.fieldnames
	for row in reader:
		vessel_classes_map[row['name'].strip()] = row
		
vessel_groups_map = {}
if os.path.exists(sys.argv[1] + "/Vessel Groups.csv"):
	with open(sys.argv[1] + "/Vessel Groups.csv") as csv_file:
		reader = csv.DictReader(csv_file)
		# fieldnames_vessel_groups = reader.fieldnames
		for row in reader:
			vessel_groups_map[row['name'].strip()] = row
 
# # Register vessel class as a group.
vessel_class_to_vessels = defaultdict(list)
new_cols = set()
for name, row in vessels_map.items():
		vessel_class_name = row['vesselClass'].strip()
		vessel_class_to_vessels[vessel_class_name].append(name)

		vessel_class = vessel_classes_map[vessel_class_name]
		del row['vesselClass']
		row['reference'] = vessel_class_name

		if not row['inaccessiblePorts']:
			row['inaccessiblePorts'] = vessel_class['inaccessiblePorts']

		if 'inaccessibleRoutes' in vessel_class and (not 'overrideInaccessibleRoutes' in row or row['overrideInaccessibleRoutes'].lower() != 'true'):
			row['inaccessibleRoutes'] = vessel_class['inaccessibleRoutes']
		if 'overrideInaccessibleRoutes' in row:
			del row['overrideInaccessibleRoutes']
 
		if not row['capacity']:
			row['capacity'] = vessel_class['capacity']

		if not row['fillCapacity']:
			row['fillCapacity'] = vessel_class['fillCapacity']

		if 'scnt' in vessel_class and not row['scnt']:
			row['scnt'] = vessel_class['scnt']


for (vessel_class, row) in vessel_classes_map.items():
		vessel_class_row = row
		buildPrototype = True
		simpleClassName = vessel_class.lower().replace(" ", "").replace("-", "")
		for (vessel, row2) in vessels_map.items():
			simpleVesselName = vessel.lower().replace(" ", "").replace("-", "")
			if simpleClassName == simpleVesselName:
				del row2['reference']
				
				row2['baseFuel'] = vessel_class_row['baseFuel']
				row2['minSpeed'] = vessel_class_row['minSpeed']
				row2['maxSpeed'] = vessel_class_row['maxSpeed']
				
				if 'minHeel' in vessel_class_row:
					row2['safetyHeel'] = vessel_class_row['minHeel']
		
				if 'warmingTime' in vessel_class_row:
					row2['warmingTime'] = vessel_class_row['warmingTime']
		
				if 'coolingVolume' in vessel_class_row:
					row2['coolingVolume'] = vessel_class_row['coolingVolume']
				
				if 'pilotLightRate' in vessel_class_row:
					row2['pilotLightRate'] = vessel_class_row['pilotLightRate']
				
				if 'minBaseFuelConsumption' in vessel_class_row:
					row2['minBaseFuelConsumption'] = vessel_class_row['minBaseFuelConsumption']
					
				if 'hasReliqCapability' in vessel_class_row:
					row2['hasReliqCapability'] = vessel_class_row['hasReliqCapability']
		
				for vessel_class_row2 in vessel_class_row:
						if vessel_class_row2.startswith('ladenAttributes'):
							row2[vessel_class_row2] = vessel_class_row[vessel_class_row2]
							new_cols.add(vessel_class_row2)
						if vessel_class_row2.startswith('ballastAttributes'):
							row2[vessel_class_row2] = vessel_class_row[vessel_class_row2]
							new_cols.add(vessel_class_row2)
						if 'canal.parameters' in vessel_class_row2:
							row2[vessel_class_row2] = vessel_class_row[vessel_class_row2]					
							new_cols.add(vessel_class_row2)
						# if 'canal.pricing' in vessel_class_row2:
							# row[vessel_class_row2] = vessel_class[vessel_class_row2]					

				buildPrototype = False
				
		if not buildPrototype:
				continue

		if 'minHeel' in row:
			row['safetyHeel'] = row['minHeel']
			del row['minHeel']

		if vessel_class in vessels_map.keys():
			row['name'] = 'CLS_' + vessel_class
			vessels_map['CLS_' + vessel_class] = row
			raise BaseException("Name clash" + vessel_class)
		else:
			vessels_map[vessel_class] = row

		vessels_ordered.append(row)

		vessel_class_to_vessels[vessel_class].append(row['name'].strip())




for (vessel_class, vessels) in vessel_class_to_vessels.items():
	row = {}
	row['name'] = vessel_class
	row['vessels'] = ",".join(vessels)
	vessel_groups_map[vessel_class] = row


# # Update dependent data
if os.path.exists(sys.argv[1] + "/Cargoes.csv"):
	with open(sys.argv[1] + "/Cargoes.csv") as csv_file:
		reader = csv.DictReader(csv_file)
		with open(sys.argv[1] + "/New Cargoes.csv", 'wb') as f:
			writer = csv.DictWriter(f, reader.fieldnames)
			writer.writeheader()		
		
			for row in reader:
				if 'buy.allowedVessels' in row:
					value = row['buy.allowedVessels']
					for (vessel_class, vessels) in vessel_class_to_vessels.items():
						if vessel_class in value:
							value = value.replace(vessel_class, 'VesselGroup:' + vessel_class)
							
					row['buy.allowedVessels'] = value
				if 'sell.allowedVessels' in row:
					value = row['sell.allowedVessels']
					for (vessel_class, vessels) in vessel_class_to_vessels.items():
						if vessel_class in value:
							value = value.replace(vessel_class, 'VesselGroup:' + vessel_class)
							
					row['sell.allowedVessels'] = value
				writer.writerow(row)

	# move New cargoes to cargoes
	os.remove(sys.argv[1] + "/Cargoes.csv")
	shutil.move(sys.argv[1] + "/New Cargoes.csv", sys.argv[1] + "/Cargoes.csv")				

if os.path.exists(sys.argv[1] + "/Events.csv"):
	with open(sys.argv[1] + "/Events.csv") as csv_file:
		reader = csv.DictReader(csv_file)
		with open(sys.argv[1] + "/New Events.csv", 'wb') as f:
			writer = csv.DictWriter(f, reader.fieldnames)
			writer.writeheader()		
		
			for row in reader:
				if 'allowedVessels' in row:
					value = row['allowedVessels']
					for (vessel_class, vessels) in vessel_class_to_vessels.items():
						if vessel_class in value:
							value = value.replace(vessel_class, 'VesselGroup:' + vessel_class)
							
					row['allowedVessels'] = value
				writer.writerow(row)
	# move New Events to Events
	os.remove(sys.argv[1] + "/Events.csv")
	shutil.move(sys.argv[1] + "/New Events.csv", sys.argv[1] + "/Events.csv")

if False and os.path.exists(sys.argv[1] + "/Charter Markets.csv"):
	with open(sys.argv[1] + "/Charter Markets.csv") as csv_file:
		reader = csv.DictReader(csv_file)
		with open(sys.argv[1] + "/New Charter Markets.csv", 'wb') as f:
			fieldnames = list(reader.fieldnames)
			if not 'vessel' in fieldnames:
				fieldnames.append('vessel')
			if not 'vessels' in fieldnames:			
				fieldnames.append('vessels')
			if 'vesselClasses' in fieldnames:
				fieldnames.remove('vesselClasses')
			if 'enabled' not in fieldnames:
				fieldnames.append('enabled')	 
			if 'charterInRate' not in fieldnames:
				fieldnames.append('charterInRate')	 
			if 'charterOutRate' not in fieldnames:
				fieldnames.append('charterOutRate')												
			
			writer = csv.DictWriter(f, fieldnames)
			writer.writeheader()		
		
			for row in reader:
				if 'vesselClasses' in row:
					if 'CharterCostModel' in row['kind']:
						charterInPrice = False
						charterOutPrice = False
						
						if 'charterInPrice' in row:
							charterInPrice = row['charterInPrice']
							del row['charterInPrice']
							
						if 'charterOutPrice' in row:
							charterOutPrice = row['charterOutPrice']
							del row['charterOutPrice']
							
						vessel_classes_str = row['vesselClasses']                            
						vessel_classes = row['vesselClasses'].split(",")
						del row['vesselClasses']
						
						row['enabled'] = 'true';
						
						if not False == charterOutPrice:
							row['vessels'] = vessel_classes_str
							row['kind'] = 'CharterOutMarket'
						 	row['charterOutRate'] = charterOutPrice
							writer.writerow(row)
							del row['vessels']
							del row['charterOutRate']
						
						if not False == charterOutPrice:
							row['charterInRate'] = charterInPrice
							row['kind'] = 'CharterInMarket'
							for vc in vessel_classes:
								row['vessel'] = vc.strip()
								writer.writerow(row)
					elif 'CharterInMarket' in row['kind']:
						 vessel_classes = row['vesselClasses'].split(",")
						 del row['vesselClasses']
						 for vc in vessel_classes:
							 row['vessel'] = vc.strip()
							 writer.writerow(row)
					elif 'CharterOutMarket' in row['kind']:
						row['vessels'] = row['vesselClasses']
						writer.writerow(row)
				elif 'vesselClass' in row:
					if 'kind' not in row:
						row['vessel'] = row['vesselClass']
						row['vessels'] = row['vesselClass']				
					elif 'CharterInMarket' in row['kind']:
						row['vessel'] = row['vesselClass']
					elif 'CharterOutMarket' in row['kind']:
						row['vessels'] = row['vesselClass']
					else:
						row['vessel'] = row['vesselClass']
						row['vessels'] = row['vesselClass']
					writer.writerow(row)
				else:
					writer.writerow(row)
	# move New Events to Events
	os.remove(sys.argv[1] + "/Charter Markets.csv")
	shutil.move(sys.argv[1] + "/New Charter Markets.csv", sys.argv[1] + "/Charter Markets.csv")	

with open(sys.argv[1] + "/Consumption Curves.csv") as csv_file:
	reader = csv.DictReader(csv_file)
	with open(sys.argv[1] + "/New Consumption Curves.csv", 'wb') as f:
		fieldnames = list(reader.fieldnames)		  
		writer = csv.DictWriter(f, fieldnames)
		writer.writeheader()		
	
		for row in reader:
			writer.writerow(row)
			vessel_class = row['class']
			for vessel in vessel_class_to_vessels[vessel_class]:
				row['class'] = vessel				
				writer.writerow(row)
# move New Events to Events
os.remove(sys.argv[1] + "/Consumption Curves.csv")
shutil.move(sys.argv[1] + "/New Consumption Curves.csv", sys.argv[1] + "/Consumption Curves.csv")								


# # Save prep
fieldnames_vessels.append('reference')
fieldnames_vessels.append('baseFuel')
fieldnames_vessels.append('minSpeed')
fieldnames_vessels.append('maxSpeed')
fieldnames_vessels.append('safetyHeel')
fieldnames_vessels.append('warmingTime')
fieldnames_vessels.append('pilotLightRate')
fieldnames_vessels.append('minBaseFuelConsumption')
fieldnames_vessels.append('coolingVolume')
fieldnames_vessels.append('hasReliqCapability')
fieldnames_vessels.extend(list(new_cols))
if 'shortName' not in fieldnames_vessels:
	fieldnames_vessels.append('shortName')


if 'vesselClass' in fieldnames_vessels:		 
	fieldnames_vessels.remove('vesselClass')
if 'minHeel' in fieldnames_vessels:
	fieldnames_vessels.remove('minHeel')


with open(sys.argv[1] + "/Vessel Groups.csv", 'wb') as f:
	writer = csv.DictWriter(f, ['name', 'vessels', 'kind'])
	writer.writeheader()
	for name, row in vessel_groups_map.items():
		writer.writerow(row)

with open(sys.argv[1] + "/Route Costs.csv", 'wb') as f:
	writer = csv.DictWriter(f, ['vessels', 'routeOption', 'ladenCost', 'ballastCost', 'ballastRoundTripCost'])
	writer.writeheader()		
	
	for name, vessel in vessel_classes_map.items():
		row = {}
		row['vessels'] = "VesselGroup:" + name
		row['routeOption'] = 'SUEZ'
		if 'Suez canal.pricing.ladenCost' in vessel:
			row['ladenCost'] = vessel['Suez canal.pricing.ladenCost']
			del vessel['Suez canal.pricing.ladenCost']
		if 'Suez canal.pricing.ballastCost' in vessel:
			row['ballastCost'] = vessel['Suez canal.pricing.ballastCost']
			del vessel['Suez canal.pricing.ballastCost']
		writer.writerow(row)
			

if 'Suez canal.pricing.ladenCost' in fieldnames_vessels:
	fieldnames_vessels.remove('Suez canal.pricing.ladenCost')
if 'Suez canal.pricing.ballastCost' in fieldnames_vessels:
	fieldnames_vessels.remove('Suez canal.pricing.ballastCost')	


with open(sys.argv[1] + "/Vessels.csv", 'wb') as f:
	writer = csv.DictWriter(f, fieldnames_vessels)
	writer.writeheader()
	for row in vessels_ordered:
		writer.writerow(row)



# # TODO: Delete Vessel Classes.csv
os.remove(sys.argv[1] + "/Vessel Classes.csv")
