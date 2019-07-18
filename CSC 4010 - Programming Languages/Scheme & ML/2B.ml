let rec unique (someList : string list) =
    if List.length someList < 2 then someList (* base case *)
    else if List.hd someList = List.nth someList 1 then unique(List.tl someList) (* compare first two elements *)
    else List.hd someList :: unique(List.tl someList);; (* concatenate unique results *)

unique ["c";"c";"b";"b";"a"];;
unique ["c";"b";"c";"b";"a"];;
