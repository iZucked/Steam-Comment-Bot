import json
import sys

mappings = {
  'com.fasterxml.jackson.core.jackson-databind' : { 'name':'jackson-databind','group':'com.fasterxml.jackson.core' },
  'com.fasterxml.jackson.datatype.jackson-datatype-jdk8' : { 'name':'jackson-datatype-jdk8','group':'com.fasterxml.jackson.datatype' },
  'com.fasterxml.jackson.datatype.jackson-datatype-jsr310' : { 'name':'jackson-datatype-jsr310','group':'com.fasterxml.jackson.datatype' },
  'com.fasterxml.jackson.core.jackson-core' : { 'name':'jackson-core','group':'com.fasterxml.jackson.core' },
  'com.fasterxml.jackson.core.jackson-annotations' : { 'name':'jackson-annotations','group':'com.fasterxml.jackson.core' },
  'org.apache.log4j' : { 'name':'log4j','group':'log4j', 'stripQualifier': true },
  'wrapped.com.squareup.okhttp3.okhttp' : { 'name':'okhttp','group':'com.squareup.okhttp3' }
}

infile = sys.argv[1]
outfile = sys.argv[2]

print("Converting", infile, "to", outfile")

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
           print (v['version'] )
        u = component.copy()
        u.update(v)
        component['bom-ref'] = 'pkg:maven/{group}/{name}@{version}?type=jar'.format(**u)
        component['purl'] = 'pkg:maven/{group}/{name}@{version}?type=jar'.format(**u)

with open(outfile, 'w') as f:
    json.dump(bom, f, indent=4)
