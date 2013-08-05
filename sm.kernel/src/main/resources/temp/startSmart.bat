@echo off
set SMART_PATH=%USERPROFILE%\.m2\repository
set SMART_VERSION=1.0-SNAPSHOT

echo jarfiles location:  %SMART_PATH%
echo smart version :  %SMART_VERSION%

::
::add classpaths to this
::
set CLASSPATH=%SMART_PATH%\org\anon\utilities\utilities\%SMART_VERSION%\utilities-%SMART_VERSION%.jar;
set CLASSPATH=%SMART_PATH%\org\anon\utilities\utilities\%SMART_VERSION%\utilities-%SMART_VERSION%.jar;

set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.kernel\%SMART_VERSION%\sm.kernel-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.smcore\%SMART_VERSION%\sm.smcore-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.base\%SMART_VERSION%\sm.base-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.channels\%SMART_VERSION%\sm.channels-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.deployment\%SMART_VERSION%\sm.deployment-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.d2cache\%SMART_VERSION%\sm.d2cache-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.atomicity\%SMART_VERSION%\sm.atomicity-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.generator\%SMART_VERSION%\sm.generator-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.template\%SMART_VERSION%\sm.template-%SMART_VERSION%.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\anon\smart\sm.codegen\%SMART_VERSION%\sm.codegen-%SMART_VERSION%.jar;

set CLASSPATH=%CLASSPATH%%SMART_PATH%\io\netty\netty\3.6.5.Final\netty-3.6.5.Final.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\concurrent\concurrent\1.0\concurrent-1.0.jar;


set CLASSPATH=%CLASSPATH%%SMART_PATH%\log4j\log4j\1.2.16\log4j-1.2.16.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\jcs\jcs\1.3\jcs-1.3.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\yaml\snakeyaml\1.5\snakeyaml-1.5.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\ow2\asm\asm\4.1\asm-4.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\ow2\asm\asm-tree\4.1\asm-tree-4.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\ow2\asm\asm-commons\4.1\asm-commons-4.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\ow2\asm\asm-util\4.1\asm-util-4.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\ow2\asm\asm-analysis\4.1\asm-analysis-4.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\commons\commons-javaflow\1.0-SNAPSHOT\commons-javaflow-1.0-SNAPSHOT.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\springframework\spring-core\3.0.5.RELEASE\spring-core-3.0.5.RELEASE.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\springframework\spring-asm\3.0.5.RELEASE\spring-asm-3.0.5.RELEASE.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-logging\commons-logging\1.1\commons-logging-1.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\net\sf\json-lib\json-lib\2.4\json-lib-2.4-jdk15.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-lang\commons-lang\2.5\commons-lang-2.5.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\net\sf\ezmorph\ezmorph\1.0.6\ezmorph-1.0.6.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-collections\commons-collections\3.2\commons-collections-3.2.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-beanutils\commons-beanutils\1.8.0\commons-beanutils-1.8.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\bouncycastle\bcprov-jdk16\1.46\bcprov-jdk16-1.46.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\com\google\guava\guava\14.0.1\guava-14.0.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\hbase\hbase\0.90.6-cdh3u5\hbase-0.90.6-cdh3u5.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\hadoop\hadoop-core\0.20.2-cdh3u5\hadoop-core-0.20.2-cdh3u5.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\zookeeper\zookeeper\3.3.6\zookeeper-3.3.6.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\hadoop\thirdparty\guava\guava\r09-jarjar\guava-r09-jarjar.jar;

set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\solr\solr-core\4.0.0\solr-core-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\solr\solr-solrj\4.0.0\solr-solrj-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\zookeeper\zookeeper\3.3.5-cdh3u5\zookeeper-3.3.5-cdh3u5.jar;

set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-analyzers-common\4.0.0\lucene-analyzers-common-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-core\4.0.0\lucene-core-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-analyzers-phonetic\4.0.0\lucene-analyzers-phonetic-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-highlighter\4.0.0\lucene-highlighter-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-memory\4.0.0\lucene-memory-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-misc\4.0.0\lucene-misc-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-queryparser\4.0.0\lucene-queryparser-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-queries\4.0.0\lucene-queries-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-spatial\4.0.0\lucene-spatial-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\com\spatial4j\spatial4j\0.3\spatial4j-0.3.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-suggest\4.0.0\lucene-suggest-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\lucene\lucene-grouping\4.0.0\lucene-grouping-4.0.0.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\slf4j\slf4j-api\1.6.1\slf4j-api-1.6.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\slf4j\slf4j-log4j12\1.6.4\slf4j-log4j12-1.6.4.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\slf4j\slf4j-api\1.6.4\slf4j-api-1.6.4.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-io\commons-io\1.4\commons-io-1.4.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-fileupload\commons-fileupload\1.2.1\commons-fileupload-1.2.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\commons-httpclient\commons-httpclient\3.1\commons-httpclient-3.1.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\httpcomponents\httpclient\4.1.3\httpclient-4.1.3.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\httpcomponents\httpcore\4.1.4\httpcore-4.1.4.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\httpcomponents\httpmime\4.1.3\httpmime-4.1.3.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\javax\servlet\servlet-api\2.3\servlet-api-2.3.jar;

set CLASSPATH=%CLASSPATH%%SMART_PATH%\velocity\velocity\1.5\velocity-1.5.jar;
::set CLASSPATH=%CLASSPATH%%SMART_PATH%\org\apache\velocity\velocity\1.7\velocity-1.7.jar;
set CLASSPATH=%CLASSPATH%%SMART_PATH%\logkit\logkit\1.0.1\logkit-1.0.1.jar;

echo classpath : %CLASSPATH%

java  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%USERPROFILE%\heapDumps -DSmart.Development.Mode=true -DSmart.Codegen.DumpPath=c:\codegen\smartdump -cp %CLASSPATH% org.anon.smart.kernel.SmartKernel %1 yes

