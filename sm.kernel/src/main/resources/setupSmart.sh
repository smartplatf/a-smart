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

#####################
function wget_authorizedget
####################
{
    if [ ! -f $2 ];
    then
        wget --post-data 'user=$3&password=$4' $1 -O $2
        if_error "Error retrieving jar: $2"
    fi
}

if [[ $# -lt 3 ]];
then
    echo "Usage: setupSmart <Path to setup in> <JDK home> <eth0/1>"
    exit
fi

if [ ! -f $1 ];
then
    mkdir -p $1
fi
if [ ! -f $1/lib ];
then
    mkdir -p $1/lib
fi

echo "[Copying required files.]"
cp startSmart.sh $1

echo "[Changing to directory]"
scriptsdir=`pwd`
cd $1

echo "[creating db directory]"
if [ ! -f hadoop ];
then
    mkdir -p hadoop
fi

echo "[Creating directories]"
for i in $(cat $scriptsdir/smartDirs.txt); do
    mkdir -p lib/$i;
    if_error "cannot create directory $i"
done;

echo "[Retrieving dependant jars. This may take sometime plase wait.]"
wget_url http://repo1.maven.org/maven2/log4j/log4j/1.2.16/log4j-1.2.16.jar lib/log4j/log4j/1.2.16/log4j-1.2.16.jar
wget_url http://repo1.maven.org/maven2/jcs/jcs/1.3/jcs-1.3.jar lib/jcs/jcs/1.3/jcs-1.3.jar
wget_url http://repo1.maven.org/maven2/org/yaml/snakeyaml/1.5/snakeyaml-1.5.jar lib/org/yaml/snakeyaml/1.5/snakeyaml-1.5.jar
wget_url http://repo1.maven.org/maven2/org/ow2/asm/asm/4.1/asm-4.1.jar lib/org/ow2/asm/asm/4.1/asm-4.1.jar
wget_url http://repo1.maven.org/maven2/org/ow2/asm/asm-tree/4.1/asm-tree-4.1.jar lib/org/ow2/asm/asm-tree/4.1/asm-tree-4.1.jar
wget_url http://repo1.maven.org/maven2/org/ow2/asm/asm-commons/4.1/asm-commons-4.1.jar lib/org/ow2/asm/asm-commons/4.1/asm-commons-4.1.jar
wget_url http://repo1.maven.org/maven2/org/ow2/asm/asm-util/4.1/asm-util-4.1.jar lib/org/ow2/asm/asm-util/4.1/asm-util-4.1.jar
wget_url http://repo1.maven.org/maven2/org/ow2/asm/asm-analysis/4.1/asm-analysis-4.1.jar lib/org/ow2/asm/asm-analysis/4.1/asm-analysis-4.1.jar
wget_url http://repo1.maven.org/maven2/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar lib/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar
wget_url http://repo1.maven.org/maven2/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar lib/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar
wget_url http://repo1.maven.org/maven2/commons-logging/commons-logging/1.1/commons-logging-1.1.jar lib/commons-logging/commons-logging/1.1/commons-logging-1.1.jar
wget_url http://repo1.maven.org/maven2/net/sf/json-lib/json-lib/2.4/json-lib-2.4-jdk15.jar lib/net/sf/json-lib/json-lib/2.4/json-lib-2.4.jar lib/
wget_url http://repo1.maven.org/maven2/commons-lang/commons-lang/2.5/commons-lang-2.5.jar lib/commons-lang/commons-lang/2.5/commons-lang-2.5.jar
wget_url http://repo1.maven.org/maven2/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar lib/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar
wget_url http://repo1.maven.org/maven2/commons-collections/commons-collections/3.2/commons-collections-3.2.jar lib/commons-collections/commons-collections/3.2/commons-collections-3.2.jar
wget_url http://repo1.maven.org/maven2/commons-beanutils/commons-beanutils/1.8.0/commons-beanutils-1.8.0.jar lib/commons-beanutils/commons-beanutils/1.8.0/commons-beanutils-1.8.0.jar
wget_url http://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk16/1.46/bcprov-jdk16-1.46.jar lib/org/bouncycastle/bcprov-jdk16/1.46/bcprov-jdk16-1.46.jar
wget_url http://repo1.maven.org/maven2/com/google/guava/guava/14.0.1/guava-14.0.1.jar lib/com/google/guava/guava/14.0.1/guava-14.0.1.jar
wget_url http://repo1.maven.org/maven2/concurrent/concurrent/1.0/concurrent-1.0.jar lib/concurrent/concurrent/1.0/concurrent-1.0.jar
wget_url http://repo1.maven.org/maven2/org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar lib/org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar

#wget_url http://repo1.maven.org/maven2/org/apache/commons/commons-javaflow/1.0-SNAPSHOT/commons-javaflow-1.0-SNAPSHOT.jar lib/org/apache/commons/commons-javaflow/1.0-SNAPSHOT/commons-javaflow-1.0-SNAPSHOT.jar
wget_url http://repo1.maven.org/maven2/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar lib/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar
wget_url http://repo1.maven.org/maven2/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar lib/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar

wget_url https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-0.90.6-cdh3u5.jar lib/org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-0.90.6-cdh3u5.jar
wget_url https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/hadoop/hadoop-core/0.20.2-cdh3u5/hadoop-core-0.20.2-cdh3u5.jar lib/org/apache/hadoop/hadoop-core/0.20.2-cdh3u5/hadoop-core-0.20.2-cdh3u5.jar
wget_url http://repo1.maven.org/maven2/org/apache/zookeeper/zookeeper/3.3.6/zookeeper-3.3.6.jar lib/org/apache/zookeeper/zookeeper/3.3.6/zookeeper-3.3.6.jar
wget_url https://repository.cloudera.com/content/groups/cloudera-repos/org/apache/hadoop/thirdparty/guava/guava/r09-jarjar/guava-r09-jarjar.jar lib/org/apache/hadoop/thirdparty/guava/guava/r09-jarjar/guava-r09-jarjar.jar

wget_url https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/zookeeper/zookeeper/3.3.5-cdh3u5/zookeeper-3.3.5-cdh3u5.jar lib/hadoop/hbase-0.90.6-cdh3u5/lib/zookeeper-3.3.5-cdh3u5.jar
wget_url http://repo1.maven.org/maven2/org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar lib/org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar lib/org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/4.0.0/lucene-analyzers-common-4.0.0.jar lib/org/apache/lucene/lucene-analyzers-common/4.0.0/lucene-analyzers-common-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar lib/org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-phonetic/4.0.0/lucene-analyzers-phonetic-4.0.0.jar lib/org/apache/lucene/lucene-analyzers-phonetic/4.0.0/lucene-analyzers-phonetic-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-highlighter/4.0.0/lucene-highlighter-4.0.0.jar lib/org/apache/lucene/lucene-highlighter/4.0.0/lucene-highlighter-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-memory/4.0.0/lucene-memory-4.0.0.jar lib/org/apache/lucene/lucene-memory/4.0.0/lucene-memory-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-misc/4.0.0/lucene-misc-4.0.0.jar lib/org/apache/lucene/lucene-misc/4.0.0/lucene-misc-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-queryparser/4.0.0/lucene-queryparser-4.0.0.jar lib/org/apache/lucene/lucene-queryparser/4.0.0/lucene-queryparser-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-queries/4.0.0/lucene-queries-4.0.0.jar lib/org/apache/lucene/lucene-queries/4.0.0/lucene-queries-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-spatial/4.0.0/lucene-spatial-4.0.0.jar lib/org/apache/lucene/lucene-spatial/4.0.0/lucene-spatial-4.0.0.jar
wget_url http://repo1.maven.org/maven2/com/spatial4j/spatial4j/0.3/spatial4j-0.3.jar lib/com/spatial4j/spatial4j/0.3/spatial4j-0.3.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-suggest/4.0.0/lucene-suggest-4.0.0.jar lib/org/apache/lucene/lucene-suggest/4.0.0/lucene-suggest-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/apache/lucene/lucene-grouping/4.0.0/lucene-grouping-4.0.0.jar lib/org/apache/lucene/lucene-grouping/4.0.0/lucene-grouping-4.0.0.jar
wget_url http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar lib/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar                                                  
wget_url http://repo1.maven.org/maven2/org/slf4j/slf4j-log4j12/1.6.4/slf4j-log4j12-1.6.4.jar lib/org/slf4j/slf4j-log4j12/1.6.4/slf4j-log4j12-1.6.4.jar
wget_url http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.4/slf4j-api-1.6.4.jar lib/org/slf4j/slf4j-api/1.6.4/slf4j-api-1.6.4.jar
wget_url http://repo1.maven.org/maven2/commons-io/commons-io/1.4/commons-io-1.4.jar lib/commons-io/commons-io/1.4/commons-io-1.4.jar
wget_url http://repo1.maven.org/maven2/commons-fileupload/commons-fileupload/1.2.1/commons-fileupload-1.2.1.jar lib/commons-fileupload/commons-fileupload/1.2.1/commons-fileupload-1.2.1.jar
wget_url http://repo1.maven.org/maven2/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar lib/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar
wget_url http://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.1.3/httpclient-4.1.3.jar lib/org/apache/httpcomponents/httpclient/4.1.3/httpclient-4.1.3.jar
wget_url http://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.1.4/httpcore-4.1.4.jar lib/org/apache/httpcomponents/httpcore/4.1.4/httpcore-4.1.4.jar
wget_url http://repo1.maven.org/maven2/org/apache/httpcomponents/httpmime/4.1.3/httpmime-4.1.3.jar lib/org/apache/httpcomponents/httpmime/4.1.3/httpmime-4.1.3.jar
wget_url http://repo1.maven.org/maven2/javax/servlet/servlet-api/2.3/servlet-api-2.3.jar lib/javax/servlet/servlet-api/2.3/servlet-api-2.3.jar
wget_url http://repo1.maven.org/maven2/velocity/velocity/1.5/velocity-1.5.jar lib/velocity/velocity/1.5/velocity-1.5.jar
wget_url http://repo1.maven.org/maven2/logkit/logkit/1.0.1/logkit-1.0.1.jar lib/logkit/logkit/1.0.1/logkit-1.0.1.jar




echo "[Copying smart jars]"
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/utilities/utilities/1.0-SNAPSHOT/utilities-1.0-20130403.053956-1.jar lib/org/anon/utilities/utilities/1.0-SNAPSHOT/utilities-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.atomicity/1.0-SNAPSHOT/sm.atomicity-1.0-20130403.054231-1.jar lib/org/anon/smart/sm.atomicity/1.0-SNAPSHOT/sm.atomicity-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.base/1.0-SNAPSHOT/sm.base-1.0-20130403.054230-1.jar lib/org/anon/smart/sm.base/1.0-SNAPSHOT/sm.base-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.channels/1.0-SNAPSHOT/sm.channels-1.0-20130403.054243-1.jar lib/org/anon/smart/sm.channels/1.0-SNAPSHOT/sm.channels-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.d2cache/1.0-SNAPSHOT/sm.d2cache-1.0-20130403.054214-2.jar lib/org/anon/smart/sm.d2cache/1.0-SNAPSHOT/sm.d2cache-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.deployment/1.0-SNAPSHOT/sm.deployment-1.0-20130403.054211-2.jar lib/org/anon/smart/sm.deployment/1.0-SNAPSHOT/sm.deployment-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.kernel/1.0-SNAPSHOT/sm.kernel-1.0-20130403.054330-1.jar lib/org/anon/smart/sm.kernel/1.0-SNAPSHOT/sm.kernel-1.0-SNAPSHOT.jar
#wget_url http://192.168.1.100:8888/archiva/repository/releases/org/anon/smart/sm.smcore/1.0-SNAPSHOT/sm.smcore-1.0-20130403.054316-1.jar lib/org/anon/smart/sm.smcore/1.0-SNAPSHOT/sm.smcore-1.0-SNAPSHOT.jar

echo "Setting up db.."
cd -
cd dbscripts
./setupdb.sh $1/hadoop $2 $3

echo "[Starting smart]"
cd $1
echo "export SMART_PATH=$1" > setupEnv.sh
echo "export SMART_VERSION=1.0-SNAPSHOT" >> setupEnv.sh
echo "export JAVA_HOME=$3" >> setupEnv.sh
chmod +x setupEnv.sh

./startSmart.sh Server true &

