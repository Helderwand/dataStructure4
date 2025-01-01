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
public class TestClass {
    public static void main(String[] args) {

        TreeNode deneme1  = new TreeNode() ;

        deneme1.readData();
        deneme1.BFS("PROBLEM2");
        deneme1.DFS(0,"PROBLEM2");
        deneme1.JTree();
        deneme1.moveNode();
    }
}
