#!/bin/sh

echo "You're running `uname -s` version `uname -r`"
for x in ash bash bsh csh ksh pdksh sh tcsh zsh; do
test -x /bin/$x && shells="$shells $x"
done
echo "You have the following shells installed:$shells"
