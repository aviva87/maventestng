#!/usr/bin/env bash
#nowDay=$(date +"%Y-%m-%d");
nowDay="2018-08-17";
nextDay=$(date -d next-day +%Y-%m-%d);
nextDay="2019-08-18";
startLine=$(cat taskmanager.log | grep -n "${nowDay}" | head -1 | awk -F ":" '{print $1}');
endLine=$(cat taskmanager.log | grep -n "${nextDay}" | head -1 | awk -F ":" '{print $1}');
endLine=$((endLine-1));
sed -n "${startLine},${endLine}p" taskmanager.log > nowDay.log;
rm -rf devLast.txt;
allDevId=$(cat nowDay.log | grep 'devId' | sort | uniq | sed 's:.*\: *\"\([0-9a-zA-Z]*\).*:\1:');
for devId in $(echo "${allDevId}"); do echo "===" >> devLast.txt; cat nowDay.log | grep -A8 -B1 -E "devId.*${devId}" | tail -10 | tr ',\n' ' ' >> devLast.txt; done
cat devLast.txt | sed 's:===::g' | sed '/^$/d' > devLast.txt
totalOnlineDev=$(cat tmp.log | grep '全天在线设备总数' | sed 's:.*\[\([0-9]*\).*:\1:')
deviceTotalNum=$(cat tmp.log | grep '归总计算gatewayOnlineTime' | sed 's:.*deviceTotalNum *【\([0-9]*\)】.*:\1:')
$mongodb

nowDay=$(date +"%Y-%m-%d")
zero=$(echo "${nowDay} 00:00:00")
current=`date "+%Y-%m-%d %H:%M:%S"`
currentTimeStamp=`date -d "${current}" +%s`
zeroTimestamp=`date -d "${zero}" +%s`
timeInterval=$((currentTimeStamp-zeroTimestamp))
timeInterval=$((timeInterval*1000*totalOnlineDev))
echo "${zeroTimestamp} ${currentTimeStamp} ${timeInterval}"
totalConnectDev=$(cat devLast.txt | grep '_CON' | wc -l)
sum=0
cat devLast.txt | grep '_CON' | sed 's:.*time"\: *"\([^"]*\)".*:\1:' | while read tmp
do
timestamp=`date -d "${tmp}" +%s`
sum=$((sum+timestamp))
echo "$sum" > doSum.txt
done
doSum=$(cat doSum.txt)
tmpOnline=$((currentTimeStamp*totalConnectDev-doSum))
tmpOnline=$((tmpOnline*1000))
echo "$tmpOnline"


rm -rf data.txt tmestamp.txt lastTime.txt
cat nowDay.log | grep -A5 'DEVICE_DISCONNECT'|grep -E "ime|devId" >> data.txt
cat data.txt |grep "time"| awk '{print $2,$3}'|sed 's:"::g'|sed 's:,::g'| while read tme
do
date -d "${tme}" +%s >> tmestamp.txt
done
sum=0
cat tmestamp.txt| while read tme
do
echo tme:${tme}
sum=$((sum+tme))
echo ${sum}> sum.txt
done
cat data.txt |grep "lastUpdateTime"| awk '{print $2}'|sed 's:"::g'|sed 's:,::g'>>lastTime.txt
lsum=0
cat lastTime.txt |while read tme
do
echo ${tme}
lsum=$((lsum+tme))
echo ${lsum}>lsum.txt
done
a=$(cat sum.txt);b=$(cat lsum.txt)
disOnline=$(echo disOnline: "$((a*1000-b))")

devTotalOnlineTime=$((timeInterval+tmpOnline+disOnline))
echo "总的设备在线时长：$devTotalOnlineTime"

percent=$((devTotalOnlineTime/currentTimeStamp*deviceTotalNum))