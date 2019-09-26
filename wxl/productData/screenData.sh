#!/bin/bash

#while true
#do
    curl -X POST https://gw.zihome.com/link-monitor/v1/control/screenData  | tee screenData.log
#done