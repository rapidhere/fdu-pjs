package core.dc;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class DCHuffmanAlgorithm implements DCAlgorithm {
    interface TokenArgMerger {
        int merge(int key1, int key2);
    }

    /**
     * build the huffman tree with token pair list
     * @param tokens pair list
     * @param tm how to merge two token pair
     * @return new huffman tree
     */
    static HuffmanTreeNode buildHuffmanTree(Pair<Integer, Token>[] tokens, TokenArgMerger tm) {
        // debug error: we assume that there always should be more than one token
        assert(tokens.length > 1);

        // create heap
        Queue<Pair<Integer, AbstractHuffmanTreeNode>> heap =
            new PriorityQueue<Pair<Integer, AbstractHuffmanTreeNode>>();

        // put tokens into heap
        for(Pair<Integer, Token> t: tokens) {
            heap.add(new Pair<Integer, AbstractHuffmanTreeNode>(
                t.getKey(),
                new HuffmanTreeLeaf(t.getValue())));
        }

        // build heap
        while(heap.size() > 1) {
            Pair<Integer, AbstractHuffmanTreeNode> p1 = heap.poll(),
                p2 = heap.poll();

            // new node
            HuffmanTreeNode t = new HuffmanTreeNode(p1.getValue(), p2.getValue());

            // put into heap
            heap.add(new Pair<Integer, AbstractHuffmanTreeNode>(
                tm.merge(p1.getKey(), p2.getKey()),
                t));
        }

        // must be HuffmanTreeNode, not leaf
        return (HuffmanTreeNode)heap.poll().getValue();
    }

    /**
     * Build huffman tree with ARG=frequency
     * @param frequency frequency array
     * @param tokens token array
     * @return new huffman tree
     */
    static HuffmanTreeNode buildHuffmanTreeWithFrequency(int []frequency, Token[] tokens) {
        assert(frequency.length == tokens.length);

        Pair<Integer, Token>[] t = new Pair[frequency.length];
        for(int i = 0;i < frequency.length;i ++) {
            t[i] = new Pair<Integer, Token>(-frequency[i], tokens[i]);
        }

        return buildHuffmanTree(t, new TokenArgMerger() {
            @Override
            public int merge(int key1, int key2) {
                return key1 + key2;
            }
        });
    }

    /**
     * Build huffman tree with ARG=depth
     * @param depth depth array
     * @param tokens tokens array
     * @return new huffman tree
     */
    static HuffmanTreeNode buildHuffmanTreeWithDepth(int []depth, Token[] tokens) {
        assert(depth.length == tokens.length);

        Pair<Integer, Token>[] t = new Pair[depth.length];

        for(int i = 0;i < depth.length;i ++) {
            t[i] = new Pair<Integer, Token>(-depth[i], tokens[i]);
        }

        return buildHuffmanTree(t, new TokenArgMerger() {
            @Override
            public int merge(int key1, int key2) {
                assert(key1 == key2);
                return key1 + 1;
            }
        });
    }

    /**
     * Get the encoding map: Token -> encoding String
     * @param root the huffman tree
     * @return encoding map
     */
    static Map<Token, String> getEncodingMap(HuffmanTreeNode root) {
        Map<Token, String> encodings = new HashMap<Token, String>();
        Queue<Pair<AbstractHuffmanTreeNode, String>> queue =
            new LinkedBlockingQueue<Pair<AbstractHuffmanTreeNode, String>>();

        queue.add(new Pair<AbstractHuffmanTreeNode, String>(root, ""));
        while(! queue.isEmpty()) {
            AbstractHuffmanTreeNode curNode = queue.peek().getKey();
            String curEncode = queue.poll().getValue();

            if(curNode.isLeaf()) {
                encodings.put(((HuffmanTreeLeaf)curNode).token, curEncode);
            } else {
                queue.add(new Pair<AbstractHuffmanTreeNode, String>(
                    ((HuffmanTreeNode)curNode).leftTree, curEncode + "0"));
                queue.add(new Pair<AbstractHuffmanTreeNode, String>(
                    ((HuffmanTreeNode)curNode).rightTree, curEncode + "1"));
            }
        }

        return encodings;
    }

    /**
     * get Decoding map: compressed data -> token
     * @param root the huffman tree
     * @return decoding map
     */
    static Map<String, Token> getDecodingMap(HuffmanTreeNode root) {
        Map<String, Token> decodings = new HashMap<String, Token>();
        Queue<Pair<AbstractHuffmanTreeNode, String>> queue =
            new LinkedBlockingQueue<Pair<AbstractHuffmanTreeNode, String>>();

        queue.add(new Pair<AbstractHuffmanTreeNode, String>(root, ""));
        while(! queue.isEmpty()) {
            AbstractHuffmanTreeNode curNode = queue.peek().getKey();
            String curEncode = queue.poll().getValue();

            if(curNode.isLeaf()) {
                decodings.put(curEncode, ((HuffmanTreeLeaf)curNode).token);
            } else {
                queue.add(new Pair<AbstractHuffmanTreeNode, String>(
                    ((HuffmanTreeNode)curNode).leftTree, curEncode + "0"));
                queue.add(new Pair<AbstractHuffmanTreeNode, String>(
                    ((HuffmanTreeNode)curNode).rightTree, curEncode + "1"));
            }
        }

        return decodings;
    }

    @Override
    public byte[] compress(Token[] tokenSequence) {
        Map<Token, Integer> tokenMap = new HashMap<Token, Integer>();

        // count tokens
        for(Token t: tokenSequence) {
            if(tokenMap.containsKey(t)) {
                tokenMap.put(t, tokenMap.get(t) + 1);
            } else {
                tokenMap.put(t, 1);
            }
        }

        // build frequency array and tokens array
        Token[] tokens = tokenMap.keySet().toArray(new Token[tokenMap.keySet().size()]);
        int[] freq = new int[tokens.length];
        for(int i = 0;i < tokens.length;i ++)
            freq[i] = tokenMap.get(tokens[i]);

        // build tree
        HuffmanTreeNode root = buildHuffmanTreeWithFrequency(freq, tokens);

        // get depth array
        Queue<Pair<AbstractHuffmanTreeNode, Integer>> queue =
            new LinkedBlockingQueue<Pair<AbstractHuffmanTreeNode, Integer>>();
        queue.add(new Pair<AbstractHuffmanTreeNode, Integer>(root, 0));

        while(! queue.isEmpty()) {
            AbstractHuffmanTreeNode curNode = queue.peek().getKey();
            int curDepth = queue.poll().getValue();

            if(curNode.isLeaf()) {
                tokenMap.put(((HuffmanTreeLeaf)curNode).token, curDepth);
            } else {
                queue.add(new Pair<AbstractHuffmanTreeNode, Integer>(
                    ((HuffmanTreeNode)curNode).leftTree, curDepth +1));
                queue.add(new Pair<AbstractHuffmanTreeNode, Integer>(
                    ((HuffmanTreeNode)curNode).rightTree, curDepth +1));
            }
        }

        int[] depth = new int[tokens.length];
        for(int i = 0;i < tokens.length;i ++)
            depth[i] = tokenMap.get(tokens[i]);

        // rebuild huffman tree
        root = buildHuffmanTreeWithDepth(depth, tokens);

        // build encoding map
        Map<Token, String> encodingMap = getEncodingMap(root);

        // compress
        BitArray compressedData = new BitArray();
        for(Token t: tokenSequence) {
            // get encode
            String encode = encodingMap.get(t);

            // put into bit array
            for(int i = 0;i < encode.length();i ++)
                compressedData.addBit((byte)(encode.charAt(i) - '0'));
        }
        byte[] compressedBytes = compressedData.dump();

        // Create ret vector
        Vector<Byte> ret = new Vector<Byte>();

        // dump huffman tree
        byte[] huffmanTreeBytes = tokens[0].dumpTokens(tokens);

        // dump huffman tree depth, < 2 ^ 16
        byte[] huffmanTreeDepthBytes = new byte[tokens.length * 2];
        for(int i = 0;i < tokens.length;i ++) {
            assert(depth[i] < (1 << 16) && depth[i] >= 0);
            huffmanTreeDepthBytes[i * 2] = (byte)(depth[i] & 0xff);
            huffmanTreeDepthBytes[i * 2 + 1] = (byte)((depth[i] >> 8) & 0xff);
        }

        // add to ret
        for(byte b: huffmanTreeBytes) ret.add(b);
        for(byte b: huffmanTreeDepthBytes) ret.add(b);
        for(byte b: compressedBytes) ret.add(b);

        // dump to bytes
        byte[] r = new byte[ret.size()];

        for(int i = 0;i < ret.size();i ++)
            r[i] = ret.get(i);
        return r;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return new byte[0];
    }
}

abstract class AbstractHuffmanTreeNode {
    abstract public boolean isLeaf();
}

class HuffmanTreeNode extends AbstractHuffmanTreeNode {
    AbstractHuffmanTreeNode leftTree, rightTree;

    public HuffmanTreeNode(AbstractHuffmanTreeNode leftTree, AbstractHuffmanTreeNode rightTree) {
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    public boolean isLeaf() {return false;}
}

class HuffmanTreeLeaf extends AbstractHuffmanTreeNode {
    Token token;

    public HuffmanTreeLeaf(Token token) {
        this.token = token;
    }

    public boolean isLeaf() {return true;}
}