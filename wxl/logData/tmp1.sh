#!/usr/bin/env bash

### 获取指定时间段的日志，默认取当天log
function getLog() {
    sourceLog="taskmanager.log" #服务器原始log
    if [ "$1" = "" ];then
        startTime=$(date +"%Y-%m-%d")
    else
        startTime=$1
    fi
    if [ "$2" = "" ];then
        endTime=$(date -d next-day +%"Y-%m-%d")
    else
        endTime=$2
    fi
    echo "startTime：$startTime ==== endTime：$endTime"
    startLine=$(cat ${sourceLog} | grep -n "${startTime}" | head -1 | awk -F ":" '{print $1}')
    if [ "${startLine}" = "" ];then
        echo "${sourceLog}中没有${startTime}的log，退出。"
        exit
    fi
    endLine=$(cat ${sourceLog} | grep -n "${endTime}" | head -1 | awk -F ":" '{print $1}')
    if [ "${endLine}" = "" ];then
        endLine=$(cat ${sourceLog} | wc -l)
    else
        endLine=$((endLine-1))
    fi
    sed -n "${startLine},${endLine}p" ${sourceLog} > nowDay.log
}

###获取log中所有设备最后一条log
function getLastLog() {
    if [ "$1" = "" ]; then
        echo "param error!"
        exit
    fi
    devLastlog="devLast_$1"
    rm -rf ${devLastlog}
    allDevId=$(cat $1 | grep 'devId' | sort | uniq | sed 's:.*\: *\"\([0-9a-zA-Z]*\).*:\1:')
    for devId in $(echo "${allDevId}")
    do
        echo "===" >> ${devLastlog}
        cat $1 | grep -A8 -B1 -E "devId.*${devId}" | tail -10 | tr ',\n' ' ' >> ${devLastlog}
    done
    cat ${devLastlog} | sed 's:=*::g' | sed '/^$/d' | sed 's:" *":"\t":g' > ${devLastlog}
}

###获取未连接设备time和lastUpdateTime差值
function getTimePlus(){
   # nowDay=$(date +"%Y-%m-%d")
    echo "=====getTimePlus $1====="
    nowDay="2019-08-17"
    zero=$(echo "${nowDay} 00:00:00")
    zeroTime=`date -d "${zero}" +%s`
    zeroTime=$((zeroTime*1000))
    rm -rf data.txt tmestamp.txt lastTime.txt
    cat $1 | grep -A5 'DEVICE_CONNECT'|grep -E "ime|devId" >> data.txt
    if [ ! -s data.txt ]; then
        return
    fi
    cat data.txt |grep "time"| awk '{print $2,$3}'|sed 's:"::g'|sed 's:,::g'| while read tme
    do
        echo "${tme}"
        date -d "${tme}" +%s >> tmestamp.txt
    done
    sum=0
    cat tmestamp.txt| while read tme
    do
        sum=$((sum+tme))
        echo ${sum} > sum.txt
    done
    cat data.txt |grep "lastUpdateTime"| awk '{print $2}'|sed 's:"::g'|sed 's:,::g' >> lastTime.txt
    lsum=0
    cat lastTime.txt |while read tme
    do
        if [ "${tme}" -lt "${zeroTime}" ];then
            tme=${zeroTime}
        fi
        lsum=$((lsum+tme))
        echo ${lsum} > lsum.txt
    done
    totalTime=$(cat sum.txt)
    totalLastUpdateTime=$(cat lsum.txt)
    echo "total time:${totalTime} total lastUpdateTime:${totalLastUpdateTime}"
    totalDisOnlineTime=$((totalTime*1000-totalLastUpdateTime))
    echo "total disOnline Time:${totalDisOnlineTime}"
    ###删除产生的中间文件
    rm -rf data.txt tmestamp.txt sum.txt lastTime.txt lsum.txt
}

### 获得网关和设备的log
function getGatewayAndDev() {
    cat nowDay.log | grep -A2 -B7 -E 'gateway":\ *true' > gateway.log
    cat nowDay.log | grep -A2 -B7 -E 'gateway":\ *false' > dev.log
    getLastLog gateway.log
    getTimePlus gateway.log
    getLastLog dev.log
    getTimePlus dev.log
}

###获取所有prodTypeId的log
function getPrdLog() {
    allProdTypeId=$(cat nowDay.log | grep -i 'prodtypeid' | sort | uniq | sed 's:.*\:\ *\"\([^"]*\)".*:\1:')
    for pt in $(echo "${allProdTypeId}")
    do
        cat nowDay.log | grep -A8 -B2 "${pt}" > prodTypeId_${pt}.log
        getLastLog prodTypeId_${pt}.log
        getTimePlus prodTypeId_${pt}.log
        #rm -rf *prodTypeId_*
    done
}

function run() {
    getLog "2019-08-17" "2019-08-18"
    getGatewayAndDev
    getPrdLog
}

run