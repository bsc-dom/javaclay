#!/bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then 
	 ## update develop branch also ##
	 git checkout develop
     git add pom.xml
	 git commit -m "Updating pom.xml"
	 git push origin HEAD:develop
else
     echo "Skipping new version tag because current branch is not master";
fi
