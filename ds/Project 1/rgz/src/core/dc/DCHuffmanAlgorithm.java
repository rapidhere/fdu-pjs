package core.dc;

import excs.BitArrayException;
import excs.DCException;
import javafx.util.Pair;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * The huffman tree DC algorithm
 */

public class DCHuffmanAlgorithm<T extends Token> implements DCAlgorithm<T> {
    /**
     * used in huffman tree building
     */
    private interface TokenArgMerger {
        int merge(int key1, int key2);
    }

    /**
     * a small trick in put map
     */
    private class IncInteger {
        int val;

        IncInteger() {val = 0;}
    }

    /**
     * build the huffman tree with token pair list
     *
     * @param tokens pair list
     * @param tm how to merge two token pair
     * @return new huffman tree
     */
    static <T extends Token> HuffmanTreeNode<T>
    buildHuffmanTree(ArrayList<Pair<Integer, T>> tokens, TokenArgMerger tm) {
        // debug error: we assume that there always should be more than one token
        if(tokens.size() == 1) {
            return new HuffmanTreeNode<>(new HuffmanTreeLeaf<>(tokens.get(0).getValue()), null);
        }

        // create heap
        Queue<Pair<Integer, AbstractHuffmanTreeNode<T>>> heap =
            new PriorityQueue<>(7, (o1, o2) -> o1.getKey() - o2.getKey());

        // put tokens into heap
        for(Pair<Integer, T> t: tokens) {
            heap.add(new Pair<>(
                t.getKey(),
                new HuffmanTreeLeaf<>(t.getValue())));
        }

        // build heap
        while(heap.size() > 1) {
            Pair<Integer, AbstractHuffmanTreeNode<T>> p1 = heap.poll(),
                p2 = heap.poll();

            // new node
            HuffmanTreeNode<T> t = new HuffmanTreeNode<>(p1.getValue(), p2.getValue());

            // put into heap
            heap.add(new Pair<>(
                tm.merge(p1.getKey(), p2.getKey()),
                t));
        }

        // must be HuffmanTreeNode, not leaf
        return (HuffmanTreeNode<T>)heap.poll().getValue();
    }

    /**
     * Build huffman tree with ARG=frequency
     * @param frequency frequency array
     * @param tokens token array
     * @return new huffman tree
     */
    static <T extends Token> HuffmanTreeNode<T>
    buildHuffmanTreeWithFrequency(int []frequency, ArrayList<T> tokens) {
        assert frequency.length == tokens.size();

        ArrayList<Pair<Integer, T>> t = new ArrayList<>();
        for(int i = 0;i < frequency.length;i ++) {
            t.add(new Pair<>(frequency[i], tokens.get(i)));
        }

        return buildHuffmanTree(t, (key1, key2) -> key1 + key2);
    }

    /**
     * Build huffman tree with ARG=depth
     * @param depth depth array
     * @param tokens tokens array
     * @return new huffman tree
     */
    static <T extends Token> HuffmanTreeNode<T> buildHuffmanTreeWithDepth(int []depth, ArrayList<T> tokens) {
        assert depth.length == tokens.size();

        ArrayList<Pair<Integer, T>> t = new ArrayList<>();

        for(int i = 0;i < depth.length;i ++) {
            t.add(new Pair<>(-depth[i], tokens.get(i)));
        }

        return buildHuffmanTree(t, (key1, key2) -> {
            assert key1 == key2;
            return key1 + 1;
        });
    }

    /**
     * Get the encoding map: Token -> encoding String
     * @param root the huffman tree
     * @return encoding map
     */
    static <T extends Token> Map<T, String> getEncodingMap(HuffmanTreeNode<T> root) {
        Map<T, String> encodings = new HashMap<>();
        Queue<Pair<AbstractHuffmanTreeNode<T>, String>> queue =
            new LinkedBlockingQueue<>();

        queue.add(new Pair<>(root, ""));
        while(! queue.isEmpty()) {
            AbstractHuffmanTreeNode<T> curNode = queue.peek().getKey();
            String curEncode = queue.poll().getValue();

            if(curNode == null)
                continue;

            if(curNode.isLeaf()) {
                encodings.put(curNode.getToken(), curEncode);
            } else {
                queue.add(new Pair<>(curNode.getLeftTree(), curEncode + "0"));
                queue.add(new Pair<>(curNode.getRightTree(), curEncode + "1"));
            }
        }

        return encodings;
    }

    @Override
    public byte[] compress(ArrayList<T> tokenSequence, CatchAlgorithm<T> ca)
    throws DCException {
        Map<T, IncInteger> tokenMap = new HashMap<>();

        // count tokens
        for(T t: tokenSequence) {
            IncInteger cnt = tokenMap.get(t);
            if(cnt != null) {
                cnt.val ++;
            } else {
                IncInteger inc = new IncInteger();
                inc.val ++;
                tokenMap.put(t, inc);
            }
        }

        // build frequency array and tokens array
        ArrayList<T> tokens = new ArrayList<>();
        tokens.addAll(tokenMap.keySet());

        // get freq
        int freq[] = new int[tokens.size()];
        for(int i = 0;i < tokens.size();i ++)
            freq[i] = tokenMap.get(tokens.get(i)).val;

        // build tree
        HuffmanTreeNode<T> root = buildHuffmanTreeWithFrequency(freq, tokens);

        // get depth array
        Queue<Pair<AbstractHuffmanTreeNode<T>, Integer>> queue =
            new LinkedBlockingQueue<>();
        queue.add(new Pair<>(root, 0));

        Map<T, Integer> depthMap = new HashMap<>();
        while(! queue.isEmpty()) {
            AbstractHuffmanTreeNode<T> curNode = queue.peek().getKey();
            int curDepth = queue.poll().getValue();

            if(curNode == null) continue;

            if(curNode.isLeaf()) {
                depthMap.put(curNode.getToken(), curDepth);
            } else {
                queue.add(new Pair<>(
                    curNode.getLeftTree(), curDepth +1));
                queue.add(new Pair<>(
                    curNode.getRightTree(), curDepth +1));
            }
        }

        int[] depth = new int[tokens.size()];
        for(int i = 0;i < tokens.size();i ++)
            depth[i] = depthMap.get(tokens.get(i));

        // rebuild huffman tree
        root = buildHuffmanTreeWithDepth(depth, tokens);

        // build encoding map
        Map<T, String> rawEncodingMap = getEncodingMap(root);
        Map<T, UnjoinableBitArray> encodingMap = new HashMap<>();

        for(T t: rawEncodingMap.keySet()) {
            String rawEncode = rawEncodingMap.get(t);
            UnjoinableBitArray encode = new UnjoinableBitArray();
            for(int i = 0;i < rawEncode.length();i ++)
                encode.addBit((byte) (rawEncode.charAt(i) - '0'));
            encodingMap.put(t, encode);
        }

        // compress
        UnjoinableBitArray compressedData = new UnjoinableBitArray();
        for(T t: tokenSequence) {
            // get encode
            UnjoinableBitArray encode = encodingMap.get(t);

            // put into bit array
            compressedData.addBitArray(encode);
        }
        byte[] compressedBytes = compressedData.dump();

        // Create ret vector
        Vector<Byte> ret = new Vector<>();

        // dump huffman tree
        byte[] huffmanTreeBytes = ca.dump(tokens);

        // dump huffman tree depth, < 2 ^ 16
        byte[] huffmanTreeDepthBytes = new byte[tokens.size() * 2];
        for(int i = 0;i < tokens.size();i ++) {
            assert depth[i] < (1 << 16) && depth[i] >= 0;
            huffmanTreeDepthBytes[i * 2] = (byte)(depth[i] & 0xff);
            huffmanTreeDepthBytes[i * 2 + 1] = (byte)((depth[i] >> 8) & 0xff);
        }

        // add to ret
        // add huffman tree bytes
        for(byte b: huffmanTreeBytes) ret.add(b);
        // add depth bytes
        for(byte b: huffmanTreeDepthBytes) ret.add(b);
        // add compressed bytes
        for(byte b: compressedBytes) ret.add(b);

        // dump to bytes
        byte[] r = new byte[ret.size()];

        for(int i = 0;i < ret.size();i ++)
            r[i] = ret.get(i);

        return r;
    }

    @Override
    public ArrayList<T> decompress(byte[] bytes, int startOffset, int length, CatchAlgorithm<T> ca)
    throws DCException {
        // load tokens
        ArrayList<T> tokens;
        int offset;

        Pair<ArrayList<T>, Integer> p = ca.load(bytes, startOffset, length);
        tokens = p.getKey();
        offset = p.getValue();

        // load depth info
        int[] depth = new int[tokens.size()];
        for(int i = 0;i < tokens.size();i ++) {
            depth[i] = (bytes[offset] & 0xff) | (((bytes[offset + 1] & 0xff)<< 8));
            offset += 2;
        }
        // load compressed bits
        UnjoinableBitArray compressedData = new UnjoinableBitArray();
        try {
            compressedData.load(bytes, offset, bytes.length);
        } catch (BitArrayException e) {
            throw new DCException(e.getMessage());
        }

        // rebuild huffman tree
        HuffmanTreeNode<T> root = buildHuffmanTreeWithDepth(depth, tokens);

        // decompress
        ArrayList<T> ret = new ArrayList<>();
        AbstractHuffmanTreeNode<T> cur = root;

        for(int i = 0;i < compressedData.size();i ++) {
            // get bit
            byte bit = compressedData.get(i);

            assert bit == 0 || bit == 1;

            if(bit == 0) {
                cur = cur.getLeftTree();
            } else {
                cur = cur.getRightTree();
            }

            if(cur.isLeaf()) {
                ret.add(cur.getToken());
                cur = root;
            }
        }

        assert cur == root;

        return ret;
    }
}

/**
 * Huffman tress
 * @param <T> the token used in the huffman tree
 */
abstract class AbstractHuffmanTreeNode <T extends Token>{
    abstract public T getToken();
    abstract public AbstractHuffmanTreeNode<T> getLeftTree();
    abstract public AbstractHuffmanTreeNode<T> getRightTree();
    abstract public boolean isLeaf();
}

/**
 * the tree node, not leaf
 * @param <T> the token used in huffman tree
 */
class HuffmanTreeNode <T extends Token> extends AbstractHuffmanTreeNode <T> {
    AbstractHuffmanTreeNode<T> leftTree, rightTree;

    public HuffmanTreeNode(AbstractHuffmanTreeNode<T> leftTree, AbstractHuffmanTreeNode<T> rightTree) {
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    @Override
    public T getToken() {
        return null;
    }

    @Override
    public AbstractHuffmanTreeNode<T> getLeftTree() {
        return leftTree;
    }

    @Override
    public AbstractHuffmanTreeNode<T> getRightTree() {
        return rightTree;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}

/**
 * the tree leaf
 * @param <T> the token used by huffman tree
 */
class HuffmanTreeLeaf <T extends Token> extends AbstractHuffmanTreeNode <T> {
    T token;

    public HuffmanTreeLeaf(T token) {
        this.token = token;
    }

    @Override
    public T getToken() {
        return token;
    }

    @Override
    public AbstractHuffmanTreeNode<T> getLeftTree() {
        return null;
    }

    @Override
    public AbstractHuffmanTreeNode<T> getRightTree() {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}