#!/bin/bash

#while true
#do
    curl -X GET http://gw.zihome.com/link-monitor/v1/screen/alarm/getScreenAlarmData  | tee getScreenAlarmData.log
#done