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
#getLastLog prodTypeId_M02C02D02Z02.log
### 获得网关和设备的log
function getTimePlus(){
   rm -rf data.txt tmestamp.txt sum.txt lastTime.txt lsum.txt
   # nowDay=$(date +"%Y-%m-%d")
    echo "=====getTimePlus $1====="
    nowDay="2019-08-26"
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
 #   rm -rf data.txt tmestamp.txt sum.txt lastTime.txt lsum.txt
}
#getTimePlus prodTypeId_M02C02D02Z02.log
function getGatewayLastConnectTimePlus(){
   rm -rf gataWaytmestamp.txt data_$1
  cat $1 |grep "DEVICE_CONNECT"|grep -E "ime|devId" >> data_$1

 cat data_$1 | sed 's:^.*time"\:\ *"\([^"]*\)".*:\1:' | while read tme
    do
        echo "time:============${tme}===================="
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
#    current="2019-08-27 00:00:00"
#    currentTimeStamp=`date -d "${current}" +%s`
#    currentTimeStamp=$((currentTimeStamp*1000))
#    echo "currentTimeStamp:${currentTimeStamp}"
#    zero="2019-08-26 00:00:00"
#    zeroTime=`date -d "${zero}" +%s`
#    zeroTime=$((zeroTime*1000))
#    AlltotalTime=$(($currentTimeStamp-$zeroTime))
#    echo "AlltotalTime:${AlltotalTime}"
#    echo "$((AlltotalTime*1))"
#    deviceOnlineTime=$((onlineTime+$((AlltotalTime*1))))
#    echo "deviceOnlineTime:${deviceOnlineTime}"

#    totalConnectDev=$(cat devLast_dev.log | grep '_CON' | wc -l)
#    echo "deviceTotalNum:${totalConnectDev}"

   }
#getGatewayLastConnectTimePlus devLast_prodTypeId_M02C02D02Z02.log
   ###获取所有prodTypeId的log
function getPrdLog() {
    rm -rf prodTypeId_*
    allProdTypeId=$(cat nowDay.log | grep -i 'prodtypeid' | sort | uniq | sed 's:.*\:\ *\"\([^"]*\)".*:\1:')
    for pt in $(echo "${allProdTypeId}")
    do
        cat nowDay.log | grep -A8 -B2 "${pt}" > prodTypeId_${pt}.log
        getLastLog prodTypeId_${pt}.log
        getTimePlus prodTypeId_${pt}.log
        getGatewayLastConnectTimePlus devLast_prodTypeId_${pt}.log
    done
}
getPrdLog

function getGatewayAndDev() {
    cat $1 | grep -A2 -B11 -E 'gateway":\ *true' > gateway.log
    cat $1 | grep -A2 -B12 -E 'gateway":\ *false' > dev.log
#    getLastLog gateway.log
#    getTimePlus gateway.log
#    getGatewayLastConnectTimePlus devLast_gateway.log
#     getLastLog dev.log
#     getTimePlus dev.log
#    getGatewayLastConnectTimePlus devLast_dev.log

}
getGatewayAndDev nowDay.log



function getDiffWithLastTimeAndTimestamp() {

    sed -i '/^$/d' lastTime.txt
    sed -i '/^$/d' tmestamp.txt
    rm -rf tmestamp-lastTime.txt
    num=$(cat tmestamp.txt | wc -l)
    num=$((num+1))
    for i in $(seq 1 ${num})
    do
        lastTime=$(sed -n "${i}p" lastTime.txt)
        timestamp=$(sed -n "${i}p" tmestamp.txt)
        timestamp=$((timestamp*1000))
        difference=$((timestamp-lastTime))
        echo "${difference}" >> tmestamp-lastTime.txt
        echo "lastTime:${lastTime} timestamp:${timestamp} difference:${difference}"
    done
}
getDiffWithLastTimeAndTimestamp