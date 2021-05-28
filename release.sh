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
  exit 0
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

POM_VERSION=$(grep version pom.xml | grep -v -e '<?xml|~'| head -n 1 | sed 's/[[:space:]]//g' | sed -E 's/<.{0,1}version>//g' | awk '{print $1}')
printMsg "Welcome to javaClay release script"
GIT_BRANCH=$(git for-each-ref --format='%(objectname) %(refname:short)' refs/heads | awk "/^$(git rev-parse HEAD)/ {print \$2}")
if [[ "$GIT_BRANCH" != "$BRANCH_TO_CHECK" ]]; then
  printError "Branch is not $BRANCH_TO_CHECK. Found $GIT_BRANCH. Aborting script"
fi

if [ "$PROMPT" = true ]; then
  read -p "Version defined is $POM_VERSION. Is this ok? (y/n) " -n 1 -r
  echo    # (optional) move to a new line
  if [[ ! $REPLY =~ ^[Yy]$ ]]
  then
      printError "Please modify pom.xml version"
  fi

  printf "${CONSOLE_RED} IMPORTANT: you're about to build and officially release javaClay $VERSION ${CONSOLE_NORMAL}\n"
  read -rsn1 -p" Press any key to continue (CTRL-C for quitting this script)";echo
fi

if [ "$DEV" = true ] ; then

  mvn -DskipTests=true -P publish deploy -s settings.xml

else

  printMsg "Pre-processing files in master"
  VERSION="${POM_VERSION//-SNAPSHOT/}"
  VERSION=$(echo "$VERSION" | cut -d '.' -f1,2)
  PREV_VERSION=$(echo "$VERSION - 0.1" | bc)
  NEW_VERSION=$(echo "$VERSION + 0.1" | bc)
  GIT_TAG=$VERSION

  # Modify README.md
  sed -i "s/$VERSION/$NEW_VERSION/g" README.md
  sed -i "s/$PREV_VERSION/$VERSION/g" README.md
  git add README.md
  git commit -m "Modified README.md"
  git push

  # NOTE: maven will tag Git repository
  mvn -P publish release:clean release:prepare release:perform -s settings.xml

  # once released maven push pom.xml without SNAPSHOT tag in master
  cp pom.xml develop.pom.xml
  sed -i "s/$VERSION-SNAPSHOT/$VERSION/g" pom.xml
  git add pom.xml
  git commit -m "Released $VERSION"
  git push

  printMsg "Preparing develop branch"
  ## update develop branch also ##
  git checkout develop
  git merge master
  mv develop.pom.xml pom.xml
  git add pom.xml
  git commit -m "New development version"
  git push

  # back to master
  git checkout master
fi

printMsg "  ==  Everything seems to be ok! Bye"