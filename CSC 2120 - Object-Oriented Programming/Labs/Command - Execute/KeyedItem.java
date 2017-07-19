
public abstract class KeyedItem<T extends Comparable<T>>
{
  private T searchKey;
  
  public KeyedItem(T key) 
  {
    searchKey = key;
  }  

  public T getKey() 
  {
    return searchKey;
  }  

  public String toString()
  {
     return searchKey.toString();
  }
}