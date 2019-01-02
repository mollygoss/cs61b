public class ArrayDeque <Item>{

    private int size;
    private Item[] items;
    private int capacity;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque(){
        items = (Item[]) new Object[100];
        capacity = 100;
        size = 0;
        nextfirst = 0;
        nextlast = 100;
    }

    //creates the index relative to the circular array
    private int trueindex(int x){
        return x % capacity;
    }

    // resizes the array to accommodate for new additions
    private void Resize(int newsize){
        Item[] a = (Item[]) new Object[newsize];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
        capacity = newsize;
    }

    //Adds an item to the front of the Deque.
    public void addFirst(Item x){
        if (isFull()){
            Resize(capacity+1);
            items[trueindex(nextfirst)] = x;
        } else{
            items[trueindex(nextfirst)] = x;
        }
        nextfirst++;
        size ++;
    }

    // Adds an item to the back of the Deque.
    public void addLast(Item x){
        if (isFull()){
            Resize(capacity +1);
            items[trueindex(nextlast)] = x;
        } else{
            items[trueindex(nextlast)] = x;
        }
        nextlast++;
        size++;
    }

    //Returns true if deque is empty, false otherwise.
    public boolean isEmpty(){
        if (size == 0){
            return true;
        } else{
            return false;
        }
    }

    private boolean isFull(){
        if (size == capacity){
            return true;
        } else{
            return false;
        }
    }

    //Returns the number of items in the Deque.
    public int size(){
        return size;
    }

    //Prints the items in the Deque from first to last, separated by a space.
    public void printDeque(){
        int i = 0;
        while (i <size){
            System.out.print(items[i] + " ");
            i ++;
        }
    }

    // Removes and returns the item at the front of the Deque. If no such item exists, returns null.
    public Item removeFirst(){
        Item item = items[nextfirst];
        items[nextfirst] = null;
        size --;
        return item;
    }

    // Removes and returns the item at the back of the Deque. If no such item exists, returns null.
    public Item removeLast(){
        Item item = items[nextlast];
        items[nextlast] = null;
        size --;
        return item;
    }

    // Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque!
    public Item get(int index) {
        return items[trueindex(index)];
    }
}