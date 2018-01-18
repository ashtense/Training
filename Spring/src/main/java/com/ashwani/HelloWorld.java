package com.ashwani;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author ASHWANI
 * Implementing the InitializingBean iterface gives us afterPropertiesSet() in which 
 * 		you can do things just after initialization by container reading from metadata file.
 * In case of xml defined metadata, the meta defines a method like init()/initGeronimo() :D.
 * 		Maybe we shouldn't use both init types in one bean but this is for demo purpose only.
 */
public class HelloWorld implements InitializingBean, DisposableBean{

	private String message;
	private int id;

	public void getMessage() {
		System.out.println("Your amazing message is " + this.message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void afterPropertiesSet() throws Exception {
		//called just at initialization time
		this.setId(45);
	}
	
	public void initGeronimo(){
		System.out.println("This is funny... isn't it? : called just at initialization time");
	}

	public void destroy() throws Exception {
		// called after the destruction of object from scope.... we will check it for both singleton and protoype beans
		System.err.println("Iam called only after the destruction of bean by container!!!");
	}
}
