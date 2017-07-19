#if !defined (LISTDOUBLYLINKEDITERATOR_H)
#define LISTDOUBLYLINKEDITERATOR_H

#include "DoubleNode.h"

template < class T >
class ListDoublyLinkedIterator
{

   private:
      DoubleNode<T>* ref;

   public:
      ListDoublyLinkedIterator(DoubleNode<T>* head);
      ~ListDoublyLinkedIterator();
      bool hasNext();
      T* next();

};

template < class T >
ListDoublyLinkedIterator<T>::ListDoublyLinkedIterator(DoubleNode<T>* first)
{
   ref = first;
}

template < class T >
ListDoublyLinkedIterator<T>::~ListDoublyLinkedIterator()
{
   ref = NULL;
}

template < class T >
bool ListDoublyLinkedIterator<T>::hasNext()
{
   return ref != NULL;
}

template < class T >
T* ListDoublyLinkedIterator<T>::next()
{
   T* item = NULL;

   if (hasNext())
   {
      item = ref->getItem();
      ref = ref->getNext();
   }

   return item;
}

#endif
