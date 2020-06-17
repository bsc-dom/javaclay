#!/bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then 
	 git fetch
     git checkout master
     git add pom.xml
	 git commit -m "Modified pom.xml"
	 git push origin HEAD:$TRAVIS_BRANCH

	 ## update develop branch also ##
	 git fetch	 
	 git checkout develop
     git add pom.xml
	 git commit -m "Modified pom.xml"
	 git push origin HEAD:develop
else
     echo "Skipping new version tag because current branch is not master";
fi
