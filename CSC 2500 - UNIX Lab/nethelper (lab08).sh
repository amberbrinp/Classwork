#!/bin/bash

function read_hosts {
	hosts=`cat $1`
	count=1
	for host in $hosts; do
		hosts_array[$count]=$host
		count=`expr $count + 1`
	done
}

function pick_host {
	count=1
	for host in $hosts; do
		echo "$count) $host"
		count=`expr $count + 1`
	done
	echo "Enter a number to select a host"
	read which_host
	if [[ $which_host -lt 1 || $which_host -ge $count ]]
	then
		echo "invalid host choice"
		exit
	fi		
}

done=0
while [ $done -eq 0 ]
do
        read_hosts $@
        echo -e "(P)ing a host\n(S)sh to a host\n(T)race route to host\n(N)slookup host\n(Q)uit\nPlease select a command:"
        read cmd
        case $cmd in
                P|p)
                        pick_host "$hosts"
                        echo "ping -c 1 ${hosts_array[$which_host]}"
                        ping -c 1 ${hosts_array[$which_host]}
                        ;;
                S|s)
                        pick_host "$hosts"
                        read -p "Username: " user
                        echo "ssh $user@${hosts_array[$which_host]}"
			ssh $user@${hosts_array[$which_host]}
                        ;;
                N|n)
                        pick_host "$hosts"
                        echo "nslookup ${hosts_array[$which_host]}"
                        nslookup ${hosts_array[$which_host]}
                        ;;
                T|t)
                        pick_host "$hosts"
                        echo "traceroute ${hosts_array[$which_host]}"
                        traceroute ${hosts_array[$which_host]}
                        ;;
                Q|q)
                        done=1;
                        ;;
                *)
                        echo "Bad choice";
                        ;;
        esac
done			
