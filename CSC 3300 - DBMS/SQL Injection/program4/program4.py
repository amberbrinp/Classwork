import sys
import MySQLdb

try:
    connection = MySQLdb.connect(host="localhost", user="root", passwd="coursework", db="TTU")

except MySQLdb.Error as e:
    print " error %d: %s" % (e.args[0], e.args[1])
    sys.exit(1)

cursor = connection.cursor()
cursor.execute("drop table if exists grades")
cursor.execute("create table grades(TNumber char(8), CourseID char(7), Semester char(1), Year numeric(4,0), Grade char(1), foreign key(TNumber) references students(TNumber)) ENGINE=INNODB")

cursor.execute("insert into grades(TNumber, CourseID, Semester, Year, Grade) values ('00003256', 'CSC4300', 'F', '2013', 'A'), ('00012345', 'MAT1910', 'F', '2011', 'B'), ('00012345', 'MAT1910', 'S', '2012', 'A'), ('00012345', 'CSC2110', 'S', '2010', 'D'), ('00012345', 'CSC2110', 'F', '2010', 'D'), ('00001423', 'BIO1010', 'S', '2014', 'D'), ('00015366', 'CSC2110', 'F', '2013', 'C'), ('00015366', 'CSC2110', 'S', '2014', 'C'), ('00015366', 'CSC2110', 'F', '2014', 'B'), ('00003256', 'CSC4100', 'S', '2012', 'A'), ('00003256', 'CSC2110', 'F', '2015', 'A')")
cursor.execute("commit")
cursor.execute("select * from grades")

while (1):
    row = cursor.fetchone()
    if row == None:
        break
    print "%s %s %s %s %s" % (row[0], row[1], row[2], row[3], row[4])
