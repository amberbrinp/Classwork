<?php

    $connection = mysql_connect('localhost', 'root', 'coursework') or die('unable to connect: ' . mysql_error());
    echo 'connected';
    mysql_select_db('TTU') or die('unable to select database');
    $query = 'select distinct TNumber as TNum2, CourseID as CID2, (select count(CourseID) from grades where TNumber = TNum2 and CourseID = CID2) as Attempts, ' . '(select min(grade) from grades where TNumber = TNum2 and CourseID = CID2) as HighestGrade' . ' from grades order by TNumber';
    $result = mysql_query($query) or die('Query failed: ' . mysql_error());

    echo "<table border=\"1\">\n";
    $f = 0;
while ($line = mysql_fetch_array($result, MYSQL_ASSOC))
    {
    echo "\t<tr>\n";
     foreach ($line as $col)
        {
            $f++;
            if ($f == 9 || $f == 13 || $f == 21):
                echo "\t\t<td></td>\n";
            else:
                echo "\t\t<td>$col</td>\n";
            endif;
        }
        echo "\t</tr>\n";
    }
    echo "</table>\n";
    mysql_free_result($result);
    mysql_close($connection);
?>
