#!/usr/bin/env bash

###该脚本用于生成接口脚本，从interface.txt中读取接口，然后给每个接口按照脚本模板template.txt生成各自的脚本
cat interface.txt | sed 's:.*Postman-Token.*::' | sed 's:.*cache-control.*::' | tr '\\\n' ' ' | sed 's:curl:\ncurl:g' | sed '/^$/d' | tr -s ' ' > tmpIf.txt
for ifName in $(cat tmpIf.txt | sed 's:.*\(http[^ ]*\).*:\1:' | sed 's:.*/::')
do
    cp template.txt ${ifName}_tmp.sh
    if=$(cat tmpIf.txt | grep ${ifName} | sed 's:\::aaa:g')
    #echo "=====${ifName} : ${if}====="
    cat ${ifName}_tmp.sh | sed "s:curlCmd:${if} | tee ${ifName}.log:" | sed 's:aaa:\::g' > ${ifName}.sh
done
rm -rf *_tmp.sh tmpIf.txt