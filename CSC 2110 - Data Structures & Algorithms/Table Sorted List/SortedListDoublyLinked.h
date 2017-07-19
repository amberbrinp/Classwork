#if !defined (SLDL_H)
#define SLDL_H

#include "DoubleNode.h"
#include "ListDoublyLinkedIterator.h"
#include "Text.h"
using CSC2110::String;

template < class T >
class SortedListDoublyLinked
{

   private:
      DoubleNode<T>* loc;
      int sze;

      DoubleNode<T>* findLocationAdd(T* item);
      DoubleNode<T>* findLocationRemove(String* sk);
      DoubleNode<T>* locateNodeRemove(String* sk);
      DoubleNode<T>* locateNodeAdd(T* item);

      DoubleNode<T>* addDN(T* item);
      T* remove(DoubleNode<T>* curr);

      DoubleNode<T>* findHead();
      DoubleNode<T>* findTail();

      int (*compare_items) (T* item_1, T* item_2);
      int (*compare_keys) (String* key, T* item);

   public:
      SortedListDoublyLinked(int (*comp_items) (T* item_1, T* item_2), int (*comp_keys) (String* key, T* item));
      ~SortedListDoublyLinked();

      bool isEmpty();
      int size();
      void removeAll();

      T* get(String* sk);
      void add(T* item);
      void remove(String* sk);

      ListDoublyLinkedIterator<T>* iterator();

};

template < class T >
SortedListDoublyLinked<T>::SortedListDoublyLinked(int (*comp_items) (T* item_1, T* item_2), int (*comp_keys) (String* key, T* item)) 
{
   sze = 0;
   loc = NULL;

   compare_items = comp_items;
   compare_keys = comp_keys;
} 

template < class T >
SortedListDoublyLinked<T>::~SortedListDoublyLinked()
{
   removeAll();
   sze = 0;
}

template < class T >
bool SortedListDoublyLinked<T>::isEmpty() 
{
   return sze == 0;
} 

template < class T >
int SortedListDoublyLinked<T>::size() 
{
   return sze;
}

template < class T >
T* SortedListDoublyLinked<T>::get(String* sk)
{
   DoubleNode<T>* curr = NULL;
   curr = locateNodeRemove(sk);
   if (curr == NULL) return NULL;

   T* item = curr->getItem();
   return item;
}

//to handle FIFO for duplicates, need a find for add and a find for remove
template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::findLocationAdd(T* item)
{
   DoubleNode<T>* curr = loc;
   int comp = (*compare_items) (item, curr->getItem());

   //use next links to find the interior location
   if (comp >= 0)   // >= FIFO for duplicates
   {
      while (curr->getNext() != NULL && comp >= 0) 
      {
         curr = curr->getNext();
         comp = (*compare_items) (item, curr->getItem());
      }
   }
   //use prev links to find the interior location
   else if (comp < 0) 
   {
      while (curr->getPrev() != NULL && comp < 0)  
      {
         curr = curr->getPrev();
         comp = (*compare_items) (item, curr->getItem());
      }
   }

   return curr;
}

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::findLocationRemove(String* sk)
{
   DoubleNode<T>* curr = loc;
   int comp = (*compare_keys) (sk, curr->getItem());

   //use next links to find the interior location
   if (comp > 0)  
   {
      while (curr->getNext() != NULL && comp > 0) 
      {
         curr = curr->getNext();
         comp = (*compare_keys) (sk, curr->getItem());
      }
   }
   //use prev links to find the interior location
   else if (comp < 0) 
   {
      while (curr->getPrev() != NULL && comp < 0)  
      {
         curr = curr->getPrev();
         comp = (*compare_keys) (sk, curr->getItem());
      }
   }

   return curr;
}

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::locateNodeRemove(String* sk)
{
   //size = 0 special case
   if (sze == 0)
   {
      return NULL;
   }

   DoubleNode<T>* curr = findLocationRemove(sk);  
   int comp = (*compare_keys) (sk, curr->getItem());

   if (comp != 0)  //removing and didn't find a match
   { 
      return NULL; 
   }
 
   //return a direct reference to the node to be removed
   return curr; 
}

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::locateNodeAdd(T* item) 
{
   //size = 0 special case
   if (sze == 0)
   {
      return NULL;  //insert into an empty list
   }

   DoubleNode<T>* curr = findLocationAdd(item);
   int comp = (*compare_items) (item, curr->getItem());

   //need to move to the node before the insertion location (if the insert is not before the head)
   //that is, if next refs were used, back up one
   if (curr->getPrev() != NULL && comp < 0)  
   {
      curr = curr->getPrev();  
   }

   return curr; 
}

template < class T >
void SortedListDoublyLinked<T>::add(T* item)
{
   addDN(item);
}

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::addDN(T* item)
{
   //find the location right before where the new node should be inserted
   DoubleNode<T>* prev = locateNodeAdd(item);
   DoubleNode<T>* node = new DoubleNode<T>(item);

   //inserting into an empty list (loc is NULL)
   if (prev == NULL) 
   {
      node->setNext(loc);
   } 

   //inserting at the top of a non-empty list
   //this is a special check as insertions at the head and right after the head are not distinguished by locateNodeAdd
   else if (prev->getPrev() == NULL && (*compare_items) (item, prev->getItem()) < 0)  
   {
      prev->setPrev(node);
      node->setNext(prev);
   }

   //general case (the node right before the insertion location returned by locateNode for add)
   else 
   {
      DoubleNode<T>* curr = prev->getNext();  //could be NULL (can't set a prev link)
      node->setNext(curr);
      node->setPrev(prev);

      prev->setNext(node);
      if (curr != NULL)  //check for end of list
      {
         curr->setPrev(node);
      }
   } 

   //reposition loc for locality of reference
   loc = node;
   sze++;
   return node;
}

template < class T >
void SortedListDoublyLinked<T>::remove(String* sk) 
{
   //identify the node to be removed
   DoubleNode<T>* curr = locateNodeRemove(sk);
   if (curr == NULL) return;

   remove(curr);
}

template < class T >
T* SortedListDoublyLinked<T>::remove(DoubleNode<T>* curr)
{
   T* item;

   DoubleNode<T>* prev;
   DoubleNode<T>* after;

   prev = curr->getPrev();
   after = curr->getNext();
   item = curr->getItem();
   loc = after;  

   if (prev != NULL)
   {
      prev->setNext(after);
      loc = prev;  //move loc here if not removing the last item (locality of reference)
   }

   if (after != NULL)
   {
      after->setPrev(prev);
   }

   sze--;
   delete curr;
   return item;
}  

template < class T >
void SortedListDoublyLinked<T>::removeAll() 
{
   DoubleNode<T>* temp;

   if (loc != NULL)
   {
      loc = findHead();
      temp = loc;
      loc = loc->getNext();
      delete temp;

      while (loc != NULL)
      {
         temp = loc;
         loc = loc->getNext();
         delete temp;
      }

      loc = NULL;
      sze = 0;
   }
} 

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::findHead()
{
   DoubleNode<T>* find = loc;  //entry point into the list

   if (find == NULL)
   {
      return find;
   }

   while (find->getPrev() != NULL)
   {
      find = find->getPrev();
   }

   return find;
}

template < class T >
DoubleNode<T>* SortedListDoublyLinked<T>::findTail()
{
   DoubleNode<T>* find = loc;  //entry point into the list

   if (find == NULL)
   {
      return find;
   }

   while (find->getNext() != NULL)
   {
      find = find->getNext();
   }

   return find;
}

template < class T >
ListDoublyLinkedIterator<T>* SortedListDoublyLinked<T>::iterator()
{
   //DO THIS
   //locate the head/first node for the iterator without moving the interior node (loc)
	DoubleNode<T>* head = findHead();
	ListDoublyLinkedIterator<T>* iter = new ListDoublyLinkedIterator<T>(head);
	return iter;

} 

#endif
