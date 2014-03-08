#Set-ExecutionPolicy --ExecutionPolicy ByPass -Scope CurrentUser
function createdir
{
	param($dir)
	if (test-path $dir)
	{
	   Write-Output "Directory already exists"
	}
	else
	{
	    mkdir -p $dir
	}
}


function wget_url
{
	param($source, $dest)
	if (test-path $dest)
	{
	   Write-Output $("File Already downloaded " + $dest)
	}
	else
	{
	   Write-Output $("Downloading " + $source + " to " + $dest)
	   $error.clear()
	   $wc = New-Object System.Net.WebClient
	   $wc.DownloadFile($source, $dest)
	   if ($error[0])
	   {
	       $error[0].Exception.ToString()
	       exit
	   }
	}
}

#################################################################################################
# Modify the following dirtories to reflect your set up
#################################################################################################
$currpath = (Get-Location).Path
$scriptdir = $($currpath + "\scripts\")
$install = $($currpath + "\smartinstall")

#################################################################################################
# Set up smart code
#################################################################################################
$mylink = $($scriptdir + "mylink.exe")
$installlib = $($install + "\lib\")
$configdir = $($install + "\config\")
$libdir = "lib\"
$smartver = "2.1"

Write-Output "[Creating directories]"
createdir $install
createdir $installlib
createdir $configdir
cd $install
cp $($scriptdir + "\startSmart.bat") .
cp -R $($scriptdir + "\solr-datastore\") .
cp -R $($scriptdir + "\testclient\") .

Get-Content $($scriptdir + "smartDirs.txt") | ForEach-Object { createdir $($libdir + $_) }

Write-Output "[Retrieving dependant jars. This may take sometime plase wait. *******************************************]"
wget_url "http://repo1.maven.org/maven2/log4j/log4j/1.2.16/log4j-1.2.16.jar" $($installlib + "log4j\log4j\1.2.16\log4j-1.2.16.jar")
wget_url "http://repo1.maven.org/maven2/jcs/jcs/1.3/jcs-1.3.jar" $($installlib + "jcs/jcs/1.3/jcs-1.3.jar")
wget_url "http://repo1.maven.org/maven2/org/yaml/snakeyaml/1.5/snakeyaml-1.5.jar" $($installlib + "org/yaml/snakeyaml/1.5/snakeyaml-1.5.jar")
wget_url "http://repo1.maven.org/maven2/org/ow2/asm/asm/4.1/asm-4.1.jar" $($installlib + "org/ow2/asm/asm/4.1/asm-4.1.jar")
wget_url "http://repo1.maven.org/maven2/org/ow2/asm/asm-tree/4.1/asm-tree-4.1.jar" $($installlib + "org/ow2/asm/asm-tree/4.1/asm-tree-4.1.jar")
wget_url "http://repo1.maven.org/maven2/org/ow2/asm/asm-commons/4.1/asm-commons-4.1.jar" $($installlib + "org/ow2/asm/asm-commons/4.1/asm-commons-4.1.jar")
wget_url "http://repo1.maven.org/maven2/org/ow2/asm/asm-util/4.1/asm-util-4.1.jar" $($installlib + "org/ow2/asm/asm-util/4.1/asm-util-4.1.jar")
wget_url "http://repo1.maven.org/maven2/org/ow2/asm/asm-analysis/4.1/asm-analysis-4.1.jar" $($installlib + "org/ow2/asm/asm-analysis/4.1/asm-analysis-4.1.jar")
wget_url "http://repo1.maven.org/maven2/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar" $($installlib + "org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar")
wget_url "http://repo1.maven.org/maven2/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar" $($installlib + "org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar")
wget_url "http://repo1.maven.org/maven2/commons-logging/commons-logging/1.1/commons-logging-1.1.jar" $($installlib + "commons-logging/commons-logging/1.1/commons-logging-1.1.jar")
wget_url "http://repo1.maven.org/maven2/commons-codec/commons-codec/1.4/commons-codec-1.4.jar" $($installlib + "commons-codec/commons-codec/1.4/commons-codec-1.4.jar")
wget_url "http://repo1.maven.org/maven2/net/sf/json-lib/json-lib/2.4/json-lib-2.4-jdk15.jar" $($installlib + "net/sf/json-lib/json-lib/2.4/json-lib-2.4.jar")
wget_url "http://repo1.maven.org/maven2/commons-lang/commons-lang/2.5/commons-lang-2.5.jar" $($installlib + "commons-lang/commons-lang/2.5/commons-lang-2.5.jar")
wget_url "http://repo1.maven.org/maven2/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar" $($installlib + "net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar")
wget_url "http://repo1.maven.org/maven2/commons-collections/commons-collections/3.2/commons-collections-3.2.jar" $($installlib + "commons-collections/commons-collections/3.2/commons-collections-3.2.jar")
wget_url "http://repo1.maven.org/maven2/commons-beanutils/commons-beanutils/1.8.0/commons-beanutils-1.8.0.jar" $($installlib + "commons-beanutils/commons-beanutils/1.8.0/commons-beanutils-1.8.0.jar")
wget_url "http://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk16/1.46/bcprov-jdk16-1.46.jar" $($installlib + "org/bouncycastle/bcprov-jdk16/1.46/bcprov-jdk16-1.46.jar")
wget_url "http://repo1.maven.org/maven2/com/google/guava/guava/14.0.1/guava-14.0.1.jar" $($installlib + "com/google/guava/guava/14.0.1/guava-14.0.1.jar")
wget_url "http://repo1.maven.org/maven2/concurrent/concurrent/1.0/concurrent-1.0.jar" $($installlib + "concurrent/concurrent/1.0/concurrent-1.0.jar")
wget_url "http://repo1.maven.org/maven2/io/netty/netty/3.6.5.Final/netty-3.6.5.Final.jar" $($installlib + "io/netty/netty/3.6.5.Final/netty-3.6.5.Final.jar")

#wget_url "http://repo1.maven.org/maven2/org/apache/commons/commons-javaflow/1.0-SNAPSHOT/commons-javaflow-1.0-SNAPSHOT.jar" $($installlib + "org/apache/commons/commons-javaflow/1.0-SNAPSHOT/commons-javaflow-1.0-SNAPSHOT.jar")
wget_url "http://repo1.maven.org/maven2/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar" $($installlib + "org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar")
wget_url "http://repo1.maven.org/maven2/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar" $($installlib + "org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar")

wget_url "https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-0.90.6-cdh3u5.jar" $($installlib + "org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-0.90.6-cdh3u5.jar")
wget_url "https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/hadoop/hadoop-core/0.20.2-cdh3u5/hadoop-core-0.20.2-cdh3u5.jar" $($installlib + "org/apache/hadoop/hadoop-core/0.20.2-cdh3u5/hadoop-core-0.20.2-cdh3u5.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/zookeeper/zookeeper/3.3.6/zookeeper-3.3.6.jar" $($installlib + "org/apache/zookeeper/zookeeper/3.3.6/zookeeper-3.3.6.jar")
wget_url "https://repository.cloudera.com/content/groups/cloudera-repos/org/apache/hadoop/thirdparty/guava/guava/r09-jarjar/guava-r09-jarjar.jar" $($installlib + "org/apache/hadoop/thirdparty/guava/guava/r09-jarjar/guava-r09-jarjar.jar")

wget_url "https://repository.cloudera.com/artifactory/cloudera-repos/org/apache/zookeeper/zookeeper/3.3.5-cdh3u5/zookeeper-3.3.5-cdh3u5.jar" $($installlib + "hadoop/hbase-0.90.6-cdh3u5/lib/zookeeper-3.3.5-cdh3u5.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar" $($installlib + "org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar" $($installlib + "org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/4.0.0/lucene-analyzers-common-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-analyzers-common/4.0.0/lucene-analyzers-common-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-phonetic/4.0.0/lucene-analyzers-phonetic-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-analyzers-phonetic/4.0.0/lucene-analyzers-phonetic-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-highlighter/4.0.0/lucene-highlighter-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-highlighter/4.0.0/lucene-highlighter-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-memory/4.0.0/lucene-memory-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-memory/4.0.0/lucene-memory-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-misc/4.0.0/lucene-misc-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-misc/4.0.0/lucene-misc-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-queryparser/4.0.0/lucene-queryparser-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-queryparser/4.0.0/lucene-queryparser-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-queries/4.0.0/lucene-queries-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-queries/4.0.0/lucene-queries-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-spatial/4.0.0/lucene-spatial-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-spatial/4.0.0/lucene-spatial-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/com/spatial4j/spatial4j/0.3/spatial4j-0.3.jar" $($installlib + "com/spatial4j/spatial4j/0.3/spatial4j-0.3.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-suggest/4.0.0/lucene-suggest-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-suggest/4.0.0/lucene-suggest-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/lucene/lucene-grouping/4.0.0/lucene-grouping-4.0.0.jar" $($installlib + "org/apache/lucene/lucene-grouping/4.0.0/lucene-grouping-4.0.0.jar")
wget_url "http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar" $($installlib + "org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar")
wget_url "http://repo1.maven.org/maven2/org/slf4j/slf4j-log4j12/1.6.4/slf4j-log4j12-1.6.4.jar" $($installlib + "org/slf4j/slf4j-log4j12/1.6.4/slf4j-log4j12-1.6.4.jar")
wget_url "http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.4/slf4j-api-1.6.4.jar" $($installlib + "org/slf4j/slf4j-api/1.6.4/slf4j-api-1.6.4.jar")
wget_url "http://repo1.maven.org/maven2/commons-io/commons-io/1.4/commons-io-1.4.jar" $($installlib + "commons-io/commons-io/1.4/commons-io-1.4.jar")
wget_url "http://repo1.maven.org/maven2/commons-fileupload/commons-fileupload/1.2.1/commons-fileupload-1.2.1.jar" $($installlib + "commons-fileupload/commons-fileupload/1.2.1/commons-fileupload-1.2.1.jar")
wget_url "http://repo1.maven.org/maven2/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar" $($installlib + "commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.1.3/httpclient-4.1.3.jar" $($installlib + "org/apache/httpcomponents/httpclient/4.1.3/httpclient-4.1.3.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.1.4/httpcore-4.1.4.jar" $($installlib + "org/apache/httpcomponents/httpcore/4.1.4/httpcore-4.1.4.jar")
wget_url "http://repo1.maven.org/maven2/org/apache/httpcomponents/httpmime/4.1.3/httpmime-4.1.3.jar" $($installlib + "org/apache/httpcomponents/httpmime/4.1.3/httpmime-4.1.3.jar")
wget_url "http://repo1.maven.org/maven2/javax/servlet/servlet-api/2.3/servlet-api-2.3.jar" $($installlib + "javax/servlet/servlet-api/2.3/servlet-api-2.3.jar")
wget_url "http://repo1.maven.org/maven2/velocity/velocity/1.5/velocity-1.5.jar" $($installlib + "velocity/velocity/1.5/velocity-1.5.jar")
wget_url "http://repo1.maven.org/maven2/logkit/logkit/1.0.1/logkit-1.0.1.jar" $($installlib + "logkit/logkit/1.0.1/logkit-1.0.1.jar")
wget_url "http://repo1.maven.org/maven2/javax/mail/mail/1.4/mail-1.4.jar" $($installlib + "javax/mail/mail/1.4/mail-1.4.jar")




Write-Output "[Copying smart jars **************************************************************]"
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.atomicity/" + $smartver + "/sm.atomicity-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.atomicity/" + $smartver + "/sm.atomicity-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.base/" + $smartver + "/sm.base-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.base/" + $smartver + "/sm.base-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.channels/" + $smartver + "/sm.channels-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.channels/" + $smartver + "/sm.channels-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.codegen/" + $smartver + "/sm.codegen-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.codegen/" + $smartver + "/sm.codegen-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.d2cache/" + $smartver + "/sm.d2cache-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.d2cache/" + $smartver + "/sm.d2cache-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.deployment/" + $smartver + "/sm.deployment-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.deployment/" + $smartver + "/sm.deployment-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.generator/" + $smartver + "/sm.generator-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.generator/" + $smartver + "/sm.generator-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.kernel/" + $smartver + "/sm.kernel-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.kernel/" + $smartver + "/sm.kernel-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.monitor/" + $smartver + "/sm.monitor-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.monitor/" + $smartver + "/sm.monitor-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.secure/" + $smartver + "/sm.secure-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.secure/" + $smartver + "/sm.secure-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.smcore/" + $smartver + "/sm.smcore-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.smcore/" + $smartver + "/sm.smcore-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/smart/sm.template/" + $smartver + "/sm.template-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/smart/sm.template/" + $smartver + "/sm.template-" + $smartver + ".jar")
wget_url $("https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/utilities/utilities/" + $smartver + "/utilities-" + $smartver + ".jar?raw=true") $($installlib + "/org/anon/utilities/utilities/" + $smartver + "/utilities-" + $smartver + ".jar")


Write-Output "[Copying FX flow jars **************************************************************]"
wget_url https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/fixchg/fx.catalogue/1.0/fx.catalogue-1.0.jar?raw=true $($installlib + "/org/anon/fixchg/fx.catalogue/1.0/fx.catalogue-1.0.jar")
wget_url https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/fixchg/fx.contact/1.0/fx.contact-1.0.jar?raw=true $($installlib + "/org/anon/fixchg/fx.contact/1.0/fx.contact-1.0.jar")
wget_url https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/fixchg/fx.registration/1.0/fx.registration-1.0.jar?raw=true $($installlib + "/org/anon/fixchg/fx.registration/1.0/fx.registration-1.0.jar")
wget_url https://github.com/smartplatf/smart-releases/blob/master/releases/org/anon/fixchg/fx.subscribe/1.0/fx.subscribe-1.0.jar?raw=true $($installlib + "/org/anon/fixchg/fx.subscribe/1.0/fx.subscribe-1.0.jar")

Write-Output "[Setting up soflink to lib for secure server path dependency ********]"
& $mylink '/d' 'org' '.\lib\org'

Write-Output "[Setting up environment ********]"
Add-Content setupEnv.bat $("set SMART_PATH=/" + $install + "\")
Add-Content setupEnv.bat $("set SMART_LIB_PATH=" + $installlib)
Add-Content setupEnv.bat $("set SMART_VERSION=" + $smartver)

Write-Output "[Setting up environment completed with creating setupEnv ********]"

