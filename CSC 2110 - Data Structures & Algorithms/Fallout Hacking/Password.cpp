#include "Password.h"
using CSC2110::Password;
#include "ListArrayIterator.h"
using CSC2110::ListArrayIterator;
#include "ListArray.h"
using CSC2110::ListArray;
#include "Text.h"
using CSC2110::String;
#include <iostream>
using namespace std;
Password::Password()//constructor
{
	len=0;
	viable_words=new ListArray<String>();
	all_words=new ListArray<String>();
}

Password::~Password()//destructor
{
	delete viable_words;
	delete all_words;//because the class is a pointer, the destructor deletes all of the items that it points to
}

int Password::getNumMatches(String* curr_word, String* word_guess)
{
	int matchcount=0;
	for (int i=0;i<curr_word->length();i++)//until you reach the end of the word
	{
		if (curr_word->charAt(i)==word_guess->charAt(i))//this is in the String class
			matchcount++;
	}
	return matchcount;
}

void Password::addWord(String* word)
{
	if (len==0)
		len=word->length();
	if (len==word->length())//not an else-if because you still have to add the first word!
	{
		viable_words->add(word);
		all_words->add(word);
	}
}

void Password::guess(int try_password, int num_matches)
{   
	//String* word_guess=all_words->get(try_password);
	ListArray<String>* newList;
	ListArrayIterator<String>* iter=viable_words->iterator();
	while(iter->hasNext())//keep looping until hasNext is false
	{
		String* Word=iter->next();
		if (getNumMatches(Word, getOriginalWord(try_password))==num_matches);//could use word_guess
		{//comparing the matches between the viable word and the guessed word with num_matches from fallout
			newList->add(Word);
		}
	}
	delete viable_words;
	viable_words=newList;//just make a new list and replace the old one with it
	delete iter;//this may not be necessary
}

int Password::getNumberOfPasswordsLeft()
{
	return viable_words->size();//num_viable_passwords
}

void Password::displayViableWords()
{
	for (int i=1;i<=viable_words->size();i++)//until you reach the end of the list
	{
		cout << viable_words->get(i) << endl;
	}
}

int Password::bestGuess()
{
   int best_guess_index = -1;
   int best_num_eliminated = -1;
   int num_viable_passwords = getNumberOfPasswordsLeft();

   //loop over ALL words, even if they have been eliminated as the password
   int count = 1;
   ListArrayIterator<String>* all_iter = all_words->iterator();
   while(all_iter->hasNext())
   {
      String* original_word = all_iter->next();

      //loop over only those words that could still be the password
      //count up the number of matches between a possible password and a word in the original list
      int* count_num_matches = new int[len + 1];

      for (int i = 0; i < len; i++) 
      {
         count_num_matches[i] = 0;
      }

      ListArrayIterator<String>* viable_iter = viable_words->iterator();
      while(viable_iter->hasNext())
      {
         String* viable_word = viable_iter->next();
         int num_matches = getNumMatches(viable_word, original_word);
         count_num_matches[num_matches]++;
      }
      delete viable_iter;

      //find the largest number in the count_num_matches array
      //the largest number indicates the guess that will generate the most eliminations
      int most_num_matches = 0;
      for (int j = 0; j < len; j++) 
      {
         int curr_num_matches = count_num_matches[j];
         if (curr_num_matches > most_num_matches)
         {
            most_num_matches = curr_num_matches;
         }
      }

      //compute the fewest that can possibly be eliminated by guessing the current word (original list)
      int num_eliminated = num_viable_passwords - most_num_matches;

      //select the word to guess that maximizes the minimum number of eliminations (minimax)
      if (num_eliminated > best_num_eliminated)
      {
         best_num_eliminated = num_eliminated;
         best_guess_index = count;
      }
      
      count++;
      delete[] count_num_matches;
   }

   delete all_iter;
   return best_guess_index;  //return a 1-based index into the all_words list of words (careful)
}

String* Password::getOriginalWord(int index)
{
	return all_words->get(index);
}