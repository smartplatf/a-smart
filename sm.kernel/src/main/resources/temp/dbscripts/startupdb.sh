#!/bin/bash
echo "[Starting Hadoop..."]
hadoop-0.20.2-cdh3u5/bin/start-all.sh
echo "[Starting HBase..."]
hbase-0.90.6-cdh3u5/bin/start-hbase.sh
echo "[DataStore is Up ....]"

