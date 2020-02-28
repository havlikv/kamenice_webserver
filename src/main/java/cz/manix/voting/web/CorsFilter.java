package cz.manix.voting.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CorsFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}



	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		if ("OPTIONS".equals(httpRequest.getMethod()))
		{
			httpResponse.setStatus(HttpServletResponse.SC_OK);

			return;
		}

		chain.doFilter(request, response);
	}



	@Override
	public void destroy()
	{

	}

}
