#include "Points.h"
#include "ListArrayIterator.h"
using CSC2110::ListArrayIterator;

Points::Points(Color* c, int r, double x_m, double y_m)
{
   color = c;
   radius = r;
   x_max_data = x_m;
   y_max_data = y_m;
   points = new ListArray<Point>();
}

Points::~Points()
{
   delete color;

   //this class will adopt and delete the individual points

   ListArrayIterator<Point>* iter = points->iterator();
   while(iter->hasNext())
   {
      Point* p = iter->next();
      delete p;
   }
   delete iter;
   delete points;
}

void Points::addPoint(Point* p)
{
   points->add(p);
}

void Points::draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height)
{
   ListArrayIterator<Point>* iter = points->iterator();
   while(iter->hasNext())
   {
      Point* p = iter->next();
      p->draw(cr, color, radius, width, height, x_max_data, y_max_data);  //needs the info for the transformation
   }
}

void Points::mouseClicked(int x, int y) {}
