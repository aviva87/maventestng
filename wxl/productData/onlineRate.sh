#!/bin/bash

#while true
#do
    curl -X GET http://gw.zihome.com/link-monitor/v1/data/onlineRate  | tee onlineRate.log
#done