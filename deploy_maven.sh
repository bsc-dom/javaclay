#!/bin/bash
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
grn=$'\e[1;32m'
blu=$'\e[1;34m'
red=$'\e[1;91m'
end=$'\e[0m'
function printMsg { 
  echo "${blu}[dataClay release] $1 ${end}"
}
function printError { 
  echo "${red}======== $1 ========${end}"
}

################################## OPTIONS #############################################

DEV=false

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
        --dev) DEV=true
            ;;
        --*) echo "bad option $1"
        	exit -1
            ;;
        *) echo "bad option $1"
        	exit -1
            ;;
    esac
    shift
done

################################## VERSIONING #############################################


################################## FUNCTIONS #############################################

pushd () {
    command pushd "$@" > /dev/null
}

popd () {
    command popd "$@" > /dev/null
}


################################## MAIN #############################################


printMsg "'"'
      _       _         _____ _             
     | |     | |       / ____| |            
   __| | __ _| |_ __ _| |    | | __ _ _   _ 
  / _` |/ _` | __/ _` | |    | |/ _` | | | |
 | (_| | (_| | || (_| | |____| | (_| | |_| |  maven release script
  \__,_|\__,_|\__\__,_|\_____|_|\__,_|\__, |
                                       __/ |
                                      |___/ 
'"'"
printMsg " Welcome to dataClay Maven release script!"

################################## VERSIONS #############################################

if [ "$DEV" = false ] ; then
	while true; do
		version=`grep -m 1 "version" $SCRIPTDIR/logicmodule/javaclay/pom.xml`
		echo "Current defined version in pom.xml: $grn $version $end" 
		read -p "Are you sure pom.xml version is correct (yes/no)? " yn
		case $yn in
			[Yy]* ) break;;
			[Nn]* ) echo "Modify it and try again."; exit;;
			* ) echo "$red Please answer yes or no. $end";;
		esac
	done
fi
################################## PUSH #############################################

printMsg " ==== Pushing dataclay to maven ===== "

pushd $SCRIPTDIR/logicmodule/javaclay

if [ ! -f "settings.xml" ]; then
	echo "ERROR: settings.xml file does not exist. Please create settings.xml file:
	More information at https://github.com/bsc-dom/javaclay/blob/master/PUBLISH.md 
	" 
	exit -1
fi
if [ "$DEV" = true ] ; then
	mvn -P publish deploy -s settings.xml
else 
	mvn -P publish release:clean release:prepare release:perform -s settings.xml
fi
if [ $? -ne 0 ]; then
	echo "ERROR: error pushing dataclay to maven "
	exit -1
fi 	
popd

printMsg " ===== Done! ====="
