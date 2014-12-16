import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 */
public class ACAutomaton {
    private Trie trie = new Trie();
    private int offset[];
    private boolean vis[];
    private int nPattern;

    public class SearchResult {
        public int getOffset() {
            return offset;
        }

        public int getPatternIndex() {
            return patternIndex;
        }

        public SearchResult(int o, int p) {
            offset = o;
            patternIndex = p;
        }

        private int offset;
        private int patternIndex;
    }

    public void insertPattern(String pattern, int patternIndex) {
        nPattern ++;
        trie.insert(pattern, patternIndex);
    }

    public void build() {
        Queue<Trie.TrieNode> q = new LinkedList<>();
        q.add(trie.getRoot());

        while(! q.isEmpty()) {
            Trie.TrieNode cur = q.poll();

            for(int i = 0;i < 4;i ++) {
                Trie.TrieNode ch = cur.getChild(i);
                if(ch == null) {
                    // no such child, point to fail
                    if(cur.isRoot()) {
                        cur.setChild(i, cur);
                    } else {
                        cur.setChild(i, cur.getFail().getChild(i));
                    }
                } else {
                    if(cur.isRoot()) {
                        ch.setFail(cur);
                    } else {
                        ch.setFail(cur.getFail().getChild(i));
                    }
                    q.add(ch);
                }
            }
        }

        offset = new int[nPattern];
        vis = new boolean[trie.getTotalNode()];
    }

    public ArrayList<SearchResult> search(String text) {
        Arrays.fill(offset, -1);
        Arrays.fill(vis, false);

        Trie.TrieNode p = trie.getRoot();
        for(int i = 0;i < text.length();i ++) {
            char ch = text.charAt(i);
            int idx = Trie.TrieNode.getIndex(ch);

            p = p.getChild(idx);

            Trie.TrieNode tmp = p;
            while(! tmp.isRoot() && ! vis[tmp.getNodeId()]) {
                vis[tmp.getNodeId()] = true;
                for(int e: tmp.getEnd())
                    offset[e] = i;
                tmp = tmp.getFail();
            }
        }

        ArrayList<SearchResult> ret = new ArrayList<>();
        for(int i = 0;i < nPattern;i ++)
            if(offset[i] != -1)
                ret.add(new SearchResult(offset[i], i));

        return ret;
    }

    public int getTrieSize() {
        return trie.getTotalNode();
    }
}
