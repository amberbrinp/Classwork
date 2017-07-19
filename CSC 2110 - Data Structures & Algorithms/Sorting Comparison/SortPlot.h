#if !defined SORTPLOT_H
#define SORTPLOT_H

#include "Plot.h"
#include "DrawPanel.h"
#include "CD.h"
using CSC2110::CD;
#include "Color.h"

class SortPlot : public DrawPanel
{
   private:
      Plot* plot;
      CD** cds;
      CD** sorted_cds;
      int n_max;
      int increment;

      CD** randomCDs(int numToCreate, int numCharacters);

   public:
      SortPlot(int w, int h, int n, int incr, Plot* plot);
      virtual ~SortPlot();

      void sort(int n_start, int n_end, int incr, int sortMethod, Color* color, int radius);
      void sort(CD** items, int numToSort, int sortMethod);
      virtual void on_sort_button_click_event();
      void startSortPlotThread();
      void sortPlot();
};

#endif
