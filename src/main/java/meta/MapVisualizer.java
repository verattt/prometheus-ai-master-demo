package meta;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class MapVisualizer extends JComponent implements Runnable {
    //TODO: Thread update when tree map changes
    private static final int WIDE = 640;
    private static final int HIGH = 480;
    private static List<Node> nodes = new ArrayList<Node>();
    private static List<Edge> edges = new ArrayList<Edge>();
    private Rectangle mouseRect = new Rectangle();
    private boolean selecting = false;
    public JFrame f = new JFrame("MapVisualizer");
    public void run() {

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MapVisualizer gp = new MapVisualizer();
        f.add(new JScrollPane(gp), BorderLayout.CENTER);
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);

    }

    public void update(){
        SwingUtilities.updateComponentTreeUI(f);
        f.setVisible(true);
    }

    public void hiding(){
        f.setVisible(false);
    }


    public MapVisualizer() {
        this.setOpaque(true);
    }

    public void addTreeNode(TreeMap<meta.Node> map) {

        if (map == null) {
            return;
        }
        if (map.getChildren().size() == 0) {
            drawTreeNode(map.getNode(), map.getNode());
        }
        for (TreeMap<meta.Node> t : map.getChildren()) {

            drawTreeNode(map.getNode(), t.getNode());
            addTreeNode(t);
        }

    }


    private void drawTreeNode(meta.Node parent, meta.Node node) {

        Node par = new Node(getPoint(parent), 10, getColor(parent), Kind.Circular);
        Node nod = new Node(getPoint(node), 10, getColor(node), Kind.Circular);
        nodes.add(par);
        nodes.add(nod);
        //if(parent.getType().equals("WAYPOINT")&&node.getType().equals("WAYPOINT"))
        //edges.add(new Edge(par, nod));

    }

    private Point getPoint(meta.Node n) {
        Point a = new Point(n.getCoordinate().get(0).intValue() * 3, n.getCoordinate().get(1).intValue() * 3);
        return a;
    }


    private Color getColor(meta.Node n) {
        Color c;
        if (n.getType().equals("WAYPOINT")) {
            c = new Color(83, 140, 255);
        } else if (n.getType().equals("Moveable")) {
            c = Color.PINK;
        }
        else if(n.getType().equals("Immovable")){
            c = Color.BLACK;
        }
        else {
            c = new Color(255, 255, 255);
        }
        return c;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.setColor(new Color(0x00f0f0f0));
        gg.fillRect(0, 0, getWidth(), getHeight());
        gg.translate(WIDE / 2, HIGH / 2);
        gg.scale(1.0, -1.0);
        for (Edge e : edges) {
            e.draw(gg);
        }
        for (Node n : nodes) {
            n.draw(gg);
        }
        if (selecting) {
            gg.setColor(Color.darkGray);
            gg.drawRect(mouseRect.x, mouseRect.y,
                    mouseRect.width, mouseRect.height);
        }
    }


    /**
     * The kinds of node in a graph.
     */
    private enum Kind {
        Circular, Rounded, Square;
    }

    /**
     * An Edge is a pair of Nodes.
     */
    private static class Edge {

        private Node n1;
        private Node n2;

        public Edge(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
        }

        public void draw(Graphics g) {
            Point p1 = n1.getLocation();
            Point p2 = n2.getLocation();
            g.setColor(Color.darkGray);
          //  System.out.println("draw edge");
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    /**
     * A Node represents a node in a graph.
     */
    private static class Node {

        private Point p;
        private int r;
        private Color color;
        private Kind kind;
        private boolean selected = false;
        private Rectangle b = new Rectangle();

        /**
         * Construct a new node.
         */
        public Node(Point p, int r, Color color, Kind kind) {
            this.p = p;
            this.r = r;
            this.color = color;
            this.kind = kind;
            setBoundary(b);
        }

        /**
         * Calculate this node's rectangular boundary.
         */
        private void setBoundary(Rectangle b) {
            b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
        }

        /**
         * Draw this node.
         */
        public void draw(Graphics g) {
            g.setColor(this.color);
            if (this.kind == Kind.Circular) {
                g.fillOval(b.x, b.y, b.width, b.height);
            } else if (this.kind == Kind.Rounded) {
                g.fillRoundRect(b.x, b.y, b.width, b.height, r, r);
            } else if (this.kind == Kind.Square) {
                g.fillRect(b.x, b.y, b.width, b.height);
            }
            if (selected) {
                g.setColor(Color.darkGray);
                g.drawRect(b.x, b.y, b.width, b.height);
            }
        }

        /**
         * Return this node's location.
         */
        public Point getLocation() {
            return p;
        }

    }
}
