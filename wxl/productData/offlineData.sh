#!/bin/bash

#while true
#do
    curl -X GET https://gw.zihome.com/link-monitor/v1/data/offlineData  | tee offlineData.log
#done