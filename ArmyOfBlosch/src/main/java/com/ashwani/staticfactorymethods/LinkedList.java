package com.ashwani.staticfactorymethods;

public class LinkedList {

	Node firstNode;

	/**
	 * Static factory method to return an instance of linkedlist.
	 * 
	 * @param firstNode
	 */
	public static LinkedList makeOne(Node firstNode) {
		return new LinkedList(firstNode);
	}

	public LinkedList(Node node) {
		this.firstNode = node;
	}

	public void readAllValues() {
		if (this.firstNode != null) {
			Node tempNode = this.firstNode;
			while (tempNode.nextNode != null) {
				System.out.println(tempNode.getValue());
				tempNode = tempNode.getNextNode();
			}
			System.out.println(tempNode.getValue());
		}
	}

	public Node popNode(final Node nodeToPop) {
		return null;
	}
	
	static class Node {
		private int value;
		private Node nextNode;

		public Node(int value) {
			this.value = value;
		}

		public void setNextNode(Node nextNode) {
			this.nextNode = nextNode;
		}

		public Node getNextNode() {
			return this.nextNode;
		}

		public int getValue() {
			return this.value;
		}
	}
	
}
