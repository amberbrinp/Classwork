#if !defined CIRCULARLISTITERATOR_H
#define CIRCULARLISTITERATOR_H

#include "DoubleNode.h"

template < class T >
class CircularListIterator
{
   private:
      DoubleNode<T>* ref;
      DoubleNode<T>* head;
      bool first;

   public:
      CircularListIterator(DoubleNode<T>* head);
      virtual ~CircularListIterator();
      bool hasNext();
      T* next();

};

template < class T >
CircularListIterator<T>::CircularListIterator(DoubleNode<T>* h)
{
   ref = h; 
   head = h;
   first = 1;
}

template < class T >
CircularListIterator<T>::~CircularListIterator()
{
   ref = NULL;
   head = NULL;
}

template < class T >
bool CircularListIterator<T>::hasNext()
{
   if (ref == NULL)
   {
      return false;
   }

   if (first)
   {
      return true;
   }

   //need to detect a wrap around or iterator will have an infinite loop
   return ref != head;
}

template < class T >
T* CircularListIterator<T>::next()
{
   T* item = NULL;

   if (hasNext())  //should be used in conjunction with hasNext(), but just in case...
   {
      item = ref->getItem();
      ref = ref->getNext();
      first = false;
   }
   return item;
}

#endif
