# Customer_Management_App
******* CUSTOMER MANAGEMENT APP: *********

1. In this project, I utilized JAVA for the main logic of implementing API calls and JSP for the front-end interface.
2. As a result of this project, I was able to develop the front-end user interface. It is a 3-page website in which 5 APIs have to be integrated.
3. The 5 APIs are customer authentication, creating a new customer, getting a customers' list, deleting a customer, and updating a customer.

========== Function: ===========

1. The user have to login to the website using emailID and password. Then click on login button.
2. Then the user will see a list of customers.
3. The user can add new customer, edit or delete an existing customer.
4. When the user clicks on add new customer button, the user will have to fill a form and when the user clicks on edit button, the user will have to edit a form and then click on save button.
5. The user will be able to see the new customer along with old customers.
6. When the user clicks on delete button, then that customer will be deleted from the list of customers.

========== Structure: ==========

1. This app contains 3 web pages of .jsp type, one customer model class, and one customermanagementservlet class.
2. The 3 webpages are login.jsp, customer-list.jsp, and customer-form.jsp
3. The login.jsp page contains a form that has two input fields and a submit button. The form action is login and form method is POST
4. The customer-list.jsp page contains a button at the top to add new customer and a table below it to display all the customers list. It also contains two buttons in each customer detail row to edit and delete that customer.
5. The customer-form.jsp page contains a form to add or edit a customer details. The form action depends on whether the customer has clicked on add button or edit button.
6. For add, edit and delete button the form action is create, edit and delete respectively.
7. The customer model class contains private data-members to ensure encapsulation, their getter-setter methods to access data-members, and constructor to initialize data-members.
8. The customer management servlet class contains main logic to handle any API calls. A servlet class has doPost method by default which handles the form action and further calls methods associated with that action. Each API calling method inside this class has two formal parameters called HttpServletRequest and HttpServletResponse.
9. The form action is received by the doPost method as a request.
10. This form action decides the API to be called using switch-case.

++++++++++  Working: ++++++++++++

1. When the customer management app loads up for the first time, login.jsp opens up.
2. The login.jsp page contains a form that has two input fields and a submit button.
3. The form action is login and its method is POST.
4. Among the two input fields, one is of type text and another is of type password. The customer has to fill up their loginID and password in the login form and click on submit button.
5. When submit button is clicked, if the loginID or password is incorrect or Http status code is not 200 then an errormessage method will be called that displays the error message on that web page else loginCustomer method which is associated with login form action is called.
6. This loginCustomer method further calls authenticate customer API to get the bearer token of the customer.
7. The bearer token received is now stored in the token reference variable. This token will be used for further API calls.
8. This loginCustomer method further calls showCustomers method which will further call getCustomer method and getCustomer method calls getCustomerList API and stores the customers list and returns the list back to the show customers method.
9. Then the customer is redirected to customer list.jsp web page and all the customer along with their details are shown on that page in the form of a table.
10. Now the customer can add new customer, edit any customers details or delete any number of customers.
11. When the customer clicks on add button in customer-list.jsp page, a create form action is called and inside doPost method, the method associated with create action is further called.
12. This createCustomer method redirects customer to customer-form.jsp page.
13. Now the customer fills up the details and clicks on save button then add form action is called and inside doPost method, the method associated with add form action is further called.
14. In this method all the received customer details are stored in a customer object and passed on to postCustomer method for addCustomer API call.
15. After that show customer method is called and the customer will be redirected to the customer-list.jsp page which now has new customer added along with old customers.
16. Now when the customer clicks on edit button in customer-list.jsp page, an edit form action is called and inside doPost method, the method associated with edit form action is further called.
17. Then store the uuid of the customer to filter out that customer whose details need to be edited.
18. Now the customer is redirected to customer-form.jsp page and the filtered out customer details are filled in that form by default.
19. When customer clicks on save button after editing then update form action is called and inside doPost method, the method associated with add form action is further called.
20. Then updateCustomer method stores the edited customer details and further calls putCustomer method which calls update Customer API.
21. After that show customer method is called and the customer will be redirected to the customer-list.jsp page which now has updated customer replaced by that old details along with old customers.
22. Now when the customer clicks on the delete button in customer-list.jsp page, an delete form action is called and inside doPost method, the method associated with delete form action is further called.
23. Then that customer UUID is stored and is passed as a URI to call delete API which deletes that customer from that customer list and redirects the customer to the customer-list.jsp page.
