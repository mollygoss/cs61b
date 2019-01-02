public class IntNode<Item>{
    public IntNode prev;
    public Item item;
    public IntNode next;

    public IntNode(IntNode p, Item i, IntNode n){
        prev = p;
        item = i;
        next = n;
    }

    public IntNode(Item i) {
        prev = null;
        item = i;
        next = null;
    }
}