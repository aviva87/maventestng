#!/usr/bin/env bash
    num=$1
    if [ "${num}" = "" ]; then
        num=55
    fi
    sno=4388139872343;
function run() {
	#ctr_time=1566374624000 #控制下发初始时间戳
	zero=$(date +"%Y-%m-%d %H:%M:%S")
	echo "${zero}"
	zeroTime=`date -d "${zero}" +%s`
    ctr_time=$((zeroTime*1000))
    echo "${ctr_time}"
	ctr_cb_time_interval=2000 #控制下发与操作回调时间间隔
	ctr_time_interval=200 #控制下发时间间隔
	cb_time=$((ctr_time+ctr_cb_time_interval))

	#cat sno.txt | while read sno
	for i in $(seq 1 ${num})
	do
	     sno=$((sno+num))
		###控制下发
		curl -X POST \
		http://link-record.t.zihome.com/v1/mq/recordControlTime \
		  -H 'content-type: application/json' \
		  -d '{"msgType":1,"direction":1,"source":3,"sno":"'"${sno}"'","isCallback":1,"isSuccess":1,"time":"'"${ctr_time}"'"}'

		###操作回调
		curl -X POST \
		http://link-record.t.zihome.com/v1/mq/recordControlTime \
		  -H 'content-type: application/json' \
		  -d '{"msgType":1,"direction":2,"source":3,"sno":"'"${sno}"'","isSuccess":1,"isCallback":1,"time":"'"${cb_time}"'"}'
		ctr_time=$((ctr_time+ctr_time_interval))
		cb_time=$((ctr_time+ctr_cb_time_interval))
	done
}

run