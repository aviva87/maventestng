function getPrdLog() {
    allProdTypeId=$(cat nowDay.log | grep -i 'prodtypeid' | sort | uniq | sed 's:.*\:\ *\"\([^"]*\)".*:\1:')
    for pt in $(echo "${allProdTypeId}")
    do
        echo "${pt}" > allPrd.txt
        cat nowDay.log | grep -A8 -B2 "${pt}" > prodTypeId_${pt}.log
      #  getLastLog prodTypeId_${pt}.log
       # getTimePlus prodTypeId_${pt}.log
    done
}
getPrdLog

   # nowDay=$(date +"%Y-%m-%d")
    nowDay="2019-08-19";
    zero1=$(echo "${nowDay} 00:00:00")
    zeroTime1=`date -d "${zero1}" +%s`
    zeroTime1=$((zeroTime1*1000))
    zero="2019-08-19 17:45:00"
    zeroTime=`date -d "${zero}" +%s`
    zeroTime=$((zeroTime*1000))
    rm -rf data.txt tmestamp.txt lastTime.txt
    cat prodTypeId_SmokeSensor-EM.log | grep -A6 'DEVICE_DISCONNECT'|grep -E "ime|devId" >> data.txt
    if [ -z data.txt ]; then
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
        echo ${sum}> sum.txt
    done
    cat data.txt |grep "lastUpdateTime"| awk '{print $2}'|sed 's:"::g'|sed 's:,::g'>>lastTime.txt
    lsum=0
    cat lastTime.txt |while read tme
    do
        #if [ "${tme}" -lt "${zeroTime}" ];then
        #tme=${zeroTime}
          # echo ${tme}
        #fi
        lsum=$((lsum+tme))
        echo ${lsum}>lsum.txt
    done
    a=$(cat sum.txt);b=$(cat lsum.txt)
    echo "dis:$a last:$b"
    disOnline="$((a*1000-b))"
    echo "disOnline:${disOnline}"

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
getLastLog prodTypeId_SmokeSensor-EM.log
current="2019-08-19 17:45:00"
currentTimeStamp=`date -d "${current}" +%s`
echo "currentTimeStamp:${currentTimeStamp}"
totalConnectDev=$(cat devLast_prodTypeId_SmokeSensor-EM.log | grep '_CON' | wc -l)
echo "deviceTotalNum:${totalConnectDev}"
sum=0
cat devLast_prodTypeId_SmokeSensor-EM.log | grep '_CON' | sed 's:.*time"\: *"\([^"]*\)".*:\1:' | while read tmp
do
    timestamp=`date -d "${tmp}" +%s`
   sum=$((sum+timestamp))
    echo "$sum" > doSum.txt
done
doSum=$(cat doSum.txt)
tmpOnline=$((currentTimeStamp*totalConnectDev-doSum))
tmpOnline=$((tmpOnline*1000))
echo "tmpOnline:$tmpOnline"
allDayOnlineTime=${tmpOnline};
devTotalOnlineTime=$((${disOnline}+${allDayOnlineTime}))
echo "总的设备在线时长：${devTotalOnlineTime}"
fenmu=$(((${zeroTime}-${zeroTime1})));
fenmu=$((${fenmu}*3))
echo "fenmu:${fenmu}"

#percent= ${devTotalOnlineTime} / ${fenmu}
percent_2=`awk 'BEGIN{printf "%.9f%%\n",('$devTotalOnlineTime'/'$fenmu')}'`
echo "percent:${percent_2}"

