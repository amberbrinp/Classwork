let rec log2 (n: int) =
    if n = 1 then 1 (* base case *)
    else (1 + log2 (n / 2));; (* integer division *)


for i = 1 to 100 do
    log2 i
done;;
