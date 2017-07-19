#!/bin/bash

echo "What is your name?"
read name

echo "Are you a (S)tudent or a (P)rofessor?"
read role
if [ $role = "P" -o $role = "p" ]; then
student_or_professor="professor";
worksat_or_attends="work";
research_or_academic="research";
verb="works";
else
student_or_professor="student";
worksat_or_attends="attend";
research_or_academic="academic";
verb="attends";
fi

echo "Where do you $worksat_or_attends?"
read work

echo "What are your top 3 research interests?"
read interest1
read interest2
read interest3

echo "$name is a $student_or_professor that $verb at $work" > ~/.plan;
echo "$name's $research_or_academic interests include:" >> ~/.plan;
echo $interest1 >> ~/.plan
echo $interest2 >> ~/.plan
echo $interest3 >> ~/.plan

echo "Running finger on your account..."
finger $(whoami)
