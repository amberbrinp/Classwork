#if !defined (PLOT_H)
#define PLOT_H

#include "Points.h"
#include "Drawable.h"
#include "ListArray.h"
using CSC2110::ListArray;
#include <gtkmm.h>

class Plot : public Drawable
{
   private:
      ListArray<Points>* data;

   public:
      Plot();
      virtual ~Plot();

      void addPoints(Points* points);
      virtual void draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height);
      virtual void mouseClicked(int x, int y);
};

#endif
