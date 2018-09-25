/**
 * D-Heap
 */

/**
 * 
 * @author  Yotam Aharony - 308338169 - yotamaharony
 * 		 & Itay Cohen - 308213883 - itayc
 *
 */

public class DHeap
{
    private int size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
	//basic concept: iterate each one of the none leaves items, and perform an heapify-down operation
	//for each one of them. complexity: O(n)
    public int arrayToHeap(DHeap_Item[] array1) 
    {
        this.array = array1; //the DHeap's array in now pointing at the new array
        this.size = array1.length; //set the right size of the new array
        int comparisonCount=0;
        int currentPower = 1;
        int powersSum =1;
        
        // we don't want to perform heapify down on leaves, so we want to calculate which level
        // of the heap is one before the last one.
        while(powersSum<this.size)
        {
        	//calculate which item will be the first to be heapified down
        	powersSum+=Math.pow(this.d,currentPower);
        	currentPower++;
        }
        
        //each one of the none-leaf items will be heapified-down
        int startIndex = powersSum - (int)Math.pow(this.d,currentPower-1) - 1;
        for(int i=startIndex;i>=0;i--)
        {
        	comparisonCount+=heapifyDown(i);
        }
        
        return comparisonCount;
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    //iterate each one the elements and check if its parent is smaller than him.
    //if not, return false (the d-ary principle was broken)
    public boolean isHeap() 
    {
    	//iterate each one the elements
        for (int i=1; i<size;i++)
        {
        	if (array[i].getKey()<array[parent(i, d)].getKey() && array[i].getPos()==i)
        		return false;
        }
        
        return true;
    }


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) 
    { 
    	int childNumber = i%d;
    	if (childNumber == 0)
    		childNumber = d;
    	
    	return (((i+1)-childNumber)/d);
    } 
    
    public static int child (int i, int k, int d)
    { 
    	return d*i+k;
    }  

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {   
    	//we make sure that we first insert the item in the first available place in the heap
    	this.array[this.size] = item;
    	item.setPos(this.size);
    	this.size++; //update heap size
    	int comparisonCount = heapifyUp(this.size-1); //the elements that has just been inserted is heapified up
    	return comparisonCount;
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    //basic concept: swap the heap's root, then decrease the heap's size by one
    //and then heapify-down the new root
    public int Delete_Min()
    {
        swapItems(0,size-1); 
     	size--;
     	int comparisonCount = heapifyDown(0);
     	return comparisonCount;
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    //by definition, the minimal key is the root of the heap, so we just need to return the first element
    public DHeap_Item Get_Min()
    {
    	return array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta)
    {
    	item.setKey(item.getKey()-delta); //decrease the key of the given item
    	array[item.getPos()] = item; //put the new item as an item in the heap
    	int comparisonCount = heapifyUp(item.getPos()); //perform an heapify up opreration on the relevant item
     	return comparisonCount;
    }
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item)
    {
    	//first, move the desired item to the root of the heap
    	int comparisonCount = Decrease_Key(item, item.getKey()-Get_Min().getKey()+1);
    	//then, do a delete-min operation
    	comparisonCount += Delete_Min();

     	return comparisonCount;
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		//since this is a static operation - create a dheap for the sort
		DHeap dheap = new DHeap(d,array1.length);
		DHeap_Item[] initArr = new DHeap_Item[array1.length];
		
		//create a DHeap-Item array with all the relevant elements that received as an input
		for(int i=0;i<array1.length;i++) {
			DHeap_Item newItem = new DHeap_Item(array1[i]+"",array1[i]);
			newItem.setPos(i);
			initArr[i] = newItem;
		}
		//insert all DHeap-Item array to the dheap using arrayToHeap method (O(n) time) 
		int counter = dheap.arrayToHeap(initArr);
		//create a sorted array using n delete-min operations (nlog(n) time)
		for (int i=0; i<array1.length;i++)
		{
			array1[i] = dheap.Get_Min().getKey();
			counter+=dheap.Delete_Min();
		}
		
		return counter;
	}
	
	//assisting function - gets an item position and perform heapify up operation
	//in order to preserve the heap's invariant
	private int heapifyUp(int i)
	{
		int counter = 0;
		int currentPos = i;
		//stop only when the item's parent is still not the root
		while (parent(currentPos,d)!=0)
		{
			int parentIndex = parent(currentPos,d);
			int parentKey = array[parentIndex].getKey();
			
			//if parent's key > child's key - swap
			counter++;
			if(parentKey>array[currentPos].getKey())
			{
				swapItems(currentPos, parentIndex);
				currentPos = parentIndex;
			}
			//otherwise, the heap's invariant is preserved and we are done
			else
				return counter;
		}
		
		//if we have got here, we have to check if the root have to be swapped with current node
		int parentKey = array[0].getKey();
		counter++;
		//if parent's key > child's key - swap
		if(parentKey>array[currentPos].getKey())
		{
			swapItems(currentPos, 0);
		}
		//otherwise, the heap's invariant is preserved and we are done
		return counter;

	}
	
	//assisting function - gets an item position and perform heapify down operation
	//in order to preserve the heap's invariant
	private int heapifyDown(int i)
	{
		int counter = 0;
		int currentPos = i;
		//stop only when the item's child is outside of the heap's bounds
		while (child(currentPos,d,d)<size)
		{
			//find the min child of current node
			int childD = child(currentPos,d,d);
			int minChildIndex = childD-d+1;
			int minChildKey = array[childD-d+1].getKey();
			
			for (int k=childD-d+2; k<=childD; k++)
			{
				//while looking for the min child - count each comparison
				counter++;
				if (array[k].getKey()<minChildKey)
				{
					minChildIndex = k;
					minChildKey = array[k].getKey();
				}
			}
			
			//if the child's key < parent's key - swap
			counter++;
			if(minChildKey<array[currentPos].getKey())
			{
				swapItems(currentPos, minChildIndex);
				currentPos = minChildIndex;
			}
			else //otherwise, the heap's invariant is preserved and we are done
				return counter;
		}
		
		//if we have got here, we have to check if current node have to be swapped with one of its children
		int firstChild = child(currentPos, 1, d);
		if (firstChild<size)
		{
			int numberOfChilds = size-firstChild;
			
	    	if(numberOfChilds>0)
	    	{
	    		int minChildIndex = firstChild;
	    		int minChildKey = array[minChildIndex].getKey();
	    		//look for the minimal child in this case as well
	    		for (int k=1; k<=numberOfChilds; k++)
	    		{
	    			counter++;
	    			if (array[child(currentPos, k, d)].getKey()<minChildKey)
	    			{
	    				minChildIndex = child(currentPos, k, d);
	    				minChildKey = array[child(currentPos, k, d)].getKey();
	    			}
	    		}
	    		
	    		//if the child's key < parent's key - swap
    			counter++;
	    		if(minChildKey<array[currentPos].getKey())
	    		{
	    			swapItems(currentPos, minChildIndex); //otherwise, the heap's invariant is preserved and we are done
	    		}
	    	}
		}
		return counter;
	}
	
	//assisting function
	//this method gets positions of two items in the array, and swaps them 
	private void swapItems(int i,int j)
	{
		DHeap_Item temp = array[i];
		
		array[i] = array[j];
		array[i].setPos(i);
		
		array[j] = temp;
		array[j].setPos(j);
	}
	
}
