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
    endLine=$(cat ${sourceLog} | grep -n " ${endTime}" | head -1 | awk -F ":" '{print $1}')
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
    if [ "${allDevId}" = "" ];then
        return
    fi
    for devId in $(echo "${allDevId}")
    do
        echo "===" >> ${devLastlog}
        cat $1 | grep -A8 -B1 -E "devId.*${devId}" | tail -10 | tr ',\n' ' ' >> ${devLastlog}
    done
    cat ${devLastlog} | sed 's:=*::g' | sed '/^$/d' | sed 's:" *":"\t":g' > ${devLastlog}
}

###获取未连接设备time和lastUpdateTime差值
function getTimePlus(){
   rm -rf data.txt tmestamp.txt sum.txt lastTime.txt lsum.txt
   # nowDay=$(date +"%Y-%m-%d")
    echo "=====getTimePlus $1====="
    nowDay="2019-08-27"
    zero=$(echo "${nowDay} 00:00:00")
    zeroTime=`date -d "${zero}" +%s`
    zeroTime=$((zeroTime*1000))
    rm -rf data.txt tmestamp.txt lastTime.txt
    cat $1 | grep -A7 'DEVICE_DISCONNECT'|grep -E "ime|devId" >> data.txt
    if [ ! -s data.txt ]; then
        return
    fi
    cat data.txt |grep "time"| awk '{print $2,$3}'|sed 's:"::g'|sed 's:,::g'| while read tme
    do
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
    echo ${totalDisOnlineTime}>totalDisOnlineTime.txt
    ###删除产生的中间文件
    rm -rf data.txt tmestamp.txt sum.txt lastTime.txt lsum.txt
}
### 获得网关和设备的log
function getGatewayAndDev() {
    cat nowDay.log | grep -A2 -B11 -E 'gateway":\ *true' > gateway.log
    cat nowDay.log | grep -A2 -B12 -E 'gateway":\ *false' > dev.log
    getLastLog gateway.log
    getTimePlus gateway.log
    getLastLog dev.log
    getTimePlus dev.log
}
#getGatewayAndDev


function getGatewayLastConnectTimePlus(){
    echo "=====getGatewayLastConnectTimePlus $1====="
   rm -rf gataWaytmestamp.txt data_$1
  cat $1 |grep "DEVICE_CONNECT"|grep -E "ime|devId" >> data_$1
  cat data_$1 | sed 's:^.*time"\:\ *"\([^"]*\)".*:\1:' | while read tme
    do
        date -d "${tme}" +%s >> gataWaytmestamp.txt
    done
    sum=0
    cat gataWaytmestamp.txt| while read tme
    do
        sum=$((sum+tme))
        echo ${sum} > gateWayLastConnectsum.txt
    done
    LastConnectGatewayNum=$(cat data_$1 |wc -l)
    echo "LastConnectGatewayNum:${LastConnectGatewayNum}"
    nowDay="2019-08-27"
    zero=$(echo "${nowDay} 00:00:00")
    zeroTime=`date -d "${zero}" +%s`
    gateWayLastConnectTime=$((zeroTime*${LastConnectGatewayNum}))
    echo "gateWayLastConnectTime:${gateWayLastConnectTime}"
    gateWayLastConnectOnlineTimeSum=$(cat gateWayLastConnectsum.txt)
    gateWayLastConnectOnlineTime=$((${gateWayLastConnectTime}-${gateWayLastConnectOnlineTimeSum}))
    echo "gateWayLastConnectOnlineTime:${gateWayLastConnectOnlineTime}"
   totalDisOnlineTime=$(cat totalDisOnlineTime.txt);
   onlineTime=$((${totalDisOnlineTime}+$((gateWayLastConnectOnlineTime*1000))))
   echo "onlineTime:${onlineTime}"
   }
###获取所有prodTypeId的log
function getPrdLog() {
    allProdTypeId=$(cat nowDay.log | grep -i 'prodtypeid' | sort | uniq | sed 's:.*\:\ *\"\([^"]*\)".*:\1:')
    for pt in $(echo "${allProdTypeId}")
    do
        cat nowDay.log | grep -A8 -B2 "${pt}" > prodTypeId_${pt}.log
        getLastLog prodTypeId_${pt}.log
        getTimePlus prodTypeId_${pt}.log
        getGatewayLastConnectTimePlus devLast_prodTypeId_${pt}.log
        #rm -rf *prodTypeId_*
    done
}
getPrdLog

function run() {
   # getLog "2019-08-17" "2019-08-18"
    getGatewayAndDev
    getPrdLog
}

#run


#####################################################################################3
#nowDay=$(date +"%Y-%m-%d")
#nowDay="2019-08-23";
#zero=$(echo "${nowDay} 16:00:00")
#current=`date "+%Y-%m-%d %H:%M:%S"`
current="2019-08-25 00:00:00"
currentTimeStamp=`date -d "${current}" +%s`
echo "currentTimeStamp:${currentTimeStamp}"
totalConnectDev=$(cat devLast_dev.log | grep '_CON' | wc -l)
echo "deviceTotalNum:${totalConnectDev}"
sum=0
#cat devLast_dev.log | grep '_CON' | sed 's:.*time"\: *"\([^"]*\)".*:\1:' | while read tmp
#do
 #   timestamp=`date -d "${tmp}" +%s`
  #  sum=$((sum+timestamp))
   # echo "$sum" > doSum.txt
#done
#doSum=$(cat doSum.txt)
#tmpOnline=$((currentTimeStamp*totalConnectDev-doSum))
#tmpOnline=$((tmpOnline*1000))
#echo "tmpOnline:$tmpOnline"
allDayOnlineTime=$((0*24*60*60*1000));
echo "allDayOnlineTime:${allDayOnlineTime}"
devTotalOnlineTime=$((${onlineTime}+${allDayOnlineTime}))
echo "总的设备在线时长：${devTotalOnlineTime}"
fenmu=$((24*60*60*1000*4))
echo "fenmu:${fenmu}"

#percent= ${devTotalOnlineTime} / ${fenmu}
percent_2=`awk 'BEGIN{printf "%.6f%%\n",('$devTotalOnlineTime'/'$fenmu')*1000}'`
echo "percent:${percent_2}"