package cabbagegl;

import java.util.*;

// whoa

class KDTree {
    public static KDTree buildTree(List<Vector3> pts) {
        List<Vector3> ptsCpy = new ArrayList(pts);
        return new KDTree(buildTree(ptsCpy, 0));
    }

    private static KDNode buildTree(List<Vector3> pts, int depth) {
        KDNode retNode;
        if (pts.isEmpty()) {
            retNode = null;
        } else {
            retNode = new KDNode();
            int axis = depth % 3;

            // Find the median of the input points on axis
            Collections.sort(pts, new VectorAxisCompare(axis));
            int median = pts.size() / 2;

            // Build subtrees using data to left and right of median
            retNode.location = pts.get(median);
            retNode.left = buildTree(pts.subList(0, median), depth + 1);
            retNode.right = buildTree(pts.subList(median + 1, pts.size()),
                    depth + 1);
        }

        return retNode;
    }
    
    private KDNode root;
    private KDTree(KDNode iroot) {
        root = iroot;
    }

    public Vector3 nearestNeighbor(Vector3 point) {
        // TODO
        return Vector3.ZERO;
    }

    public List<Vector3> nearestNeighbors(Vector3 point, int k) {
        // TODO
        List<Vector3> nearest = new ArrayList<Vector3>();

        return nearest;
    }


    private static class KDNode {
        public KDNode left;
        public KDNode right;
        public Vector3 location;
    }

    private static class VectorAxisCompare implements Comparator<Vector3> {
        private int axis;
        public VectorAxisCompare(int iaxis) {
            axis = iaxis;
        }

        public int compare(Vector3 a, Vector3 b) {
            int ret = 0;
            double comp = a.get(axis) - b.get(axis);
            if (comp < 0) {
                ret = -1;
            } else if (comp > 0) {
                ret = 1;
            }
            return ret;
        }
    }
}
