public class Node {
    private Node prevNode;
    private int xPosition;
    private int yPosition;

    public Node(int x, int y) {
        this.prevNode = null;
        this.xPosition = x;
        this.yPosition = y;
    }

    public Node(Node prevNode, int x, int y, int distance) {
        this.prevNode = prevNode;
        this.xPosition = x;
        this.yPosition = y;
    }

    // Getters
    public Node getPrev() {
        return prevNode;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() { return yPosition; }



    // Setters
    public void setPrev(Node prevNode) {
        this.prevNode = prevNode;
    }

    public void setX(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setY(int yPosition) {
        this.yPosition = yPosition;
    }


}
