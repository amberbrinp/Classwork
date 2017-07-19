#ifndef CLGUI_H
#define CLGUI_H

#include "CircularList.h"
#include "CD.h"
using CSC2110::CD;
#include "DrawPanel.h"
#include <gtkmm.h>

class CLGUI : public DrawPanel, Update
{
   private:
      CircularList<CSC2110::CD>* cl;
      Gtk::Entry* txtGet;

   public:
      CLGUI(CircularList<CSC2110::CD>* cl, Gtk::Entry* txtGet, int width, int height);
      virtual ~CLGUI();
      virtual void update();       
      virtual void on_cl_button_click_event();
};

#endif 
