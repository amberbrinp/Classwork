#if !defined CIRCULARLIST_H
#define CIRCULARLIST_H
#include<cmath>
#include "CircularListIterator.h"

#include "Drawable.h"
#include "Line.h"
#include "Circle.h"
#include "Update.h"
#include "Text.h"
using CSC2110::String;

template < class T >
class CircularList : public Drawable
{

   private:
      DoubleNode<T>* loc;
      int loc_pos;
      int sze;
      DoubleNode<T>* find(int index);
      void animateMovement(bool clockwise, DoubleNode<T>* where);

      bool update_active;
      Update* gui;

   public:
      CircularList();
      virtual ~CircularList();
      bool isEmpty();
      int size();
      void removeAll();
      T* get(int index);
      void add(int index, T* item);
      void add(T* item);
      void remove(int index);
      void set(int index, T* item);
      CircularListIterator<T>* iterator();

      void addListener(Update* gui);
      void draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height);
      void mouseClicked(int x, int y);

};

template < class T >
DoubleNode<T>* CircularList<T>::find(int index) 
{
   DoubleNode<T>* where = loc;
   int min_dist;
   int dist_prev;  //prev links (negative)
   int dist_next;  //next links (positive)

   if (sze == 0)
   {
      return NULL;
   }
   
   //complete the distance calculations below
   //loc_pos is the index that loc currently points to
   //index is the requested index
 
   if (index >= loc_pos)
   {
       dist_next=index-loc_pos;   //distance without the bridge (next refs, positive)
       dist_prev=loc_pos-index+sze;   //distance using the bridge (prev refs, negative)
   }
   else
   {
         //use abs
        dist_prev=index-loc_pos;  //distance without the bridge (prev refs, negative)
        dist_next=sze-loc_pos+index;  //distance using the bridge (next refs, positive)                              
   }

   if (abs(dist_next)>=abs(dist_prev))//find the minimum distance using absolute value
	   min_dist=dist_prev;
   else//if dist_next is smaller
   {
	   min_dist=dist_next;
   }//set min_dist to the smaller value, keeping the sign




   if (min_dist < 0)  //negative distance means use prev links, counterclockwise
   {
      for (int i = 1; i <= -1*min_dist; i++)
      {
         where = where->getPrev();
         animateMovement(false, where);
      }
   }
   else  //positive distance means use next links, clockwise
   {
      for (int i = 1; i <= min_dist; i++)
      {
         where = where->getNext();
         animateMovement(true, where);
      }
   }

   return where;
}

template < class T >
void CircularList<T>::animateMovement(bool clockwise, DoubleNode<T>* where)
{
   if (update_active)  //animate the movement of loc around the circular list
   {
      loc = where;
      if (!clockwise)
      {
         //moving counterclockwise
         loc_pos--;
         if (loc_pos == 0) 
         {
            loc_pos = sze;
         }
      }

      else
      {
         //moving clockwise
         loc_pos++;
         if (loc_pos == sze + 1) 
         {
            loc_pos = 1;
         }
      }

      gui->update();
   }
}

template < class T >
void CircularList<T>::remove(int index) 
{
   if (index >= 1 && index <= sze) 
   {//remember to move loc and loc_pos to the location of the removal
    //remember to delete the node after it has been removed from the list
      if (sze == 1) //special case
      {
		delete loc;
		loc=0;
		loc_pos=0;
      }
      else
      {
         //use local variables
		DoubleNode<T>* curr=find(index);
		DoubleNode<T>* previous=curr->getPrev();
		DoubleNode<T>* next=curr->getNext();
		loc=next;
		loc_pos=index;
		previous->setNext(next);
		next->setPrev(previous);
		if(index==sze)
		{
			loc_pos=1;
		}
		else
		{
			loc_pos=index;
		}
		delete curr;
      }
      sze--;
   } 
}

template < class T >
T* CircularList<T>::get(int index)
{
   T* item = NULL;
   if (index >= 1 && index <= sze) 
   {
      // get reference to node, then data in node
      DoubleNode<T>* where = find(index);
      T* item = where->getItem();
      loc = where;  //stay at location of most recent activity
      loc_pos = index;
      return item;
   } 
}

template < class T >
void CircularList<T>::add(T* item)
{
   add(size() + 1, item);
}

template < class T >
void CircularList<T>::add(int index, T* item)
{
   if (index >= 1 && index <= sze + 1) 
   {
      if (sze == 0) 
      {
         DoubleNode<T>* node = new DoubleNode<T>(item);
         node->setNext(node);
         node->setPrev(node);
         loc = node;
         loc_pos = 1;
      } 
      else 
      {
         DoubleNode<T>* prev;

         if (index > 1)
         {
            prev = find(index - 1);
         }
         else
         {
            prev = find(1)->getPrev();
         }

         DoubleNode<T>* curr = prev->getNext();
         // insert the new node containing item after 
         // the node that prev references
         DoubleNode<T>* node = new DoubleNode<T>(item);
         node->setPrev(prev);
         node->setNext(curr);
         prev->setNext(node);
         curr->setPrev(node);

         loc = node;
         loc_pos = index;
      } 

      sze++;
   } 
}

template < class T >
CircularList<T>::CircularList() 
{
   sze = 0;
   removeAll();

   update_active = 0;
   gui = NULL;
}

template < class T >
void CircularList<T>::removeAll() 
{
   if (sze > 0)
   {
      DoubleNode<T>* curr = find(1);
      curr->getPrev()->setNext(NULL);  //break circular reference first

      // need to remove all previous links to prevent memory leak
      while (curr != NULL)
      {
         curr->setPrev(NULL);
         DoubleNode<T>* next = curr->getNext();
         delete curr;
         curr = next;
      }
   }

   loc = NULL;
   loc_pos = 0;
   sze = 0;
} 

template < class T >
CircularList<T>::~CircularList() 
{
   removeAll();
} 

template < class T >
bool CircularList<T>::isEmpty() 
{
   return sze == 0;
} 

template < class T >
int CircularList<T>::size() 
{
   return sze;
}

template < class T >
void CircularList<T>::set(int index, T* item)
{
   if (index >= 1 && index <= sze) 
   {
      loc = find(index);
      loc->setItem(item);
      loc_pos = index;
   }    
}

template < class T >
CircularListIterator<T>* CircularList<T>::iterator()
{
   return new CircularListIterator<T>(find(1));
}

template < class T >
void CircularList<T>::addListener(Update* g)
{
   gui = g;
   update_active = 1;
}

template < class T >
void CircularList<T>::draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height)
{
   update_active = 0;  //turn off painting inside the paint method

   double sze = (double) this->sze;
   //included so that the small circles will stay entirely on the panel (they have a radius too)
   int offset = 10;  

   double angleIncr = 360.0/sze;  
   //the circle will start being drawn at the top of the panel
   double angle = -90;  
   double r1 = (double) (width - 2*offset)/2; 
   double r2 = (double) (height - 2*offset)/2;
   double r;
   //find the constraining dimension and use that for the radius
   if (r1 < r2)  
   {
      r = r1;
   }
   else
   {
      r = r2;
   }

   //starting positions-- start at the top of the panel, in the center (y increases downward)
   double x = r + offset;
   double y = offset;

   CircularListIterator<T>* iter = iterator();

   T* before = NULL;
   T* item = NULL;
   int xd = -1, yd = -1;
   int xbefore = -1, ybefore = -1;

   //draw the lines
   Line* line = new Line(new Color(0, 0, 0), 5);
   while(iter->hasNext())
   {
       before = item;  //connect current node to the previously drawn node (if not the first node)
       item = iter->next();
         
       xbefore = xd;
       ybefore = yd;
       xd = (int) x;
       yd = (int) y;

       //connect the current node to the previously drawn node
       if (before != NULL)
       {
          line->draw(cr, xbefore, ybefore, xd, yd);
       }

       angle = angle + angleIncr;

       //update x and y
       x = r*cos(angle*3.1415927/180) + (r + offset);  //move x and y to the center of the circle and then compute the distance to move from there (based on r and theta)
       y = r*sin(angle*3.1415927/180) + (r + offset);
   }
   delete iter;

   //connect last node to the first node
   if (before != NULL)
   {
      line->draw(cr, xd, yd, (int) (r + offset), (int) offset);
   }
   delete line;

   iter = iterator();

   //draw the circles
   int radius = 20;  //the radius of the circles representing links
   int count = 0;  //the node that loc points to (located at index loc_pos) needs to be painted blue rather than red
   while(iter->hasNext())
   {
       item = iter->next();

       count++;
       CSC2110::String* text = item->getKey();  
       Circle* circle;
       if (count == loc_pos)
       {
          circle = new Circle(new Color(0, 1, 0), radius, text);
       }
       else
       {
          circle = new Circle(new Color(1, 0, 0), radius, text);
       }
         
       //draw the small circles
       xd = (int) x;
       yd = (int) y;

       circle->draw(cr, xd, yd);
       delete circle;

       angle = angle + angleIncr;

       //update x and y
       x = r*cos(angle*3.1415927/180) + r + offset;
       y = r*sin(angle*3.1415927/180) + r + offset;
   }
   delete iter;

   update_active = 1;
}

template < class T >
void CircularList<T>::mouseClicked(int x, int y) {}

#endif
