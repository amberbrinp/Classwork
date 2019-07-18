let rec min someList =
    if List.length someList = 1 then List.hd someList (* base case *)
    else (* min computation *)
        let a = List.hd someList in
        let b = min (List.tl someList) in
            if b < a then b
            else a;;

min [1;2;3];;
min ["a";"b";"c"];;
