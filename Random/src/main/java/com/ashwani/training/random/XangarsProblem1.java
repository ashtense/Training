
package com.ashwani.training.random;
import java.util.ArrayList;
import java.util.List;

/**
 * @author a.solanki
 * Problem as given by Vaibhav from Xangars.
 */
public class XangarsProblem1 {

	/**
	 * Hard coding these values as no one has mentioned 
	 * 		about feeding these from user. 
	 */
	int sizeOfGroup = 41;
	int murderIndex = 3;

	public static void main(String[] args) {
		XangarsProblem1 problem = new XangarsProblem1();
		for(Integer murderedPerson : problem.getSafePersonsList()){
			System.err.print(murderedPerson + " ");
		}
	}

	private List<Integer> getSafePersonsList(){
		List<Integer> lstSafePersons = new ArrayList<>();
		for (int index = 1; index <= sizeOfGroup; index++) {
			if (index % murderIndex == 0) {
				//This person dies as %3 will get murdered.
			} else {
				//These persons won't be murdered
				lstSafePersons.add(index);
			}
		}
		return lstSafePersons;
	}
	
	class DoublyLinkedList {

		private Node firstNode;
		private int count;
		
		void addNode(int data){
			count++;
			if(firstNode != null){
				firstNode = new Node(data);
			}else{
				Node existingNode = firstNode;
				while(existingNode.next != firstNode){
					existingNode = existingNode.next;
				}
				existingNode.next = new Node(data);
			}
		}
		
		class Node {
			
			private int data;
			private Node next;

			public Node(int data) {
				this.data = data;
			}
		}
	}
}
