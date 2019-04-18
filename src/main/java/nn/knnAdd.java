package nn;

import com.google.inject.Guice;
import knn.api.KnowledgeNode;
import knn.api.KnowledgeNodeNetwork;
import knn.api.KnowledgeNodeParseException;
import nn.NewGrid;
import meta.Node;
import prometheus.api.Prometheus;
import prometheus.guice.PrometheusModule;
import tags.Argument;
import tags.Fact;
import tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import static nn.demo.ParseFact;
import static nn.demo.addWaypoint;
import static nn.demo.checkWaypoint;

public class knnAdd {
    public static void main(String[] args) {


//        NewGrid newGrid = new NewGrid();
//        newGrid.run();

        Prometheus prometheus = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
        KnowledgeNodeNetwork knn = prometheus.getKnowledgeNodeNetwork();
        knn.loadData("drone.txt");
        LocalMap l1 = null;
        String[] data = {"WAYPOINT(x4, y4)", "Nothing(x3,  y3)", "Nothing(x4,  y3)", "Immovable(x5,  y3)"};
        String[] newdata = {"WAYPOINT(x4, y4)", "Nothing(x3, y2)", "Nothing(x4, y2)", "Immovable(x5, y2)", "Nothing(x3,  y3)", "Nothing(x4,  y3)", "Immovable(x5,  y3)"};
        try {
            KnowledgeNode kn = new KnowledgeNode(data);
            knn.addKnowledgeNode(kn);
            l1 = new LocalMap(kn, 7);
            l1.run();
//                newGrid.updateKN(kn);
//                newGrid.update();
            //System.out.println(knn.getKnowledgeNodes());
            // knn.deleteKnowledgeNode(kn.getInputTag());
            System.out.println(knn.getKnowledgeNodes());
        } catch (KnowledgeNodeParseException e) {
            System.out.println("Error: invalid form of KnowledgeNode");
        }

        try {
            KnowledgeNode kn = new KnowledgeNode(newdata);
            knn.addKnowledgeNode(kn);

            //TODO UPDATE LOCAL Map
            l1.updateKnowledgeNode(kn);
            System.out.println("l1" + l1.getKnowledgeNode());
            l1.update();
            //System.out.println(knn.getKnowledgeNodes());
            // knn.deleteKnowledgeNode(kn.getInputTag());
//            newGrid.updateKN(kn);
//            newGrid.update();
            System.out.println(knn.getKnowledgeNodes());
        } catch (KnowledgeNodeParseException e) {
            System.out.println("Error: invalid form of KnowledgeNode");
        }

        System.out.println(knn.getKnowledgeNode(new Fact("WAYPOINT(x4, y4)")));


        for (int i = 1; i < 100; i++) {

            String[] datal = new String[i+1];
            datal[0] = "WAYPOINT(x3y4)";


            for (int j = 0; j < i; j++) {
                int x = (int) (Math.random()*1000);
                String Value = "Moveable(x1,y" + x + ")";
                datal[j+1] = Value;
            }
            try {
                KnowledgeNode kn = new KnowledgeNode(datal);
                knn.addKnowledgeNode(kn);
               // System.out.println(knn.getKnowledgeNodes());
                System.out.println(datal.length);
                System.out.println(knn.getKnowledgeNode(kn.getInputTag()).getOutputTags().size());
                // knn.deleteKnowledgeNode(kn.getInputTag());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Wait issue");
                }
                //System.out.println(knn.getKnowledgeNode(kn.getInputTag()));
            } catch (KnowledgeNodeParseException e) {
                System.out.println("Error: invalid form of KnowledgeNode");
            }

        }
    }
            //if(i==99){

            //}

//        System.out.println(knn.getKnowledgeNode(new Fact("WAYPOINT(x3y4)")));
public static void display(KnowledgeNode kn, int size){
    Tag    input  = kn.getInputTag();
    Set<Tag> output  =  kn.getOutputTags();
    JFrame mainFrame = new JFrame("Local Map" +input.simpleToString());
    mainFrame.setSize(1000,1000);
    mainFrame.setLayout(new GridLayout(1, 1));									//Initializing the window

    JPanel grid = new JPanel();													//Initializing the Jpanel
    grid.setLayout(new GridLayout(size, size));														//and placing it in the window
    mainFrame.add(grid);

    String[][] WorldArray= new String[size][size];
    JLabel[][] LabelArray =new JLabel[size][size];
    int x_w = ParseFact((Fact) input)[0];
    int y_w = ParseFact((Fact) input)[1];
    for(Tag  a:  output){
        int x = ParseFact((Fact) a)[0]-x_w+size/2;
        int y = ParseFact((Fact) a)[1]-y_w+size/2;
        WorldArray[y][x] = ((Fact) a).getPredicateName();
    }




        for(int i = 0; i < WorldArray.length; i++){
            for(int j = 0; j < WorldArray[0].length; j++){					//Iterating through the 2D array of objects

                JLabel currentObject = new JLabel("",SwingConstants.CENTER);
                String currentTokenS = "";
                if(WorldArray[i][j] ==null){															//If the current cell is empty
                    currentObject.setBackground(Color.white);					//but the text is blank (no token)
                    currentObject.setOpaque(true);
                    //currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
                }
                else if (WorldArray[i][j].equals("Immovable") || WorldArray[i][j].equals("Moveable")||WorldArray[i][j].equals("Nothing")){
                    //If the current cell hosts any of our 3 objects

                    char currentTokenC = WorldArray[i][j].charAt(0);
                    currentTokenS += Character.toString(currentTokenC);
                    currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
                    currentObject.setBackground(Color.white);
                    if(WorldArray[i][j].equals("Immovable")){currentObject.setBackground(Color.darkGray);}
                    if(WorldArray[i][j].equals("Moveable")){currentObject.setBackground(Color.pink);}
                    currentObject.setOpaque(true);								//Aesthetic additions to make the world more grid-like
                }


                currentObject.setText(currentTokenS);
                LabelArray[i][j] = currentObject;						//Insert the current JLabel in our 2D array of labels
                grid.add(LabelArray[i][j]);							//Then also place it into the cell of the JPanel corresponding with the cell in the 2D array of objects


            }
        }
        mainFrame.setVisible(true);  // Activate window
    }

    public static int[] ParseFact(Fact f){
        List<Argument> a =f.getArguments();
        int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""));
        int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""));
        int[] v = {x,y};
        return v;
    }


}





