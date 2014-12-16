import java.util.ArrayList;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */

public class Trie {
    public static class TrieNode {
        private TrieNode fail = null;

        public int getNodeId() {
            return nodeId;
        }

        private int nodeId;

        private ArrayList<Integer> end = new ArrayList<>();
        private TrieNode[] children = new TrieNode[4];

        public ArrayList<Integer> getEnd() {
            return end;
        }

        public TrieNode(int nodeId) {
            this.nodeId = nodeId;
        }

        public TrieNode getFail() {
            return fail;
        }

        public void setFail(TrieNode fail) {
            this.fail = fail;
        }


        public void addNewEndIndex(int index) {
            end.add(index);
        }


        public TrieNode getChild(int i) {
            return children[i];
        }

        public void setChild(int i, TrieNode t) {
            children[i] = t;
        }

        public static int getIndex(char ch) {
            switch (ch){
                case 'A': return 0;
                case 'T': return 1;
                case 'C': return 2;
                case 'G': return 3;
            }

            return -1;
        }

        public boolean isRoot() {
            return nodeId == 0;
        }
    }

    public TrieNode getRoot() {
        return root;
    }

    private TrieNode root = new TrieNode(0);
    private int nNodeId = 1;

    public void insert(String s, int index) {
        TrieNode p = root;
        for(int i = 0;i < s.length();i ++) {
            char ch = s.charAt(i);
            int chIdx = TrieNode.getIndex(ch);

            if(p.children[chIdx] == null) {
                p.children[chIdx] = new TrieNode(nNodeId);
                nNodeId ++;
            }
            p = p.children[chIdx];
        }

        p.addNewEndIndex(index);
    }

    public int getTotalNode() {
        return nNodeId;
    }
}
