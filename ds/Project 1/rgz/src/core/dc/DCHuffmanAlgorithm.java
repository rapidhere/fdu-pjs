package core.dc;

import excs.DCUpdateException;
import excs.FirstChuckSizeTooSmall;

import java.util.*;

/**
 * Copyright : all rights reserved,rapidhere@gmail.com
 * Mail: rapidhere@gmail.com
 * Class :
 * Version :
 * Usage :
 */

public class DCHuffmanAlgorithm extends DCAlgorithm {
    @Override
    public DCCompressAlgorithm getCompressAlgorithm() {
        return new DCHuffmanCompressAlgorithm();
    }

    @Override
    public DCDecompressAlgorithm getDecompressAlgorithm() {
        return null;
    }
}

class DCHuffmanCompressAlgorithm implements DCCompressAlgorithm {
    private long dcSize = 0;
    private int sampleLength = 8;
    private Queue<HuffmanTreeNode> heap =
        new PriorityQueue<HuffmanTreeNode>(7, HuffmanTreeNode.getComparator());
    private byte[] remains;
    private Map<Long, HuffmanTreeNode> labels = new HashMap<Long, HuffmanTreeNode>();

    private static final int maxSampleLength = 8;
    private static final int minFirstChuckSize = 64;


    public DCHuffmanCompressAlgorithm() {
        clean();
    }

    /**
     * Clean up updated data
     */
    private void clean() {
        dcSize = 0;
        heap.clear();
        remains = new byte[0];
        labels.clear();
    }

    /**
     * is this updated chuck the first chuck?
     * @return true of false
     */
    private boolean isFirstChuck() {
        return dcSize == 0;
    }

    /**
     * Set the sample size of the algorithm
     * Sample size is the byte to make labels when update data
     * NOTE: use this function will cause updated data clean
     * @param size the length of new sample length
     */
    public void setSampleLength(int size) {
        assert(size >= 0 && size <= maxSampleLength);
        sampleLength = size;
        clean();
    }

    /**
     * requires: first chuck size is bigger than 64bits
     * @param bytes the bytes to update
     */
    @Override
    public void update(byte[] bytes) throws DCUpdateException {
        if(isFirstChuck() && bytes.length < minFirstChuckSize) {
            throw new FirstChuckSizeTooSmall(bytes.length, minFirstChuckSize);
        }

        // create new buffer
        byte[] buffer = new byte[bytes.length + remains.length];

        // copy it into buffer
        System.arraycopy(remains, 0, buffer, 0, remains.length);
        System.arraycopy(bytes, 0, buffer, remains.length, bytes.length);

        // loop up buffer
        for(int i = 0;i * sampleLength <= buffer.length;i ++) {
            // calculate current label
            long label = 0;
            for(int j = 0;j < sampleLength;j ++) {
                label = (label << 8) | (buffer[j + i * sampleLength]);
            }

            // add label into record
            if(! labels.containsKey(label)) {
                labels.put(label, new HuffmanTreeNode(label, null, null));
            }
            labels.get(label).incFrequency();
        }

        // set remains
        int remainLength = buffer.length % sampleLength;
        remains = new byte[remainLength];
        System.arraycopy(buffer, buffer.length - sampleLength, remains, 0, sampleLength);
    }

    /**
     * Compress the data, return compressed byte, include dc info
     * @return compressed byte
     */
    @Override
    public byte[] compress() {
        // add labels into heap
        heap.addAll(labels.values());

        while (heap.size() > 1) {
            // get two smallest node
            HuffmanTreeNode h1 = heap.poll(),
                h2 = heap.poll();

            // merge with a new node
            HuffmanTreeNode h = new HuffmanTreeNode(0, h1, h2);
            h.setFrequency(h1.getFrequency() + h2.getFrequency());

            // add it into heap
            heap.add(h);
        }

        clean();
        return null;
    }
}

class HuffmanTreeNode {
    /**
     * Get the Comparator of HuffmanTreeNode
     * @return the Comparator
     */
    public static Comparator<HuffmanTreeNode> getComparator() {
        return new Comparator<HuffmanTreeNode>() {
            @Override
            public int compare(HuffmanTreeNode t1, HuffmanTreeNode t2) {
                if(t1.getFrequency() < t2.getFrequency())
                    return -1;
                else if(t1.getFrequency() > t2.getFrequency())
                    return 1;
                return 0;
            }
        };
    }

    /**
     * check current node is leaf
     * @return true or false
     */
    public boolean isLeaf() {
        return getLeftTree() == null && getRightTree() == null;
    }
    /**
     * get the label of current node
     * when the node is not a leaf, the return value of this function has no meaning
     * @return the label
     */
    public long getLabel() {
        return label;
    }

    /**
     * set the label of current node
     * @param label the label to set
     */
    public void setLabel(long label) {
        this.label = label;
    }

    /**
     * get the left tree, return null if don't have
     * @return left tree
     */
    public HuffmanTreeNode getLeftTree() {
        return leftTree;
    }

    /**
     * set the leftTree of current node
     * @param leftTree the left tree
     */
    public void setLeftTree(HuffmanTreeNode leftTree) {
        this.leftTree = leftTree;
    }

    /**
     * get the right tree of current node, return null if don't have
     * @return right tree
     */
    public HuffmanTreeNode getRightTree() {
        return rightTree;
    }

    /**
     * set the right tree of current node
     * @param rightTree the right tree
     */
    public void setRightTree(HuffmanTreeNode rightTree) {
        this.rightTree = rightTree;
    }

    /**
     * set the frequency of current node
     * @param fre the new frequency
     */
    public void setFrequency(int fre) {
        frequency = fre;
    }

    /**
     * get the current frequency
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * inc the frequency by 1
     */
    public void incFrequency() {
        frequency ++;
    }

    private long label;
    private int frequency;
    private HuffmanTreeNode leftTree, rightTree;

    public HuffmanTreeNode(long label, HuffmanTreeNode leftTree, HuffmanTreeNode rightTree) {
        setLabel(label);
        setLeftTree(leftTree);
        setRightTree(rightTree);
    }
}