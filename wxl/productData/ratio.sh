#!/bin/bash

#while true
#do
    curl -X POST https://gw.zihome.com/link-monitor/v1/data/ratio -H 'Content-Type: application/json' -d '{ "type": 4 }'  | tee ratio.log
#done