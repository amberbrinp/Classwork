execute procedure drop_if_exists('trigger', 'tr_department_rating_update');
commit work;

set term # ; 

create trigger tr_department_rating_update for department_tester after update  
as 
 DECLARE old_rating varchar(5) DEFAULT 'LOW';
 DECLARE new_rating varchar(5) DEFAULT 'LOW';
begin 
 select rating from f_rating(OLD.budget) into :old_rating;
 select rating from f_rating(NEW.budget) into :new_rating;

 update department_rating
 set occurrences = occurrences - 1
 where rating = :old_rating;

 update department_rating
 set occurrences = occurrences + 1
 where rating = :new_rating;
end # 

set term ; #
commit work;

update department_tester
set budget = budget + 160000
where dept_no = 120;

commit work;

select * from department_rating;
