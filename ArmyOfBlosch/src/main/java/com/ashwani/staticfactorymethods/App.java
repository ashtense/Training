package com.ashwani.staticfactorymethods;

import com.ashwani.staticfactorymethods.LinkedList.Node;

public class App {

	public static void main(String[] args) {
		Node firstNode = new Node(1);
		Node secondNode = new Node(2);
		firstNode.setNextNode(secondNode);
		secondNode.setNextNode(new Node(3));
		
		LinkedList linkedList = LinkedList.makeOne(firstNode);
		linkedList.readAllValues();
	}
}
