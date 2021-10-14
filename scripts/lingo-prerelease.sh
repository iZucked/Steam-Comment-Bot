#!/bin/bash

echo $RELEASE_VERSION
echo $DEVELOPMENT_VERSION

## Check Variables
if [ -z "$RELEASE_VERSION" ]; then
	echo "Release version is not set"
	exit 1
fi
LEN1=`expr match "$RELEASE_VERSION" '^[0-9]\+\.[0-9]\+\.[0-9]\+\.RELEASE$'`
if [ ! $LEN1 -gt 0 ]; then
	echo "Release version does not match expected pattern:  '^[0-9]\+\.[0-9]\+\.[0-9]\+\.RELEASE$'";
	exit 1
fi

if [ -z "$DEVELOPMENT_VERSION" ]; then
	echo "Development version is not set"
	exit 1
fi
LEN2=`expr match "$DEVELOPMENT_VERSION" '^[0-9]\+\.[0-9]\+\.[0-9]\+-SNAPSHOT$'`
if [ ! $LEN2 -gt 0 ]; then
	echo "Development version does not match expected pattern:  '^[0-9]\+\.[0-9]\+\.[0-9]\+-SNAPSHOT$'";
	exit 1
fi

## Check for clean repos
for repo in com*/; do
	pushd $repo

	## CleanCheck
	git status
	RC=$?
	if [ $RC != 0 ] ; then
		echo "Repository is not clean!"
		exit 1
	fi
	popd
done

## Generate settings override
python2 ${WORKSPACE}/scripts/ammend-settings.py > ${WORKSPACE}/lsettings.xml

## Get list of known projects.
java -jar ${WORKSPACE}/libs/com.inventage.tools.versiontiger-1.3.0-SNAPSHOT-cli.jar  ${WORKSPACE}/scripts/list-projects.tiger | grep "Added project" | sed -e "s/.* - Added project: \(.*\)/\1/g" > known-projects.txt


git tag TMP_RELEASE_BASE
## Header update
for repo in com*/; do
	pushd $repo
	## Update Headers
	JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 mvn -Dtycho.mode=maven license:format
	popd
done
git commit -a -m "Update headers"

## Set release version number
for repo in com*/; do
	pushd $repo
	mvn -Dtycho.mode=maven -DnewVersion=$RELEASE_VERSION org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version
	popd
done
mvn -Dtycho.mode=maven -DnewVersion=$RELEASE_VERSION org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version
git commit -a -m "Bump version to $RELEASE_VERSION in prep for release"

SHORTVERSION=`echo $RELEASE_VERSION | sed -e "s/.RELEASE//"`
echo "projectRoot ." > update-refs.tiger
echo "setting osgi.release.qualifier RELEASE" >> update-refs.tiger
for project in `cat known-projects.txt`; do
	echo "updateReferences $project $SHORTVERSION" >> update-refs.tiger
done

java -jar libs/com.inventage.tools.versiontiger-1.3.0-SNAPSHOT-cli.jar update-refs.tiger

for repo in com*/; do
	pushd $repo
	# Update references does unexpected things to some files which we do not want to persist.
	git checkout -- pom.xml
	git checkout -- */pom.xml
	git checkout -- */feature.xml
	git checkout -- */category.xml
	git checkout -- */*.product
	popd
done

## Also on master repo
git commit -a --amend --no-edit
git tag -f "$RELEASE_VERSION" -m "Create $RELEASE_VERSION tag"
