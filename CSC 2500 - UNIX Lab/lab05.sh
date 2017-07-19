#!/bin/bash
HI='\033[0;32m'
NORMAL='\033[0m'
echo -e "${HI}1. starts or ends with Jose$NORMAL"
grep -E '^Jose' phonebook.dat
grep -E 'Jose$' phonebook.dat

echo -e "${HI}2. start with at least twenty-seven upper or lower case characters a - z.$NORMAL"
grep -E '^[a-z]+{27,}' phonebook.dat


echo -e "${HI}3.consists of more than 18 characters. The character can be anything, including alphabetic and numeric.$NORMAL"
grep -E '.{19,}' phonebook.dat


echo -e "${HI}4. contains exactly 18 characters. the character can be anything, including alphabetic and numeric.$NORMAL"
grep -E '^.{18}$' phonebook.dat


echo -e "${HI}5. contains a sequence of between 6 and 8 alphabetic characters, i.e. upper or lower case a through z. The sequence must be separated from the rest of the line by a space or tab character (on each side)$NORMAL"
grep -E '[ \t]\w{6,8}[ \t]' phonebook.dat

echo -e "${HI}6. contains a local phone number: 3 digits, dash 4 digits$NORMAL"
grep -E '[^-][0-9]{3}[-][0-9]{4}' phonebook.dat

echo -e "${HI}7. contains a valid URL on a line by itself$NORMAL"
grep -E '^(http|HTTP)://[[:alnum:]]+\.[[:alnum:]]+\.(com|edu)$' phonebook.dat
