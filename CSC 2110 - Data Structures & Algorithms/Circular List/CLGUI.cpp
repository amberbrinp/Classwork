#include "CLGUI.h"
#include "Text.h"
using CSC2110::String;

CLGUI::CLGUI(CircularList<CD>* list, Gtk::Entry* txtGt, int w, int h) : DrawPanel(w, h, list)
{
   cl = list;
   txtGet = txtGt;
}

CLGUI::~CLGUI()
{
   delete cl;
}

void CLGUI::update()
{
   render();
}

void CLGUI::on_cl_button_click_event()
{
   Glib::ustring utxt = txtGet->get_text();
   int len = utxt.size();
   const char* chtxt = utxt.data();

   String* str = new String(chtxt);
   delete[] chtxt;
   int index = str->a_to_i();
   delete str;

   int size = cl->size();
   if (index >= 1 && index <= size)
   {
      cl->remove(index);
   }
   update();
}
