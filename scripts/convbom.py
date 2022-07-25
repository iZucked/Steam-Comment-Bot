import json
import sys



## This script is used to map the p2 generated URL to something DepTrack understands. Most dependencies have a maven equivalent or have been imported directly from maven.

## Mapping the eclipse bundle name to the maven artifactid and groupid.
## Some bundles will have an osgi version and qualifier that does not map directly to the maven version.
## These need special treatment. For now stripQualifier from the qualifier component from the osgi version string.
mappings = {}

with open('mappings.json') as json_data:
	mappings = json.load(json_data)

infile = sys.argv[1]
outfile = sys.argv[2]

print("Converting", infile, "to", outfile)

bom = None
with open(infile) as json_data:
    bom = json.load(json_data)


for component in bom['components']:
    if component['name'] in mappings:
        v = mappings[component['name']]
        #component['name'] = v['name']
        #component['group'] = v['group']
        #if 'version' in v:
        #    component['version'] = v['version']
        #    
        #u = { **component , **v}
        
        if 'stripQualifier' in v:
           v['version'] =    component['version'][:component['version'].rindex('.')]
           #print (v['version'] )
        u = component.copy()
        u.update(v)
        
        ## PURL is the main piece used by deptrack to link back to the original maven artifact and any issues linked to it.
        
        #component['bom-ref'] = 'pkg:maven/{group}/{name}@{version}?type=jar'.format(**u)
        component['purl'] = 'pkg:maven/{group}/{name}@{version}?type=jar'.format(**u)

with open(outfile, 'w') as f:
    json.dump(bom, f, indent=4)
