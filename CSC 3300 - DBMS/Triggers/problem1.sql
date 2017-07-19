execute procedure drop_if_exists('table','department_rating');
execute procedure drop_if_exists('procedure', 'f_rating');
execute procedure drop_if_exists('trigger', 'tr_department_rating_insert');
execute procedure drop_if_exists('trigger', 'tr_department_rating_delete');
execute procedure drop_if_exists('trigger', 'tr_department_rating_update');
commit work;

CREATE TABLE department_rating
(
rating varchar(5) primary key,
occurrences int);

commit work;

insert into department_rating(rating, occurrences) values ('ULTRA',0);
insert into department_rating(rating, occurrences) values ('HIGH', 0);
insert into department_rating(rating, occurrences) values ('MID', 0);
insert into department_rating(rating, occurrences) values ('LOW',0);

set term # ;
create procedure f_rating(budget int)
returns(rating varchar(5)) as
-- don't need to declare anything here
begin
 if (budget <= 500000) then
	rating = 'LOW';
 else if (budget <= 850000) then
	rating = 'MID';
 else if (budget <= 1200000) then
	rating = 'HIGH';
 else
	rating = 'ULTRA';
 suspend;
end #
set term ; #

commit work;

select * from department_rating;

select rating from f_rating(200000);
select rating from f_rating(850000);
select rating from f_rating(980000);
select rating from f_rating(1200001);


