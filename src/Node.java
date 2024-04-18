public class Node {
    private Node prevNode;
    private Node nextNode;
    private int xPosition;
    private int yPosition;
    private int distance;

//    constructor-1-----------------------------------------------------------------------------------------------------
    public Node(int x, int y) {
        this.prevNode = null;
        this.nextNode = null;
        this.xPosition = x;
        this.yPosition = y;
        this.distance = -1;
    }

    //    constructor-2 -----------------------------------------------------------------------------------------------
    public Node(Node prevNode, Node nextNode, int x, int y, int distance) {
        this.prevNode = prevNode;
        this.nextNode = nextNode;
        this.xPosition = x;
        this.yPosition = y;
        this.distance = distance;
    }

    // Getters----------------------------------------------------------------------------------------------------------
    public Node getPrev() {
        return prevNode;
    }
    public Node getNext() {
        return nextNode;
    }
    public int getX() {
        return xPosition;
    }
    public int getY() {
        return yPosition;
    }
    public int getDistance() {
        return distance;
    }
    //------------------------------------------------------------------------------------------------------------------

    // Setters----------------------------------------------------------------------------------------------------------
    public void setPrev(Node prevNode) {
        this.prevNode = prevNode;
    }

    public void setNext(Node nextNode) {
        this.nextNode = nextNode;
    }

    public void setX(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setY(int yPosition) {
        this.yPosition = yPosition;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    //------------------------------------------------------------------------------------------------------------------
}
