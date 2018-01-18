package com.ashwani.training.random;

public class Employee implements Comparable<Employee> {

	private Long id;
	private String name;

	public Employee(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Employee other = (Employee) obj;
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId()) || getName().equalsIgnoreCase(other.getName())) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Employee o) {
		if (o.getId() == this.getId()) {
			if (o	.getName()
					.equalsIgnoreCase(this.getName())) {
				return 0;
			}
			return -1;
		}
		return 0;
	}
}
