#include <gtkmm/main.h>
#include <gtkmm/window.h>

#include "CLGUI.h"
#include "CircularList.h"
#include "ListArray.h"
#include "Random.h"
using namespace CSC2110;

#include <iostream>
using namespace std;

int main(int argc, char** argv)
{
   ListArray<CD>* cds = CD::readCDs("cds.txt");
   int num_cds = cds->size();

   CircularList<CD>* cl = new CircularList<CD>();
   Random* r = Random::getRandom();

   int count = 1;
   while(count <= 25)  //add 25 random cds to the circular list
   {
      int random_cd_index = r->getRandomInt(1, num_cds);
      CD* cd = cds->get(random_cd_index);
      cl->add(count, cd);
      count++;
   }

   delete cds;
   cl->get(1);

   Gtk::Main kit(argc, argv);

   Gtk::Window win;
   win.set_title("Circular List!");
   win.set_position(Gtk::WIN_POS_CENTER);

   //the size of the window
   int width = 700;
   int height = 700;

   win.set_size_request(width, height);
   win.set_resizable(false);  

   Gtk::Table tbl(10, 2, true);
   int rows = tbl.property_n_rows();
   int button_height = (int) (((double) height)/rows + 0.5);

   Gtk::Entry* txtGet = new Gtk::Entry();
   txtGet->set_text("1");

   CLGUI* clg = new CLGUI(cl, txtGet, width, height - button_height);  //needs to know its own dimensions
   cl->addListener((Update*) clg);

   Gtk::Button btnGet("Remove!");
   btnGet.signal_clicked().connect(sigc::mem_fun(clg, &CLGUI::on_cl_button_click_event));

   tbl.attach(*clg, 0, 2, 0, 9, Gtk::FILL | Gtk::EXPAND, Gtk::FILL | Gtk::EXPAND, 0, 0);
   tbl.attach(btnGet, 0, 1, 9, 10, Gtk::FILL | Gtk::EXPAND, Gtk::FILL | Gtk::EXPAND, 0, 0);
   tbl.attach(*txtGet, 1, 2, 9, 10, Gtk::FILL | Gtk::EXPAND, Gtk::FILL | Gtk::EXPAND, 0, 0);

   win.add(tbl);
   win.show_all_children();

   Gtk::Main::run(win);

   return 0;
}
