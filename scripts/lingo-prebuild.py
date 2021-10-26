#!/bin/python

from contextlib import contextmanager
import os, sys
import subprocess
import re

@contextmanager
def cd(newdir):
    prevdir = os.getcwd()
    os.chdir(os.path.expanduser(newdir))
    try:
        yield
    finally:
        os.chdir(prevdir)

lingoVersion = sys.argv[1]
print(lingoVersion)

## Find the sub projects
thedir = "."
subdirs =  [ name for name in os.listdir(thedir) if os.path.isdir(os.path.join(thedir, name)) and name.startswith("com-") and len(name) > 4 ]

## Update license headers and version number
subprocess.call("git tag TEMP_TAG", shell=True)
for subdir in subdirs:
	print (subdir)
	with cd(subdir):
		subprocess.call("mvn -Dtycho.mode=maven -DnewVersion={} org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version".format(lingoVersion), shell=True)

			
#with open('known-projects.txt') as fin:
#	with open('update-refs.tiger', 'w') as fout:
#		fout.write("projectRoot .\n")
#		fout.write("setting osgi.release.qualifier RELEASE\n")
##		for line in fin:
#			fout.write("updateReferences $project $SHORTVERSION\n")
#
#subprocess.call("java -jar libs/com.inventage.tools.versiontiger-1.3.0-SNAPSHOT-cli.jar update-refs.tiger")

## Clean up changes, commit and tag
#for subdir in subdirs:
#	print (subdir)
#	with cd(subdir):
#		subprocess.call(['hg', 'revert', 'pom.xml', '*/pom.xml', '*/feature.xml', '*/category.xml', '*/*.product'])
#		subprocess.call(['hg', 'commit', '-m', "Update version references to $RELEASE_VERSION in prep for release"])
#		subprocess.call(['hg', 'tag', '-f', "$RELEASE_VERSION"])

## Also on master repo
subprocess.call("mvn -Dtycho.mode=maven -DnewVersion={} org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version".format(lingoVersion), shell=True)
subprocess.call("git commit -a -m 'Update version references to {}'".format(lingoVersion), shell=True)
subprocess.call("git tag -f {} -m 'Tagging {}'".format(lingoVersion, lingoVersion), shell=True)
subprocess.call("git bundle create changes.git --tags={} TEMP_TAG..{}".format(lingoVersion, lingoVersion), shell=True)
