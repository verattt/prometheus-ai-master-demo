//package meta;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//public class META_Utility {
//    private static final int maxdistance=1000000;
//
//    //return a list of globalmaps(each globalsmap node is a localmap)
//    public static ArrayList<GlobalMap> getAllGlobals(GlobalMap Gnode) {
//        ArrayList<GlobalMap> allGlobals=new ArrayList<>();
//        allGlobals.add(Gnode);
//        ArrayList<GlobalMap> Globalsleft=new ArrayList<>();
//        Globalsleft.add(Gnode);
//
//        while (Globalsleft.size()!=0){
//            Gnode=Globalsleft.get(0);
//            ArrayList<Edge> edges= Gnode.getEdges();
//            for (Edge edge: edges){
//                GlobalMap Gmap=edge.getGlobalStart();
//                if (!allGlobals.contains(Gmap))
//                {
//                    allGlobals.add(Gmap);
//                    Globalsleft.add(Gmap);
//                }
//                Gmap=edge.getGlobalDestination();
//                if (!allGlobals.contains(Gmap))
//                {
//                    allGlobals.add(Gmap);
//                    Globalsleft.add(Gmap);
//                }
//            }
//            Globalsleft.remove(0);
//        }
//        return allGlobals;
//    }
//
//    public static ArrayList<LocalMap> getAllLocals(ArrayList<GlobalMap> AllGlobals) {
//        ArrayList<LocalMap> AllLocals=new ArrayList<>();
//        ArrayList<LocalMap> Localsleft=new ArrayList<>();
//
//        for (GlobalMap Gnode : AllGlobals){
//            LocalMap Lnode=Gnode.getLocalMapRoot();
//            if (!AllLocals.contains(Lnode))
//            {
//                AllLocals.add(Lnode);
//                Localsleft.add(Lnode);
//            }
//            while (Localsleft.size() != 0) {
//                Lnode = Localsleft.get(0);
//                ArrayList<Edge> edges = Lnode.getEdges();
//                for (Edge edge : edges) {
//                    LocalMap Lmap = edge.getStart();
//                    if (!AllLocals.contains(Lmap)) {
//                        AllLocals.add(Lmap);
//                        Localsleft.add(Lmap);
//                    }
//                    Lmap = edge.getDestination();
//                    if (!AllLocals.contains(Lmap)) {
//                        AllLocals.add(Lmap);
//                        Localsleft.add(Lmap);
//                    }
//                }
//                Localsleft.remove(0);
//            }
//        }
//        return AllLocals;
//    }
//
//    //bug!!!! expected :19 edges, get:38edges
//    public static ArrayList<Edge> getAllEdges(ArrayList<LocalMap> AllLocals){
//        ArrayList<Edge> Alledges=new ArrayList<Edge>();
//        HashSet <Edge> ESet = new HashSet<Edge>();
//        for (LocalMap localmap:AllLocals){
//            ESet.addAll(localmap.getEdges());
//        }
//        Alledges.addAll(ESet);
//        return Alledges;
//    }
//
//    public static ArrayList<LocalMap> FindAllGoal(ArrayList<LocalMap> Locals,ArrayList<Goal> goals) {
//        ArrayList<LocalMap> result=new ArrayList<>();
//        for (LocalMap local : Locals){
//            Node node=local.getNode();
//            for (Goal goal:goals){
//                if (goal.getGoal()==node){
//                    result.add(local);
//                    local.setAssociatedGoal(goal);
//                    break;
//                }
//            }
//        }
//        return result;
//    }
//
//    /** Calcualte the shortest distance between all pairs of nodes in the whole graph
//     * floyd warshall algorithm; algorithm taken from below:
//     https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/
//     https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
//     **/
//    public static double[][] AllPairsShortestD(ArrayList<Edge> alledges,ArrayList<LocalMap> alllocalmaps){
//        int numVertices=alllocalmaps.size();
//
//        double[][] dist=new double[numVertices][numVertices];
//
//        for (int i=0;i<numVertices;i++){
//            for (int j=0;j<numVertices;j++){
//                dist[i][j]=maxdistance;
//            }
//        }
//        for (int i=0;i<numVertices;i++){
//            dist[i][i]=0;
//        }
//        for (int i=0;i<alledges.size();i++){
//            Edge currentedge =  alledges.get(i);
//            int startindex=alllocalmaps.indexOf(currentedge.getStart());
//            int endindex=alllocalmaps.indexOf(currentedge.getDestination());
//            dist[startindex][endindex]=currentedge.getWeight();
//        }
//        for (int k=0;k<numVertices;k++)
//        {
//            for (int i=0;i<numVertices;i++){
//                for (int j=0;j<numVertices;j++){
//                    if (dist[i][j]>dist[i][k]+dist[k][j]){
//                        dist[i][j]=dist[i][k]+dist[k][j];
//                    }
//                }
//            }
//        }
//        return dist;
//    }
//
//    //shortest distance and its path
//    public static double[][][] AllPairsShortestDandP(ArrayList<Edge> alledges,ArrayList<LocalMap> alllocalmaps){
//       // System.out.println("entered shortest distance method");
//        int numVertices=alllocalmaps.size();
//
//        double[][][] dist=new double[2][numVertices][numVertices];
//
//        for (int i=0;i<numVertices;i++){
//            for (int j=0;j<numVertices;j++){
//                dist[0][i][j]=maxdistance;
//                dist[1][i][j]=j;
//            }
//        }
//      //  System.out.println("maxdistance and j done");
//
//        for (int i=0;i<numVertices;i++){
//            dist[0][i][i]=0;
//            dist[1][i][i]=-1;
//        }
//      //  System.out.println("Start setting intial distance");
//      //  System.out.println("alledgesize="+alledges.size());
//        for (int i=0;i<alledges.size();i++) {
//            Edge currentedge = alledges.get(i);
//         //   System.out.println("Start="+currentedge.getStart().getNode().getObject()+"Eng "+currentedge.getDestination().getNode().getObject());
//            int startindex = alllocalmaps.indexOf(currentedge.getStart());
//            int endindex = alllocalmaps.indexOf(currentedge.getDestination());
//           // System.out.println("Startindex="+startindex);
//         //   System.out.println("endindex="+endindex);
//            dist[0][startindex][endindex] = currentedge.getWeight();
//            dist[1][startindex][endindex] = endindex;
//
//            dist[0][endindex][startindex] = currentedge.getWeight();
//            dist[1][endindex][startindex] = startindex;
//
//            //System.out.println("dist="+currentedge.getWeight());
//        }
//
//        for (int k=0;k<numVertices;k++)
//        {
//            for (int i=0;i<numVertices;i++){
//                for (int j=0;j<numVertices;j++){
//                    if (dist[0][i][j]>dist[0][i][k]+dist[0][k][j]){
//                        dist[0][i][j]=dist[0][i][k]+dist[0][k][j];
//                        dist[1][i][j]=dist[1][i][k];
//                    }
//                }
//            }
//        }
//       // System.out.println("All done");
//       // printArray(dist[0]);
//       // printArray(dist[1]);
//        return dist;
//    }
//
//    public static int[][] doubletoint(double[][] next){
//        int[][] next_int=new int[next.length][next[0].length];
//        for (int i=0;i<next.length;i++){
//            for (int j=0;j<next[0].length;j++){
//                next_int[i][j]= (int) next[i][j];
//            }
//        }
//        return next_int;
//    }
//
//    public static ArrayList<Integer> PathfromAllPairsShortestDandP(int u,int v,int[][] next){
//        ArrayList<Integer> path = new ArrayList<Integer>();
//        if (next[u][v]==-1){
//            return path;
//        }
//        path.add(u);
//        while (u!=v){
//            u=next[u][v];
//            path.add(u);
//        }
//        return path;
//    }
//
//    public static int[] Find2V(ArrayList<HashSet<LocalMap>> VSet, LocalMap v1, LocalMap v2){
//        int[] result=new int[2];
//        for (int i=0;i< VSet.size();i++){
//            if (VSet.get(i).contains(v1)){
//                result[0]=i;
//            }
//            if (VSet.get(i).contains(v2)){
//                result[1]=i;
//            }
//        }
//        return result;
//    }
//
//    public static void setAllEdgescToLocalMaps(ArrayList<LocalMap> goals,ArrayList<Edge_Compressed> edges_c){
//        for (int i=0;i<edges_c.size();i++){
//            Edge_Compressed edge_c_temp =  edges_c.get(i);
//            edge_c_temp.getDestination().addEdges_c(edge_c_temp);
//            edge_c_temp.getStart().addEdges_c(edge_c_temp);
//        }
//    }
//
//    //plz always check StartingPoint.getEdges_c().size!=0 before calling this method
//    public static Edge_Compressed FindMaxPbranch(LocalMap StartingPoint){
//      //  System.out.println("in FindMaxBranch");
//        ArrayList<Edge_Compressed> branches=StartingPoint.getEdges_c();
//       //System.out.println(branches.size());
//        int maxP=0;
//        int maxBranchindex=0;
//        for (int i=0;i<branches.size();i++) {
//            Edge_Compressed branch=branches.get(i);
//            if (!branch.getDestination().equals(StartingPoint)){
//           //     System.out.println("1stif");
//
//                int P= branch.getDestination().getAssoicatedGoal().getPriority();
//            //    System.out.println("before dfs P="+P);
//                P+=DFSOnPriority(branch.getDestination(),StartingPoint);
//            //    System.out.println("After dfs P="+P);
//                if (P>maxP){
//                    maxBranchindex=i;
//                    maxP=P;
//                }
//            }else if (!branch.getStart().equals(StartingPoint)){
//           //     System.out.println("2ndif");
//                int P=branch.getStart().getAssoicatedGoal().getPriority();
//           //     System.out.println("before dfs P="+P);
//                P+=DFSOnPriority(branch.getStart(),StartingPoint);
//            //    System.out.println("After dfs P="+P);
//                if (P>maxP){
//                    maxBranchindex=i;
//                    maxP=P;
//                }
//            }
//        }
//        //System.out.println(maxP);
//      //  System.out.println("maxBranchindex"+maxBranchindex);
//        return branches.get(maxBranchindex);
//    }
//
//    public static int DFSOnPriority(LocalMap StartingPoint,LocalMap pre){
//      //  System.out.println("insideDFS");
//        ArrayList<Edge_Compressed> branches=StartingPoint.getEdges_c();
//        if (branches.size()==0)
//            return 0;
//        if (branches.size()==1 && branches.get(0).equals(pre))
//            return 0;
//        int maxP=0;
//        Edge_Compressed maxbranch=branches.get(0);
//        for (Edge_Compressed branch:branches){
//            if (branch.getDestination().equals(pre) || branch.getStart().equals(pre)) continue;
//            if (!branch.getDestination().equals(StartingPoint)){
//                int P= branch.getDestination().getAssoicatedGoal().getPriority();
//                P+=DFSOnPriority(branch.getDestination(),StartingPoint);
//                if (P>maxP){
//                    maxbranch=branch;
//                    maxP=P;
//                }
//            }else if (!branch.getStart().equals(StartingPoint)){
//                int P= branch.getDestination().getAssoicatedGoal().getPriority();
//                P+=DFSOnPriority(branch.getStart(),StartingPoint);
//                if (P>maxP){
//                    maxbranch=branch;
//                    maxP=P;
//                }
//            }
//        }
//        return maxP;
//    }
//
//    public static ArrayList<LocalMap>  getALlBatterys(ArrayList<LocalMap> Localmaps){
//        ArrayList<LocalMap> batterys= new ArrayList<>();
//        for (LocalMap local:Localmaps){
//            if ( local.getNode().getType()== NodeType.BATTERY){
//                batterys.add(local);
//            }
//        }
//        return batterys;
//    }
//
//    public static int[] getAllBatterysIndex(ArrayList<LocalMap> AllLocalMaps,ArrayList<LocalMap> Batterys){
//        int[] index=new int[Batterys.size()];
//        for (int i=0;i<Batterys.size();i++){
//            index[i]=AllLocalMaps.indexOf(Batterys.get(i));
//        }
//        return index;
//    }
//
//    public static LocalMap  getClosestBattery(ArrayList<LocalMap> AllLocalMaps,ArrayList<LocalMap> Batterys,LocalMap currentposition, double[][] distall){
//        int[] index=getAllBatterysIndex(AllLocalMaps,Batterys);
//        int indexcurrentPosition =AllLocalMaps.indexOf(currentposition);
//        double minDistance=maxdistance;
//        int minBattery=0;
//        for (int i=0;i<index.length;i++){
//            if (distall[indexcurrentPosition][index[i]]<minDistance){
//                minDistance=distall[indexcurrentPosition][index[i]];
//                minBattery=index[i];
//            }
//        }
//        return AllLocalMaps.get(minBattery);
//    }
//
//    public static double  getClosestBatteryDistance(ArrayList<LocalMap> AllLocalMaps,LocalMap battery,LocalMap currentposition, double[][] distall){
//        int batteryindex=AllLocalMaps.indexOf(battery);
//        int indexcurrentPosition =AllLocalMaps.indexOf(currentposition);
//        return distall[indexcurrentPosition][batteryindex];
//    }
//
//    public static LocalMap LocalmapFromEdge(Edge_Compressed edgec,LocalMap localmap){
//        if (edgec.getStart()!=localmap){
//            return edgec.getStart();
//        }else {
//            return edgec.getDestination();
//        }
//    }
//
//    public static ArrayList<Edge_Compressed> cloneListEdge_Compressed(ArrayList<Edge_Compressed> edges_c) {
//        ArrayList<Edge_Compressed> clonedList = new ArrayList<Edge_Compressed>(edges_c.size());
//        for (Edge_Compressed edge_c : edges_c) {
//            clonedList.add(new Edge_Compressed(edge_c));
//        }
//        return clonedList;
//    }
//
//    public static ArrayList<LocalMap> cloneListLocalMap(ArrayList<LocalMap> localmaps) {
//        ArrayList<LocalMap> clonedList = new ArrayList<LocalMap>(localmaps.size());
//        for (LocalMap localmap : localmaps) {
//            clonedList.add(new LocalMap(localmap));
//        }
//        return clonedList;
//    }
//
//    public static void printArray(double[][] dist){
//        System.out.println("Print dist[][] array");
//        for (int i=0;i<dist.length-1;i++){
//            for (int j=0;j<dist[0].length-1;j++){
//                System.out.print(dist[i][j]+"  ");
//            }
//            System.out.println();
//        }
//    }
//}
