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

#####################
function wget_url
####################
{
    if [ ! -f $2 ];
    then
        wget $1 -O $2
        if_error "Error retrieving jar: $2"
    fi
}

if [[ $# -lt 3 ]];
then
    echo "Usage: setupdb.sh <Directory to setup in> <JAVA HOME> <network to use>"
    exit 1
fi

if [ ! -f $1 ];
then
    mkdir -p $1
fi

export JAVA_HOME=$2
export CLASSPATH=$CLASSPATH:$1
export IPADDR=`ip addr show $3 | grep inet | grep $3 | cut -d\/ -f1 | sed 's/^ *//g' | cut -d' ' -f2`

echo "[Copying required files]"
cp hconfig.tar $1
cp startupdb.sh $1
cp stopdb.sh $1

echo "[Changing directory $1]"
cd $1

echo "[Downloading hadoop jar]"
wget_url http://archive.cloudera.com/cdh/3/hadoop-0.20.2-cdh3u5.tar.gz hadoop-0.20.2-cdh3u5.tar.gz
echo "[Downloading hbase jar]"
wget_url http://archive.cloudera.com/cdh/3/hbase-0.90.6-cdh3u5.tar.gz hbase-0.90.6-cdh3u5.tar.gz

echo "[Extracting... hadoop]"
tar xzf hadoop-0.20.2-cdh3u5.tar.gz
if_error "Cannot extract: hadoop-0.20.2-cdh3u5.tar.gz"
echo "[Extracting... hbase]"
tar xzf hbase-0.90.6-cdh3u5.tar.gz
if_error "Cannot extract: hbase-0.90.6-cdh3u5.tar.gz"
echo "[Extracting... config]"
tar xf hconfig.tar
if_error "Cannot extract: hconfig.tar"

echo "[Setting up hadoop]"
sed '/#hadoophome#/c\
 '"$PWD"'/hadoop-0.20.2-cdh3u5/hadoop/datastore' hadoop-0.20.2-cdh3u5/conf/core-site.xml > hadoop-0.20.2-cdh3u5/conf/core-site.xml.1
if_error "Cannot create: core-site.xml"
sed '/#javahome#/c\
 export JAVA_HOME='"$2"'' hadoop-0.20.2-cdh3u5/conf/hadoop-env.sh > hadoop-0.20.2-cdh3u5/conf/hadoop-env.sh.1
if_error "Cannot create: hadoop-env.sh"
sed '/#javahome#/c\
 export JAVA_HOME='"$2"'' hbase-0.90.6-cdh3u5/conf/hbase-env.sh > hbase-0.90.6-cdh3u5/conf/hbase-env.sh.1
if_error "Cannot create: hbase-env.sh"

mv hadoop-0.20.2-cdh3u5/conf/core-site.xml.1 hadoop-0.20.2-cdh3u5/conf/core-site.xml
if_error "Cannot create: core-site.xml"
mv hadoop-0.20.2-cdh3u5/conf/hadoop-env.sh.1 hadoop-0.20.2-cdh3u5/conf/hadoop-env.sh
if_error "Cannot create: hadoop-env.sh"
mv  hbase-0.90.6-cdh3u5/conf/hbase-env.sh.1  hbase-0.90.6-cdh3u5/conf/hbase-env.sh
if_error "Cannot create: hbase-env.sh"

echo "[setting up namenode...]"
hadoop-0.20.2-cdh3u5/bin/hadoop namenode -format

echo "[setting up solr...]"
cd ../
ln -s  hadoop/solr-datastore solr-datastore
cd hadoop
./startupdb.sh

