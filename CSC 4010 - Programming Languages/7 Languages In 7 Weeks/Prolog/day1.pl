% Make a simple knowledge base. Represent some of your favorite books and authors.
% Find all books in your knowledge base written by one author.

% Make a knowledge base representing musicians and instruments.
% Also represent musicians and their genre of music.
% Find all musicians who play the guitar.

book(les_Miserables, victor_Hugo).
book(crime_and_Punishment, fyodor_Dostoyevsky).
book(harry_Potter, jk_Rowling).
book(the_Casual_Vacancy, jk_Rowling).
book(sherlock_Holmes, arthur_Conan_Doyle).
book(sevenLanguages_sevenWeeks, bruce_Tate).
book(introduction_Parallel_Programming, peter_Pacheco).
book(users_Guide_MPI, peter_Pacheco).

% | ?- book(What, peter_Pacheco).
% What = introduction_Parallel_Programming ? ;
% What = users_Guide_MPI

% -------------------------------------------------------------------------------------------------

musician(jimi_Hendrix, guitar).
musician(jimi_Hendrix, voice).
musician(jimi_Hendrix, teeth).
musician(patrick_Stump, guitar).
musician(patrick_Stump, voice).
musician(patrick_Stump, piano).
musician(nina_Simone, voice).
musician(nina_Simone, piano).

genre(jimi_Hendrix, rock).
genre(jimi_Hendrix, blues).
genre(jimi_Hendrix, blues_rock).
genre(patrick_Stump, pop_punk).
genre(nina_Simone, blues).
genre(nina_Simone, jazz).
genre(nina_Simone, gospel).

% | ?- musician(What, guitar).
% What = jimi_Hendrix
% What = patrick_Stump

