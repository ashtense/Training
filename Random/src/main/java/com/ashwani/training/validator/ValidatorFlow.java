package com.ashwani.training.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ValidatorFlow {

	static Activity activity1 = new Activity();
	static Activity activity2 = new Activity();

	static {
		activity1.setId(456L);
		activity1.setAddress("Hel");
		activity1.setName("Ashwani Geronimo");

		activity1.setId(2L);
		activity1.setAddress("Helaghaklga;lgj;ajg;akgakglljlajgag");
		activity1.setName("LOL");
	}

	public static void main(String[] args) {
		final List<Predicate<Activity>> lstValidators = new ArrayList<>();
		addValidators(activity1, lstValidators);
		lstValidators.forEach(validator -> {
			System.out.println(validator.test(activity1));
		});

		lstValidators.clear();
		addValidators(activity2, lstValidators);
		lstValidators.forEach(validator -> {
			System.out.println(validator.test(activity2));
		});

		System.err.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	}

	private static void addValidators(Activity incomingActivity, List<Predicate<Activity>> lstValidators) {
		lstValidators.add(new NullValidator());
		if (incomingActivity.getName() != null) {
			lstValidators.add(new NameValidator());
		}
		if (incomingActivity.getAddress() != null) {
			lstValidators.add(new SizeValidator());
		}
	}

}

class NullValidator implements Predicate<Activity> {

	@Override
	public boolean test(Activity t) {
		if (t == null) {
			throw new NullPointerException();
		}
		return Boolean.TRUE;
	}

}

class NameValidator implements Predicate<Activity> {

	@Override
	public boolean test(Activity t) {
		return t.getName().contains("Geronimo");
	}

}

class SizeValidator implements Predicate<Activity> {

	@Override
	public boolean test(Activity t) {
		return t.getAddress().length() > 10;
	}

}