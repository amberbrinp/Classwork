#include <gtkmm.h>
#include "BinarySearchTree.h"
#include "BinaryTreeIterator.h"
#include "DrawPanel.h"
#include "CD.h"
using CSC2110::CD;
#include "ListArray.h"
using CSC2110::ListArray;
#include "ListArrayIterator.h"
using CSC2110::ListArrayIterator;

#include <iostream>
using namespace std;

int main(int argc, char** argv)
{
   ListArray<CD>* cds = CD::readCDs("cds.txt");
   int num_items = cds->size();
   cout << num_items << endl;

   ListArrayIterator<CD>* iter = cds->iterator();
   BinarySearchTree<CD>* bst = new BinarySearchTree<CD>(&CD::compare_items, &CD::compare_keys);
   while(iter->hasNext())
   {
      CD* cd = iter->next();
      bst->insert(cd);
   }
   delete iter;

   iter = cds->iterator();
   int count = 1;
   while(iter->hasNext() && count <= 250)
   {
      CD* cd = iter->next();
      bst->remove(cd->getKey());
      count++;
   }
   delete iter;
   delete cds;

   Gtk::Main kit(argc, argv);

   Gtk::Window win;
   win.set_title("BST!");
   win.set_position(Gtk::WIN_POS_CENTER);

   //the size of the window
   int width = 800;
   int height = 800;

   win.set_size_request(width, height);
   win.set_resizable(false);  

   DrawPanel pnl(width, height, bst);  //needs to know its own dimensions

   win.add(pnl);

   win.show_all_children();
   Gtk::Main::run(win);

   return 0;
}
