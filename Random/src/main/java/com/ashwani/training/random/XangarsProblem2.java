package com.ashwani.training.random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author a.solanki
 *	Problem as given by Vaibhav from Xangars.
 *  V2
 */
public class XangarsProblem2 {

	/**
	 * Hard coding these values as no one has mentioned about feeding these from
	 * user.
	 */
	static int sizeOfGroup = 41;
	static int murderIndex = 3;

	public static void main(String[] args) {
		XangarsProblem2 objJosephus = new XangarsProblem2();

		List<Integer> lstRemainingPersons = objJosephus.getRemainingPerson(sizeOfGroup, murderIndex);

		Iterator<Integer> iter = lstRemainingPersons.iterator();
		while (iter.hasNext()) {
			System.err.println("This person survives " + iter.next());
		}
	}

	/**
	 * @param sizeOfGroup
	 * @param murderIndex
	 * @return a list containing remaining persons.
	 */
	public List<Integer> getRemainingPerson(int sizeOfGroup, int murderIndex) {
		int murderHim = 0;

		ArrayList<Integer> lstGroup = new ArrayList<Integer>(sizeOfGroup);
		for (int i = 0; i < sizeOfGroup; i++) {
			lstGroup.add(i);
		}
	
		/**
		 * Here we remove every third person from the list until no possible
		 * 		iteration possible.
		 */
		for (int i = lstGroup.size(); i > 1; i--) {
			murderHim = (murderHim + murderIndex - 1) % lstGroup.size();
			lstGroup.remove(murderHim);
		}

		return lstGroup;
	}

}