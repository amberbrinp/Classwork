#if !defined (BINARYSEARCHTREE_H)
#define BINARYSEARCHTREE_H

#include "BinaryTreeIterator.h"
#include "TreeNode.h"
#include "Text.h"
#include "Line.h"
#include "Drawable.h"
using CSC2110::String;

#include <iostream>
using namespace std;

template < class T >
class BinarySearchTree : public Drawable
{

   private:
      TreeNode<T>* root;
      int sze;
      void destroyItem(TreeNode<T>* tNode);
      void destroy();

      TreeNode<T>* insertItem(TreeNode<T>* tNode, T* item);
      TreeNode<T>* removeItem(TreeNode<T>* tNode, String* sk);
      TreeNode<T>* removeNode(TreeNode<T>* tNode);
      TreeNode<T>* removeLeftMost(TreeNode<T>* tNode);
      T* findLeftMost(TreeNode<T>* tNode);

      TreeNode<T>* getRootNode();
      void setRootNode(TreeNode<T>* tNode);
      int getHeight(TreeNode<T>* tNode);
      bool isBalanced(TreeNode<T>* tNode);

      int (*compare_items) (T* item_1, T* item_2);
      int (*compare_keys) (String* key, T* item);

      void minimize(T** items, int first, int last);
      T** toArrayComplete();
      bool traverseComplete(T** items, int array_length, TreeNode<T>* tNode, int index);
      void minimizeComplete(T** items, int first, int last);

      virtual void drawRec(TreeNode<T>* tNode, Cairo::RefPtr<Cairo::Context> cr, Line* line, int x_parent, int x_curr, int y_curr);

   public:
      BinarySearchTree(int (*comp_items) (T* item_1, T* item_2), int (*comp_keys) (String* key, T* item));
      ~BinarySearchTree();

      bool isEmpty();
      void makeEmpty();

      T* retrieve(String* search_keys);
      void insert(T* item);
      void remove(String* search_keys);

      BinaryTreeIterator<T>* iterator();
      T* getRootItem();
      int getHeight();
      bool isBalanced();

      T** toArray();

      virtual void draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height);
      virtual void mouseClicked(int x, int y);

      BinarySearchTree<T>* minimize();
      BinarySearchTree<T>* minimizeComplete();

};

template < class T >
int BinarySearchTree<T>::getHeight()
{
	int height = getHeight(root);
	return height;
}

template < class T >
int BinarySearchTree<T>::getHeight(TreeNode<T>* tNode)
{
   //DO THIS
	int height;
	if (tNode == NULL)
	{
		return 0;
	}
	
	int left = getHeight(tNode->getLeft());
	int right = getHeight(tNode->getRight());
	if (left < right)
	{
		height = right+1;
	}
	else
	{
		height = left+1;
	}
	return height;


}

template < class T >
bool BinarySearchTree<T>::isBalanced()
{
	return isBalanced(root);
}

template < class T >
bool BinarySearchTree<T>::isBalanced(TreeNode<T>* tNode)
{
   //DO THIS
	if (tNode != NULL)
	{
		bool balRight = isBalanced(tNode->getRight());
		bool balLeft = isBalanced(tNode->getLeft());
		if (balRight != true && balLeft != true)
		{
			return false;
		}
		else
		{
			int left = getHeight(tNode->getLeft());
			int right = getHeight(tNode->getRight());
			if (left - right < -1 || left - right > 1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}
	return true;



}

template < class T >
BinarySearchTree<T>* BinarySearchTree<T>::minimize()
{
   T** items = toArray();
   BinarySearchTree<T>* bst = new BinarySearchTree<T>(compare_items, compare_keys);
   //DO THIS
	int last = sze - 1;
	int first = 0;

	bst->minimize(items, first, last);

	return bst;
}

template < class T >
void BinarySearchTree<T>::minimize(T** items, int first, int last)
{
   //DO THIS (recursive minimize method)
   int mid = 0;
	if (first > last)
	{
		return;
	}
	mid = (last + first)/2;
	insert(items[mid]);
	minimize(items, first, mid - 1);
	minimize(items, mid + 1, last);

}

template < class T >
T** BinarySearchTree<T>::toArray()
{
   T** items = new T*[sze];

   BinaryTreeIterator<T>* iter = iterator();
   iter->setInorder();
   int i = 0;
   while(iter->hasNext())
   {
      items[i] = iter->next();
      i++;
   }
   delete iter;

   return items;
}

template < class T >
BinarySearchTree<T>* BinarySearchTree<T>::minimizeComplete()
{
	T** items = toArray();
	BinarySearchTree<T>* bst = new BinarySearchTree<T>(compare_items, compare_keys);
	//DO THIS
	int first = 0;
	int last = sze - 1;
	bst->minimizeComplete(items, first, last);
	return bst;
}

template < class T >
void BinarySearchTree<T>::minimizeComplete(T** items, int first, int last)
{
	double TOL = 0.0001;
	//the log base e of 2 is 0.69314718
	//one over 0.69314718 = 1.442695042
	double log_factor = 1.442695042;

	if (first <= last)
	{
		//the rounding ensures that mid is included in the count (it is necessary)
		int mid = (int) ((last + first)/2.0 + 0.5);
		
		//start at mid and gradually move to the right to find the next element to insert into the tree
		//if first and last are the same, mid automatically succeeds (leaf element)
		if (first < last)
		{
			//initial log computations using mid
			double k_left = log(mid - first+1)*log_factor;           //log base 2 of the number of items to the left of mid (including mid)
			double int_k_left = (int)(k_left + 0.5);               //same as above but rounded
			double k_right = log(last - mid+1)*log_factor;
			double int_k_right = (int)(k_right + 0.5);

			//keep searching for spot where the number of elements to the left of mid is 2^k - 1 (a full tree)
			//which means the number of elements to the left of mid including mid is 2^k 
			//or the number of elements to the right of mid is 2^k
			//compare the direct log computation and the computation cast to an int
			//to determine if the direct computation is an int
			while (fabs(k_left - int_k_left) > TOL && fabs(k_right - int_k_right) > TOL)
			{
            mid++;
            //DO THIS
            //try again with mid shifted one to the right
			k_left = log(mid - first+1)*log_factor;     
			int_k_left = (int)(k_left + 0.5);               
			k_right = log(last - mid+1)*log_factor;
			int_k_right = (int)(k_right + 0.5);
			}
		}
		//DO THIS
		//found the next item to insert into the tree
		//get it, insert it, and make two recursive calls
		insert(items[mid]);
		minimizeComplete(items, first, mid - 1);
		minimizeComplete(items, mid + 1, last);
	}
}

template < class T >
void BinarySearchTree<T>::remove(String* sk)
{
   root = removeItem(root, sk);
}

template < class T >
TreeNode<T>* BinarySearchTree<T>::removeItem(TreeNode<T>* tNode, String* sk)
{
   if (tNode == NULL)
   { 
      return tNode;  //take no action, item not present
   }

   T* node_items = tNode->getItem();
   int comp = (*compare_keys) (sk, node_items);

   if (comp == 0)
   {
      sze--;
      return removeNode(tNode);  //delete the node
   }
   else if (comp < 0)
   {
      TreeNode<T>* subtree = removeItem(tNode->getLeft(), sk);
      tNode->setLeft(subtree);
      return tNode;
   }
   else
   {
      TreeNode<T>* subtree = removeItem(tNode->getRight(), sk);
      tNode->setRight(subtree);
      return tNode;
   }
}

template < class T >
TreeNode<T>* BinarySearchTree<T>::removeNode(TreeNode<T>* tNode)
{
   if (tNode->getLeft() == NULL && tNode->getRight() == NULL)
   {
      delete tNode;
      return NULL;
   }
   else if (tNode->getLeft() == NULL)
   {
      TreeNode<T>* temp = tNode->getRight();
      delete tNode;
      return temp;
   }
   else if (tNode->getRight() == NULL)
   {
      TreeNode<T>* temp = tNode->getLeft();
      delete tNode;
      return temp;
   }
   else 
   {
      T* replace = findLeftMost(tNode->getRight());
      tNode->setItem(replace);
      TreeNode<T>* subtree = removeLeftMost(tNode->getRight());
      tNode->setRight(subtree);
      return tNode;
   }
}

template < class T >
T* BinarySearchTree<T>::findLeftMost(TreeNode<T>* tNode)
{
   while (tNode->getLeft() != NULL)
   {
      tNode = tNode->getLeft();
   }
   return tNode->getItem();
}

template < class T >
TreeNode<T>* BinarySearchTree<T>::removeLeftMost(TreeNode<T>* tNode)
{
   TreeNode<T>* subtree;

   if (tNode->getLeft() != NULL)
   {
      subtree = removeLeftMost(tNode->getLeft());
      tNode->setLeft(subtree);
      return tNode;
   }
   else
   {
      subtree = tNode->getRight();
      delete tNode;
      return subtree;
   }
}

template < class T >
BinarySearchTree<T>::BinarySearchTree(int (*comp_items) (T* item_1, T* item_2), int (*comp_keys) (String* key, T* item))
{
   root = NULL;
   sze = 0;

   compare_items = comp_items;
   compare_keys = comp_keys;
}

template < class T >
BinarySearchTree<T>::~BinarySearchTree()
{
   destroy();
}

template < class T >
void BinarySearchTree<T>::destroy()
{
   destroyItem(root);
}

template < class T >
void BinarySearchTree<T>::destroyItem(TreeNode<T>* tNode)
{
   if (tNode != NULL)
   {
      destroyItem(tNode->getLeft());
      destroyItem(tNode->getRight());
      delete tNode;
   }
}

template < class T >
bool BinarySearchTree<T>::isEmpty()
{
   return sze == 0;
}

template < class T >
void BinarySearchTree<T>::makeEmpty()
{
   destroy();
   root == NULL;
   sze = 0;
}

template < class T >
TreeNode<T>* BinarySearchTree<T>::getRootNode()
{
   return root;
}

template < class T >
void BinarySearchTree<T>::setRootNode(TreeNode<T>* tNode)
{  
   root = tNode;
}

template < class T >
T* BinarySearchTree<T>::getRootItem()
{
   T* rootItem = root->getItem();
   return rootItem;
}

template < class T >
void BinarySearchTree<T>::insert(T* item)
{
   root = insertItem(root, item);
}

template < class T >
T* BinarySearchTree<T>::retrieve(String* sk)
{
   TreeNode<T>* tNode = getRootNode();

   while (tNode != NULL)
   {
      T* node_items = tNode->getItem();
      int comp = (*compare_keys) (sk, node_items);

      if (comp == 0)
      {
         //no duplicate search keys allowed, so do nothing
         return node_items;
      }
      else if (comp < 0)
      {
         tNode = tNode->getLeft();
      }
      else
      {
         tNode = tNode->getRight();
      }
   }

   return NULL; //item is not in the tree
}

template < class T >
TreeNode<T>* BinarySearchTree<T>::insertItem(TreeNode<T>* tNode, T* item)
{
   TreeNode<T>* subtree;
   if (tNode == NULL)
   { 
      sze++;
      return new TreeNode<T>(item);
   }

   T* node_items = tNode->getItem();
   int comp = (*compare_items) (item, node_items);

   if (comp == 0)
   {
      //no duplicate search keys allowed, so do nothing
      return tNode;
   }
   else if (comp < 0)
   {
      subtree = insertItem(tNode->getLeft(), item);
      tNode->setLeft(subtree);
   }
   else
   {
      subtree = insertItem(tNode->getRight(), item);
      tNode->setRight(subtree);
   }
   
   return tNode;
}

template < class T >
BinaryTreeIterator<T>* BinarySearchTree<T>::iterator()
{
   return new BinaryTreeIterator<T>(root);
}

//the test for completeness involves traversing the reference based BST and inserting the items in an array
//the left and right child of a given node are placed in the array using the index formula for complete binary trees
//the formulas should never result in going out of bounds of the array if the ref based BST is complete
template < class T >
bool BinarySearchTree<T>::traverseComplete(T** items, int array_length, TreeNode<T>* tNode, int index)
{
   if (tNode != NULL)
   {
      if (index >= array_length)
      {
         return false;  //return false (the ref based BST is not complete)
      }

      items[index] = tNode->getItem();

      int left = 2*index + 1;
      int right = left + 1;

      bool bl = traverseComplete(items, array_length, tNode->getLeft(), left);
      bool br = false;

      if (bl)
      {
         br = traverseComplete(items, array_length, tNode->getRight(), right);
      }

      return (bl && br); //both left and right subtrees are complete
   }

   return true;  //return true
}

template < class T >
T** BinarySearchTree<T>::toArrayComplete()
{
   T** items = new T*[sze];
   bool result = traverseComplete(items, sze, getRootNode(), 0);

   if (result)
   {
      return items;
   }
   else
   {
      delete[] items;
      return NULL;
   }
}

template < class T >
void BinarySearchTree<T>::draw(Cairo::RefPtr<Cairo::Context> cr, int width, int height)
{
   Line line(new Color(0, 0, 0), 5.0);
   drawRec(getRootNode(), cr, &line, width, width/2, 20);
}

template < class T >
void BinarySearchTree<T>::drawRec(TreeNode<T>* tNode, Cairo::RefPtr<Cairo::Context> cr, Line* line, int x_parent, int x_curr, int y_curr)
{

   //traversal to draw the entire binary tree
   if (tNode != NULL)
   {
      //computing the location of the current node's two children
      //the distance between a node's two children is the same as the horizontal distance between
      //the current node and the current node's parent
      //need to do this first as the drawing is from the bottom to the top
      int separation = abs(x_curr - x_parent);

      //need to make sure that the children are symmetrically placed with respect to the parent
      int x_left = x_curr - separation/2;  //the location of the left child
      int x_right = x_left + separation;   //the location of the right child

      //compute the vertical location of the current node's parent
      //and the current node's two children
      int y_parent = y_curr - 50;
      int y_children = y_curr + 50;

      //draw the line connecting the current node to its parent
      if (tNode != root)
      {
         line->draw(cr, x_curr, y_curr, x_parent, y_parent);
      }

      //draw the children
      drawRec(tNode->getLeft(), cr, line, x_curr, x_left, y_children);
      drawRec(tNode->getRight(), cr, line, x_curr, x_right, y_children);

      //draw tNode
      tNode->draw(cr, x_curr, y_curr);
   }
}

template < class T >
void BinarySearchTree<T>::mouseClicked(int x, int y) {}

#endif
