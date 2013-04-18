#!/bin/bash
echo "[Stopping HBase..."]
hbase-0.90.6-cdh3u5/bin/stop-hbase.sh
echo "[Stopping Hadoop..."]
hadoop-0.20.2-cdh3u5/bin/stop-all.sh
echo "[DataStore is down ....]"

