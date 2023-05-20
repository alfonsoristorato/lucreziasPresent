Feature: User Controller Features

  Scenario Outline: the client makes requests to the endpoint and is refused access if not an admin
    Given Client provides "valid" "user" authentication credentials
    Given The next request contains the following path parameters: "<pathParams>"
    When Client makes a "<verb>" request to "<endpoint>" endpoint
      | "" | "" |
    Then Client receives a status code of 500
    And The response has the following text: "Access Denied"
    Examples:
      | verb  | endpoint            | pathParams        |
      | GET   | user                |                   |
      | PATCH | user/edit-role      | userRoleChange    |
      | PATCH | user/edit-attempts  | userLocked        |
      | POST  | user                |                   |
      | PATCH | user/reset-password | userResetPassword |

  Scenario Outline: the client makes requests to the endpoint and is refused access if admin but doing their first login
    Given Client provides "valid" "adminFirstLogin" authentication credentials
    Given The next request contains the following path parameters: "<pathParams>"
    When Client makes a "<verb>" request to "<endpoint>" endpoint
      | "" | "" |
    Then Client receives a status code of 500
    And The response has the following text: "Access Denied"
    Examples:
      | verb  | endpoint            | pathParams        |
      | GET   | user                |                   |
      | PATCH | user/edit-role      | userRoleChange    |
      | PATCH | user/edit-attempts  | userLocked        |
      | POST  | user                |                   |
      | PATCH | user/reset-password | userResetPassword |

  Scenario: the client makes a GET request to the user endpoint with correct details and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    When Client makes a "GET" request to "user" endpoint
      | "" | "" |
    Then Client receives a status code of 200
    And The response contains a list of "users"
      | "" | "" |

  Scenario: the client makes a PATCH request to the user/edit-role endpoint with correct details and receives expected body and response code, and the user role changes
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userRoleChange |
      | password | defaultPass    |
    And The response has the following properties
      | username   | userRoleChange |
      | role       | user           |
      | attempts   | 0              |
      | firstLogin | false          |
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "userRoleChange"
    When Client makes a "PATCH" request to "user/edit-role" endpoint with the following body
      | newRole | admin |
    Then Client receives a status code of 200
    And The response has the following text: "User role updated."
    Given the headers are cleared
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userRoleChange |
      | password | defaultPass    |
    Then The response has the following properties
      | username   | userRoleChange |
      | role       | admin          |
      | attempts   | 0              |
      | firstLogin | false          |

  Scenario: the client makes a PATCH request to the user/edit-role endpoint with correct details to an user that does not exist and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "notAnUser"
    When Client makes a "PATCH" request to "user/edit-role" endpoint with the following body
      | newRole | admin |
    Then Client receives a status code of 404
    And The response has the following properties
      | description | User error      |
      | details     | User not found. |

  Scenario: the client makes a PATCH request to the user/edit-attempts endpoint with correct details and receives expected body and response code, and the user attempts change
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userLocked  |
      | password | defaultPass |
    And The response has the following properties
      | description | User error                                     |
      | details     | Account bloccato, contatta l'amministratore per sbloccarlo. |
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "userLocked"
    When Client makes a "PATCH" request to "user/edit-attempts" endpoint with the following body
      | newAttempts | 0 |
    Then Client receives a status code of 200
    And The response has the following text: "User attempts updated."
    Given the headers are cleared
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userLocked  |
      | password | defaultPass |
    Then The response has the following properties
      | username   | userLocked |
      | role       | user       |
      | attempts   | 0          |
      | firstLogin | false      |

  Scenario: the client makes a PATCH request to the user/edit-attempts endpoint with correct details to the user 'admin' with newAttempts equal to 0 and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "admin"
    When Client makes a "PATCH" request to "user/edit-attempts" endpoint with the following body
      | newAttempts | 0 |
    Then Client receives a status code of 200
    And The response has the following text: "User attempts updated."

  Scenario: the client makes a PATCH request to the user/edit-attempts endpoint with correct details to an user that does not exist and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "notAnUser"
    When Client makes a "PATCH" request to "user/edit-attempts" endpoint with the following body
      | newAttempts | 0 |
    Then Client receives a status code of 404
    And The response has the following properties
      | description | User error      |
      | details     | User not found. |

  Scenario: the client makes a POST request to the user endpoint with correct details and receives expected body and response code, it creates an user with firstLogin as 'true' and role as 'user'
    Given Client provides "valid" "admin" authentication credentials
    When Client makes a "POST" request to "user" endpoint with the following body
      | username | newUser |
    Then Client receives a status code of 200
    And The response contains a new password with a total length higher than 8
    When Client makes a "GET" request to "user" endpoint
      | "" | "" |
    Then The response contains a list of "users" including the user below
      | username   | newUser |
      | role       | utente  |
      | attempts   | 0       |
      | firstLogin | true    |

  Scenario: the client makes a POST request to the user endpoint with correct details to an user that already exists and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    When Client makes a "POST" request to "user" endpoint with the following body
      | username | user |
    Then Client receives a status code of 500
    And The response has the following properties
      | description | User error           |
      | details     | User already exists. |

  Scenario: the client makes a PATCH request to the user/reset-password endpoint with correct details and receives expected body and response code, and the user 'firstLogin' is set to true and their password changes
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userResetPassword |
      | password | defaultPass       |
    And The response has the following properties
      | username   | userResetPassword |
      | role       | user              |
      | attempts   | 0                 |
      | firstLogin | false             |
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "userResetPassword"
    When Client makes a "PATCH" request to "user/reset-password" endpoint
      | "" | "" |
    Then Client receives a status code of 200
    And The response contains a new password with a total length higher than 8
    When Client makes a "GET" request to "user" endpoint
      | "" | "" |
    Then The response contains a list of "users" including the user below
      | username   | userResetPassword |
      | role       | user              |
      | attempts   | 0                 |
      | firstLogin | true              |
    Given the headers are cleared
    When Client makes a "POST" request to "login" endpoint with the following body
      | username | userResetPassword |
      | password | defaultPass       |
    And The response has the following properties
      | description | User error                    |
      | details     | Credenziali non riconosciute. |

  Scenario: the client makes a PATCH request to the user/reset-password endpoint with an user that does not exists and receives expected body and response code
    Given Client provides "valid" "admin" authentication credentials
    Given The next request contains the following path parameters: "notAnUser"
    When Client makes a "PATCH" request to "user/reset-password" endpoint
      | "" | "" |
    Then Client receives a status code of 404
    And The response has the following properties
      | description | User error      |
      | details     | User not found. |


