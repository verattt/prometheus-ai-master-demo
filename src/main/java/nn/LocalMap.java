package nn;

import knn.api.KnowledgeNode;
import meta.MapVisualizer;
import tags.Argument;
import tags.Fact;
import tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LocalMap implements Runnable {
    private KnowledgeNode kn;
    private int size;
    private JFrame mainFrame;
    private JPanel panel;

    public LocalMap(KnowledgeNode kn, int size){
        this.kn = kn;
        this.size =  size;
        this.mainFrame= new JFrame("Local Map"+kn.getInputTag().simpleToString());
        mainFrame.setSize(250,250);
        mainFrame.setLayout(new GridLayout(1, 1));

        //this is to set the position of the localMap, might be change from 100 to smaller number if globalmap is bigger
        int x  = ParseFact((Fact)kn.getInputTag())[0]*100;
        int y  = ParseFact((Fact)kn.getInputTag())[1]*100;

        mainFrame.setLocation(x, y);
    }

    public JPanel display(){
        Tag input  = kn.getInputTag();
        Set<Tag> output  =  kn.getOutputTags();
        //JFrame mainFrame = new JFrame("Local Map" +input.simpleToString());
        								//Initializing the window

        JPanel grid = new JPanel();													//Initializing the Jpanel
        grid.setLayout(new GridLayout(size, size));														//and placing it in the window


        String[][] WorldArray= new String[size][size];
        JLabel[][] LabelArray =new JLabel[size][size];
        int x_w = ParseFact((Fact) input)[0];
        int y_w = ParseFact((Fact) input)[1];
       // System.out.println(kn.getInputTag().simpleToString()+kn.getOutputTags());
        for(Tag  a:  output){
            int x = ParseFact((Fact) a)[0]-x_w+size/2;
            int y = ParseFact((Fact) a)[1]-y_w+size/2;
           // System.out.println(y +" " +x);
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

                if(i==size/2&&j==size/2){
                        currentObject.setBackground(Color.BLUE);
                }

                currentObject.setText(currentTokenS);
                LabelArray[i][j] = currentObject;//Insert the current JLabel in our 2D array of labels
                grid.add(LabelArray[i][j]);							//Then also place it into the cell of the JPanel corresponding with the cell in the 2D array of objects


            }
        }

        return grid;
    }

    public static int[] ParseFact(Fact f){
        List<Argument> a =f.getArguments();
        int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""));
        int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""));
        int[] v = {x,y};
        return v;
    }
    public void updateKnowledgeNode(KnowledgeNode kn1){
        this.kn = kn1;
    }

    @Override
    public void run() {
        this.panel  =  display();
        mainFrame.add(panel);
        mainFrame.setVisible(true);

    }

    public void update(){
        mainFrame.remove(panel);
        this.panel=display();
        mainFrame.add(panel);
        mainFrame.setVisible(true);



    }

    public void hiding(){
        mainFrame.setVisible(false);
    }
    public String getKnowledgeNode(){
        return this.kn.toString();
    }
    public void paint(Graphics g )
    {
        for ( int x = 30; x <= 300; x += 30 )
            for ( int y = 30; y <= 300; y += 30 )
                g.drawRect( x, y, 30, 30 );

    }
}
