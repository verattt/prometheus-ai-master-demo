package nn;

import knn.api.KnowledgeNode;
import tags.Argument;
import tags.Fact;
import tags.Tag;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class NewGrid extends JComponent implements Runnable {
    //TODO: Thread update when tree map changes
    private static final int WIDE = 640;
    private static final int HIGH = 480;
    private static List<Node> nodes = new ArrayList<Node>();
    private static List<Edge> edges = new ArrayList<Edge>();
    private Rectangle mouseRect = new Rectangle();
    private boolean selecting = false;
    public JFrame f = new JFrame("MapVisualizer");
    public KnowledgeNode kn;
    public void run() {

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        NewGrid gp = new NewGrid(kn);
        f.add(new JScrollPane(new NewGrid(kn)), BorderLayout.CENTER);
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x  = ParseFact((Fact)kn.getInputTag())[0]*10;
        int y  = ParseFact((Fact)kn.getInputTag())[1]*10;
        int windowX = Math.max(0, (screenSize.width  - WIDE) / 2)+x;
        int windowY = Math.max(0, (screenSize.height - HEIGHT) / 2)+y;

        f.setLocation(windowX, windowY);

    }

    public void update(){
        SwingUtilities.updateComponentTreeUI(f);
    }


    public NewGrid(KnowledgeNode KN) {
        this.kn = KN;
        this.setOpaque(true);
    }

    public void updateKN(KnowledgeNode kn) {
        this.kn=kn;
        if (kn.getOutputTags() == null) {
            return;
        }
        else{
            for(Tag n: kn.getOutputTags()){
                drawTag((Fact) n);
            }
            drawTag((Fact) kn.getInputTag());
        }


    }


    private void drawTag(Fact f) {

        Node par = new Node(getPoint(f), 10, getColor(f), Kind.Square);
        nodes.add(par);


    }

    private Point getPoint(Fact f) {
        int[] xy  = ParseFact(f);
        Point a = new Point(xy[0] * 25, xy[1] * 25);
        return a;
    }

    public int[] ParseFact(Fact f){
        List<Argument> a =f.getArguments();
        int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""));
        int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""));
        int[] v = {x,y};
        return v;
    }

    private Color getColor(Fact n) {
        Color c;
        if (n.getPredicateName().equals("WAYPOINT")) {
            c = new Color(83, 140, 255);
        } else if (n.getPredicateName().equals("Moveable")) {
            c = Color.PINK;
        }
        else if(n.getPredicateName().equals("Immovable")){
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
