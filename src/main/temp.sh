#!/bin/bash
for i in 1 2 3 4 5
do
  curl -X GET "http://localhost:8088/test" &
  curl -X GET "http://localhost:8089/test" &
done