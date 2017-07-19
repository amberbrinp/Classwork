#!/bin/bash

wget $1/$2

7z x -so SwissProt.xml.7z| xqilla -i /dev/stdin q.xq | sort -k 2 | split -l 10000


