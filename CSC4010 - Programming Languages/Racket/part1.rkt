;;;worked on this with Jorge Ortega
#lang scheme
(define (sublist sub full)
   (let iterate ((cur sub) (full full))
        (cond 
          ((null? cur ) #t)
          ((null? full) #f)
          ((and
            (equal? (car cur) (car full))
            (iterate (cdr cur) (cdr full))
          ))
          (else (iterate sub (cdr full)))
        )
   )
)

(define (lgrep sub list)
  (cond
    ((null? list) '())
    ((sublist sub (car list))
     (cons(car list) (lgrep sub (cdr list)))
    )
    (else (lgrep sub (cdr list)))
  )
)
      

(define list1 '((a b c d e f g)
                (c d c d e)
                (a b c d)
                (h i c d e k)
                (x y z)))
      

(lgrep '(c d e) list1)
