#include "SortPlot.h"
#include "Sort.h"
#include "Random.h"
using CSC2110::Random;
#include "HighPerformanceCounter.h"
using CSC2110::HighPerformanceCounter;
#include <gtkmm.h>

#include <iostream>
using namespace std;

SortPlot::SortPlot(int w, int h, int n, int incr, Plot* p) : DrawPanel(w, h, p)
{
   n_max = n;
   increment = incr;
   plot = p;

   cds = randomCDs(n_max, 5);  
   sorted_cds = Sort<CD>::mergeSort(cds, n_max, &CD::compare_items);
}

SortPlot::~SortPlot()
{
   for (int i = 0; i < n_max; i++)
   {
      CD* cd = cds[i];
      delete cd;
   }
   delete[] cds;
   delete[] sorted_cds;
   //delete plot;  //deleted in DrawPanel
}

void SortPlot::on_sort_button_click_event()
{
   //start, end, increment, sort method, color for points, radius
   //1 = mergesort (red), 2 = quicksort (blue)
   sort(1, n_max, increment, 1, new Color(1, 0, 0), 5); //merge
   //sort(1, (int) (n_max*1.0/2.0), increment, 2, new Color(0, 0, 1), 5); //quick
   sort(1, (int) (n_max*1.0/2.0), increment, 3, new Color(0, 1, 0), 5); //selection
   sort(1, n_max, increment, 4, new Color(1, 0, 1), 5); //insertion binary
}

void SortPlot::sort(int n_start, int n_end, int incr, int sortMethod, Color* color, int radius)
{
   HighPerformanceCounter* hpc = HighPerformanceCounter::getHighPerformanceCounter();
   Points* points = new Points(color, radius, n_max, 200000);  //last parameter is microseconds for max y
   plot->addPoints(points);

   for (int i = n_start; i <= n_end; i += incr)
   {
      int numToSort = i;

      int start_time = hpc->getCurrentTimeInTicks();
      sort(cds, numToSort, sortMethod);
      int end_time = hpc->getCurrentTimeInTicks();

      double micro = hpc->getTimeDifferenceInMicroSeconds(start_time, end_time);
      Point* p = new Point(numToSort, (int) micro);
      points->addPoint(p);

      render();
   }
}

void SortPlot::sort(CD** cds, int num_to_sort, int sort_method)
{
   CD** sorted;
   if (sort_method == 1)
   {
      sorted = Sort<CD>::mergeSort(cds, num_to_sort, &CD::compare_items);
   }
   else if (sort_method == 2)
   {
      //note use of sorted_cds, the worst case for quick sort
      sorted = Sort<CD>::quickSort(sorted_cds, num_to_sort, &CD::compare_items);
   }
   else if (sort_method == 3)
   {
      sorted = Sort<CD>::selectionSort(cds, num_to_sort, &CD::compare_items);
   }
   else if (sort_method == 4)
   {
      sorted = Sort<CD>::insertionBinarySort(cds, num_to_sort, &CD::compare_items);
   }
   else if (sort_method == 5)
   {
      //sorted = Sort<CD>::heapSort(cds, num_to_sort, &CD::compare_items);
   }

   delete[] sorted;
}

CD** SortPlot::randomCDs(int num_to_create, int num_characters)
{
   Random* rand = Random::getRandom();
   CD** cds = new CD*[num_to_create];

   for (int i = 0; i < num_to_create; i++)
   {
      char* title_char = new char[num_characters + 1];
      for (int j = 0; j < num_characters; j++)
      {
         char the_char;

         int temp = rand->getRandomInt(0, 35);

         if (temp < 10)  //digit
         {
            the_char = (char) (temp + 48);
         }
         else  //lower case character
         {
            the_char = (char) (temp + 87);
         }
         title_char[j] = the_char;
      }

      title_char[num_characters] = 0;  //need a null terminator
      String* title = new String(title_char);
      delete[] title_char;
      String* artist = new String("ABCDE");
      cds[i] = new CD(artist, title, 2004, 9, 9);
   }
   return cds;
}

int main(int argc, char** argv)
{
   Gtk::Main kit(argc, argv);

   Gtk::Window win;
   win.set_title("Sort!");
   win.set_position(Gtk::WIN_POS_CENTER);

   //the size of the window
   int width = 640;
   int height = 520;

   win.set_size_request(width, height);
   win.set_resizable(false);  

   Gtk::Table tbl(10, 1, true);
   int rows = tbl.property_n_rows();
   int button_height = (int) (((double) height)/rows + 0.5);

   Plot* plot = new Plot();
   SortPlot sp(width, height - button_height, 8000, 100, plot);  //needs to know its own dimensions

   Gtk::Button btnSort("Sort!");

   btnSort.signal_clicked().connect(sigc::mem_fun(sp, &SortPlot::on_sort_button_click_event));

   tbl.attach(sp, 0, 1, 0, 9, Gtk::FILL | Gtk::EXPAND, Gtk::FILL | Gtk::EXPAND, 0, 0);
   tbl.attach(btnSort, 0, 1, 9, 10, Gtk::FILL | Gtk::EXPAND, Gtk::FILL | Gtk::EXPAND, 0, 0);
   win.add(tbl);

   win.show_all_children();
   Gtk::Main::run(win);

   return 0;
}
