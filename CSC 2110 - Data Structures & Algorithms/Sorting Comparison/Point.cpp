#include "Point.h"
#include "Circle.h"
#include "Text.h"
using CSC2110::String;

#include <iostream>
using namespace std;

Point::Point(double xp, double yp)
{
   x = xp;
   y = yp;
}

Point::~Point()
{}

double Point::getX()
{
   return x;
}

double Point::getY()
{
   return y;
}

void Point::draw(Cairo::RefPtr<Cairo::Context> cr, Color* color, int radius, int width, int height, double x_max, double y_max)
{
   double w = (double) width;
   double h = (double) height;

   //compute the transformation
   double x_t = w*x/x_max;
   double y_t = (-h*y/y_max) + h;
   int x_t_r = (int) (x_t + 0.5);
   int y_t_r = (int) (y_t + 0.5);

   //draw the point on the provided canvas
   Color* copy_color = new Color(color->getRed(), color->getGreen(), color->getBlue());
   String* circle_text = new String("");
   Circle* c = new Circle(copy_color, radius, circle_text);

   c->draw(cr, x_t_r, y_t_r);
   delete circle_text;
   delete c;
}
