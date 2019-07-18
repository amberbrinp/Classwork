(* Trace of the code available in PLP HW 5.pdf *)
(define unique
	(lambda (L)
	(cond
		((null? L) L)
		((null? (cdr L)) L)
		((eqv? (car L) (car (cdr L))) (unique (cdr L)))
		(else (cons
			    (car L)
			    (unique (cdr L)))
		      )
	))
)

(display (unique '(c c c b b a)))
(newline)
(display (unique '(c b c b c a)))
