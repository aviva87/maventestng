#!/usr/bin/env bash
    num=$1
    if [ "${num}" = "" ]; then
        num=10
    fi
    sno=20190822;
function run() {
	#ctr_time=1566374624000 #控制下发初始时间戳
	zero=$(date +"%Y-%m-%d %H:%M:%S")
	echo "${zero}"
	zeroTime=`date -d "${zero}" +%s`
    ctr_time=$((zeroTime*1000))
    echo "${ctr_time}"
	ctr_cb_time_interval=200 #控制下发与操作回调时间间隔
	ctr_time_interval=200 #控制下发时间间隔
	cb_time=$((ctr_time+ctr_cb_time_interval))

	#cat sno.txt | while read sno
	for i in $(seq 1 ${num})
	do
	     sno=$((sno+num))
	     echo "${sno},${ctr_time}"
		###控制下发
		curl -X POST \
		http://link-record.t.zihome.com/v1/mq/recordControlTime \
		  -H 'content-type: application/json' \
		  -d '{"msgType":1,"direction":1,"source":1,"sno":"'"${sno}"'","isCallback":1,"isSuccess":1,"time":"'"${ctr_time}"'"}'

	done
}

run