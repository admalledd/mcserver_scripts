#!/bin/bash
#Download the latest backup data from the server to the local computer (executed from CRON)

cd ~/minecraft/
source ~/.secrets/mc.admalledd.com/mysql

rsync -av --del mc.admalledd.com:/home/admalledd/minecraft/backup/ ./backup/
mv mysql.gz "mysql/$(date +%Y%m%d%H%M%S -r "mysql.gz")-mysql.gz"

echo "mysqldump -u$MC_ADMALLEDD_COM_MYSQL_USER -p$MC_ADMALLEDD_COM_MYSQL_PW --all-databases|gzip" |ssh -C mc.admalledd.com 'bash -s' |pv >mysql.gz
