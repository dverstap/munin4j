#/usr/bin/env bash

set -e

# See this to understand how this is supposed to work:
# http://cemerick.com/2010/08/24/hosting-maven-repos-on-github/

mvn --batch-mode release:prepare

GIT_TAG=`grep scm.tag release.properties | cut -d'=' -f2`

# the release build will actually run in target/checkout,
# so let's clone the maven repo at target/checkout/../maven-repo :
cd target
git clone git@github.com:dverstap/maven-repo.git
cd ..

# this only works when the maven-release-plugin is configured with:
# <arguments>-DaltDeploymentRepository="${munin4jAltDeploymentRepository}"</arguments>
# because it this command actually runs maven again in the target/checkout directory:
mvn -Dmunin4jAltDeploymentRepository=dverstap-github-releases::default::file:../maven-repo/maven2/releases release:perform

cd target/maven-repo
git add .
git commit -a -m "New release: $GIT_TAG"
git push origin master
cd ../..

git fetch
