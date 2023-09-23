package customer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import customer.model.Customer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@jakarta.servlet.annotation.WebServlet("/")
public class CustomerManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String baseUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=%s";
	private String token=null;
	
	String asJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		
		System.out.println("user action " + action);
		
		switch (action) {
		case "/login":
			loginCustomer(request, response);
			break;
		case "/create":
			createCustomer(request, response);
			break;
		case "/add":
			addCustomer(request, response);
			break;
		case "/delete":
			deleteCustomer(request, response);
			break;
		case "/edit":
			editCustomer(request, response);
			break;
		case "/update":
			updateCustomer(request, response);
			break;
		default:
			redirect(request, response);
			break;
		}
	}
	
	private void loginCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginId = request.getParameter("login_id");
		String password = request.getParameter("password");
		
		HttpResponse loginResp = authenticateCustomer(loginId, password);
		
		if(loginResp != null) {
			if(loginResp.statusCode() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				System.out.println("login resp class "+loginResp.body().getClass());
				Map<String,Object> tokenMap = mapper.readValue(loginResp.body().toString(), HashMap.class);
				this.token=(String)tokenMap.get("access_token");
				System.out.println("token map "+tokenMap);
				showCustomers(request, response);
			} else {
				errorMessage("login.jsp", "Incorrect credentials! Try again.", request, response);
			}
		}
	}
	
	private void errorMessage(String dispatchPage, String message, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println(message);
		RequestDispatcher rd = request.getRequestDispatcher(dispatchPage);
		rd.include(request, response);
	}
	
	private HttpResponse authenticateCustomer(String loginId, String password) {
		Map<String, String> map = new HashMap<>();
		map.put("login_id", loginId);
		map.put("password", password);
		HttpRequest loginUser = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(asJsonString(map))).
				uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp")).build();
		HttpClient client = HttpClient.newBuilder().build();
		
		try {
			HttpResponse responseData = client.send(loginUser, HttpResponse.BodyHandlers.ofString());
			return responseData;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void showCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Customer> customers = getCustomers();
		request.setAttribute("customers", customers);
		RequestDispatcher rd = request.getRequestDispatcher("customer-list.jsp");
		rd.forward(request, response);
	}
	
	private List<Customer> getCustomers() {
		System.out.println( " this token "+this.token);
		List<Customer> customers = new ArrayList<>();
		HttpRequest getCustomerList = HttpRequest.newBuilder().GET().uri(URI.create(String.format(baseUrl, "get_customer_list"))).header("Authorization", "Bearer " + this.token).build();
		HttpClient client = HttpClient.newBuilder().build();
		
		try {
			HttpResponse responseData = client.send(getCustomerList, HttpResponse.BodyHandlers.ofString());
//			System.out.println("status code " + responseData.statusCode());
//			System.out.println("body " + responseData.body());
			ObjectMapper mapper = new ObjectMapper();
			List<Object> customerObjs = mapper.readValue(responseData.body().toString(), List.class);
			for(Object o : customerObjs) {
				Customer customer = mapper.convertValue(o, Customer.class);
				customers.add(customer);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return customers;
	}
	
	private void createCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start createCustomer");
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer-form.jsp");
		dispatcher.forward(request, response);
	}
	
	private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start addCustomer");
		Customer obj = new Customer(request.getParameter("first_name"),request.getParameter("last_name"),
				request.getParameter("street"),request.getParameter("address"),
				request.getParameter("city"),request.getParameter("state"),
				request.getParameter("email"),request.getParameter("phone"));
		HttpResponse responseBody = postCustomer(obj);
		if(responseBody.statusCode() == 400) {
			errorMessage("customer-form.jsp", "First Name or Last Name is missing", request, response);
		}
		else {
			showCustomers(request, response);
		}
	}
	
	private HttpResponse postCustomer(Customer customer) {
		HttpRequest postRequest = HttpRequest.newBuilder().
				POST(HttpRequest.BodyPublishers.ofString(asJsonString(customer))).
				uri(URI.create(String.format(baseUrl, "create"))).header("Authorization", "Bearer " + this.token).build();
		HttpClient client = HttpClient.newBuilder().build();
		
		try {
			HttpResponse responseData = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
			return responseData;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void editCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start editCustomer");
		String uuid = request.getParameter("uuid");
		System.out.println("input uuid "+uuid);
		List<Customer> customers = getCustomers();
		Customer editCustomer = null;
		for(Customer c : customers) {
			System.out.println("c uuid "+c.getUuid());
			if(c.getUuid().equals(uuid)) {
				System.out.println("matched");
				editCustomer = c;
				break;
			}
		}
		RequestDispatcher dispatcher;
		if(editCustomer != null) {
			request.setAttribute("customer", editCustomer);
			dispatcher = request.getRequestDispatcher("customer-form.jsp");
		}
		else {
			dispatcher = request.getRequestDispatcher("login.jsp");
		}
		dispatcher.forward(request, response);
	}
	
	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start updateCustomer");
		Customer obj = new Customer(request.getParameter("first_name"),request.getParameter("last_name"),
				request.getParameter("street"),request.getParameter("address"),
				request.getParameter("city"),request.getParameter("state"),
				request.getParameter("email"),request.getParameter("phone"));
		HttpResponse responseData = putCustomer(obj, request.getParameter("uuid"),(String)request.getAttribute("token"));
		if(responseData.statusCode() == 401) {
			errorMessage("login.jsp", "Invalid authorization", request, response);
		}
		else if(responseData.statusCode() == 500) {
			errorMessage("customer-list.jsp", "UUID not found", request, response);
		}
		else if(responseData.statusCode() == 400) {
			errorMessage("customer-list.jsp", "Body is empty", request, response);
		}
		else {
			showCustomers(request,response);
		}
	}
	
	private HttpResponse putCustomer(Customer customer, String uuid,String token) {
		System.out.println("start putCustomer");
		HttpRequest putRequest = HttpRequest.newBuilder().
				POST(HttpRequest.BodyPublishers.ofString(asJsonString(customer))).
				uri(URI.create(String.format(baseUrl, "update&uuid=" + uuid))).header("Authorization", "Bearer " + this.token).build();
		HttpClient client = HttpClient.newBuilder().build();
		
		try {
			HttpResponse responseData = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
			return responseData;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start deleteCustomer");
		String uuid = request.getParameter("uuid");
		System.out.println("start deleteCustomer uuid "+uuid);
		HttpRequest deleteRequest = HttpRequest.newBuilder().
				POST(HttpRequest.BodyPublishers.ofString(asJsonString(new HashMap()))).
				uri(URI.create(String.format(baseUrl, "delete&uuid=" + uuid))).header("Authorization", "Bearer " + this.token).build();
		HttpClient client = HttpClient.newBuilder().build();
		
		try {
			HttpResponse responseData = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
			if(responseData.statusCode() == 401) {
				errorMessage("login.jsp", "Invalid authorization", request, response);
			}
			else if(responseData.statusCode() == 500) {
				errorMessage("customer-list.jsp", "Error not deleted", request, response);
			}
			else if(responseData.statusCode() == 400) {
				errorMessage("customer-list.jsp", "UUID not found", request, response);
			}
			else {
				showCustomers(request,response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void redirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("start redirect");
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
}