#!/bin/sh

case "$1" in
    start)
        cd $2/hadoop
        ./startupdb.sh
        sleep 1m
        cd $2
        ./startSmart.sh SecureServer true 
        ;;
    stop)
        if [ -e "$2/smartkernel.pid" ]
        then
            kill $(cat $2/smartkernel.pid)
            rm $2/smartkernel.pid
        fi

        cd $2/hadoop
        ./stopdb.sh
        ;;
    restart)
        if [ -e "$2/smartkernel.pid" ]
        then
            kill $(cat $2/smartkernel.pid)
            rm $2/smartkernel.pid
        fi

        cd $2/hadoop
        ./stopdb.sh
        ./startupdb.sh
        sleep 1m
        cd $2
        ./startSmart.sh SecureServer true 
        ;;
    *)
        echo "Usage runsmart.sh start/stop/restart";
        ;;
esac

