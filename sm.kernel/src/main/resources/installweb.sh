#!/bin/bash

##################
function if_error
##################
{
    if [[ $? -ne 0 ]]; then # check return code passed to function
        print "$1 TIME:$TIME" | tee -a $LOG # if rc > 0 then print error msg and quit
        exit $?
    fi
}

mkdir smartinstall
mkdir scripts
pwd=`pwd`
javahome=$JAVA_HOME
wget https://github.com/smartplatf/a-smart/raw/master/sm.kernel/src/main/resources/setupsmart.tar -O setupsmart.tar
if_error "Error retrieving jar: https://github.com/smartplatf/a-smart/raw/master/sm.kernel/src/main/resources/setupsmart.tar"
cd scripts
tar xvf ../setupsmart.tar
#this is the default implementation. If not, let them download the install with different parameters
./setupSmart.sh $pwd/smartinstall/ $javahome/ eth0

