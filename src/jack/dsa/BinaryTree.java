package jack.dsa;

//节点数据结构

//树结构
class BinaryTree {
    private Node root;

    public BinaryTree() {
        root = null;
    }
    class Node{
        public int data;
        public Node left;
        public Node right;
        public Node parent;
        public Node(int value){
            data = value;
            left = null;
            right = null;
            parent = null;
        }
    }
    //依据值寻找Node,若没有则返回父节点
    // 小于父节点，左子树查找；大于父节点，右子树查找；否则返回
    public Node find(int key) {
        Node current = root;
        while (current != null) {
            if (key < current.data) {
                //返回父节点
                if (current.left == null) {
                    return current;
                }
                current = current.left;
            } else if (key > current.data) {
                if (current.right == null) {
                    return current;
                }
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    //删除操作中用到的方法，用于查找被删除节点处的替换节点
    // 从右子树中查找能够替换被删除节点的值，其值应该为右子树中最小的值(遍历左子树)
    // 若当前节点无右子树，则返回当前节点；若有，则返回右子树中左子树的最小值
    public Node findSuccessor(Node n) {
        if (n.right == null) {
            return n;
        }
        Node current = n.right;
        Node parent = n.right;
        while (current != null) {
            parent = current;
            current = current.left;
        }
        return parent;
    }

    //添加操作
    /*
    	思路：若为空树，则添加的节点为根节点；
    	若不为空树，先找到添加位置：
    		若值小于父节点：左子树添加
    		否则：右子树添加
    */
    public void put(int key) {
        Node newNode = new Node(key);
        if (root == null) {
            root = newNode;
        } else {
            Node parent = find(key);
            if (key < parent.data) {
                parent.left = newNode;
                parent.left.parent = parent;
                return;
            } else {
                parent.right = newNode;
                parent.right.parent = parent;
                return;
            }
        }
    }

    //删除操作
    /*
    	思路：查找到被删除节点位置：如果节点不存在，返回false
    		1.被删除节点没有子节点
    			①若为根节点：直接删除
    			②若为右节点：删除右节点
    			③若为左节点：删除左节点
    		2.被删除节点有两个子节点
    			找到被删除节点的替换节点：一般方法为查找被删除节点右子树中的最小值节点
    			①处理被删除节点的左子树：赋值给替换节点
    			②处理被删除节点的右子树
    				若替换节点的父节点不是被删除节点
    				a.若替换节点有右子树
    				b.若替换节点无右子树
    			③处理被删除节点的父节点
    				a.若被删除节点为根结点:将替换节点赋值给root
    				b.若被删除节点不为根节点：
    					判断被删除节点在其父节点的哪一个子树中：左子树还是右子树
    		3.被删除节点有一个子节点(判断是左子节点还是右子节点)
    			①若被删除节点右子节点不为空
    				a.若为根节点，将被删除节点的右子节点赋给root
    				b.否则：
    					Ⅰ.处理父节点
    					Ⅱ.判断在被删除节点父节点的哪一个子树中：
    						若在左，则把其右子节点赋给其父节点的左子节点上
    						若在右，则把其右子节点赋给其父节点的右子节点上。
    			②若被删除节点的左子节点不为空，同上
    */
    public boolean remove(int key) {
        //查找到要删除节点的位置
        Node temp = find(key);
        //节点不存在
        if (temp.data != key) {
            return false;
        }
        //没有子节点
        if (temp.left == null && temp.right == null) {
            //判断是否为根结点
            if (temp == root) {
                root = null;
            } else if (temp.data > temp.parent.data) {//判断在左子树还是右子树
                temp.parent.right = null;
            } else {
                temp.parent.left = null;
            }
        } else if (temp.left != null && temp.right != null) {//双子节点
            Node successor = findSuccessor(temp);//找到替换节点
            //处理被替换节点左子树
            successor.left = temp.left;
            successor.left.parent = successor;
            //判断替换节点是否是被替换节点的右子树的根节点
            if (successor.parent != temp) {
                //如果替换节点有右子树
                if (successor.right != null) {
                    successor.right.parent = successor.parent;
                    successor.parent.left = successor.right;
                    successor.right = temp.right;
                    successor.right.parent = successor;
                } else {
                    successor.parent.left = null;
                    successor.right = temp.right;
                    successor.right.parent = successor;
                }
            }
            //处理被替换节点的父节点
            //判断被替换节点是否为根节点
            if (temp == root) {
                successor.parent = null;
                root = successor;
                return true;
            } else {
                successor.parent = temp.parent;
                if (temp.data > temp.parent.data) {
                    temp.parent.right = successor;
                } else {
                    temp.parent.left = successor;
                }
                return true;
            }
        } else {
            if (temp.right != null) {
                if (temp == root) {
                    root = temp.right;
                    return true;
                }
                temp.right.parent = temp.parent;
                if (temp.data < temp.parent.data) {
                    temp.parent.left = temp.right;
                } else {
                    temp.parent.right = temp.right;
                }
                return true;
            } else {
                if (temp == root) {
                    root = temp.left;
                    return true;
                }
                temp.left.parent = temp.parent;
                if (temp.data < temp.parent.data) {
                    temp.parent.left = temp.left;
                } else {
                    temp.parent.right = temp.left;
                }
                return true;
            }
        }
        return false;
    }

    //得到根节点
    public Node getRoot() {
        return root;
    }

    //遍历
    public void preOrder(Node localNode) {
        if (localNode != null) {
            System.out.print(localNode.data + " ");
            preOrder(localNode.left);
            preOrder(localNode.right);
        }
    }

    public void inOrder(Node localNode) {
        if (localNode != null) {
            inOrder(localNode.left);
            System.out.print(localNode.data + " ");
            inOrder(localNode.right);
        }
    }

    public void postOrder(Node localNode) {
        if (localNode != null) {
            postOrder(localNode.left);
            postOrder(localNode.right);
            System.out.print(localNode.data + " ");
        }
    }
}