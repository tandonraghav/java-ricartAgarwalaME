#!/bin/bash

echo "Wait starting ..."


pid1=`ps -ef|grep "nodeId=node2" | grep -v grep | awk '{print $2}' | xargs kill`

pid2=`ps -ef|grep "nodeId=node1" | grep -v grep | awk '{print $2}' | xargs kill`


sleep 5

cd "$1"

nohup java -DnodeId=node1 -DtcpPort=6666 -Dserver.port=8088 -DpId=1234 -jar target/poc-1.0-SNAPSHOT.jar >nohup.out &


nohup java -DnodeId=node2 -DtcpPort=6667 -Dserver.port=8089 -DpId=1235 -jar target/poc-1.0-SNAPSHOT.jar > nohup2.out &

echo "Wait for a min...Starting 2 java processes"

sleep 20

for i in 1 2 3 4 5
do
  curl -X GET "http://localhost:8088/test" &
  curl -X GET "http://localhost:8089/test" &
done

tail -f nohup.out nohup2.out
