#!/bin/bash
echo "Enter a month number"
read month
cal -m $month
echo " "

exec lab02.sh
