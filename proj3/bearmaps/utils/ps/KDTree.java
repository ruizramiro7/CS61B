package bearmaps.utils.ps;

import java.util.HashSet;
import java.util.List;

public class KDTree implements PointSet{
    private static final boolean Horizontal = false;
    private static final boolean Vertical = true;

    public Node root;

    public static class Node{
        private Point p;
        private boolean direction = false;
        private Node left; // down
        private Node right; //up

        Node(Point point, boolean dir){
            p = point;
            direction = dir;
            left = null;
            right = null;
        }
    }

    /**
     * Compares two point and dermines which one is bigger and smaller.
     * @param p1 takes a Point object.
     * @param p2 takes a Point object.
     * @param direction the orientation of the point.
     * @return returns -1,0,1 when comparing the points X or Y values.
     */
    public int comparePoints(Point p1, Point p2, boolean direction){
        if (direction == Horizontal){
            return Double.compare(p1.getX(), p2.getX());
        }
        return Double.compare(p1.getY(), p2.getY());
    }

    /**
     * Adds a point to an already existing node.
     * @param n The node that a point is being added to.
     * @param newPoint Point to add at node.
     * @param level Direction of the point.
     * @return Adds a node to the left or right of the node.
     */
    private Node add(Node n, Point newPoint, boolean level){
        if (n == null){
            return new Node(newPoint, level);
        }
        if (newPoint.equals(n.p)){
            return n;
        }
        int compare = comparePoints(newPoint, n.p, n.direction);
        if (compare >= 0){
            n.right = add(n.right, newPoint, !level);
        }else if (compare < 0){
            n.left = add(n.left, newPoint, !level);
        }
        return n;
    }

    public KDTree(List<Point> points){
        for (Point p : points){
            root = add(root, p, Horizontal);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x ,y);
        return nearest(root, target, root).p;
    }

    public Node nearest(Node n, Point target, Node closest){
        if (n == null){
            return closest;
        }
        double targetDistance = Point.distance(n.p, target);
        double closestDistance = Point.distance(closest.p, target);
        if (targetDistance < closestDistance){
            closest = n;
        }
        int compare = comparePoints(n.p, target, n.direction);
        Node goodSide;
        Node badSide;
        if (compare > 0){
            goodSide = n.left;
            badSide = n.right;
        }else {
            goodSide = n.right;
            badSide = n.left;
        }
        closest = nearest(goodSide, target, closest);
        double newCloseDist = Point.distance(closest.p, target);
        if (badSide != null){
            if (n.direction == Horizontal){
                if ((Math.pow(target.getX() - n.p.getX(), 2) < newCloseDist)){
                    closest = nearest(badSide, target, closest);
                }
            }else {
                if ((Math.pow(target.getY() - n.p.getY(), 2) < newCloseDist)){
                    closest = nearest(badSide, target, closest);

                }
            }
        }
        return closest;
    }

}
