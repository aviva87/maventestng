#!/usr/bin/env bash
for i in $(seq 1 100)
do
curl -X POST \
  http://10.216.4.158:8081/v1/device/controlDeviceByOperCode \
  -H 'Accept: */*' \
  -H 'AccessToken: 599705ff0415c7132b8ac66d6dc9f170' \
  -H 'Postman-Token: b67c2a81-f5be-4e70-952d-4554423fc449,5a44d8f1-9898-454f-982a-8f4110304e2d' \
  -H 'User-Agent: PostmanRuntime/7.17.1' \
  -H 'cache-control: no-cache' \
  -d '{"data":[{"k":"bright","v":"1"}],"devUuid":"634c9c9a02bfad2ccd110a870677adf2","hid":"20190917","prodOperCode":"31e9c81af2d9953c2d8cb69a247138ef_1","prodTypeId":"LIGHT_MQTT_00302","sno":"'${sno}'","userCode":"9c58dab7-c04e-4f8d-a745-6bd0be695a59","userName":"aviva"}
'
done