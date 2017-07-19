execute procedure drop_if_exists('trigger', 'tr_department_rating_delete');
commit work;

set term # ; 

create trigger tr_department_rating_delete for department_tester after delete  
as 
 DECLARE old_rating varchar(5) DEFAULT 'LOW';
begin 
 select rating from f_rating(OLD.budget) into :old_rating;
 update department_rating
 set occurrences = occurrences - 1
 where rating = :old_rating;
end # 

set term ; #
commit work;

delete from department_tester where dept_no = 115 or dept_no = 672;
commit work;

select * from department_rating;

