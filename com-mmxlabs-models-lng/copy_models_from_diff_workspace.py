#
# Copyright (C) Minimax Labs Ltd., 2010 - 2023
# All rights reserved.
#

import shutil, os
#####################################
path_to_dest_ws = os.getcwd()
path_to_source_ws = path_to_dest_ws + '/../../../../lingo-master3/ws/lingo.git/com-mmxlabs-models-lng'
version = 112
#####################################

root_src = "{0}/com.mmxlabs.models.lng.".format(path_to_source_ws)
root_dest = "{0}/com.mmxlabs.models.lng.".format(path_to_dest_ws)

files = ["port",
		 "actuals",
		 "pricing",
		 "fleet",
		 "commercial",
		 "spotmarkets",
		 "cargo",
		 "schedule",
		 "analytics",
		 "parameters",
		 "adp",
		 "nominations",
		 "transfers",
		 ]
for f in files:
	source = "{0}{1}/model/{1}-v{2}.ecore".format(root_src,f,version)
	dest = "{0}{1}/model/{1}-v{2}.ecore".format(root_dest,f,version)
	print('Copying ' + source + ' to ' + dest)
	shutil.copyfile(source,dest)

# special cases
f = "lngtypes"
source = "{0}types/model/{1}-v{2}.ecore".format(root_src,f,version)
dest = "{0}types/model/{1}-v{2}.ecore".format(root_dest,f,version)
print('Copying ' + source + ' to ' + dest)
shutil.copyfile(source,dest)

f = "scenario"
source = "{0}{1}.model/model/{1}-v{2}.ecore".format(root_src,f,version)
dest = "{0}{1}.model/model/{1}-v{2}.ecore".format(root_dest,f,version)
print('Copying ' + source + ' to ' + dest)
shutil.copyfile(source,dest)
