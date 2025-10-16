#!/bin/bash


echo  "Enter host name: "

read host

echo "Pinging $host..."

ping -c 3 $host > /dev/null 2>&1


if [ $? -eq 0 ]
then
        echo "✅ $host is reachable."
else
        echo "❌ $host is NOT reachable."
fi