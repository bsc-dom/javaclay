#!/bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then 
	 ## update develop branch also ##
	 git fetch	 
	 git checkout develop
     git add pom.xml
	 git commit -m "New release"
	 git push origin HEAD:develop
else
     echo "Skipping new version tag because current branch is not master";
fi
