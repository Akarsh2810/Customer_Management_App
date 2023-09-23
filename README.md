# Customer_Management_App
******* CUSTOMER MANAGEMENT APP: *********

-> In this project, I utilized JAVA for the main logic of implementing API calls and JSP for the front-end interface.
-> As a result of this project, I was able to develop the front-end user interface. It is a 3-page website in which 5 APIs have to be integrated.
-> The 5 APIs are customer authentication, creating a new customer, getting a customers' list, deleting a customer, and updating a customer.

========== Function: ===========

-> The user have to login to the website using emailID and password. Then click on login button.
-> Then the user will see a list of customers.
-> The user can add new customer, edit or delete an existing customer.
-> When the user clicks on add new customer button, the user will have to fill a form and when the user clicks on edit button, the user will have to edit a form and then click on save button.
-> The user will be able to see the new customer along with old customers.
-> When the user clicks on delete button, then that customer will be deleted from the list of customers.

========== Structure: ==========

-> This app contains 3 web pages of .jsp type, one customer model class, and one customermanagementservlet class.
-> The 3 webpages are login.jsp, customer-list.jsp, and customer-form.jsp
-> The login.jsp page contains a form that has two input fields and a submit button. The form action is login and form method is POST
-> The customer-list.jsp page contains a button at the top to add new customer and a table below it to display all the customers list. It also contains two buttons in each customer detail row to edit and delete that customer.
-> The customer-form.jsp page contains a form to add or edit a customer details. The form action depends on whether the customer has clicked on add button or edit button.
-> For add, edit and delete button the form action is create, edit and delete respectively.
-> The customer model class contains private data-members to ensure encapsulation, their getter-setter methods to access data-members, and constructor to initialize data-members.
-> The customer management servlet class contains main logic to handle any API calls. A servlet class has doPost method by default which handles the form action and further calls methods associated with that action. Each API calling method inside this class has two formal parameters called HttpServletRequest and HttpServletResponse.
-> The form action is received by the doPost method as a request.
-> This form action decides the API to be called using switch-case.

++++++++++  Working: ++++++++++++

-> When the customer management app loads up for the first time, login.jsp opens up.
-> The login.jsp page contains a form that has two input fields and a submit button. 
-> The form action is login and its method is POST. 
-> Among the two input fields, one is of type text and another is of type password. The customer has to fill up their loginID and password in the login form and click on submit button.
-> When submit button is clicked, if the loginID or password is incorrect or Http status code is not 200 then an errormessage method will be called that displays the error message on that web page else loginCustomer method which is associated with login form action is called.
-> This loginCustomer method further calls authenticate customer API to get the bearer token of the customer.
-> The bearer token received is now stored in the token reference variable. This token will be used for further API calls.
-> This loginCustomer method further calls showCustomers method which will further call getCustomer method and getCustomer method calls getCustomerList API and stores the customers list and returns the list back to the show customers method.
-> Then the customer is redirected to customer list.jsp web page and all the customer along with their details are shown on that page in the form of a table.
-> Now the customer can add new customer, edit any customers details or delete any number of customers.
-> When the customer clicks on add button in customer-list.jsp page, a create form action is called and inside doPost method, the method associated with create action is further called.
-> This createCustomer method redirects customer to customer-form.jsp page.
-> Now the customer fills up the details and clicks on save button then add form action is called and inside doPost method, the method associated with add form action is further called.
-> In this method all the received customer details are stored in a customer object and passed on to postCustomer method for addCustomer API call.
-> After that show customer method is called and the customer will be redirected to the customer-list.jsp page which now has new customer added along with old customers.
-> Now when the customer clicks on edit button in customer-list.jsp page, an edit form action is called and inside doPost method, the method associated with edit form action is further called.
-> Then store the uuid of the customer to filter out that customer whose details need to be edited.
-> Now the customer is redirected to customer-form.jsp page and the filtered out customer details are filled in that form by default.
-> When customer clicks on save button after editing then update form action is called and inside doPost method, the method associated with add form action is further called.
-> Then updateCustomer method stores the edited customer details and further calls putCustomer method which calls update Customer API.
-> After that show customer method is called and the customer will be redirected to the customer-list.jsp page which now has updated customer replaced by that old details along with old customers.
-> Now when the customer clicks on the delete button in customer-list.jsp page, an delete form action is called and inside doPost method, the method associated with delete form action is further called.
-> Then that customer UUID is stored and is passed as a URI to call delete API which deletes that customer from that customer list and redirects the customer to the customer-list.jsp page.
