local status, err = pcall(function ()
    env = assert (require"luasql.mysql".mysql(), "unable to load library")

    con = assert (env:connect("TTU", "root", "coursework"), "unable to connect to the database")

    cur = assert (con:execute"select distinct FirstName, LastName, if(Grade in ('A', 'B', 'C'), 'YES', 'NO') as Answer from students as S, grades as G where CourseID = 'CSC2110' and S.TNumber = G.TNumber")
    end)
if not status then
    print("Error: " .. err)
    os.exit()
end

row = cur:fetch ({}, "a")
while row do
    print(string.format(" %-20s %-20s %-3s ", row.FirstName, row.LastName, row.Answer))
    row = cur:fetch (row, "a")
end

cur:close()
con:close()
env:close()
