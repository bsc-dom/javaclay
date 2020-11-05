#!/bin/bash
set -e
#-----------------------------------------------------------------------
# Helper functions (miscellaneous)
#-----------------------------------------------------------------------
CONSOLE_CYAN="\033[1m\033[36m"; CONSOLE_NORMAL="\033[0m"; CONSOLE_RED="\033[1m\033[31m"
printMsg() {
  printf "${CONSOLE_CYAN}${1}${CONSOLE_NORMAL}\n"
}
printError() {
  printf "${CONSOLE_RED}${1}${CONSOLE_NORMAL}\n"
  exit 1
}
#-----------------------------------------------------------------------
# MAIN
#-----------------------------------------------------------------------
DEV=false
PROMPT=true
BRANCH_TO_CHECK="master"
while test $# -gt 0
do
    case "$1" in
        --dev)
          DEV=true
          BRANCH_TO_CHECK="develop"
            ;;
        -y)
        	PROMPT=false
        	;;
        *) echo "Bad option $1"
        	exit 1
            ;;
    esac
    shift
done

VERSION=$(grep version pom.xml | grep -v -e '<?xml|~'| head -n 1 | sed 's/[[:space:]]//g' | sed -E 's/<.{0,1}version>//g' | awk '{print $1}')
printMsg "Welcome to javaClay release script"
GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$GIT_BRANCH" != "$BRANCH_TO_CHECK" ]]; then
  printError "Branch is not $BRANCH_TO_CHECK. Aborting script"
fi

if [ "$PROMPT" = true ]; then
  read -p "Version defined is $VERSION. Is this ok? (y/n) " -n 1 -r
  echo    # (optional) move to a new line
  if [[ ! $REPLY =~ ^[Yy]$ ]]
  then
      printError "Please modify pom.xml version"
  fi

  printf "${CONSOLE_RED} IMPORTANT: you're about to build and officially release javaClay $VERSION ${CONSOLE_NORMAL}\n"
  read -rsn1 -p" Press any key to continue (CTRL-C for quitting this script)";echo
fi

if [ "$DEV" = true ] ; then

  mvn -P publish deploy -s settings.xml

else

  printMsg "Pre-processing files in master"
  VERSION="${VERSION//-SNAPSHOT/}"
  VERSION=$(echo "$VERSION" | cut -d '.' -f1,2)
  PREV_VERSION=$(echo "$VERSION - 0.1" | bc)
  NEW_VERSION=$(echo "$VERSION + 0.1" | bc)
  GIT_TAG=$VERSION

  # Modify README.md
  sed -i "s/$VERSION/$NEW_VERSION/g" README.md
  sed -i "s/$PREV_VERSION/$VERSION/g" README.md
  git add README.md

  # NOTE: maven will tag Git repository
  mvn -P publish release:clean release:prepare release:perform -s settings.xml

  exit 0

  #git add README.md
  #git commit -m "Release ${GIT_TAG}"
  #git push origin master

  #printMsg "Tagging new release in Git"
  #git tag -a ${GIT_TAG} -m "Release ${GIT_TAG}"
  #git push origin ${GIT_TAG}

  printMsg "Preparing develop branch"
  #git fetch
  #git checkout master
  #git add pom.xml
  #git commit -m "Modified pom.xml"
  #git push origin HEAD:$TRAVIS_BRANCH

  ## update develop branch also ##
  #git fetch --all
  git checkout develop
  git merge master
  git add pom.xml
  git commit -m "Modified pom.xml"
  git push origin develop

  # back to master
  git checkout master
fi

printMsg "  ==  Everything seems to be ok! Bye"