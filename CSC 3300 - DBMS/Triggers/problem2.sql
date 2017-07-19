execute procedure drop_if_exists('trigger', 'tr_department_rating_insert');
delete from department_tester;
commit work;

set term # ; 

create trigger tr_department_rating_insert for department_tester after insert  
as 
 DECLARE new_rating varchar(5) DEFAULT 'LOW';
begin 
 select rating from f_rating(NEW.budget) into :new_rating;
 update department_rating
 set occurrences = occurrences + 1
 where rating = :new_rating;
end # 

set term ; #
commit work;

insert into department_tester(dept_no, department, head_dept, mngr_no, budget, location, phone_no) select * from department;

commit work;

select * from department_rating;
