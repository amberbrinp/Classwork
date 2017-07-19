#if !defined (TREENODE_H)
#define TREENODE_H

#include "Color.h"
#include "Circle.h"
#include "String.h"
using CSC2110::String;

template < class T >
class TreeNode 
{

   private:
      TreeNode* left;
      TreeNode* right;
      T* item;

   public:
      TreeNode(T* item);
      ~TreeNode();

      TreeNode<T>* getLeft();
      TreeNode<T>* getRight();
      T* getItem();

      void setLeft(TreeNode<T>* tNode);
      void setRight(TreeNode<T>* tNode);
      void setItem(T* item);

      void draw(Cairo::RefPtr<Cairo::Context> cr, int x, int y);

};

template < class T >
TreeNode<T>::TreeNode(T* itm)
{
   left = NULL;
   right = NULL;
   item = itm;
}

template < class T >
TreeNode<T>::~TreeNode()
{}

template < class T >
TreeNode<T>* TreeNode<T>::getLeft()
{
   return left;
}

template < class T >
TreeNode<T>* TreeNode<T>::getRight()
{
   return right;
}

template < class T >
T* TreeNode<T>::getItem()
{
   return item;
}

template < class T >
void TreeNode<T>::setLeft(TreeNode<T>* l)
{
   left = l;
}

template < class T >
void TreeNode<T>::setRight(TreeNode<T>* r)
{
   right = r;
}

template < class T >
void TreeNode<T>::setItem(T* itm)
{
  item = itm;
}

template < class T >
void TreeNode<T>::draw(Cairo::RefPtr<Cairo::Context> cr, int x, int y)
{
   T* item = getItem();
   String* key = item->getKey();
   //char* text = key->getText();
   Color* color = new Color(0.886, 0.49, 0.067);
   Circle* circle = new Circle(color, 20, key);
   circle->draw(cr, x, y);

/*
   cr->save();
      Glib::RefPtr<Pango::Layout> pangoLayout = Pango::Layout::create (cr);
      cr->move_to(x - 10, y - 12);
      Pango::FontDescription verdana_9 = Pango::FontDescription("verdana 9"); 
      pangoLayout->set_font_description(verdana_9);
      pangoLayout->set_text(text);
      pangoLayout->update_from_cairo_context(cr); //gets cairo cursor position
      pangoLayout->add_to_cairo_context(cr); //adds text to cairos stack of stuff to be drawn
      cr->set_source_rgba(0, 0, 0, 1.0); //opaque, black
      cr->fill();
   cr->restore();
*/

   delete circle;
}

#endif
