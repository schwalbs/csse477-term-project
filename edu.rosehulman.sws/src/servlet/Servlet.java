package servlet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import protocol.HttpRequest;
import protocol.response.HttpResponse;

public interface Servlet {
	public void process(HttpRequest request, HttpResponse response);
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ServletDetails{
		public String method();
		public String[] urlPatterns();
	}
}
