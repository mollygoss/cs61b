public class LinkedListDeque<Item>{
    private IntNode sentinel;
    private int size;

    // Creates an empty linked list deque.
    public LinkedListDeque(){
        //sentinel = new IntNode(sentinel, 0, sentinel);
        sentinel = new IntNode(new IntNode(0), 0, new IntNode(0));
        size = 0;
    }
    //Adds an item to the front of the Deque.
    public void addFirst(Item Item){
        IntNode newnode = new IntNode(sentinel, Item, sentinel.next);
        if (size == 0){
            sentinel.next = newnode;
            sentinel.prev = newnode;
        }
        sentinel.next.prev = newnode;
        sentinel.next = newnode;
        size += 1;
    }
    // Adds an item to the back of the Deque.
    public void addLast(Item Item){
        IntNode newnode = new IntNode(sentinel.prev, Item, sentinel);
        if (size == 0){
            sentinel.next = newnode;
            sentinel.prev = newnode;
        }
        sentinel.prev.next = newnode;
        sentinel.prev = newnode;
        size += 1;
    }
    //Returns true if deque is empty, false otherwise.
    public boolean isEmpty(){
//        if (sentinel.next == sentinel){
//            return true;
//        }
//        else{
//            return false;
//        }

        if (size == 0) {
            return true;
        }
        return false;
    }
    //Returns the number of items in the Deque.
    public int size(){
        return size;
    }
    //Prints the items in the Deque from first to last, separated by a space.
    public void printDeque(){
        int i = 0;
        IntNode curr = sentinel.next;
        while (i < size - 1){
            System.out.print(curr.item + " ");
            i ++;
            curr = curr.next;
        }
    }
    // Removes and returns the item at the front of the Deque. If no such item exists, returns null.
    public Item removeFirst(){
        if (isEmpty()){
            return null;
        }else {
            Item temp = (Item) sentinel.next.item;
            if (size >1){
                sentinel.next = sentinel.next.next;
                sentinel.next.next.prev = sentinel;
            }else{
                sentinel.next = sentinel;
                sentinel.prev = sentinel;
            }

            size -= 1;
            return temp;
        }
        //return temp;
    }
    // Removes and returns the item at the back of the Deque. If no such item exists, returns null.
    public Item removeLast(){
        if (size == 0){
            return null;
        } else {
            Item temp = (Item) sentinel.prev.item;
            if (size>1) {
                sentinel.prev = sentinel.prev.prev;
                sentinel.prev.next = sentinel;
            } else {
                sentinel.next = sentinel;
                sentinel.prev = sentinel;
            }
            size -= 1;
            return temp;
        }
        //return temp;
    }
    // Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque!
    public Item get(int index){
        int i = 0;
        IntNode curr = sentinel.next;
        if (index > size+1){
            return null;
        }else {
            while (i < size) {
             if (i == index) {
                    return (Item) curr.item;
             }
             curr = curr.next;
             i++;
            }
        }
        return null;
    }
    //Same as get, but uses recursion.
    public Item getRecursive(int index){
        return recursivehelper(sentinel.next, 0);
    }
    private Item recursivehelper(IntNode intnode, int index){
        if (index == 0){
            return (Item) intnode.item;
        } else{
            return recursivehelper(intnode.next, index -1);
        }
    }
    }