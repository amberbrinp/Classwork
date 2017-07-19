#include "Plot.h"
#include "ListArrayIterator.h"
using CSC2110::ListArrayIterator;

Plot::Plot()
{
   data = new ListArray<Points>();
} 

Plot::~Plot()
{
   ListArrayIterator<Points>* iter = data->iterator();
   while(iter->hasNext())
   {
      Points* pts = iter->next();
      delete pts;
   }
   delete iter;
   delete data;
}

void Plot::addPoints(Points* points)
{
   data->add(points);
}

void Plot::draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height)
{
   ListArrayIterator<Points>* iter = data->iterator();
   while(iter->hasNext())
   {
      Points* pts = iter->next();
      pts->draw(cr, width, height);
   }
   delete iter;
}

void Plot::mouseClicked(int x, int y) {}
