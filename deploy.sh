#!/bin/bash

cd /home/ec2-user/application/libs

pkill java # 기존 프로세스 종료
sleep 20

chmod +x ./*SNAPSHOT.jar # jar 파일에 실행권한 부여
nohup java -jar ./*SNAPSHOT.jar > nohup.out 2>&1 & # 백그라운드 실행
