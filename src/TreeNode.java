import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class TreeNode {
    private String data;
    Map<String, Map<String, Map<String, Map<String, TreeNode>>>> sections = new HashMap<>();// I am using this for 4 sections (Year,Course,Lecture,Problem)
    private Map<String, TreeNode> children;
    public TreeNode(String data) {
        this.data = data;
        this.children = new HashMap<>();
    }
    public TreeNode() {
        this.children = new HashMap<>();
    }
    public void setData(String myData) {
        this.data = myData;
    }

    public String getData() {
        return data;
    }
    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }
    public Map<String, TreeNode> getChildren() {
        return children;
    }
    public void addChild(TreeNode child) {
        children.put(child.getData(), child);
    }

    public void readData(){//file name should be “tree.txt”
        try{// I am Using try-catch for exceptionHandling
            File file = new File("tree.txt");
            Scanner fread = new Scanner(file);

            while (fread.hasNextLine()) {
                String line = fread.nextLine();
                String[] data = line.split(";");// ı am storing the datas in the data array as string .(split with ";")

                // Add the 1st node(root)
                String year = data[0];
                if (!sections.containsKey(year)) {
                    sections.put(year, new HashMap<>());
                }

                // Add the 2nd level node
                String className = null;
                if (data.length > 1) {
                    className = data[1];
                    if (!sections.get(year).containsKey(className)) {
                        sections.get(year).put(className, new HashMap<>());
                    }
                }

                // Add the 3rd level node
                String lecture = null;
                if (data.length > 2) {
                    lecture = data[2];
                    if (!sections.get(year).get(className).containsKey(lecture)) {
                        sections.get(year).get(className).put(lecture, new HashMap<>());
                    }
                }

                // Add the 4th level node
                if (data.length > 3) {
                    String problem = data[3];
                    if (!sections.get(year).get(className).get(lecture).containsKey(problem)) {
                        sections.get(year).get(className).get(lecture).put(problem, new TreeNode(problem));
                    }
                }
                // Connect the nodes
                if (data.length > 1) {
                    TreeNode parentNode = (TreeNode) sections.get(year).get(className);
                    if (data.length > 2) {
                        parentNode = (TreeNode) sections.get(year).get(className).get(lecture);
                    }
                    if (data.length > 3) {
                        String problem = data[3];
                        TreeNode childNode = sections.get(year).get(className).get(lecture).get(problem);
                        parentNode.addChild(childNode);
                    }
                }
            }
        }
        catch(FileNotFoundException exceptionn){
            exceptionn.printStackTrace();
        }
    }
    public JTree JTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");

        for (String year : sections.keySet()) {
            DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(year);

            for (String className : sections.get(year).keySet()) {
                DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(className);

                for (String lecture : sections.get(year).get(className).keySet()) {
                    DefaultMutableTreeNode lectureNode = new DefaultMutableTreeNode(lecture);

                    for (String problem : sections.get(year).get(className).get(lecture).keySet()) {
                        DefaultMutableTreeNode problemNode = new DefaultMutableTreeNode(problem);
                        lectureNode.add(problemNode);
                    }

                    classNode.add(lectureNode);
                }

                yearNode.add(classNode);
            }

            rootNode.add(yearNode);
        }

        JTree tree = new JTree(rootNode);
        return tree;
    }
    public void printTree() {
        if (children.isEmpty()) {
            // Base case: if the node has no children, just print its data
            System.out.println(data);
        } else {
            // Recursive case: print the data of this node and all its children
            System.out.println(data);
            for (TreeNode child : children.values()) {
                child.printTree();
            }
        }
    }

    public boolean DFS(int countSteps, String compareData) {
        boolean found = false;
        if (compareData.equals(data)) {//It is my base case , ıf code enter this we can sure we found the comparing data in our tree, ıf it doesn't enter it will continue with recurwsion untill the end
            System.out.println("Step\t" + countSteps + "->" + data + "\tFOUND!!");
            return true;
        } else {
            System.out.println("Step\t" + countSteps + "->" + data);
        }
        for (TreeNode child : children.values()) {
            countSteps++;
            found = child.DFS(countSteps, compareData);
            if (found) {
                return true;
            }
        }
        System.out.println("Step\t" + countSteps + "->" + data + "\tNOT FOUND!!!");
        return false;
    }
    public boolean BFS(String compareData) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(this);
        int countSteps = 0;

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            countSteps++;

            if (compareData.equals(current.data)) {
                System.out.println("Step\t" + countSteps + "->" + current.data + "\tFOUND!!");
                return true;
            } else {
                System.out.println("Step\t" + countSteps + "->" + current.data);
            }

            for (TreeNode child : current.children.values()) {
                queue.offer(child);
            }
        }

        System.out.println("Step\t" + countSteps + "->" + data + "\tNOT FOUND!!!");
        return false;
    }
    public boolean postOrder(int countSteps, String compareData) {
        boolean found = false;
        for (TreeNode child : children.values()) {
            countSteps++;
            found = child.postOrder(countSteps, compareData);
            if (found) {
                break;
            }
        }
        countSteps++;
        if (compareData.equals(data)) {
            System.out.println("Step\t" + countSteps + "->" + data + "\tFOUND!!");
            return true;
        } else {
            System.out.println("Step\t" + countSteps + "->" + data);
        }
        return found;
    }

    public void moveNode() {
        Scanner scanner = new Scanner(System.in);// ı take inputs with scanner to 2D source input and one string for destination year
        String[][] inputs = new String[3][20];
        String line;
        int i = 0;
        System.out.println("Enter input Source 1st for Year 2nd for Course 3rd for lecture (use ':q' to exit): ");
        while (i < 3) {
            System.out.println("Enter " + (i+1) + ". input :");
            line = scanner.nextLine();
            if (line.equals(":q")) {
                break;
            }
            inputs[i] = line.split(" ");
            i++;
        }
        System.out.println("Enter destination year: ");
        String destYear = scanner.nextLine();
        scanner.close();

        String sourceYear = inputs[0][0];
        String sourceCourse = inputs[1][0];
        String sourceLecture = inputs[2][0];
        Map<String, TreeNode> sourceLectureMap = sections.get(sourceYear).get(sourceCourse).get(sourceLecture);
        if (sourceLectureMap == null) {
            System.out.println("Source node not found.");
            return;
        }
        TreeNode sourceNode = sourceLectureMap.remove("node");

        Map<String, Map<String, TreeNode>> sourceCourseMap = sections.get(sourceYear).get(sourceCourse);
        if (sourceCourseMap.isEmpty()) {
            sections.get(sourceYear).remove(sourceCourse);
            if (sections.get(sourceYear).isEmpty()) {
                sections.remove(sourceYear);
            }
        }

        Map<String, Map<String, Map<String, TreeNode>>> sourceYearMap = sections.get(sourceYear);
        if (sourceYearMap.isEmpty()) {
            sections.remove(sourceYear);
        }

        TreeNode destNode = searchNode(destYear, sourceCourse, sourceLecture);
        if (destNode != null) {
            System.out.println("Destination node already exists. Overwriting...");
            destNode.data = sourceNode.data;
        } else {
            Map<String, Map<String, Map<String, TreeNode>>> destYearMap = sections.get(destYear);
            if (destYearMap == null) {
                System.out.println("Creating destination year node: " + destYear);
                destYearMap = new HashMap<>();
                sections.put(destYear, destYearMap);
            }
            Map<String, Map<String, TreeNode>> destCourseMap = destYearMap.getOrDefault(sourceCourse, new HashMap<>());
            destCourseMap.put(sourceLecture, sourceLectureMap);
            destYearMap.put(sourceCourse, destCourseMap);
        }

        System.out.println("\nTree after moving the node:");
        this.JTree();
    }

    private TreeNode searchNode(String year, String course, String lecture) {// ı am creating this searching algorithm for using in move method.
        if (year == null) {
            return null;
        }
        Map<String, Map<String, Map<String, TreeNode>>> yearMap = sections.get(year);//ı assume that all of the year  has course and lecture and ı will check it . ı will do it to all of the courses and lectures too
        if (yearMap == null) {
            return null;
        }
        if (course == null) {
            Map<String, Map<String, TreeNode>> nullCourseMap = yearMap.get(null);
            if (nullCourseMap == null) {
                return null;
            }
            Map<String, TreeNode> nullLectureMap = nullCourseMap.get(null);
            if (nullLectureMap == null) {
                return null;
            }
            return nullLectureMap.get("node");
        }
        Map<String, Map<String, TreeNode>> courseMap = yearMap.get(course);
        if (courseMap == null) {
            return null;
        }
        if (lecture == null) {
            Map<String, TreeNode> nullLectureMap = courseMap.get(null);
            if (nullLectureMap == null) {
                return null;
            }
            return nullLectureMap.get("node");
        }
        Map<String, TreeNode> lectureMap = courseMap.get(lecture);
        if (lectureMap == null) {
            return null;
        }
        return lectureMap.get("node");
    }

}
