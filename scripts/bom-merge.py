import json
import sys

 

infile1 = sys.argv[2]
infile2 = sys.argv[3]
outfile = sys.argv[4]

bom = None
with open(infile1,  encoding='utf-8') as json_data:
    bom = json.load(json_data)


bom2 = None
with open(infile2,  encoding='utf-8') as json_data:
    bom2 = json.load(json_data)


if 'metadata' in bom2:
	if 'tools' in bom2['metadata']:
		for tool in bom2['metadata']['tools']:
		    bom['metadata']['tools'].append(tool)


if 'dependencies' in bom2:
	for dependency in bom2['dependencies']:
	    bom['dependencies'].append(dependency)

if 'component' in bom2:
	for component in bom2['components']:
	    bom['components'].append(component)


    
with open(outfile, 'w') as f:
    json.dump(bom, f, indent=4)
